package com.johnsoft.library.util.data.primary;

/**
 * 封装double值,限制set范围,确保get出的是0到100的double值
 * @author 李哲浩
 */
public final class PercentDouble
{
	private double data;
	
	public PercentDouble(){
	}
	
	public PercentDouble(double data)
	{
		set(data); 
	}

	public double get()
	{
		return data;
	}

	public void set(double data)
	{
		if(data>100||data<0)
			throw new IllegalArgumentException("Data must be between 0 to 100, include 0 and 100!");
		this.data = data;
	}
}
