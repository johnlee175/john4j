package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Color3;
import com.johnsoft.library.swing.o3d.data.Vector3;

public class DirectionalLight extends Object3d implements Renderable
{
	private static final long serialVersionUID = 1L;
	Vector3 direction;
	double intensity = 1.0;
	Color3 color;
	boolean on = true;
	double ambientIntensity = 0.0;

	public DirectionalLight()
	{
		direction = new Vector3(0, 0, -1);
		intensity = 1.0;
		color = new Color3(1, 1, 1);
		on = true;
		ambientIntensity = 0.0;
	}

	public DirectionalLight(DirectionalLight p)
	{
		direction = new Vector3(p.direction);
		intensity = p.intensity;
		color = new Color3(p.color);
		on = p.on;
		ambientIntensity = p.ambientIntensity;
	}

	public void setDirection(Vector3 v)
	{
		if (v.isZeroVector())
			throw new InternalError("");
		direction = v;
	}

	public void setDirection(double x, double y, double z)
	{
		if (Math.abs(x) < EPSILON && Math.abs(y) < EPSILON && Math.abs(z) < EPSILON)
			throw new InternalError("");
		direction.x = x;
		direction.y = y;
		direction.z = z;
	}

	public Vector3 getDirection()
	{
		return direction;
	}

	public void setIntensity(double intensity)
	{
		if (intensity < 0)
			throw new InternalError("");
		this.intensity = intensity;
	}

	public double getIntensity()
	{
		return intensity;
	}

	public void setAmbientIntensity(double ambientIntensity)
	{
		if (ambientIntensity <= 0)
			throw new InternalError("");
		this.ambientIntensity = ambientIntensity;
	}

	public double getAmbientIntensity()
	{
		return ambientIntensity;
	}

	public void setColor(Color3 color)
	{
		if (color.isInvalidColor())
			throw new InternalError("");
		this.color = color;
	}

	public void setColor(double r, double g, double b)
	{
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		color.r = r;
		color.g = g;
		color.b = b;
	}

	public Color3 getColor()
	{
		return color;
	}

	public void setSwitch(boolean on)
	{
		this.on = on;
	}

	public boolean getSwitch()
	{
		return on;
	}

	public void getNearerIntersection(Ray ray, ObjectNode node)
	{
	}

	public void setNearestIntersection(Ray ray)
	{
	}

	public void draw(Camera c, ObjectNode node)
	{
	}
}