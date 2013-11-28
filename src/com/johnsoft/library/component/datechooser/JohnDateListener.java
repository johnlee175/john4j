package com.johnsoft.library.component.datechooser;

import java.util.EventListener;


/**
 * 日历控件事件监听器接口
 */
public interface JohnDateListener extends EventListener
{
	/**
	 * 日历控件隐藏时调用
	 */
	public void chooserHide(JohnDateEvent event);
		
	/**
	 * 日历控件显示时调用
	 */
	public void chooserShow(JohnDateEvent event);
}

