package com.johnsoft.library.util.data;

import java.util.ArrayList;
import java.util.Collection;

public class JohnArrayList<E> extends ArrayList<E>
{
	private static final long serialVersionUID = 1L;
	
	private boolean listen=false;
	
	public JohnArrayList()
	{
		super();
		init();
	}
	
	public JohnArrayList(Collection<? extends E> c)
	{
		super(c);
		init();
	}
	
	public JohnArrayList(int initialCapacity)
	{
		super(initialCapacity);
		init();
	}
	
	/**初始化方法,构造中调用,默认空实现,如果需要应覆写*/
	protected void init()
	{
		
	}
	
	/**是否开启了监听开关*/
	public boolean listen()
	{
		return listen;
	}
	
	/**如果为true,表示开启了监听,如果为false,不再调用监听方法*/
	public void listen(boolean listen)
	{
		this.listen = listen;
	}
	
	/**
	 * 监听方法,覆写此方法以便在增加元素后回调
	 * @param e 增加的元素
	 */
	protected void valueAdded(E e)
	{
		
	}
	
	/**
	 * 监听方法,覆写此方法以便在移除元素后回调
	 * @param e 被移除的元素
	 */
	protected void valueRemoved(E e)
	{
		
	}
	
	/**
	 * 监听方法,覆写此方法以便在修改元素后回调
	 * @param oldE 修改前的元素
	 * @param newE 修改后的元素
	 */
	protected void valueModified(E oldE,E newE)
	{
		
	}
	
	/**
	 * 监听方法,覆写此方法以便在批量增加元素后回调
	 * @param eList 增加的元素列表
	 */
	protected void valueAdded(ArrayList<E> eList)
	{
		
	}
	
	/**
	 * 监听方法,覆写此方法以便在批量移除元素后回调
	 * @param eList 被移除的元素列表
	 */
	protected void valueRemoved(ArrayList<E> eList)
	{
		
	}
	
	/**为监听值变覆写*/
	@Override
	public boolean add(E e)
	{
		boolean result=super.add(e);
		if(listen)
		{
			valueAdded(e);
		}
		return result;
	}
	
	/**为监听值变覆写*/
	@Override
	public void add(int i, E e)
	{
		super.add(i, e);
		if(listen)
		{
			valueAdded(e);
		}
	}
	
	/**为监听值变覆写*/
	@Override
	public E remove(int i)
	{
		E e=super.remove(i);
		if(listen)
		{
			valueRemoved(e);
		}
		return e;
	}
	
	/**为监听值变覆写*/
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object e)
	{
		 boolean result=super.remove(e);
		 if(listen)
		 {
			valueRemoved((E)e);
		 }
		 return result;
	}
	
	/**为监听值变覆写*/
	@Override
	public E set(int i, E e)
	{
		E ex=super.set(i, e);
		if(listen)
		{
			valueModified(ex, e);
		}
		return ex;
	}
	
	/**为监听值变覆写*/
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		boolean result=super.addAll(c);
		if(listen)
		{
			valueAdded(new ArrayList<E>(c));
		}
		return result;
	}
	
	/**为监听值变覆写*/
	@Override
	public boolean addAll(int i, Collection<? extends E> c)
	{
		boolean result=super.addAll(i, c);
		if(listen)
		{
			valueAdded(new ArrayList<E>(c));
		}
		return result;
	}
	
	/**为监听值变覆写*/
	@Override
	public void clear()
	{
		super.clear();
		if(listen)
		{
			valueRemoved(this);
		}
	}
	
	/**为监听值变覆写*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean removeAll(Collection c)
	{
		boolean result=super.removeAll(c);
		if(listen)
		{
			valueRemoved(new ArrayList<E>(c));
		}
		return result;
	}
	
	/**为监听值变覆写*/
	@Override
	public void removeRange(int i1, int i2)
	{
		ArrayList<E> list=new ArrayList<E>();
		for(int i=i1;i<i2;i++)
		{
			list.add(get(i));
		}
		super.removeRange(i1, i2);
		if(listen)
		{
			valueRemoved(list);
		}
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		 throw new RuntimeException("The method must override before using!");
	}

}
