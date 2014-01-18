package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Index3;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex2;
import com.johnsoft.library.swing.o3d.data.Vertex3;


public class Cylinder extends Object3d implements Renderable
{
	private static final long serialVersionUID = 1L;
	// F = x*x+z*z-r*r = 0 ( -h/2 <= y <= h/2)
	double r; // (r>0)
	double h; // (h>0)
	BoundingBox bbox;
	int numUTessellate;
	int numVTessellate;

	public Cylinder(double r, double h)
	{
		if (r <= 0 || h <= 0)
			throw new InternalError("");
		this.r = r;
		this.h = h;
		this.numUTessellate = 30;
		this.numVTessellate = 4;
		Vertex3 center;
		double xsize, ysize, zsize;
		center = new Vertex3();
		center.x = center.y = center.z = 0;
		xsize = zsize = 2 * r * (1 + DELTA);
		ysize = h * (1 + DELTA);
		this.bbox = new BoundingBox(center, xsize, ysize, zsize);
	}

	public Cylinder()
	{
		this(1.0, 2.0);
	}

	public void print()
	{
		System.out.println(r + "," + h);
	}

	public void setTessellate(int unum, int vnum)
	{
		if (unum <= 0 || vnum <= 0)
			throw new InternalError("");
		numUTessellate = unum;
		numVTessellate = vnum;
	}

	public void getNearerIntersection(Ray ray, ObjectNode p)
	{
		if (ray == null || p == null)
			throw new NullPointerException();
		if (!(p.element instanceof Cylinder))
			return;
		Vertex3 o = p.getLocalPosition(ray.origin);
		Vector3 d = p.getLocalVector(ray.direction);

		if (!bbox.isHit(o, d))
			return;

		double t = HUGE, t1, x, z;
		if (Math.abs(d.y) > EPSILON)
		{
			// o.y+t*d.y = -h/2
			t1 = -(h / 2 + o.y) / d.y;
			x = o.x + t1 * d.x;
			z = o.z + t1 * d.z;
			if (x * x + z * z < r * r && t1 > EPSILON)
				t = t1;
			// o.y+t*d.y = h/2
			t1 = (h / 2 - o.y) / d.y;
			x = o.x + t1 * d.x;
			z = o.z + t1 * d.z;
			if (x * x + z * z < r * r && t1 > EPSILON && t1 < t)
				t = t1;
		}

		// (o.x+t*d.x)^2 + (o.z+t*d.z)^2 = (r*r)^2
		// これを　a * t^2 + b * t + c = 0　としよう。
		double a, b, c, det, q, t2;
		a = d.x * d.x + d.z * d.z;
		b = 2 * (o.x * d.x + o.z * d.z);
		c = o.x * o.x + o.z * o.z - r * r;
		det = b * b - 4.0 * a * c;
		if (det < 0.0 || Math.abs(a) < EPSILON)
			return;
		det = Math.sqrt(det);
		if (Math.abs(det) < EPSILON)
		{
			t1 = (-b) / (2.0 * a);
			q = (o.y + t1 * d.y + h / 2) / h;
			if (t1 < t && q * (1 - q) >= 0)
				t = t1;
		} else
		{
			t1 = (-b - det) / (2.0 * a);
			q = (o.y + t1 * d.y + h / 2) / h;
			if (Math.abs(t1) > EPSILON && t1 > 0.0)
			{
				if (t1 < t && q * (1 - q) >= 0)
					t = t1;
			} else
			{
				t2 = (-b + det) / (2.0 * a);
				if (t2 < 0.0 || Math.abs(t2) < EPSILON)
				{
					if (t > LARGE)
						return;
				}
				q = (o.y + t2 * d.y + h / 2) / h;
				if (t2 < t && q * (1 - q) >= 0)
					t = t2;
			}
		}
		if (t > LARGE)
			return;
		Vertex3 localSolution = new Vertex3(o.x + t * d.x, o.y + t * d.y, o.z + t
				* d.z);
		Vertex3 worldSolution = p.getWorldPosition(localSolution);
		double distance = Util3d.distance(ray.origin, worldSolution);
		if (distance < ray.t)
		{
			ray.t = distance;
			ray.hitNode = p;
			ray.intersection = worldSolution;
			ray.intersectionLocal = localSolution;
			return;
		}
		return;
	}

	public void setNearestIntersection(Ray ray)
	{
		boolean isBottomCap = false;
		boolean isTopCap = false;
		double x = ray.intersectionLocal.x;
		double y = ray.intersectionLocal.y;
		double z = ray.intersectionLocal.z;
		Vector3 localNormal;
		if (Math.abs(y + h / 2) < EPSILON && x * x + z * z <= r * r)
		{
			localNormal = new Vector3(0, -1, 0);
			isBottomCap = true;
		} else if (Math.abs(y - h / 2) < EPSILON && x * x + z * z <= r * r)
		{
			localNormal = new Vector3(0, 1, 0);
			isTopCap = true;
		} else
		{
			localNormal = new Vector3(x, 0, z);
		}
		localNormal.normalize();
		ray.intersectionNormal = ray.hitNode.getWorldNormal(localNormal);
		ray.intersectionNormal.normalize();
		ray.isNormal = true;
		ray.isColor = false;

		Material m = ray.hitNode.dummyMaterial;
		if (m == null)
			throw new NullPointerException("Material Null");
		if (m.texture == null)
		{
			ray.isTexture = false;
			return;
		}

		double u, v;
		if (isBottomCap || isTopCap)
		{
			u = (x + r) / (2 * r);
			v = (z + r) / (2 * r);
		} else
		{
			// x = -r * sin(theta)
			// y = t
			// z = -r * cos(theta)
			v = (y + h / 2) / h;
			// 0 <= theta <= 2*PI , atan2=>[-PI,PI]
			double theta = Math.PI + Math.atan2(x, z);
			u = theta / (2 * Math.PI);
		}
		Vector3 w = new Vector3(u, v, 1);
		Vector3 s = m.texture.mat.multiplyVector3(w);
		ray.u = s.x;
		ray.v = s.y;
	}

