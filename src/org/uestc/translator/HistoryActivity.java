package org.uestc.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class HistoryActivity extends Activity {
	private static final int DELETE_REQUEST_CODE = 1;
	private List<String> historyList;
	private ListView listView;
	private ArrayAdapter<String> historyAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		// 初始化历史查询单词
		AppContext appContext = (AppContext) getApplicationContext();
		historyList = new ArrayList<String>(appContext.getHistorySet());
		Collections.reverse(historyList);
		listView = (ListView) findViewById(R.id.historyList);

		// 重载Adapter以修改字体样式
		historyAdapter = this.new HistoryAdapter(this, 
				android.R.layout.simple_spinner_item, historyList);
		
		historyAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		listView.setAdapter(historyAdapter);
		
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
				String word = historyAdapter.getItem(arg2);
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
				Intent i = new Intent(HistoryActivity.this, DeleteWordActivity.class);
				i.putExtra(getString(R.string.deleteWord), historyAdapter.getItem(arg2));
				i.putExtra(getString(R.string.deleteType), getString(R.string.history));
				HistoryActivity.this.startActivityForResult(i, DELETE_REQUEST_CODE);
				return true;
			}
		});
		
		appContext.setHistoryActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
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
					historyList = new ArrayList<String>(appContext.getHistorySet());
					Collections.reverse(historyList);
					historyAdapter = this.new HistoryAdapter(this, 
							android.R.layout.simple_spinner_item, historyList);
					historyAdapter.setDropDownViewResource(
							android.R.layout.simple_spinner_dropdown_item);
					listView.setAdapter(historyAdapter);
					break;
				default:
					break;
			}
		}
	}

	class HistoryAdapter extends ArrayAdapter<String> {
		
		public HistoryAdapter(Context context, int resource,
				List<String> objects) {
			super(context, resource, objects);
		}
		
		@Override
		public View getView(int position, View convertView,
				ViewGroup parent) {
            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(historyList.get(position));
            mTextView.setTextSize(35);
            mTextView.setTextColor(Color.BLACK);
            return mTextView;
		}
	}
}
