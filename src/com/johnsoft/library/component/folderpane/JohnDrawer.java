package com.johnsoft.library.component.folderpane;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class JohnDrawer extends JComponent
{
	private static final long serialVersionUID = 1L;
	private double ratio = 1.0;
	private boolean animating;
	private JComponent content;
	private Image offImage;

	JohnDrawer(double r, JComponent comp)
	{
		this.ratio = r;
		this.content = comp;
		if(new Rectangle().equals(comp.getBounds()))
		{
			comp.setSize(comp.getPreferredSize());
		}
		add(comp);
		setLayout(null);
	}
	
	public JComponent getContent()
	{
		return content;
	}

	int getContentHeight()
	{
		return content.getHeight();
	}

	int getContentWidth()
	{
		return content.getWidth();
	}

	void setRatio(double ratio)
	{
		this.ratio = ratio;
		repaint();
	}

	double getRatio()
	{
		return this.ratio;
	}

	protected void paintChildren(Graphics g)
	{
		if (animating)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, (float) ratio));
			g2d.drawImage(getOffImage(), 0, getHeight() - content.getHeight(),
					this);
		} else
			super.paintChildren(g);
	}

	void setAnimating(boolean animating)
	{
		this.animating = animating;
	}

	private Image getOffImage()
	{
		if (offImage == null)
		{
			int contentWidth = content.getWidth();
			int contentHeight = content.getHeight();
			if (offImage == null)
			{
				offImage = createImage(contentWidth, contentHeight);
				Graphics g = offImage.getGraphics();
				content.paint(g);
			}
		}
		return offImage;
	}
}
