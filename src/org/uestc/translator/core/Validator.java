package org.uestc.translator.core;

public class Validator {
	/**
	 * 验证登录帐号密码
	 * @param username
	 * @param passwd
	 * @return
	 */
	public static int validateLogin(
			String username, String passwd) {
		return 1;
	}
	
	/**
	 * 验证注册帐号密码合法性
	 * @param username
	 * @param passwd
	 * @return
	 */
	public static int validateReg(
			String username, String passwd) {
		return 1;
	}
	
	/**
	 * 检查用户是否登录
	 * @return
	 */
	public static int validateLoginStatus() {
		return -1;
	}
}
