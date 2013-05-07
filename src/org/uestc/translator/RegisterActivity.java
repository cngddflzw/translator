package org.uestc.translator;

import org.uestc.translator.core.UsernameDupThread;
import org.uestc.translator.core.Validator;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	private final int QUERY_REQUEST_CODE = 1;
	public boolean usernameFlag = false;	// 标记用户名密码是否合法
	public boolean passwdFlag = false;
	public boolean passwdEqual = false;
	public boolean regResult = false;
	public TextView usernameValidMsg;	// 用户名验证信息
	public EditText usernameEt;	// 用户名输入框
	public TextView pwdValidMsg;	// 密码验证信息
	public EditText pwdEt;	//  密码输入框
	public TextView pwdRepValidMsg;	// 重复密码验证信息
	public EditText pwdRepEt;	// 重复密码输入框
	public Button unameDupCheckBtn;	// 检测用户名重复按钮
	public Button regBtn;	// 注册按钮
	public Button cancelBtn;	// 取消按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
		
		AppContext appContext = (AppContext) getApplicationContext();

		usernameValidMsg = (TextView) findViewById(R.id.regUsernameValid);
		usernameEt = (EditText) findViewById(R.id.regUsername);
		unameDupCheckBtn = (Button) findViewById(R.id.regUnameDupBtn);
		
		// 检测网络可用性
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() == null ||
				!connManager.getActiveNetworkInfo().isAvailable()) {
			usernameValidMsg.setText("网络连接不可用");
			return;
		}
		
		// 获取组件并绑定其输入字串验证监听器
		unameDupCheckBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String username = usernameEt.getText().toString();
				usernameValidMsg.setTextColor(Color.YELLOW);
				usernameValidMsg.setText("正在检测请稍侯...");
				
				checkUname(username);
			}
			
		});
		
		// 密码格式验证
		pwdEt = (EditText) findViewById(R.id.regPwd);
		pwdValidMsg = (TextView) findViewById(R.id.regPwdValid);
		pwdRepEt = (EditText) findViewById(R.id.regPwdRepeat);
		pwdRepValidMsg = (TextView) findViewById(R.id.regPwdRepeatValid);
		
		pwdEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String pwd = pwdEt.getText().toString();
				String pwdRep = pwdRepEt.getText().toString();
				
				// 验证密码是否合法
				if (Validator.validateRegPwd(pwd) < 0) {
					pwdValidMsg.setTextColor(Color.RED);
					pwdValidMsg.setText("密码只能使用6-14位数字字母");
					passwdFlag = false;
				} else {
					pwdValidMsg.setTextColor(Color.GREEN);
					pwdValidMsg.setText("密码合法");
					passwdFlag = true;
				}
				
				if (!pwd.equals(pwdRep)) {
					pwdRepValidMsg.setTextColor(Color.RED);
					pwdRepValidMsg.setText("两次密码输入不一致");
					passwdEqual = false;
				} else {
					pwdRepValidMsg.setTextColor(Color.GREEN);
					pwdRepValidMsg.setText("密码合法");
					passwdEqual = true;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// 重复密码输入验证
		pwdRepEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String pwd = pwdEt.getText().toString();
				String pwdRepeat = pwdRepEt.getText().toString();
				
				// 验证密码是否合法
				if (!pwd.equals(pwdRepeat)) {
					pwdRepValidMsg.setTextColor(Color.RED);
					pwdRepValidMsg.setText("两次密码输入不一致");
					passwdEqual = false;
				} else {
					pwdRepValidMsg.setTextColor(Color.GREEN);
					pwdRepValidMsg.setText("密码合法");
					passwdEqual = true;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// 确认注册
		regBtn = (Button) findViewById(R.id.regEnter);
		regBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkUname(usernameEt.getText().toString());
				if (usernameFlag == false || passwdFlag == false ||
						passwdEqual == false) {
					// 提示错误信息
					Dialog dialog = new AlertDialog.Builder(RegisterActivity.this).setIcon(
					     android.R.drawable.btn_star).setTitle("注册失败").setMessage(
					     "请检查用户名和密码是否合法").setPositiveButton("确定",
					     new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
					     }).create();
					dialog.show();
				} else {
					// 进入注册
					Intent intent = new Intent(RegisterActivity.this, RegingActivity.class);
					startActivityForResult(intent, QUERY_REQUEST_CODE);
				}
			}
			
		});
		
		cancelBtn = (Button) findViewById(R.id.regCancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
			
		});
		
		appContext.setRegisterActivity(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 接收单词查询数据并显示
		if (requestCode == QUERY_REQUEST_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					String result;
					usernameEt = (EditText) findViewById(R.id.regUsername);
					if (regResult == true) {
						result = "注册成功";
						// 自动将用户登录
						AppContext appContext = (AppContext) getApplicationContext();
						appContext.setUsername(usernameEt.getText().toString());
					} else {
						result = "注册失败";
					}
					Dialog dialog = new AlertDialog.Builder(RegisterActivity.this).setIcon(
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
	
	private void checkUname(String username) {
		// 验证用户名是否合法
		if (Validator.validateRegUsername(username) < 0) {
			usernameValidMsg.setTextColor(Color.RED);
			usernameValidMsg.setText("用户名必须在6～14位以字母开头，" +
					"字母或数字结尾仅包含[a-zA-Z0-9_]的字符串");
			usernameFlag = false;
			return;
		} else {
			usernameFlag = true;
		}
		
		// 验证用户名是否存在
		if (usernameFlag == true) {
			new UsernameDupThread(RegisterActivity.this).start();
		}
	}
}
