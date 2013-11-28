package com.johnsoft.library.component;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.text.Document;

import com.johnsoft.library.util.JohnSwingUtilities;



/**
 * 针对十进制实数内数值,非数字字符的按键显示前校验的文本框;
 * 覆写了boolean checkKeyEvent(KeyEvent)完成校验;
 * 注:不能太依赖于此类的内置校验,如果可能,给此文本框添加InputVerifier或FocusListener或KeyListener做进一步校验总是对的.
 * @author 李哲浩
 */
public class JohnNumberField extends JohnTextField 
{
	private static final long serialVersionUID = 1L;
	
//	public static void main(String[] args)
//	{
//		final JTextField tf=new JTextField("1111111");
//		final JohnNumberField jtf=new JohnNumberField();
//		
//		jtf.setMaxValue(new BigDecimal("10"),true);
//		jtf.setMinValue(new BigDecimal("0"), false);
//		
//		jtf.setPreferredSize(new Dimension(200, 25));
//		JPanel jp=new JPanel();
//		jp.add(jtf);
//		jp.add(tf);
//		JFrame jf=new JFrame();
//		jf.add(jp);
//		jf.setSize(600, 100);
//		jf.setLocationRelativeTo(null);
//		jf.setAlwaysOnTop(true);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jf.setVisible(true);
//	}
	
	public static String decimalStartWidthZeroHelpTip="如果需要输入类似0.1的数值,请输入.1";
	public static String numberFormatExceptionTip="数值非法或系统故障,请重新输入!";
	public static String maxValueTip="所输入的值不能大于";
	public static String minValueTip="所输入的值不能小于";
	public static String equalString="等于";
	
	protected boolean isNegativeUnAllowed;//负数不被允许
	protected boolean isZeroUnAllowed;//数值而非数字0不被允许
	protected boolean isDecimalUnAllowed;//小数不被允许
	
	protected boolean isDecimalStartWidthZeroAllowed;//不允许输入0值,但小数允许时,是否允许0.形式的小数
	protected boolean isMaxValueAllowed;//是否允许等于最大值
	protected boolean isMinValueAllowed;//是否允许等于最小值
	protected BigDecimal maxValue;//允许的最大值
	protected BigDecimal minValue;//允许的最小值
	
	public JohnNumberField()
	{
		super();
		init();
	}
	
	public JohnNumberField(int columns)
	{
		super(columns);
		init();
	}
	
	public JohnNumberField(String text)
	{
		super(text);
		init();
	}
	
	public JohnNumberField(String text,int columns)
	{
		super(text, columns);
		init();
	}
	
