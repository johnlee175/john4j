package com.johnsoft.library.util.data.vcm;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

public class CVCMBoolean extends CVCMObject<Boolean>
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
	
	public CVCMBoolean(Boolean t)
	{
		super(t);
		list.add(this);
	}
	
	public void setVisible(Component c)
	{
		bind(c, "setVisible");
	}
	
	public void setEnabled(Component c)
	{
		bind(c, "setEnabled");
	}
	
	public void setFocusable(Component c)
	{
		bind(c, "setFocusable");
	}
	
	public void setOpaque(JComponent c)
	{
		bind(c, "setOpaque");
	}
	
	public void setSelected(AbstractButton c)
	{
		bind(c, "setSelected");
	}
	
	public void setEditable(JTextComponent c)
	{
		bind(c, "setEditable");
	}
	
}
