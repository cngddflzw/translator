package org.uestc.translator;

import java.util.Set;

import org.uestc.translator.core.RemoteDatabase;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private final int QUERY_REQUEST_CODE = 1;
	public EditText usernameEt;
	public EditText passwdEt;
	public Button loginBtn;
	public Button cancelBtn;
	public boolean loginResult = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		
		usernameEt = (EditText) findViewById(R.id.loginUsername);
		passwdEt = (EditText) findViewById(R.id.loginPwd);
		loginBtn = (Button) findViewById(R.id.loginEnter);
		cancelBtn = (Button) findViewById(R.id.loginCancel);
		
		// 绑定登录事件
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 登录
				Intent intent = new Intent(LoginActivity.this, LoginingActivity.class);
				startActivityForResult(intent, QUERY_REQUEST_CODE);
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				LoginActivity.this.finish();
			}
			
		});
		
		AppContext appContext = (AppContext) getApplicationContext();
		
		// 保存当前act实例
		appContext.setLoginActivity(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 接收单词查询数据并显示
		if (requestCode == QUERY_REQUEST_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					String result;
					usernameEt = (EditText) findViewById(R.id.loginUsername);
					if (loginResult == true) {
						result = "登录成功";
						AppContext appContext = (AppContext) getApplicationContext();
						appContext.setUsername(usernameEt.getText().toString());
						
						// 将云端单词同步到本地
						Set<String> remoteHisWords = RemoteDatabase.getHistory(appContext.getUsername());
						Set<String> remoteNewWords = RemoteDatabase.getNewWords(appContext.getUsername());
						appContext.setHistorySet(remoteHisWords);
						appContext.setNewWordSet(remoteNewWords);
					} else {
						result = "登录失败,请检查用户名密码是否正确";
					}
					Dialog dialog = new AlertDialog.Builder(LoginActivity.this).setIcon(
						     android.R.drawable.btn_star).setTitle(null).setMessage(
						    		 result).setPositiveButton("确定",
						     new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
						     }).create();
					dialog.show();
					break;
				default:
					break;
			}
		}
	}
}
