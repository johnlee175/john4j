package com.johnsoft.library.util.data.adt;

public class LinkedQueue<T> implements Queue<T>
{
	private static final long serialVersionUID = 1L;
	
	private LinkedItem<T> mFront;
	private LinkedItem<T> mRear;
	private int length;
	private LinkedQueueHelper helper = new LinkedQueueHelper();
	
	public LinkedQueue()
	{
	}
	
	public LinkedQueue(LinkedQueue<T> queue)
	{
		this.mFront = queue.mFront;
		this.mRear = queue.mRear;
		this.length = queue.length;
		this.helper = queue.helper;
	}
	
	@Override
	protected Object clone()
	{
		return new LinkedQueue<T>(this);
	}

	@Override
	public void enqueue(T t)
	{
		if(full()) throw new IllegalStateException("No space remained to enqueue a full queue!");
		LinkedItem<T> item = new LinkedItem<T>(t);
		if(mFront == null)
			mFront = item;
		if(mRear != null)
		{
			item.tail = mRear;
			mRear.head = item;
		}
		mRear = item;
		++length;
	}

	@Override
	public T dequeue()
	{
		if(empty()) throw new IllegalStateException("Can't dequeue a empty queue!");
		T t = mFront.value;
		if(mFront != mRear)
		{
			mFront = mFront.head;
			mFront.tail.head = null;
			mFront.tail = null;
		}else{
			mFront = mRear = null;
		}
		--length;
		return t;
	}

	@Override
	public boolean empty()
	{
		return mFront == null && mRear == null;
	}

	@Override
	public boolean full()
	{
		return false;
	}

	@Override
	public T front()
	{
		if(mFront == null) return null;
		return mFront.value;
	}

	@Override
	public T rear()
	{
		if(mRear == null) return null;
		return mRear.value;
	}

	@Override
	public Helper<T> getHelper()
	{
		return helper;
	}
	
	private class LinkedQueueHelper implements Helper<T>
	{
		@Override
		public boolean has(T t)
		{
			if(mFront == null) return false;
			return recurse(mFront, t);
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
			if(mFront != null)
				recurse(mFront, sb);
			if(sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}

		@Override
		public void clear()
		{
			if(!empty())
			{
				if(mFront == mRear)
				{
					mFront = mRear = null;
				}else{
					mFront.head.tail = null;
					mFront.head = null;
					mFront = null;
					if(mRear.tail != null)
					{
						mRear.tail.head = null;
						mRear.tail = null;
					}
					mRear = null;
				}
				length = 0;
			}
		}
		
		private void recurse(LinkedItem<T> item,StringBuffer sb)
		{
			sb.append(item.value).append(',');
			if(item.head != null)
				recurse(item.head,sb);
			else
				return;
		}
		
		private boolean recurse(LinkedItem<T> item,T t)
		{
			if(item.value == null && t == null)
				return true;
			else if(item.value.equals(t))
				return true;
			if(item.head != null)
				return recurse(item.head, t);
			else
				return false;
		}
	}
}
