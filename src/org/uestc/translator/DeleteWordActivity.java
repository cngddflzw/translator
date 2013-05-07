package org.uestc.translator;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class DeleteWordActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_delete_word);
		
		// 获取待删除词语
		Intent i = getIntent();
		final String deleteWord = i.getStringExtra(getString(R.string.deleteWord));
		final String deleteType = i.getStringExtra(getString(R.string.deleteType));
		
		// 设置提示文本信息
		TextView tip = (TextView) findViewById(R.id.deleteWordTips);
		tip.setText("你确定要删除单词 \"" + deleteWord + "\" 吗？");
		
		// 获取确认取消按钮并绑定事件
		Button confirmBtn = (Button) findViewById(R.id.deleteWordConfirm);
		Button cancelBtn = (Button) findViewById(R.id.deleteWordCancel);
		
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppContext ac = (AppContext) getApplicationContext();
				if (deleteType.equals(getString(R.string.history))) {
					ac.getHistorySet().remove(deleteWord);
				} else if (deleteType.equals(getString(R.string.newWord))) {
					ac.getNewWordSet().remove(deleteWord);
				}
				DeleteWordActivity.this.setResult(RESULT_OK);
				DeleteWordActivity.this.finish();
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DeleteWordActivity.this.finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_word, menu);
		return true;
	}

}
