package com.johnsoft.library.util.data.primary;

/**
 * 针对某些C/C++语言函数并不返回结果而是传入参数接收结果,由返回值判断接收成功与否做出不同处理,客观的讲,有其严谨的好处.
 * 故而模拟实现C/C++语言当中的这部分优良特性:
 * 1.C++风格引用
 * 2.C风格条件判断
 * 注:此类还在尝试摸索期,可能不断扩充,但保证不会修改;
 * 另注:该类名之所以命名为C是必要的,除因模拟C语言特性的因素,便利开发人员书写是主要原因
 * @author 李哲浩
 * @param <T> 引用所指泛型类型
 */
public class C<T>
{
	public T ref;
	
	public C(T ref)
	{
		this.ref=ref;
	}
	
	@Override
	public C<T> clone() 
	{
		C<T> c=new C<T>(ref);
		return c;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ref == null) ? 0 : ref.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null||ref == null)
			return false;
		if(ref.getClass().equals(obj.getClass()))
		{
			return ref.equals(obj);
		}
		if(obj instanceof C)
		{
			@SuppressWarnings("unchecked")
			C<T> other = (C<T>) obj;
			if (other.ref == null)
				return false;
			return ref.equals(other.ref);
		}
		return false;
	}

	@Override
	public String toString()
	{
		return ref==null?"null":ref.toString();
	}
	
	/**将任何类型转换成boolean类型以便用于if或while等的条件判断中*/
	public static boolean bool(Object obj)
	{
		if(obj==null)
		{
			return false;
		}
		if(obj instanceof Boolean)
		{
			return (Boolean)obj;
		}
		if(obj instanceof Number)
		{
			return ((Number)obj).doubleValue()!=0;
		}
		if(obj instanceof Character)
		{
			return ((Character)obj).charValue()!=0;
		}
		if(obj instanceof C)
		{
			@SuppressWarnings("rawtypes")
			C c=(C)obj;
			if(c.ref==null)
			{
				return false;
			}
			if(c.ref instanceof Boolean)
			{
				return (Boolean)c.ref;
			}
			if(c.ref instanceof Number)
			{
				return ((Number)c.ref).doubleValue()!=0;
			}
			if(c.ref instanceof Character)
			{
				return ((Character)c.ref).charValue()!=0;
			}
		}
		return true;
	}
}
