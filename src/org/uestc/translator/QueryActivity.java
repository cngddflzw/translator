package org.uestc.translator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.uestc.translator.core.Translator;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class QueryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_query);
		
		String queryResult = getString(R.string.networkError);
		
		if (isOpenNetwork() == true) {
			// 网络可用，进入联网查询
			Intent intent = getIntent();
			Bundle b = intent.getExtras();
			String srcLang = b.getString(getString(R.string.srcLang));
			String tgLang = b.getString(getString(R.string.tgLang));
			String queryString = b.getString(getString(R.string.queryString));
			// 实际查询单词线程
			Translator t = new Translator(this, srcLang, tgLang, queryString, queryResult);
			ExecutorService exec = Executors.newCachedThreadPool();
			exec.execute(t);
		} else {
			// 网络不可用，直接返回结果
			Intent intent = getIntent();
			Bundle resultBundle = new Bundle();
			resultBundle.putString(getString(R.string.queryResult), queryResult);
			intent.putExtras(resultBundle);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}
	
	/**
	 * 对网络连接状态进行判断
	 * @return  true, 可用； false， 不可用
	 */
	private boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}

}
