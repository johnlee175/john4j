package com.johnsoft.library.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

/**
 * 设计一种与屏幕分辨率无关的尺寸方位的管理类
 * 注意:未做任何null判断,请确保此类所有方法的所有参数为非null
 * @author 李哲浩
 */
public class JohnResolutionManager
{
	/**逻辑屏幕尺寸(逻辑比例单位,非像素单位),这里所指的是不含任务栏的屏幕尺寸*/
	public static final int LOGIC_SCREEN_WIDTH=1000,LOGIC_SCREEN_HEIGHT=1000;
	
	/*private JohnResolutionManager(){
	}
	
	private static class JohnResolutionHandler
	{
		public static final JohnResolutionManager instance=new JohnResolutionManager();
	}
	
	public static JohnResolutionManager getSharedInstance()
	{
		return JohnResolutionHandler.instance;
	}*/
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的逻辑宽和高*/
	public static void setSize(Component c,int w,int h)
	{
		c.setSize(LSizeToDSize(w, h));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的逻辑宽和高*/
	public static void setMaximumSize(Component c,int w,int h)
	{
		c.setMaximumSize(LSizeToDSize(w, h));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的逻辑宽和高*/
	public static void setPreferredSize(Component c,int w,int h)
	{
		c.setPreferredSize(LSizeToDSize(w, h));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的逻辑宽和高*/
	public static void setMinimumSize(Component c,int w,int h)
	{
		c.setMinimumSize(LSizeToDSize(w, h));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static void setSize(Component c,Dimension d)
	{
		c.setSize(LSizeToDSize(d));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static void setMaximumSize(Component c,Dimension d)
	{
		c.setMaximumSize(LSizeToDSize(d));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static void setPreferredSize(Component c,Dimension d)
	{
		c.setPreferredSize(LSizeToDSize(d));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static void setMinimumSize(Component c,Dimension d)
	{
		c.setMinimumSize(LSizeToDSize(d));
	}
	
	/**获取假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static Dimension getSize(Component c)
	{
		 return DSizeToLSize(c.getSize());
	}
	
	/**获取假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static Dimension getMaximumSize(Component c)
	{
		return DSizeToLSize(c.getMaximumSize());
	}
	
	/**获取假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static Dimension getPreferredSize(Component c)
	{
		return DSizeToLSize(c.getPreferredSize());
	}
	
	/**获取假定在屏幕逻辑宽1000高1000的情况下的组件逻辑尺寸*/
	public static Dimension getMinimumSize(Component c)
	{
		return DSizeToLSize(c.getMinimumSize());
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的组件逻辑左上角x坐标和y坐标*/
	public static void setLocation(Component c,int x,int y)
	{
		c.setLocation(LPointToDPoint(x, y));
	}
	
	/**设置假定在屏幕逻辑宽1000高1000的情况下的组件逻辑左上角坐标*/
	public static void setLocation(Component c,Point p)
	{
		c.setLocation(LPointToDPoint(p));
	}
	
	/**获取假定在屏幕逻辑宽1000高1000的情况下的组件逻辑左上角坐标*/
	public static Point getLocation(Component c)
	{
		return DPointToLPoint(c.getLocation());
	}
	
	/**获取假定在屏幕逻辑宽1000高1000的情况下的组件逻辑左上角坐标*/
	public static Point getLocationOnScreen(Component c)
	{
		return DPointToLPoint(c.getLocationOnScreen());
	}
	
	/**
	 * @param lWidth 逻辑物件尺寸的宽,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @param lHeight 逻辑物件尺寸的高,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备尺寸,像素单位计
	 */
	public static Dimension LSizeToDSize(int lWidth,int lHeight)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(lWidth*dScreenSize.width/LOGIC_SCREEN_WIDTH, lHeight*dScreenSize.height/LOGIC_SCREEN_HEIGHT);
	}
	
	/**
	 * @param lSize 逻辑物件尺寸,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备尺寸,像素单位计
	 */
	public static Dimension LSizeToDSize(Dimension lSize)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(lSize.width*dScreenSize.width/LOGIC_SCREEN_WIDTH, lSize.height*dScreenSize.height/LOGIC_SCREEN_HEIGHT);
	}
	
	/**
	 * @param lWidth 逻辑物件尺寸的宽
	 * @param lHeight 逻辑物件尺寸的高
	 * @param lScreenWidth 逻辑屏幕尺寸的宽,这里所指的是不含任务栏的屏幕尺寸
	 * @param lScreenHeight 逻辑屏幕尺寸的高,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备尺寸,像素单位计
	 */
	public static Dimension LSizeToDSize(int lWidth,int lHeight,int lScreenWidth,int lScreenHeight)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(lWidth*dScreenSize.width/lScreenWidth, lHeight*dScreenSize.height/lScreenHeight);
	}
	
	/**
	 * @param lSize 逻辑物件尺寸
	 * @param lScreenSize 逻辑屏幕尺寸,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备尺寸,像素单位计
	 */
	public static Dimension LSizeToDSize(Dimension lSize,Dimension lScreenSize)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(lSize.width*dScreenSize.width/lScreenSize.width, lSize.height*dScreenSize.height/lScreenSize.height);
	}
	
	/**
	 * @param dWidth 设备物件尺寸的宽,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @param dHeight 设备物件尺寸的高,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑尺寸,逻辑单位计
	 */
	public static Dimension DSizeToLSize(int dWidth,int dHeight)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(dWidth*LOGIC_SCREEN_WIDTH/dScreenSize.width, dHeight*LOGIC_SCREEN_HEIGHT/dScreenSize.height);
	}
	
	/**
	 * @param dSize 设备物件尺寸,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑尺寸,逻辑单位计
	 */
	public static Dimension DSizeToLSize(Dimension dSize)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(dSize.width*LOGIC_SCREEN_WIDTH/dScreenSize.width, dSize.height*LOGIC_SCREEN_HEIGHT/dScreenSize.height);
	}
	
	/**
	 * @param dWidth 设备物件尺寸的宽
	 * @param dHeight 设备物件尺寸的高
	 * @param lScreenWidth 逻辑屏幕尺寸的宽,这里所指的是不含任务栏的屏幕尺寸
	 * @param lScreenHeight 逻辑屏幕尺寸的高,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑尺寸,逻辑单位计
	 */
	public static Dimension DSizeToLSize(int dWidth,int dHeight,int lScreenWidth,int lScreenHeight)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(dWidth*lScreenWidth/dScreenSize.width, dHeight*lScreenHeight/dScreenSize.height);
	}
	
	/**
	 * @param dSize 设备物件尺寸
	 * @param lScreenSize 逻辑屏幕尺寸,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑尺寸,逻辑单位计
	 */
	public static Dimension DSizeToLSize(Dimension dSize,Dimension lScreenSize)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Dimension(dSize.width*lScreenSize.width/dScreenSize.width, dSize.height*lScreenSize.height/dScreenSize.height);
	}

