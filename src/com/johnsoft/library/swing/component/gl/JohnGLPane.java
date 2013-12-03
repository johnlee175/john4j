package com.johnsoft.library.swing.component.gl;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;


public class JohnGLPane extends GLJPanel
{
	private static final long serialVersionUID = 1L;
	
	public JohnGLPane(JohnGLRenderer renderer, JohnGLInputListener listener)
	{
		 super(new GLCapabilities(GLProfile.getDefault()));
		 renderer.setGLPane(this);
		 addGLEventListener(renderer);
		 if(listener!=null)
		 {
			 addKeyListener(listener);
			 addMouseListener(listener);
			 addMouseMotionListener(listener);
			 addMouseWheelListener(listener);
		 }
	}
}
