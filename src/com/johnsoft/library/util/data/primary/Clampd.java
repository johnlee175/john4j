package com.johnsoft.library.util.data.primary;

/**
 * 封装double值,限制set范围,确保get出的是0到1的double值
 * @author 李哲浩
 */
public final class Clampd
{
	private double data;
	
	public Clampd(){
	}
	
	public Clampd(double data)
	{
		set(data); 
	}

	public double get()
	{
		return data;
	}

	public void set(double data)
	{
		if(data>1||data<0)
			throw new IllegalArgumentException("Data must be between 0 to 1, include 0 and 1!");
		this.data = data;
	}
}
