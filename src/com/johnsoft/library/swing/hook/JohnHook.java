package com.johnsoft.library.swing.hook;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.POINT;

/**
 * 全局键盘钩子类
 * @author 李哲浩
 */
public abstract class JohnHook
{
	public static final int VK_ESCAPE = 27;
	public static final int VK_F1 = 112;
	public static final int VK_F2 = 113;
	public static final int VK_F3 = 114;
	public static final int VK_F4 = 115;
	public static final int VK_F5 = 116;
	public static final int VK_F6 = 117;
	public static final int VK_F7 = 118;
	public static final int VK_F8 = 119;
	public static final int VK_F9 = 120;
	public static final int VK_F10 = 121;/**系统键,不宜用作非全局快捷键*/
	public static final int VK_F11 = 122;
	public static final int VK_F12 = 123;

	public static final int VK_BACK_QUOTE = 192;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_0 = 48;
	public static final int VK_1 = 49;
	public static final int VK_2 = 50;
	public static final int VK_3 = 51;
	public static final int VK_4 = 52;
	public static final int VK_5 = 53;
	public static final int VK_6 = 54;
	public static final int VK_7 = 55;
	public static final int VK_8 = 56;
	public static final int VK_9 = 57;
	public static final int VK_MINUS = 189;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_EQUALS = 187;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_BACK_SPACE = 8;/**old, java风格*/
	public static final int VK_BACK = 8;/**new, windows风格*/
	public static final int VK_TAB = 9;
	public static final int VK_CAPS_LOCK = 20;/**old, java风格*/
	public static final int VK_CAPITAL = 20;/**new, windows风格*/
	public static final int VK_SHIFT_LEFT = 160;/**old, java风格*/
	public static final int VK_LSHIFT = 160;/**new, windows风格*/
	public static final int VK_CONTROL_LEFT = 162;/**old, java风格*/
	public static final int VK_LCONTROL = 162;/**new, windows风格*/
	public static final int VK_WINDOWS_LEFT = 91;/**old, java风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_LWIN = 91;/**new, windows风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_ALT_LEFT = 164;/**old, java风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_LMENU = 164;/**new, windows风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_SPACE = 32;
	public static final int VK_ALT_RIGHT = 165;/**old, java风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_RMENU = 165;/**new, windows风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_WINDOWS_RIGHT = 92;/**old, java风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_RWIN = 92;/**new, windows风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_POPUPMENU = 93;/**old, java风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_APPS = 93;/**new, windows风格,系统键,不宜用作非全局快捷键*/
	public static final int VK_CONTROL_RIGHT = 163;/**old, java风格*/
	public static final int VK_RCONTROL = 163;/**new, windows风格*/
	public static final int VK_SHIFT_RIGHT = 161;/**old, java风格*/
	public static final int VK_RSHIFT = 161;/**new, windows风格*/
	public static final int VK_ENTER = 13;/**old, java风格*/
	public static final int VK_RETURN = 13;/**new, windows风格*/
	public static final int VK_BACK_SLASH = 220;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_OPEN_BRACKET = 219;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_CLOSE_BRACKET = 221;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_SEMICOLON = 186;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_QUOTE = 222;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_COMMA = 188;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_PERIOD = 190;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_SLASH = 191;/**键值随机器不同而不同,不宜使用*/
	public static final int VK_A = 65;
	public static final int VK_B = 66;
	public static final int VK_C = 67;
	public static final int VK_D = 68;
	public static final int VK_E = 69;
	public static final int VK_F = 70;
	public static final int VK_G = 71;
	public static final int VK_H = 72;
	public static final int VK_I = 73;
	public static final int VK_J = 74;
	public static final int VK_K = 75;
	public static final int VK_L = 76;
	public static final int VK_M = 77;
	public static final int VK_N = 78;
	public static final int VK_O = 79;
	public static final int VK_P = 80;
	public static final int VK_Q = 81;
	public static final int VK_R = 82;
	public static final int VK_S = 83;
	public static final int VK_T = 84;
	public static final int VK_U = 85;
	public static final int VK_V = 86;
	public static final int VK_W = 87;
	public static final int VK_X = 88;
	public static final int VK_Y = 89;
	public static final int VK_Z = 90;

	public static final int VK_PRINTSCREEN = 44;/**old, java风格*/
	public static final int VK_SNAPSHOT = 44;/**new, windows风格*/
	public static final int VK_SCROLL_LOCK = 145;/**old, java风格*/
	public static final int VK_SCROLL = 145;/**new, windows风格*/
	public static final int VK_PAUSE = 19;
	public static final int VK_PAGE_UP = 33;/**old, java风格*/
	public static final int VK_PRIOR = 33;/**new, windows风格*/
	public static final int VK_PAGE_DOWN = 34;/**old, java风格*/
	public static final int VK_NEXT = 34;/**new, windows风格*/
	public static final int VK_END = 35;
	public static final int VK_HOME = 36;
	public static final int VK_INSERT = 45;
	public static final int VK_DELETE = 46;
	public static final int VK_LEFT = 37;
	public static final int VK_UP = 38;
	public static final int VK_RIGHT = 39;
	public static final int VK_DOWN = 40;

