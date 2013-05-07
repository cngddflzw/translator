package org.uestc.translator.core;

import static org.uestc.translator.core.Data.PASSWD_TAG;
import static org.uestc.translator.core.Data.RESULT_SUCC;
import static org.uestc.translator.core.Data.USERNAME_TAG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;

public class RemoteDatabase {
	/**
	 * 获取远程最近查询单词
	 * @return
	 */
	public static Set<String> getHistory(String username) {
		String resultStr = "";
		try {
			String urlStr = "http://uestctranslator.appspot.com/dlhiswords";
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);	// 可以发送数据
			conn.setDoInput(true);	// 可以接收数据
			conn.setRequestMethod("POST");	// POST方法
			conn.setRequestProperty
			("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();
			//	写入的POST数据
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(USERNAME_TAG + "=" + username);
			osw.flush();
			osw.close();
			// 读取响应数据
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String s;
			StringBuilder result = new StringBuilder("");
			while ((s = in.readLine()) != null)
				result.append(s);
			resultStr = result.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (resultStr != null && !resultStr.equals(""))
			return new LinkedHashSet<String>(Arrays.asList(resultStr.split("|")));
		else
			return new LinkedHashSet<String>();
	}
	
	/**
	 * 添加远程最近查询单词
	 * @param text
	 * @return
	 */
	public static int addHistory(String text) {
		return 1;
	}
	
	/**
	 * 获取远程生词本
	 * @return
	 */
	public static Set<String> getNewWords(String username) {
		String resultStr = "";
		try {
			String urlStr = "http://uestctranslator.appspot.com/dlnewwords";
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);	// 可以发送数据
			conn.setDoInput(true);	// 可以接收数据
			conn.setRequestMethod("POST");	// POST方法
			conn.setRequestProperty
			("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();
			//	写入的POST数据
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(USERNAME_TAG + "=" + username);
			osw.flush();
			osw.close();
			// 读取响应数据
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String s;
			StringBuilder result = new StringBuilder("");
			while ((s = in.readLine()) != null)
				result.append(s);
			resultStr = result.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (resultStr != null && !resultStr.equals(""))
			return new LinkedHashSet<String>(Arrays.asList(resultStr.split("|")));
		else
			return new LinkedHashSet<String>();
	}
	
	/**
	 * 添加远程生词
	 * @param text
	 * @return
	 */
	public static int addNewWord(String text) {
		return 1;
	}
	
	/**
	 * 根据可变条件查询用户表
	 * @param conditions
	 * @return
	 */
	public static String queryUser(HashMap<String, String> conditions) {
		return null;
	}
}
