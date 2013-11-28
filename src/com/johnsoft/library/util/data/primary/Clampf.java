package com.johnsoft.library.util.data.primary;

/**
 * 封装float值,限制set范围,确保get出的是0到1的float值
 * @author 李哲浩
 */
public final class Clampf
{
	private float data;
	
	public Clampf(){
	}
	
	public Clampf(float data)
	{
		set(data); 
	}

	public float get()
	{
		return data;
	}

	public void set(float data)
	{
		if(data>1||data<0)
			throw new IllegalArgumentException("Data must be between 0 to 1, include 0 and 1!");
		this.data = data;
	}
	
}
