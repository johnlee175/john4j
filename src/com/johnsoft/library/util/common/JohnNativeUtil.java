package com.johnsoft.library.util.common;

import java.io.IOException;

public class JohnNativeUtil {
	/**
	 * 执行bat文件
	 * 
	 * @param batPath
	 *            bat文件路径
	 */
	public static void exeBat(String batPath) 
	{
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("cmd /c start " + batPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void regAutoStart(String name, String value) 
	{
		Runtime runtime = Runtime.getRuntime();
		String key = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
		String command = "reg add " + key + " /v " + name + " /d " + value;
		try {
			runtime.exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void regKey(String key, String name, String value) 
	{
		// String
		// key="HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
		// String name="myname";//启动项名称
		// String value="mypath";//程序路径
		String command = "reg add " + key + " /v " + name + " /d " + value;
		try {
			Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
