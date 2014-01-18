package com.johnsoft.library.swing.o3d;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import com.johnsoft.library.swing.o3d.data.Matrix3;

public class Texture extends Component implements Serializable, Cloneable
{
	private static final long serialVersionUID = 1L;
	Matrix3 mat;
	Applet applet;
	URL url;
	String filename;
	protected Image texture;
	protected int[] texel;
	protected int width;
	protected int height;
	boolean repeatU = true;
	boolean repeatV = true;
	boolean linearFilter = true;

	public Texture(Applet applet, URL url, String filename)
	{
		this.mat = new Matrix3();
		this.applet = applet;
		try
		{
			this.url = new URL(url, filename);
		} catch (MalformedURLException e)
		{
		}
		this.filename = new String(filename);
		this.texture = null;
		this.texel = null;
		this.width = this.height = 0;
		this.repeatU = true;
		this.repeatV = true;
		this.linearFilter = true;
	}

	public Texture(String filename)
	{
		this.mat = new Matrix3();
		this.applet = null;
		this.url = null;
		this.filename = new String(filename);
		this.texture = null;
		this.texel = null;
		this.width = this.height = 0;
		this.repeatU = true;
		this.repeatV = true;
		this.linearFilter = true;
	}

	public Texture()
	{
		this("");
	}

	public Texture(Texture p)
	{
		mat = new Matrix3(p.mat);
		applet = p.applet;
		try
		{
			url = new URL(p.url, p.filename);
		} catch (MalformedURLException e)
		{
		}
		filename = new String(p.filename);
		texture = p.texture;
		texel = p.texel;
		width = p.width;
		height = p.height;
		repeatU = p.repeatU;
		repeatV = p.repeatV;
		linearFilter = p.linearFilter;
	}

	public void loadImage()
	{
		MediaTracker mt = new MediaTracker(this);
		if (filename == null)
			throw new NullPointerException("");
		if (applet == null && url == null)
			texture = this.getToolkit().getImage(filename);
		else
			texture = this.applet.getImage(url, filename);
		if (texture == null)
			throw new InternalError(filename);
		mt.addImage(texture, 1);
		try
		{
			mt.waitForAll();
		} catch (InterruptedException e)
		{
		}
		width = texture.getWidth(this);
		height = texture.getHeight(this);
		texel = new int[width * height];
		PixelGrabber pg = new PixelGrabber(texture, 0, 0, width, height, texel, 0,
				width);
		try
		{
			pg.grabPixels();
		} catch (InterruptedException e)
		{
		}
	}

	public void setRepeat(boolean repeatU, boolean repeatV)
	{
		this.repeatU = repeatU;
		this.repeatV = repeatV;
	}

	public void setRepeatU(boolean repeatU)
	{
		this.repeatU = repeatU;
	}

	public void setRepeatV(boolean repeatV)
	{
		this.repeatU = repeatV;
	}

	public boolean getRepeatU()
	{
		return repeatU;
	}

	public boolean getRepeatV()
	{
		return repeatV;
	}

	public void setLinearFilter(boolean linearFilter)
	{
		this.linearFilter = linearFilter;
	}

	public boolean getLinearFilter()
	{
		return linearFilter;
	}

	public void translate(double x, double y)
	{
		mat.a[2] += x;
		mat.a[5] += y;
	}

	public void scale(double x, double y)
	{
		mat.a[0] *= x;
		mat.a[4] *= y;
	}

	public void rotate(double theta)
	{
		Matrix3 m = new Matrix3();
		double s = Math.sin(theta);
		double c = Math.cos(theta);
		m.a[0] = c;
		m.a[1] = s;
		m.a[3] = -s;
		m.a[4] = c;
		mat.multiply(m);
	}
}
