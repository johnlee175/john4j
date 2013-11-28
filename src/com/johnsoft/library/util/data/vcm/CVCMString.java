package com.johnsoft.library.util.data.vcm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

public class CVCMString extends CVCMObject<String>
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
	
	public CVCMString(String string)
	{
		super(string);
		list.add(this);
	}
	
	public void setText(JTextComponent c)
	{
		bind(c, "setText");
	}
	
	public void setText(JLabel c)
	{
		bind(c, "setText");
	}
	
	public void setText(AbstractButton c)
	{
		bind(c, "setText");
	}
	
	public void setToolTipText(JComponent c)
	{
		bind(c, "setToolTipText");
	}
	
}
