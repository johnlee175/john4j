package com.johnsoft.library.swing.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

public class ImageRootPaneUI extends BasicRootPaneUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new ImageRootPaneUI();
	}

	@Override
	public void paint(Graphics g, JComponent c)
	{
		c.setOpaque(false);
		Rectangle r=g.getClipBounds();
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(new Color(0,0,0));
		g2.fill(r);
		Image image=new ImageIcon(getClass().getResource("/Resource/s2.png")).getImage();
		g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		g2.drawImage(image, 0, 0, r.width, r.height, null);
		super.paint(g, c);
	}
}
