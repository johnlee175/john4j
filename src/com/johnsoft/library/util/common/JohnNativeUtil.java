package com.johnsoft.library.util.common;

import java.io.IOException;

public class JohnNativeUtil 
{
	/**
	 * 执行bat文件
	 * @param batPath  bat文件路径
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
}
