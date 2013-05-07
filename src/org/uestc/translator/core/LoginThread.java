package org.uestc.translator.core;

import static org.uestc.translator.core.Data.RESULT_SUCC;
import static org.uestc.translator.core.Data.USERNAME_TAG;
import static org.uestc.translator.core.Data.PASSWD_TAG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.locks.Lock;

import org.uestc.translator.R;
import org.uestc.translator.LoginingActivity;
import org.uestc.translator.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class LoginThread extends Thread {
	LoginActivity la;
	LoginingActivity laing;
	
	public LoginThread(LoginActivity la, LoginingActivity laing) {
		this.la = la;
		this.laing = laing;
	}

	@Override
	public void run() {
		try {
			Log.w("用户登录", "开始登录");
			String urlStr = "http://uestctranslator.appspot.com/login";
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
			osw.write(USERNAME_TAG + "=" + la.usernameEt.getText().toString() + 
					"&" + PASSWD_TAG + "=" + la.passwdEt.getText().toString());
			osw.flush();
			osw.close();
			// 读取响应数据
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String s;
			StringBuilder result = new StringBuilder("");
			while ((s = in.readLine()) != null)
				result.append(s);
			String resultStr = result.toString();
			if (!resultStr.equals(RESULT_SUCC)) {
				la.loginResult = false;
			} else {
				la.loginResult = true;
			}
			laing.setResult(Activity.RESULT_OK);
			laing.finish();
			Log.w("用户登录", "结束登录");
			Log.w("用户登录", "登录结果: " + la.loginResult);
			Log.w("用户登录", "登录结果: " + resultStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
