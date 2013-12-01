package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * 主要用于工具栏的傻瓜按钮
 * @author john
 */
public abstract class JohnToolbarItem  extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 工具栏中创建的按钮枚举类型,
	 * TOGGLE为Toggle按钮,有两种状态,可注册两种事件
	 * DROP_DOWN为下拉按钮,需要注册弹出菜单以便在点击下拉图标时显示,
	 * 按钮和下拉按钮在被按压时产生联动按压效果,如果想取消这种效果请使用SIMPLE和DROPICON进行组合
	 * SIMPLE为普通按钮,实质是图标标签或面板所绘,不同于原生JButton
	 * DROPICON 一种便捷获得下拉按钮三角图标的模式,同样需要注册弹出菜单
	 */
	public enum ItemModel
	{
		SIMPLE,TOGGLE,DROP_DOWN,DROPICON
	}
	
	private String name;//该组件和action的名字
	private ImageIcon icon;//按钮图标
	private String tooltip;//按钮提示
	private KeyStroke quickKey;//快捷键
	
	private JPopupMenu popup;//弹出菜单
	private JohnToolbarItem mine=this;
	
	private boolean isToggled;//是否是Toggle按钮,并处于按压状态
	protected boolean isRollover;//鼠标是否悬停
	protected boolean isPressed;//鼠标是否按压
	protected boolean isDropDownModel;//是否是DROP_DOWN下拉按钮模式
	protected boolean isDropIcon;//是否是DROPICON下拉按钮模式
	
	/**
	 * 放置下拉图标的区域宽度
	 */
	protected int dropsize;
	/**
	 * 放置按钮的区域宽高,同放置下拉图标的区域高度
	 */
	protected int size=28;
	protected Color backgroundColor=this.getBackground();//按钮非图标区域背景色
	protected Color toggledColor=this.getBackground().darker();//TOGGLE模式下按钮被按压时的非图标区域背景色
	protected Color dropIconColor=Color.BLACK;//DROP_DOWN下拉按钮模式下下拉图标的颜色

	/**
	 * @param model 按钮枚举类型
	 * @param name 该组件和action的名字
	 * @param icon 按钮图标,最好是背景透明的简单低像素图片,会自动缩放成指定大小,
	 *             DROPICON模式将不采用此图标,可以传入null
	 * @param tooltip 按钮工具提示信息
	 * @param quickKey 按钮快捷键
	 */
	public JohnToolbarItem(ItemModel model,String name,ImageIcon icon,String tooltip,KeyStroke quickKey)
	{
		this.name=name;
	  this.icon=icon;
		this.tooltip=tooltip;
		this.quickKey=quickKey;
		
		if(model==ItemModel.SIMPLE)
		{
			createSimpleItem();
		}
		else if(model==ItemModel.TOGGLE)
		{
			createToggleItem();
		}
		else if(model==ItemModel.DROP_DOWN)
		{
			isDropDownModel=true;
			createDropDownItem();
		}
		else if(model==ItemModel.DROPICON)
		{
			isDropIcon=true;
			createDropIcon();
		}
	}
	
	/**
	 * 组件的共同构造阶段,
	 * 注册提示信息,
	 * 注册快捷键,
	 * 注册鼠标滑入滑出按压事件进行重绘,
	 * 注册按钮大小
	 */
	protected void createBaseItem()
	{
		if(name!=null&&!"".equals(name))
		{
			this.setName(name);
		}
		
		if(tooltip!=null&&!"".equals(tooltip))
		{
			this.setToolTipText(tooltip);
		}
		
		if(quickKey!=null)
		{
			this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(quickKey, name);
			this.getActionMap().put(name, new AbstractAction()
			{
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e)
				{//点击快捷键时模拟鼠标点击,以便响应toggle状态的改变
				  final EventQueue   eq   =   Toolkit.getDefaultToolkit().getSystemEventQueue();
					MouseEvent   pevent   =   new   MouseEvent(mine,MouseEvent.MOUSE_PRESSED,0,MouseEvent.BUTTON1_MASK,5,5,1,false);
					eq.postEvent(pevent);
					final Timer timer=new Timer();
					timer.schedule(new TimerTask()
					{
						@Override
						public void run()
						{
							MouseEvent   revent   =   new   MouseEvent(mine,MouseEvent.MOUSE_RELEASED,0,MouseEvent.BUTTON1_MASK,5,5,1,false);	  
							eq.postEvent(revent);
							timer.cancel();
							timer.purge();
						}
					}, 50);
				}
			});
		}
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				isRollover=false;
				isPressed=true;
				repaint();
			}
			@Override
			public void mouseEntered(MouseEvent e)
			{
				isRollover=true;
				isPressed=false;
				repaint();
			}
			@Override
			public void mouseExited(MouseEvent e)
			{
				isPressed=false;
				isRollover=false;
				repaint();
			}
		});
		
		setPaneSize();
		
	}
	
	/**
	 * SIMPLE模式下鼠标释放动作
	 */
	protected void createSimpleItem()
	{
		createBaseItem();
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				isPressed=false;
				isRollover=false;
				repaint();
				doAction(e);		
			}
		});
	}

	/**
	 * TOGGLE模式下鼠标释放动作
	 */
	protected void createToggleItem()
	{
		createBaseItem();
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				isRollover=false;
				isPressed=false;
				if(isToggled)
				{//按压则释放,释放则按压
					isToggled=false;
				}else{
					isToggled=true;
				}
				repaint();
				doAction(e);		
			}
		});
	}
	
	/**
	 * DROP_DOWN模式下鼠标释放动作
	 */
	protected void createDropDownItem()
	{
		createBaseItem();
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				isPressed=false;
				isRollover=false;
				if(e.getX()<size)
				{//在按钮图标上则触发事件,在下拉图标上则弹出菜单
					doAction(e);	
				}else{
					popup.show(mine, 0, size);
				}
				repaint();		
			}
		});
	}

	/**
	 * DROPICON模式下鼠标释放弹出菜单事件
	 */
	protected void createDropIcon()
	{
		createBaseItem();
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				isPressed=false;
				isRollover=false;
				popup.show(mine, 0, size);
				repaint();		
			}
		});
	}
	
	@Override
	public void paint(Graphics g)
	{
	 if(!isDropIcon)
	 {
    g.setColor(backgroundColor);//获取背景色
		g.clearRect(0, 0, size, size);//清除区域
		g.fillRect(0, 0, size, size);//填充背景色
			if(isDropDownModel)
			{
				g.clearRect(size, 0, dropsize, size);
				g.fillRect(size, 0, dropsize, size);
			}
		if(isRollover)
		{
			g.fill3DRect(0, 0, size, size, true);//实现凸起
			if(isDropDownModel)
			{
				g.fill3DRect(size, 0, dropsize, size, true);
			}
		}
		if(isPressed)
		{
			g.draw3DRect(0, 0, size-1, size-1, false);//实现凹陷
			if(isDropDownModel)
			{
				g.draw3DRect(size, 0, dropsize-1, size-1, false);
			}
		}
		if(isToggled)
		{//强化保持凹陷状态
			g.fill3DRect(0, 0, size, size, false);
			g.setColor(toggledColor);
			g.fillRect(2, 2, size-4, size-4);
		}
		//绘制图标
		g.drawImage(icon.getImage(), 2, 2, size-4, size-4, this);
		  if(isDropDownModel)
		  {//绘制下拉三角箭头
		  	g.setColor(dropIconColor);//获取箭头颜色
		  	g.fillPolygon(new int[]{size+dropsize/4, size+dropsize/4*3, size+dropsize/2},new int[]{size/2, size/2, size/2+dropsize/4} , 3);
		  } 
	  }else{//如果是DROPDOWN模式则仅画箭头,步骤相似
	  	g.setColor(backgroundColor);
			g.clearRect(0, 0, dropsize, size);
			g.fillRect(0, 0, dropsize, size);
			if(isRollover)
			{
				g.fill3DRect(0, 0, dropsize, size, true);
			}
			if(isPressed)
			{
				g.draw3DRect(0, 0, dropsize-1, size-1, false);
			}
			g.setColor(dropIconColor);
	  	g.fillPolygon(new int[]{dropsize/4, dropsize/4*3, dropsize/2},new int[]{size/2, size/2, size/2+dropsize/4} , 3);
	  }
	};
	
	/** 
	 * @return 如果按钮处于被压形态则返回true
	 */
	public boolean isToggleButtonPressed()
	{
		return isToggled;
	}
	
	/**
	 * 设置非图标区域背景色
	 */
	public JohnToolbarItem setBackgroundColor(Color color)
	{
		this.backgroundColor=color;
		this.setBackground(color);
		return this;
	}
	
	/**
	 * 设置TOGGLE模式下被按压形态的背景色
	 */
	public JohnToolbarItem setToggledColor(Color color)
	{
		this.toggledColor=color;
		return this;
	}
	
	/**
	 * DROP_DOWN,DROPICON模式下,设置下拉图标的颜色
	 */
	public JohnToolbarItem setDropIconColor(Color color)
	{
		this.dropIconColor=color;
		return this;
	}
	
	/**
	 * DROP_DOWN,DROPICON模式下,注册弹出菜单,以便在点下拉图标时显示
	 */
	public JohnToolbarItem setDropDown(JPopupMenu popup)
	{
		this.popup=popup;
		return this;
	}
	
	/**
	 * 设置工具栏中按钮占用的尺寸大小,除DROPICON模式下为长方形外,均为正方形
	 */
	public JohnToolbarItem setItemSize(int size)
	{
		this.size=size;
		setPaneSize();
		return this;
	}
	
	/**
	 * 计算下拉图标区域的宽度
	 */
	protected void computeDropSize()
	{
		int temp=size/2;
		for(int i=0;i<4;i++)
		{
			if(temp%4==0)
			{
				dropsize=temp;
			}else{
				temp++;
			}
		}
	}
	
	/**
	 * 给本组件注册组件大小
	 */
	protected void setPaneSize()
	{
		computeDropSize();
		if(isDropDownModel)
		{
			this.setPreferredSize(new Dimension(size+dropsize, size));
			this.setMaximumSize(new Dimension(size+dropsize, size));
			this.setMinimumSize(new Dimension(size+dropsize, size));
			this.setSize(new Dimension(size+dropsize, size));
		}
		else if(isDropIcon)
		{
			this.setPreferredSize(new Dimension(dropsize, size));
			this.setMaximumSize(new Dimension(dropsize, size));
			this.setMinimumSize(new Dimension(dropsize, size));
			this.setSize(new Dimension(dropsize, size));
		}
		else
		{
			this.setPreferredSize(new Dimension(size, size));
			this.setMaximumSize(new Dimension(size, size));
			this.setMinimumSize(new Dimension(size, size));
			this.setSize(new Dimension(size, size));
		}
	}
	
	/**
	 * 点击按钮要完成的主事件
	 * @param e 鼠标在按钮中按压后释放的事件
	 */
	public abstract void doAction(MouseEvent e);
}
