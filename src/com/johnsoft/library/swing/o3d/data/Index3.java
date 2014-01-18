package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Index3 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public int v1, v2, v3;

	public Index3(int v1, int v2, int v3)
	{
		if (v1 < 0 || v2 < 0 || v3 < 0)
			throw new InternalError("The param shouldn't be negative!");
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public Index3(Index3 v)
	{
		this(v.v1, v.v2, v.v3);
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[v1=" + v1 + ", v2=" + v2 + ", v3=" + v3 + "]";
	}
	
	@Override
	protected Index3 clone()
	{
		return new Index3(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + v1;
		result = prime * result + v2;
		result = prime * result + v3;
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
		Index3 other = (Index3) obj;
		if (v1 != other.v1)
			return false;
		if (v2 != other.v2)
			return false;
		if (v3 != other.v3)
			return false;
		return true;
	}
	
}
