package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Vector4 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public double x, y, z, w;

	public Vector4(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}

	public Vector4()
	{
		x = y = z = w = 0.0;
	}

	public Vector4(Vector4 v)
	{
		this(v.x, v.y, v.z, v.w);
	}
	
	public Vector4(Vertex4 v)
	{
		this(v.x, v.y, v.z, v.w);
	}
	
	public Vector4(Vector3 v)
	{
		this(v.x, v.y, v.z);
	}
	
	public Vector4(Vertex3 v)
	{
		this(v.x, v.y, v.z);
	}
	
	public boolean isZeroVector()
	{
		if (Math.abs(x) < EPSILON && Math.abs(y) < EPSILON && Math.abs(z) < EPSILON && Math.abs(w) < EPSILON)
			return true;
		return false;
	}
	
	public double size()
	{
		double t = x * x + y * y + z * z + w * w;
		t = Math.sqrt(t);
		return t;
	}

	public double size2()
	{
		double t = x * x + y * y + z * z + w * w;
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
		w *= t;
	}
	
	public void scale(double t)
	{
		this.x *= t;
		this.y *= t;
		this.z *= t;
		this.w *= t;
	}
	
	public void add(double x, double y, double z, double w)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
	}
	
	public void add(Vector4 v)
	{
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		this.w += v.w;
	}

	public void add(Vertex4 v)
	{
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		this.w += v.w;
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
	
	public void subtract(double x, double y, double z, double w)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
	}
	
	public void subtract(Vector4 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		this.w -= v.w;
	}

	public void subtract(Vertex4 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		this.w -= v.w;
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

	public void assign(Vector4 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	public void assign(Vertex4 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	public void assign(Vector3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = 1.0;
	}

	public void assign(Vertex3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = 1.0;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + "; " + "[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
	
	@Override
	protected Vector4 clone()
	{
		return new Vector4(this);
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
		Vector4 other = (Vector4) obj;
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

	public double innerProduct(double x, double y, double z, double w)
	{
		double t = this.x * x + this.y * y + this.z * z + this.w * w;
		return t;
	}
	
	public double innerProduct(Vector4 a)
	{
		double t = this.x * a.x + this.y * a.y + this.z * a.z + this.w * w;
		return t;
	}

	public double innerProduct(Vertex4 a)
	{
		double t = this.x * a.x + this.y * a.y + this.z * a.z + this.w * a.w;
		return t;
	}
	
}