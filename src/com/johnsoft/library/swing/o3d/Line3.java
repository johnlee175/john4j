package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Vertex3;

public class Line3 extends Object3d
{
	private static final long serialVersionUID = 1L;
	public Vertex3 v1, v2;

	public Line3(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		v1 = new Vertex3(x1, y1, z1);
		v2 = new Vertex3(x2, y2, z2);
	}

	public Line3()
	{
		v1 = new Vertex3(0, 0, 0);
		v2 = new Vertex3(0, 0, 0);
	}

	public Line3(Vertex3 v1, Vertex3 v2)
	{
		this.v1 = new Vertex3(v1);
		this.v2 = new Vertex3(v2);
	}

	public void draw(Camera c, ObjectNode node)
	{

		Vertex3 w1 = node.getWorldPosition(v1);
		Vertex3 w2 = node.getWorldPosition(v2);

		Vertex3 v1 = c.getCameraPosition(w1);
		Vertex3 v2 = c.getCameraPosition(w2);

		if (v1.z > -c.focalLength && v2.z > -c.focalLength)
			return;
		double t;
		if (v1.z > -c.focalLength)
		{
			t = (c.focalLength + v1.z) / (v1.z - v2.z);
			v1.z = -c.focalLength;
			v1.x = v1.x + t * (v2.x - v1.x);
			v1.y = v1.y + t * (v2.y - v1.y);
		} else if (v2.z > -c.focalLength)
		{
			t = (c.focalLength + v1.z) / (v1.z - v2.z);
			v2.z = -c.focalLength;
			v2.x = v1.x + t * (v2.x - v1.x);
			v2.y = v1.y + t * (v2.y - v1.y);
		}

		int code1 = c.areaCode(v1);
		int code2 = c.areaCode(v2);
		int code;
		double s, x, y, z, x1, y1, z1, x2, y2, z2;
		x = y = z = 0;
		x1 = v1.x;
		y1 = v1.y;
		z1 = v1.z;
		x2 = v2.x;
		y2 = v2.y;
		z2 = v2.z;
		while ((code1 != 0) || (code2 != 0))
		{
			if ((code1 & code2) != 0)
				return;
			code = code1;
			if (code == 0)
				code = code2;
			if ((code & Camera.LEFT_VIEWVOLUME) != 0)
			{
				s = (x2 - x1) + c.screenMinX * (z2 - z1) / c.focalLength;
				if (Math.abs(s) < EPSILON)
					return;
				t = -(c.screenMinX * z1 / c.focalLength + x1) / s;
				z = z1 + t * (z2 - z1);
				x = -c.screenMinX / c.focalLength * z + EPSILON;
				y = y1 + t * (y2 - y1);
			} else if ((code & Camera.RIGHT_VIEWVOLUME) != 0)
			{
				s = (x2 - x1) + c.screenMaxX * (z2 - z1) / c.focalLength;
				if (Math.abs(s) < EPSILON)
					return;
				t = -(c.screenMaxX * z1 / c.focalLength + x1) / s;
				z = z1 + t * (z2 - z1);
				x = -c.screenMaxX / c.focalLength * z - EPSILON;
				y = y1 + t * (y2 - y1);
			} else if ((code & Camera.BOTTOM_VIEWVOLUME) != 0)
			{
				s = (y2 - y1) + c.screenMinY * (z2 - z1) / c.focalLength;
				if (Math.abs(s) < EPSILON)
					return;
				t = -(c.screenMinY * z1 / c.focalLength + y1) / s;
				z = z1 + t * (z2 - z1);
				y = -c.screenMinY / c.focalLength * z + EPSILON;
				x = x1 + t * (x2 - x1);
			} else if ((code & Camera.TOP_VIEWVOLUME) != 0)
			{
				s = (y2 - y1) + c.screenMaxY * (z2 - z1) / c.focalLength;
				if (Math.abs(s) < EPSILON)
					return;
				t = -(c.screenMaxY * z1 / c.focalLength + y1) / s;
				z = z1 + t * (z2 - z1);
				y = -c.screenMaxY / c.focalLength * z - EPSILON;
				x = x1 + t * (x2 - x1);
			}
			if (code == code1)
			{
				x1 = x;
				y1 = y;
				z1 = z;
				code1 = c.areaCode(new Vertex3(x, y, z));
			} else
			{
				x2 = x;
				y2 = y;
				z2 = z;
				code2 = c.areaCode(new Vertex3(x, y, z));
			}
		}
		c.drawLine(x1, y1, z1, x2, y2, z2);
	}
}
