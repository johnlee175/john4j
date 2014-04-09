package com.johnsoft.library.util.data.adt;

import java.io.Serializable;

public interface Stack<T> extends Cloneable, Serializable
{
	public void push(T t);
	
	public T pop();
	
	public boolean empty();
	
	public boolean full();
	
	public T peek();
	
	public Helper<T> getHelper();
	
	public interface Helper<T>
	{
		public boolean has(T t);
		
		public int length();
		
		public String scan();
		
		public void clear();
	}
}
