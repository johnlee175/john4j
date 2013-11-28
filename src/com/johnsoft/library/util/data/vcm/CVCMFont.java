package com.johnsoft.library.util.data.vcm;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class CVCMFont extends CVCMObject<Font>
{
	private static final List<CVCMObject<?>> list=new ArrayList<CVCMObject<?>>();
	
	protected void bind(Object obj,String methodName)
	{
		try
		{
			interruptOldConntect(obj, methodName, list);
			obj.getClass().getMethod(methodName, get().getClass()).invoke(obj, get());
			connect(obj, methodName);//关键
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	protected void interruptOldConntect(Object obj,String methodName,List<CVCMObject<?>> vcmList)
	{
		for(CVCMObject<?> vcm:vcmList)
		{
			if(vcm!=this)
			{
				disconnect(obj, methodName);//关键
			}
		}
	}
	
	public CVCMFont(Font font)
	{
		super(font);
		list.add(this);
	}
	
	public void setFont(Component c)
	{
		bind(c, "setFont");
	}
	
}
