package com.johnsoft.library.swing.anim;


public class BounceInterpolator implements Interpolator
{
	public BounceInterpolator()
	{
	}
	
	@Override
	public float getInterpolation(float t)
	{
		 float factor = 8.0f;
		 if(t < (1/2.75))
		 {
			 return factor * t * t;
		 }
		 else if (t < (2/2.75))
		 {
			 t -= 1.5/2.75;
			 return factor * t * t + 0.7f;
		 }
		 else if (t < (2.5/2.75))
		 {
			 t -= 2.25/2.75;
			 return factor * t * t + 0.9f;
		 }
		 else
		 {
			 t -= 2.625/2.75;
			 return factor * t * t + 0.95f;
		 }
	}
}
