package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.johnsoft.library.util.gui.JohnShortcutManager;

public abstract class JohnSimpleButton extends JLabel
{
	private static final long serialVersionUID = 1L;
	private Color rollOverColor=null;
	private Color pressColor=null;
	private Color defaultColor=getBackground();
	private Icon icon;
	private String label;
	
	public JohnSimpleButton(String label,Icon icon)
	{
		setOpaque(true);
		if(label!=null&&!"".equals(label))
		{
			this.label=label;
		}
		if(icon!=null)
		{
			this.icon=icon;
		}
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(isEnabled())
				{
					setBorder(BorderFactory.createLoweredBevelBorder());
					if(pressColor!=null)
					{
						setBackground(pressColor);
					}
					repaint();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e)
			{
				if(isEnabled())
				{
					setBorder(BorderFactory.createRaisedBevelBorder());
				  if(rollOverColor!=null)
				  {
				  	setBackground(rollOverColor);
				  }
				  repaint();
				}
			}
			@Override
			public void mouseExited(MouseEvent e)
			{
				if(isEnabled())
				{
					setBorder(BorderFactory.createEmptyBorder());
					setBackground(defaultColor);
					repaint();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if(isEnabled())
				{
					setBorder(BorderFactory.createRaisedBevelBorder());
					setBackground(rollOverColor);
					repaint();
					doAction();
				}
			}
		});	
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(isEnabled())
		{ 
			super.paint(g);
			Rectangle rect=g.getClipBounds();
			g.drawImage(((ImageIcon)icon).getImage(), rect.x+2, rect.y+1, rect.height-4, rect.height-4, this);
			g.drawString(label, rect.height-4, rect.height/2+rect.y+4);
		}else{
			setBorder(BorderFactory.createEmptyBorder());
			setIcon(icon);
			setText(label);
			super.paint(g);
		}
	}
	
  public JohnSimpleButton setButtonSize(Dimension size)
  {
  	super.setPreferredSize(size);
  	super.setMaximumSize(size);
  	super.setMinimumSize(size);
  	super.setSize(size);
  	return this;
  }
	
  public JohnSimpleButton setRollOverAndPressColor(Color...colors)
  {
  	if(colors.length==1)
  	{
  		rollOverColor=colors[0];
  		pressColor=colors[0];
  	}
  	else if(colors.length==2)
  	{
  		rollOverColor=colors[0];
  		pressColor=colors[1];
  	}
  	return this;
  }
  
  public JohnSimpleButton setDefaultButtonBGColor(Color bg)
  {
  	defaultColor=bg;
  	setBackground(bg);
  	return this;
  }
  
  public JohnSimpleButton setActionKey(int... keys)
  {
  	JohnShortcutManager.getInstance().addShortcutListener(
  			new JohnShortcutManager.ShortcutListener()
  			{
					@Override
					public void handle()
					{
						doAction();
					}
  			},keys);
  	return this;
  }
  
	public abstract void  doAction();
}
