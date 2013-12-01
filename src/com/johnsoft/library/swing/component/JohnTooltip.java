package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sun.awt.AWTUtilities;

/**
 * 右下角气泡提示类，尽力保证每行7字，以\n折行
 * @author john
 */
public class JohnTooltip extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel frame;
	
	private Timer timer;
	private int screenWidth;
	private int screeanHeight;
	private int thisY;
	private float count=1.0f;
	private String texts;
	private boolean color;
	private boolean updown;
	
	public JohnTooltip(String texts)
	{
		color=new Random().nextBoolean();
		updown=new Random().nextBoolean();
		this.texts=texts;
		initComponent();
	  timer=new Timer(10, this);
	  timer.start();	
	  
	}
	
	private void initComponent()
	{
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
  	this.screenWidth=dim.width;
  	this.screeanHeight=dim.height;
  	
		frame=new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2=(Graphics2D)g;
				if(color)
				{
					g2.setPaint(new GradientPaint(30, 30, new Color(175,255,0), 180, 150,new Color(255, 255, 175)));
				}	else
				{
					g2.setPaint(new GradientPaint(30, 30, new Color(255, 255, 175), 180, 150, Color.RED));
				}		
				g2.fill(new Ellipse2D.Double(0, 0, 200, 160));
				if(color)
				{
					g2.setColor(Color.RED);
				}else{
					g2.setColor(Color.BLUE);
				}
				g2.setFont(new Font("微软雅黑", Font.BOLD, 14));
				String[] txt=texts.split("\n");
				for(int i=0;i<txt.length;i++)
				{
					g2.drawString(txt[i], 50, 40+i*25);
				}
			}
		};
    
		frame.setLayout(null);
		frame.setPreferredSize(new Dimension(200, 160));

  	this.setContentPane(frame);
  	this.setUndecorated(true);
  	AWTUtilities.setWindowOpaque(this, false);
  	this.setAlwaysOnTop(true);
  	this.pack();
  	if(updown)
  	{
  		this.setLocation(this.screenWidth-210, this.screeanHeight);
  	}else{
  		this.setLocation(10, -160);
  	}
  	this.setVisible(true);
  	
	}

	public void actionPerformed(ActionEvent e)
	{
			thisY=this.getLocation().y;
			if(updown)
			{
				if(thisY>this.screeanHeight-200)
				{
					this.setLocation(this.screenWidth-210, thisY-2);
				}else
				{
					fadeOut();
				}
			}else{
				if(thisY<80)
				{
					this.setLocation(10, thisY+2);
				}else
				{
					fadeOut();
				}
			}		
	}
	
	private void fadeOut()
	{
		if(count>0.2f)
		{
			AWTUtilities.setWindowOpacity(this, count);
		}else{
			timer.stop();
			this.dispose();
		}
		count-=0.01f;
	}

}
