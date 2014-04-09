package com.johnsoft.library.util.data.adt;

import java.io.Serializable;

public interface Set<T> extends Cloneable, Serializable
{
	public boolean add(T t);
	
	public boolean remove(T t);
	
	public boolean change(T t1, T t2);
	
	public boolean empty();
	
	public boolean full();
	
	public boolean hasNext();
	
	public T next();
	
	public void reset();
	
	public Helper<T> getHelper();
	
	public interface Helper<T>
	{
		public boolean has(T t);
		
		public int length();
		
		public String scan();
		
		public void clear();
		
		public T[] toArray();
		
		public void fromArray(T[] ts);
	}
}
