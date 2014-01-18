package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Color3;
import com.johnsoft.library.swing.o3d.data.Index3;
import com.johnsoft.library.swing.o3d.data.Matrix3;
import com.johnsoft.library.swing.o3d.data.Matrix4;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vector4;
import com.johnsoft.library.swing.o3d.data.Vertex2;
import com.johnsoft.library.swing.o3d.data.Vertex3;
import com.johnsoft.library.swing.o3d.data.Vertex4;

public class ObjectNode extends Object3d
{
	private static final long serialVersionUID = 1L;
	String name = null;
	Matrix4 mat = null;
	Matrix4 acm = null;
	Matrix4 inv = null;
	Material material = null;
	Material dummyMaterial = null;
	Renderable element = null;
	ObjectNode next = null;
	ObjectNode child = null;
	ObjectNode parent = null;

	ObjectNode(Renderable element, String name)
	{
		this.element = element;
		this.name = name;
		this.mat = new Matrix4();
		this.acm = null;
		this.inv = null;
		this.material = null;
		this.dummyMaterial = null;
		this.next = null;
		this.parent = null;
		this.child = null;
	}

	private ObjectNode(ObjectNode node)
	{
		this.element = node.duplicate(node.element);
		this.name = node.name;
		this.mat = new Matrix4(node.mat);
		this.acm = null;
		this.inv = null;
		if (node.material == null)
			this.material = null;
		else
			this.material = new Material(node.material);
		this.dummyMaterial = null;
		if (node.next == null)
			this.next = null;
		else
			this.next = new ObjectNode(node.next);
		this.parent = node.parent;
		if (node.child == null)
			this.child = null;
		else
			this.child = new ObjectNode(node.child);
	}

	ObjectNode(ObjectNode node, String name)
	{
		if (node == null)
			throw new NullPointerException();
		this.element = node.duplicate(node.element);
		this.name = name;
		this.mat = new Matrix4(node.mat);
		this.acm = null;
		this.inv = null;
		if (node.material == null)
			this.material = null;
		else
			this.material = new Material(node.material);
		this.dummyMaterial = null;
		this.next = null;
		this.parent = null;
		if (node.child == null)
			this.child = null;
		else
			this.child = new ObjectNode(node.child);
	}

	private boolean isInvalidNodeType(Object element)
	{
		if (element instanceof Material || element instanceof Texture
				|| element instanceof Camera || element instanceof CameraList
				|| element instanceof ObjectNode || element instanceof ObjectWorld
				|| element instanceof Ray || element instanceof Matrix4
				|| element instanceof Matrix3 || element instanceof Vector4
				|| element instanceof Vector3 || element instanceof Vertex3
				|| element instanceof Vertex4 || element instanceof Vertex2
				|| element instanceof Index3 || element instanceof Color3)
		{
			String s = element.toString();
			int index = s.indexOf((int) '@');
			String ss = s.substring(0, index);
			System.out.println(ss);
			return true;
		}
		return false;
	}

	public boolean isShapeNode()
	{
		if (element instanceof Sphere || element instanceof TriangleSet
				|| element instanceof Box || element instanceof Cone
				|| element instanceof Cylinder)
			return true;
		return false;
	}

	public boolean isLightNode()
	{
		if (element instanceof PointLight || element instanceof DirectionalLight)
			return true;
		return false;
	}

	public Renderable duplicate(Renderable element)
	{
		if (element instanceof Sphere)
		{
			Sphere p = (Sphere) element;
			Sphere newP = new Sphere(p.r);
			return newP;
		} else if (element instanceof Box)
		{
			Box p = (Box) element;
			Box newP = new Box(p.xsize, p.ysize, p.zsize);
			return newP;
		} else if (element instanceof Cone)
		{
			Cone p = (Cone) element;
			Cone newP = new Cone(p.r, p.h);
			return newP;
		} else if (element instanceof Cylinder)
		{
			Cylinder p = (Cylinder) element;
			Cylinder newP = new Cylinder(p.r, p.h);
			return newP;
		} else if (element instanceof TriangleSet)
		{
			TriangleSet p = (TriangleSet) element;
			TriangleSet newP = new TriangleSet(p.numTriangle, p.faceIndex,
					p.numVertex, p.vertex, p.colorIndex, p.numColor, p.color,
					p.normalIndex, p.numNormal, p.normal, p.textureIndex, p.numTexture,
					p.texture);
			return newP;
		} else if (element instanceof Group)
		{
			// Group p = (Group)element;
			Group newP = new Group();
			return newP;
		} else if (element instanceof PointLight)
		{
			PointLight p = (PointLight) element;
			PointLight newP = new PointLight(p);
			return newP;
		} else if (element instanceof DirectionalLight)
		{
			DirectionalLight p = (DirectionalLight) element;
			DirectionalLight newP = new DirectionalLight(p);
			return newP;
		}
		return null;
	}

	public ObjectNode addChild(Renderable element, String name)
	{
		if (isInvalidNodeType(element))
			throw new InternalError("");
		ObjectNode t = null;
		if (this.child == null)
		{
			this.child = t = new ObjectNode(element, name);
		} else
			t = this.child.addBrother(element, name);
		t.parent = this;
		return t;
	}

	public ObjectNode addChild(ObjectNode node)
	{
		ObjectNode t;
		if (node == null)
			throw new NullPointerException();
		if (this.child == null)
			this.child = t = node;
		else
			t = this.child.addBrother(node);
		t.parent = this;
		return t;
	}

