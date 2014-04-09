package com.johnsoft.library.util.data.adt;

import java.io.Serializable;

public interface Queue<T> extends Cloneable, Serializable
{
	public void enqueue(T t);
	
	public T dequeue();
	
	public boolean empty();
	
	public boolean full();
	
	public T front();
	
	public T rear();
	
	public Helper<T> getHelper();
	
	public interface Helper<T>
	{
		public boolean has(T t);
		
		public int length();
		
		public String scan();
		
		public void clear();
	}
}
