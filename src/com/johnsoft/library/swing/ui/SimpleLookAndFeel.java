package com.johnsoft.library.swing.ui;

import javax.swing.UIDefaults;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

public class SimpleLookAndFeel extends WindowsLookAndFeel
{
	private static final long serialVersionUID = 4281428672270340849L;

		@Override
		protected void initClassDefaults(UIDefaults table)
		{
			super.initClassDefaults(table);
			final String simplePackageName = "com.johnsoft.swing.ui.";
			Object[] simpleUIDefaults = {
					"RootPaneUI", simplePackageName + "ImageRootPaneUI",
					"PanelUI", simplePackageName + "SimplePanelUI",
					"OptionPaneUI", simplePackageName + "SimpleOptionPaneUI",
					"ToolBarUI", simplePackageName + "SimpleToolBarUI",	
					"CheckBoxUI", simplePackageName + "SimpleCheckBoxUI",
	        "RadioButtonUI", simplePackageName + "SimpleRadioButtonUI",
	        "ToggleButtonUI", simplePackageName + "ColorToggleButtonUI",
          "ButtonUI", simplePackageName + "ColorButtonUI",
          "ScrollBarUI", simplePackageName + "ColorScrollBarUI",
          "ComboBoxUI", simplePackageName + "ColorComboBoxUI"
    };
    table.putDefaults(simpleUIDefaults);
		}
		
}
