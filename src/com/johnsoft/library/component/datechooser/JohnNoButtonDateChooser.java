package com.johnsoft.library.component.datechooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;

import com.johnsoft.library.component.JohnSpinner;
import com.johnsoft.library.component.datechooser.JohnDateField.PatternType;
import com.johnsoft.library.util.JohnStringUtil;


/**
 * 日期选择器类，通过静态调用getInstance方法，其中可传入日期格式，即可获得该类的实例；
 * 再调用register方法，将JLabel或者JTextField作为参数传入即可把JLabel或JTextField注册为日期控件；
 */
public class JohnNoButtonDateChooser extends JPanel
{
	private static final long serialVersionUID = 1L;

	protected JohnDateModel dateModel;
	protected String[] forLocale = new String[] { "时间", "今天:", "一", "二", "三",
			"四", "五", "六", "日", "点击选择今天日期" };
	private String[] forEnglish = new String[] { "Time", "Today:", "Mon",
			"Tues", "Wed", "Thur", "Fri", "Sat", "Sun",
			"Click me and select today" };
	protected JPanel headPane;
	protected JPanel dayPane;
	protected JPanel datePane;
	protected JPanel footPane;
	private Date initDate;
	private Calendar now = Calendar.getInstance();
	protected Calendar select;
	protected JDialog dialog;
	private JPanel mainPanel;
	final LabelManager labelmgr = new LabelManager();
	protected SimpleDateFormat sdf;
	protected boolean isChina;
	protected boolean isShow = false;// 日历控件是否在显示中
	protected JComponent dateField;// 要注册的文本框或标签
	protected JohnDateField lowDateField;
	protected JComponent dateButton;// 触发日历控件显示的按钮
	
	private String oldText;
	private boolean remembered=true;//是否记忆上次日期
	
	private EventListenerList listenerList=new EventListenerList();

	// 以下是style
	private Font headFont = new Font("宋体", Font.BOLD, 14);
	private Font dayFont = new Font("宋体", Font.BOLD, 12);
	private Font dateFont = new Font("Courier New", Font.PLAIN, 12);
	private Font footFont = new Font("宋体", Font.BOLD, 12);
	private Font todayLabFont;
	private Border mainPaneBorder = BorderFactory.createEtchedBorder();
	private Color headPaneColor = new Color(0xFAFF66);
	private Color dayPaneColor = Color.WHITE;
	private Color datePaneColor = Color.WHITE;
	private Color footPaneColor = new Color(0xFAFFCC);
	private Color headPaneFontColor = Color.BLACK;
	private Color footPaneFontColor = Color.RED;
	private Color fieldOverColor = Color.CYAN;
	private Color fieldOutColor = Color.BLACK;
	private Color fieldDownColor = Color.RED;
	private Color fieldUpColor = Color.BLACK;
	private Color todayLabOverColor = Color.RED;
	private Color todayLabOutColor = Color.BLUE;
	private Color todayLabDownColor = Color.CYAN;
	private Color todayLabUpColor = Color.BLUE;
	private Color weekendFontColor = Color.RED;
	private Color weekFontColor = Color.BLUE;
	private Color dateOverColor = Color.PINK;
	private Color dateOverBorderColor = Color.WHITE;
	private Color selectedDayFillColor = Color.YELLOW;
	private Color selectedDayBorderColor = Color.RED;
	private Color todayFillColor = Color.LIGHT_GRAY;
	private Color todayBorderColor = Color.GREEN;
	private Color dateNoInMonthColor = Color.LIGHT_GRAY;

	// 获取实例与构造方法开始
	public JohnNoButtonDateChooser()
	{
		this(new Date());
	}

	public JohnNoButtonDateChooser(Date date)
	{
		this(date, false);
	}

	public JohnNoButtonDateChooser(boolean isChina)
	{
		this(new Date(), isChina);
	}

	public JohnNoButtonDateChooser(String format)
	{
		this(new Date(), format);
	}

	public JohnNoButtonDateChooser(Date date, boolean isChina)
	{
		this(date, "yyyy-MM-dd HH:mm:ss", isChina);
	}

	public JohnNoButtonDateChooser(String format, boolean isChina)
	{
		this(new Date(), format, isChina);
	}

	public JohnNoButtonDateChooser(Date date, String format)
	{
		this(date, format, false);
	}

	public JohnNoButtonDateChooser(Date date, String format, boolean isChina)
	{
		this.isChina = isChina;
		initDate = date;
		sdf = new SimpleDateFormat(format);
		select = Calendar.getInstance();
		select.setTime(initDate);
	}

	// 获取实例与构造方法结束

