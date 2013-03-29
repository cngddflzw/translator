package org.uestc.translator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import android.app.Application;

/**
 * 利用上下文存储生词和历史类
 * @author alexis
 *
 */
public class AppContext extends Application {
	private String queryString;
	private MainActivity mainActivity;	// MainActivity实例
	private DicActivity dicActivity;	// DicActivity实例
	private HistoryActivity historyActivity;	// DicActivity实例
	private NewWordActivity newWordActivity;	// DicActivity实例
	private Set<String> newWordSet = new TreeSet<String>();	// 生词表
	private Set<String> historySet = new LinkedHashSet<String>();	// 历史查询表
	
	public DicActivity getDicActivity() {
		return dicActivity;
	}
	public void setDicActivity(DicActivity dicActivity) {
		this.dicActivity = dicActivity;
	}
	public MainActivity getMainActivity() {
		return mainActivity;
	}
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	public Set<String> getNewWordSet() {
		return newWordSet;
	}
	public void setNewWordSet(Set<String> newWordSet) {
		this.newWordSet = newWordSet;
	}
	public Set<String> getHistorySet() {
		return historySet;
	}
	public void setHistorySet(Set<String> historySet) {
		this.historySet = historySet;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public HistoryActivity getHistoryActivity() {
		return historyActivity;
	}
	public void setHistoryActivity(HistoryActivity historyActivity) {
		this.historyActivity = historyActivity;
	}
	public NewWordActivity getNewWordActivity() {
		return newWordActivity;
	}
	public void setNewWordActivity(NewWordActivity newWordActivity) {
		this.newWordActivity = newWordActivity;
	}
}
