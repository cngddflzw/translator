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
import org.uestc.translator.RegingActivity;
import org.uestc.translator.RegisterActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class RegisterThread extends Thread {
	RegisterActivity ra;
	RegingActivity ra2;
	
	public RegisterThread(RegisterActivity ra, RegingActivity ra2) {
		this.ra = ra;
		this.ra2 = ra2;
	}

	@Override
	public void run() {
		try {
//			Log.w("用户注册", "开始注册");
			String urlStr = "http://uestctranslator.appspot.com/register";
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
			osw.write(USERNAME_TAG + "=" + ra.usernameEt.getText().toString() + 
					"&" + PASSWD_TAG + "=" + ra.pwdEt.getText().toString());
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
				ra.regResult = false;
			} else {
				ra.regResult = true;
			}
			ra2.setResult(Activity.RESULT_OK);
			ra2.finish();
//			Log.w("用户注册", "结束注册");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
