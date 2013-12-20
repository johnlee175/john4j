package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.johnsoft.library.util.common.JohnStringUtil;

/**可校验时间的输入框,格式为HHmm*/
public class JohnTimeInput extends JTextField
{
	private static final long serialVersionUID = 1L;
	
	private Border oldBorder=getBorder();
	private Border newBorder=BorderFactory.createLineBorder(Color.RED);
	
	private boolean selectAllOnFocus;
	
	public JohnTimeInput(long millionseconds)
	{
		String dtStr=parseDateTime(millionseconds);
		createTimeInput(dtStr);
	}
	
	protected String parseDateTime(long millionseconds)
	{
		return new SimpleDateFormat("HHmm").format(new Date(millionseconds));
	}

	protected void createTimeInput(final String timeString)
	{
		setDocument(new PlainDocument()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException
			{
				if(JohnStringUtil.isNotEmpty(str))
				{
					str=str.replaceAll("\\s+", str);
					String text=JohnTimeInput.this.getText().replaceAll("\\s+", str);
					try
					{
						Integer.parseInt(str);
						if(!text.isEmpty())
							Integer.parseInt(text);
					} catch (Exception e)
					{
						return;
					}
					switch (text.length())
					{
					case 0:
					{
						if(Integer.parseInt(""+str.charAt(0))>2)
							str="0"+str;
						if(str.length()>4)
							str=str.substring(0, 4);
					}
						break;
					case 1:
					{
						if(text.charAt(0)=='2')
						{
							if(Integer.parseInt(""+str.charAt(0))>3)
							{
								if(Integer.parseInt(""+str.charAt(0))>5)
								{
									str="00"+str;
								}else{
									str="0"+str;
								}
							}
						}
						if(str.length()>3)
							str=str.substring(0, 3);
					}
						break;
					case 2:
					{
						if(Integer.parseInt(""+str.charAt(0))>5)
							str="0"+str;
						if(str.length()>2)
							str=str.substring(0, 2);
					}
						break;
					case 3:
					{
						if(str.length()>1)
							str=str.substring(0, 1);
					}
						break;
					default:
						return;
					}
					String p=text.substring(0, offs)+str+text.substring(offs);
					if(p.length()>4) return;
					if(p.length()>=2&&Integer.parseInt(p.substring(0, 2))>23)
						return;
					if(p.length()==4&&Integer.parseInt(p.substring(2, 4))>59)
						return;
					super.insertString(offs, str, a);
				}
			}
		});
		setInputVerifier(new InputVerifier()
		{
			@Override
			public boolean verify(JComponent c)
			{
				boolean valid=isInputValid();
				if(!valid)
				{
					setBorder(newBorder);
				}else{
					setBorder(oldBorder);
				}
				return valid;
			}
		});
		addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				if(isInputValid())
					setText(timeString);
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				 if(selectAllOnFocus)
				 {
					 selectAll();
				 }
			}
		});
		setText(timeString);
	}
	
	public long getValue()
	{
		String time=getText();
		SimpleDateFormat sdf=new SimpleDateFormat("HHmm");
		try
		{
			return sdf.parse(time).getTime();
		} catch (ParseException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean isInputValid()
	{
		String t=getText();
		if(t.length()!=4)
		{
			return false;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("HHmm");
		sdf.setLenient(false);
		try
		{
			sdf.parse(t);
			return true;
		} catch (ParseException e)
		{
			return false;
		}
	}
	
	public void setSelectAllOnFocus(boolean selectAllOnFocus)
	{
		this.selectAllOnFocus=selectAllOnFocus;
	}
}
