package org.uestc.translator.core;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoteDatabase {
	/**
	 * 获取远程最近查询单词
	 * @return
	 */
	public static Set<String> getHistory() {
		return new LinkedHashSet<String>();
	}
	
	/**
	 * 添加远程最近查询单词
	 * @param text
	 * @return
	 */
	public static int addHistory(String text) {
		return 1;
	}
	
	/**
	 * 获取远程生词本
	 * @return
	 */
	public static Set<String> getNewWords() {
		return null;
	}
	
	/**
	 * 添加远程生词
	 * @param text
	 * @return
	 */
	public static int addNewWord(String text) {
		return 1;
	}
	
	/**
	 * 根据可变条件查询用户表
	 * @param conditions
	 * @return
	 */
	public static String queryUser(HashMap<String, String> conditions) {
		return null;
	}
}
