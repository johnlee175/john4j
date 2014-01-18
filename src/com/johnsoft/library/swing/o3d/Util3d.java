package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Index3;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex3;

public class Util3d
{
	public static double distance(Vertex3 a, Vertex3 b)
	{
		double t = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)
				+ (a.z - b.z) * (a.z - b.z);
		return Math.sqrt(t);
	}
	
	public static boolean isOutOfBounds(Index3 idx, int min, int max)
	{
		if (idx.v1 < min || idx.v1 > max)
			return true;
		if (idx.v2 < min || idx.v2 > max)
			return true;
		if (idx.v3 < min || idx.v3 > max)
			return true;
		return false;
	}
	
	public static Vector3 crossProduct(Vector3 a, Vector3 b)
	{
		Vector3 c = new Vector3();
		c.x = a.y * b.z - b.y * a.z;
		c.y = a.z * b.x - b.z * a.x;
		c.z = a.x * b.y - b.x * a.y;
		return c;
	}
	
	public static double innerProduct(Vector3 a, Vector3 b)
	{
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public static double determinant(
			double a0, double a1, double a2,
			double a3, double a4, double a5,
			double a6, double a7, double a8)
	{
		  return
			a0*a4*a8 +
			a1*a5*a6 +
			a2*a7*a3 -
			a6*a4*a2 -
			a5*a7*a0 -
			a8*a3*a1;
	}
}
