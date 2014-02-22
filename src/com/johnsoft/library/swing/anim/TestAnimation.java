package com.johnsoft.library.swing.anim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class TestAnimation extends Animation
{
	private static final int SPEED = 10;
	private TestBallCanvas c;
	private Timer timer;
	
	public TestAnimation(TestBallCanvas c, Interpolator ip, long duration, int start, int end)
	{
		super(start, end, SPEED, duration, ip);
		this.c = c;
		timer = new Timer(SPEED, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				compute();
			}
		});
	}


	@Override
	public void startAnimation()
	{
		timer.start();
	}

	@Override
	protected void stopAnimation()
	{
		timer.stop();
	}

	@Override
	protected void refreshUI()
	{
		c.repaint();
	}

	@Override
	protected void updatePropertyValue(int val)
	{
		c.setXPropertyValue(val);
	}
}
