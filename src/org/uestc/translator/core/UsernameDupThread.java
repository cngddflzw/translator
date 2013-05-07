package org.uestc.translator.core;

import static org.uestc.translator.core.Data.RESULT_FAILED;
import static org.uestc.translator.core.Data.USERNAME_TAG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.locks.Lock;

import org.uestc.translator.RegisterActivity;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

public class UsernameDupThread extends Thread {
	RegisterActivity ra;
	Handler h = new Handler();
	
	public UsernameDupThread(RegisterActivity ra) {
		this.ra = ra;
	}

	@Override
	public void run() {
		try {
			Log.w("检测用户名重复性", "开始检测");
			String urlStr = "http://uestctranslator.appspot.com/registerdup";
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
			osw.write(USERNAME_TAG + "=" + ra.usernameEt.getText().toString());
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
			if (resultStr.equals("") || resultStr.equals(RESULT_FAILED)) {
				h.post(new Runnable() {

					@Override
					public void run() {
						ra.usernameValidMsg.setTextColor(Color.RED);
						ra.usernameValidMsg.setText("用户名已存在");
					}
					
				});
				ra.usernameFlag = false;
			} else {
				h.post(new Runnable() {

					@Override
					public void run() {
						ra.usernameValidMsg.setTextColor(Color.GREEN);
						ra.usernameValidMsg.setText("用户名合法");
					}
					
				});
				ra.usernameFlag = true;
			}

			Log.w("检测用户名重复性", "结束检测");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
