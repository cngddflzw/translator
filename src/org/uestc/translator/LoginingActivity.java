package org.uestc.translator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.uestc.translator.core.LoginThread;
import org.uestc.translator.core.RegisterThread;
import org.uestc.translator.core.Translator;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LoginingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_logining);
		
		AppContext appContext = (AppContext) getApplicationContext();
		
		// 检测网络可用性
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() == null ||
				!connManager.getActiveNetworkInfo().isAvailable()) {
			// 提示错误信息
			Dialog dialog = new AlertDialog.Builder(LoginingActivity.this).setIcon(
			     android.R.drawable.btn_star).setTitle("登录失败").setMessage(
			     "网络连接不可用").setPositiveButton("确定",
			     new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						return;
					}
			     }).create();
			dialog.show();
			LoginingActivity.this.finish();
			return;
		}
		
		LoginThread t = new LoginThread(appContext.getLoginActivity(), this);
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(t);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

}
