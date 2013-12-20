package com.johnsoft.library.swing.ui;

import java.awt.Dimension;

import com.sun.java.swing.plaf.windows.WindowsScrollBarUI;

public class WindowsLimitSizeScrollBarUI extends WindowsScrollBarUI
{
	private Dimension d;
	
	public WindowsLimitSizeScrollBarUI(Dimension minimumThumbSize)
	{
		 this.d=minimumThumbSize;
	}
	
	@Override
	protected Dimension getMinimumThumbSize()
	{
		 
		return this.d;
	}
}
