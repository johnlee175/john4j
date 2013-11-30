package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.sun.awt.AWTUtilities;

/**
 * loading界面类,由单独的dialog探索实现
 * @author 李哲浩
 */
public class JohnLoadingGlass extends JComponent implements WindowListener,ComponentListener
{
	private static final long serialVersionUID = 1L;
	
	public static ImageIcon icon=new ImageIcon("images/loading.gif");
	
	protected JComponent comp;//被绑定对象
	protected JDialog dialog;//loading界面
	protected Timer timer;
	protected boolean isLoading=false;
	protected long oldMillSecd;//记录定时器启动时间
	protected long overMillSecd;//超时时间,如果当前时间相对于定时器启动时间大于超时时间,将停止计时,并调用超时动作
	protected ActionListener al;//超时动作
	
	/**
	 * @param comp 被绑定的组件,将覆盖此组件,并以此顶级窗口为父窗口
	 * @param overMillSecd 超时设置,如果不用,可以传入-1;
	 * @param al 超时时的动作,如果不用,可以传入null;
	 */
	public JohnLoadingGlass(JComponent comp,long overMillSecd,ActionListener al)
	{
		if(comp==null)
		{
			throw new NullPointerException("glass binded by null");
		}
		this.comp=comp;
		this.overMillSecd=overMillSecd;
		this.al=al;
	}
	
	/**
	 * @param comp 被绑定的组件,将覆盖此组件,并以此顶级窗口为父窗口
	 */
	public JohnLoadingGlass(JComponent comp)
	{
		if(comp==null)
		{
			throw new NullPointerException("glass binded by null");
		}
		this.comp=comp;
	}
	
	protected void createDialog()
	{
		Window wnd=SwingUtilities.windowForComponent(comp);
		dialog=new JDialog(wnd);
		dialog.setUndecorated(true);
		AWTUtilities.setWindowOpaque(dialog, false);
		dialog.setContentPane(this);
		dialog.setSize(comp.getSize());
		dialog.setLocationRelativeTo(wnd);
	}
	
	protected void createTimer()
	{
		timer=new Timer(100, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(dialog!=null)
				{
					dialog.repaint();
				}
				if(overMillSecd>0&&oldMillSecd>0&&overMillSecd<System.currentTimeMillis()-oldMillSecd)
				{
					destroy();
					if(al!=null)
					{
					   al.actionPerformed(e);
					}
				}
			}
		});
	}
	
	protected void installListeners()
	{
		Container container=comp.getTopLevelAncestor();
		comp.addComponentListener(this);
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			frame.addComponentListener(this);
			frame.addWindowListener(this);
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			dialog.addComponentListener(this);
			dialog.addWindowListener(this);
		}
		else
		{
			throw new IllegalArgumentException("just JFrame and JDialog is support at present");
		}
	}
	
	protected void unInstallListeners()
	{
		Container container=comp.getTopLevelAncestor();
		comp.removeComponentListener(this);
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			frame.removeComponentListener(this);
			frame.removeWindowListener(this);
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			dialog.removeComponentListener(this);
			dialog.removeWindowListener(this);
		}
	}
	
	//阻止其他窗口获得焦点导致的隐藏
	protected void stillShowing()
	{
		if(isLoading&&dialog!=null&&comp!=null)
		{
			dialog.setSize(comp.getSize());
			dialog.setLocation(comp.getLocationOnScreen());
			dialog.setVisible(true);
		}
	}
	
	//绑定窗口的隐藏同被绑定的对象的隐藏一致
	protected void hiddenWithBindings()
	{
		if(dialog!=null)
			dialog.setVisible(false);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Rectangle r=comp.getBounds();
		int w=icon.getIconWidth();
		int h=icon.getIconHeight();
		int x=r.width>w?((r.x+r.width-w)/2):r.x;
		int y=r.height>h?((r.y+r.height-h)/2):r.y;
		g.setColor(new Color(255,255,255,75));
		g.fillRect(r.x, r.y, r.width, r.height);
		g.drawImage(icon.getImage(), x, y, w, h, null);
	}
	
	/**
	 * 隐藏loading
	 */
	public void destroy()
	{
		isLoading=false;
		unInstallListeners();
		if(dialog!=null&&(dialog.isVisible()||dialog.isShowing()))
		{
		    dialog.setVisible(false);
		    dialog.dispose();
		}
		dialog=null;
		if(timer!=null)
		{
			timer.stop();
		}
		timer=null;
	}
	
	/**
	 * 展示loading
	 */
	public void show()
	{
		isLoading=true;
		if(dialog==null)
		{
			createDialog();
		}
		if(timer==null)
		{
			createTimer();
		}
		installListeners();
		dialog.setVisible(true);
		timer.start();
		oldMillSecd=System.currentTimeMillis();
	}
	
	/**是否还在加载*/
	public boolean isLoading()
	{
		return isLoading;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		stillShowing();
	}
	@Override
	public void componentMoved(ComponentEvent e)
	{
		stillShowing();
	}
	@Override
	public void componentShown(ComponentEvent e)
	{	
		stillShowing();
	}
	@Override
	public void componentHidden(ComponentEvent e)
	{	
		hiddenWithBindings();
	}
	@Override
	public void windowClosed(WindowEvent e)
	{
		hiddenWithBindings();
	}
	@Override
	public void windowIconified(WindowEvent e)
	{
		hiddenWithBindings();
	}
	@Override
	public void windowDeiconified(WindowEvent e)
	{
		stillShowing();
	}
	@Override
	public void windowActivated(WindowEvent e)
	{
		stillShowing();
	}
	@Override
	public void windowOpened(WindowEvent e){	
	}
	@Override
	public void windowClosing(WindowEvent e){	
	}
	@Override
	public void windowDeactivated(WindowEvent e){	
	}

}
