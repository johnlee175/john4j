package com.johnsoft.library.swing.anim;

public class ElasticInterpolator implements Interpolator
{
	private boolean high;
	private boolean fast;
	
	public ElasticInterpolator()
	{
	}
	
	public ElasticInterpolator(boolean high, boolean fast)
	{
		this.high = high;
		this.fast = fast;
	}
	
	@Override
	public float getInterpolation(float t)
	{
		if (t == 0 || t == 1) return t; 
		float p, a, s;
		if (fast)
			p = 0.1f;
		else
			p = 0.2f;
		if (high) { 
			a = 1.6f;
			s = (float)(p/(2*Math.PI) * Math.asin(1/a));
		}else{
			a = 0.8f; 
			s = p/4; 
		}
		return (float)(a * Math.pow(2,-10*t) * Math.sin( (t-s) * (2*Math.PI)/p ) + 1);
	}
}
