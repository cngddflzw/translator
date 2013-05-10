package org.uestc.translator;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class TestResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_test_result);
		
//		LinearLayout originWordsLayout = (LinearLayout) findViewById(R.id.testResultCol1);
//		LinearLayout transWordsLayout = (LinearLayout) findViewById(R.id.testResultCol2);
//		LinearLayout crrTransWordsLayout = (LinearLayout) findViewById(R.id.testResultCol3);
		
		AppContext ac = (AppContext) getApplicationContext();
		ScrollView sv = new ScrollView(this);
		LinearLayout mainLayout = new LinearLayout(this);
		LinearLayout originWordsLayout = new LinearLayout(this);
		LinearLayout transWordsLayout = new LinearLayout(this);
		LinearLayout crrTransWordsLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);  
		originWordsLayout.setOrientation(LinearLayout.VERTICAL);  
		transWordsLayout.setOrientation(LinearLayout.VERTICAL);
		crrTransWordsLayout.setOrientation(LinearLayout.VERTICAL);
		
		// 设置原词列
		TextView originTitle = new TextView(this);
		originTitle.setWidth(250);
		originTitle.setHeight(100);
		originTitle.setGravity(Gravity.CENTER);
		originTitle.setText("原词");
		originTitle.setTextSize(20);
		originTitle.setTextColor(Color.BLACK);
		originWordsLayout.addView(originTitle);
		for (String word : ac.getOriginWords()) {
			TextView orgWordTv = new TextView(this);
			orgWordTv.setWidth(250);
			orgWordTv.setHeight(100);
			orgWordTv.setGravity(Gravity.CENTER);
			orgWordTv.setText(word);
			orgWordTv.setTextSize(20);
			orgWordTv.setTextColor(Color.BLACK);
			originWordsLayout.addView(orgWordTv);
		}
		
		// 设置翻译列
		TextView transTitle = new TextView(this);
		transTitle.setWidth(250);
		transTitle.setHeight(100);
		transTitle.setGravity(Gravity.CENTER);
		transTitle.setText("你的翻译");
		transTitle.setTextColor(Color.BLACK);
		transTitle.setTextSize(20);
		transWordsLayout.addView(transTitle);
		for (String word : ac.getTransWords()) {
			TextView transWordTv = new TextView(this);
			transWordTv.setWidth(250);
			transWordTv.setHeight(100);
			transWordTv.setGravity(Gravity.CENTER);
			transWordTv.setText(word);
			transWordTv.setTextSize(20);
			
			// 设置正确和错误的颜色
			for (int i = 0; i < ac.getCorrectWords().size(); i++) {
				ac.getCorrectWords().set(i, ac.getCorrectWords().get(i).toLowerCase());
			}
			if (ac.getCorrectWords().indexOf(word) 
					== ac.getTransWords().indexOf(word)) {
				transWordTv.setTextColor(Color.GREEN);
			} else {
				transWordTv.setTextColor(Color.RED);
			}
			transWordsLayout.addView(transWordTv);
		}
		
		// 设置正解列
		TextView correctTitle = new TextView(this);
		correctTitle.setWidth(250);
		correctTitle.setHeight(100);
		correctTitle.setGravity(Gravity.CENTER);
		correctTitle.setText("正解");
		correctTitle.setTextColor(Color.BLACK);
		correctTitle.setTextSize(20);
		crrTransWordsLayout.addView(correctTitle);
		for (String word : ac.getCorrectWords()) {
			TextView orgWordTv = new TextView(this);
			orgWordTv.setWidth(250);
			orgWordTv.setHeight(100);
			orgWordTv.setGravity(Gravity.CENTER);
			orgWordTv.setText(word);
			orgWordTv.setTextColor(Color.BLACK);
			orgWordTv.setTextSize(20);
			crrTransWordsLayout.addView(orgWordTv);
		}
		
		sv.addView(mainLayout);
		mainLayout.addView(originWordsLayout);
		mainLayout.addView(transWordsLayout);
		mainLayout.addView(crrTransWordsLayout);
		setContentView(sv);
	}

}