	/**
	 * @param lx 逻辑物件左上角x坐标,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @param ly 逻辑物件左上角y坐标,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备左上角坐标,像素单位计
	 */
	public static Point LPointToDPoint(int lx,int ly)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(lx*dScreenSize.width/LOGIC_SCREEN_WIDTH, ly*dScreenSize.height/LOGIC_SCREEN_HEIGHT);
	}
	
	/**
	 * @param lPoint 逻辑物件左上角坐标,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备左上角坐标,像素单位计
	 */
	public static Point LPointToDPoint(Point lPoint)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(lPoint.x*dScreenSize.width/LOGIC_SCREEN_WIDTH, lPoint.y*dScreenSize.height/LOGIC_SCREEN_HEIGHT);
	}
	
	/**
	 * @param lx 逻辑物件左上角x坐标
	 * @param ly 逻辑物件左上角y坐标
	 * @param lScreenWidth 逻辑屏幕尺寸的宽,这里所指的是不含任务栏的屏幕尺寸
	 * @param lScreenHeight 逻辑屏幕尺寸的高,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备左上角坐标,像素单位计
	 */
	public static Point LPointToDPoint(int lx,int ly,int lScreenWidth,int lScreenHeight)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(lx*dScreenSize.width/lScreenWidth, ly*dScreenSize.height/lScreenHeight);
	}
	
	/**
	 * @param lPoint 逻辑物件左上角坐标
	 * @param lScreenSize 逻辑屏幕尺寸,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的设备左上角坐标,像素单位计
	 */
	public static Point LPointToDPoint(Point lPoint,Dimension lScreenSize)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(lPoint.x*dScreenSize.width/lScreenSize.width, lPoint.y*dScreenSize.height/lScreenSize.height);
	}
	
	/**
	 * @param dx 设备物件左上角x坐标,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @param dy 设备物件左上角y坐标,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑左上角坐标,逻辑单位计
	 */
	public static Point DPointToLPoint(int dx,int dy)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(dx*LOGIC_SCREEN_WIDTH/dScreenSize.width, dy*LOGIC_SCREEN_HEIGHT/dScreenSize.height);
	}
	
	/**
	 * @param dPoint 设备物件左上角坐标,假定逻辑屏幕尺寸统一为1000*1000,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑左上角坐标,逻辑单位计
	 */
	public static Point DPointToLPoint(Point dPoint)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(dPoint.x*LOGIC_SCREEN_WIDTH/dScreenSize.width, dPoint.y*LOGIC_SCREEN_HEIGHT/dScreenSize.height);
	}
	
	/**
	 * @param dx 设备物件左上角x坐标
	 * @param dy 设备物件左上角y坐标
	 * @param lScreenWidth 逻辑屏幕尺寸的宽,这里所指的是不含任务栏的屏幕尺寸
	 * @param lScreenHeight 逻辑屏幕尺寸的高,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑左上角坐标,逻辑单位计
	 */
	public static Point DPointToLPoint(int dx,int dy,int lScreenWidth,int lScreenHeight)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(dx*lScreenWidth/dScreenSize.width, dy*lScreenHeight/dScreenSize.height);
	}
	
	/**
	 * @param dPoint 设备物件左上角坐标
	 * @param lScreenSize 逻辑屏幕尺寸,这里所指的是不含任务栏的屏幕尺寸
	 * @return 与屏幕分辨率和当前开发环境无关的你给定合适尺寸相当的逻辑左上角坐标,逻辑单位计
	 */
	public static Point DPointToLPoint(Point dPoint,Dimension lScreenSize)
	{
		Dimension dScreenSize=getDeviceScreenSize();
		return new Point(dPoint.x*lScreenSize.width/dScreenSize.width, dPoint.y*lScreenSize.height/dScreenSize.height);
	}
	
	/**获取默认逻辑屏幕尺寸,返回宽高各1000逻辑单位,即把屏幕宽高各分成1000份,这里所指的是不含任务栏的屏幕尺寸*/
	public static Dimension getLogicScreenSize()
	{
		return new Dimension(LOGIC_SCREEN_WIDTH, LOGIC_SCREEN_HEIGHT);
	}
	
	/**获取设备屏幕尺寸,即当前程序的运行环境的显示器的屏幕尺寸,单位为像素,这里所指的是不含任务栏的屏幕尺寸*/
	public static Dimension getDeviceScreenSize()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
	}
	
}
