package com.johnsoft.library.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

/**
 * 此类是按键显示前校验的文本框;采用校验keyChar实现,没有采用校验"keyCode是哪个按键"的理由是:不能禁止输入法输入.例如在输入法的备选字词中可能5对应的标点,当按"5"键时,可能传进文本框的是该标点,如果按按键判决就会失误<br/>
 * 核心方法是void processKeyEvent(KeyEvent),基本思路:1.检测可粘贴性以及是否在粘贴;2.替换指定字符;3.检测是否超出最大位数;4.检测是否是允许字符;5.检测标点,中文,数字,大小写字母,功能键是否允许;6.如果有,转换大小写;7.执行boolean checkKeyEvent(KeyEvent),如果返回true,则8.调用父类的void processKeyEvent(KeyEvent).<br/>
 * 注:不能太依赖于此类的内置校验,如果可能,给此文本框添加InputVerifier或FocusListener或KeyListener做进一步校验总是对的;<br/>
 * <code>常识注释:</code>
 * 字符到代码点的转换只需将该char强转为int,代码点到字符的转换只需将该int强转为char;<br />
 * 可见可打印英文字符十进制代码点(这里皆包含端点值):都在32到126,其中:32为空格字符;数字从48到57;大写字母从65到90,小写字母从97到122,其他为标点;<br/>
 * 转换成十六机制Unicode对应为:都在\u0020到\u007d,其中:\u0020为空格字符;数字从\u0030到\u0039;大写字母从\u0041到\u005a,小写字母从\u0061到\u007a,其他为标点;<br/>
 * 中文字符在19968(\u4e00)到40869(\u9fa5)之间;<br/>
 * 而对应方向键,锁定键,F1到F12,Ctrl,shift,Alt等辅助功能键都会返回65535(\uffff)的代码点.<br/>
 * 需要掌握的特殊键值有:\b(Backspace退格键,退格符)为8(\u0008),\t(Tab键,水平制表符)为9(\u0009),\n(Enter键,换行符)为10(\u000a),\r(回车符)为13(\u000d),Esc键返回27(\u001b),Del键返回127(\u007f);<br/>
 * 需要掌握的标点值有:\(92->\u005c)	_(95->\u005f)	.(46->\u002e)	-(45->\u002d)<br/>
 * 全角的英文大小写字母数字及标点比对于的半角英文大小写字母数字及标点的代码点大65248;(不含空格,西文空格对于的全角代码点为12288)<br/>
 * 中文一般有按键可输入的独有的标点是:·￥…—【】‘’“”。《》、× <br/>
 * <code>策略:</code>
 * 基本上可以认为粗鲁的过滤非英文标识并不影响功能键的使用的区间是160<x<65535,<br/>
 * 这里会参杂很多非打印字符,但是可被用户使用的只有代码点为32的空格,用户打不出像\u0000这样的空字符,故所定的非英文校验区间就是它.<br/>
 * @author 李哲浩
 */
public class JohnTextField extends JTextField
{
	private static final long serialVersionUID = 1L;
	
	/*public static void main(String[] args)
	{
		final JohnTextField jtf=new JohnTextField();
		
		//jtf.installIcon(new ImageIcon("D:\\icon_weibo_24.png"), true, null);
		//jtf.setHint("Username");
		//jtf.replaceFromSBCOrChineseStyle();
		//jtf.setEnglishNameStyle(';');
		
		jtf.setPreferredSize(new Dimension(200, 25));
		JPanel jp=new JPanel();
		jp.add(jtf);
		JFrame jf=new JFrame();
		jf.add(jp);
		jf.setSize(600, 100);
		jf.setLocationRelativeTo(null);
		jf.setAlwaysOnTop(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}*/
	
