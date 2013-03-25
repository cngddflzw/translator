package org.uestc.translator;

import org.uestc.translator.core.LocalDatabase;
import org.uestc.translator.core.RemoteDatabase;
import org.uestc.translator.core.Validator;

import android.os.Bundle;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends ActivityGroup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		AppContext appContext = (AppContext) getApplicationContext();
		
		// 初始化生词
		if (Validator.validateLoginStatus() < 0)
			appContext.setNewWordSet(LocalDatabase.getNewWords());
		else
			appContext.setNewWordSet(RemoteDatabase.getNewWords());
		
		// 初始化历史词语
		if (Validator.validateLoginStatus() < 0)
			appContext.setHistorySet(LocalDatabase.getHistory());
		else
			appContext.setHistorySet(RemoteDatabase.getHistory());
		
		// 初始化tabhost
		TabHost tabs = (TabHost) findViewById(R.id.mainTabhost);
		tabs.setup(this.getLocalActivityManager());
		
		// 初始化词典tab
		TabHost.TabSpec spec = tabs.newTabSpec(getString(R.string.dic));
		Intent intent = new Intent().setClass(this, DicActivity.class);
		spec.setIndicator(getString(R.string.dic));
		spec.setContent(intent);
		tabs.addTab(spec);
		
		// 初始化生词tab
		intent = new Intent().setClass(this, NewWordActivity.class);
		spec = tabs.newTabSpec(getString(R.string.newWord));
		spec.setIndicator(getString(R.string.newWord));
		spec.setContent(intent);
		tabs.addTab(spec);
		
		// 初始化历史tab
		intent = new Intent().setClass(this, HistoryActivity.class);
		spec = tabs.newTabSpec(getString(R.string.history));
		spec.setIndicator(getString(R.string.history));
		spec.setContent(intent);
		tabs.addTab(spec);
		tabs.setCurrentTab(0);
		
		// 修改tab样式
		TabWidget tabWidget = tabs.getTabWidget();
		int count = tabWidget.getChildCount();//TabHost中有一个getTabWidget()的方法
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);   
			view.getLayoutParams().height = 150; //tabWidget.getChildAt(i)
			final TextView tv = (TextView) view.findViewById(android.R.id.title);
			tv.setTextSize(30);
			tv.setTextColor(Color.BLACK);
		}
		 
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// 根据登录状态动态改变菜单内容
		menu.clear();
		if (Validator.validateLoginStatus() < 0)
			getMenuInflater().inflate(R.menu.unlog_menu, menu);
		else
			getMenuInflater().inflate(R.menu.logined_menu, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			case R.id.optionLogin:
				// 进入登录界面
				intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				break;
			case R.id.optionLogout:
				break;
			case R.id.optionReg:
				// 进入注册界面
				intent = new Intent();
				intent.setClass(this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.optionExit:
				// 退出程序
				intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
