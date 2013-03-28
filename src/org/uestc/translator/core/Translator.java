package org.uestc.translator.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.uestc.translator.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 翻译器类
 * @author alexis
 *
 */
public class Translator extends Thread {
	private String srcLang;	// 源语言
	private String tgLang;	// 目标语言
	private String queryString;	// 待翻译文本
	private String queryResult;	// 翻译结果
	private Activity queryActivity;	// 翻译Activity
	
	public Translator(Activity qa, String sl, String tl,
			String qs, String qr) {
		this.queryActivity = qa;
		this.srcLang = sl;
		this.tgLang = tl;
		this.queryString = qs;
		this.queryResult = qr;
	}
	
	/**
	 * 翻译核心方法
	 * @return
	 * @throws IOException
	 */
	@Override
	public void run() {
		try {
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
			osw.write("q=" + queryString + "&sl=" + srcLang + "&tl=" + tgLang + "&tc=1");
			osw.flush();
			osw.close();
			// 读取响应数据
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String s;
			StringBuilder result = new StringBuilder("");
			while ((s = in.readLine()) != null)
				result.append(s);
			queryResult = result.toString();
//			Log.i("测试查询结果", queryResult);
			
			// 返回查询结果
			Intent intent = queryActivity.getIntent();
			Bundle resultBundle = new Bundle();
			resultBundle.putString(queryActivity.getString(R.string.queryResult), queryResult);
			intent.putExtras(resultBundle);
			queryActivity.setResult(Activity.RESULT_OK, intent);
			queryActivity.finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