	/**半角西文标点字符串*/
	public static final String punctuationString="`~!@#$%^&*()-_=+{}[]\\|;:\'\",.<>?/";
	/**半角下的西文标点*/
	//public static final char[] punctuationCh=new char[]{'`','~','!','@','#','$','%','^','&','*','(',')','-','_','=','+','{','}','[',']','\\','|',';',':','\'','\"',',','.','<','>','?','/'};
	/**全角下的西文标点*/
	//public static final char[] qPunctuationCh=new char[]{'｀','～','！','＠','＃','＄','％','＾','＆','＊','（','）','－','＿','＝','＋','｛','｝','［','］','＼','｜','；','：','＇','＂','，','．','＜','＞','？','／'};
	/**半角下的中文标点*/
	//public static final char[] cPunctuationCh=new char[]{'·','～','！','＠','＃','￥','％','…','＆','*','（','）','－','—','＝','＋','｛','｝','【','】','＼','｜','；','：','‘','“','，','。','《','》','？','、'};
	/**全角下的中文标点*/
	//public static final char[] cqPunctuationCh=new char[]{'·','~','！','@','#','￥','%','…','&','×','（','）','-','—','=','+','{','}','【','】','、','|','；','：','‘','“','，','。','《','》','？','、'};

	protected boolean isPasteUnAllowed;//是否允许粘贴操作
	protected boolean isUnECUnAllowed;//日文,韩文及中日韩标点符号等非中英文字符不被允许
	protected boolean isChineseUnAllowed;//中文不被允许
	protected boolean isDigitUnAllowed;//数字不被允许
	protected boolean isUpperCaseUnAllowed;//大写字母不被允许
	protected boolean isLowerCaseUnAllowed;//小写字母不被允许
	protected boolean isPunctuationUnAllowed;//标点不被允许
	
	protected List<Character> allowedList;//允许的字符列表
	protected Map<Character,Character> replaceMap;//需要替换的字符列表
	protected int maxLength=Integer.MAX_VALUE;//输入最大位数
	
	protected UndoManager undoManager;//撤销重做管理者
	
	protected Icon icon;//图标
	protected String hint;//文本框内提示
	protected boolean iconPosAtEnd;//按钮是放在后面还是前面
	protected MouseAdapter iconMouseAdapter;//图标点击时动作
	
	private Rectangle iconBounds;//图标区域
	
	public JohnTextField()
	{
		super();
		installListeners();
	}
	
	public JohnTextField(int columns)
	{
		super(columns);
		installListeners();
	}
	
	public JohnTextField(String text)
	{
		super(text);
		installListeners();
	}
	
	public JohnTextField(String text,int columns)
	{
		super(text, columns);
		installListeners();
	}
	
