package com.johnsoft.library.swing.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ColorScrollBarUI extends BasicScrollBarUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new ColorScrollBarUI();
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
	{
		g.setColor(Color.GREEN);
		int x=Math.min(thumbBounds.width, thumbBounds.height);
		g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height,x,x);
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation)
	{
		 return new ColorArrowButton(orientation, 
		    UIManager.getColor("ScrollBar.thumb"),
		    UIManager.getColor("ScrollBar.thumbShadow"),
		    UIManager.getColor("ScrollBar.thumbDarkShadow"),
		    UIManager.getColor("ScrollBar.thumbHighlight"));
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation)
	{
		 return new ColorArrowButton(orientation,
			    UIManager.getColor("ScrollBar.thumb"),
			    UIManager.getColor("ScrollBar.thumbShadow"),
			    UIManager.getColor("ScrollBar.thumbDarkShadow"),
			    UIManager.getColor("ScrollBar.thumbHighlight"));
	}
}