	public ObjectNode getParent(ObjectNode node)
	{
		return node.parent;
	}

	public ObjectNode addBrother(Renderable element, String name)
	{
		ObjectNode t = null;
		if (this.next == null)
		{
			this.next = t = new ObjectNode(element, name);
		} else
		{
			ObjectNode q;
			ObjectNode p = this.next;
			do
			{
				q = p;
				p = p.next;
			} while (p != null);
			t = q.next = new ObjectNode(element, name);
		}
		t.parent = this.parent;
		return t;
	}

	public ObjectNode addBrother(ObjectNode node)
	{
		ObjectNode t = null;
		if (node == null)
			throw new NullPointerException();
		if (this.next == null)
		{
			this.next = t = node;
		} else
		{
			ObjectNode q;
			ObjectNode p = this.next;
			do
			{
				q = p;
				p = p.next;
			} while (p != null);
			t = q.next = node;
		}
		t.parent = this.parent;
		return t;
	}

	public String getObjectNodeName()
	{
		return name;
	}

	public void reset()
	{
		mat.identity();
	}

	public void translate(Vertex3 v)
	{
		mat.a[3] += v.x;
		mat.a[7] += v.y;
		mat.a[11] += v.z;
	}

	public void translate(double x, double y, double z)
	{
		mat.a[3] += x;
		mat.a[7] += y;
		mat.a[11] += z;
	}

	public void scale(Vector3 v)
	{
		mat.a[0] *= v.x;
		mat.a[5] *= v.y;
		mat.a[10] *= v.z;
	}

	public void scale(double x, double y, double z)
	{
		mat.a[0] *= x;
		mat.a[5] *= y;
		mat.a[10] *= z;
	}

	public void rotate(Vector3 axis, double theta)
	{
		Matrix4 m = new Matrix4();
		Matrix4 m2 = m.rotate(axis, theta);
		mat.multiply(m2);
	}

	public void rotate(double dx, double dy, double dz, double theta)
	{
		Vector3 v = new Vector3(dx, dy, dz);
		rotate(v, theta);
	}

	public void rotate(Vector4 v)
	{
		rotate(v.x, v.y, v.z, v.w);
	}

	public void setMaterial(Material material)
	{
		if (material == null)
			throw new NullPointerException();
		this.material = material;
		this.dummyMaterial = material;
	}

	public Material getOriginalMaterial()
	{
		return material;
	}

	public Material getMaterial()
	{
		return dummyMaterial;
	}

	public Vertex3 getWorldPosition(Vertex3 v)
	{
		if (v == null)
			throw new NullPointerException();
		Vertex4 p = new Vertex4(v.x, v.y, v.z, 1.0);
		Vertex4 q = this.acm.multiplyVertex4(p);
		Vertex3 r = new Vertex3(q.x, q.y, q.z);
		return r;
	}

	public Vertex3 getWorldPosition(double x, double y, double z)
	{
		Vertex3 v = new Vertex3(x, y, z);
		return getWorldPosition(v);
	}

	public Vector3 getWorldVector(Vector3 v)
	{
		if (v == null)
			throw new NullPointerException();
		if (this.acm == null)
			throw new NullPointerException();
		Matrix3 m = new Matrix3(acm.a[0], acm.a[1], acm.a[2], acm.a[4], acm.a[5],
				acm.a[6], acm.a[8], acm.a[9], acm.a[10]);
		return m.multiplyVector3(v);
	}

	public Vector3 getWorldVector(double x, double y, double z)
	{
		Vector3 v = new Vector3(x, y, z);
		return getWorldVector(v);
	}

	public Vector3 getWorldNormal(Vector3 v)
	{
		if (v == null)
			throw new NullPointerException();
		Matrix4 m = null;
		try
		{
			m = acm.transpose().inverse();
		} catch (ArithmeticException e)
		{
		}
		if (m == null)
			return null;
		Vector4 p = m.multiplyVector4(new Vector4(v.x, v.y, v.z, 1));
		Vector3 q = new Vector3(p.x, p.y, p.z);
		return q;
	}

	public Vector3 getWorldNormal(double x, double y, double z)
	{
		Vector3 v = new Vector3(x, y, z);
		return getWorldNormal(v);
	}

	public Vertex3 getLocalPosition(Vertex3 v)
	{
		if (v == null)
			throw new NullPointerException();
		if (this.inv == null)
			throw new NullPointerException();
		Vertex4 p = new Vertex4(v.x, v.y, v.z, 1.0);
		Vertex4 q = this.inv.multiplyVertex4(p);
		Vertex3 r = new Vertex3(q.x, q.y, q.z);
		return r;
	}

	public Vertex3 getLocalPosition(double x, double y, double z)
	{
		Vertex3 v = new Vertex3(x, y, z);
		return getLocalPosition(v);
	}

	public Vector3 getLocalVector(Vector3 v)
	{
		if (v == null)
			throw new NullPointerException();
		if (this.inv == null)
			throw new NullPointerException();
		Matrix3 m = new Matrix3(inv.a[0], inv.a[1], inv.a[2], inv.a[4], inv.a[5],
				inv.a[6], inv.a[8], inv.a[9], inv.a[10]);
		Vector3 r = m.multiplyVector3(v);
		return r;
	}

	public Vector3 getLocalVector(double x, double y, double z)
	{
		Vector3 v = new Vector3(x, y, z);
		return getLocalVector(v);
	}
}