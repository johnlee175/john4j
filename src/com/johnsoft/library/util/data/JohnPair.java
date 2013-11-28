package com.johnsoft.library.util.data;

/**
 * 类似c++中STL的pair
 * @author 李哲浩
 */
public class JohnPair<T,V>
{
	public T first;
	public V second;
	
	public JohnPair(T first, V second)
	{
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JohnPair<?,?> other = (JohnPair<?,?>) obj;
		if (first == null)
		{
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null)
		{
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "JohnPair [first=" + first + ", second=" + second + "]";
	}
	
}
