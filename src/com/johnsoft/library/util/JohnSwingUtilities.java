package com.johnsoft.library.util;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;

import com.johnsoft.library.effect.tween.ComponentTweenAccessor;
import com.johnsoft.library.effect.tween.ComponentTweenHelper;
import com.johnsoft.library.ui.ActiveHeaderRenderer;
import com.sun.awt.AWTUtilities;

public class JohnSwingUtilities
{
	/**如果为true,将开启控制台,图形化异常提示,如果为false,将开启控制台异常提示,如果为null,将隐藏异常信息*/
	public static Boolean DEBUG_FLAG=true;
	
	/**默认的资源路径*/
	public static String IMAGE_RESOURCE_BASE="images/";
	/**
	 * 稍稍封装了获得image的过程,默认搜索
	 */
	public static Image getImageResource(String resourceName)
	{
		return getIconResource(resourceName).getImage();
		//return Toolkit.getDefaultToolkit().getImage(JohnSwingUtilities.class.getResource(resourcePath));
	}
	
	/**
	 * 稍稍封装了获得icon的过程,默认搜索
	 */
	public static ImageIcon getIconResource(String iconName)
	{
		return new ImageIcon(IMAGE_RESOURCE_BASE+iconName);
	}
	
	/**
	 * 弹出警告框
	 */
	public static void alert(Component parent,String text)
	{
		JOptionPane.showMessageDialog(null, text,"警告",JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 用JLabel实现的简单的ToolTip,调用本类的showToolTip(Component,int,int,int,int),将于20ms左右显示提示
	 * @param text 要显示的文本
	 * @param target 给哪个组件显示提示
	 * @param milliseconds 显示多长时间
	 */
	public static Popup showToolTip(String text,Component target,int milliseconds)
	{
		JLabel label=new JLabel(text);
		label.setOpaque(true);
		label.setFont(UIManager.getFont("ToolTip.font"));
		label.setForeground(UIManager.getColor("ToolTip.foreground"));
		label.setBackground(UIManager.getColor("ToolTip.background"));
		label.setBorder(UIManager.getBorder("ToolTip.border"));
		Point p=target.getLocationOnScreen();
		return showToolTip(label, p.x, p.y+target.getHeight(), milliseconds, 20);
	}
	
	/**
	 * 用组件实现的ToolTip,将直接显示;
	 * 优点是可自由设置提示框位置和提示框组件,也可摆脱鼠标悬浮单一事件类型的束缚,缺点是不在归ToolTipManager统一管理导致多个事件无法合并为一次执行,并游离于JComponent组件体系之外.
	 * @param contents 要作为ToolTip显示的文本内容组件
	 * @param x ToolTip要放置的水平位置
	 * @param y ToolTip要放置的垂直位置
	 * @param dismissDelay ToolTip显示多长时间,毫秒计算
	 * @param initialDelay 多长时间后开始显示ToolTip,毫秒计算
	 */
	public static Popup showToolTip(Component contents,int x,int y,final int dismissDelay,int initialDelay)
	{
		final Popup popup=PopupFactory.getSharedInstance().getPopup(null, contents, x, y);
		final Timer timer=new Timer(initialDelay, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Timer hideTimer=new Timer(dismissDelay, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						popup.hide();
					}
				});
				hideTimer.setRepeats(false);
				popup.show();
				hideTimer.start();
			}
		});
		timer.setRepeats(false);
		timer.start();
		return popup;
	}
	
	/**
	 * 获取数字校验器
	 * @param allowDecimals 如果为true,将校验实数,即作为数学上的数值处理,可以为负,可以为小数;如果为false,将校验数字,例如用于id号,电话号,身份证号等,不允许负号或小数点出现
	 */
	public static InputVerifier getNumberVerifier(final boolean allowDecimals)
	{
		return new InputVerifier()
		{
			@Override
			public boolean verify(JComponent input)
			{
				JTextComponent jtf=(JTextComponent)input;
				String text=jtf.getText();
				if(allowDecimals)
				{
					if(JohnStringUtil.isEmpty(text)||JohnStringUtil.isNumeric(text))
					{
						return true;
					}
				}else{
					if(JohnStringUtil.isEmpty(text)||JohnStringUtil.isNumber(text))
					{
						return true;
					}
				}
				return false;
			}
			@Override
			public boolean shouldYieldFocus(JComponent input)
			{
				if(!verify(input))
				{
					alert(input,allowDecimals?"请输入数值!":"请输入整数");
				}
				return super.shouldYieldFocus(input);
			}
		};
	}
	
	/**
	 * 获取字符串正则校验器
	 * @param regex 输入框要匹配的正则表达式
	 * @param message 如果不匹配的提示信息
	 */
	public static InputVerifier getRegexVerifier(final String regex,final String message)
	{
		return new InputVerifier()
		{
			@Override
			public boolean verify(JComponent input)
			{
				JTextComponent jtf=(JTextComponent)input;
				String text=jtf.getText();
				return text.matches(regex);
			}
			@Override
			public boolean shouldYieldFocus(JComponent input)
			{
				if(!verify(input))
				{
					alert(input,message);
				}
				return super.shouldYieldFocus(input);
			}
		};
	}
	
	/**
	 * 使得在JLayeredPane这样的Layout为null的容器安置的组件在容器发生大小变化时随着变动
	 */
	public static void autoFitResize(final JLayeredPane pane,final Component...comps)
	{
		pane.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				for(Component comp : comps)
				{
					comp.setBounds(0,0,pane.getWidth(),pane.getHeight());
				}
				pane.revalidate();
				pane.repaint();		
			}
		});
	}
	
	/**获取window外观的文件对话框的打开或保存按钮*/
	public static JButton getWindowFileChooserOKButton(JFileChooser chooser)
	{
		return (JButton)(((Container)((Container)((Container)chooser.getComponent(2)).getComponent(2)).getComponent(4)).getComponent(1));
	}
	
	/**
	 * 注册文本组件在用户输入时由小写变大写
	 */
	public static void registerUpperCase(final JTextComponent textComp)
	{
		textComp.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				textComp.setText(textComp.getText().toUpperCase());
			}
		});
	}
	
	/**
	 * 注册文本组件在用户输入时由小写变大写,只允许数字和字母,数字可选
	 * @param textComp
	 */
	public static void registerUpperCaseEx(final JTextComponent textComp,final boolean isDigitAllowed,final char... dots)
	{
		textComp.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				checkAndUpperCase(textComp, isDigitAllowed, dots);
			}
		});
		textComp.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
 				checkAndUpperCase(textComp, isDigitAllowed, dots);
			}
		});
	}
	//检测是否为字母和或数字,并大写其字母
	private static void checkAndUpperCase(JTextComponent textComp,boolean isDigitAllowed,char... dots)
	{
		String text=textComp.getText();
		if(text==null) text="";
		if(isDigitAllowed)
		{
			if(JohnStringUtil.isAscii(text))
			{
				textComp.setText(text.toUpperCase());
				return;
			}else{
				StringBuffer sb=new StringBuffer(text);
				for(int i=0;i<sb.length();i++)
				{
					boolean isDot=false;
					char x=sb.charAt(i);
					for(int j=0;j<dots.length;j++)
					{
						if(x==dots[j])
						{
							isDot=true;
						}
					}
					if(!isDot&&!JohnStringUtil.isAscii(x))
					{
						sb.deleteCharAt(i);
					}
				}
				textComp.setText(sb.toString().toUpperCase());
				return;
			}
		}else{
			if(JohnStringUtil.isEnglish(text))
			{
				textComp.setText(text.toUpperCase());
				return;
			}else{
				StringBuffer sb=new StringBuffer(text);
				for(int i=0;i<sb.length();i++)
				{
					if(!JohnStringUtil.isEnglish(sb.charAt(i)))
					{
						sb.deleteCharAt(i);
						i--;
					}
				}
				textComp.setText(sb.toString().toUpperCase());
				return;
			}
		}
	}
	/**
	 * 获取似JLabel的JTextPane以获得自动换行,格式化样式等特性
	 */
	public static JTextPane getTextPaneLabel(JScrollPane jsp,Color textColor)
	{
		JTextPane textPane=new JTextPane();
		textPane.setOpaque(false);
		textPane.setEditable(false);
		//textPane.setEnabled(false);
		textPane.setDisabledTextColor(textColor);
		if(jsp!=null)
		{
			jsp.setViewportView(textPane);
			jsp.setBorder(BorderFactory.createEmptyBorder());
		}
		return textPane;
	}
	/**水平分隔符*/
	public static JSeparator getHSeparator(int width)
	{
		JSeparator sep=new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(width,1));
		sep.setSize(width, 1);
		return sep;
	}
	/**垂直分隔符*/
	public static JSeparator getVSeparator(int height)
	{
		JSeparator sep=new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(1,height));
		sep.setSize(1,height);
		return sep;
	}
	/**获取常用简单样式的字体*/
	@SuppressWarnings("unchecked")
	public static Font getFont(String name,int style,int size,Color color)
	{
		Font font=new Font(name, style, size);
		Map<TextAttribute,Object> map=(Map<TextAttribute, Object>) font.getAttributes();
		map.put(TextAttribute.FOREGROUND, color);
		return font.deriveFont(map);
	}
	/**
	 * @param bg 字符背景填充
	 * @param fg 字符颜色
	 * @param family 字符字体
	 * @param size 字符大小
	 * @param weight 是否加粗,有以下TextAttribute的常量 可选:<br>
	 * WEIGHT_EXTRA_LIGHT、WEIGHT_LIGHT、WEIGHT_DEMILIGHT、WEIGHT_REGULAR、WEIGHT_SEMIBOLD、WEIGHT_MEDIUM、WEIGHT_DEMIBOLD、WEIGHT_BOLD、WEIGHT_HEAVY、WEIGHT_EXTRABOLD和 WEIGHT_ULTRABOLD;<br>
	 * 其中WEIGHT_BOLD值对应于传递给 Font构造方法的样式值 Font.BOLD
	 * @param obliquePosture 是否倾斜
	 * @param underline 是否加下划线
	 * @param strikethrough 是否加删除线
	 * @param extendWidth 正常应为null,即不考虑此属性,如果为true,加拉宽字符,否则挤窄字符
	 * @param superscript 正常应为null,即不考虑此属性,如果为true,将字符作为上标,否则作为下标
	 * @return 多样化样式的字体
	 */
	public static Font getFont(Paint bg,Paint fg,String family,int size,
			Float weight,boolean obliquePosture,boolean underline,boolean strikethrough,
			Boolean extendWidth,Boolean superscript)
	{
		Map<TextAttribute,Object> attributes=new HashMap<TextAttribute,Object>();
		if(bg!=null) attributes.put(TextAttribute.BACKGROUND, bg);
		if(fg!=null) attributes.put(TextAttribute.FOREGROUND, fg);
		if(family!=null) attributes.put(TextAttribute.FAMILY, family);
		if(size>0) attributes.put(TextAttribute.SIZE, size);
		if(obliquePosture) attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		if(strikethrough) attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		if(underline) attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		if(weight!=null) attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		if(extendWidth!=null)
		{
			if(extendWidth)
			{ 
				attributes.put(TextAttribute.WIDTH, TextAttribute.WIDTH_EXTENDED);
			}else{
				attributes.put(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
			}
		}
		if(superscript!=null)
		{
			if(superscript)
			{
				attributes.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
			}else{
				attributes.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
			}
		}
		return new Font(attributes);
	}
	
	/**
	 * 在JTextPane输入面板里插入带样式的字符
	 * @param textPane 要插入字符的面板
	 * @param text 要插入的字符
	 * @param attrSet 如果已经存在字符样式属性集,传入将在此基础上添加样式, 并于最后返回此样式属性集的引用,如果传入null,将新建一个添加样式并返回;<br>
	 *   比如可调用此类的getParagraphStyle(SimpleAttributeSet,int,float,float,float,float,float,float,TabSet)
	 * @param background 字符背景填充
	 * @param foreground 字符颜色
	 * @param fontFamily 字符字体
	 * @param fontSize 字符大小
	 * @param bold 是否加粗
	 * @param italic 是否倾斜
	 * @param underline 是否加下划线
	 * @param strikeThrough 是否加删除线
	 * @param superscript 正常应为null,即不考虑此属性,如果为true,将字符作为上标,否则作为下标
	 * @return 字符样式属性集,如果attrSet形参不为空就返回attrSet
	 */
	public static SimpleAttributeSet insertStyledString(JTextPane textPane, String text, SimpleAttributeSet attrSet,  
			 Color background, Color foreground, String fontFamily, int fontSize,
			 boolean bold, boolean italic, boolean underline, boolean strikeThrough, Boolean superscript)
	{
		if(attrSet==null)
		{
			attrSet = new SimpleAttributeSet();
		}
		
		if(background!=null) StyleConstants.setBackground(attrSet, background);
		if(foreground!=null) StyleConstants.setForeground(attrSet, foreground);
		if(fontFamily!=null) StyleConstants.setFontFamily(attrSet, fontFamily);
		if(fontSize>0) StyleConstants.setFontSize(attrSet, fontSize);
		if (bold) StyleConstants.setBold(attrSet, true);
		if(italic) StyleConstants.setItalic(attrSet, italic);
		if(underline) StyleConstants.setUnderline(attrSet, underline);
		if(strikeThrough) StyleConstants.setStrikeThrough(attrSet, strikeThrough);
		if(superscript!=null)
		{
			if(superscript)
			{
				StyleConstants.setSuperscript(attrSet, true);
			}else{
				StyleConstants.setSubscript(attrSet, true);
			}
		}
		
		if(textPane!=null)
		{
			Document doc = textPane.getDocument();
			try
			{
				doc.insertString(doc.getLength(), text, attrSet);
			} catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		
		return attrSet;
	}
	
	/**
	 * 获取段落相关的字符样式属性集
	 * @param attrSet 如果已经存在字符样式属性集,传入将在此基础上添加样式, 并于最后返回此样式属性集的引用,如果传入null,将新建一个添加样式并返回;
	 * @param align 对齐方式:StyleConstants.ALIGN_CENTER,StyleConstants.ALIGN_JUSTIFIED,StyleConstants.ALIGN_LEFT,StyleConstants.ALIGN_RIGHT
	 * @param firstLineIndent 首行缩进
	 * @param leftIndent 左缩进
	 * @param rightIndent 右缩进
	 * @param lineSpace 行间距
	 * @param spaceAbove 上部空间,可查看StyleConstants类
	 * @param spaceBelow 下部空间,可查看StyleConstants类
	 * @param tabs 在指定位置插入的制表符集
	 * @return 字符样式属性集,如果attrSet形参不为空就返回attrSet
	 */
	public static SimpleAttributeSet getParagraphStyle(SimpleAttributeSet attrSet,int align,
								 float firstLineIndent, float leftIndent, float rightIndent,
								 float lineSpace, float spaceAbove, float spaceBelow, TabSet tabs)
	{
		if(attrSet==null)
		{
			attrSet = new SimpleAttributeSet();
		}
		
		if(align>=0) StyleConstants.setAlignment(attrSet, align);
		if(firstLineIndent>=0) StyleConstants.setFirstLineIndent(attrSet, firstLineIndent);
		if(leftIndent>=0) StyleConstants.setLeftIndent(attrSet, leftIndent);
		if(rightIndent>=0) StyleConstants.setRightIndent(attrSet, rightIndent);
		if(lineSpace>=0) StyleConstants.setLineSpacing(attrSet, lineSpace);
		if(spaceAbove>=0) StyleConstants.setSpaceAbove(attrSet, spaceAbove);
		if(spaceBelow>=0) StyleConstants.setSpaceBelow(attrSet, spaceBelow);
		if(tabs!=null) StyleConstants.setTabSet(attrSet, tabs);
		
		return attrSet;
	}
	
	/**
	 * 获取在区域居中画字符串时的位置
	 * @param g 图形上下文
	 * @param r 作图画字符串的区域,一般需要把r的x,y置为零
	 * @param text 指定字符串
	 * @param font 可以为null来获取当前字体下的尺寸,也可以指定特定字体
	 * @return 将传入drawString(String text,int x,int y)的x,y值的组合点
	 */
	public static Point getDrawStringCenterAt(Graphics g,Rectangle r,String text,Font font)
	{
		if(text==null||text.isEmpty()) return new Point();
		FontMetrics fm;
		if(font==null)
		{
			fm=g.getFontMetrics();
		}else{
			fm=g.getFontMetrics(font);
		}
		int w=fm.stringWidth(text);
		int h=fm.getDescent()-fm.getAscent();
		int x=r.x+(r.width-w)/2;
		int y=r.y+(r.height-h)/2;
		return new Point(x,y);
	}
	
	/**
	 * @param g 图形上下文
	 * @param text 指定字符串
	 * @param font 可以为null来获取当前字体下的尺寸,也可以指定特定字体
	 * @return 在paint时特定图形上下文和字体下字符串的尺寸,一般含有insets在内,比字符串的实际尺寸微大
	 */
	public static Dimension getStringSize(Graphics g,String text,Font font)
	{
		if(text==null||text.isEmpty()) return new Dimension();
		FontMetrics fm;
		if(font==null)
		{
			fm=g.getFontMetrics();
		}else{
			fm=g.getFontMetrics(font);
		}
		int w=fm.stringWidth(text);
		int h=Math.max(fm.getHeight(),fm.getAscent()+fm.getDescent());
		return new Dimension(w,h);
	}
	
	/**
	 * @param isHorizontal 是否是长大于宽的水平光泽
	 * @param style 有"column","metal","highlight"可选,不区分大小写,如果为其他值包括null,都将报错
	 * @param size 如果是水平柱,请传入柱的宽度,如果是垂直柱,请传入柱的长度
	 * @param color 主要基调色
	 * @return 渐变着色
	 */
	public static Paint getComponentLinearGradient(boolean isHorizontal,String style,int size, Color color)
	{
		Point2D start,end;
		float[] dist;
		Color[] colors;
		
		start= new Point2D.Float(0, 0);
		if(isHorizontal)
		{
			end = new Point2D.Float(0, size);
		}else{
			end = new Point2D.Float(size, 0);
		}
		
		int r=color.getRed()-64<0?0:color.getRed()-64;
		int g=color.getGreen()-64<0?0:color.getGreen()-64;
		int b=color.getBlue()-64<0?0:color.getBlue()-64;
		int a=color.getAlpha()+64>255?255:color.getAlpha()+64;
		
		float[] hsbvals=new float[3];
		hsbvals=Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
		float bright=hsbvals[2];
		if(hsbvals[2]>=0.8f)
		{
			hsbvals[2]=hsbvals[2]-0.2f;
		}else{
			hsbvals[2]=hsbvals[2]+0.2f;
		}
		
		if("column".equals(style))
		{
			dist = new float[]{0.0f,0.2f, 0.5f,0.8f,1.0f};
			colors = new Color[]{new Color(r,g,b,a),color, Color.WHITE,color, new Color(r,g,b,a)};
		}
		else if("metal".equals(style))
		{
			dist = new float[]{0.0f,0.2f,1.0f};
			colors = new Color[]{new Color(r,g,b,a), Color.WHITE, color};
		}
		else if("highlight".equals(style))
		{
			if(isHorizontal)
			{
				start = new Point2D.Float(0, size/3);
				end = new Point2D.Float(0, size);
			}else{
				start = new Point2D.Float(size/3, 0);
				end = new Point2D.Float(size, 0);
			}
			if(bright>hsbvals[2])
			{
				return new GradientPaint(start, color,end, Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));
			}else{
				return new GradientPaint(start, Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]),end, color);
			}
		}
		else
		{
			throw new IllegalArgumentException("the param named style must be the value in column,metal,highlight");
		}
		
		LinearGradientPaint paint =new LinearGradientPaint(start, end, dist, colors);
		return paint;
	}
	
	/**
	 * 显示等待加载的动画面板
	 * @param win 为获取GlassPane必需传入的JFrame或JDialog对象
	 * @param image GIF格式的动画图片,如果为null,将使用默认图片
	 * @return 动画由Timer实现,每次请求此方法将创建一个Timer,如果您不想自己销毁,请回传给hideWaitPane
	 */
	public static Timer showWaitPane(final Window win,Image image)
	{
		if(image==null)
		{
			image=new ImageIcon(JohnSwingUtilities.class.getResource("/resources/images/loading.gif")).getImage();
		}
		final Image icon=image;
		
		final JPanel pane=new JPanel()
		{
			private static final long serialVersionUID = 1L;
			private boolean isInit=false;
			@Override
			protected void paintComponent(Graphics g)
			{
				Container container=null;
				if(win instanceof JFrame)
				{
					JFrame frame=(JFrame)win;
					container=frame.getContentPane();
				}
				else if(win instanceof JDialog)
				{
					JDialog dialog=(JDialog)win;
					container=dialog.getContentPane();
				}
				else
				{
					throw new IllegalArgumentException("just JFrame and JDialog is support at present");
				}
				
				Rectangle r=container.getBounds();
				int w=icon.getWidth(null);
				int h=icon.getHeight(null);
				int x=r.width>w?((r.x+r.width-w)/2):r.x;
				int y=r.height>h?((r.y+r.height-h)/2):r.y;
				
				if(!isInit)
				{
					isInit=true;
					BufferedImage background=new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2d=background.createGraphics();
					container.paint(g2d);
					g.drawImage(background, r.x, r.y, r.width, r.height, null);
					g.setColor(new Color(255,255,255,75));
					g.fillRect(r.x, r.y, r.width, r.height);
				}
				
				g.drawImage(icon, x, y, w, h, null);
			}
		};
		
		Timer timer=new Timer(100, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
//				Rectangle r=null;
//				if(win instanceof JFrame)
//				{
//					JFrame frame=(JFrame)win;
//					r=frame.getContentPane().getBounds();
//				}
//				else if(win instanceof JDialog)
//				{
//					JDialog dialog=(JDialog)win;
//					r=dialog.getContentPane().getBounds();
//				}
//				else
//				{
//					throw new IllegalArgumentException("just JFrame and JDialog is support at present");
//				}
//				
//				int w=icon.getWidth(null);
//				int h=icon.getHeight(null);
//				int x=r.width>w?((r.x+r.width-w)/2):r.x;
//				int y=r.height>h?((r.y+r.height-h)/2):r.y;
//				pane.repaint(x,y,w,h);
				pane.repaint();
			}
		});
		
		if(win instanceof JFrame)
		{
			JFrame frame=(JFrame)win;
			frame.setGlassPane(pane);
		}
		else if(win instanceof JDialog)
		{
			JDialog dialog=(JDialog)win;
			dialog.setGlassPane(pane);
		}
		else
		{
			throw new IllegalArgumentException("just JFrame and JDialog is support at present");
		}
		pane.setVisible(true);
		timer.start();
		
		return timer;
	}
	
	/**
	 * 隐藏等待加载的动画面板,对应showWaitPane;
	 * @param win 为获取GlassPane需传入的JFrame或JDialog对象
	 * @param timer 每次showWaitPane时返回的timer,如果未曾销毁,请传入此方法销毁,否则可传null
	 */
	public static void hideWaitPane(Window win,Timer timer)
	{
		JPanel pane=new JPanel();
		pane.setOpaque(false);
		if(win instanceof JFrame)
		{
			JFrame frame=(JFrame)win;
			frame.setGlassPane(pane);
			
		}
		else if(win instanceof JDialog)
		{
			JDialog dialog=(JDialog)win;
			dialog.setGlassPane(pane);
		}
		else
		{
			throw new IllegalArgumentException("just JFrame and JDialog is support at present");
		}
		pane.setVisible(false);
		
		if(timer!=null)
		{
			timer.stop();
			timer=null;
		}
		
		win.repaint();
	}
	
	/**获取子组件的顶层玻璃面板*/
	public static Component getGlassPane(JComponent comp)
	{
		Container container=comp.getTopLevelAncestor();
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			return frame.getGlassPane();
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			return dialog.getGlassPane();
		}
		else
		{
			throw new RuntimeException("unsupported top level ancestor!");
		}
	}
	
	/**
	 * 设置子组件的顶层玻璃面板
	 * @param comp 子组件
	 * @param glass 玻璃面板
	 * @param visible 是否使玻璃面板可见
	 */
	public static void setGlassPane(JComponent comp,Component glass,boolean visible)
	{
		Container container=comp.getTopLevelAncestor();
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			frame.setGlassPane(glass);
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			dialog.setGlassPane(glass);
		}
		else
		{
			throw new RuntimeException("unsupported top level ancestor!");
		}
		glass.setVisible(visible);
	}
	
	/**最大化组件所在的顶层容器窗口*/
	public static void maximizeWindow(JComponent comp)
	{
		Container container=comp.getTopLevelAncestor();
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			dialog.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds());
		}
		else
		{
			throw new RuntimeException("unsupported top level ancestor!");
		}
	}
	/**
	 * @param isHorizontal 是否水平摆放
	 * @param floatable 是否可悬浮拖出
	 * @param borderPainted 是否绘制工具栏中的按钮边框
	 * @param rollover 是否仅当鼠标指针悬停在工具栏按钮上时，才绘制该工具栏按钮的边框
	 * @return 获取普通的常用设置的工具栏
	 */
	public static JToolBar getToolBar(boolean isHorizontal,boolean floatable,boolean borderPainted,boolean rollover)
	{
		JToolBar toolbar=new JToolBar();
		toolbar.setOrientation(isHorizontal?JToolBar.HORIZONTAL:JToolBar.VERTICAL);
		toolbar.setFloatable(floatable);
		toolbar.setBorderPainted(borderPainted);
		toolbar.setRollover(rollover);
		return toolbar;
	}
	
	/**不能使用输入法输入的文本框*/
	public static JTextField getUnInputMethodTextField()
	{
		return new JTextField(){
			private static final long serialVersionUID = 1L;
			protected void processInputMethodEvent(InputMethodEvent e){
		}};
	}
	
	/**
	 * 给指定文本框加上自动提示,JCombox实现
	 * 
	 * @param textField
	 *            指定文本框
	 * @param itemMap
	 *            自动提示时展示的项为键,当点击鼠标或按下回车选择项后填入文本框中的值为值
	 * @param stringMethodType
	 *            String的方法名,支持的有startsWith,endsWith,contains
	 */
	public static void setupAutoComplete(final JTextField textField,
			final Map<String, String> itemMap, final String stringMethodType,
			final boolean ignoreCase)
	{
		final DefaultComboBoxModel model = new DefaultComboBoxModel(itemMap
				.keySet().toArray());
		final JComboBox combo = new JComboBox(model)
		{
			private static final long serialVersionUID = 1L;

			public Dimension getPreferredSize()
			{
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		combo.putClientProperty(Integer.toHexString(combo.hashCode())
				+ "@is_adjusting", false);
		combo.setSelectedItem(null);

		combo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!(Boolean) combo.getClientProperty(Integer
						.toHexString(combo.hashCode()) + "@is_adjusting"))
				{
					Object item = combo.getSelectedItem();
					if (item != null)
					{
						textField.setText(itemMap.get(item));
					}
				}
			}
		});

		textField.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", true);
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					e.setSource(combo);
					combo.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						Object item = combo.getSelectedItem();
						if (item != null)
						{
							textField.setText(itemMap.get(item));
						}
						combo.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					combo.setPopupVisible(false);
				}
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", false);
			}
		});
		textField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void insertUpdate(DocumentEvent e)
			{
				updateList();
			}

			public void removeUpdate(DocumentEvent e)
			{
				updateList();
			}

			public void changedUpdate(DocumentEvent e)
			{
				updateList();
			}

			private void updateList()
			{
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", true);
				model.removeAllElements();
				String text = textField.getText();
				if (ignoreCase)
					text = text.toUpperCase();
				if (!text.isEmpty())
				{
					for (String item : itemMap.keySet())
					{
						String itemU = ignoreCase?item.toUpperCase():item;
						if (stringMethodType.equals("startsWith"))
						{
							if (itemU.startsWith(text))
							{
								model.addElement(item);
							}
						} else if (stringMethodType.equals("endsWith"))
						{
							if (itemU.endsWith(text))
							{
								model.addElement(item);
							}
						} else if (stringMethodType.equals("contains"))
						{
							if (itemU.contains(text))
							{
								model.addElement(item);
							}
						} else
							throw new IllegalArgumentException(
									"The size of viewItems unequal to the size of modelItems!");
					}
				}
				combo.setPopupVisible(model.getSize() > 0);
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", false);
			}
		});
		textField.setLayout(new BorderLayout());
		textField.add(combo, BorderLayout.SOUTH);
	}

	/**
	 * 给指定文本框加上自动提示,JCombox实现
	 * 
	 * @param textField
	 *            指定文本框
	 * @param items
	 *            提示的项目
	 * @param stringMethodType
	 *            String的方法名,支持的有startsWith,endsWith,contains
	 */
	public static void setupAutoComplete(final JTextField textField,
			final List<String> items, final String stringMethodType,
			final boolean ignoreCase)
	{
		final DefaultComboBoxModel model = new DefaultComboBoxModel(
				items.toArray());
		final JComboBox combo = new JComboBox(model)
		{
			private static final long serialVersionUID = 1L;

			public Dimension getPreferredSize()
			{
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		combo.putClientProperty(Integer.toHexString(combo.hashCode())
				+ "@is_adjusting", false);
		combo.setSelectedItem(null);

		combo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!(Boolean) combo.getClientProperty(Integer
						.toHexString(combo.hashCode()) + "@is_adjusting"))
				{
					Object item = combo.getSelectedItem();
					if (item != null)
					{
						textField.setText(item.toString());
					}
				}
			}
		});

		textField.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", true);
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					e.setSource(combo);
					combo.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						Object item = combo.getSelectedItem();
						if (item != null)
						{
							textField.setText(item.toString());
						}
						combo.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					combo.setPopupVisible(false);
				}
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", false);
			}
		});
		textField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void insertUpdate(DocumentEvent e)
			{
				updateList();
			}

			public void removeUpdate(DocumentEvent e)
			{
				updateList();
			}

			public void changedUpdate(DocumentEvent e)
			{
				updateList();
			}

			private void updateList()
			{
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", true);
				model.removeAllElements();
				String text = textField.getText();
				if (ignoreCase)
					text = text.toUpperCase();
				if (!text.isEmpty())
				{
					for (String item : items)
					{
						String itemU = ignoreCase?item.toUpperCase():item;
						if (stringMethodType.equals("startsWith"))
						{
							if (itemU.startsWith(text))
							{
								model.addElement(item);
							}
						} else if (stringMethodType.equals("endsWith"))
						{
							if (itemU.endsWith(text))
							{
								model.addElement(item);
							}
						} else if (stringMethodType.equals("contains"))
						{
							if (itemU.contains(text))
							{
								model.addElement(item);
							}
						} else
							throw new IllegalArgumentException(
									"The size of viewItems unequal to the size of modelItems!");
					}
				}
				combo.setPopupVisible(model.getSize() > 0);
				combo.putClientProperty(Integer.toHexString(combo.hashCode())
						+ "@is_adjusting", false);
			}
		});
		textField.setLayout(new BorderLayout());
		textField.add(combo, BorderLayout.SOUTH);
	}
	
	/**安装自动完成功能*/
	public static void installAutoComplete(final JTextField textField,
			final List<String> items, final String stringMethodType,
			final boolean ignoreCase)
	{
		final JList jList=new JList();
		jList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				System.out.println("select");
			}
		});
		final DefaultListModel model=new DefaultListModel();
		textField.addKeyListener(new KeyAdapter()
		{
			Popup popup;
			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					e.setSource(jList);
					jList.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						Object item = jList.getSelectedValue();
						if (item != null)
						{
							textField.setText(item.toString());
						}
						if(popup!=null)
						{
							popup.hide();
							popup=null;
						}
					}
					return;
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					if(popup!=null)
					{
						popup.hide();
						popup=null;
					}
					return;
				}
				
				model.removeAllElements();
				String text = textField.getText();
				if (ignoreCase)
					text = text.toUpperCase();
				if (!text.isEmpty())
				{
					for (String item : items)
					{
						String	itemU = ignoreCase?item.toUpperCase():item;
						if (stringMethodType.equals("startsWith"))
						{
							if (itemU.startsWith(text))
							{
								model.addElement(item);
							}
						} else if (stringMethodType.equals("endsWith"))
						{
							if (itemU.endsWith(text))
							{
								model.addElement(item);
							}
						} else if (stringMethodType.equals("contains"))
						{
							if (itemU.contains(text))
							{
								model.addElement(item);
							}
						} else
							throw new IllegalArgumentException(
									"The size of viewItems unequal to the size of modelItems!");
					}
				}
				if(model.size()>0)
				{
					jList.setModel(model);
					Point p=textField.getLocationOnScreen();
					if(popup==null)
					{
						popup=PopupFactory.getSharedInstance().getPopup(null, jList, p.x, p.y+textField.getHeight());
						popup.show();
					}
				}else{
					if(popup!=null)
					{
						popup.hide();
						popup=null;
					}
				}
			}
		});
	}
	
	/**
	 * 点击文本框获得光标时全选
	 * @param jtf 点击的文本框
	 * @param onlyInText 如果不为null,则只在文本框是该字符串时才清空,并在文本框为空时自动恢复该字符串
	 */
	public static void clearOnFocus(final JTextField jtf,final String onlyInText)
	{
		jtf.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(FocusEvent e)
			{
				if(onlyInText!=null)
				{
					if(jtf.getText().equals(onlyInText))
					{
						jtf.setText("");
					}
				}else{
					jtf.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				if(jtf.getText().isEmpty())
				{
					jtf.setText(onlyInText);
				}
			}
		});
	}
	
	/**
	 * 点击文本框获得光标时清空已有文本
	 * @param jtf 点击的文本框
	 * @param recoverText 如果不为null,在文本框为空时自动恢复该字符串
	 * @param forTip 当恢复提示文字字符串时的文本颜色
	 * @param forContent 当用户输入文字时的文本颜色
	 */
	public static void selectAllOnFocus(final JTextField jtf,final String recoverText,final Color forTip,final Color forContent)
	{
		jtf.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				if(recoverText!=null)
				{
					String text=jtf.getText();
					if(text.isEmpty())
					{
						jtf.setForeground(forTip);
						jtf.setText(recoverText);
					}
					else if(text.equals(recoverText))
					{
						jtf.setForeground(forTip);
					}
				}
			}
			@Override
			public void focusGained(FocusEvent e)
			{
				jtf.selectAll();
				jtf.setForeground(forContent);
			}
		});
		jtf.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==2&&recoverText!=null&&jtf.getText().equals(recoverText))
				{
					jtf.setText("");
				}
			}
		});
	}
	
	/**模拟单击左键*/
	public static void doLeftClick(final Component source,final int x,final int y)
	{
		final EventQueue eq=Toolkit.getDefaultToolkit().getSystemEventQueue();
		eq.postEvent(new MouseEvent(source, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), MouseEvent.BUTTON1_DOWN_MASK, x, y, 1, false, MouseEvent.BUTTON1));
		Timer timer=new Timer(50, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				eq.postEvent(new MouseEvent(source, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), MouseEvent.BUTTON1_DOWN_MASK, x, y, 1, false, MouseEvent.BUTTON1));
				eq.postEvent(new MouseEvent(source, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), MouseEvent.BUTTON1_DOWN_MASK, x, y, 1, false, MouseEvent.BUTTON1));
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	/**程序级别的全局输入事件即鼠标键盘事件的监听*/
	public static void addProgramInputEventListener(AWTEventListener listener)
	{
		Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK+AWTEvent.MOUSE_MOTION_EVENT_MASK+AWTEvent.MOUSE_WHEEL_EVENT_MASK+AWTEvent.KEY_EVENT_MASK);
	}
	
	/**左键双击*/
	public static boolean isLButtonDBClick(MouseEvent e)
	{
		return e.getID()==MouseEvent.MOUSE_CLICKED&&e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==2;
	}
	
	/**左键弹起*/
	public static boolean isLButtonUp(MouseEvent e)
	{
		return e.getID()==MouseEvent.MOUSE_RELEASED&&e.getButton()==MouseEvent.BUTTON1;
	}
	
	/**右键弹起*/
	public static boolean isRButtonUp(MouseEvent e)
	{
		return e.getID()==MouseEvent.MOUSE_RELEASED&&e.getButton()==MouseEvent.BUTTON3;
	}
	
	/**左键按下*/
	public static boolean isLButtonDown(MouseEvent e)
	{
		return e.getID()==MouseEvent.MOUSE_PRESSED&&e.getButton()==MouseEvent.BUTTON1;
	}
	
	/**右键按下*/
	public static boolean isRButtonDown(MouseEvent e)
	{
		return e.getID()==MouseEvent.MOUSE_PRESSED&&e.getButton()==MouseEvent.BUTTON3;
	}
	
	/**左键按下并按下了某个功能键,即"Alt","Ctrl","Shift"*/
	public static boolean isLButtonSGClickWith(MouseEvent e,String type)
	{
		type=type.trim().toLowerCase();
		if(type.equals("Ctrl"))
		{
			return e.isControlDown()&&e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==1;
		}
		else if(type.equals("Alt"))
		{
			return e.isAltDown()&&e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==1;
		}
		else if(type.equals("Shift"))
		{
			return e.isShiftDown()&&e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==1;
		}
		else
		{
			return e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==1;
		}
	}
	
	/**单个显示器下的 菜单,任务栏高度*/
	public static int getTaskBarHeight()
	{
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc=ge.getDefaultScreenDevice().getDefaultConfiguration();
		Toolkit tkit=Toolkit.getDefaultToolkit();
		Insets insets=tkit.getScreenInsets(gc);
		return insets.bottom;
	}
	
	/**主显示屏下的屏幕尺寸,含菜单,任务栏*/
	public static Dimension getScreenSize()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**单个显示器下的屏幕矩形,含菜单,任务栏*/
	public static Rectangle getScreenBounds()
	{
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc=ge.getDefaultScreenDevice().getDefaultConfiguration();
		return gc.getBounds();
	}
	
	/** 最大化窗口时的大小,即不含菜单,任务栏的屏幕大小,如果是多屏幕,将返回整个显示区域的边界,所有屏幕之和*/
	public static Rectangle getWindowMaxBounds()
	{
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		return ge.getMaximumWindowBounds();
	}
	
	/**
	 * 异常用户提示
	 * @param e 异常
	 * @param suggests 建设性意见
	 * @param parentWindow 父窗口
	 * @param action 点击继续时执行的动作
	 */
	public static void showException(final Exception e,final String suggests,final Window parentWindow,final Action action)
	{
		if(DEBUG_FLAG!=null)
		{
			e.printStackTrace();
			if(DEBUG_FLAG)
			{
				new Thread("visualException")
				{
					public void run() 
					{
						try
						{
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception ex){
						} 
						
						String tip="应用程序中发生了无法处理的异常。如果单击“继续”，应用程序将忽略此错误并尝试继续。如果单击“退出”，应用程序将立即关闭。";
						
						final JDialog dialog = new JDialog(parentWindow,"未知错误");
						dialog.setIconImage(((ImageIcon)UIManager.getIcon("OptionPane.errorIcon")).getImage());
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setModal(true);
						dialog.setAlwaysOnTop(true);
						dialog.setResizable(false);
						
						final JLabel icon = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
						icon.setBounds(20, 20, 40, 40);
						
						final JTextPane msg = new JTextPane();
						msg.setOpaque(false);
						msg.setEnabled(false);
						msg.setDisabledTextColor(Color.BLACK);
						msg.setText(tip);
						msg.setBounds(80, 10, 330, 60);
						
						final JLabel reason=new JLabel("·原因: "+e.getMessage());
						reason.setBounds(80, 70, 330, 25);
						
						final JToggleButton detail = new JToggleButton("详细信息(D)>>");
						detail.setMnemonic(KeyEvent.VK_D);
						detail.setDisplayedMnemonicIndex(5);
						detail.setBounds(10, 110, 120, 25);
						
						final JButton cancel = new JButton("退出(Q)");
						cancel.setMnemonic(KeyEvent.VK_Q);
						cancel.setDisplayedMnemonicIndex(3);
						cancel.setBounds(335, 110, 95, 25);
						
						final JButton ok = new JButton("继续(R)");
						ok.setMnemonic(KeyEvent.VK_R);
						ok.setDisplayedMnemonicIndex(3);
						ok.setBounds(235, 110, 95, 25);
						
						final JTextPane detailMsg = new JTextPane();
						detailMsg.setEditable(false);
						StringBuffer sb=new StringBuffer();
						sb.append(e.toString()+"\n");
						StackTraceElement[] ste=e.getStackTrace();
						for(StackTraceElement el:ste)
						{
							sb.append("\tat ").append(el.toString()).append("\n");
						}
						if(suggests!=null)
						{
							sb.append("建议:").append(suggests);
						}
						detailMsg.setText(sb.toString());
						
						final JScrollPane jsp=new JScrollPane(detailMsg);
						jsp.setBounds(0, 0, 0, 0);
						
						final JPanel contentPanel=new JPanel(null);
						contentPanel.add(icon);
						contentPanel.add(msg);
						contentPanel.add(reason);
						contentPanel.add(detail);
						contentPanel.add(ok);
						contentPanel.add(cancel);
						contentPanel.add(jsp);
						
						detail.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								if(detail.isSelected())
								{
									jsp.setBounds(12, 150, 420, 150);
									dialog.setSize(450, 350);
									detail.setText("详细信息(D)<<");
								}else{
									jsp.setBounds(0, 0, 0, 0);
									dialog.setSize(450, 180);
									detail.setText("详细信息(D)>>");
								}
							}
						});
						
						ok.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								if(action!=null)
								{
									action.actionPerformed(e);
								}
								dialog.dispose();
							}
						});
						
						cancel.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								System.exit(0);
							}
						});
						
						dialog.setContentPane(contentPanel);
						dialog.getRootPane().setDefaultButton(ok);
						dialog.setSize(450, 180);
						dialog.setLocationRelativeTo(parentWindow);
						SwingUtilities.invokeLater(new Runnable()
						{
							@Override
							public void run()
							{
								dialog.setVisible(true);
							}
						});
					}
				}.start();
			}
		}
	}
	
	/**
	 * 安装行表头,注:需要保证传入的table已有了非null的TableModel,并且此TableModel已经考虑了行号;如果已设置行高,将覆写为25
	 * @param table 不能为null,实际的table
	 * @return 包含table,行表头所用的表格组件,header,行表头所用的表格组件的列表头的滚动窗格
	 */
	public static JScrollPane installRowHeader(JTable table)
	{
		return ActiveHeaderRenderer.installRowHeader(table, null, 25);
	}
	
	/**判断字体是否为等宽字体*/
	public static boolean isMonospacedFont(Font f)
	{
		FontRenderContext frc=new FontRenderContext(null, true, false);
		double M=f.getStringBounds("M", frc).getWidth();
		double l=f.getStringBounds("l", frc).getWidth();
		return M==l;
	}
	
	/**用字体文件创建新字体*/
	public static Font createNewFont(String ttfFilePath,int fontStyle,int fontSize)
	{
		try
		{
			Font f=Font.createFont(Font.TRUETYPE_FONT, new File(ttfFilePath));
			f=f.deriveFont(fontStyle,fontSize);
			return f;
		} catch (Exception e)
		{
			showException(e, "字体文件加载失败", null, null);
			return null;
		} 
	}
	
	/**类似jquery,flex中的easing函数,实现动画移动*/
	public static void easeMove(Component c,float duration,float dx,float dy,TweenEquation te)
	{
		new ComponentTweenHelper()
				.start(Tween.to(c, ComponentTweenAccessor.LOCATION_MOVE, duration)
				.target(dx, dy)
				.ease(te));
	}
	
	/**类似在打开浏览器时,360软件在屏幕正中顶部做一个类似露头按钮的提示,隔几秒后缩回去*/
	public static void showDeclineTip(final String text,final String imageName)
	{
		final JDialog dialog=new JDialog();
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
		dialog.setSize(200, 40);
		AWTUtilities.setWindowOpaque(dialog, false);
		dialog.setContentPane(new JLabel()
		{
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
			{
				 Graphics2D g2d=(Graphics2D)g;
				 g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				 g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				 if(imageName==null)
				 {
					 g2d.setPaint(getComponentLinearGradient(true, "highlight", 30, Color.CYAN));
					 g2d.fillRoundRect(0, 0, 200, 40, 30, 30);
					 g2d.setColor(new Color(80,80,255));
				 }else{
					 g2d.clip(new RoundRectangle2D.Double(0, 0, 200, 40, 30, 30));
					 g2d.drawImage(getImageResource(imageName), 0, 0, 200, 40, null);
					 g2d.setColor(Color.WHITE);
				 }
				 Font font=new Font("微软雅黑", Font.BOLD, 14);
				 g2d.setFont(font);
				 Point p=getDrawStringCenterAt(g2d, new Rectangle(0, 0, 200, 40), text, font);
				 g2d.drawString(text, p.x, 30);
			}
		});
		int x=(getScreenSize().width-200)/2;
		dialog.setLocation(x,-40);
		dialog.setVisible(true);
		new ComponentTweenHelper()
				.start(Timeline.createSequence()
				.push(Tween.to(dialog, ComponentTweenAccessor.LOCATION_MOVE, 0.1f).target(x,-15))
				.pushPause(0.2f)
				.push(Tween.to(dialog, ComponentTweenAccessor.LOCATION_MOVE, 0.1f).target(x,-40))
				.push(Tween.call(new TweenCallback()
				{
					public void onEvent(int i, BaseTween<?> bt)
					{
						dialog.dispose();
					}
				})));
	}
	
}























