package org.uestc.translator;

import java.util.Set;

import android.app.Application;

/**
 * 利用上下文存储生词和历史类
 * @author alexis
 *
 */
public class AppContext extends Application {
	private Set<String> newWordSet;	// 生词表
	private Set<String> historySet;	// 历史查询表
	
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
}
