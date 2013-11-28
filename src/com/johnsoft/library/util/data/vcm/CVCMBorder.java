package com.johnsoft.library.util.data.vcm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.border.Border;

public class CVCMBorder extends CVCMObject<Border>
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
	
	public CVCMBorder(Border border)
	{
		super(border);
		list.add(this);
	}
	
	public void setBorder(JComponent c)
	{
		bind(c, "setBorder");
	}
	
}
