package org.uestc.translator;

import org.uestc.translator.core.UsernameDupThread;
import org.uestc.translator.core.Validator;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	public boolean usernameFlag = false;	// 标记用户名密码是否合法
	public boolean passwdFlag = false;
	public TextView usernameValidMsg;
	public EditText usernameEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏程序标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
		
		usernameValidMsg = (TextView) findViewById(R.id.regUsernameValid);
		usernameEt = (EditText) findViewById(R.id.regUsername);
		
		// 检测网络可用性
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() == null ||
				!connManager.getActiveNetworkInfo().isAvailable()) {
			usernameValidMsg.setText("网络连接不可用");
			return;
		}
		
		// 获取组件并绑定其输入字串验证监听器
		usernameEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// 验证用户名是否存在
				new UsernameDupThread(RegisterActivity.this).start();
			}
			
		});
		
		usernameEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String username = usernameEt.getText().toString();
				
				// 验证用户名是否合法
				if (Validator.validateRegUsername(username) < 0) {
					usernameValidMsg.setTextColor(Color.RED);
					usernameValidMsg.setText("用户名必须在6～14位以字母开头，" +
							"字母或数字结尾仅包含[a-zA-Z0-9_]的字符串");
				} else {
					usernameValidMsg.setTextColor(Color.GREEN);
					usernameValidMsg.setText("用户名合法");
					usernameFlag = true;
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
	}
}
