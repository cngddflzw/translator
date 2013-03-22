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

public class NewWordActivity extends Activity {
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
		listView = (ListView) findViewById(R.id.newWordList);

		// 重载Adapter以修改字体样式
		newWordAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, newWordList) {
					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
			            TextView mTextView = new TextView(getApplicationContext());
			            mTextView.setText(newWordList.get(position));
			            mTextView.setTextSize(35);
			            mTextView.setTextColor(Color.BLACK);
			            return mTextView;
					}
		};
		
		newWordAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		listView.setAdapter(newWordAdapter);
		
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
		getMenuInflater().inflate(R.menu.new_word, menu);
		return true;
	}

}