	public static final int VK_NUM_LOCK = 144;/**old, java风格*/
	public static final int VK_NUMLOCK = 144;/**new, windows风格*/
	public static final int VK_MULTIPLY = 106;
	public static final int VK_DIVIDE = 111;
	public static final int VK_SUBTRACT = 109;
	public static final int VK_ADD = 107;
	public static final int VK_NUMPAD0 = 96;
	public static final int VK_NUMPAD1 = 97;
	public static final int VK_NUMPAD2 = 98;
	public static final int VK_NUMPAD3 = 99;
	public static final int VK_NUMPAD4 = 100;
	public static final int VK_NUMPAD5 = 101;
	public static final int VK_NUMPAD6 = 102;
	public static final int VK_NUMPAD7 = 103;
	public static final int VK_NUMPAD8 = 104;
	public static final int VK_NUMPAD9 = 105;
	public static final int VK_DECIMAL = 110;
	public static final int VK_CLEAR = 12;/**一般为非Num Lock情况下的数字键5,键值随机器不同而不同,不宜使用*/
	
	public static final int VK_LBUTTON=1;
	public static final int VK_RBUTTON=2;
	public static final int VK_MBUTTON=4;
	
	public static final int WM_MOUSEMOVE=512;
	public static final int WM_MOUSEWHEEL=522;
	public static final int WM_LBUTTONDOWN=513;
	public static final int WM_LBUTTONUP=514;
	public static final int WM_MBUTTONDOWN=520;
	public static final int WM_MBUTTONUP=519;
	public static final int WM_RBUTTONDOWN=516;
	public static final int WM_RBUTTONUP=517;
	  
	protected HHOOK keyHook;
	protected HHOOK mouseHook;
	protected HMODULE hModule;
	
