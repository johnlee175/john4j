package com.johnsoft.library.effect;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;


/**
 * 此类用来使组件或组件所在的窗口可以拖动
 * @author 李哲浩
 */
public class MovableObject extends MouseAdapter
{
	protected Cursor moveWindowCursor=Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	protected Cursor moveComponentCursor=DragSource.DefaultMoveDrop;
	protected Cursor oldCursor=Cursor.getDefaultCursor();
	
	private Component c;
	private Shape shape;
	private Point point;
	
	private boolean canMovable=true;
	private boolean moveWindow=true;
	private boolean useDefaultCursor=true;
	
	/**
	 * @param c 要移动的组件或要移动的窗口内的触发组件,确保不为null
	 * @param shape 该组件的拖动热点区,仅在拖动热点区可拖动,为null时将视组件大小区域为热点
	 */
	public MovableObject(Component c, Shape shape)
	{
		 this.c=c;
		 this.shape=shape;
		 installListeners();
	}
	
	protected void installListeners()
	{
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
	}
	
	protected void uninstallListeners()
	{
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		point=e.getPoint();
		oldCursor=c.getCursor();
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		 c.setCursor(oldCursor);
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(canMovable&&(shape==null?true:shape.contains(e.getPoint())))
		{
			if(moveWindow)
			{
				if(useDefaultCursor)
				{
					c.setCursor(moveWindowCursor);
				}
				SwingUtilities.windowForComponent(c).setLocation(e.getXOnScreen()-point.x, e.getYOnScreen()-point.y);
			}else{
				if(useDefaultCursor)
				{
					c.setCursor(moveComponentCursor);
				}
				c.setLocation(e.getX()-point.x, e.getY()-point.y);
			}
		}else{
			c.setCursor(oldCursor);
		}
	}

	public boolean canMovable()
	{
		return canMovable;
	}

	/**设置是否能够移动,默认为true*/
	public void canMovable(boolean canMovable)
	{
		this.canMovable = canMovable;
	}

	public boolean moveWindow()
	{
		return moveWindow;
	}

	/**设置是移动窗口本身的位置还是移动组件在窗口内的位置,默认为移动窗口*/
	public void moveWindow(boolean moveWindow)
	{
		this.moveWindow = moveWindow;
	}

	public boolean useDefaultCursor()
	{
		return useDefaultCursor;
	}

	/**如果为true,则当移动的是窗口本身时是十字移动光标,而移动组件时是文件移动光标,注意,仅限于热点区域,热点区外为箭头光标,如果为false,不做任何光标处理*/
	public void useDefaultCursor(boolean useDefaultCursor)
	{
		this.useDefaultCursor = useDefaultCursor;
	}
	
}
