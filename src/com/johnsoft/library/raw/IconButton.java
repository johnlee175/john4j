package com.johnsoft.library.raw;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

public class IconButton extends JButton implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public IconButton()
	{
		setUI(new BasicButtonUI());
		setContentAreaFilled(false);
		setFocusable(false);
		setBorder(BorderFactory.createEtchedBorder());
		setBorderPainted(false);
		// Making nice rollover effect
		// we use the same listener for all buttons
		addMouseListener(new buttonMouseListener());
		setRolloverEnabled(true);
	}

	// paint the cross
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// shift the image for pressed buttons
		if (!getModel().isPressed())
		{
			g2.translate(-1, -1);
		}

		// leave the graphics unchanged
		if (!getModel().isPressed())
		{
			g2.translate(1, 1);
		}
	}

	public void actionPerformed(ActionEvent e)
	{

	}

	class buttonMouseListener extends MouseAdapter
	{

		public void mouseEntered(MouseEvent e)
		{
			Component component = e.getComponent();
			if (component instanceof AbstractButton)
			{
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e)
		{
			Component component = e.getComponent();
			if (component instanceof AbstractButton)
			{
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}