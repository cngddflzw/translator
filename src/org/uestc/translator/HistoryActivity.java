package org.uestc.translator;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class HistoryActivity extends Activity {
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
		listView = (ListView) findViewById(R.id.historyList);

		// 重载Adapter以修改字体样式
		historyAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, historyList) {
					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
			            TextView mTextView = new TextView(getApplicationContext());
			            mTextView.setText(historyList.get(position));
			            mTextView.setTextSize(35);
			            mTextView.setTextColor(Color.BLACK);
			            return mTextView;
					}
		};
		
		historyAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		listView.setAdapter(historyAdapter);
		
		// 选择监听器
		listView.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

}
