package com.johnsoft.library.util.gui;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JohnException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	/**如果为true,将开启控制台,图形化异常提示,如果为false,将开启控制台异常提示,如果为null,将隐藏异常信息*/
	public static Boolean DEBUG_FLAG=true;
	private String message;
	
  public JohnException(Exception e,String message)
	{
  	  if(e==null){e=this;}
  	  if(message==null){message="";}
  	  this.message=message;
  	  
  	  StringBuilder sb=new StringBuilder();
  	  if(e!=this){sb.append(message).append("\n");};
  	  StackTraceElement trace=e.getStackTrace()[0];
  	  sb.append("异常方法: ").append(trace.getClassName()).append("!").append(trace.getMethodName());
  	  if(trace.getFileName()!=null)
  	  {
  	  	sb.append("\n异常位置: ").append(trace.getFileName()).append(" 文件 ").append(trace.getLineNumber()).append(" 行");
  	  }
  	  sb.append("\n异常类型: ").append(e.getClass().getName());
  	  sb.append("\n异常原因: ").append(e.getMessage());
  	 
	  JOptionPane.showMessageDialog(null, sb.toString(), "错误或异常报告", JOptionPane.ERROR_MESSAGE);
	  e.printStackTrace();
	}
  
  @Override
  public String getMessage()
  {
  	return message;
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
}
