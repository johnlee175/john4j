package com.johnsoft.library.util.data.adt;

import java.lang.reflect.Array;

public class ArrayStack<T> implements Stack<T>
{
	private static final long serialVersionUID = 1L;
	
	private T[] items;
	private int top = -1;
	private ArrayStackHelper helper = new ArrayStackHelper();
	
	@SuppressWarnings("unchecked")
	public ArrayStack(Class<T> clazz, int size)
	{
		items = (T[])Array.newInstance(clazz, size);
	}
	
	public ArrayStack(ArrayStack<T> stack)
	{
		this.items = stack.items;
		this.top = stack.top;
		this.helper = stack.helper;
	}
	
	@Override
	protected Object clone()
	{
		return new ArrayStack<T>(this);
	}
	
	@Override
	public void push(T t)
	{
		if(full()) throw new IllegalStateException("No space remained to push a full stack!");
		items[++top] = t;
	}
	
	@Override
	public T pop()
	{
		if(empty()) throw new IllegalStateException("Can't pop a empty stack!");
		return items[top--];
	}
	
	@Override
	public boolean empty()
	{
		return top == -1;
	}
	
	@Override
	public boolean full()
	{
		return top == items.length - 1;
	}
	
	@Override
	public T peek()
	{
		if(empty()) return null;
		return items[top];
	}
	
	@Override
	public Helper<T> getHelper()
	{
		return helper;
	}
	
	private class ArrayStackHelper implements Helper<T>
	{
		@Override
		public boolean has(T t)
		{
			for(int i = 0; i <= top; ++i)
			{
				if(items[i] == null && t == null)
					return true;
				else if(items[i].equals(t))
					return true;
			}
			return false;
		}

		@Override
		public int length()
		{
			return top + 1;
		}
		
		@Override
		public String scan()
		{
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i <= top; ++i)
				sb.append(items[i]).append(',');
			if(sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}

		@Override
		public void clear()
		{
			for(int i = 0; i <items.length; ++i)
			{
				items[i] = null;
			}
			top = -1;
		}
	}
}
