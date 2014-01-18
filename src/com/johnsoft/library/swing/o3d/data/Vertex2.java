package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Vertex2 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public double x, y;

	public Vertex2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vertex2()
	{
		x = y = 0.0;
	}

	public Vertex2(Vertex2 v)
	{
		this(v.x, v.y);
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[x=" + x + ", y=" + y + "]";
	}
	
	@Override
	protected Vertex2 clone()
	{
		return new Vertex2(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Vertex2 other = (Vertex2) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
}