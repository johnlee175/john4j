package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Color3;

public class Material extends Object3d
{
	private static final long serialVersionUID = 1L;
	Color3 ambientColor;
	Color3 diffuseColor;
	Color3 specularColor;
	Color3 emissiveColor;
	double shininess;
	double transparency;
	double reflection;
	double refraction;
	Texture texture = null;

	public Material()
	{
		ambientColor = new Color3(0, 0, 0);
		diffuseColor = new Color3(1, 1, 1);
		specularColor = new Color3(0, 0, 0);
		emissiveColor = new Color3(0, 0, 0);
		shininess = 20.0;
		transparency = 0.0;
		reflection = 0.0;
		refraction = 1.0;
		texture = null;
	}

	public Material(Material p)
	{
		ambientColor = new Color3(p.ambientColor);
		diffuseColor = new Color3(p.diffuseColor);
		specularColor = new Color3(p.specularColor);
		emissiveColor = new Color3(p.emissiveColor);
		shininess = p.shininess;
		transparency = p.transparency;
		reflection = p.reflection;
		refraction = p.refraction;
		texture = p.texture;
	}

	public void setAmbientColor(Color3 c)
	{
		if (c.isInvalidColor())
			throw new InternalError("");
		ambientColor = c;
	}

	public void setAmbientColor(double r, double g, double b)
	{
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		ambientColor.r = r;
		ambientColor.g = g;
		ambientColor.b = b;
	}

	public Color3 getAmbientColor()
	{
		return ambientColor;
	}

	public void setDiffuseColor(Color3 c)
	{
		if (c.isInvalidColor())
			throw new InternalError("");
		diffuseColor = c;
	}

	public void setDiffuseColor(double r, double g, double b)
	{
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		diffuseColor.r = r;
		diffuseColor.g = g;
		diffuseColor.b = b;
	}

	public Color3 getDiffuseColor()
	{
		return diffuseColor;
	}

	public void setSpecularColor(Color3 c)
	{
		if (c.isInvalidColor())
			throw new InternalError("");
		specularColor = c;
	}

	public void setSpecularColor(double r, double g, double b)
	{
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		specularColor.r = r;
		specularColor.g = g;
		specularColor.b = b;
	}

	public Color3 getSpecularColor()
	{
		return specularColor;
	}

	public void setEmissiveColor(Color3 c)
	{
		if (c.isInvalidColor())
			throw new InternalError("");
		emissiveColor = c;
	}

	public void setEmissiveColor(double r, double g, double b)
	{
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		emissiveColor.r = r;
		emissiveColor.g = g;
		emissiveColor.b = b;
	}

	public Color3 getEmissiveColor()
	{
		return emissiveColor;
	}

	public void setShininess(double s)
	{
		if (s < 0)
			throw new InternalError("");
		shininess = s;
	}

	public double getShininess()
	{
		return shininess;
	}

	public void setTransparency(double t)
	{
		if (t < 0)
			throw new InternalError("");
		transparency = t;
	}

	public double getTransparency()
	{
		return transparency;
	}

	public void setReflection(double r)
	{
		if (r < 0)
			throw new InternalError("");
		reflection = r;
	}

	public double getReflection()
	{
		return reflection;
	}

	public void setRefraction(double r)
	{
		if (r < 0)
			throw new InternalError("");
		refraction = r;
	}

	public double getRefraction()
	{
		return refraction;
	}

	public void setTexture(Texture p)
	{
		if (p == null)
			throw new NullPointerException("");
		texture = p;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public void print()
	{
		System.out.println(shininess);
		System.out.println(transparency);
		System.out.println(reflection);
		System.out.println(refraction);
		System.out.println(texture);
	}
}
