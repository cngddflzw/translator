package org.uestc.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.uestc.translator.core.LocalDatabase;
import org.uestc.translator.core.RemoteDatabase;
import org.uestc.translator.core.Validator;
import org.uestc.translator.property.MainAct;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

public class DicActivity extends Activity {
	private List<String> spnList = new ArrayList<String>();	// 下拉菜单内容
	private Spinner spn;	// 下拉菜单
	private ArrayAdapter<String> spnAdapter;
	private AutoCompleteTextView dicInput;
	private List<String> autoText;
	private ArrayAdapter<String> inputAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dic);
		
		AppContext appContext = (AppContext) getApplicationContext();
		
		// 初始化左下拉菜单
		spnList.add(MainAct.cn2en);
		spnList.add(MainAct.en2cn);
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// 触摸监听器
		spn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		// 准备自动补全文本
		dicInput = (AutoCompleteTextView) findViewById(R.id.dicInput);
		Set<String> autoTextSet = new TreeSet<String>();
		if (Validator.validateLoginStatus() < 0)
			autoTextSet = new TreeSet<String>(appContext.getHistorySet());
		else
			autoTextSet = new TreeSet<String>(appContext.getHistorySet());
		autoText = new ArrayList<String>(autoTextSet);
		inputAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, autoText);
		dicInput.setAdapter(inputAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dic, menu);
		return true;
	}

}
