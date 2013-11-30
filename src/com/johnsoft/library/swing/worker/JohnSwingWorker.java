package com.johnsoft.library.swing.worker;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.johnsoft.library.util.gui.JohnException;


/**
 * 封装JDK的SwingWorker,主要用来执行长时间异步任务,将启用多个线程执行,因不产生任何结果而取消泛型,将弹出异常信息界面报告而不是在异步蒸发发生的异常.并伴有加载进度界面可做当前任务描述.
 * 如果仅仅需要加载进度界面而调用此类,可将其内部方法尽可能的设置成同步或锁住.如果已不好同步推荐使用JohnProgressSynchroWorker
 * @author 李哲浩
 */
public abstract class JohnSwingWorker extends SwingWorker<Void,Void> implements PropertyChangeListener,JohnWorker
{
	/**异常发生时的建议*/
	protected String exceptionSuggests;
	/**异常信息界面和加载进度界面的父窗口*/
	protected Window parentWindow;
	/**当发生异常弹出异常信息界面后点击继续按钮时的动作*/
	protected Action continueButtonAction;
	/**使用此后台执行的长时间任务的主题*/
	protected String taskName;
	/**当前正在执行的步骤描述*/
	protected String doingString;
	/**加载进度界面的进度条*/
	protected JProgressBar progressBar;
	/**加载进度界面的取消按钮*/
	protected JButton cancel;
	/**加载进度界面对话框*/
	protected JDialog dialog;
	/**加载进度界面放置任务主题的渐变背景区域*/
	protected JLabel bkImgTitle;
	/**具体的当前执行步骤描述的显示区域*/
	protected JTextArea progressTip;
	/**是否在执行后台任务时发生了异常,如果发生异常将不在调用safe_done方法作为收尾*/
	protected boolean occurExceptionInBack;
	
	/**
	 * @param exceptionSuggests 异常发生时的建议
	 * @param parentWindow 异常信息界面和加载进度界面的父窗口
	 * @param continueButtonAction 当发生异常弹出异常信息界面后点击继续按钮时的动作
	 * @param taskName 使用此后台执行的长时间任务的主题
	 */
	public JohnSwingWorker(String exceptionSuggests,Window parentWindow,Action continueButtonAction,String taskName)
	{
		this.exceptionSuggests=exceptionSuggests;
		this.parentWindow=parentWindow;
		this.continueButtonAction=continueButtonAction;
		this.taskName=taskName;
		
		dialog = new JDialog(parentWindow);
		
		bkImgTitle = new JLabel()
		{
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g)
			{
				Graphics2D g2d=(Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.setPaint(new LinearGradientPaint(new Point(0,0), new Point(getWidth(),getHeight()), 
						new float[]{0.0f,0.2f,0.6f,0.8f,1.0f}, 
						new Color[]{Color.WHITE,new Color(206,221,240),new Color(140,177,213),new Color(53,94,133),new Color(36,113, 144)},
						CycleMethod.NO_CYCLE));
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("微软雅黑",Font.PLAIN,18));
				g2d.drawString(getTaskName(), 25, 30);
			}
		};
		
		progressTip = new JTextArea();
		progressTip.setEnabled(false);
		progressTip.setDisabledTextColor(Color.BLACK);
		progressTip.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		progressTip.setOpaque(false);
		
		progressBar=new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setIndeterminate(true);
		
		cancel=new JButton("取消");
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent paramActionEvent)
			{
				cancel(true);
			}
		});
		
		prepareProgressPane();
		
		dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(parentWindow);
		dialog.setVisible(true);
		
		addPropertyChangeListener(this);
		execute();
	}
	
	/**相当于原SwingWorker的done方法,不过在发生异常时弹出信息窗口*/
	protected abstract void safe_done();
	/**相当于原SwingWorker的doInBackground方法,不过在发生异常时弹出信息窗口*/
	protected abstract void safe_doInBackground();
	/**首先终止覆写,因不需要在依托此方法传递中间结果*/
	@Override
	protected final void process(List<Void> chunks){}
	
	/**首先终止覆写,该方法无返回值,其中设置progress属性的0,100值,而且捕获了任何异常,并调用safe_doInBackground方法*/
	@Override
	protected final Void doInBackground() 
	{
		try
		{
			setProgress(0);
			safe_doInBackground();
			setProgress(100);
		} catch (Exception e)
		{
			JohnException.showException(e, exceptionSuggests, parentWindow, continueButtonAction);
			occurExceptionInBack=true;
		}
		return null;
	}
	
	/**首先终止覆写,该方法无返回值,其中发出嘟嘟声,恢复光标,回收加载进度窗口资源,而且捕获了任何异常,如果没有取消任务并未发生异常将调用safe_done方法*/
	@Override
	protected final void done()
	{
		try
		{
			Toolkit.getDefaultToolkit().beep();
			dialog.setCursor(null);
			dialog.dispose();
			if(!isOccurExceptionInBack()&&!isCancelled())
			{
				safe_done();
			}
		} catch (Exception e)
		{
			JohnException.showException(e, exceptionSuggests, parentWindow, continueButtonAction);
			occurExceptionInBack=true;
		}
	}
	
	/**首先终止覆写,因progress属性变更,调整进度条和步骤描述已经标题栏标题*/
	@Override
	public final void propertyChange(PropertyChangeEvent evt)
	{
		 if ("progress" == evt.getPropertyName())
		 {
			 Integer value=(Integer) evt.getNewValue();
			 progressBar.setIndeterminate(false);
	         progressBar.setValue(value);
	         dialog.setTitle(value+"%——"+doingString);
	 		 progressTip.setText("请稍候，选定任务已开始执行，可能需要几分钟。\n\n状态：\t"+doingString);
		 }
	}
	
	/**首先终止覆写,返回任务主题*/
	public final String getTaskName()
	{
		return taskName;
	}
	
	/**首先终止覆写,查看是否执行过程发生过异常*/
	public final boolean isOccurExceptionInBack()
	{
		return occurExceptionInBack;
	}
	
	/**首先终止覆写,设置进度值和步骤描述,进度值应大于0并小于100,步骤描述应尽可能言简意赅;
	 * 应在safe_doInBackground方法中调用,如果safe_doInBackground没有逻辑,而是创建了一个对象,但此对象创建过程耗时,则可将本类传入对象构造,调用此方法
	 */
	@Override
	public final void setProgressValue(int value,String doingString)
	{
		setProgress(value);
		this.doingString=doingString;
	}
	
	/**采用绝对布局布局加载进度界面,如果需要添加其他组件,请覆写此方法,并添加到dialog成员上,再在其类外添加事件监听*/
	protected void prepareProgressPane()
	{
		bkImgTitle.setBounds(0, 0, 414, 42);
		dialog.add(bkImgTitle);
		
		progressTip.setText("正在计算...");
		progressTip.setBounds(25, 50, 365, 50);
		dialog.add(progressTip);
		
		progressBar.setBounds(25, 110, 365, 18);
		dialog.add(progressBar);
		
		JPanel optionPane = new JPanel(null);
		optionPane.setBounds(0, 140, 414, 42);
		dialog.add(optionPane);
		
		cancel.setBounds(320, 8, 70, 25);
		optionPane.add(cancel);
		
		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.setTitle("正在计算...");
		dialog.setLayout(null);
		dialog.setSize(420, 210);
	}

}
