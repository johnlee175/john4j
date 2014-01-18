package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Vertex3 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public double x, y, z;

	public Vertex3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vertex3()
	{
		x = y = z = 0.0;
	}

	public Vertex3(Vertex3 v)
	{
		this(v.x, v.y, v.z);
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	@Override
	protected Vertex3 clone()
	{
		return new Vertex3(this);
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
		temp = Double.doubleToLongBits(z);
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
		Vertex3 other = (Vertex3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
}