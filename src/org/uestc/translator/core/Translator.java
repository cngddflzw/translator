package org.uestc.translator.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 翻译器类
 * @author alexis
 *
 */
public class Translator {
	
	/**
	 * 翻译核心方法
	 * @param sourceLang 源语言
	 * @param targetLang 目标语言
	 * @param text 待翻译文本
	 * @return
	 * @throws IOException
	 */
	public static String translate(String sourceLang, String targetLang, String text) throws IOException {
		String urlStr = 
				"http://translate.googleapis.com/translate_a/t?" +
		"anno=3&client=te&format=html&v=1.0&logld=v10";
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);	// 可以发送数据
		conn.setDoInput(true);	// 可以接收数据
		conn.setRequestMethod("POST");	// POST方法
		// 必须注意此处需要设置UserAgent，否则google会返回403
		conn.setRequestProperty
		("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.connect();
		//	写入的POST数据
		OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		osw.write("q=" + text + "&sl=" + sourceLang + "&tl=" + targetLang + "&tc=1");
		osw.flush();
		osw.close();
		// 读取响应数据
		BufferedReader in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		String s;
		StringBuilder result = new StringBuilder("");
		while ((s = in.readLine()) != null)
			result.append(s);
		return result.toString();
	}
}
