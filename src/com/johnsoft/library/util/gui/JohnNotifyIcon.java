package com.johnsoft.library.util.gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

/**
 * 系统托盘图标类
 * 
 * @author john
 */
public class JohnNotifyIcon
{
	protected TrayIcon trayIcon;// 系统托盘图标
	protected Window wnd; //托盘图标绑定的应用程序主窗口
	protected boolean inTray;//是否现在以托盘图标形式在托盘中
	
	// 显示主窗口事件
	protected ActionListener showListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			hideNotifyIcon();
			wnd.setVisible(true);
		}
	};
	// 退出程序事件
	protected ActionListener exitListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			hideNotifyIcon();
			System.exit(0);
		}
	};
	
	/**
	 * @param wnd 托盘图标绑定的应用程序主窗口,不能为null,请区别主窗口有图标
	 */
	public JohnNotifyIcon(Window wnd)
	{
		this.wnd=wnd;
	}
	
	protected PopupMenu getPopupMenu()
	{
		// 右键菜单
		PopupMenu popup = new PopupMenu();
		MenuItem showItem = new MenuItem("\u8BBE\u7F6E");
		MenuItem exitItem = new MenuItem("\u9000\u51FA");
		showItem.addActionListener(showListener);
		exitItem.addActionListener(exitListener);
		popup.add(showItem);
		popup.add(exitItem);
		return popup;
	}

	/**
	 * 绑定事件：关闭窗口时询问是否最小化到托盘
	 * @param tooltip 鼠标放在托盘图标上的提示
	 */
	public void installToSystemTray(final String tooltip)
	{
		wnd.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (SystemTray.isSupported())
				{
					Object[] objs = new Object[] { "最小化到托盘", "退出程序", "取消" };
					int response = JOptionPane.showOptionDialog(
							null, "请在以下可选的操作中抉择:",
							"关闭窗口时", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, objs, objs[0]);
					if (response == 0)
					{
						wnd.setVisible(false);
						showNotifyIcon(tooltip);
					} else if (response == 1)
					{
						System.exit(0);
					} else
					{
						try
						{
							Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent();
						} catch (InterruptedException e1)
						{
							e1.printStackTrace();
						}
					}
				} else
				{
					System.exit(0);
				}
			}
		});
	}

	/**
	 * 初始化系统托盘图标并显示在托盘上
	 * @param tooltip 鼠标放在托盘图标上的提示
	 */
	public void showNotifyIcon(String tooltip)
	{
		// 创建托盘图标
		trayIcon = new TrayIcon(wnd.getIconImages().get(0), tooltip, getPopupMenu());
		trayIcon.setImageAutoSize(true);
		// 双击显示主窗口
		trayIcon.addActionListener(showListener);
		try
		{// 创建系统托盘
			SystemTray.getSystemTray().add(trayIcon);
			inTray=true;
		} catch (AWTException e)
		{
			e.printStackTrace();
		}
	}
	
	/**销毁托盘图标*/
	public void hideNotifyIcon()
	{
		SystemTray.getSystemTray().remove(trayIcon);
		inTray=false;
	}
	
	/**是否现在以托盘图标形式在托盘中*/
	public boolean isInTray()
	{
		return inTray;
	}
}
