package com.johnsoft.library.swing.component.emptysplitpane;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


public class JohnEmptySplitPaneUI extends BasicSplitPaneUI
{
	public static ComponentUI createUI(JComponent paramJComponent)
    {
	    return new JohnEmptySplitPaneUI();
	}
	
	@Override
	protected void installDefaults()
	{
		super.installDefaults();
		LookAndFeel.installProperty(this.splitPane, "dividerSize", 6);
		this.divider.setDividerSize(this.splitPane.getDividerSize());
	}
	
	@Override
	public void paint(Graphics paramGraphics, JComponent c)
	{
		c.setBorder(BorderFactory.createEmptyBorder());
		super.paint(paramGraphics, c);
		c.repaint();
	}
	
	@Override
	public BasicSplitPaneDivider createDefaultDivider()
	{
		return new JohnEmptySplitPaneDivider(this);
	}
}