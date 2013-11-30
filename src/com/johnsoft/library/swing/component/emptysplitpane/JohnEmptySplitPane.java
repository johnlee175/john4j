package com.johnsoft.library.swing.component.emptysplitpane;

import javax.swing.JSplitPane;

public class JohnEmptySplitPane extends JSplitPane
{
	private static final long serialVersionUID = 1L;
	
	public JohnEmptySplitPane()
	{
		super();
		setUI(new JohnEmptySplitPaneUI());
	}
	
	public JohnEmptySplitPane(int direction,boolean live)
	{
		super(direction,live);
		setUI(new JohnEmptySplitPaneUI());
	}

}
