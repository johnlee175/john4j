package com.johnsoft.library.swing.component.datechooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.johnsoft.library.util.common.JohnDateHelper;
import com.johnsoft.library.util.common.JohnStringUtil;
import com.johnsoft.library.util.gui.JohnGridBagHelper;

/**
 * 改写自李哲浩的JohnExtDateChooser<br/>
 * Example:<br/>
 * JTextField field=new JTextField();<br/>
 * JPanel pane=JohnExtDateChooser.installTrigger(field, pattern, hGap);<br/>
 * Container container=new Container();<br/>
 * container.add(pane);<br/>
 * @author 桑学文
 * 2013-04-25
 */
public class JohnExtNoButtonDateChooser extends JohnNoButtonDateChooser
{
	private static final long serialVersionUID = 1L;
	
	public String defaultText;//文本框中默认提示文字
	
	private JButton okButton;
	
	public JTextField field;//日期上限文本框
	public JTextField lowField;//日期下限文本框
	
	private static Icon icon;//日历控件图标
	private static Icon rolloverIcon;//日历控件悬停图标
	
	static{
		icon=new ImageIcon("images/calendar.png");
		rolloverIcon=new ImageIcon("images/calendar_hover.png");
	}
	
	/**
	 * 获取默认的日历控件触发器,如果field不为null将按field高度设置触发器大小
	 */
	public static JButton getDateChooserTrigger(JTextField field)
	{
		JButton trigger=new JButton();//new BasicArrowButton(SwingConstants.SOUTH);
		trigger.setIcon(icon);
		trigger.setRolloverIcon(rolloverIcon);
		trigger.setFocusPainted(false);
		trigger.setContentAreaFilled(false);
		if(field!=null)
		{
			int height=field.getPreferredSize().height;
			trigger.setPreferredSize(new Dimension(height+2, height+2));
		}
		return trigger;
	}
	
	public static JPanel installTrigger(int key,JTextField field,String pattern,int...hMargin)
	{
		int hPadding=10;
		if(hMargin!=null&&hMargin.length>0)
		{
			hPadding=hMargin[0];
		}
		
		JButton bab=getDateChooserTrigger(field);
		JohnExtNoButtonDateChooser chooser=new JohnExtNoButtonDateChooser(pattern,true);
	    chooser.register(key,field,null,bab);
	    chooser.defaultText="";
	    chooser.setRemembered(false);
	    field.setEditable(false);
		
	    JPanel pane=new JPanel();
		JohnGridBagHelper helper=new JohnGridBagHelper(pane);
		helper.othbounds(GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,new Insets(5, hPadding, 5, 0));
		helper.weight(1.0, 0.0).ipad(1, 3).bounds(0, 0, 3, 1).add(field);
		helper.getGridBagConstraints().insets=new Insets(5, 0, 5, hPadding);
		helper.weight(0.0, 0.0).ipad(1, 1).bounds(3, 0, 1, 1).add(bab);
		return pane;
	}
	
	public static JPanel installTrigger(int key,JTextField field,String pattern,JTextField buddyField,boolean isFirstFieldLower,int...hMargin)
	{
		int hPadding=10;
		if(hMargin!=null&&hMargin.length>0)
		{
			hPadding=hMargin[0];
		}
		
		JButton bab=getDateChooserTrigger(field);
		JohnExtNoButtonDateChooser chooser=new JohnExtNoButtonDateChooser(pattern,true);
	    chooser.register(key,field,null,bab);
	    chooser.defaultText="";
	    chooser.setRemembered(false);
	    field.setEditable(false);
	    
	    if(isFirstFieldLower)
		{
			chooser.lowField=field;
			chooser.field=buddyField;
		}else{
			chooser.lowField=buddyField;
			chooser.field=field;
		}
		
	    JPanel pane=new JPanel();
		JohnGridBagHelper helper=new JohnGridBagHelper(pane);
		helper.othbounds(GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,new Insets(5, hPadding, 5, 0));
		helper.weight(1.0, 0.0).ipad(1, 3).bounds(0, 0, 3, 1).add(field);
		helper.getGridBagConstraints().insets=new Insets(5, 0, 5, hPadding);
		helper.weight(0.0, 0.0).ipad(1, 1).bounds(3, 0, 1, 1).add(bab);
		return pane;
	}

	public JohnExtNoButtonDateChooser()
	{
		super();
	}

	/**
	 * @param format 文本框中将显示的日期格式
	 * @param isChina 日期控件标签是否使用中文
	 */
	public JohnExtNoButtonDateChooser(String format, boolean isChina)
	{
		super(format, isChina);
	}

	/**
	 * 为改变日历控件滑过效果覆写
	 */
	@Override
	protected MyLabel getMyLabelInstance(int year, int month, int day, int index)
	{
		return new ZLabel(year, month, day, index);
	}
	
	/**
	 * 为取消在窗口移动时,随同移动日历控件覆写
	 */
	@Override
	public void followMoveAndShow(Window win){
	}
	
