package com.johnsoft.library.util.data.primary;

/**
 * 封装int值,限制set范围,确保get出的是0到100的int值
 * @author 李哲浩
 */
public final class PercentInt
{
	private int data;
	
	public PercentInt(){
	}
	
	public PercentInt(int data)
	{
		set(data); 
	}

	public int get()
	{
		return data;
	}

	public void set(int data)
	{
		if(data>100||data<0)
			throw new IllegalArgumentException("Data must be between 0 to 100, include 0 and 100!");
		this.data = data;
	}
}
