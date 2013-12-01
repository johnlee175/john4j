package com.johnsoft.library.swing.ui;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;

public class SimpleRadioButtonUI extends BasicRadioButtonUI
{
	public static ComponentUI createUI(JComponent c) {
		return new SimpleRadioButtonUI();
	    }
 public void installUI( JComponent c )
{
	 super.installUI(c);
	 LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
}
}
