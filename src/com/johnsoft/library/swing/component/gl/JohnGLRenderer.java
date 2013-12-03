package com.johnsoft.library.swing.component.gl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;


public class JohnGLRenderer implements GLEventListener
{
	private GLU glu = new GLU();
	private JohnGLPane pane;
	
	public final void setGLPane(JohnGLPane pane)
	{
		this.pane = pane;
	}
	
	protected JohnGLPane getGLPane()
	{
		return pane;
	}
	
	protected GLU getGLU()
	{
		return glu;
	}
	
	protected void defaultReshape(GLAutoDrawable drawable, int w, int h, float fovy, float zNear, float zFar)
	{
		 GL2 gl = drawable.getGL().getGL2();
		 gl.glViewport(0, 0, w, h);
		 gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		 gl.glLoadIdentity();
		 glu.gluPerspective(fovy, (float)w/h, zNear, zFar);
	}
	
	protected GL2 optionalDisposeMethodInitialize(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
		return gl;
	}
	
	@Override
	public void display(GLAutoDrawable drawable)
	{
	}

	@Override
	public void dispose(GLAutoDrawable drawable)
	{
	}

	@Override
	public void init(GLAutoDrawable drawable)
	{
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
	}
}
