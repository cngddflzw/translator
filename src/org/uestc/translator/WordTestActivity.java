package org.uestc.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.uestc.translator.core.LocalDatabase;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WordTestActivity extends Activity {
	private TextView originWord;
	private EditText transWord;
	private Button nextWordBtn;
	private int nextCount = 0;	// 已测试单词计数器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_word_test);
		
		// 检测网络可用性
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() == null ||
				!connManager.getActiveNetworkInfo().isAvailable()) {
			// 提示错误信息
			Dialog dialog = new AlertDialog.Builder(WordTestActivity.this).setIcon(
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
			WordTestActivity.this.finish();
			return;
		}
		
		// 从数据库中找出测试中错的最多的10个单词
		final LocalDatabase db = LocalDatabase.getInstance(WordTestActivity.this);
		final AppContext ac = (AppContext) getApplicationContext();
		Set<String> wordsSet = new HashSet<String>(db.getMistakeWords());
		wordsSet.addAll(db.getHistory());
		wordsSet.addAll(db.getNewWords());
		// 如果生词和历史单词少于10，提示用户先多储存生词和历史单词
		if (wordsSet.size() < 10) {
			Dialog dialog = new AlertDialog.Builder(WordTestActivity.this).setIcon(
				     android.R.drawable.btn_star).setTitle(null).setMessage(
				    		 "生词及历史单词不足10个，请先存储10个生词和历史单词").setPositiveButton("确定",
				     new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							WordTestActivity.this.finish();
						}
				     }).create();
			dialog.show();
			return;
		}
		
		// 储存原词，翻译单词，正确翻译单词
		List<String> wordsList = new ArrayList<String>(wordsSet);
		ac.setOriginWords(wordsList);
		ac.setTransWords(new ArrayList<String>());
		
		// 将正确单词结果置空
		for (int i = ac.getCorrectWords().size(); i <= 10; i++) {
			ac.getCorrectWords().add("");
		}
		
		// 一次性查出所有正确结果
		ExecutorService exec = Executors.newCachedThreadPool();
		int crrCount = 0;
		for (String originWord : wordsList) {
			// 自动判断中英互译模式
			Pattern p = Pattern.compile("[A-z]+");
			Matcher m = p.matcher(originWord);
			String srcLang, tgLang;
			if (m.find()) {
				srcLang = "en";
				tgLang = "zh_CN";
			} else {
				srcLang = "zh_CN";
				tgLang = "en";
			}
			Translator t = new Translator(srcLang, tgLang,
					originWord, ac.getCorrectWords(), crrCount++);
			exec.execute(t);
		}
		exec.shutdown();
		
		originWord = (TextView) findViewById(R.id.originWord);
		originWord.setText(wordsList.get(0));
		transWord = (EditText) findViewById(R.id.translateText);
		nextWordBtn = (Button) findViewById(R.id.nextWord);
		
		// 绑定"下一个"按钮事件
		nextWordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextCount++;
				
				// 记录翻译结果并显示下一个单词
				ac.getTransWords().add(transWord.getText().toString().toLowerCase());
				originWord.setText(ac.getOriginWords().get(nextCount));
				transWord.setText("");
				
				// 已经测试9个单词，改变按钮文本
				if (nextCount == 9) {
					nextWordBtn.setText("测试结果");
					nextWordBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ac.getTransWords().add(transWord.getText().toString().toLowerCase());
							// 显示测试结果页面
							Intent intent = new Intent();
							intent.setClass(WordTestActivity.this, TestResultActivity.class);
							startActivity(intent);
							// 用户翻译错误单词存入数据，错误次数+1
							for (int i = 0; i < 10; i++) {
								if (!ac.getTransWords().get(i).equals(
										ac.getCorrectWords().get(i))) {
									db.addMistakeWord(ac.getOriginWords().get(i));
								}
							}
						}
						
					});
				}
				
			}
			
		});
	}

}
