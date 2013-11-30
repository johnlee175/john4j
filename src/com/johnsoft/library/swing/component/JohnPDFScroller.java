package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.johnsoft.library.util.gui.JohnSwingUtilities;

/**
 * 实现PDF滚轴滚动
 * @author 李哲浩
 */
public class JohnPDFScroller
{
	protected static int SPEED_FACTOR=200;
	public static Image icon=JohnSwingUtilities.getImageResource("wheel.png");
	public static Image cursor=JohnSwingUtilities.getImageResource("wheelCursor.gif");
	
	protected JScrollPane jsp;
	protected JScrollBar jsb;

	protected Timer timer;

	private boolean isTrigger;
	private int oldValue;
	private int direction;
	
	private Component oldGlass;

	private JohnPDFScroller(JScrollPane jsp)
	{
		this(jsp,jsp);
	}
	
	private JohnPDFScroller(JScrollPane jsp,Component comp)
	{
		this.jsp = jsp;
		this.jsb = jsp.getVerticalScrollBar();
		createTimer();
		installListeners(comp);
	}
	
	public static void installPDFScroller(JScrollPane jsp)
	{
		new JohnPDFScroller(jsp);
	}
	
	public static void installPDFScroller(JScrollPane jsp,Component comp)
	{
		new JohnPDFScroller(jsp,comp);
	}

	protected void createTimer()
	{
		timer = new Timer(10, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				 scroll(jsp, SwingConstants.VERTICAL, direction, false, SPEED_FACTOR);
			}
		});
	}
	
	protected void installListeners(Component comp)
	{
		comp.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if(e.getButton()==MouseEvent.BUTTON2)
				{
					if(!isTrigger)
					{
						isTrigger=true;
						drawAnchor(e.getLocationOnScreen());
						oldValue=e.getY();
						timer.start();
					}else{
						unTrigger();
						
					}
					return;
				}
				unTrigger();
			}
		});
		comp.addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				if(isTrigger)
				{
					direction=e.getY()-oldValue;
				}
				Component comp=JohnSwingUtilities.getGlassPane(jsp);
				if(comp instanceof JohnPDFGlass)
				{
					JohnPDFGlass glass=(JohnPDFGlass)comp;
					Point p=e.getPoint();
					p=SwingUtilities.convertPoint(jsp, p, glass);
					glass.checkAndChangeCursor(p);
				}
			}
		});
	}

	private void scroll(JScrollPane scrollpane, int orientation, int direction,
			boolean block,int speedFactor)
	{
		JViewport vp = scrollpane.getViewport();
		Component view;
		if (vp != null && (view = vp.getView()) != null)
		{
			Rectangle visRect = vp.getViewRect();
			Dimension vSize = view.getSize();
			int amount;

			if (view instanceof Scrollable)
			{
				if (block)
				{
					amount = ((Scrollable) view).getScrollableBlockIncrement(
							visRect, orientation, direction);
				} else
				{
					amount = ((Scrollable) view).getScrollableUnitIncrement(
							visRect, orientation, direction);
				}
			} else
			{
				if (block)
				{
					if (orientation == SwingConstants.VERTICAL)
					{
						amount = visRect.height;
					} else
					{
						amount = visRect.width;
					}
				} else
				{
					amount = 10;
				}
			}
			if (orientation == SwingConstants.VERTICAL)
			{
				visRect.y += (amount * direction)/speedFactor;
				if ((visRect.y + visRect.height) > vSize.height)
				{
					visRect.y = Math.max(0, vSize.height - visRect.height);
					postEOFMessage();
				} else if (visRect.y < 0)
				{
					visRect.y = 0;
					postBOFMessage();
				}
			} else
			{
				if (scrollpane.getComponentOrientation().isLeftToRight())
				{
					visRect.x += (amount * direction)/speedFactor;
					if ((visRect.x + visRect.width) > vSize.width)
					{
						visRect.x = Math.max(0, vSize.width - visRect.width);
						postEOFMessage();
					} else if (visRect.x < 0)
					{
						visRect.x = 0;
						postBOFMessage();
					}
				} else
				{
					visRect.x -= (amount * direction)/speedFactor;
					if (visRect.width > vSize.width)
					{
						visRect.x = vSize.width - visRect.width;
						postBOFMessage();
					} else
					{
						visRect.x = Math.max(0, Math.min(vSize.width
								- visRect.width, visRect.x));
						postEOFMessage();
					}
				}
			}
			vp.setViewPosition(visRect.getLocation());
		}
	}
	
	protected void postEOFMessage()
	{
		unTrigger();
	}
	
	protected void postBOFMessage()
	{
		unTrigger();
	}
	
	protected void drawAnchor(final Point p)
	{
		SwingUtilities.convertPointFromScreen(p, JohnSwingUtilities.getGlassPane(jsp));
		oldGlass=JohnSwingUtilities.getGlassPane(jsp);
		JohnSwingUtilities.setGlassPane(jsp, new JohnPDFGlass(p), true);
	}
	
	protected void unTrigger()
	{
		isTrigger=false;
		if(timer.isRunning())
		{
			timer.stop();
		}
		
		oldValue=0;
		direction=0;
		if(oldGlass!=null&&JohnSwingUtilities.getGlassPane(jsp)!=oldGlass)
		{
			JohnSwingUtilities.setGlassPane(jsp, oldGlass, false);
		}
	}
	
	protected class JohnPDFGlass extends JComponent
	{
		private static final long serialVersionUID = 1L;
		
		private Rectangle rect;
		private Point p;
		
		public JohnPDFGlass(Point p)
		{
			this.p=p;
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			if(icon==null)
			{
				int w=40;
				int h=40;
				g.setColor(Color.GRAY);
				g.fillOval(p.x-w/2, p.y-h/2, w, h);
				rect=new Rectangle(p.x-w/2, p.y-h/2, w, h);
			}else{
				int w=icon.getWidth(null);
				int h=icon.getHeight(null);
				g.drawImage(icon, p.x-w/2, p.y-h/2, null);
				rect=new Rectangle(p.x-w/2, p.y-h/2, w, h);
			}
		}
		
		public void checkAndChangeCursor(Point p)
		{
			if(rect!=null&&rect.contains(p))
			{
				Cursor cs=Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(16,16), "wheelCursor");
				setCursor(cs);
				return;
			}
			setCursor(Cursor.getDefaultCursor());
		}
	};

}
