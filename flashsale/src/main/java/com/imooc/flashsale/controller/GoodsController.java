package com.imooc.flashsale.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.alibaba.druid.util.StringUtils;
import com.imooc.flashsale.access.AccessLimit;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.GoodsKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.IGoodsService;
import com.imooc.flashsale.vo.GoodsDetailVo;
import com.imooc.flashsale.vo.GoodsVo;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;

	/**
	 * 压测第一次：10*2000并发，QPS：561 
	 * 压测第二次：10*2000并发，QPS：1022
	 */
	@AccessLimit(needLogin = true)
	@RequestMapping(value = "/to_list", produces = "text/html")
	public String toGoodsList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
		model.addAttribute("user", user);

		// 取页面缓存
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}

		// 查询商品列表
		List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
		model.addAttribute("goodsList", goodsVoList);

		// 手动渲染
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),
				model.asMap());
		// 进行渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		// 如果不为空，保存缓存
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
	}

	/**
	 * 通过对象缓存以及URL缓存来访问
	 */
	@RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
	public String toGoodsDetail(HttpServletRequest request, HttpServletResponse response, Model model, User user,
			@PathVariable("goodsId") long goodsId) {
		// 数据库中的ID一般不使用自增id，可使用snowflake解决
		model.addAttribute("user", user);

		// 取缓存
		String html = redisService.get(GoodsKey.getGoodsList, "" + goodsId, String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}

		// 查询商品详情
		GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
		model.addAttribute("goods", goodsVo);

		long startTime = goodsVo.getStartDate().getTime();
		long endTime = goodsVo.getEndDate().getTime();
		long nowTime = System.currentTimeMillis();
		// System.out.println("nowTime=" + nowTime + "-startTime=" + startTime +
		// "-endTime=" + endTime);
		// 秒杀状态
		int saleStatus = 0;
		// 距离开始剩余时间
		int remainSeconds = 0;

		if (nowTime < startTime) {
			// 秒杀还未开始
			saleStatus = 0;
			remainSeconds = (int) ((startTime - nowTime) / 1000);
			// System.out.println("秒杀还未开始，时间差=" + (startTime - nowTime));
		} else if (nowTime > endTime) {
			// 秒杀结束
			saleStatus = 2;
			remainSeconds = -1;
			// System.out.println("秒杀结束");
		} else {
			// 秒杀正在进行中
			saleStatus = 1;
			remainSeconds = 0;
			// System.out.println("秒杀进行中");
		}
		model.addAttribute("saleStatus", saleStatus);
		model.addAttribute("remainSeconds", remainSeconds);

		// 手动渲染
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),
				model.asMap());
		// 进行渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
		// 如果不为空，保存缓存
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
		}
		return html;
	}

	/**
	 * 重写toGoodsDetail
	 */
	@RequestMapping(value = "/detail/{goodsId}")
	public Result<GoodsDetailVo> toGoodsDetailPage(User user, @PathVariable("goodsId") long goodsId) {

		// 查询商品详情
		GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);

		long startTime = goodsVo.getStartDate().getTime();
		long endTime = goodsVo.getEndDate().getTime();
		long nowTime = System.currentTimeMillis();
		// 秒杀状态
		int saleStatus = 0;
		// 距离开始剩余时间
		int remainSeconds = 0;

		if (nowTime < startTime) {
			// 秒杀还未开始
			saleStatus = 0;
			remainSeconds = (int) ((startTime - nowTime) / 1000);
		} else if (nowTime > endTime) {
			// 秒杀结束
			saleStatus = 2;
			remainSeconds = -1;
		} else {
			// 秒杀正在进行中
			saleStatus = 1;
			remainSeconds = 0;
		}

		GoodsDetailVo vo = new GoodsDetailVo();
		vo.setUser(user);
		vo.setGoodsVo(goodsVo);
		vo.setRemainSeconds(remainSeconds);
		vo.setSaleStatus(saleStatus);

		return Result.success(vo);
	}
}
