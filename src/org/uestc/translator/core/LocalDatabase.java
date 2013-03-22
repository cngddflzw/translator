package org.uestc.translator.core;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class LocalDatabase {
	/**
	 * 获取本地最近查询单词
	 * @return
	 */
	public static Set<String> getHistory() {
		Set<String> set = new LinkedHashSet<String>();
		set.add("abc");
		set.add("acc");
		return set;
	}
	
	/**
	 * 添加本地最近查询单词
	 * @param text
	 * @return
	 */
	public static int addHistory(String text) {
		return 1;
	}
	
	/**
	 * 获取本地生词本
	 * @return
	 */
	public static Set<String> getNewWords() {
		Set<String> set = new TreeSet<String>();
		set.add("abc");
		set.add("acc");
		set.add("gcc");
		set.add("kof");
		return set;
	}
	
	/**
	 * 添加本地生词
	 * @param text
	 * @return
	 */
	public static int addNewWord(String text) {
		return 1;
	}
}