package org.uestc.translator;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class NewWordActivity extends Activity {
	private static final int DELETE_REQUEST_CODE = 1;
	private List<String> newWordList;
	private ListView listView;
	private ArrayAdapter<String> newWordAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_word);
		
		// 初始化生词本单词
		AppContext appContext = (AppContext) getApplicationContext();
		newWordList = new ArrayList<String>(appContext.getNewWordSet());
		Collections.reverse(newWordList);
		listView = (ListView) findViewById(R.id.newWordList);
		
		// 将生词按照拼音排序
		String[] wordsArray = newWordList.toArray(new String[0]);
		Arrays.sort(wordsArray, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return Collator.getInstance(Locale.CHINESE).compare(arg0, arg1); 
			}
			
		});
		newWordList = new ArrayList<String>(Arrays.asList(wordsArray));

		// 重载Adapter以修改字体样式
		newWordAdapter = this.new NewWordAdapter(this, 
				android.R.layout.simple_spinner_item, newWordList);
		
		newWordAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		listView.setAdapter(newWordAdapter);
		
		// 选择监听器
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 自动查询选择的词语
				AppContext appContext = (AppContext) getApplicationContext();
				MainActivity ma = appContext.getMainActivity();
				DicActivity da = appContext.getDicActivity();
				TabHost tabs = ma.getTabs();
				Button queryBtn = da.getQueryBtn();
				AutoCompleteTextView dicInput = da.getDicInput();
				tabs.setCurrentTab(0);
				String word = newWordAdapter.getItem(arg2);
				// 自动判断中英互译模式
				Pattern p = Pattern.compile("[A-z]+");
				Matcher m = p.matcher(word);
				if (m.find()) {
					da.setSrcLang("en");
					da.setTgLang("zh_CN");
				} else {
					da.setSrcLang("zh_CN");
					da.setTgLang("en");
				}
				appContext.setQueryString(word);
				dicInput.setText(word);
				OnClickListener queryBtnOnClickListener = da.new QueryBtnOnClickListener();
				queryBtn.setOnClickListener(queryBtnOnClickListener);
				queryBtnOnClickListener.onClick(null);
			}
			
		});
		
		// 长按删除词语
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent i = new Intent(NewWordActivity.this, DeleteWordActivity.class);
				i.putExtra(getString(R.string.deleteWord), newWordAdapter.getItem(arg2));
				i.putExtra(getString(R.string.deleteType), getString(R.string.newWord));
				NewWordActivity.this.startActivityForResult(i, DELETE_REQUEST_CODE);
				return true;
			}
		});

		appContext.setNewWordActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_word, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 删除单词后重载单词列表
		if (requestCode == DELETE_REQUEST_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					AppContext appContext = (AppContext) getApplicationContext();
					newWordList = new ArrayList<String>(appContext.getNewWordSet());
					Collections.reverse(newWordList);
					newWordAdapter = this.new NewWordAdapter(this, 
							android.R.layout.simple_spinner_item, newWordList);
					newWordAdapter.setDropDownViewResource(
							android.R.layout.simple_spinner_dropdown_item);
					listView.setAdapter(newWordAdapter);
					break;
				default:
					break;
			}
		}
	}
	
	// 生词列表适配器
	class NewWordAdapter extends ArrayAdapter<String> {
		
		public NewWordAdapter(Context context, int resource,
				List<String> objects) {
			super(context, resource, objects);
		}
		
		@Override
		public View getView(int position, View convertView,
				ViewGroup parent) {
            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(newWordList.get(position));
            mTextView.setTextSize(35);
            mTextView.setTextColor(Color.BLACK);
            return mTextView;
		}
	}
}
