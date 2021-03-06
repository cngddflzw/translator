package org.uestc.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.uestc.translator.core.LocalDatabase;
import org.uestc.translator.core.Validator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class DicActivity extends Activity {
	private final int QUERY_REQUEST_CODE = 1;
	private String srcLang = "zh_CN";	// 翻译源语言
	private String tgLang = "en";	// 翻译目标语言
	private List<String> spnList = new ArrayList<String>();	// 下拉菜单内容
	private Spinner spn;	// 下拉菜单
	private ArrayAdapter<String> spnAdapter;
	private AutoCompleteTextView dicInput;	// 单词输入框
	private List<String> autoText;
	private ArrayAdapter<String> inputAdapter;
	private Button queryBtn; 	// 查询按钮
	private Button addNewWordBtn; 	// 查询按钮
	private TextView dicDisplay;	// 结果现实文本

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dic);
		
		AppContext appContext = (AppContext) getApplicationContext();
		
		// 初始化左下拉菜单
		spnList.add(getString(R.string.cn2en));
		spnList.add(getString(R.string.en2cn));
		spn = (Spinner) findViewById(R.id.dicSpinner);
		spnAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, spnList);
		spnAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		spn.setAdapter(spnAdapter);
		
		// 选择监听器
		spn.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 切换翻译源语言与目标语言
				String pattern = spnAdapter.getItem(arg2);
				if (pattern.equals(getString(R.string.cn2en))) {
					srcLang = "zh_CN";
					tgLang = "en";
				} else if (pattern.equals(getString(R.string.en2cn))) {
					srcLang = "en";
					tgLang = "zh_CN";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// 准备自动补全文本
		dicInput = (AutoCompleteTextView) findViewById(R.id.dicInput);
		Set<String> autoTextSet = new TreeSet<String>();
		autoTextSet = new TreeSet<String>(appContext.getHistorySet());
		autoText = new ArrayList<String>(autoTextSet);
		inputAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, autoText);
		dicInput.setAdapter(inputAdapter);
		
		// 查询按钮
		queryBtn = (Button) findViewById(R.id.dicEnter);
		queryBtn.setOnClickListener(new QueryBtnOnClickListener());
		
		// 添加生词按钮
		addNewWordBtn = (Button) findViewById(R.id.dicAddNewWord);
		addNewWordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 将生词加入生词本
				String newWord = dicInput.getText().toString();
				if (newWord == null || newWord.equals(""))
					return;
				AppContext ac = (AppContext) getApplicationContext();
				ac.getNewWordSet().add(newWord);
				// 显示添加成功信息
				Intent intent = new Intent(DicActivity.this, AddNewWordActivity.class);
				startActivity(intent);
			}
			
		});
		
		appContext.setDicActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dic, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 接收单词查询数据并显示
		if (requestCode == QUERY_REQUEST_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					Bundle b = data.getExtras();
					String result = b.getString(getString(R.string.queryResult));
					if (result.equals(getString(R.string.networkError)))
						result = "\"无法获取结果，请查看网络是否正常\"";
					dicDisplay = (TextView) findViewById(R.id.dicDisplay);
					// 将 \x26#39 替换为 单引号
					// 去除双引号并显示结果
					AppContext ac = (AppContext) getApplicationContext();
					dicInput.setText(ac.getQueryString());
					result = result.replaceAll("\\\\x26#39;", "'");
					dicDisplay.setText(result.substring(1, result.length() - 1));
					break;
				default:
					break;
			}
		}
	}
	
	class QueryBtnOnClickListener implements OnClickListener {
		
		@Override
		public void onClick(View arg0) {
			String queryString = dicInput.getText().toString();
			
			// 清除无效数据
			if (queryString == null || queryString.equals(""))
				return;
			
			// 将查询的单词加入历史记录
			AppContext appContext = (AppContext) getApplicationContext();
			appContext.getHistorySet().add(dicInput.getText().toString());
			appContext.setQueryString(queryString);
			
			// 启动单词查询Activity
			Intent intent = new Intent(DicActivity.this, QueryActivity.class);
			Bundle queryParam = new Bundle();
			queryParam.putString(getString(R.string.srcLang), srcLang);
			queryParam.putString(getString(R.string.tgLang), tgLang);
			queryParam.putString(getString(R.string.queryString), queryString);
			intent.putExtras(queryParam);
			startActivityForResult(intent, QUERY_REQUEST_CODE);
			
		}
	}

	public AutoCompleteTextView getDicInput() {
		return dicInput;
	}

	public String getSrcLang() {
		return srcLang;
	}

	public void setSrcLang(String srcLang) {
		this.srcLang = srcLang;
	}

	public String getTgLang() {
		return tgLang;
	}

	public void setTgLang(String tgLang) {
		this.tgLang = tgLang;
	}

	public Button getQueryBtn() {
		return queryBtn;
	}
	
}
