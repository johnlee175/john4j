package com.johnsoft.library.raw;


public class TimeoutException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public TimeoutException(String errMessage)
	{
		super(errMessage);
	}
}
