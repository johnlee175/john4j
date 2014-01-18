package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Color3;
import com.johnsoft.library.swing.o3d.data.Index3;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex2;
import com.johnsoft.library.swing.o3d.data.Vertex3;

public class TriangleSet extends Object3d implements Renderable
{
	private static final long serialVersionUID = 1L;
	boolean vertexColor = false;
	boolean vertexNormal = false;
	boolean vertexTexture = false;
	int numTriangle;
	Index3[] faceIndex;
	int numVertex;
	Vertex3[] vertex;
	int numColor;
	Index3[] colorIndex;
	Color3[] color;
	int numNormal;
	Index3[] normalIndex;
	Vector3[] normal;
	int numTexture;
	Index3[] textureIndex;
	Vertex2[] texture;
	Vertex3 center;
	double xsize, ysize, zsize;
	BoundingBox bbox;
	private double xmin, ymin, zmin, xmax, ymax, zmax;
	// private final String fErr = "";
	private final String cErr = "";
	private final String nErr = "";
	private final String tErr = "";

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] colorIndex, int numColor, Color3[] color,
			Index3[] normalIndex, int numNormal, Vector3[] normal,
			Index3[] textureIndex, int numTexture, Vertex2[] texture)
	{
		this.numTriangle = numTriangle;
		this.faceIndex = new Index3[numTriangle];
		for (int i = 0; i < numTriangle; i++)
		{
			this.faceIndex[i] = new Index3(faceIndex[i]);
		}
		xmin = ymin = zmin = HUGE;
		xmax = ymax = zmax = -HUGE;
		this.numVertex = numVertex;
		this.vertex = new Vertex3[numVertex];
		for (int i = 0; i < numVertex; i++)
		{
			this.vertex[i] = new Vertex3(vertex[i]);
			if (vertex[i].x < xmin)
				xmin = vertex[i].x;
			if (vertex[i].x > xmax)
				xmax = vertex[i].x;
			if (vertex[i].y < ymin)
				ymin = vertex[i].y;
			if (vertex[i].y > ymax)
				ymax = vertex[i].y;
			if (vertex[i].z < zmin)
				zmin = vertex[i].z;
			if (vertex[i].z > zmax)
				zmax = vertex[i].z;
		}
		double deltax = (xmax - xmin) * DELTA;
		double deltay = (ymax - ymin) * DELTA;
		double deltaz = (zmax - zmin) * DELTA;
		xmin -= deltax / 2;
		xmax += deltax / 2;
		ymin -= deltay / 2;
		ymax += deltay / 2;
		zmin -= deltaz / 2;
		zmax += deltaz / 2;
		center = new Vertex3();
		center.x = (xmin + xmax) / 2;
		center.y = (ymin + ymax) / 2;
		center.z = (zmin + zmax) / 2;
		xsize = (xmax - xmin);
		ysize = (ymax - ymin);
		zsize = (zmax - zmin);
		bbox = new BoundingBox(center, xsize, ysize, zsize);
		this.numColor = numColor;
		if (color == null)
		{
			this.vertexColor = false;
			this.color = null;
			this.colorIndex = null;
		} else if (colorIndex == null)
		{
			this.vertexColor = true;
			this.colorIndex = null;
			this.color = new Color3[numColor];
			for (int i = 0; i < numColor; i++)
				this.color[i] = new Color3(color[i]);
		} else
		{
			this.vertexColor = true;
			this.colorIndex = new Index3[numTriangle];
			for (int i = 0; i < numTriangle; i++)
			{
				if (Util3d.isOutOfBounds(colorIndex[i], 0, numColor - 1))
					throw new IndexOutOfBoundsException(cErr);
				this.colorIndex[i] = new Index3(colorIndex[i]);
			}
			this.color = new Color3[numColor];
			for (int i = 0; i < numColor; i++)
				this.color[i] = new Color3(color[i]);
		}
		this.numNormal = numNormal;
		if (normal == null)
		{
			this.vertexNormal = false;
			this.normal = null;
			this.normalIndex = null;
		} else if (normalIndex == null)
		{
			this.vertexNormal = true;
			this.normalIndex = null;
			this.normal = new Vector3[numNormal];
			for (int i = 0; i < numNormal; i++)
			{
				this.normal[i] = new Vector3(normal[i]);
				this.normal[i].normalize();
			}
		} else
		{
			this.vertexNormal = true;
			this.normalIndex = new Index3[numTriangle];
			for (int i = 0; i < numTriangle; i++)
			{
				if (Util3d.isOutOfBounds(normalIndex[i], 0, numNormal - 1))
					throw new IndexOutOfBoundsException(nErr);
				this.normalIndex[i] = new Index3(normalIndex[i]);
			}
			this.normal = new Vector3[numNormal];
			for (int i = 0; i < numNormal; i++)
			{
				this.normal[i] = new Vector3(normal[i]);
				this.normal[i].normalize();
			}
		}
		this.numTexture = numTexture;
		if (texture == null)
		{
			this.vertexTexture = false;
			this.textureIndex = null;
			this.texture = null;
		} else if (textureIndex == null)
		{
			this.vertexTexture = true;
			this.textureIndex = null;
			this.texture = new Vertex2[numTexture];
			for (int i = 0; i < numTexture; i++)
				this.texture[i] = new Vertex2(texture[i]);
		} else
		{
			this.vertexTexture = true;
			this.textureIndex = new Index3[numTriangle];
			for (int i = 0; i < numTriangle; i++)
			{
				if (Util3d.isOutOfBounds(textureIndex[i], 0, numTexture - 1))
					throw new IndexOutOfBoundsException(tErr);
				this.textureIndex[i] = new Index3(textureIndex[i]);
			}
			this.texture = new Vertex2[numTexture];
			for (int i = 0; i < numTexture; i++)
				this.texture[i] = new Vertex2(texture[i]);
		}
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, null, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, null, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] colorIndex, int numColor, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, null, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] normalIndex, int numNormal, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				normalIndex, numNormal, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, null, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] textureIndex, int numTexture, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, null, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Color3[] color, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vector3[] normal, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] colorIndex, int numColor, Color3[] color,
			Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vector3[] normal, Index3[] colorIndex, int numColor,
			Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Color3[] color, Index3[] normalIndex, int numNormal,
			Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				normalIndex, numNormal, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] normalIndex, int numNormal, Vector3[] normal,
			Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				normalIndex, numNormal, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] colorIndex, int numColor, Color3[] color,
			Index3[] normalIndex, int numNormal, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, normalIndex, numNormal, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] normalIndex, int numNormal, Vector3[] normal,
			Index3[] colorIndex, int numColor, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, normalIndex, numNormal, normal, null, numVertex, null);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Color3[] color, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, null, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vertex2[] texture, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, null, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] colorIndex, int numColor, Color3[] color,
			Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, null, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Color3[] color, Index3[] textureIndex, int numTexture,
			Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, null, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] textureIndex, int numTexture,
			Vertex2[] texture, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, color,
				null, numVertex, null, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vertex2[] texture, Index3[] colorIndex, int numColor,
			Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, null, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] textureIndex, int numTexture,
			Vertex2[] texture, Index3[] colorIndex, int numColor, Color3[] color)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, null, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] colorIndex, int numColor, Color3[] color,
			Index3[] textureIndex, int numTexture, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, colorIndex, numColor,
				color, null, numVertex, null, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vector3[] normal, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, normal, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vertex2[] texture, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, normal, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] normalIndex, int numNormal, Vector3[] normal,
			Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				normalIndex, numNormal, normal, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vector3[] normal, Index3[] textureIndex,
			int numTexture, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, normal, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] textureIndex, int numTexture,
			Vertex2[] texture, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				null, numVertex, normal, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Vertex2[] texture, Index3[] normalIndex, int numNormal,
			Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				normalIndex, numNormal, normal, null, numVertex, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] textureIndex, int numTexture,
			Vertex2[] texture, Index3[] normalIndex, int numNormal, Vector3[] normal)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				normalIndex, numNormal, normal, textureIndex, numTexture, texture);
	}

	public TriangleSet(int numTriangle, Index3[] faceIndex, int numVertex,
			Vertex3[] vertex, Index3[] normalIndex, int numNormal, Vector3[] normal,
			Index3[] textureIndex, int numTexture, Vertex2[] texture)
	{
		this(numTriangle, faceIndex, numVertex, vertex, null, numVertex, null,
				normalIndex, numNormal, normal, textureIndex, numTexture, texture);
	}

	public boolean isSameDirection(double a, double b, double c, double x1,
			double y1, double z1, double x2, double y2, double z2, double x,
			double y, double z)
	{
		Vector3 u = new Vector3(x2 - x1, y2 - y1, z2 - z1);
		Vector3 v = new Vector3(x - x2, y - y2, z - z2);
		Vector3 w = u.crossProduct(v);
		double t = w.innerProduct(a, b, c);
		if (t > 0 || Math.abs(t) < EPSILON)
			return true;
		return false;
	}

	// 0.5*|(b-a)x(c-a)|=0.5*|b-a||c-a|sin(theta)
	public double triangleArea(Vertex3 a, Vertex3 b, Vertex3 c)
	{
		Vector3 u = new Vector3(b);
		u.subtract(a);
		Vector3 v = new Vector3(c);
		v.subtract(a);
		Vector3 w = u.crossProduct(v);
		double t = 0.5 * w.size();
		return t;
	}

	public void getNearerIntersection(Ray ray, ObjectNode p)
	{
		if (ray == null || p == null)
			throw new NullPointerException();
		if (!(p.element instanceof TriangleSet))
			return;
		Vertex3 o = p.getLocalPosition(ray.origin);
		Vector3 d = p.getLocalVector(ray.direction);
		double t, x, y, z;

		if (!bbox.isHit(o, d))
			return;

		t = HUGE;
		for (int i = 0; i < numTriangle; i++)
		{
			Vertex3 v1 = vertex[faceIndex[i].v1];
			Vertex3 v2 = vertex[faceIndex[i].v2];
			Vertex3 v3 = vertex[faceIndex[i].v3];
			// a*x+b*y+c*z+f=0
			double a, b, c, f;
			double x1, y1, z1, x2, y2, z2, x3, y3, z3;
			x1 = v1.x;
			y1 = v1.y;
			z1 = v1.z;
			x2 = v2.x;
			y2 = v2.y;
			z2 = v2.z;
			x3 = v3.x;
			y3 = v3.y;
			z3 = v3.z;
			a = Util3d.determinant(y1, z1, 1, y2, z2, 1, y3, z3, 1);
			b = -Util3d.determinant(x1, z1, 1, x2, z2, 1, x3, z3, 1);
			c = Util3d.determinant(x1, y1, 1, x2, y2, 1, x3, y3, 1);
			f = -Util3d.determinant(x1, y1, z1, x2, y2, z2, x3, y3, z3);
			Vector3 nv = new Vector3(a, b, c);
			t = nv.innerProduct(d);
			if (Math.abs(t) < EPSILON)
				continue;
			t = -(f + nv.innerProduct(o)) / t;
			if (t < 0)
				continue;

			x = o.x + t * d.x;
			y = o.y + t * d.y;
			z = o.z + t * d.z;
			boolean b1 = isSameDirection(a, b, c, x1, y1, z1, x2, y2, z2, x, y, z);
			boolean b2 = isSameDirection(a, b, c, x2, y2, z2, x3, y3, z3, x, y, z);
			boolean b3 = isSameDirection(a, b, c, x3, y3, z3, x1, y1, z1, x, y, z);
			if (!b1 || !b2 || !b3)
				continue;
			Vertex3 localSolution = new Vertex3(x, y, z);
			Vertex3 worldSolution = p.getWorldPosition(localSolution);
			double distance = Util3d.distance(ray.origin, worldSolution);
			if (distance < ray.t)
			{
				ray.t = distance;
				ray.hitNode = p;
				ray.hitIndex = i;
				ray.intersection = worldSolution;
				ray.intersectionLocal = localSolution;
				TriangleSet ts = (TriangleSet) p.element;
				ray.isColor = ts.vertexColor;
				ray.isNormal = ts.vertexNormal;
				ray.isTexture = ts.vertexTexture;
				continue;
			}
		}
		return;
	}

	public void setNearestIntersection(Ray ray)
	{
		if (ray.hitIndex < 0)
			return;
		double u = 0, v = 0;
		Vector3 localNormal = new Vector3();
		Vertex3 v1 = vertex[faceIndex[ray.hitIndex].v1];
		Vertex3 v2 = vertex[faceIndex[ray.hitIndex].v2];
		Vertex3 v3 = vertex[faceIndex[ray.hitIndex].v3];
		double x1, y1, z1, x2, y2, z2, x3, y3, z3;
		x1 = v1.x;
		y1 = v1.y;
		z1 = v1.z;
		x2 = v2.x;
		y2 = v2.y;
		z2 = v2.z;
		x3 = v3.x;
		y3 = v3.y;
		z3 = v3.z;
		if (!ray.isNormal)
		{
			// a*x+b*y+c*z+d=0
			double a, b, c;
			a = Util3d.determinant(y1, z1, 1, y2, z2, 1, y3, z3, 1);
			b = -Util3d.determinant(x1, z1, 1, x2, z2, 1, x3, z3, 1);
			c = Util3d.determinant(x1, y1, 1, x2, y2, 1, x3, y3, 1);
			localNormal = new Vector3(a, b, c);
		}
		if (ray.isNormal || ray.isColor || ray.isTexture)
		{
			double w = triangleArea(v1, v2, v3);
			double w1 = triangleArea(ray.intersectionLocal, v2, v3);
			double w2 = triangleArea(ray.intersectionLocal, v3, v1);
			double w3 = triangleArea(ray.intersectionLocal, v1, v2);
			if (Math.abs(w) < EPSILON)
				throw new ArithmeticException();
			w1 /= w;
			w2 /= w;
			w3 /= w;
			if (ray.isNormal)
			{
				double nx, ny, nz;
				Vector3 n1, n2, n3;
				if (normalIndex == null)
				{
					n1 = normal[faceIndex[ray.hitIndex].v1];
					n2 = normal[faceIndex[ray.hitIndex].v2];
					n3 = normal[faceIndex[ray.hitIndex].v3];
				} else
				{
					n1 = normal[normalIndex[ray.hitIndex].v1];
					n2 = normal[normalIndex[ray.hitIndex].v2];
					n3 = normal[normalIndex[ray.hitIndex].v3];
				}
				nx = w1 * n1.x + w2 * n2.x + w3 * n3.x;
				ny = w1 * n1.y + w2 * n2.y + w3 * n3.y;
				nz = w1 * n1.z + w2 * n2.z + w3 * n3.z;
				localNormal = new Vector3(nx, ny, nz);
			}
			if (ray.isColor)
			{
				double r, g, b;
				Color3 c1, c2, c3;
				if (colorIndex == null)
				{
					c1 = color[faceIndex[ray.hitIndex].v1];
					c2 = color[faceIndex[ray.hitIndex].v2];
					c3 = color[faceIndex[ray.hitIndex].v3];
				} else
				{
					c1 = color[colorIndex[ray.hitIndex].v1];
					c2 = color[colorIndex[ray.hitIndex].v2];
					c3 = color[colorIndex[ray.hitIndex].v3];
				}
				r = w1 * c1.r + w2 * c2.r + w3 * c3.r;
				g = w1 * c1.g + w2 * c2.g + w3 * c3.g;
				b = w1 * c1.b + w2 * c2.b + w3 * c3.b;
				ray.intersectionColor = new Color3(r, g, b);
			}
			if (ray.isTexture)
			{
				Vertex2 t1, t2, t3;
				if (textureIndex == null)
				{
					t1 = texture[faceIndex[ray.hitIndex].v1];
					t2 = texture[faceIndex[ray.hitIndex].v2];
					t3 = texture[faceIndex[ray.hitIndex].v3];
				} else
				{
					t1 = texture[textureIndex[ray.hitIndex].v1];
					t2 = texture[textureIndex[ray.hitIndex].v2];
					t3 = texture[textureIndex[ray.hitIndex].v3];
				}
				u = w1 * t1.x + w2 * t2.x + w3 * t3.x;
				v = w1 * t1.y + w2 * t2.y + w3 * t3.y;
			}
		}
		localNormal.normalize();
		ray.intersectionNormal = ray.hitNode.getWorldNormal(localNormal);
		ray.intersectionNormal.normalize();
		ray.isNormal = vertexNormal;
		ray.isColor = vertexColor;

		Material m = ray.hitNode.dummyMaterial;
		if (m == null)
			throw new NullPointerException("Material Null");
		if (m.texture == null || ray.isTexture == false)
		{
			return;
		}
		Vector3 w = new Vector3(u, v, 1);
		Vector3 s = m.texture.mat.multiplyVector3(w);
		ray.u = s.x;
		ray.v = s.y;
	}

	public void draw(Camera c, ObjectNode node)
	{
		int v1, v2, v3;
		double x1, y1, z1, x2, y2, z2, x3, y3, z3;
		for (int i = 0; i < numTriangle; i++)
		{
			v1 = faceIndex[i].v1;
			v2 = faceIndex[i].v2;
			v3 = faceIndex[i].v3;
			x1 = vertex[v1].x;
			y1 = vertex[v1].y;
			z1 = vertex[v1].z;
			x2 = vertex[v2].x;
			y2 = vertex[v2].y;
			z2 = vertex[v2].z;
			x3 = vertex[v3].x;
			y3 = vertex[v3].y;
			z3 = vertex[v3].z;
			Line3 l1 = new Line3(x1, y1, z1, x2, y2, z2);
			l1.draw(c, node);
			Line3 l2 = new Line3(x2, y2, z2, x3, y3, z3);
			l2.draw(c, node);
			Line3 l3 = new Line3(x3, y3, z3, x1, y1, z1);
			l3.draw(c, node);
		}
	}

	public void print()
	{
		System.out.println("TriangleSet: numTriangle = " + numTriangle);
		for (int i = 0; i < numTriangle; i++)
		{
			System.out.println("  triangle[" + i + "] = (" + faceIndex[i].v1 + ","
					+ faceIndex[i].v2 + "," + faceIndex[i].v3 + ")");
		}
		System.out.println("TriangleSet: numVertex = " + numVertex);
		for (int i = 0; i < numVertex; i++)
		{
			System.out.println(" vertex[" + i + "]=(" + vertex[i].x + ","
					+ vertex[i].y + "," + vertex[i].z + ")");
		}
	}
}
