package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Matrix4;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex3;
import com.johnsoft.library.swing.o3d.data.Vertex4;


public class Camera extends Object3d
{
	private static final long serialVersionUID = 1L;
	JohnCanvas m;
	Matrix4 viewmat;
	Matrix4 cammat;
	protected double screenMaxX, screenMaxY;
	protected double screenMinX, screenMinY;
	protected double deltaHorizontal;
	protected double deltaVertical;
	Vertex3 eyePosition;
	Vector3 upDirection;
	Vector3 toDirection;
	Vector3 rightDirection;
	boolean parallel = false;
	double focalLength = 1.0;
	double fieldOfView = Math.PI / 2;
	double aspectRatio = 4.0 / 3.0;
	int resHorizontal = 640;
	int resVertical = 480;
	final static int LEFT_VIEWVOLUME = 0x1;
	final static int RIGHT_VIEWVOLUME = 0x2;
	final static int BOTTOM_VIEWVOLUME = 0x4;
	final static int TOP_VIEWVOLUME = 0x8;

	public Camera(JohnCanvas m)
	{
		this.m = m;
		eyePosition = new Vertex3(0, 0, 0);
		upDirection = new Vector3(0, 1, 0);
		toDirection = new Vector3(0, 0, 1);
		rightDirection = new Vector3(1, 0, 0);
		cammat = new Matrix4();
		parallel = false;
		focalLength = 1.0;
		fieldOfView = Math.PI / 2.0;
		aspectRatio = 4.0 / 3.0;
		resHorizontal = 640;
		resVertical = 480;
		setScreen();
		setViewMatrix();
	}

	public Camera()
	{
		this(null);
	}

	public void reset()
	{
		eyePosition.x = eyePosition.y = eyePosition.z = 0;
		upDirection.x = upDirection.z = 0;
		upDirection.y = 1;
		toDirection.x = toDirection.y = 0;
		toDirection.z = 1;
		rightDirection.x = 1;
		rightDirection.y = rightDirection.z = 0;
		cammat.identity();
		setViewMatrix();
	}

	private Matrix4 getCurrentPlainViewMatrix()
	{
		Matrix4 m = new Matrix4();
		m.a[0] = rightDirection.x;
		m.a[1] = upDirection.x;
		m.a[2] = toDirection.x;
		m.a[4] = rightDirection.y;
		m.a[5] = upDirection.y;
		m.a[6] = toDirection.y;
		m.a[8] = rightDirection.z;
		m.a[9] = upDirection.z;
		m.a[10] = toDirection.z;
		m.a[3] = eyePosition.x;
		m.a[7] = eyePosition.y;
		m.a[11] = eyePosition.z;
		cammat.assign(m);
		return m;
	}

	private void setViewMatrixInverse()
	{
		try
		{
			viewmat = cammat.inverse();
		} catch (ArithmeticException e)
		{
		}
	}

	private void setViewMatrix()
	{
		/* Matrix4 m = */getCurrentPlainViewMatrix();
		setViewMatrixInverse();
	}

	private void setScreenDetail()
	{
		screenMaxX = focalLength * Math.tan(fieldOfView / 2.0);
		screenMaxY = screenMaxX / aspectRatio;
		screenMinX = -screenMaxX;
		screenMinY = -screenMaxY;
		deltaHorizontal = (screenMaxX - screenMinX) / (resHorizontal - 1);
		deltaVertical = (screenMaxY - screenMinY) / (resVertical - 1);
	}

	private void setScreen()
	{
		aspectRatio = (double) resHorizontal / (double) resVertical;
		setScreenDetail();
	}

	public double getScreenX()
	{
		return (screenMaxX - screenMinX);
	}

	public double getScreenY()
	{
		return (screenMaxY - screenMinY);
	}

	public void setScreenXY(double xsize, double ysize)
	{
		if (xsize <= 0 || ysize <= 0)
			throw new InternalError("");
		screenMaxX = xsize / 2;
		screenMinX = -xsize / 2;
		screenMaxY = ysize / 2;
		screenMinY = -ysize / 2;
		fieldOfView = 2 * Math.atan2(screenMaxX, focalLength);
		aspectRatio = xsize / ysize;
		deltaHorizontal = (screenMaxX - screenMinX) / (resHorizontal - 1);
		deltaVertical = (screenMaxY - screenMinY) / (resVertical - 1);
	}

