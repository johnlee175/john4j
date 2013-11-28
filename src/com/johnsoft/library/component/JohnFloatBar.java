package com.johnsoft.library.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import sun.swing.ImageIconUIResource;

import com.johnsoft.library.util.JohnSwingUtilities;

public class JohnFloatBar extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private int vgap=5,hgap=5,itemSize=28;
	
	private int rollOverIdx=-1,pressIdx=-1;
	private Point dragFromPtr;
	private boolean hadDrag;
	private boolean hideMode;
	
	private JPopupMenu popupMenu;
	private ModuleItem[] items;
	
	public JohnFloatBar(ModuleItem[] items,JPopupMenu popupMenu)
	{
		this.items=items;
		this.popupMenu=popupMenu;
		setOpaque(false);
		installListeners();
	}
	
	protected void installListeners()
	{
		MouseAdapter adapter=new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton()==MouseEvent.BUTTON1)
				{
					if(isValidRollover((dragFromPtr=e.getPoint()).y))
					{
						int idx=(dragFromPtr.x-hgap)/itemSize;
						if(isSafeIndex(idx))
						{
							pressIdx=idx;
						}
					}
					repaint();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				int btn=e.getButton();
				if(btn==MouseEvent.BUTTON1)
				{
					dragFromPtr=null;
					setCursor(Cursor.getDefaultCursor());
					int idx=pressIdx;
					pressIdx=-1;
					repaint();
					if(hadDrag)
					{
						hadDrag=false;
						checkBoundsForHide();
					}else{
						if(isSafeIndex(idx))
						{
							action(idx);
						}
					}
				}
				else if(btn==MouseEvent.BUTTON3&&popupMenu!=null)
				{
					popupMenu.show(JohnFloatBar.this, e.getX()-popupMenu.getPreferredSize().width, 
							e.getY()-popupMenu.getPreferredSize().height);
				}
			}
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				if(hideMode)
				{
					moveTo(true);
				}
				rollOverIdx=-1;
				repaint();
			}
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if(dragFromPtr!=null)
				{
					hadDrag=true;
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					SwingUtilities.windowForComponent(JohnFloatBar.this).setLocation(e.getXOnScreen()-dragFromPtr.x, e.getYOnScreen()-dragFromPtr.y);
				}
			}
			@Override
			public void mouseMoved(MouseEvent e)
			{
				if(hideMode)
				{
					moveTo(false);
				}
				setCursor(Cursor.getDefaultCursor());
				int x=e.getX();
				if(isValidRollover(e.getY()))
				{
					int idx=(x-hgap)/itemSize;
					if(isSafeIndex(idx))
					{
						rollOverIdx=idx;
					}
				}else{
					rollOverIdx=-1;
				}
				if(isSafeIndex(rollOverIdx))
				{
					setToolTipText((String)items[rollOverIdx].getTooltip());
					if(items[rollOverIdx].isEnabled())
					{
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
				}
				repaint();
			}
		};
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		drawBackground(g2d);
		if(isSafeIndex(rollOverIdx)&&items[rollOverIdx].isEnabled())
		{
			g2d.setPaint(new LinearGradientPaint(new Point(hgap+rollOverIdx*itemSize,vgap),
					new Point(hgap+rollOverIdx*itemSize,vgap*2+itemSize), new float[]{0.1f,0.6f}, new Color[]{Color.YELLOW,Color.ORANGE}));
			g2d.fillRoundRect(hgap+rollOverIdx*itemSize, vgap, itemSize, itemSize, 10, 10);
		}
		for(int i=0,offset=hgap;i<items.length;i++,offset+=itemSize)
		{
			Image img=items[i].getIcon().getImage();
			if(items[i].isEnabled())
			{
				g2d.drawImage(img, offset, vgap, itemSize, itemSize, null);
			}else{
				g2d.drawImage(new ImageIconUIResource(GrayFilter.createDisabledImage(img)).getImage(), offset, vgap, itemSize, itemSize, null);
			}
		}
		if(isSafeIndex(pressIdx)&&items[pressIdx].isEnabled())
		{
			g2d.setColor(new Color(200,200,0,100));
			g2d.fillRoundRect(hgap+pressIdx*itemSize, vgap, itemSize, itemSize, 10, 10);
		}
	}
	
	private final boolean isSafeIndex(int idx)
	{
		return idx>-1&&idx<items.length;
	}
	
	private final boolean isValidRollover(int y)
	{
		return y>vgap&&y<vgap+itemSize;
	}
	
	protected void checkBoundsForHide()
	{
		int w=getWidth();
		int x=getLocationOnScreen().x;
		if(x>0)
		{
			float over=(x+w)-JohnSwingUtilities.getScreenSize().width;
			if(over/w>0.5)
			{
				hideMode=true;
				moveTo(true);
				return;
			}
		}else{
			if((float)x/w<-0.5)
			{
				hideMode=true;
				moveTo(true);
				return;
			}
		}
		hideMode=false;
	}
	
	protected void moveTo(boolean hide)
	{
		int x=getLocationOnScreen().x;
		Window wnd=SwingUtilities.windowForComponent(JohnFloatBar.this);
		int sw=JohnSwingUtilities.getScreenSize().width;
		if(hide)
		{
			if(x<sw/2)
			{
				wnd.setLocation(-wnd.getWidth()+8, wnd.getY());
			}else{
				wnd.setLocation(sw-8, wnd.getY());
			}
		}else{
			if(x<sw/2)
			{
				wnd.setLocation(-8, wnd.getY());
			}else{
				wnd.setLocation(sw-wnd.getWidth()+8, wnd.getY());
			}
		}
	}

	protected void drawBackground(Graphics2D g2d){
	}
	
	public void action(int idx)
	{
		if(items[idx].isEnabled())
		{
			items[idx].action();
		}
	}
	
	public void setGap(int hgap,int vgap)
	{
		this.hgap = hgap;
		this.vgap = vgap;
	}

	public int getItemSize()
	{
		return itemSize;
	}

	public void setItemSize(int itemSize)
	{
		this.itemSize = itemSize;
	}
	
	public static abstract class ModuleItem
	{
		private String commandName;
		private String tooltip;
		private ImageIcon icon;
		
		public ModuleItem(String commandName,String tooltip,ImageIcon icon)
		{
			 if(commandName==null||tooltip==null||icon==null) 
				 throw new IllegalArgumentException("The JohnFloatBar constructor doesn't work if any param valued null!");
			 this.commandName=commandName;
			 this.tooltip=tooltip;
			 this.icon=icon;
		}
		
		public abstract void action();
		
		public abstract boolean isEnabled();
		
		public String getCommandName()
		{
			return commandName;
		}
		public String getTooltip()
		{
			return tooltip;
		}
		public ImageIcon getIcon()
		{
			return icon;
		}
	}
}
