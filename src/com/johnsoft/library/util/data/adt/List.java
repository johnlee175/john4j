package com.johnsoft.library.util.data.adt;

import java.io.Serializable;

public interface List<T> extends Cloneable, Serializable
{
	public boolean insert(int index, T t);
	
	public T delete(int index);
	
	public boolean set(int index, T t);
	
	public T get(int index);
	
	public boolean empty();
	
	public boolean full();
	
	public int length();
	
	public boolean has(T t);
	
	public Helper<T> getHelper();
	
	public interface Helper<T>
	{
		public void append(T t);
		
		public T remove();
		
		public void prepend(T t);
		
		public T cut();
		
		public T first();
		
		public void modifyBegin(T t);
		
		public T last();
		
		public void modifyEnd(T t);
		
		public String scan();
		
		public void clear();
		
		public void fromArray(T[] arr);
		
		public T[] toArray();
		
		public int firstIndexOf(T t);
		
		public int lastIndexOf(T t);
		
		public boolean addAll(int index, T[] arr);
		
		public void appendAll(T[] arr);
		
		public void prependAll(T[] arr);
		
		public int replaceAll(T[] ts, T t);
		
		public int dropAll(T[] ts);
		
		public boolean hasAny(T[] ts);
		
		public boolean hasAll(T[] ts);
		
		public T[] useRange(int fromIdx, int toIdx);
		
		public int eraseRange(int fromIdx, int toIdx);
		
		public void reverse();
		
		public void swap(int i1, int i2);
	}
}
