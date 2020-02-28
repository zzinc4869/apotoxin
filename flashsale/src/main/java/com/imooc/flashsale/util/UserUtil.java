package com.imooc.flashsale.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.flashsale.domain.User;

/**
 * 生成用户插入数据库并将token写入文件以供测试
 * 
 * @author ZZinc
 *
 */
public class UserUtil {

	private static final int IDBEGIN = 10;

	private static void createUser(int count) throws Exception {
		List<User> userList = new ArrayList<User>(count);
		// 生成用户
		for (int i = 0; i < count; i++) {
			User user = new User();
			user.setMobile(13000000000L + i);
			user.setLoginCount(1);
			user.setNickname("user" + (i + 1));
			user.setRegisterDate(new Date());
			user.setSalt(MD5Util.getRandomSalt());
			user.setPassword(MD5Util.inputPassToDBPass("qweasd", user.getSalt()));
			user.setId(IDBEGIN + i);
			userList.add(user);
		}
		System.err.println("create user done");
		// 插入数据库
		Connection conn = DBUtil.getConn();
		String sql = "insert into tb_user(login_count, nickname, register_date, salt, password, mobile, id)values(?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			pstmt.setInt(1, user.getLoginCount());
			pstmt.setString(2, user.getNickname());
			pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
			pstmt.setString(4, user.getSalt());
			pstmt.setString(5, user.getPassword());
			pstmt.setLong(6, user.getMobile());
			pstmt.setInt(7, user.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		conn.close();
		System.err.println("insert to db done");
		// 登录，生成token
		String urlString = "http://localhost:8080/user/do_login";
		File file = new File("E:/tokens.txt");
		if (file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		file.createNewFile();
		raf.seek(0);
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			URL url = new URL(urlString);
			HttpURLConnection co = (HttpURLConnection) url.openConnection();
			co.setRequestMethod("POST");
			co.setDoOutput(true);
			OutputStream out = co.getOutputStream();
			String params = "mobile=" + user.getMobile() + "&password=" + MD5Util.inputPassToFormPass("qweasd");
			out.write(params.getBytes());
			out.flush();
			InputStream inputStream = co.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0, len);
			}
			inputStream.close();
			bout.close();
			String response = new String(bout.toByteArray());
			JSONObject jo = JSON.parseObject(response);
			String token = jo.getString("data");
			System.err.println("create token : " + user.getId());

			String row = user.getId() + "," + token;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\r\n".getBytes());
			System.err.println("write to file : " + user.getMobile());
		}
		raf.close();

		System.err.println("over");
	}

	public static void main(String[] args) throws Exception {
		createUser(2020);
	}
}