	private LowLevelKeyboardProc keyProc=new LowLevelKeyboardProc()
	{
		@Override
		public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT lParam)
		{
			int w=wParam.intValue();
			if(w==WinUser.WM_KEYDOWN||w==WinUser.WM_SYSKEYDOWN)
			{
				 keydown(lParam.vkCode);
			}
			if(w==WinUser.WM_KEYUP||w==WinUser.WM_SYSKEYUP)
			{
				 keyup(lParam.vkCode);
			}
			return User32.INSTANCE.CallNextHookEx(keyHook, nCode, wParam, lParam.getPointer());
		}
	};
	
	private LowLevelMouseProc mouseProc=new LowLevelMouseProc()
	{
		@Override
		public LRESULT callback(int nCode, WPARAM wParam, MSLLHOOKSTRUCT lParam)
		{
			int w=wParam.intValue();
			POINT p=lParam.pt;
			switch (w)
			{
			case WM_MOUSEMOVE:
				mousemove(p.x, p.y);
				break;
			case WM_MOUSEWHEEL:
				mousewheel(p.x, p.y, lParam.mouseData>0?false:true);
				break;
			case WM_LBUTTONDOWN:
				mousedown(p.x, p.y, VK_LBUTTON);
				break;
			case WM_LBUTTONUP:
				mouseup(p.x, p.y, VK_LBUTTON);
				break;
			case WM_MBUTTONDOWN:
				mousedown(p.x, p.y, VK_MBUTTON);
				break;
			case WM_MBUTTONUP:
				mouseup(p.x, p.y, VK_MBUTTON);
				break;
			case WM_RBUTTONDOWN:
				mousedown(p.x, p.y, VK_RBUTTON);
				break;
			case WM_RBUTTONUP:
				mouseup(p.x, p.y, VK_RBUTTON);
				break;
			default:
				break;
			}
			return User32.INSTANCE.CallNextHookEx(mouseHook, nCode, wParam, lParam.getPointer());
		}
	};
	
	/**
	 * 如果测试系统通过,则安装键盘鼠标钩子,调用过程和消息循环,最后卸载钩子
	 */
	public JohnHook()
	{
		if(testSystem())
		{
			hModule=Kernel32.INSTANCE.GetModuleHandle(null);
			installKeyHook();
			installMouseHook();
			procedure();
			msg_loop();
			uninstallKeyHook();
			uninstallMouseHook();
		}
	}
	
	/**测试系统环境是否为Windows32或兼容Windows32,如果为true,则安排钩子;注:一般不应覆写此方法.*/
	protected boolean testSystem()
	{
		return Platform.isWindows();
	}
	
	/**安装键盘钩子*/
	public final void installKeyHook()
	{
		keyHook=User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyProc, hModule, 0);
	}
	
	/**卸载键盘钩子*/
	public final void installMouseHook()
	{
		mouseHook=User32.INSTANCE.SetWindowsHookEx(WinUser.WH_MOUSE_LL, mouseProc, hModule, 0);
	}
	
	/**卸载键盘钩子*/
	public final void uninstallKeyHook()
	{
		User32.INSTANCE.UnhookWindowsHookEx(keyHook);
	}
	
	/**卸载鼠标钩子*/
	public final void uninstallMouseHook()
	{
		User32.INSTANCE.UnhookWindowsHookEx(mouseHook);
	}
	
	/**消息循环*/
	public final void msg_loop()
	{
		int result=0;
		MSG msg=new MSG();
		while((result=User32.INSTANCE.GetMessage(msg, null, 0, 0))!=0)
		{
			if(result==-1)
			{
				System.err.println("Error in GetMessage!");
				uninstallKeyHook();
				uninstallMouseHook();
				break;
			}else{
				User32.INSTANCE.TranslateMessage(msg);
				User32.INSTANCE.DispatchMessage(msg);
			}
		}
	}
	
	/**异步判断当前左右Alt键是否至少一个被按下*/
	public final boolean isAltDown()
	{
		boolean l=(User32.INSTANCE.GetAsyncKeyState(JohnHook.VK_ALT_LEFT)&(1<<15))!=0;
		boolean r=(User32.INSTANCE.GetAsyncKeyState(JohnHook.VK_ALT_RIGHT)&(1<<15))!=0;
		return l||r;
	}
	
	/**异步判断当前左右Shift键是否至少一个被按下*/
	public final boolean isShiftDown()
	{
		boolean l=(User32.INSTANCE.GetAsyncKeyState(JohnHook.VK_SHIFT_LEFT)&(1<<15))!=0;
		boolean r=(User32.INSTANCE.GetAsyncKeyState(JohnHook.VK_SHIFT_RIGHT)&(1<<15))!=0;
		return l||r;
	}
	
	/**异步判断当前左右Ctrl键是否至少一个被按下*/
	public final boolean isCtrlDown()
	{
		boolean l=(User32.INSTANCE.GetAsyncKeyState(JohnHook.VK_CONTROL_LEFT)&(1<<15))!=0;
		boolean r=(User32.INSTANCE.GetAsyncKeyState(JohnHook.VK_CONTROL_RIGHT)&(1<<15))!=0;
		return l||r;
	}
	
	/**处理消息循环前调用此方法做预处理*/
	public abstract void procedure(); 
	
	/** @param vkCode 键盘按下触发,虚拟键代码,以本类声明的VK_前缀常量为准*/
	protected void keydown(int vkCode){}
	
	/** @param vkCode 键盘弹起触发,虚拟键代码,以本类声明的VK_前缀常量为准*/
	protected void keyup(int vkCode){}
	
	/**
	 * 鼠标移动触发;
	 * @param px 事件发生时鼠标光标所在屏幕横坐标
	 * @param py 事件发生时鼠标光标所在屏幕纵坐标
	 */
	protected void mousemove(int px,int py){}
	
	/**
	 * 鼠标键按下触发
	 * @param px 事件发生时鼠标光标所在屏幕横坐标
	 * @param py 事件发生时鼠标光标所在屏幕纵坐标
	 * @param vkCode 如果为VK_LBUTTON,则为左键,如果为VK_RBUTTON,则为右键,如果为VK_MBUTTON,则为中键
	 */
	protected void mousedown(int px,int py,int vkCode){}
	
	/**
	 * 鼠标键弹起触发
	 * @param px 事件发生时鼠标光标所在屏幕横坐标
	 * @param py 事件发生时鼠标光标所在屏幕纵坐标
	 * @param vkCode 如果为VK_LBUTTON,则为左键,如果为VK_RBUTTON,则为右键,如果为VK_MBUTTON,则为中键
	 */
	protected void mouseup(int px,int py,int vkCode){}
	
	/**
	 * 鼠标滚轮滑动触发
	 * @param px 事件发生时鼠标光标所在屏幕横坐标
	 * @param py 事件发生时鼠标光标所在屏幕纵坐标
	 * @param wheeldown 如果为true,则为向下滑动滚轮,如果为false,则为向上滑动滚轮 .
	 */
	protected void mousewheel(int px,int py,boolean wheeldown){}
	
	
	//test
	/*public static void main(String[] args)
	{
		new JohnHook()
		{
			@Override
			public void procedure()
			{
				JDialog dlg=new JDialog();
				dlg.add(new JTextField("this is a test"));
				dlg.setVisible(true);
			}
			
			@Override
			protected void keydown(int vkCode)
			{
				System.out.println("keydown:"+vkCode);
			}
			@Override
			protected void keyup(int vkCode)
			{
				System.out.println("keyup:"+vkCode);
			}
			@Override
			protected void mousedown(int px, int py, int vkCode)
			{
				System.out.println("mousedown:"+px+","+py+","+vkCode);
			}
			@Override
			protected void mouseup(int px, int py, int vkCode)
			{
				System.out.println("mouseup:"+px+","+py+","+vkCode);
			}
			@Override
			protected void mousemove(int px, int py)
			{
				System.out.println(isCtrlDown());
				System.out.println("mousemove:"+px+","+py);
			}
			@Override
			protected void mousewheel(int px, int py, boolean wheeldown)
			{
				System.out.println("mousewheel:"+px+","+py+","+wheeldown);
			}
			
		};
	}*/
	
	
}
