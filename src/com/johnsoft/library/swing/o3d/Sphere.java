package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Index3;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex2;
import com.johnsoft.library.swing.o3d.data.Vertex3;

public class Sphere extends Object3d implements Renderable
{
	private static final long serialVersionUID = 1L;
	// f(x,y,z) = x*x+y*y+z*z-r*r = 0
	double r; // (r>0)
	BoundingBox bbox;
	int numUTessellate;
	int numVTessellate;

	public Sphere(double r)
	{
		if (r <= 0)
			throw new InternalError("");
		Vertex3 center;
		double xsize, ysize, zsize;
		this.r = r;
		this.numUTessellate = 40;
		this.numVTessellate = 20;
		center = new Vertex3();
		center.x = center.y = center.z = 0;
		xsize = ysize = zsize = 2 * r * (1 + DELTA);
		this.bbox = new BoundingBox(center, xsize, ysize, zsize);
	}

	public Sphere()
	{
		this(1.0);
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
		if (!(p.element instanceof Sphere))
			return;
		Vertex3 o = p.getLocalPosition(ray.origin);
		Vector3 d = p.getLocalVector(ray.direction);

		if (!bbox.isHit(o, d))
			return;

		// (o.x+t*d.x)^2 + (o.y+t*d.y)^2 + (o.z+t*d.z)^2 = r^2
		// a * t^2 + b * t + c = 0　としよう。
		double a, b, c, det, t1, t2, t;
		a = d.x * d.x + d.y * d.y + d.z * d.z;
		b = 2 * (o.x * d.x + o.y * d.y + o.z * d.z);
		c = o.x * o.x + o.y * o.y + o.z * o.z - r * r;
		det = b * b - 4.0 * a * c;
		if (det < 0.0 || Math.abs(a) < EPSILON)
			return;
		det = Math.sqrt(det);
		if (Math.abs(det) < EPSILON)
		{
			t = (-b) / (2.0 * a);
		} else
		{
			t1 = (-b - det) / (2.0 * a);
			if (Math.abs(t1) > EPSILON && t1 > 0.0)
				t = t1;
			else
			{
				t2 = (-b + det) / (2.0 * a);
				if (t2 < 0.0 || Math.abs(t2) < EPSILON)
					return;
				t = t2;
			}
		}
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

		Vector3 localNormal = new Vector3(ray.intersectionLocal);
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

		double x = ray.intersectionLocal.x;
		double y = ray.intersectionLocal.y;
		double z = ray.intersectionLocal.z;

		// x = -r * sin(phi) * sin(theta)
		// y = -r * cos(phi)
		// z = -r * sin(phi) * cos(theta)
		// 0 <= phi <= PI , acos(1)=0, acos(-1)=PI acos=>[0,PI]
		double phi = Math.acos(-y / this.r);
		// 0 <= theta <= 2*PI , atan2=>[-PI,PI]
		double theta = Math.PI + Math.atan2(x, z);
		double u = theta / (2 * Math.PI);
		double v = phi / Math.PI;
		Vector3 w = new Vector3(u, v, 1);
		Vector3 s = m.texture.mat.multiplyVector3(w);
		ray.u = s.x;
		ray.v = s.y;
	}

	public void draw(Camera c, ObjectNode node)
	{
		if (!(node.element instanceof Sphere))
			return;
		Sphere p = (Sphere) node.element;
		double z0 = -c.focalLength;

		Vertex3 localCenter = new Vertex3(0, 0, 0);
		Vertex3 worldCenter = node.getWorldPosition(localCenter);
		Vertex3 cameraCenter = c.getCameraPosition(worldCenter);
		if (cameraCenter.z - p.r >= z0)
			return;

		double xmax = c.screenMaxX;
		double xmin = c.screenMinX;
		double ymax = c.screenMaxY;
		double ymin = c.screenMinY;
		if (!c.parallel)
		{
			Vector3 v = new Vector3(-z0, 0, xmax);
			double t = v.innerProduct(cameraCenter);
			// ax+by+cz+d=0と(x0,y0,z0)
			// h^2 = |a*x0+b*y0+c*z0+d|^2/(a*a+b*b+c*c)
			double u = t * t - p.r * p.r * v.size2();
			if (t >= 0 && u > 0)
				return;
			v = new Vector3(z0, 0, -xmin);
			t = v.innerProduct(cameraCenter);
			u = t * t - p.r * p.r * v.size2();
			if (t >= 0 && u > 0)
				return;
			v = new Vector3(0, -z0, ymax);
			t = v.innerProduct(cameraCenter);
			u = t * t - p.r * p.r * v.size2();
			if (t >= 0 && u > 0)
				return;
			v = new Vector3(0, z0, -ymin);
			t = v.innerProduct(cameraCenter);
			u = t * t - p.r * p.r * v.size2();
			if (t >= 0 && u > 0)
				return;
		} else
		{
			if (cameraCenter.x - r > c.screenMaxX)
				return;
			if (cameraCenter.x + r < c.screenMinX)
				return;
			if (cameraCenter.y - r > c.screenMaxY)
				return;
			if (cameraCenter.y + r < c.screenMinY)
				return;
		}
		drawSphere(c, node, numUTessellate, numVTessellate);
	}

