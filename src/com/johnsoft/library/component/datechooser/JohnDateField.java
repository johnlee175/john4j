package com.johnsoft.library.component.datechooser;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTextField;

/**
 * 日期文本框的包装校验器,
 * 可检测当前文本框日期是否小于下限文本框日期,
 * 解析用户习惯的日期输入格式并转换为所需格式
 * @author john
 */
public class JohnDateField implements FocusListener
{
	protected Date date;//存储文本框字符串解析后的日期
	protected String pattern;//文本框需要的结果日期格式
	protected JohnDateField dateField;//在区域日期模式下,关联下限日期文本框的包装校验器
	protected JTextField textField;//被包装的文本框
	protected String dateString;//在区域日期模式下,关联下限日期文本框的值
	protected JohnDateFormat mainFormat;//文本框结果日期格式器
	protected String[] defaultPatterns;//用户可输入的默认日期格式集
	protected String[] customPatterns=new String[]{};//用户可输入的定制日期格式集
	protected PatternType patternType=PatternType.DATE;//默认日期格式类型
	
	/**
	 * 枚举默认日期格式类型
	 * @author john
	 */
  public enum PatternType {
  		DATE,TIME,FULL;
  }

	//构造方法开始
  public JohnDateField()
  {
  	this(new JTextField());
  }
  
  public JohnDateField(JTextField textField)
  {
  	this(textField,"yyyy-MM-dd");
  }
  
  public JohnDateField(String pattern)
  {
  	this(new JTextField(),pattern);
  }
  
  public JohnDateField(JohnDateField dateField)
  {
  	this(new JTextField(),"yyyy-MM-dd",dateField);
  }
  
  public JohnDateField(JTextField textField,String pattern)
  {
  	this(textField,pattern,null);
  }
  
  public JohnDateField(JTextField textField,String pattern,JohnDateField dateField)
  {
  	this.textField=textField;
  	this.dateField=dateField;
  	this.pattern=pattern;
  	mainFormat=new JohnDateFormat(pattern);
  	mainFormat.setLenient(false);
  	this.textField.addFocusListener(this);
  }
  //构造方法结束
  
  @Override
	public void focusGained(FocusEvent e){
  	if(dateField!=null)
  	{
  		dateString=dateField.getText();
  	}
	}
	
	@Override
	public void focusLost(FocusEvent e)
	{
		if(getText().equals(""))
		{
			return;
		}
		if(getPatternType()==PatternType.DATE)
		{
			defaultPatterns=new String[]{
					"yyyy-MM-dd","yyyy-M-d",
					"yyyy/MM/dd","yyyy/M/d",
					"yyyy.MM.dd","yyyy.M.d",
					"dd/MM/yyyy","d/M/yyyy",
					"dd.MM.yyyy","d.M.yyyy",
					"yyyyMMdd"
			};
		}
		else if(getPatternType()==PatternType.TIME)
		{
			defaultPatterns=new String[]{
					"HH:mm:ss","HH:mm",
					"HHmmss"
			};
		}
		else 
		{
			defaultPatterns=new String[]{
					"yyyy-MM-dd HH:mm:ss","yyyy-M-d HH:mm",
					"yyyy/MM/dd HH:mm:ss","yyyy/M/d HH:mm",
					"yyyy.MM.dd HH:mm:ss","yyyy.M.d HH:mm",
					"dd/MM/yyyy HH:mm:ss","d/M/yyyy HH:mm",
					"dd.MM.yyyy HH:mm:ss","d.M.yyyy HH:mm",
					"yyyyMMddHHmmss"
			};
		}
		defaultDateHandler();
	}
  
	/**
	 * 默认的文本框失焦格式转化器,处理从输入的格式到需要的格式
	 */
	protected void defaultDateHandler()
	{
		String txt=getText();
		if(mainFormat.canParse(txt))
		{
			date=mainFormat.parse(txt);
		}
		if(date==null)
		{
			JohnDateFormat format;
		  for(int i=0;i<defaultPatterns.length;i++)
		  {
		  	format=new JohnDateFormat(defaultPatterns[i]);
		  	format.setLenient(false);
		  	if(format.canParse(txt))
		  	{
		  		date=format.parse(txt);
		  		break;
		  	}else{
		  		continue;
		  	}
		  }
		}
		localeFormatHandler();
		customFormatHandler();
		if(date==null)
		{
			setText("");
			return;
		}
		setText(mainFormat.format(date));
		compareDate();
		date=null;
	}