	public void draw(Camera c, ObjectNode node)
	{
		if (!(node.element instanceof Cylinder))
			return;
		drawCylinder(c, node, numUTessellate, numVTessellate);
	}

	public Vertex3 getCylinderCoord(double r, double h, double u0, double v0)
	{
		double u = 2.0 * Math.PI * u0;
		double v = h * (v0 - 0.5);
		double x = -Math.sin(u);
		double y = v;
		double z = -Math.cos(u);
		if (Math.abs(x) < EPSILON)
			x = 0;
		if (Math.abs(y) < EPSILON)
			y = 0;
		if (Math.abs(z) < EPSILON)
			z = 0;
		Vertex3 w = new Vertex3(r * x, y, r * z);
		return w;
	}

	public void drawCylinder(Camera c, ObjectNode node, int lx, int ly)
	{
		double u, v;
		int i, j/*, k*/;
		if (lx <= 0 || ly <= 0)
			return;
		Cylinder p = (Cylinder) (node.element);
		double du = 1.0;
		double dv = 1.0;
		double ustep = du / lx;
		double vstep = dv / ly;
		int numVertex = lx * (ly + 1) + 2 * (1 + lx);
		Vertex3[] vtx = new Vertex3[numVertex];
		int cnt = 0;
		vtx[cnt++] = new Vertex3(0, h / 2, 0);
		for (i = 0, u = 0.0; i < lx; i++, u += ustep)
			vtx[cnt++] = getCylinderCoord(p.r, p.h, u, 1.0);
		for (j = 0, v = 0.0; j < ly + 1; j++, v += vstep)
		{
			if (j == ly)
				v = 1.0;
			for (i = 0, u = 0.0; i < lx; i++, u += ustep)
				vtx[cnt++] = getCylinderCoord(p.r, p.h, u, v);
		}
		vtx[cnt++] = new Vertex3(0, -h / 2, 0);
		for (i = 0, u = 0.0; i < lx; i++, u += ustep)
			vtx[cnt++] = getCylinderCoord(p.r, p.h, u, 0.0);

		Vector3[] nml = new Vector3[numVertex];
		cnt = 0;
		nml[cnt++] = new Vector3(0, 1, 0);
		for (i = 0; i < lx; i++)
			nml[cnt++] = new Vector3(0, 1, 0);
		for (j = 0, v = vstep; j < ly + 1; j++, v += vstep)
		{
			if (j == ly)
				v = 1.0;
			for (i = 0, u = 0.0; i < lx; i++, u += ustep)
			{
				nml[cnt] = new Vector3(vtx[cnt].x / r, 0, vtx[cnt].z / r);
				cnt++;
			}
		}
		nml[cnt++] = new Vector3(0, -1, 0);
		for (i = 0; i < lx; i++)
			nml[cnt++] = new Vector3(0, -1, 0);

		Vertex2[] tx = new Vertex2[numVertex];
		cnt = 0;
		tx[cnt++] = new Vertex2(0.5, 0.5);
		for (i = 0, u = 0.0; i < lx; i++, u += ustep)
		{
			tx[cnt] = new Vertex2(vtx[cnt].x / r, -vtx[cnt].z / r);
			cnt++;
		}
		for (j = 0, v = 0.0; j < ly + 1; j++, v += vstep)
		{
			if (j == ly)
				v = 1.0;
			for (i = 0, u = 0.0; i < lx; i++, u += ustep)
			{
				tx[cnt] = new Vertex2();
				tx[cnt].x = u;
				tx[cnt].y = v;
				if (Math.abs(tx[cnt].x) < EPSILON)
					tx[cnt].x = 0;
				if (Math.abs(tx[cnt].y) < EPSILON)
					tx[cnt].y = 0;
				cnt++;
			}
		}
		tx[cnt++] = new Vertex2(0.5, 0.5);
		for (i = 0, u = 0.0; i < lx; i++, u += ustep)
		{
			tx[cnt] = new Vertex2(u, 0.0);
			cnt++;
		}
		int numTriangle = 2 * lx * ly + 2 * lx;
		Index3[] ind = new Index3[numTriangle];
		int m, n, o, r, q;
		cnt = 0;
		for (i = 0; i < lx; i++)
		{
			m = i + 1;
			n = (i == lx - 1) ? 1 : m + 1;
			ind[cnt++] = new Index3(0, m, n);
		}
		for (j = 0; j < ly; j++)
			for (i = 0; i < lx; i++)
			{
				m = (j + 1) * lx + i + 1;
				n = (i == lx - 1) ? m - i : m + 1;
				r = (j + 2) * lx + i + 1;
				q = (i == lx - 1) ? r - i : r + 1;
				ind[cnt++] = new Index3(m, q, r);
				ind[cnt++] = new Index3(m, n, q);

			}
		o = lx * (ly + 1) + lx + 1;
		for (i = 0; i < lx; i++)
		{
			m = o + 1 + i;
			n = (i == lx - 1) ? m - i : m + 1;
			ind[cnt++] = new Index3(o, n, m);

		}
		TriangleSet ts = new TriangleSet(numTriangle, ind, numVertex, vtx, nml, tx);
		ts.draw(c, node);
	}
}
