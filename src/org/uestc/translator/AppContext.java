package org.uestc.translator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
	private HistoryActivity historyActivity;
	private NewWordActivity newWordActivity;
	private RegisterActivity registerActivity;
	private LoginActivity loginActivity;
	private String username;	// 登录用户用户名
	private Set<String> newWordSet = new TreeSet<String>();	// 生词表
	private Set<String> historySet = new LinkedHashSet<String>();	// 历史查询表
	private List<String> originWords = new ArrayList<String>();	// 测试原词
	private List<String> transWords = new ArrayList<String>();	// 测试用户翻译词语
	private List<String> correctWords = new ArrayList<String>();	// 测试中正确翻译结果
	
	
	public List<String> getCorrectWords() {
		return correctWords;
	}
	public void setCorrectWords(List<String> correctWors) {
		this.correctWords = correctWors;
	}
	public List<String> getOriginWords() {
		return originWords;
	}
	public void setOriginWords(List<String> originWords) {
		this.originWords = originWords;
	}
	public List<String> getTransWords() {
		return transWords;
	}
	public void setTransWords(List<String> transWords) {
		this.transWords = transWords;
	}
	public LoginActivity getLoginActivity() {
		return loginActivity;
	}
	public void setLoginActivity(LoginActivity loginActivity) {
		this.loginActivity = loginActivity;
	}
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
	public RegisterActivity getRegisterActivity() {
		return registerActivity;
	}
	public void setRegisterActivity(RegisterActivity registerActivity) {
		this.registerActivity = registerActivity;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
