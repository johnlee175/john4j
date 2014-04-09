package com.johnsoft.library.util.data.adt;

import java.lang.reflect.Array;

public class ArrayQueue<T> implements Queue<T>
{
	private static final long serialVersionUID = 1L;
	
	private T[] items;
	private int front;
	private int rear = -1;
	private int length;
	private ArrayQueueHelper helper = new ArrayQueueHelper();
	
	@SuppressWarnings("unchecked")
	public ArrayQueue(Class<T> clazz, int size)
	{
		items = (T[])Array.newInstance(clazz, size);
	}
	
	public ArrayQueue(ArrayQueue<T> queue)
	{
		this.items = queue.items;
		this.front = queue.front;
		this.rear = queue.rear;
		this.length = queue.length;
		this.helper = queue.helper;
	}
	
	@Override
	protected Object clone()
	{
		return new ArrayQueue<T>(this);
	}
	
	@Override
	public void enqueue(T t)
	{
		if(full()) throw new IllegalStateException("No space remained to enqueue a full queue!");
		rear = ++rear % items.length;
		items[rear] = t;
		++length;
	}
	
	@Override
	public T dequeue()
	{
		if(empty()) throw new IllegalStateException("Can't dequeue a empty queue!");
		front = front % items.length;
		T t = items[front];
		++front;
		--length;
		return t;
	}
	
	@Override
	public boolean empty()
	{
		return length == 0;
	}
	
	@Override
	public boolean full()
	{
		return length == items.length;
	}
	
	@Override
	public T front()
	{
		return items[front];
	}
	
	@Override
	public T rear()
	{
		if(empty()) return null;
		return items[rear];
	}
	
	@Override
	public Helper<T> getHelper()
	{
		return helper;
	}
	
	private class ArrayQueueHelper implements Helper<T>
	{
		@Override
		public boolean has(T t)
		{
			for(int i = 0; i < length; ++i)
			{
				T curr = items[(front + i) % items.length];
				if(curr == null && t == null)
					return true;
				else if(curr.equals(t))
					return true;
			}
			return false;
		}

		@Override
		public int length()
		{
			return length;
		}
		
		@Override
		public String scan()
		{
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < length; ++i)
				sb.append(items[(front + i) % items.length]).append(',');
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
			front = 0;
			rear = -1;
			length = 0;
		}
	}
}
