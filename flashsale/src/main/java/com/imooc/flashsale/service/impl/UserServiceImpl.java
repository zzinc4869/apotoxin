package com.imooc.flashsale.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.imooc.flashsale.dao.UserMapper;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.domain.UserExample;
import com.imooc.flashsale.domain.UserExample.Criteria;
import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.redis.UserKey;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.service.IUserService;
import com.imooc.flashsale.util.MD5Util;
import com.imooc.flashsale.util.UUIDUtil;
import com.imooc.flashsale.vo.LoginVo;
import com.imooc.flashsale.vo.RegisterVo;

@Service
public class UserServiceImpl implements IUserService {

	/**
	 * 解决问题：分布式session。 问题描述：对于分布式的集群环境，如果登陆之后存储session的服务器与下一次请求提交的服务器不是同一台服务器，
	 * 则可能需要在服务器之间同步session。对于大量集群，同步的效率必将降低。因此需要解决分布式session的问题。
	 * 本项目解决方案：用户登录后生成一个标识用户的token，将token作为key存入redis，并将该token存入cookie中，将cookie存放至客户端。
	 * 当下一次页面向服务器发起请求时，同时上传自己的cookie，服务器从cookie中获取token，并从redis中得到相应的value返回给页面。
	 */

	@Autowired
	private UserMapper userDao;

	@Autowired
	private RedisService redisService;

	/**
	 * 对象缓存
	 */
	public User getById(int id) {
		// 取缓存
		User user = redisService.get(UserKey.getById, "" + id, User.class);
		if (user != null) {
			return user;
		}
		// 取数据库
		user = userDao.selectByPrimaryKey(id);
		// 保存缓存
		redisService.set(UserKey.getById, "" + id, user);
		return user;
	}

	public boolean updatePassword(String token, int id, String password) {
		// 取user
		User user = getById(id);
		if (user == null) {
			throw new GlobalException(CodeMsg.NULL_USER);
		}
		// 更新user
		User toBeUpdate = new User();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
		int effectNums = userDao.updateByPrimaryKeySelective(toBeUpdate);
		if (effectNums <= 0) {
			throw new GlobalException(CodeMsg.FAIL_UPDATE);
		}
		// 处理更新缓存
		redisService.delete(UserKey.getById, "" + id);
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(UserKey.token, "" + id, user);
		return true;
	}

	@Override
	public User getByToken(HttpServletResponse response, String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		// 从redis中获取session对象
		User user = redisService.get(UserKey.token, token, User.class);
		// 延长有效期
		if (user != null) {
			addCookie(response, token, user);
		}
		return user;
	}

	private void addCookie(HttpServletResponse response, String token, User user) {
		// 放入redis缓存，可由分布式的其它服务器获取
		redisService.set(UserKey.token, token, user);
		// 生成cookie存放token
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.getExpireSeconds());
		cookie.setPath("/");
		// 添加cookie至客户端
		response.addCookie(cookie);
	}

	@Override
	public int register(RegisterVo registerVo) {
		if (registerVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		Long mobile = registerVo.getMobile();
		// 判断手机号是否存在
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(mobile);
		List<User> userList = userDao.selectByExample(example);

		User user = new User();
		// 不存在的话开始注册
		if (userList == null || userList.size() == 0) {
			// 生成真正的密码
			String salt = MD5Util.getRandomSalt();
			String formPass = registerVo.getPassword();
			String realDbPass = MD5Util.formPassToDBPass(formPass, salt);
			// 构造实体对象
			user.setNickname(registerVo.getNickname());
			user.setMobile(registerVo.getMobile());
			user.setSalt(salt);
			user.setPassword(realDbPass);
			user.setLoginCount(0);
			user.setRegisterDate(new Date());
			// 插入数据库
			int effectedNum = userDao.insert(user);
			if (effectedNum <= 0) {
				throw new GlobalException(CodeMsg.SERVER_ERROR);
			}
			// System.out.println("用户注册：" + user);
		} else {
			// 存在的话提醒已经注册
			throw new GlobalException(CodeMsg.MOBILE_EXIST);
		}
		return user.getId();
	}

	@Transactional
	public String login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		Long mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		// 判断手机号是否存在
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(mobile);
		List<User> userList = userDao.selectByExample(example);
		if (userList == null || userList.size() == 0) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}

		// 验证密码
		User user = userList.get(0);
		// 真实的两次MD5加密密码
		String realDbPass = user.getPassword();
		// System.out.println("用户登录：" + user);
		// 实际的密码
		String actualDbPass = MD5Util.formPassToDBPass(formPass, user.getSalt());
		if (!realDbPass.equals(actualDbPass)) {
			// 密码错误
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}

		// 登陆成功，更新登录时间与登陆次数
		user.setLastLoginDate(new Date());
		user.setLoginCount(user.getLoginCount() + 1);
		int effectedNum = userDao.updateByPrimaryKey(user);
		if (effectedNum <= 0) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}

		// 生成一个标识用户的token
		String token = UUIDUtil.getRandomUUID();
		// 添加Cookie
		addCookie(response, token, user);

		// 返回token
		return token;
	}

}
