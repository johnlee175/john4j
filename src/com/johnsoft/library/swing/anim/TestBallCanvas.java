package com.johnsoft.library.swing.anim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class TestBallCanvas extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private int x;
	private int r = 10;
	
	public TestBallCanvas()
	{
		setPreferredSize(new Dimension(900, 400));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		int h = getHeight();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), h);
		g.setColor(Color.ORANGE);
		g.fillOval(x, h/2-r, 2 * r, 2 *r);
	}
	
	public void setXPropertyValue(int x)
	{
		this.x = x;
	}
}
