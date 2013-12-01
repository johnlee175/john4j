package com.johnsoft.library.swing.component;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * 系统托盘图标工厂类
 * @author john
 */
public class JohnNotifyIconFactory 
{
	private TrayIcon trayIcon;//系统托盘图标

	/**
	 * 绑定事件：关闭窗口时询问是否最小化到托盘
	 * @param jframe
	 * @param image
	 * @param tooltip
	 */
	public  void installToSystemTray(final JFrame jframe,final Image image,final String tooltip)
	{
	
		jframe.addWindowListener(new WindowAdapter() 
  	{
  		public void windowClosing(WindowEvent e) 
  		{
  			if (SystemTray.isSupported()) 
  			{ 
  				Object[] objs=new Object[]{"最小化到托盘","退出程序","取消"};
  				int response=JOptionPane.showOptionDialog((JPanel)jframe.getContentPane(), 
  																									"请在以下可选的操作中抉择:", 
  																									"关闭窗口时", 
  																									JOptionPane.YES_NO_CANCEL_OPTION, 
  																									JOptionPane.PLAIN_MESSAGE, 
  																									null, objs ,objs[0]);
  				if(response==0)
  				{
  					jframe.setVisible(false);
    				showNotifyIcon(jframe,image,tooltip);
  				}
  				else if(response==1)
  				{
  					System.exit(0);
  				}
  				else
  				{
  					jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
  				}
  			}
  			else 
  			{
  				System.exit(0);
  			}
  		}
  	});
	}

	/**
	 * 初始化系统托盘图标并显示在托盘上
	 * @param jframe
	 * @param image
	 * @param tooltip
	 */
  public void showNotifyIcon(final JFrame jframe,Image image,String tooltip) 
  { 
  	//显示主窗口事件
  	ActionListener showListener = new ActionListener() 
   	{
   		public void actionPerformed(ActionEvent e)
   		{
   			jframe.setVisible(true);
   			int width=jframe.getSize().width;
   			int height=jframe.getSize().height;
   		  int screenWidth=Toolkit.getDefaultToolkit().getScreenSize().width;
			  int screenHeight=Toolkit.getDefaultToolkit().getScreenSize().height;
			  jframe.setBounds(screenWidth/2-width/2, screenHeight/2-height/2, width, height);
   			SystemTray.getSystemTray().remove(trayIcon);
   		}
   	};
   	//退出程序事件
   	ActionListener exitListener = new ActionListener() 
   	{
   		public void actionPerformed(ActionEvent e)
   		{
   			System.exit(0);
   		}
   	};
    
   	//托盘图标
//   	Image image = Toolkit.getDefaultToolkit().getImage(imgPath);
  	
  	//右键菜单
  	PopupMenu popup = new PopupMenu();
  	MenuItem showItem = new MenuItem("\u8BBE\u7F6E");
  	MenuItem exitItem = new MenuItem("\u9000\u51FA");
	  showItem.addActionListener(showListener);
	  exitItem.addActionListener(exitListener);
	  popup.add(showItem);
	  popup.add(exitItem);
	  
	  //创建托盘图标
	  trayIcon = new TrayIcon(image, "TrayIcon", popup);
	  trayIcon.setImageAutoSize(true);
	  //托盘提示
	  trayIcon.setToolTip(tooltip);
	  //双击显示主窗口
	  trayIcon.addActionListener(showListener);
	  
	  
	  //创建系统托盘
	  SystemTray tray = SystemTray.getSystemTray();
    try 
    {
    	tray.add(trayIcon);
    }
    catch (AWTException e) 
    {
    	e.printStackTrace();
    }
	}
} 
