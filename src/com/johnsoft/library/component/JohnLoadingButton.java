package com.johnsoft.library.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.johnsoft.library.util.JohnSwingUtilities;
import com.johnsoft.library.util.data.fn.JohnOvoidIvoidFn;

/**加载时动画提示按钮*/
public class JohnLoadingButton extends JButton 
{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1)
		{
			e1.printStackTrace();
		} 
		final JohnLoadingButton butt=new JohnLoadingButton("Click me!");
		butt.setRoundStyle();
		JPanel jp=new JPanel();
		jp.add(butt);
		jp.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getButton()==MouseEvent.BUTTON3)
				{
					butt.finishLoading();
				}else{
					butt.beginLoading();
				}
			}
		});
		JFrame jf=new JFrame();
		jf.add(jp);
		jf.setSize(600, 100);
		jf.setLocationRelativeTo(null);
		jf.setAlwaysOnTop(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
	
	protected Timer timer;
	protected int counter;
	protected String oldText;
	protected Icon oldIcon;
	protected int oldHorizontalAlignment;
	public static String[] Chinese=new String[]{"载入中   ","载入中.  ","载入中.. ","载入中..."};
	public static String[] English=new String[]{"Loading   ","Loading.  ","Loading.. ","Loading..."};
	public static Icon loadingIcon=JohnSwingUtilities.getIconResource("loading2.gif");
	private boolean isChineseStyle;
	private boolean loadingUnabled;
	private boolean roundStyle;
	private boolean isCanceled;
	
	public JohnLoadingButton()
	{
		super();
		init();
	}
	
	public JohnLoadingButton(Action a)
	{
		super(a);
		init();
	}
	
	public JohnLoadingButton(String text)
	{
		super(text);
		init();
	}
	
	public JohnLoadingButton(String text, Icon icon)
	{
		super(text,icon);
		init();
	}
	
	public JohnLoadingButton(String text,boolean isChineseStyle)
	{
		super(text);
		this.isChineseStyle=isChineseStyle;
		init();
	}
	
	public JohnLoadingButton(String text, Icon icon,boolean isChineseStyle)
	{
		super(text,icon);
		this.isChineseStyle=isChineseStyle;
		init();
	}

	protected void init()
	{
		Dimension dim=new Dimension(120, 25);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setFocusPainted(false);
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(!roundStyle) 
			super.paint(g);
		else
			drawRoundStyle(g);
	}
	
	protected void drawRoundStyle(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(isEnabled()?Color.ORANGE:Color.LIGHT_GRAY);
		g2d.setFont(new Font("微软雅黑",Font.BOLD,14));
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
		if(getText()!=null)
		{
			g2d.setColor(Color.WHITE);
			Point p=JohnSwingUtilities.getDrawStringCenterAt(g2d, new Rectangle(new Point(), getSize()), getText(), getFont());
			g2d.drawString(getText(), getHorizontalAlignment()==LEFT?28:p.x, p.y);
		}
		if(getIcon()!=null)
		{
			if(!isEnabled()) g2d.setComposite(AlphaComposite.SrcOver.derive(0.4f));
			g2d.drawImage(((ImageIcon)getIcon()).getImage(), 10, (getHeight()-16)/2, null);
		}
	}
	
	public void exeWithLoading(final JohnOvoidIvoidFn fn)
	{
		beginLoading();
		new SwingWorker<Void, Void>()
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				fn.function();
				return null;
			}
			@Override
			protected void done()
			{
				finishLoading();
			}
		}.execute();
	}
	
	public void beginLoading()
	{
		isCanceled=false;
		oldText=getText();
		oldIcon=getIcon();
		oldHorizontalAlignment=getHorizontalAlignment();
		if(!loadingUnabled)
		{
			timer=new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							if(isCanceled) return;
							setEnabled(false);
							setIcon(loadingIcon);
							counter=counter==4?0:counter;
							setText(isChineseStyle?Chinese[counter]:English[counter]);
							setHorizontalAlignment(JButton.LEFT);
							repaint();
							counter++;
						}
					});
				}
			}, 100, 800);
		}
	}
	
	public void finishLoading()
	{
		isCanceled=true;
		timer.cancel();
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				setText(oldText);
				setIcon(oldIcon);
				setHorizontalAlignment(oldHorizontalAlignment);
				setEnabled(true);
			}
		});
	}

	public boolean isLoadingEnabled()
	{
		return !loadingUnabled;
	}

	public void setLoadingEnabled(boolean loadingEnabled)
	{
		this.loadingUnabled = !loadingEnabled;
	}
	
	public void setRoundStyle()
	{
		roundStyle=true;
		Dimension dim=new Dimension(120,45);
		setPreferredSize(dim);
		setMinimumSize(dim);
	}

}
