package org.uestc.translator;

import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.uestc.translator.core.DlHisWordsThread;
import org.uestc.translator.core.DlNewWordsThread;
import org.uestc.translator.core.LocalDatabase;
import org.uestc.translator.core.RemoteDatabase;
import org.uestc.translator.core.UploadWordsThread;
import org.uestc.translator.core.Validator;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends ActivityGroup {
	private LocalDatabase ldb;
	private TabHost tabs;
	private static final String GCM_SENDER_ID = "Nexus_Galaxy_Alexis";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // 详见StrictMode文档
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
		
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		AppContext appContext = (AppContext) getApplicationContext();
		ldb = LocalDatabase.getInstance(this);
		
		// 初始化生词
		appContext.setNewWordSet(ldb.getNewWords());
		
		// 初始化历史词语
		appContext.setHistorySet(ldb.getHistory());
		
		// 初始化tabhost
		tabs = (TabHost) findViewById(R.id.mainTabhost);
		tabs.setup(this.getLocalActivityManager());
		
		// 初始化词典tab
		TabHost.TabSpec spec = tabs.newTabSpec(getString(R.string.dic));
		Intent intent = new Intent().setClass(this, DicActivity.class);
		// tab选择时重新加载tab的Activity内容
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec.setIndicator(getString(R.string.dic));
		spec.setContent(intent);
		tabs.addTab(spec);
		
		// 初始化生词tab
		intent = new Intent().setClass(this, NewWordActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabs.newTabSpec(getString(R.string.newWord));
		spec.setIndicator(getString(R.string.newWord));
		spec.setContent(intent);
		tabs.addTab(spec);
		
		// 初始化历史tab
		intent = new Intent().setClass(this, HistoryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabs.newTabSpec(getString(R.string.history));
		spec.setIndicator(getString(R.string.history));
		spec.setContent(intent);
		tabs.addTab(spec);
		tabs.setCurrentTab(0);
		
		// 修改tab样式
		TabWidget tabWidget = tabs.getTabWidget();
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);   
			view.getLayoutParams().height = 150; //tabWidget.getChildAt(i)
			final TextView tv = (TextView) view.findViewById(android.R.id.title);
			tv.setTextSize(30);
			tv.setTextColor(Color.BLACK);
		}
		
		appContext.setMainActivity(this);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// 根据登录状态动态改变菜单内容
		menu.clear();
		AppContext ac = (AppContext) getApplicationContext();
		if (ac.getUsername() == null || ac.getUsername().equals("")) {
			getMenuInflater().inflate(R.menu.unlog_menu, menu);
			super.onPrepareOptionsMenu(menu);
		} else {
			getMenuInflater().inflate(R.menu.logined_menu, menu);
			super.onPrepareOptionsMenu(menu);
			menu.getItem(0).setTitle("当前用户: " + ac.getUsername());
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		AppContext ac = (AppContext) getApplicationContext();
		switch (item.getItemId()) {
			// 单词小测试
			case R.id.wordTest:
				// 进入测试界面
				intent = new Intent();
				intent.setClass(this, WordTestActivity.class);
				startActivity(intent);
				break;
			// 上传单词
			case R.id.optionUpW:
				ExecutorService exec = Executors.newCachedThreadPool(); 
				Thread tUpWords = new UploadWordsThread(ac);
				exec.execute(tUpWords);
				exec.shutdown();
				Dialog dialog = new AlertDialog.Builder(MainActivity.this).setIcon(
					     android.R.drawable.btn_star).setTitle(null).setMessage(
					    		 "上传完成").setPositiveButton("确定",
					     new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
					     }).create();
				dialog.show();
				break;
			case R.id.optionLogin:
				// 进入登录界面
				intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				break;
			case R.id.optionLogout:
				ac.setUsername(null);
				break;
			case R.id.optionReg:
				// 进入注册界面
				intent = new Intent();
				intent.setClass(this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.optionExit:
				// 退出程序
				MainActivity.this.finish();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStop() {
		// 保存本地历史查询单词和生词
		AppContext ac = (AppContext) getApplicationContext();
		ldb.refreshHistory(ac.getHistorySet());
		ldb.refreshNewWord(ac.getNewWordSet());
		super.onStop();
	}

	public TabHost getTabs() {
		return tabs;
	}
}
