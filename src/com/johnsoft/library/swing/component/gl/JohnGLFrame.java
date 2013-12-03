package com.johnsoft.library.swing.component.gl;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.media.opengl.awt.GLJPanel;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class JohnGLFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	protected JohnGLPane pane;
	
	public JohnGLFrame(JohnGLRenderer renderer, JohnGLInputListener listener, boolean show)
	{
		add(createGLPane(renderer,listener),BorderLayout.CENTER);
		initFrame(show);
	}
	
	protected JComponent createGLPane(JohnGLRenderer renderer, JohnGLInputListener listener)
	{
		pane=new JohnGLPane(renderer, listener);
		return pane;
	}
	
	protected void initFrame(boolean show)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 750);
		setLocationRelativeTo(null);
		setVisible(show);
	}
	
	public GLJPanel getGLJPanel()
	{
		return pane;
	}
	
	public void addAtEast(Component c)
	{
		add(c,BorderLayout.EAST);
	}
	
	public void addAtWest(Component c)
	{
		add(c,BorderLayout.WEST);
	}
	
	public void addAtSouth(Component c)
	{
		add(c,BorderLayout.SOUTH);
	}
	
	public void addAtNorth(Component c)
	{
		add(c,BorderLayout.NORTH);
	}
}
