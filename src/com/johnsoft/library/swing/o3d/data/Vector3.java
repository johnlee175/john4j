package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Vector3 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public double x, y, z;

	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3()
	{
		x = y = z = 0.0;
	}

	public Vector3(Vector3 v)
	{
		this(v.x, v.y, v.z);
	}

	public Vector3(Vertex3 v)
	{
		this(v.x, v.y, v.z);
	}

	public boolean isZeroVector()
	{
		if (Math.abs(x) < EPSILON && Math.abs(y) < EPSILON && Math.abs(z) < EPSILON)
			return true;
		return false;
	}

	public double size()
	{
		double t = x * x + y * y + z * z;
		t = Math.sqrt(t);
		return t;
	}

	public double size2()
	{
		double t = x * x + y * y + z * z;
		return t;
	}

	public void normalize() throws ArithmeticException
	{
		double t = size();
		if (Math.abs(t) < EPSILON)
			throw new ArithmeticException();
		t = 1.0 / t;
		x *= t;
		y *= t;
		z *= t;
	}

	public void scale(double t)
	{
		this.x *= t;
		this.y *= t;
		this.z *= t;
	}
	
	public void add(double x, double y, double z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void add(Vector3 v)
	{
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	public void add(Vertex3 v)
	{
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}
	
	public void subtract(double x, double y, double z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void subtract(Vector3 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}

	public void subtract(Vertex3 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}

	public void assign(Vector3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public void assign(Vertex3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	@Override
	protected Vector3 clone()
	{
		return new Vector3(this);
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
		Vector3 other = (Vector3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	public double innerProduct(double x, double y, double z)
	{
		double t = this.x * x + this.y * y + this.z * z;
		return t;
	}
	
	public double innerProduct(Vector3 a)
	{
		double t = this.x * a.x + this.y * a.y + this.z * a.z;
		return t;
	}

	public double innerProduct(Vertex3 a)
	{
		double t = this.x * a.x + this.y * a.y + this.z * a.z;
		return t;
	}
	
	public Vector3 crossProduct(Vector3 b)
	{
		Vector3 c = new Vector3();
		c.x = this.y * b.z - b.y * this.z;
		c.y = this.z * b.x - b.z * this.x;
		c.z = this.x * b.y - b.x * this.y;
		return c;
	}

}