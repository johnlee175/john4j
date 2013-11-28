package com.johnsoft.library.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JComponent;

import com.johnsoft.library.layout.JohnGridLayout;

public class JohnNavigatorPane extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	protected JComponent navigatorBar;
	protected JComponent componentContainer;
	
	private JohnGridLayout gridLyt;
	
	public JohnNavigatorPane()
	{
		createNavigatorBar();
		createComponentContainer();
		installLayout();
	}
	
	protected void createNavigatorBar()
	{
		navigatorBar=new JComponent()
		{
			private static final long serialVersionUID = 1L;
			
		};
	}
	
	protected void createComponentContainer()
	{
		componentContainer=new JComponent()
		{
			private static final long serialVersionUID = 1L;
		};
	}
	
	private void installLayout()
	{
		setLayout(new BorderLayout(5,5));
		componentContainer.setLayout(new CardLayout(5,5));
		gridLyt=new JohnGridLayout(1,1,10,5);
		navigatorBar.setLayout(gridLyt);
	}
	
	public void addNavigator(String title,Component comp)
	{
		gridLyt.setCols(gridLyt.getCols()+1);
		navigatorBar.add(comp);
		componentContainer.add(comp,title);
	}
	
	public void removeNavigator(String title)
	{
		
	}
	
	protected void getNavigatorButton()
	{
		
	}
	
	
}
