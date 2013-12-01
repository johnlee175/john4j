package com.johnsoft.library.swing.ui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class ColorComboBoxUI extends BasicComboBoxUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new ColorComboBoxUI();
	}
	
	@Override
	protected JButton createArrowButton()
	{
		 JButton button = new ColorArrowButton(BasicArrowButton.SOUTH,
			    UIManager.getColor("ComboBox.buttonBackground"),
			    UIManager.getColor("ComboBox.buttonShadow"),
			    UIManager.getColor("ComboBox.buttonDarkShadow"),
			    UIManager.getColor("ComboBox.buttonHighlight"));
     button.setName("ComboBox.arrowButton");
     return button;
	}
}