	public Vertex3 getSphereCoord(double r, double u0, double v0)
	{
		double u = 2.0 * Math.PI * u0;
		double v = Math.PI * v0;
		double s = -Math.sin(v);
		double x = s * Math.sin(u);
		double y = -Math.cos(v);
		double z = s * Math.cos(u);
		if (Math.abs(x) < EPSILON)
			x = 0;
		if (Math.abs(y) < EPSILON)
			y = 0;
		if (Math.abs(z) < EPSILON)
			z = 0;
		Vertex3 w = new Vertex3(r * x, r * y, r * z);
		return w;
	}

	public void drawSphere(Camera c, ObjectNode node, int lx, int ly)
	{
		double u, v;
		int i, j/* ,k */;
		if (lx <= 0 || ly <= 0)
			return;
		if (!(node.element instanceof Sphere))
			return;
		Sphere p = (Sphere) (node.element);
		double du = 1.0;
		double dv = 1.0;
		double ustep = du / lx;
		double vstep = dv / ly;
		int numVertex = lx * (ly - 1) + 2;
		Vertex3[] vtx = new Vertex3[numVertex];
		int cnt = 0;
		vtx[cnt++] = getSphereCoord(p.r, 0, 0);
		for (j = 0, v = vstep; j < ly - 1; j++, v += vstep)
			for (i = 0, u = 0.0; i < lx; i++, u += ustep)
				vtx[cnt++] = getSphereCoord(p.r, u, v);
		vtx[cnt++] = getSphereCoord(p.r, 0, 1);
		Vector3[] nml = new Vector3[numVertex];
		cnt = 0;
		nml[cnt++] = new Vector3(0, -1, 0);
		for (j = 0, v = vstep; j < ly - 1; j++, v += vstep)
			for (i = 0, u = 0.0; i < lx; i++, u += ustep)
			{
				nml[cnt] = new Vector3(vtx[cnt]);
				nml[cnt].scale(1.0 / r);
				cnt++;
			}
		nml[cnt++] = new Vector3(0, 1, 0);
		Vertex2[] tx = new Vertex2[numVertex];
		cnt = 0;
		tx[cnt++] = new Vertex2(0, 0);
		for (j = 0, v = vstep; j < ly - 1; j++, v += vstep)
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
		tx[cnt++] = new Vertex2(0, 1);
		int numTriangle = 2 * lx * (ly - 1);
		Index3[] ind = new Index3[numTriangle];
		int m, n, o, r, q;
		cnt = 0;
		for (i = 0; i < lx; i++)
		{
			m = i + 1;
			n = (i == lx - 1) ? 0 : m + 1;
			ind[cnt++] = new Index3(m, 0, n);
		}
		for (j = 0; j < ly - 2; j++)
			for (i = 0; i < lx; i++)
			{
				m = j * lx + i + 1;
				n = (i == lx - 1) ? m - i : m + 1;
				r = (j + 1) * lx + i + 1;
				q = (i == lx - 1) ? r - i : r + 1;
				ind[cnt++] = new Index3(r, m, n);
				ind[cnt++] = new Index3(r, n, q);
			}
		o = lx * (ly - 1) + 1;
		for (i = 0; i < lx; i++)
		{
			m = lx * (ly - 2) + 1 + i;
			n = (i == lx - 1) ? m - i : m + 1;
			ind[cnt++] = new Index3(o, m, n);
		}
		TriangleSet ts = new TriangleSet(numTriangle, ind, numVertex, vtx, nml, tx);
		ts.draw(c, node);
	}

	public void print()
	{
		System.out.println(r);
	}
}