	/**
	 * 日历控件是否可用
	 */
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		dateField.setEnabled(b);
		if(dateField!=dateButton)
		{
			dateButton.setEnabled(false);
		}
	}
	
	/**
	 * 设置是否能输入
	 */
	public void setCanInput(boolean canInput)
	{
		if(dateField instanceof JTextField)
		{
			((JTextField)dateField).setEditable(canInput);
			((JTextField)dateField).setFocusable(canInput);
		}
	}

	/**
	 * 得到当前选择框的日期
	 */
	public Date getDate()
	{
		return select.getTime();
	}

	/**
	 * 得到当前选择框的日期的格式化字符串
	 */
	public String getStrDate()
	{
		return sdf.format(select.getTime());
	}

	/**
	 * 得到当前选择框的日期的格式化字符串
	 */
	public String getStrDate(String format)
	{
		sdf = new SimpleDateFormat(format);
		return sdf.format(select.getTime());
	}

	/**
	 * 初始化数据模型
	 */
	protected void initDateModel()
	{
		dateModel = new JohnDateModel();
	}

	/**
	 * 初始化四大面板:JHeadPane,JDayPane,JDatePane,JFootPane
	 */
	protected void initPanel(int key)
	{
		headPane = new JHeadPane(key);
		dayPane = new JDayPane();
		datePane = new JDatePane(key);
		footPane = new JFootPane();
	}

	private void execute(int key)
	{
		initDateModel();
		initPanel(key);
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(mainPaneBorder);
		JPanel up = new JPanel(new BorderLayout());
		up.add(headPane, BorderLayout.NORTH);
		up.add(dayPane, BorderLayout.CENTER);
		mainPanel.add(up, BorderLayout.NORTH);
		mainPanel.add(datePane, BorderLayout.CENTER);
		mainPanel.add(footPane, BorderLayout.SOUTH);
	}

	/**
	 * 设置日期区间
	 */
	public void setRange(JTextField lowField)
	{
		lowDateField = new JohnDateField(lowField, sdf.toPattern());
		lowDateField.setPatternType(PatternType.FULL);
	}

	/**
	 * 将日历控件注册给文本框
	 * 
	 * @param dateField
	 *            文本框
	 * @param isChina
	 *            中文还是英文
	 */
	public void register(int key,final JComponent dateField, Window win,
			final JComponent dateButton)
	{
		
		execute(key);
		this.dateButton=dateButton;
		this.dateField = dateField;
		if (dateField instanceof JTextField)
		{
			JohnDateField upDateField = new JohnDateField(
					(JTextField) dateField, sdf.toPattern(), lowDateField);
			upDateField.setPatternType(PatternType.FULL);
		}
		
		followMoveAndShow(win);
		
		dateButton.setRequestFocusEnabled(true);

		dateButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent me)
			{
				if (dateButton.isEnabled())
				{
					dateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
					dateButton.setForeground(fieldOverColor);
				}
			}

			public void mouseExited(MouseEvent me)
			{
				if (dateButton.isEnabled())
				{
					dateButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					dateButton.setForeground(fieldOutColor);
				}
			}

			public void mousePressed(MouseEvent me)
			{
				dateButton.requestFocusInWindow();
				if (dateButton.isEnabled())
				{
					dateButton.setForeground(fieldDownColor);
					if (isShow)
					{
						hidePanel();
					} else
					{
						showPanel();
					}
				}
			}

			public void mouseReleased(MouseEvent me)
			{
				if (dateButton.isEnabled())
				{
					dateButton.setForeground(fieldUpColor);
				}
			}
		});

		if (dateButton.getClass() != dateField.getClass())
		{
			JPanel pane = new JPanel(new GridLayout(1, 2));
			pane.add(dateField);
			pane.add(dateButton);
			this.add(pane);
		} else
		{
			this.add(dateField);
		}

	}
	
	/**
	 * 设置是否日历控件在win移动时跟随移动到文本框正下方
	 */
	public void followMoveAndShow(Window win)
	{
		win.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				if (isShow)
				{
					showPanel();
				} else
				{
					hidePanel();
				}
			}

			@Override
			public void componentMoved(ComponentEvent e)
			{
				if (isShow)
				{
					showPanel();
				} else
				{
					hidePanel();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e)
			{
				hidePanel();
			}
		});

		win.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowIconified(WindowEvent e)
			{
				hidePanel();
			}
		});
	}

	// 根据新的日期刷新
	void refresh()
	{
		if (datePane instanceof JDatePane)
		{
			((JDatePane) datePane).updateDate();
		}
		SwingUtilities.updateComponentTreeUI(this);
	}

	// 判断是要中文，还是英文格式
	String getEnZhName(int index)
	{
		String[] forLocale = setForLocale();
		if (isChina)
		{
			return forLocale[index];
		} else
		{
			return forEnglish[index];
		}
	}

	/**
	 * 继承覆写此方法，可切换到除中英文的状态
	 * 
	 * @return
	 */
	protected String[] setForLocale()
	{
		return forLocale;
	}

	// 提交日期
	public void commit()
	{
		String text=sdf.format(select.getTime());
		if(text!=null&&!"".equals(text.trim()))
		{
			if (dateField instanceof JTextField)
			{
				((JTextField) dateField).setText(text);
			} else if (dateField instanceof JLabel)
			{
				((JLabel) dateField).setText(text);
			}
		}
		
		hidePanel();
	}

	// 隐藏日期选择面板
	protected void hidePanel()
	{
		isShow = false;
		if (dateField instanceof JTextField)
		{
			JTextField field=(JTextField) dateField;
			String old=field.getText();
			if(old!=null&&old.trim().isEmpty())
			{
				field.setText(oldText);
			}
		} else if (dateField instanceof JLabel)
		{
			JLabel label=(JLabel) dateField;
			String old=label.getText();
			if(old!=null&&old.trim().isEmpty())
			{
				label.setText(oldText);
			}
		}
		if (dialog != null && dialog.isVisible())
		{
			setDialogHide();
		}
		fireChooserHide();
		dateField.requestFocus();
	}

	// 显示日期选择面板
	protected void showPanel()
	{
		if(isEnabled())
		{
			if(remembered)
			{
				if(dateField instanceof JTextField)
				{
					oldText=((JTextField)dateField).getText();
				}
				else if(dateField instanceof JLabel)
				{
					oldText=((JLabel)dateField).getText();
				}
			}
			
			if (dialog != null)
			{
				if (dialog.isVisible())
				{
					dialog.setVisible(false);
				}
			} else
			{
				createDialog();
			}
			Point show = new Point(0, dateField.getHeight());
			SwingUtilities.convertPointToScreen(show, dateField);
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			int x = show.x;
			int y = show.y;
			if (x < 0)
			{
				x = 0;
			}
			if (x > size.width - 295)
			{
				x = size.width - 295;
			}
			if (y < size.height - 170)
			{
			} else
			{
				y -= 188;
			}
			dialog.setLocation(x, y);
			dialog.pack();
			setDialogShow();
			isShow = true;
			fireChooserShow();
		}
	}
	
	/**
	 * 创建日历控件对话框
	 */
	protected void createDialog()
	{
		dialog = new JDialog();
		dialog.getContentPane().add(mainPanel, BorderLayout.CENTER);
		initDialogDefaults();
	}
	
	/**
	 * 初始化日历控件对话框默认值
	 */
	protected void initDialogDefaults()
	{
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
	}

	/**
	 * 覆写此方法可以控制最顶层容器的显示方式，即是否始终在最上层，是淡入淡出
	 */
	protected void setDialogShow()
	{
		dialog.setVisible(true);
	}

	/**
	 * 覆写此方法可以控制最顶层容器的隐藏方式，即是否始终在最上层，是淡入淡出
	 */
	protected void setDialogHide()
	{
		dialog.setVisible(false);
	}

	/**
	 * 最上面的面板用来显示年月的增减
	 */
	protected class JHeadPane extends JPanel
	{
		private static final long serialVersionUID = -5638853772805561174L;

		JLabel yearLab, monthLab, container;

		public JHeadPane(int key)
		{
			super(new BorderLayout());
			initJHeadPane(key);
		}

		private void initJHeadPane(int key)
		{
			YearSpinner spinYear = null;
			MonthSpinner spinMonth = null;
			if(key!=2)
			{
			yearLab = new JLabel();
			monthLab = new JLabel();
			spinYear = new YearSpinner(yearLab,
			dateModel.getYearList(), dateModel.getSelectedYear());
			spinMonth = new MonthSpinner(monthLab,
			dateModel.getMonthList(), dateModel.getSelectedMonth());
	
			spinMonth.setBackground(headPaneColor);
			spinYear.setBackground(headPaneColor);
			}
			container = new JLabel();
			container.setOpaque(true);
			container.setBackground(headPaneColor);
			container.setForeground(headPaneFontColor);
			container.setFont(headFont);
			if(key==2)
			{
				container.setText("            "+dateModel.getSelectedYear() + "年"
						+ dateModel.getSelectedMonth()+ "月");
			}
			else
			{
				container.setText(dateModel.getSelectedYear() + "-"
						+ dateModel.getSelectedMonth());
			}
			
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setBackground(headPaneColor);
			if(key!=2)
			{
			this.add(spinYear);
			}
			this.add(container);
			if(key!=2)
			{
			this.add(spinMonth);
			}
			this.setPreferredSize(new Dimension(260, 35));
			if(key!=2)
			{
			yearLab.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent evt)
				{
					if (yearLab.getText() != "")
					{
						select.set(Calendar.YEAR,
								Integer.parseInt(yearLab.getText()));
						container.setText(yearLab.getText() + "-"
								+ monthLab.getText());
						refresh();
					}
				}
			});
			monthLab.addPropertyChangeListener(new PropertyChangeListener()
			{
					@Override
					public void propertyChange(PropertyChangeEvent evt)
					{
						if (monthLab.getText() != "")
						{
							int v = Integer.parseInt(monthLab.getText());
							select.set(Calendar.MONTH, v - 1);
							container.setText(yearLab.getText() + "-"
									+ monthLab.getText());
							refresh();
						}
					}
				});
			}
		}
	}

	protected class JDayPane extends JPanel
	{
		private static final long serialVersionUID = -8176264838786175724L;

		public JDayPane()
		{
			super(new GridLayout(1, 7));
			String colname[] = { getEnZhName(8), getEnZhName(2),
					getEnZhName(3), getEnZhName(4), getEnZhName(5),
					getEnZhName(6), getEnZhName(7) };
			this.setBackground(dayPaneColor);
			JLabel cell;
			for (int i = 0; i < 7; i++)
			{
				cell = new JLabel(colname[i]);
				cell.setFont(dayFont);
				cell.setHorizontalAlignment(JLabel.CENTER);
				if (i == 0 || i == 6)
				{
					cell.setForeground(weekendFontColor);
				} else
				{
					cell.setForeground(weekFontColor);
				}
				this.add(cell);
			}
			this.setPreferredSize(new Dimension(220, 20));
		}
	}

	protected class JDatePane extends JPanel
	{
		private static final long serialVersionUID = 43157272447522985L;

		public JDatePane(int key)
		{
			super(new GridLayout(6, 7));
			this.setPreferredSize(new Dimension(220, 100));
			this.setBackground(datePaneColor);
			if(key==2)
			{
				initTwoDayJDatePane();
			}else
			{
				initJDatePane();
			}
		}

		private void initJDatePane()
		{
			updateDate();
		}
		private void initTwoDayJDatePane()
		{
			updateTwoDayDate();
		}

		public void updateDate()
		{
			this.removeAll();
			labelmgr.clear();
			Date temp = select.getTime();
			Calendar select1 = Calendar.getInstance();
			select1.setTime(temp);
			select1.set(Calendar.DAY_OF_MONTH, 1);
			int dayOfWeek = select1.get(Calendar.DAY_OF_WEEK);
			select1.add(Calendar.DAY_OF_MONTH, -(dayOfWeek==1?8:dayOfWeek));
			for (int i = 0; i < 42; i++)
			{
				select1.add(Calendar.DAY_OF_MONTH, 1);
				labelmgr.addLabel(getMyLabelInstance(
						select1.get(Calendar.YEAR),
						select1.get(Calendar.MONTH),
						select1.get(Calendar.DAY_OF_MONTH), i));
			}
			for (MyLabel my : labelmgr.getLabels())
			{
				this.add(my);
			}
			select.setTime(temp);
		}
		public void updateTwoDayDate()
		{
			this.removeAll();
			labelmgr.clear();
			Date temp = select.getTime();
			Calendar select1 = Calendar.getInstance();
			select1.setTime(temp);
			select1.set(Calendar.DAY_OF_MONTH, 1);
			int dayOfWeek = select1.get(Calendar.DAY_OF_WEEK);
			select1.add(Calendar.DAY_OF_MONTH, -(dayOfWeek==1?8:dayOfWeek));
			for (int i = 0; i < 42; i++)
			{
				select1.add(Calendar.DAY_OF_MONTH, 1);
				labelmgr.addLabel(getMyLabelInstance(
						select1.get(Calendar.YEAR),
						select1.get(Calendar.MONTH),
						select1.get(Calendar.DAY_OF_MONTH), i,2));
			}
			for (MyLabel my : labelmgr.getLabels())
			{
				this.add(my);
			}
			select.setTime(temp);
		}
	}

	protected MyLabel getMyLabelInstance(int year, int month, int day, int index)
	{
		return new MyLabel(year, month, day, index);
	}
	protected MyLabel getMyLabelInstance(int year, int month, int day, int index,int type)
	{
		return new MyLabel(year, month, day, index,type);
	}

	protected class MyLabel extends JLabel implements Comparator<MyLabel>,
			MouseListener, MouseMotionListener
	{
		private static final long serialVersionUID = 3668734399227577214L;

		private int year, month, day;
		private boolean isSelected;

		public MyLabel(int year, int month, int day, int index)
		{
			super("" + day, JLabel.CENTER);
			this.year = year;
			this.day = day;
			this.month = month;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.setFont(dateFont);
			if (month == select.get(Calendar.MONTH))
			{
				setOnDateCursor();
				this.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseEntered(MouseEvent e)
					{
						setOverDateStyle();
					}

					@Override
					public void mouseExited(MouseEvent e)
					{
						setOutDateStyle();
					}
				});
				if ((index + 1) % 7 == 0 || index % 7 == 0 || index == 0)
				{
					this.setForeground(weekendFontColor);
				} else
				{
					this.setForeground(weekFontColor);
				}
			} else
			{
				this.setForeground(dateNoInMonthColor);
				this.setEnabled(false);
			}
			if (day == select.get(Calendar.DAY_OF_MONTH))
			{
				this.setBackground(Color.RED);
			} else
			{
				this.setBackground(Color.GREEN);
			}
		}
		public MyLabel(int year, int month, int day, int index,int type)
		{
			super("" + day, JLabel.CENTER);
			this.year = year;
			this.day = day;
			this.month = month;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.setFont(dateFont);
			if ((day == select.get(Calendar.DAY_OF_MONTH)) || ((day-1)==select.get(Calendar.DAY_OF_MONTH)))
			{
				setOnDateCursor();
				this.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseEntered(MouseEvent e)
					{
						setOverDateStyle();
					}

					@Override
					public void mouseExited(MouseEvent e)
					{
						setOutDateStyle();
					}
				});
				if ((index + 1) % 7 == 0 || index % 7 == 0 || index == 0)
				{
					this.setForeground(weekendFontColor);
				} else
				{
					this.setForeground(weekFontColor);
				}
			} else
			{
				this.setForeground(dateNoInMonthColor);
				this.setEnabled(false);
			}
			if (day == select.get(Calendar.DAY_OF_MONTH))
			{
				this.setBackground(Color.RED);
			} else
			{
				this.setBackground(Color.GREEN);
			}
		}

		public boolean getIsSelected()
		{
			return isSelected;
		}

		public void setSelected(boolean b, boolean isDrag)
		{
			isSelected = b;
			if (b && !isDrag)
			{
				int temp = select.get(Calendar.MONTH);
				select.set(year, month, day);
				if (temp == month)
				{
					SwingUtilities.updateComponentTreeUI(datePane);
				} else
				{
					refresh();
				}
			}
			this.repaint();
		}

		protected void setOnDateCursor()
		{
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		protected void setOverDateStyle()
		{
			setOpaque(true);
			setBackground(dateOverColor);
			setBorder(BorderFactory.createLineBorder(dateOverBorderColor));
		}

		protected void setOutDateStyle()
		{
			setOpaque(false);
			setBackground(datePaneColor);
			setBorder(null);
		}

		protected void paintComponent(Graphics g)
		{
			if (day == select.get(Calendar.DAY_OF_MONTH)
					&& month == select.get(Calendar.MONTH))
			{
				// 如果当前日期是选择日期,则高亮显示
				g.setColor(selectedDayFillColor);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			if (year == now.get(Calendar.YEAR)
					&& month == now.get(Calendar.MONTH)
					&& day == now.get(Calendar.DAY_OF_MONTH))
			{
				// 如果日期和当前日期一样,则用红框
				Graphics2D gd = (Graphics2D) g;
				gd.setColor(todayFillColor);
				gd.fillRect(0, 0, getWidth(), getHeight());
				gd.setColor(todayBorderColor);
				Polygon p = new Polygon();
				p.addPoint(0, 0);
				p.addPoint(getWidth() - 1, 0);
				p.addPoint(getWidth() - 1, getHeight() - 1);
				p.addPoint(0, getHeight() - 1);
				gd.drawPolygon(p);
			}
			if (isSelected)
			{
				Graphics2D gd = (Graphics2D) g;
				gd.setColor(selectedDayBorderColor);
				Polygon p = new Polygon();
				p.addPoint(0, 0);
				p.addPoint(getWidth() - 1, 0);
				p.addPoint(getWidth() - 1, getHeight() - 1);
				p.addPoint(0, getHeight() - 1);
				gd.drawPolygon(p);
			}
			super.paintComponent(g);
		}

		public boolean contains(Point p)
		{
			return this.getBounds().contains(p);
		}

		private void update()
		{
			repaint();
		}

		public void mouseClicked(MouseEvent e)
		{
		}

		public void mousePressed(MouseEvent e)
		{
			if(isEnabled())
			{
				isSelected = true;
				update();
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			if(isEnabled())
			{
				Point p = SwingUtilities.convertPoint(this, e.getPoint(), datePane);
				labelmgr.setSelect(p, false);
				commit();
			}
		}

		public void mouseEntered(MouseEvent e)
		{
		}

		public void mouseExited(MouseEvent e)
		{
		}

		public void mouseDragged(MouseEvent e)
		{
//			if(isEnabled())
//			{
//				Point p = SwingUtilities.convertPoint(this, e.getPoint(), datePane);
//				labelmgr.setSelect(p, true);
//			}
		}

		public void mouseMoved(MouseEvent e)
		{
		}

		public int compare(MyLabel o1, MyLabel o2)
		{
			Calendar c1 = Calendar.getInstance();
			c1.set(o1.year, o2.month, o1.day);
			Calendar c2 = Calendar.getInstance();
			c2.set(o2.year, o2.month, o2.day);
			return c1.compareTo(c2);
		}
	}

	class LabelManager
	{
		private List<MyLabel> list;

		public LabelManager()
		{
			list = new ArrayList<MyLabel>();
		}

		public List<MyLabel> getLabels()
		{
			return list;
		}

		public void addLabel(MyLabel my)
		{
			list.add(my);
		}

		public void clear()
		{
			list.clear();
		}

		public void setSelect(MyLabel my, boolean b)
		{
			for (MyLabel m : list)
			{
				if(m.isEnabled())
				{
					if (m.equals(my))
					{
						m.setSelected(true, b);
					} else
					{
						m.setSelected(false, b);
					}
				}
			}
		}

		public void setSelect(Point p, boolean b)
		{
			// 如果是拖动,则要优化一下,以提高效率
			if (b)
			{
				// 表示是否能返回,不用比较完所有的标签,能返回的标志就是把上一个标签和
				// 将要显示的标签找到了就可以了
				boolean findPrevious = false, findNext = false;
				for (MyLabel m : list)
				{
					if(m.isEnabled())
					{
						if (m.contains(p))
						{
							findNext = true;
							if (m.getIsSelected())
							{
								findPrevious = true;
							} else
							{
								m.setSelected(true, b);
							}
						} else if (m.getIsSelected())
						{
							findPrevious = true;
							m.setSelected(false, b);
						}
						if (findPrevious && findNext)
						{
							return;
						}
					}
				}
			} else
			{
				MyLabel temp = null;
				for (MyLabel m : list)
				{
					if(m.isEnabled())
					{
						if (m.contains(p))
						{
							temp = m;
						} else if (m.getIsSelected())
						{
							m.setSelected(false, b);
						}
					}
				}
				if (temp != null &&temp.isEnabled())
				{
					temp.setSelected(true, b);
				}
			}
		}
	}

	protected class JFootPane extends JPanel
	{
		private static final long serialVersionUID = -6391305687575714469L;
		private JLabel hourLab, minLab, secLab;

		public JFootPane()
		{
			super(new BorderLayout());
			HashMap<TextAttribute, Object> hm = new HashMap<TextAttribute, Object>();
			hm.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON); // 定义是否有下划线
			hm.put(TextAttribute.SIZE, 12); // 定义字号
			hm.put(TextAttribute.FAMILY, "宋体");// 定义字体名
			hm.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);// 定义斜体
			todayLabFont = new Font(hm);
			this.setPreferredSize(new Dimension(260, 60));
			this.setBackground(footPaneColor);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			final JLabel jl = new JLabel(getEnZhName(1)
					+ sdf.format(new Date()));
			jl.setHorizontalAlignment(JLabel.CENTER);
			jl.setForeground(todayLabOutColor);
			jl.setFont(todayLabFont);
			jl.setToolTipText(getEnZhName(9));
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
			container.setPreferredSize(new Dimension(260, 30));
			hourLab = new JLabel();
			minLab = new JLabel();
			secLab = new JLabel();
			HourSpinner spinHour = new HourSpinner(hourLab,
					dateModel.getHourList(), dateModel.getSelectedHour());
			MiniteSpinner spinMin = new MiniteSpinner(minLab,
					dateModel.getMiniteList(), dateModel.getSelectedMinite());
			SecondSpinner spinSec = new SecondSpinner(secLab,
					dateModel.getSecondList(), dateModel.getSelectedSecond());
			spinHour.setPreferredSize(new Dimension(60, 25));
			spinMin.setPreferredSize(new Dimension(60, 25));
			spinSec.setPreferredSize(new Dimension(60, 25));
			JLabel timeLab = new JLabel(getEnZhName(0));
			timeLab.setForeground(footPaneFontColor);
			timeLab.setFont(footFont);
			container.add(timeLab);
			container.add(spinHour);
			JLabel dot1 = new JLabel("：");
			JLabel dot2 = new JLabel("：");
			dot1.setForeground(footPaneFontColor);
			dot2.setForeground(footPaneFontColor);
			container.add(dot1);
			container.add(spinMin);
			container.add(dot2);
			container.add(spinSec);
			container.setBackground(footPaneColor);
			for (int i = 0; i < container.getComponentCount(); i++)
			{
				Component comptemp = container.getComponent(i);
				if (comptemp instanceof JLabel)
				{
					((JLabel) comptemp).setOpaque(true);
				}
				comptemp.setBackground(footPaneColor);
			}
			this.add(container, BorderLayout.NORTH);
			this.add(jl, BorderLayout.CENTER);
			jl.addMouseListener(new MouseAdapter()
			{
				public void mouseEntered(MouseEvent me)
				{
					jl.setCursor(new Cursor(Cursor.HAND_CURSOR));
					jl.setForeground(todayLabOverColor);
				}

				public void mouseExited(MouseEvent me)
				{
					jl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					jl.setForeground(todayLabOutColor);
				}

				public void mousePressed(MouseEvent me)
				{
					jl.setForeground(todayLabDownColor);
					select.setTime(new Date());
					refresh();
					commit();
				}

				public void mouseReleased(MouseEvent me)
				{
					jl.setForeground(todayLabUpColor);
				}
			});
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
			secLab.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent evt)
				{
					setTimeModeOnChange();
				}
			});
		}

		public void setTimeModeOnChange()
		{
			select.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(hourLab.getText()));
			select.set(Calendar.MINUTE, Integer.parseInt(minLab.getText()));
			select.set(Calendar.SECOND, Integer.parseInt(secLab.getText()));
		}
	}

	protected class MonthSpinner extends JohnSpinner
	{
		private static final long serialVersionUID = 1L;

		public MonthSpinner(JComponent comp, List<String> list,
				String selectedItem)
		{
			super(comp, list, selectedItem);
		}

		@Override
		public void inputException()
		{
			String str = getTextfield().getText();
			if (JohnStringUtil.isNumber(str) && str.length() < 3)
			{
				int istr = Integer.parseInt(str);
				if ((str.length() == 1 && istr > 1)
						|| (str.length() == 2 && istr >= dateModel
								.getMaxMonth()) || str.equals("00"))
				{
					getTextfield().setText("");
				} else
				{
					if (str.equals("0") || str.equals("1"))
					{
					} else
					{
						setJohnSpinnerValue(str);
						setOutComponentValue();
					}
				}
			} else
			{
				getTextfield().setText("");
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String val = getCombox().getSelectedItem().toString();
			if (val.length() == 2)
			{
				setJohnSpinnerValue(val);
				setOutComponentValue();
			}
		}
	}

	protected class HourSpinner extends JohnSpinner
	{
		private static final long serialVersionUID = 1L;

		public HourSpinner(JComponent comp, List<String> list,
				String selectedItem)
		{
			super(comp, list, selectedItem);
		}

		@Override
		public void inputException()
		{
			String str = getTextfield().getText();
			if (JohnStringUtil.isNumber(str) && str.length() < 3)
			{
				int istr = Integer.parseInt(str);
				if ((str.length() == 1 && istr > 2)
						|| (str.length() == 2 && istr >= dateModel.getMaxHour()))
				{
					getTextfield().setText("");
				} else
				{
					if (str.equals("0") || str.equals("1") || str.equals("2"))
					{
					} else
					{
						setJohnSpinnerValue(str);
						setOutComponentValue();
					}
				}
			} else
			{
				getTextfield().setText("");
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String val = getCombox().getSelectedItem().toString();
			if (val.length() == 2)
			{
				setJohnSpinnerValue(val);
				setOutComponentValue();
			}
		}
	}

	protected class MiniteSpinner extends JohnSpinner
	{
		private static final long serialVersionUID = 1L;

		public MiniteSpinner(JComponent comp, List<String> list,
				String selectedItem)
		{
			super(comp, list, selectedItem);
		}

		@Override
		public void inputException()
		{
			String str = getTextfield().getText();
			if (JohnStringUtil.isNumber(str) && str.length() < 3)
			{
				int istr = Integer.parseInt(str);
				if ((str.length() == 1 && istr > 5)
						|| (str.length() == 2 && istr >= dateModel
								.getMaxMinite()))
				{
					getTextfield().setText("");
				} else
				{
					if (str.equals("0") || str.equals("1") || str.equals("2")
							|| str.equals("3") || str.equals("4")
							|| str.equals("5"))
					{
					} else
					{
						setJohnSpinnerValue(str);
						setOutComponentValue();
					}
				}
			} else
			{
				getTextfield().setText("");
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String val = getCombox().getSelectedItem().toString();
			if (val.length() == 2)
			{
				setJohnSpinnerValue(val);
				setOutComponentValue();
			}
		}
	}

	protected class SecondSpinner extends JohnSpinner
	{
		private static final long serialVersionUID = 1L;

		public SecondSpinner(JComponent comp, List<String> list,
				String selectedItem)
		{
			super(comp, list, selectedItem);
		}

		@Override
		public void inputException()
		{
			String str = getTextfield().getText();
			if (JohnStringUtil.isNumber(str) && str.length() < 3)
			{
				int istr = Integer.parseInt(str);
				if ((str.length() == 1 && istr > 5)
						|| (str.length() == 2 && istr >= dateModel
								.getMaxSecond()))
				{
					getTextfield().setText("");
				} else
				{
					if (str.equals("0") || str.equals("1") || str.equals("2")
							|| str.equals("3") || str.equals("4")
							|| str.equals("5"))
					{
					} else
					{
						setJohnSpinnerValue(str);
						setOutComponentValue();
					}
				}
			} else
			{
				getTextfield().setText("");
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String val = getCombox().getSelectedItem().toString();
			if (val.length() == 2)
			{
				setJohnSpinnerValue(val);
				setOutComponentValue();
			}
		}
	}

	protected class YearSpinner extends JohnSpinner
	{
		private static final long serialVersionUID = 1L;

		public YearSpinner(JComponent comp, List<String> list,
				String selectedItem)
		{
			super(comp, list, selectedItem);
		}

		@Override
		public void inputException()
		{
			String str = getTextfield().getText();
			if (JohnStringUtil.isNumber(str) && str.length() < 5)
			{
				int istr = Integer.parseInt(str);
				if (str.length() == 1 && istr != 1 && istr != 2)
				{
					getTextfield().setText("");
				} else if (str.length() == 2 && istr != 19 && istr != 20)
				{
					getTextfield().setText("");
				} else if (str.length() == 3 && (istr < 197 || istr > 206))
				{
					getTextfield().setText("");
				} else if (str.length() == 4
						&& (istr < 1970 || istr >= dateModel.getMaxYear()))
				{
					getTextfield().setText("");
				} else if (str.length() == 4
						&& (istr > 1970 || istr < dateModel.getMaxYear()))
				{
					setJohnSpinnerValue(str);
					setOutComponentValue();
				} else
				{
				}
			} else
			{
				getTextfield().setText("");
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String val = getCombox().getSelectedItem().toString();
			if (val.length() == 4)
			{
				setJohnSpinnerValue(val);
				setOutComponentValue();
			}
		}
	}
	
	public void addJohnDateListener(JohnDateListener listener)
	{
		listenerList.add(JohnDateListener.class, listener);
	}
	
	public void removeJohnDateListener(JohnDateListener listener)
	{
		listenerList.remove(JohnDateListener.class, listener);
	}
	
	public void fireChooserShow()
	{
		Object[] listeners=listenerList.getListenerList();
		for(int i=listeners.length-2;i>=0;i-=2)
		{
			if(listeners[i]==JohnDateListener.class)
			{
				String dateString=null;
				if(dateField instanceof JTextField)
				{
					dateString=((JTextField)dateField).getText();
				}
				else if(dateField instanceof JLabel)
				{
					dateString=((JLabel)dateField).getText();
				}
				JohnDateEvent evt=new JohnDateEvent(this,dateString);
				((JohnDateListener)listeners[i+1]).chooserShow(evt);
			}
		}
	}
	
	public void fireChooserHide()
	{
		Object[] listeners=listenerList.getListenerList();
		for(int i=listeners.length-2;i>=0;i-=2)
		{
			if(listeners[i]==JohnDateListener.class)
			{
				String dateString=null;
				if(dateField instanceof JTextField)
				{
					dateString=((JTextField)dateField).getText();
				}
				else if(dateField instanceof JLabel)
				{
					dateString=((JLabel)dateField).getText();
				}
				JohnDateEvent evt=new JohnDateEvent(this,dateString);
				((JohnDateListener)listeners[i+1]).chooserHide(evt);
			}
		}
	}

	// getter and setter
	public Font getDayFont()
	{
		return dayFont;
	}

	public void setDayFont(Font dayFont)
	{
		this.dayFont = dayFont;
	}

	public Font getDateFont()
	{
		return dateFont;
	}

	public void setDateFont(Font dateFont)
	{
		this.dateFont = dateFont;
	}

	public Font getFootFont()
	{
		return footFont;
	}

	public void setFootFont(Font footFont)
	{
		this.footFont = footFont;
	}

	public Font getTodayLabFont()
	{
		return todayLabFont;
	}

	public void setTodayLabFont(Font todayLabFont)
	{
		this.todayLabFont = todayLabFont;
	}

	public Border getMainPaneBorder()
	{
		return mainPaneBorder;
	}

	public void setMainPaneBorder(Border mainPaneBorder)
	{
		this.mainPaneBorder = mainPaneBorder;
	}

	public Color getHeadPaneColor()
	{
		return headPaneColor;
	}

	public void setHeadPaneColor(Color headPaneColor)
	{
		this.headPaneColor = headPaneColor;
	}

	public Color getDayPaneColor()
	{
		return dayPaneColor;
	}

	public void setDayPaneColor(Color dayPaneColor)
	{
		this.dayPaneColor = dayPaneColor;
	}

	public Color getDatePaneColor()
	{
		return datePaneColor;
	}

	public void setDatePaneColor(Color datePaneColor)
	{
		this.datePaneColor = datePaneColor;
	}

	public Color getFootPaneColor()
	{
		return footPaneColor;
	}

	public void setFootPaneColor(Color footPaneColor)
	{
		this.footPaneColor = footPaneColor;
	}

	public Color getHeadPaneFontColor()
	{
		return headPaneFontColor;
	}

	public void setHeadPaneFontColor(Color headPaneFontColor)
	{
		this.headPaneFontColor = headPaneFontColor;
	}

	public Color getFootPaneFontColor()
	{
		return footPaneFontColor;
	}

	public void setFootPaneFontColor(Color footPaneFontColor)
	{
		this.footPaneFontColor = footPaneFontColor;
	}

	public Color getFieldOverColor()
	{
		return fieldOverColor;
	}

	public void setFieldOverColor(Color fieldOverColor)
	{
		this.fieldOverColor = fieldOverColor;
	}

	public Color getFieldOutColor()
	{
		return fieldOutColor;
	}

	public void setFieldOutColor(Color fieldOutColor)
	{
		this.fieldOutColor = fieldOutColor;
	}

	public Color getFieldDownColor()
	{
		return fieldDownColor;
	}

	public void setFieldDownColor(Color fieldDownColor)
	{
		this.fieldDownColor = fieldDownColor;
	}

	public Color getFieldUpColor()
	{
		return fieldUpColor;
	}

	public void setFieldUpColor(Color fieldUpColor)
	{
		this.fieldUpColor = fieldUpColor;
	}

	public Color getTodayLabOverColor()
	{
		return todayLabOverColor;
	}

	public void setTodayLabOverColor(Color todayLabOverColor)
	{
		this.todayLabOverColor = todayLabOverColor;
	}

	public Color getTodayLabOutColor()
	{
		return todayLabOutColor;
	}

	public void setTodayLabOutColor(Color todayLabOutColor)
	{
		this.todayLabOutColor = todayLabOutColor;
	}

	public Color getTodayLabDownColor()
	{
		return todayLabDownColor;
	}

	public void setTodayLabDownColor(Color todayLabDownColor)
	{
		this.todayLabDownColor = todayLabDownColor;
	}

	public Color getTodayLabUpColor()
	{
		return todayLabUpColor;
	}

	public void setTodayLabUpColor(Color todayLabUpColor)
	{
		this.todayLabUpColor = todayLabUpColor;
	}

	public Color getWeekendFontColor()
	{
		return weekendFontColor;
	}

	public void setWeekendFontColor(Color weekendFontColor)
	{
		this.weekendFontColor = weekendFontColor;
	}

	public Color getWeekFontColor()
	{
		return weekFontColor;
	}

	public void setWeekFontColor(Color weekFontColor)
	{
		this.weekFontColor = weekFontColor;
	}

	public Color getDateOverColor()
	{
		return dateOverColor;
	}

	public void setDateOverColor(Color dateOverColor)
	{
		this.dateOverColor = dateOverColor;
	}

	public Color getSelectedDayFillColor()
	{
		return selectedDayFillColor;
	}

	public void setSelectedDayFillColor(Color selectedDayFillColor)
	{
		this.selectedDayFillColor = selectedDayFillColor;
	}

	public Color getSelectedDayBorderColor()
	{
		return selectedDayBorderColor;
	}

	public void setSelectedDayBorderColor(Color selectedDayBorderColor)
	{
		this.selectedDayBorderColor = selectedDayBorderColor;
	}

	public Color getTodayFillColor()
	{
		return todayFillColor;
	}

	public void setTodayFillColor(Color todayFillColor)
	{
		this.todayFillColor = todayFillColor;
	}

	public Color getTodayBorderColor()
	{
		return todayBorderColor;
	}

	public void setTodayBorderColor(Color todayBorderColor)
	{
		this.todayBorderColor = todayBorderColor;
	}

	public Color getDateOverBorderColor()
	{
		return dateOverBorderColor;
	}

	public void setDateOverBorderColor(Color dateOverBorderColor)
	{
		this.dateOverBorderColor = dateOverBorderColor;
	}

	public Color getDateNoInMonthColor()
	{
		return dateNoInMonthColor;
	}

	public void setDateNoInMonthColor(Color dateNoInMonthColor)
	{
		this.dateNoInMonthColor = dateNoInMonthColor;
	}

	public Font getHeadFont()
	{
		return headFont;
	}

	public void setHeadFont(Font headFont)
	{
		this.headFont = headFont;
	}

	public boolean isRemembered()
	{
		return remembered;
	}

	public void setRemembered(boolean remembered)
	{
		this.remembered = remembered;
	}
	
}
