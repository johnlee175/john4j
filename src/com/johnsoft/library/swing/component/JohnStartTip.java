package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sun.awt.AWTUtilities;

/**
 * 启动提示类
 * @author john
 */
public class JohnStartTip extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel frame;
	
	private Random random;
	private Timer timer;
	private String texts;
	private boolean isMax;
	private int fontSize=10;

	private Color[] colors=new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.CYAN,Color.YELLOW,Color.PINK,Color.ORANGE,new Color(175,255,0)}; 
	private Color color1,color2;
	
	public JohnStartTip(String texts)
	{
		this.texts=texts;
		random=new Random();
		color1=colors[random.nextInt(8)];
		color2=colors[random.nextInt(8)];
		initComponent();
	  timer=new Timer(10, this);
	  timer.start();	
	}

	private void initComponent()
	{
		frame=new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2=(Graphics2D)g;
				g2.setPaint(new GradientPaint(30, 30,color1, 180, 150,color2));
				Font font=new Font("微软雅黑", Font.BOLD, fontSize);
				Rectangle2D rect2=g2.getFontMetrics(font).getStringBounds(texts, g2);
				Rectangle rect=g2.getClipBounds();
				g2.setFont(font);
				g2.drawString(texts, new Double((rect.width-rect2.getWidth())/2).intValue(), rect.height/2);
			}
		};
    
		frame.setLayout(null);
		
		frame.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 600));

  	this.setContentPane(frame);
  	this.setUndecorated(true);
  	AWTUtilities.setWindowOpaque(this, false);
  	this.setAlwaysOnTop(true);
  	this.pack();
  	this.setLocationRelativeTo(null);
  	this.setVisible(true);
  	
	}

	public void actionPerformed(ActionEvent e)
	{
			if(isMax)
			{
				fontSize-=2;
				repaint();
			}else{
				fontSize+=2;
				repaint();
			}
			if(fontSize<10)
			{
				timer.stop();
				this.dispose();
			}
			else if(fontSize==60)
			{
				color1=colors[random.nextInt(8)];
				color2=colors[random.nextInt(8)];
			}
			else if(fontSize==110)
			{
				color1=colors[random.nextInt(8)];
				color2=colors[random.nextInt(8)];
			}
			else if(fontSize>=150)
			{
				isMax=true;
			}	
	}

}