	public JohnTextField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		installListeners();
	}
	
	protected void installListeners()
	{
		undoManager = new UndoManager();
		getDocument().addUndoableEditListener(new UndoableEditListener()
		{
			@Override
			public void undoableEditHappened(UndoableEditEvent e)
			{
				undoManager.addEdit(e.getEdit());
			}
		});
		getActionMap().put("Undo", new AbstractAction("Undo")
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent evt)
			{
				if (undoManager.canUndo())
				{
					undoManager.undo();
				}
			}
		});
		getInputMap().put(KeyStroke.getKeyStroke("ctrl Z"), "Undo");

		getActionMap().put("Redo", new AbstractAction("Redo")
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent evt)
			{
				if (undoManager.canRedo())
				{
					undoManager.redo();
				}
			}
		});
		getInputMap().put(KeyStroke.getKeyStroke("ctrl Y"), "Redo");
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		int width = this.getWidth();
		int height = this.getHeight();
		int hGap = 5,vGap = 2;
		int textS = hGap, textE = hGap, x = 0, y = 0;

		if (icon != null)
		{
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			y = (height - iconHeight) / 2;
			if(iconPosAtEnd)
			{
				x=width-hGap-iconWidth;
				textE = iconWidth+hGap*2;
			}else{
				x=hGap;
				textS += x + iconWidth;
			}
			iconBounds=new Rectangle(x, y, iconWidth, iconHeight);
			setMargin(new Insets(vGap, textS, vGap, textE));
		}
		
		super.paintComponent(g);
		if(icon!=null)
		{
			icon.paintIcon(this, g, x, y);
		}
		
		if (hint!=null&&getText().isEmpty())
		{
			Font prev = g.getFont();
			Font italic = prev.deriveFont(Font.ITALIC);
			Color prevColor = g.getColor();
			g.setFont(italic);
			g.setColor(UIManager.getColor("textInactiveText"));
			int h = g.getFontMetrics().getHeight();
			int textBottom = (height - h) / 2 + h - 4;
			Graphics2D g2d = (Graphics2D) g;
			RenderingHints hints = g2d.getRenderingHints();
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.drawString(hint, this.getInsets().left, textBottom);
			g2d.setRenderingHints(hints);
			g.setFont(prev);
			g.setColor(prevColor);
		}
	}
	
	@Override
	protected void processKeyEvent(KeyEvent e)
	{
		if(isPasteUnAllowed&&isPasting(e))
		{//如果不许粘贴却在做粘贴操作就返回true,以便不传递事件
			return;
		}
		
		char keyChar=e.getKeyChar();
		if(replaceMap!=null)
		{//替换字符,比如将中文标点自动转成英文标点
			Character replacedChar=replaceMap.get(keyChar);
			if(replacedChar!=null)
			{
				keyChar=replacedChar;
				e.setKeyChar(keyChar);
			}
		}
		
		int codePoint=(int)keyChar;
		
		if(getText().length()>=maxLength)
		{//限制字符位数
			if((getSelectedText()==null||getSelectedText().isEmpty())&&!isOtherFnKeys(codePoint))
			{//如果选中了文本应该允许被替换
				return;
			}
		}
		
		if(allowedList!=null)
		{//是否是指定免检字符,如果是,直接跳过
			for(Character c:allowedList)
			{
				if(keyChar==c)
				{
					super.processKeyEvent(e);
					return;
				}
			}
		}
		
		if ((isPunctuationUnAllowed && isPunctuation(codePoint)) || 
			(isUnECUnAllowed && isUnEC(codePoint)) ||
			(isChineseUnAllowed && isChinese(codePoint)) ||
			(isDigitUnAllowed && isDigit(codePoint)) ||
			(isUpperCaseUnAllowed && isLowerCaseUnAllowed && isLetter(codePoint)))
		{//标点不允许却是标点,中文不允许却是中文,数字不允许却是数字,字母不允许却是字母的情况不响应
			return;
		}
		
		if(!isUpperCaseUnAllowed&&isLowerCaseUnAllowed&&isLowerCase(codePoint))
		{//如果仅不许小写,则小写变大写
			e.setKeyChar(Character.toUpperCase(keyChar));
		}
		else if(isUpperCaseUnAllowed&&!isLowerCaseUnAllowed&&isUpperCase(codePoint))
		{//如果仅不许大写,则大写变小写
			e.setKeyChar(Character.toLowerCase(keyChar));
		}
		
		if(checkKeyEvent(e))
		{
			super.processKeyEvent(e);
		}
	}
	
	/**无条件直接返回true,如果仍需要进行其他检查进行字符校验或击键校验,可复写此方法*/
	protected boolean checkKeyEvent(KeyEvent e)
	{
		return true;
	}
	
	/**添加允许字符集*/
	public void addAllowedChars(char...allowedChars)
	{
		if(allowedList==null)
		{
			allowedList=new ArrayList<Character>();
		}
		for(char ch:allowedChars)
		{
			allowedList.add(ch);
		}
	}
	
	/**删除允许字符集*/
	public void removeAllowedChars(char...allowedChars)
	{
		if(allowedList==null) return;
		for(char ch:allowedChars)
		{
			allowedList.remove(ch);
		}
	}
	
	/**增加替换字符*/
	public void addReplaceChar(char key,char value)
	{
		if(replaceMap==null)
		{
			replaceMap=new HashMap<Character,Character>();
		}
		replaceMap.put(key, value);
	}
	
	/**删除替换字符*/
	public void removeReplaceChar(char key)
	{
		if(replaceMap==null) return;
		replaceMap.remove(key);
	}
	
	/** 判断是否正在执行粘贴快捷键操作 */
	public static boolean isPasting(KeyEvent e)
	{
		int keyCode=e.getKeyCode();
		if((e.isControlDown()&&keyCode==KeyEvent.VK_V)||(e.isShiftDown()&&keyCode==KeyEvent.VK_INSERT)||(keyCode==KeyEvent.VK_PASTE))
		{
			return true;
		}
		return false;
	}
	
	/**如果是大写则转为小写,如果是小写则转为大写,否则返回原字符*/
	public static char changeCase(char ch)
	{
		if(Character.isLowerCase(ch))
		{
			return Character.toUpperCase(ch);
		}
		else if(Character.isUpperCase(ch))
		{
			return Character.toLowerCase(ch);
		}
		return ch;
	}
	
	/**全角西文字符转半角,如不能转换返回'\u0000'*/
	public static char SBCToDBC(char ch)
	{
		int codePoint=(int)ch;
		if(isSBCEnglish(codePoint))
		{
			return (char)(codePoint-65248);
		}
		return '\u0000';
	}
	
	/**半角西文字符转全角,如不能转换返回'\u0000'*/
	public static char DBCToSBC(char ch)
	{
		int codePoint=(int)ch;
		if(codePoint>32&&codePoint<127)
		{
			return (char)(codePoint+65248);
		}
		return '\u0000';
	}
	
	/** "a" --> 'a' 或者 "\u0097" --> 'a'  也可传入"0x4e00"或"4e00"等形式的十六进制数字字符串*/
	public static char stringToChar(String str)
	{
		if(str!=null)
		{
			if(str.length()==1)
			{
				return str.toCharArray()[0];
			}
			else if(str.length()==4)
			{
				return (char)(Integer.parseInt(str, 16));
			}
			else if(str.length()==6)
			{
				return (char)(Integer.parseInt(str.substring(2), 16));
			}
		}
		throw new IllegalArgumentException("the param length isn't fit a char length or can't be parsed");
	}
	
	/** 'a' --> "a" 或者 '\u0097' --> "a" */
	public static String charToString(char ch)
	{
		return String.valueOf(ch);
	}
	
	/**返回字符对应代码点的16进制表示形式*/
	public static String getHexCodePoint(char ch)
	{
		return Integer.toHexString((int)ch);
	}
	
	/**
	 * 判断是否敲入了英文标点符号,如果敲入了英文标点字符,则为true
	 * @param codePoint 字符代码点
	 */
	public static boolean isPunctuation(int codePoint)
	{
		return (codePoint>32&&codePoint<48)||(codePoint>57&&codePoint<65)||(codePoint>90&&codePoint<97)||(codePoint>122&&codePoint<127);
	}
	
	/**
	 * 判断是否敲入了中文,如果敲入中文字符,则为true
	 * @param codePoint 字符代码点
	 */
	public static boolean isChinese(int codePoint)
	{
		return codePoint>=19968&&codePoint<=40869;
	}
	
	/**
	 * E指English,C指Chinese,判断是否敲入了非中英文,如敲入日文字符,中文标点字符,则为true
	 * @param codePoint 字符代码点
	 */
	public static boolean isUnEC(int codePoint)
	{
		return (codePoint>160&&codePoint<19968)||(codePoint>40869&&codePoint<65535);
	}
	
	/** 判断是否为下划线_*/
	public static boolean isUnderLine(int codePoint)
	{
		return codePoint==95;
	}
	
	/** 判断是否为数学负号-*/
	public static boolean isMinus(int codePoint)
	{
		return codePoint==45;
	}
	
	/** 判断是否为数学小数点.*/
	public static boolean isDecimalPoint(int codePoint)
	{
		return codePoint==46;
	}
	
	/** 判断是否为数字0*/
	public static boolean isZero(int codePoint)
	{
		return codePoint==48;
	}
	
	/** 判断是否敲入了空格*/
	public static boolean isSpace(int codePoint)
	{
		return codePoint==32;
	}
	
	/** 判断是否为tab键水平制表符*/
	public static boolean isTab(int codePoint)
	{
		return codePoint==9;
	}
	
	/** 判断是否为换行或回车符*/
	public static boolean isEnterOrWrapLine(int codePoint)
	{
		return codePoint==10||codePoint==13;
	}
	
	/** 判断是否为退格或删除符*/
	public static boolean isBackspaceOrDel(int codePoint)
	{
		return codePoint==8||codePoint==127;
	}
	
	/**判断是否是代码点为65535的功能键*/
	public static boolean isMaxFnKey(int codePoint)
	{
		return codePoint==65535;
	}
	
	/**判断是否是西文全角字符,包括大小写字母数字和标点,但不包括空格*/
	public static boolean isSBCEnglish(int codePoint)
	{
		int shortWidth=codePoint-65248;
		return shortWidth>32&&shortWidth<127;
	}
	
	/**
	 * 判断是否敲入了数字,如果敲入了数字字符,则为true
	 * @param codePoint 字符代码点
	 */
	public static boolean isDigit(int codePoint)
	{
		return codePoint>=48&&codePoint<=57;
	}
	
	/**
	 * 判断是否敲入了大写字母,如果敲入了大写字母字符,则为true
	 * @param codePoint 字符代码点
	 */
	public static boolean isUpperCase(int codePoint)
	{
		return codePoint>=65&&codePoint<=90;
	}
	
	/**
	 * 判断是否敲入了小写字母,如果敲入了小写字母字符,则为true
	 * @param codePoint 字符代码点
	 * @return
	 */
	public static boolean isLowerCase(int codePoint)
	{
		return codePoint>=97&&codePoint<=122;
	}
	
	/**
	 * 判断敲击是否是敲入功能键等引起的非打印字符
	 * @param codePoint 字符代码点
	 */
	public static boolean isOtherFnKeys(int codePoint)
	{
		return (codePoint>=0&&codePoint<=31)||(codePoint>=127&&codePoint<=160)||codePoint==65535;
	}
	
	/**判断代码点是否为字母*/
	public static boolean isLetter(int codePoint)
	{
		return isUpperCase(codePoint)||isLowerCase(codePoint);
	}
	
	/**判断代码点是否为字母或数字*/
	public static boolean isLetterOrDigit(int codePoint)
	{
		return isLetter(codePoint)||isDigit(codePoint);
	}
	
	/**判断代码点是否为字母或数字或下划线或空格*/
	public static boolean isUserNameStyle(int codePoint)
	{
		return isLetterOrDigit(codePoint)||isSpace(codePoint)||isUnderLine(codePoint);
	}

	/**是否允许粘贴操作*/
	public  boolean isPasteUnAllowed()
	{
		return isPasteUnAllowed;
	}
	
	/**是否允许粘贴操作*/
	public void setPasteUnAllowed(boolean isPasteUnAllowed)
	{
		this.isPasteUnAllowed = isPasteUnAllowed;
	}

	/**日文,韩文及中日韩标点符号等非中英文字符是否允许*/
	public boolean isUnECUnAllowed()
	{
		return isUnECUnAllowed;
	}

	/**日文,韩文及中日韩标点符号等非中英文字符是否允许*/
	public void setUnECUnAllowed(boolean isUnECUnAllowed)
	{
		this.isUnECUnAllowed = isUnECUnAllowed;
	}

	/**中文是否允许*/
	public boolean isChineseUnAllowed()
	{
		return isChineseUnAllowed;
	}
	
	/**中文是否允许*/
	public void setChineseUnAllowed(boolean isChineseUnAllowed)
	{
		this.isChineseUnAllowed = isChineseUnAllowed;
	}

	/**数字是否允许*/
	public boolean isDigitUnAllowed()
	{
		return isDigitUnAllowed;
	}

	/**数字是否允许*/
	public void setDigitUnAllowed(boolean isDigitUnAllowed)
	{
		this.isDigitUnAllowed = isDigitUnAllowed;
	}

	/**大写字母是否允许,如果不允许大写,但允许小写,则直接转换成小写*/
	public boolean isUpperCaseUnAllowed()
	{
		return isUpperCaseUnAllowed;
	}

	/**大写字母是否允许,如果不允许大写,但允许小写,则直接转换成小写*/
	public void setUpperCaseUnAllowed(boolean isUpperCaseUnAllowed)
	{
		this.isUpperCaseUnAllowed = isUpperCaseUnAllowed;
	}

	/**小写字母是否允许,如果不允许小写,但允许大写,则直接转换成大写*/
	public boolean isLowerCaseUnAllowed()
	{
		return isLowerCaseUnAllowed;
	}

	/**小写字母是否允许,如果不允许小写,但允许大写,则直接转换成大写*/
	public void setLowerCaseUnAllowed(boolean isLowerCaseUnAllowed)
	{
		this.isLowerCaseUnAllowed = isLowerCaseUnAllowed;
	}

	/**西文标点是否允许*/
	public boolean isPunctuationUnAllowed()
	{
		return isPunctuationUnAllowed;
	}

	/**西文标点是否允许*/
	public void setPunctuationUnAllowed(boolean isPunctuationUnAllowed)
	{
		this.isPunctuationUnAllowed = isPunctuationUnAllowed;
	}
	
	/**获取特殊允许的字符集*/
	public List<Character> getAllowedList()
	{
		return allowedList;
	}

	/**设置特殊允许的字符集*/
	public void setAllowedList(List<Character> allowedList)
	{
		this.allowedList = allowedList;
	}

	/**获取替换字符映射表*/
	public Map<Character, Character> getReplaceMap()
	{
		return replaceMap;
	}

	/**设置替换字符映射表*/
	public void setReplaceMap(Map<Character, Character> replaceMap)
	{
		this.replaceMap = replaceMap;
	}

	/**获取文本框允许输入的最大长度,中英文及其标点都算1个字符长度,而非中文为英文2倍*/
	public int getMaxLength()
	{
		return maxLength;
	}

	/**设置文本框允许输入的最大长度,中英文及其标点都算1个字符长度,而非中文为英文2倍*/
	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
	}
	
	/**获取图标*/
	public Icon getIcon()
	{
		return icon;
	}
	
	/**设置图标*/
	public void setIcon(Icon icon)
	{
		this.icon = icon;
	}
	
	/**获取图标鼠标动作*/
	public MouseAdapter getIconMouseAdapter()
	{
		return iconMouseAdapter;
	}
	
	/**设置图标鼠标动作,可通过getIconBounds方法获取图标区域并判断鼠标是否在此区域*/
	public void setIconMouseAdapter(MouseAdapter iconMouseAdapter)
	{
		this.iconMouseAdapter = iconMouseAdapter;
		if(iconMouseAdapter!=null)
		{
			addMouseListener(iconMouseAdapter);
			addMouseMotionListener(iconMouseAdapter);
			addMouseWheelListener(iconMouseAdapter);
		}
	}
	
	/**获取图标位置,如果为true,即为在后端,否则为前端*/
	public boolean isIconPosAtEnd()
	{
		return iconPosAtEnd;
	}
	
	/**设置图标位置,如果为true,即为在后端,否则为前端*/
	public void setIconPosAtEnd(boolean iconPosAtEnd)
	{
		this.iconPosAtEnd = iconPosAtEnd;
	}
	
	/**获取文本框提示*/
	public String getHint()
	{
		return hint;
	}
	
	/**设置文本框提示*/
	public void setHint(String hint)
	{
		this.hint = hint;
	}
	
	/**获取图标所在的区域*/
	public Rectangle getIconBounds()
	{
		return iconBounds;
	}
	
	//-------------------便捷新加方法-----------------//
	
	/**
	 * 最大长度为500,不允许粘贴,设置只能输入中文汉字和指定的字符
	 * @param allowedChars 特殊允许的字符
	 */
	public void setChineseNameStyle(char...allowedChars)
	{
		setPasteUnAllowed(true);
		setUnECUnAllowed(true);
		setPunctuationUnAllowed(true);
		setDigitUnAllowed(true);
		setUpperCaseUnAllowed(true);
		setLowerCaseUnAllowed(true);
		setMaxLength(500);
		if(allowedList==null)
		{
			allowedList=new ArrayList<Character>();
		}
		for(char ch:allowedChars)
		{
			allowedList.add(ch);
		}
	}
	
	/**
	 * 最大长度为500,不允许粘贴,设置只能输入英文大小写和数字及下划线_,以及指定的字符
	 * @param allowedChars 特殊允许的字符
	 */
	public void setEnglishNameStyle(char...allowedChars)
	{
		setPasteUnAllowed(true);
		setUnECUnAllowed(true);
		setPunctuationUnAllowed(true);
		setChineseUnAllowed(true);
		setMaxLength(500);
		if(allowedList==null)
		{
			allowedList=new ArrayList<Character>();
		}
		char underline='_';
		boolean hasUnderLine=false;
		for(char ch:allowedChars)
		{
			if(ch==underline)
			{
				hasUnderLine=true;
			}
			allowedList.add(ch);
		}
		if(!hasUnderLine)
		{
			allowedList.add(underline);
		}
	}
	
	/**
	 * 最大长度为500,不允许粘贴,设置只能输入数字,以及指定的字符
	 * @param allowedChars 特殊允许的字符
	 */
	public void setNumberStyle(char...allowedChars)
	{
		setPasteUnAllowed(true);
		setUnECUnAllowed(true);
		setPunctuationUnAllowed(true);
		setChineseUnAllowed(true);
		setUpperCaseUnAllowed(true);
		setLowerCaseUnAllowed(true);
		setMaxLength(500);
		if(allowedList==null)
		{
			allowedList=new ArrayList<Character>();
		}
		for(char ch:allowedChars)
		{
			allowedList.add(ch);
		}
	}
	
	/** 将中文标点以及全角状态下的英文大小写字母数字标点都自动替换成对应英文半角下的形式*/
	public void replaceFromSBCOrChineseStyle()
	{
		if(replaceMap==null)
		{
			replaceMap=new HashMap<Character, Character>();
		}
		replaceMap.put((char)12288, (char)32);//空格
		for(int i=33;i<127;i++)
		{//全角西文
			replaceMap.put((char)(i+65248), (char)i);
		}
		//中文特有标点
		replaceMap.put('·', '`');
		replaceMap.put('￥', '$');
		replaceMap.put('…', '^');
		replaceMap.put('—', '_');
		replaceMap.put('【', '[');
		replaceMap.put('】', ']');
		replaceMap.put('‘', '\'');
		replaceMap.put('’', '\'');
		replaceMap.put('“', '\"');
		replaceMap.put('”', '\"');
		replaceMap.put('。', '.');
		replaceMap.put('《', '<');
		replaceMap.put('》', '>');
		replaceMap.put('、', '/');
		replaceMap.put('×', '*');
	}
	
	/**给文本框安装图标及点击时的动作*/
	public void installIcon(Icon icon, boolean iconPosAtEnd,MouseAdapter iconMouseAdapter)
	{
		setIcon(icon);
		setIconPosAtEnd(iconPosAtEnd);
		setIconMouseAdapter(iconMouseAdapter);
	}
	
	/**禁用输入法*/
	public void disabledInputMethod()
	{
		enableInputMethods(false);
	}
	
}
