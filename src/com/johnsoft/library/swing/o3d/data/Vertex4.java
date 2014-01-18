package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Vertex4 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public double x, y, z, w;

	public Vertex4(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vertex4(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}

	public Vertex4()
	{
		x = y = z = w = 0.0;
	}

	public Vertex4(Vertex4 v)
	{
		this(v.x, v.y, v.z, v.w);
	}
	
	public Vertex4(Vertex3 v)
	{
		this(v.x, v.y, v.z);
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
	
	@Override
	protected Vertex4 clone()
	{
		return new Vertex4(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(w);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Vertex4 other = (Vertex4) obj;
		if (Double.doubleToLongBits(w) != Double.doubleToLongBits(other.w))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

}