	//在设置为区间日期模式的情况下,比较上下限文本框日期的大小
	private void compareDate()
	{
		if(dateString!=null)
		{
			Date lowdate=dateField.getMainFormat().parse(dateString);
			if(lowdate.getTime()>date.getTime())
			{
				setText("");
			}
		}
	}
	
	/**
	 * 尝试采用当地语言环境默认的格式解析
	 */
	protected void localeFormatHandler()
	{
		String txt=getText();
		if(date==null)
		{
			DateFormat format;
			if(getPatternType()==PatternType.DATE)
			{
				format=DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.getDefault());
				format.setLenient(false);
				try
				{
					date=format.parse(txt);
				} catch (Exception e)
				{
					return;
				}
			}
			else if(getPatternType()==PatternType.TIME)
			{
				format=DateFormat.getTimeInstance(DateFormat.DEFAULT,Locale.getDefault());
				format.setLenient(false);
				try
				{
					date=format.parse(txt);
				} catch (Exception e)
				{
					return;
				}
			}
			else
			{
				format=DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT,Locale.getDefault());
				format.setLenient(false);
				try
				{
					date=format.parse(txt);
				} catch (Exception e)
				{
					return;
				}
			}
		}
	}

	/**
	 * 在默认的格式无法解析文本框中字符串为日期时,如何客户化处理
	 */
	protected void customFormatHandler()
	{
		String txt=getText();
		if(date==null)
		{
			JohnDateFormat format;
		  for(int i=0;i<customPatterns.length;i++)
		  {
		  	format=new JohnDateFormat(customPatterns[i]);
		  	format.setLenient(false);
		  	if(format.canParse(txt))
		  	{
		  		date=format.parse(txt);
		  		return;
		  	}else{
		  		continue;
		  	}
		  }
		}
	}
 
	/**
	 * 给文本框赋值
	 */
	public void setText(String text)
	{
		textField.setText(text);
	}
	
	/**
	 * 获取文本框的值
	 */
	public String getText()
	{
		return textField.getText();
	}
	
	/**
	 * 获取文本框失焦时将转化成的日期格式
	 */
	public JohnDateFormat getMainFormat()
	{
		return mainFormat;
	}
	
	/**
	 *获取客户化日期格式
	 */
	public String[] getCustomPatterns()
	{
		return customPatterns;
	}
  
	/**
	 * 设置客户化日期格式
	 */
	public void setCustomPatterns(String[] customPatterns)
	{
		this.customPatterns = customPatterns;
	}

	/**
	 * 获取内置的日期格式
	 */
	public String[] getDefaultPatterns()
	{
		return defaultPatterns;
	}

	/**
	 * 设置默认日期格式类型
	 */
	public void setPatternType(PatternType patternType)
	{
		this.patternType=patternType;
	}
	
	/**
	 * 获取默认日期格式类型,有日期,时间,日期和时间
	 */
	public PatternType getPatternType()
	{
		return patternType;
	}

	
	/**
	 * 为SimpleDateFormat增加一个判断能否解析字符串为日期的方法,
	 * 隐藏parse方法抛出的异常
	 * @author john
	 */
	public class JohnDateFormat extends SimpleDateFormat
	{
		private static final long serialVersionUID = 1L;

		public JohnDateFormat(String pattern)
		{
			super(pattern);
		}
		
		public JohnDateFormat(String pattern,Locale locale)
		{
			super(pattern, locale);
		}
		
		/**
		 * 判断以当前的日期模型能否解析字符串为日期
		 */
		public boolean canParse(String date)
		{
			try
			{
				super.parse(date);
				return true;
			} catch (ParseException e)
			{
				return false;
			}
		}
		
		@Override
		public Date parse(String date)
		{
			try
			{
				return super.parse(date);
			} catch (ParseException e)
			{
				return null;
			}
		 }
	 }
}


