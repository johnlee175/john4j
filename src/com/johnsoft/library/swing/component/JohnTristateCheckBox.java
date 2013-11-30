package com.johnsoft.library.swing.component;


import java.awt.Graphics;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.ButtonModel;
import javax.swing.JCheckBox;

/**
 * 三态复选框 ,多用于全选,全消复选框或有类似是,否,忽略三种状态时;
 * 请尽量不要使用isSelected做判断,请尽量使用isSelect做判断,前者将不可靠;
 * isSelect返回的是Boolean值,即为三种状态:true,false,null(即为第三态或半选状态);
 * 请尽量不要使用setSelected做设置,请尽量使用setSelect做设置,否则失去了三态的意义;
 * @author 李哲浩
 */
public class JohnTristateCheckBox extends JCheckBox
{
	private static final long serialVersionUID = 1L;
	
	protected class ProxyHandler implements InvocationHandler
	{
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable
		{
			String methodName = method.getName();
			if (isEnabled() && !isPainting && methodName.equals("setPressed"))
			{
				boolean isPressed = ((Boolean) args[0]).booleanValue();
				if (isPressed == false && buttonModel.isArmed())
				{
					nextState();
				}
			}
			if (state == null && isPainting)
			{
				if (methodName.equals("isPressed")
						|| methodName.equals("isArmed"))
				{
					return Boolean.TRUE;
				}
			}
			if (methodName.equals("isSelected"))
			{
				if (state == null||state == true)
				{
					return Boolean.TRUE;
				} else
				{
					return Boolean.FALSE;
				}
			}
			if (methodName.equals("setSelected"))
			{
				if (Boolean.TRUE.equals(args[0]))
				{
					setSelect(true);
				} else
				{
					setSelect(false);
				}
			}
			return method.invoke(buttonModel, args);
		}
	}

	private boolean isPainting = false;
	private ButtonModel buttonModel = null;
	private Boolean state = false;

	public JohnTristateCheckBox()
	{
		this(null);
	}

	public JohnTristateCheckBox(String text)
	{
		this(text, false);
	}

	public JohnTristateCheckBox(String text, Boolean state)
	{
		super(text);
		this.buttonModel = this.getModel();
		ButtonModel proxyModel = (ButtonModel) Proxy.newProxyInstance(
				JohnTristateCheckBox.class.getClassLoader(),
				new Class[] { ButtonModel.class }, new ProxyHandler());
		this.setModel(proxyModel);
		this.setSelect(state);
	}

	public Boolean isSelect()
	{
		return state;
	}

	public void nextState()
	{
		if ( state!=null && true == state )
		{
			state = null;
		} else if ( state!=null && false == state )
		{
			state = true;
		} else
		{
			state = false;
		}
		this.fireStateChanged();
	}

	public void setSelect(Boolean state)
	{
		this.state = state;
		this.fireStateChanged();
	}

	public void paintComponent(Graphics g)
	{
		isPainting = true;
		super.paintComponent(g);
		isPainting = false;
	}
	
}