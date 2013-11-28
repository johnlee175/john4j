package com.johnsoft.library.component.emptysplitpane;

import java.awt.Graphics;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class JohnEmptySplitPaneDivider extends BasicSplitPaneDivider
{

	private static final long serialVersionUID = 1L;

	public JohnEmptySplitPaneDivider(BasicSplitPaneUI paramBasicSplitPaneUI)
	{
		super(paramBasicSplitPaneUI);
	}
	
	@Override
	public void paint(Graphics g)
	{
		
	}
}