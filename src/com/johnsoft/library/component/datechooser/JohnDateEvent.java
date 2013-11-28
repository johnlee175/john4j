package com.johnsoft.library.component.datechooser;

import java.util.EventObject;

/**
 * 日历控件监听事件
 * @author 李哲浩
 */
public class JohnDateEvent extends EventObject
{
	private static final long serialVersionUID = 1L;

	protected transient String dateString;
	
	public JohnDateEvent(Object source,String dateString)
	{
		super(source);
		this.dateString=dateString;
	}
	
	/**
	 * @return 返回用户选择的日期的格式化文本
	 */
	public String getDateString()
	{
		return dateString;
	}
	
	@Override
	public String toString()
	{
	    return super.getClass().getName() + "[source=" + this.source + ",dateString=" + this.dateString + "]";
	}
}