	public JohnNumberField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		init();
	}
	
	private void init()
	{
		disabledInputMethod();
		replaceFromSBCOrChineseStyle();//允许输入。．－－时替换为半角.-
		setNumberStyle();//设置只能输入数字
		setPunctuationUnAllowed(false);//允许.-标点,setNumberStyle()方法会屏蔽标点
	}
	
	@Override
	protected boolean checkKeyEvent(KeyEvent e)
	{
		char keyChar=e.getKeyChar();
		int codePoint=(int)keyChar;
		String text=getText();
		int textLength=text.length();
		
		if(isSpace(codePoint)) return false;//防止输入空格
		
		if(isPunctuation(codePoint))
		{//过滤标点
			if(isMinus(codePoint))
			{
				if((textLength>0)||(textLength==0&&isNegativeUnAllowed))
				{//负号只允许出现在第一个位置上
					return false;
				}
			}  
			else if(isDecimalPoint(codePoint))
			{
				if(isDecimalUnAllowed||(text.indexOf(".")>=0))
				{//小数点只能出现一次
					return false;
				}
			}else{
				return false;
			}
		}
		
		if(isZero(codePoint)&&((text.equals(""))||(text.equals("-"))))
		{//除非是(-)0.的小数形式或者就是(-)0,否则第一位不能为0或者第一位为'-',则第二位不能为0
			if(isZeroUnAllowed&&isDecimalUnAllowed)
			{
				return false;
			}
			if((isZeroUnAllowed&&!isDecimalUnAllowed)&&!isDecimalStartWidthZeroAllowed())
			{
				return false;
			}
		}
		
		if(text.equals("0")||text.equals("-0"))
		{
			if(!isOtherFnKeys(codePoint)&&!isDecimalPoint(codePoint))
			{//只允许输入退格删除键或小数点
				return false;
			}
		}
		
		if(!handleMaxValueAndMinValueValidity(e.getKeyCode(),text, textLength, keyChar, codePoint)) return false;
		
		return super.checkKeyEvent(e);
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e)
	{//在鼠标插入到文本中间时屏蔽,即不允许回溯光标,除非仅用于选择;如果不屏蔽,将有可能删除前面的负号或小数点越过最大最小值的限制
		if(e.getID()==MouseEvent.MOUSE_PRESSED||e.getID()==MouseEvent.MOUSE_DRAGGED)
		{
			int textLen=getText().length();
			if(textLen>0&&viewToModel(e.getPoint())!=textLen)
			{
				setCaretPosition(textLen);
				return;
			}
		}
		super.processMouseEvent(e);
	}
	
	/**
	 * 处理按键后组成的文本框中的数值是否大于(等于)设置的最大值或小于(等于)指定的最小值,可复写做更细致的校验
	 * @param text checkKeyEvent方法中传入时文本框中的文本
	 * @param textLength checkKeyEvent方法中传入时文本框中的文本长度
	 * @param keyChar checkKeyEvent方法中传入的按键字符
	 * @param codePoint checkKeyEvent方法中传入的按键代码点
	 * @return 如果符合小于(等于)最大值并大于(等于)最小值的要求返回true
	 */
	protected boolean handleMaxValueAndMinValueValidity(int keyCode,String text,int textLength,char keyChar,int codePoint)
	{
		if(keyCode==KeyEvent.VK_LEFT||keyCode==KeyEvent.VK_RIGHT||keyCode==KeyEvent.VK_HOME) return false;//如果不屏蔽,将有可能删除前面的负号或小数点而越过最大最小值的限制
		if(maxValue!=null&&minValue!=null)
		{
			if(maxValue.compareTo(minValue)<0)
			{
				throw new RuntimeException("Error:MaxValue must be greater than or equal to minValue!");
			}
			if(maxValue.compareTo(minValue)==0&&!(isMaxValueAllowed&&isMinValueAllowed))
			{
				throw new RuntimeException("Error:Unable to judge the endpoint value as the properties named isMaxValueAllowed and isMinValueAllowed!");
			}
		}
		checkMaxValueAndMinValue();
		if(maxValue!=null)
		{
			maxValue=maxValue.stripTrailingZeros();//处理不能大于最大值应从个位开始把关每一位
			int maxValueStartCheckIndex=getValueStartCheckIndex(maxValue);
			if(textLength==0)
			{
				if(maxValue.signum()<0&&!isMinus(codePoint))
				{//如果是最大值是负数,则限制了第一位只能为负号
					JohnSwingUtilities.showToolTip(maxValueTip+(isMaxValueAllowed?"":equalString)+getMaxValue().floatValue(), this, 2000);
					return false;
				}
				else if(maxValue.signum()==0)
				{//如果是最大值是0,则限制了第一位只能为负号或零,但如果不能等于最大值,则只能是负号
					if(isMaxValueAllowed&&!(isMinus(codePoint)||isZero(codePoint)))
					{
						JohnSwingUtilities.showToolTip(maxValueTip+getMaxValue().floatValue(), this, 2000);
						return false;
					}
					else if(!isMaxValueAllowed&&!isMinus(codePoint))
					{
						JohnSwingUtilities.showToolTip(maxValueTip+equalString+getMaxValue().floatValue(), this, 2000);
						return false;
					}
				}
			}
			if(textLength>=maxValueStartCheckIndex&&isDigit(codePoint))
			{
				BigDecimal value;
				try
				{
					value=new BigDecimal(text+keyChar);
				} catch (NumberFormatException e1)
				{
					JOptionPane.showMessageDialog(null, numberFormatExceptionTip, "", JOptionPane.WARNING_MESSAGE);
					value=null;
					setText("");
					return false;
				}
				if(value!=null)
				{
					if(isMaxValueAllowed&&value.compareTo(maxValue)>0)
					{
						alwaysTipValue(maxValueTip+getMaxValue().floatValue());
						return false;
					}
					else if(!isMaxValueAllowed&&value.compareTo(maxValue)>=0)
					{
						alwaysTipValue(maxValueTip+equalString+getMaxValue().floatValue());
						return false;
					}
				}
			}
		}
		if(minValue!=null)
		{
			minValue=minValue.stripTrailingZeros();
			int minValueStartCheckIndex=getValueStartCheckIndex(minValue);//处理不能小于于最小值应从个位开始把关每一位
			if(textLength==0)
			{
				if(minValue.signum()>=0&&isMinus(codePoint))
				{//如果是最小值是正数或零,则限制了第一位不能为负号
					JohnSwingUtilities.showToolTip(minValueTip+(isMinValueAllowed?"":equalString)+getMinValue().floatValue(), this, 2000);
					return false;
				}
				if(!isMinValueAllowed&&minValue.signum()==0&&isZero(codePoint))
				{//最小值不允许等于0确是以0开头,需要屏蔽0,但如果用户意图是输入0.5,则应该允许以.5的方式输入
					JohnSwingUtilities.showToolTip(decimalStartWidthZeroHelpTip, this, 2000);
					return false;
				}
			}
			if(textLength>=minValueStartCheckIndex&&isDigit(codePoint))
			{
				BigDecimal value;
				try
				{
					value=new BigDecimal(text+keyChar);
				} catch (NumberFormatException e1)
				{
					JOptionPane.showMessageDialog(null, numberFormatExceptionTip, "", JOptionPane.WARNING_MESSAGE);
					value=null;
					setText("");
					return false;
				}
				if(value!=null)
				{
					if(isMinValueAllowed&&value.compareTo(minValue)<0)
					{
						alwaysTipValue(minValueTip+getMinValue().floatValue());
						return false;
					}
					else if(!isMinValueAllowed&&value.compareTo(minValue)<=0)
					{
						alwaysTipValue(minValueTip+equalString+getMinValue().floatValue());
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**仅做抛出异常用,仅当以下情况:1.maxValue设置为负数;2.minValue设置为正数;*/
	protected void checkMaxValueAndMinValue()
	{
		if(maxValue!=null&&maxValue.signum()<0) throw new IllegalArgumentException("So far maxValue must be greater than or equal to zero!");
		if(minValue!=null&&minValue.signum()>0) throw new IllegalArgumentException("So far minValue must be less than or equal to zero!");
		
	}
	
	/**获取开始校验的数值位置,一般为value的个位所在的索引*/
	protected int getValueStartCheckIndex(BigDecimal value)
	{
		int scale=value.scale();
		int precision=value.precision();
		int signum=value.signum();
		int valueStartCheckIndex=signum>0?(precision-scale-1):(precision-scale);
		if(precision==scale) valueStartCheckIndex+=1;
		return valueStartCheckIndex;
	}
	
	/** 提前提示用户不能大于最大值或不能小于最小值*/
	protected void alwaysTipValue(String tip)
	{
		JohnSwingUtilities.showToolTip(tip, this, 2000);
	}
	
	/**
	 * 在不允许输入0值,但小数允许时,是否允许类似0.1形式的小数,还是仅依托.1形式的小数;
	 * 如果覆写此方法以便做更细致的校验或更豪华的提示,请确保在不允许0.1形式的情况下让此方法返回false;
	 * 如果仅依托.1的形式,即默认值时,将自动为文本框做提示.
	 */
	protected boolean isDecimalStartWidthZeroAllowed()
	{
		if(!isDecimalStartWidthZeroAllowed)
		{
			JohnSwingUtilities.showToolTip(decimalStartWidthZeroHelpTip, this, 2000);
		}
		return isDecimalStartWidthZeroAllowed;
	}

	/**
	 * 在不允许输入0值,但小数允许时,设置是否允许类似0.1形式的小数,还是仅依托.1形式的小数;
	 * 默认为false,即仅允许.1的形式;如果设置为true,又必须确保该文本框值不能为0,必须实现InputVerifier或添加FocusListener或KeyListener做进一步校验.
	 */
	public void setDecimalStartWidthZeroAllowed(
			boolean isDecimalStartWidthZeroAllowed)
	{
		this.isDecimalStartWidthZeroAllowed = isDecimalStartWidthZeroAllowed;
	}

	/**是否不允许输入负数*/
	public boolean isNegativeUnAllowed()
	{
		return isNegativeUnAllowed;
	}

	/**设置不允许输入负数*/
	public void setNegativeUnAllowed(boolean isNegativeUnAllowed)
	{
		this.isNegativeUnAllowed = isNegativeUnAllowed;
	}

	/**是否不允许输入数值而非数字0*/
	public boolean isZeroUnAllowed()
	{
		return isZeroUnAllowed;
	}

	/**设置不允许输入数值而非数字0*/
	public void setZeroUnAllowed(boolean isZeroUnAllowed)
	{
		this.isZeroUnAllowed = isZeroUnAllowed;
	}

	/**是否不允许输入小数*/
	public boolean isDecimalUnAllowed()
	{
		return isDecimalUnAllowed;
	}

	/**设置不允许输入小数*/
	public void setDecimalUnAllowed(boolean isDecimalUnAllowed)
	{
		this.isDecimalUnAllowed = isDecimalUnAllowed;
	}
	
	/**获取允许的最大值,由于暂只能限制正数范围内的值返回值大于等于0或为null*/
	public BigDecimal getMaxValue()
	{
		return maxValue;
	}

	/**设置允许的最大值,如果想取消最大值比较,请传入null,注:暂只能设置正数范围内的值,即maxValue必须大于等于0*/
	public void setMaxValue(BigDecimal maxValue,boolean isMaxValueAllowed)
	{
		this.maxValue = maxValue;
		this.isMaxValueAllowed=isMaxValueAllowed;
	}

	/**获取允许的最小值,由于暂只能限制正数范围内的值返回值小于等于0或为null*/
	public BigDecimal getMinValue()
	{
		return minValue;
	}

	/**设置允许的最小值,如果想取消最小值比较,请传入null,注:暂只能设置负数范围内的值,即minValue必须小于等于0*/
	public void setMinValue(BigDecimal minValue,boolean isMinValueAllowed)
	{
		this.minValue = minValue;
		this.isMinValueAllowed=isMinValueAllowed;
	}
	
	/** 返回两个长度的数组,第一个表示是否可以输入最大值本身,第二个表示是否可以输入最小值本身,为true,表示可以*/
	public boolean[] isMaxValueAndMinValueAllowed()
	{
		return new boolean[]{isMaxValueAllowed,isMinValueAllowed};
	}
}
