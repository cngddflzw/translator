package org.uestc.translator.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.uestc.translator.AppContext;

public class Validator {
	
	/**
	 * 验证注册帐号合法性
	 * @param username
	 * @return
	 */
	public static int validateRegUsername(
			String username) {
		// 验证用户名是否存在
		// 合法性为以字母开头，字母或数字结尾，一共有6~14个仅包含[a-zA-Z0-9_]的字符
		Pattern p = Pattern.compile("^[A-Za-z]\\w{4,12}[A-Za-z0-9]$");
		Matcher m = p.matcher(username);
		if (m.find())
			return 1;
		else
			return -1;
	}
	
	/**
	 * 验证注册密码合法性
	 * @param pwd
	 * @return
	 */
	public static int validateRegPwd(String pwd) {
		// 合法性为只能使用6-14位字母数字
		Pattern p = Pattern.compile("[a-zA-Z0-9]{6,14}");
		Matcher m = p.matcher(pwd);
		if (m.find())
			return 1;
		else
			return -1;
	}
	
	public static void main(String[] args) {
		String t = "tttttttalexis_k0913";
		System.out.println(validateRegUsername(t));
	}
}