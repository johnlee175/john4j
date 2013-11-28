package com.johnsoft.library.util.data.vcm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CVCMImageIcon extends CVCMObject<ImageIcon>
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
	
	public CVCMImageIcon(ImageIcon imgIcon)
	{
		super(imgIcon);
		list.add(this);
	}
	
	public void setIcon(JLabel c)
	{
		bind(c, "setIcon");
	}
	
	public void setIcon(AbstractButton c)
	{
		bind(c, "setIcon");
	}
	
	public void setDisabledIcon(AbstractButton c)
	{
		bind(c, "setDisabledIcon");
	}
	
	public void setPressedIcon(AbstractButton c)
	{
		bind(c, "setPressedIcon");
	}
	
	public void setSelectedIcon(AbstractButton c)
	{
		bind(c, "setSelectedIcon");
	}
	
	public void setRolloverIcon(AbstractButton c)
	{
		bind(c, "setRolloverIcon");
	}
	
	public void setRolloverSelectedIcon(AbstractButton c)
	{
		bind(c,"setRolloverSelectedIcon");
	}
	
}
