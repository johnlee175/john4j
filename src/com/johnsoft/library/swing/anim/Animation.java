package com.johnsoft.library.swing.anim;

public abstract class Animation
{
	public abstract void startAnimation();
	protected abstract void stopAnimation();
	protected abstract void refreshUI();
	protected abstract void updatePropertyValue(int val);
	
	private int start, end;
	private long duration;
	private Interpolator interpolator;
	private int speed;
	private int calledCount;
	
	public Animation(int start, int end, int speed, long duration, Interpolator interpolator)
	{
		this.start=start;
		this.end=end;
		this.speed=speed;
		this.duration=duration;
		this.interpolator=interpolator;
	}
	
	protected final void compute()
	{
		float currPercent = ((++calledCount) * speed) / ((float)duration);
		if(currPercent < 1.0f)
		{
			updatePropertyValue((int)(start + (end - start) * interpolator.getInterpolation(currPercent)));
			refreshUI();
		}else{
			updatePropertyValue(end);
			stopAnimation();
		}
	}
}
