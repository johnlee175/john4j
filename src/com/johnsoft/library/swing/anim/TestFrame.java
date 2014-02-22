package com.johnsoft.library.swing.anim;

import javax.swing.JFrame;

public class TestFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception
	{
		TestFrame f = new TestFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TestBallCanvas c = new TestBallCanvas();
		f.add(c);
		f.pack();
		f.setLocationRelativeTo(null);
		TestAnimation a = new TestAnimation(c, new ElasticInterpolator(), 3000, 0, 500);
		f.setVisible(true);
		Thread.sleep(1000);
		a.startAnimation();
	}
}
