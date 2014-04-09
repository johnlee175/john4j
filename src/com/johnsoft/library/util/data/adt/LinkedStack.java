package com.johnsoft.library.util.data.adt;

public class LinkedStack<T> implements Stack<T>
{
	private static final long serialVersionUID = 1L;
	
	private LinkedItem<T> header = new LinkedItem<T>(null);
	private int length;
	private LinkedStackHelper helper = new LinkedStackHelper();
	
	public LinkedStack()
	{
	}
	
	public LinkedStack(LinkedStack<T> stack)
	{
		this.header = stack.header;
		this.length = stack.length;
		this.helper = stack.helper;
	}
	
	@Override
	protected Object clone()
	{
		return new LinkedStack<T>(this);
	}

	@Override
	public void push(T t)
	{
		if(full()) throw new IllegalStateException("No space remained to push a full stack!");
		LinkedItem<T> item = new LinkedItem<T>(t);
		header.head = item;
		item.tail = header;
		header = item;
		++length;
	}
	
	@Override
	public T pop()
	{
		if(empty()) throw new IllegalStateException("Can't pop a empty stack!");
		T t = header.value;
		header = header.tail;
		header.head = null;
		--length;
		return t;
	}
	
	@Override
	public boolean empty()
	{
		return header.tail == null;
	}
	
	@Override
	public boolean full()
	{
		return false;
	}

	@Override
	public T peek()
	{
		return header.value;
	}
	
	@Override
	public Helper<T> getHelper()
	{
		return helper;
	}
	
	private class LinkedStackHelper implements Helper<T>
	{
		@Override
		public boolean has(T t)
		{
			return recurse(header, t);
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
			recurse(header, sb);
			if(sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}

		@Override
		public void clear()
		{
			if(!empty())
			{
				header.tail.head = null;
				header.tail = null;
				header.value = null;
				length = 0;
			}
		}
		
		private void recurse(LinkedItem<T> item,StringBuffer sb)
		{
			if(item.tail == null)
				return;
			else
			{
				sb.append(item.value).append(',');
				recurse(item.tail,sb);
			}
		}
		
		private boolean recurse(LinkedItem<T> item,T t)
		{
			if(item.tail == null)
				return false;
			else
			{
				if(item.value == null && t == null)
					return true;
				else if(item.value.equals(t))
					return true;
				return recurse(item.tail, t);
			}
		}
	}
}
