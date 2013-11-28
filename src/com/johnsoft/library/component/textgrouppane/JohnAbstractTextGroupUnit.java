package com.johnsoft.library.component.textgrouppane;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.johnsoft.library.util.JohnSwingUtilities;

public abstract class JohnAbstractTextGroupUnit implements JohnTextGroupUnit
{
	protected static int PLUS_HEIGHT;
	protected static ImageIcon plusIcon=JohnSwingUtilities.getIconResource("plus.png");
	protected static ImageIcon plusHoverIcon=JohnSwingUtilities.getIconResource("plus_hover.png");
	protected static ImageIcon plusPressIcon=JohnSwingUtilities.getIconResource("plus_press.png");
	protected static ImageIcon minusIcon=JohnSwingUtilities.getIconResource("minus.png");
	protected static ImageIcon minusHoverIcon=JohnSwingUtilities.getIconResource("minus_hover.png");
	protected static ImageIcon minusPressIcon=JohnSwingUtilities.getIconResource("minus_press.png");
	@Override
	public JButton getPlusButton()
	{
		return createIconButton(plusIcon, plusHoverIcon, plusPressIcon);
	}

	@Override
	public JButton getMinusButton()
	{
		return createIconButton(minusIcon, minusHoverIcon, minusPressIcon);
	}
	
	protected JButton createIconButton(Icon icon,Icon hoverIcon,Icon pressIcon)
	{
		PLUS_HEIGHT=plusIcon.getIconHeight();
		final JButton but=new JButton();
		but.setIcon(icon);
		but.setRolloverIcon(hoverIcon);
		but.setPressedIcon(pressIcon);
		but.setFocusPainted(false);
		but.setContentAreaFilled(false);
		but.setRolloverEnabled(true);
		but.setBorderPainted(false);
		but.setHideActionText(true);
		but.setPreferredSize(new Dimension(plusIcon.getIconWidth(), PLUS_HEIGHT));
		but.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				but.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				but.setCursor(Cursor.getDefaultCursor());
			}
		});
		return but;
	}
	
}
