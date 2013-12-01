package com.johnsoft.library.util.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 全局快捷键类
 * @author john
 */
public class JohnShortcutManager implements AWTEventListener
{
	// 使用单态模式
	private static JohnShortcutManager instance = new JohnShortcutManager();
	
	// 快捷键与事件处理对象键值对
	private Map<String, ShortcutListener> listeners;
	
	// 某组件上发生了快捷键列表中的快捷键事件, 如果他的父组件在被忽略组件列表中, 则放弃这个事件.
	private Set<Component> ignoredComponents;
	
	// 保存键盘上键与它的ascii码对应
	// 如果以某键的ascii码为下标, 数组中此下标的值为true, 说明此键被按下了.
	// 当此键被释放开后, 数组中对应的值修改为false
	private boolean[] keys;
	
	private JohnShortcutManager() 
	{
		keys = new boolean[256];
		ignoredComponents = new HashSet<Component>();
		listeners = new HashMap<String, ShortcutListener>();
		// 只关心键盘事件
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
	}

	@Override
	public void eventDispatched(AWTEvent event) 
	{
		if (event.getClass() == KeyEvent.class) 
		{
			KeyEvent ke = (KeyEvent) event;
			if (ke.getID() == KeyEvent.KEY_PRESSED) 
			{
				keys[ke.getKeyCode()] = true;
				// 查找快捷键对应的处理对象, 然后调用事件处理函数
				String shortcut = searchShortcut();
				ShortcutListener l = listeners.get(shortcut);
				if (l != null && !isIgnored(event))
				{
					l.handle();
				}
			} 
			else if (ke.getID() == KeyEvent.KEY_RELEASED)
			{
				keys[ke.getKeyCode()] = false;
			}
		}
	}
	
	protected String searchShortcut()
	{ // 每个键之间用一个"."来隔开.
		// 例如ctr + x的对应值为"17.88."
		StringBuilder shortcut = new StringBuilder();
		for (int i = 0; i < keys.length; ++i)
		{
			if (keys[i]) 
			{
				shortcut.append(i).append(".");
			}
		}
		return shortcut.toString();
	}
	
	protected boolean isIgnored(AWTEvent event) 
	{	// 查找此快捷键事件是否要被抛弃
		if (!(event.getSource() instanceof Component)) 
		{
			return false;
		}
		boolean ignored = false;
		for(Component com = (Component) event.getSource(); com != null; com = com.getParent()) 
		{
			if (ignoredComponents.contains(com))
			{
				ignored = true;
				break;
			}
		}
		return ignored;
	}
	
	/**
	 * 采用单例,获取ShortcutManager实例
	 */
	public static JohnShortcutManager getInstance() 
	{
		return instance;
	}
	
	/**
	 * 移除指定快捷键监听器
	 * @param l 要移除的监听器
	 */
	public void removeShortcutListener(ShortcutListener l) 
	{
		String tempKey = null;	
		for (Map.Entry<String, ShortcutListener> e : listeners.entrySet())
		{	
			if (e.getValue().equals(l)) 
			{	
				tempKey = e.getKey();	
			}	
		}	
		listeners.remove(tempKey);	
	}
	
	/**
	 * 注册快捷键监听
	 * @param l 要增加的快捷键监听器
	 * @param keys 一组快捷键
	 */
	public void addShortcutListener(ShortcutListener l, int... keys) 
	{ // 快捷键的对应值按它们的键顺序大小来进行创建
		StringBuilder sb = new StringBuilder();	
		Arrays.sort(keys);
		for (int i = 0; i < keys.length; ++i)
		{
			if (0 < keys[i] && keys[i] < this.keys.length)
			{
				sb.append(keys[i]).append(".");
			} else {
				System.out.println("Key is not valid!");
				System.out.println("键值无效！");
				return;
			}
		}
		String shortcut = sb.toString();
		// 如果还不存在, 则加入
		if (listeners.containsKey(shortcut))
		{
			System.out.println("The shourt cut is already used!");
			System.out.println("快捷键已被注册！");
		} else {
			listeners.put(shortcut, l);
		}
	}
	
	/**
	 * 增加忽略组件,在此组件是快捷键失效
	 * @param com 要忽略的组件
	 */
	public void addIgnoredComponent(Component com) 
	{
		ignoredComponents.add(com);
	}
	
	/**
	 * 取消原忽略组件,在此组件是快捷键复效
	 * @param com 要取消忽略的组件
	 */
	public void removeDiscardComponent(Component com) 
	{
		ignoredComponents.remove(com);
	}
	
	/**
	 * 嵌套监听器接口,全局快捷键注册接口
	 */
	public static interface ShortcutListener
	{
		public void handle();
	}
	
//	public static void main(String[] args)
//	{
//		JFrame frame = new JFrame();
//		JButton button = new JButton("Button");
//		JTextArea textArea = new JTextArea();
//		JScrollPane scroller = new JScrollPane(textArea);
//		frame.add(button, BorderLayout.NORTH);
//		frame.add(scroller, BorderLayout.CENTER);
//
//		// 注册快捷键方法
//		JohnShortcutManager.getInstance().addShortcutListener(
//				new JohnShortcutManager.ShortcutListener()
//				{
//					public void handle()
//					{
//						System.out.println("Shift + I");
//					}
//				}, KeyEvent.VK_SHIFT, KeyEvent.VK_I);
//
//		JohnShortcutManager.getInstance().addShortcutListener(
//				new JohnShortcutManager.ShortcutListener()
//				{
//					public void handle()
//					{
//						System.out.println("Ctrl + Alt + M");
//					}
//				}, KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_M);
//
//		// 测试忽略文本输入里面的快捷键事件, 当按钮得到焦点时, 快捷键事件能正常发生
//		JohnShortcutManager.getInstance().addIgnoredComponent(textArea);
//
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(500, 500);
//		frame.setVisible(true);
//	}
	
}