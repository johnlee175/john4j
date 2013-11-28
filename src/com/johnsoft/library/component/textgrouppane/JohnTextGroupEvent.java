package com.johnsoft.library.component.textgrouppane;

import java.util.EventObject;

/**
 * 文本组件组面板监听事件
 * @author 李哲浩
 */
public class JohnTextGroupEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	/**通知将增加一行文本组件的事件*/
	public static final int UNIT_ADDING=0;
	/**通知已增加一行文本组件的事件*/
	public static final int UNIT_ADDED=1;
	/**通知将减少一行文本组件的事件*/
	public static final int UNIT_REMOVEING=1<<1;
	/**通知已减少一行文本组件的事件*/
	public static final int UNIT_REMOVEED=1<<2;
	/**通知将清空文本组件组面板的事件*/
	public static final int GROUP_CLEARING=1<<3;
	/**通知已清空文本组件组面板的事件*/
	public static final int GROUP_CLEARED=1<<4;
	//事件类型
	protected transient int type;
	//相关的对象
	protected transient Object param;
	
	public JohnTextGroupEvent(Object source,int type,Object param)
	{
		super(source);
		this.type=type;
		this.param=param;
	}
	
	/**
	 * @return 返回事件类型
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * @return 	如果是UNIT_ADDED事件,将传入被增加的文本组件,
	 * 			如果是UNIT_REMOVEING,将传入将被删除的文本组件,
	 * 			如果是UNIT_ADDING,将传入增加前最后的文本组件,
	 * 			如果是UNIT_REMOVEED,将传入删除后最后的文本组件,
	 * 			如果是GROUP_CLEARING或GROUP_CLEARED,因暂未做设定,将返回null
	 */
	public Object getParam()
	{
		return param;
	}
	
	@Override
	public String toString()
	{
	    return super.getClass().getName() + "[source=" + this.source + ",dateString=" + this.type + "]";
	}

}