	/**
	 * 为取消toggle效果和失焦隐藏日历控件覆写
	 */
	@Override
	protected void initDialogDefaults()
	{
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
		dialog.getRootPane().setDefaultButton(okButton);
		dialog.addWindowFocusListener(new WindowFocusListener()
		{
			@Override
			public void windowLostFocus(WindowEvent e)
			{
				isShow=false;//此句促使按钮不再成为Toggle控制器
				dialog.setVisible(false);
			}
			@Override
			public void windowGainedFocus(WindowEvent e){
			}
		});
	}

	public void initPanel(int key)
	{
		headPane = new JHeadPane(key);
		dayPane = new JDayPane();
		datePane = new JDatePane(key);
		footPane = new FootPane();
	}
	
	/**
	 * 判断日期下限是否大于上限
	 */
	protected boolean check(String text1,String text2,String defaultText,String pattern,String message)
	{
		if(JohnStringUtil.isUnemptyAndUnEqual(text1, new String[]{defaultText})&&JohnDateHelper.isDate(pattern, text1))
		{
			if(JohnStringUtil.isUnemptyAndUnEqual(text2, new String[]{defaultText})&&JohnDateHelper.isDate(pattern, text2))
			{
				if(!JohnDateHelper.isTimeOfFirstItemLower(pattern, text1, text2))
				{
					JOptionPane.showMessageDialog(field.getTopLevelAncestor(), message);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 添加区间检测
	 */
	@Override
	protected void hidePanel()
	{
		if(lowField==null||field==null)
		{
			super.hidePanel();
			return;
		}
		if(check(lowField.getText(), field.getText(), defaultText, sdf.toPattern(), "时间段下限必须小于时间段上限,请重新选择!"))
		{
			super.hidePanel();
		}
		showPanel();
	}

	/**
	 * 覆写的日历控件的最底下的面板,安装三个按钮:确定,取消,清空;可选安装时间选择器
	 */
	public class FootPane extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private JLabel timeLab,hourLab, minLab;
		
		public FootPane()
		{
			super(new BorderLayout());
			this.setBackground(getFootPaneColor());
			//时间选择器面板
			if(sdf.toPattern().indexOf("HH:mm")>=0)
			{
				this.setPreferredSize(new Dimension(260, 66));
				JPanel timeContainer = new JPanel();
				timeContainer.setLayout(new BoxLayout(timeContainer, BoxLayout.X_AXIS));
				timeContainer.setPreferredSize(new Dimension(260, 30));
				hourLab = new JLabel();
				minLab = new JLabel();
				HourSpinner spinHour = new HourSpinner(hourLab,
						dateModel.getHourList(), dateModel.getSelectedHour());
				MiniteSpinner spinMin = new MiniteSpinner(minLab,
						dateModel.getMiniteList(), dateModel.getSelectedMinite());
				spinHour.setPreferredSize(new Dimension(80, 25));
				spinMin.setPreferredSize(new Dimension(80, 25));
				timeLab = new JLabel(hourLab.getText()+":"+minLab.getText());
				timeLab.setForeground(getHeadPaneFontColor());
				timeLab.setFont(getHeadFont());
				timeContainer.add(spinHour);
				timeContainer.add(timeLab);
				timeContainer.add(spinMin);
				timeContainer.setBackground(getFootPaneColor());
				for (int i = 0; i < timeContainer.getComponentCount(); i++)
				{
					Component comptemp = timeContainer.getComponent(i);
					if (comptemp instanceof JLabel)
					{
						((JLabel) comptemp).setOpaque(true);
					}
					comptemp.setBackground(getFootPaneColor());
				}
				this.add(timeContainer, BorderLayout.NORTH);
				
				hourLab.addPropertyChangeListener(new PropertyChangeListener()
				{
					@Override
					public void propertyChange(PropertyChangeEvent evt)
					{
						setTimeModeOnChange();
					}
				});
				minLab.addPropertyChangeListener(new PropertyChangeListener()
				{
					@Override
					public void propertyChange(PropertyChangeEvent evt)
					{
						setTimeModeOnChange();
					}
				});
			}
		}
		
		/**
		 * 随时间选择器的选择,改变之间的时间提示
		 */
		public void setTimeModeOnChange()
		{
			select.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(hourLab.getText()));
			select.set(Calendar.MINUTE, Integer.parseInt(minLab.getText()));
			timeLab.setText(hourLab.getText()+" : "+minLab.getText());
		}
	}

	/**
	 * 改变鼠标指针在日历面板滑动时的效果:手型,放大
	 */
	public class ZLabel extends MyLabel
	{
		private static final long serialVersionUID = 1L;

		public ZLabel(int year, int month, int day, int index)
		{
			super(year, month, day, index);
		}

		@Override
		protected void setOnDateCursor()
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		@Override
		protected void setOverDateStyle()
		{
			setFont(new Font("Courier New", Font.PLAIN, 18));
		}

		@Override
		protected void setOutDateStyle()
		{
			setFont(new Font("Courier New", Font.PLAIN, 12));
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(isEnabled())
			{
				Point p = SwingUtilities.convertPoint(this, e.getPoint(), datePane);
				labelmgr.setSelect(p, false);
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(e.getButton()==MouseEvent.BUTTON1)
			{
				commit();
			}
		}
		
	}
	
	
}
