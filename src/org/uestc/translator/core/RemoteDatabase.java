package org.uestc.translator.core;

import static org.uestc.translator.core.Data.USERNAME_TAG;
import static org.uestc.translator.core.Data.HISWORD_TAG;
import static org.uestc.translator.core.Data.NEWWORD_TAG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.uestc.translator.AppContext;

import android.os.Handler;
import android.util.Log;

public class RemoteDatabase {
	/**
	 * 获取远程最近查询单词
	 * @return
	 */
	public static void getHistory(AppContext ac) {
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
			osw.write(USERNAME_TAG + "=" + ac.getUsername());
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
		Log.w("下载到的历史单词", resultStr + "!");
		Log.w("下载前ac的历史单词", ac.getHistorySet().toString());
		if (resultStr != null && !resultStr.equals(""))
			ac.setHistorySet(new LinkedHashSet<String>(
					Arrays.asList(resultStr.split("\\|"))));
		else
			ac.setHistorySet(new LinkedHashSet<String>());
		Log.w("下载完后的ac的历史单词", ac.getHistorySet().toString() + "!");
	}
	
	
	
	/**
	 * 获取远程生词本
	 * @return
	 */
	public static void getNewWords(AppContext ac) {
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
			osw.write(USERNAME_TAG + "=" + ac.getUsername());
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
			ac.setNewWordSet(new LinkedHashSet<String>(
					Arrays.asList(resultStr.split("\\|"))));
		else
			ac.setNewWordSet(new LinkedHashSet<String>());
	}
	
	/**
	 * 添加远程历史单词和生词
	 * @param text
	 * @return
	 */
	public static int addHisNewWords(String username, 
			Set<String> hisWords, Set<String> newWords) {
		// 拼接历史单词和生词
		String resultStr = "";
		String hisWordsStr = "";
		String newWordsStr = "";
		
		if (hisWords.size() > 0) {
		    StringBuilder hisWordsBuilder = new StringBuilder();
		    for (String word : hisWords) {
		    	hisWordsBuilder.append(word + "|");
		    }
		    hisWordsStr = hisWordsBuilder.toString()
		    		.substring(0, hisWordsBuilder.length() - 1);
		}
		
		if (newWords.size() > 0) {
		    StringBuilder newWordsBuilder = new StringBuilder();
		    for (String word : newWords) {
		    	newWordsBuilder.append(word + "|");
		    }
		    newWordsStr = newWordsBuilder.toString()
		    		.substring(0, newWordsBuilder.length() - 1);
		}
		
		Log.w("上传的内容1", hisWordsStr + "!");
		Log.w("上传的内容2", newWordsStr + "!");
		
		try {
			String urlStr = "http://uestctranslator.appspot.com/uploadwords";
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
			osw.write(USERNAME_TAG + "=" + username + "&" + HISWORD_TAG + "=" +
					hisWordsStr + "&" + NEWWORD_TAG + "=" + newWordsStr);
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
		Log.w("上传完后返回的数据", resultStr + "!");
		if (resultStr != null && !resultStr.equals(""))
			return 1;
		else
			return -1;
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
