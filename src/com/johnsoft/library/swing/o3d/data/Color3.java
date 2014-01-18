package com.johnsoft.library.swing.o3d.data;

import com.johnsoft.library.swing.o3d.Object3d;

public class Color3 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public double r, g, b;

	public Color3(double r, double g, double b)
	{
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("The params shouldn't be a negative!");
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Color3()
	{
		this(0, 0, 0);
	}

	public Color3(Color3 c)
	{
		this(c.r, c.g, c.b);
	}

	public void clamp()
	{
		if (this.r < 0.0)
			this.r = 0.0;
		else if (this.r > 1.0)
			this.r = 1.0;
		if (this.g < 0.0)
			this.g = 0.0;
		else if (this.g > 1.0)
			this.g = 1.0;
		if (this.b < 0.0)
			this.b = 0.0;
		else if (this.b > 1.0)
			this.b = 1.0;
	}

	public boolean isInvalidColor()
	{
		if (r < 0 || g < 0 || b < 0)
			return true;
		return false;
	}

	public void scale(double ractor)
	{
		if (ractor < 0)
			throw new InternalError("The param shouldn't be a negative!");
		this.r *= ractor;
		this.g *= ractor;
		this.b *= ractor;
	}

	public void assign(Color3 c)
	{
		if (c.isInvalidColor())
			throw new InternalError("The param of Color3 object is invalid color!");
		this.r = c.r;
		this.g = c.g;
		this.b = c.b;
	}

	public void add(Color3 c)
	{
		this.r += c.r;
		this.g += c.g;
		this.b += c.b;
	}
	
	public void add(double r, double g, double b)
	{
		this.r += r;
		this.g += g;
		this.b += b;
	}

	public void subtract(Color3 c)
	{
		this.r -= c.r;
		this.g -= c.g;
		this.b -= c.b;
	}
	
	public void subtract(double r, double g, double b)
	{
		this.r -= r;
		this.g -= g;
		this.b -= b;
	}

	public void multiply(Color3 c)
	{
		this.r *= c.r;
		this.g *= c.g;
		this.b *= c.b;
	}
	
	public void multiply(double r, double g, double b)
	{
		this.r *= r;
		this.g *= g;
		this.b *= b;
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[r=" + r + ", g=" + g + ", b=" + b + "]";
	}
	
	@Override
	protected Color3 clone()
	{
		return new Color3(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(g);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(r);
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
		Color3 other = (Color3) obj;
		if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
			return false;
		if (Double.doubleToLongBits(g) != Double.doubleToLongBits(other.g))
			return false;
		if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
			return false;
		return true;
	}
	
}