	public int areaCode(Vertex3 v)
	{
		double x = v.x;
		double y = v.y;
		double z = v.z;
		int code = 0;
		if (x * focalLength < -screenMinX * z)
			code |= LEFT_VIEWVOLUME;
		else if (x * focalLength > -screenMaxX * z)
			code |= RIGHT_VIEWVOLUME;
		if (y * focalLength < -screenMinY * z)
			code |= BOTTOM_VIEWVOLUME;
		else if (y * focalLength > -screenMaxY * z)
			code |= TOP_VIEWVOLUME;
		return code;
	}

	public Vertex3 getWorldPosition(Vertex3 v)
	{
		Vertex4 v1 = new Vertex4(v.x, v.y, v.z, 1);
		Vertex4 v2 = cammat.multiplyVertex4(v1);
		Vertex3 p = new Vertex3(v2.x, v2.y, v2.z);
		return p;
	}

	public Vertex3 getCameraPosition(Vertex3 v)
	{
		Vertex4 v1 = new Vertex4(v.x, v.y, v.z, 1);
		Vertex4 v2 = viewmat.multiplyVertex4(v1);
		Vertex3 p = new Vertex3(v2.x, v2.y, v2.z);
		return p;
	}

	public void setEyePosition(Vertex3 eyePosition)
	{
		this.eyePosition.x = eyePosition.x;
		this.eyePosition.y = eyePosition.y;
		this.eyePosition.z = eyePosition.z;
		setViewMatrix();
	}

	public void setEyePosition(double x, double y, double z)
	{
		eyePosition.x = x;
		eyePosition.y = y;
		eyePosition.z = z;
		setViewMatrix();
	}

	public Vertex3 getEyePosition()
	{
		return eyePosition;
	}

	public void setScreenResolution(int Horizontal, int Vertical)
	{
		if (Horizontal <= 0 || Vertical <= 0)
			throw new InternalError("");
		resHorizontal = Horizontal;
		resVertical = Vertical;
		setScreen();
	}

	public int getScreenWidth()
	{
		return resHorizontal;
	}

	public int getScreenHeight()
	{
		return resVertical;
	}

	public void setAspectRatio(double ratio)
	{
		if (ratio <= 0)
			throw new InternalError("");
		aspectRatio = ratio;
		setScreen();
	}

	public double getAspectRatio()
	{
		return aspectRatio;
	}

	public void setFieldOfView(double fov)
	{
		if (fov <= 0 || fov >= Math.PI)
			throw new InternalError("");
		fieldOfView = fov;
		setScreenDetail();
	}

	public double getFieldOfView()
	{
		return fieldOfView;
	}

	public void setFocalLength(double focalLength)
	{
		if (focalLength <= 0)
			throw new InternalError("");
		this.focalLength = focalLength;
		setScreenDetail();
	}

	public double getFocalLength()
	{
		return focalLength;
	}

	public void setParallel(boolean on)
	{
		parallel = on;
	}

	public boolean getParallel()
	{
		return parallel;
	}

	public void rotate(Vector3 axis, double theta)
	{
		Matrix4 m = new Matrix4();
		Matrix4 m2 = m.rotate(axis, theta);
		cammat.multiply(m2);
		rightDirection.x = cammat.a[0];
		rightDirection.y = cammat.a[4];
		rightDirection.z = cammat.a[8];
		upDirection.x = cammat.a[1];
		upDirection.y = cammat.a[5];
		upDirection.z = cammat.a[9];
		toDirection.x = cammat.a[2];
		toDirection.y = cammat.a[6];
		toDirection.z = cammat.a[10];
		eyePosition.x = cammat.a[3];
		eyePosition.y = cammat.a[7];
		eyePosition.z = cammat.a[11];
		setViewMatrixInverse();
	}

	public void rotate(double x, double y, double z, double theta)
	{
		Vector3 v = new Vector3(x, y, z);
		rotate(v, theta);
	}

	public void drawLine(double x1, double y1, double z1, double x2, double y2,
			double z2)
	{
		double xs1, ys1, xs2, ys2;
		if (!parallel)
		{
			xs1 = -x1 / z1 * focalLength;
			ys1 = -y1 / z1 * focalLength;
			xs2 = -x2 / z2 * focalLength;
			ys2 = -y2 / z2 * focalLength;
		} else
		{
			xs1 = x1;
			ys1 = y1;
			xs2 = x2;
			ys2 = y2;
		}
		m.drawLine(xs1, ys1, xs2, ys2);
	}
}
