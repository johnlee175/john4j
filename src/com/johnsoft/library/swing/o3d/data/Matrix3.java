package com.johnsoft.library.swing.o3d.data;

import java.util.Arrays;

import com.johnsoft.library.swing.o3d.Object3d;

/**
 * a[0] a[1] a[2] 	<br/>
 * a[3] a[4] a[5] 	<br/>
 * a[6] a[7] a[8] 	<br/>
**/
public class Matrix3 extends Object3d 
{
	private static final long serialVersionUID = 1L;
	public double a[];
	
	public Matrix3()
	{ 
		this.a = new double[9];
		for (int i = 0; i < 9; i++) 
			this.a[i] = 0.0;
		this.a[0] = this.a[4] = this.a[8] = 1.0;
	}

	public Matrix3(double b[])
	{
		this.a = new double[9];
		for (int i = 0; i < 9; i++) 
			this.a[i] = b[i];
	}
	
	public Matrix3(double a0, double a1, double a2,
								double a3, double a4, double a5,
								double a6, double a7, double a8)
	{
		this.a = new double[9];
		this.a[0] = a0; this.a[1] = a1; this.a[2] = a2;
		this.a[3] = a3; this.a[4] = a4; this.a[5] = a5;
		this.a[6] = a6; this.a[7] = a7; this.a[8] = a8;
	}

	public Matrix3(Matrix3 t)
	{ 
		this(t.a);
	}
	
	public Matrix3 identity()
	{
		for (int i=0; i < 9; i++) 
			this.a[i] = 0.0;
		this.a[0] = this.a[4] = this.a[8] = 1.0;
		return this;
	}
	
	public void assign(Matrix3 v)
	{
		for (int i=0 ; i < 9 ; i++ ) 
			a[i] = v.a[i];
	}
	
	public Vector3 multiplyVector3(Vector3 v)
	{
		Vector3 w = new Vector3();
		w.x = a[0] * v.x + a[1] * v.y + a[2] * v.z;
		w.y = a[3] * v.x + a[4] * v.y + a[5] * v.z;
		w.z = a[6] * v.x + a[7] * v.y + a[8] * v.z;
		return w;
	}
	
	public Vertex3 multiplyVertex3(Vertex3 v)
	{
		Vertex3 w = new Vertex3();
		w.x = a[0] * v.x + a[1] * v.y + a[2] * v.z;
		w.y = a[3] * v.x + a[4] * v.y + a[5] * v.z;
		w.z = a[6] * v.x + a[7] * v.y + a[8] * v.z;
		return w;
	}
	
	public Matrix3 multiplyMatrix3(Matrix3 b)
	{
		Matrix3 c = new Matrix3();
		int i, j, k, p, q, r, s;
    for (k = p = q = 0; k < 3; k++, q += 3)
    {
    	 for (j = 0; j < 3; j++)
    	 {
    			r = q;
    			s = j;
        	c.a[p] = 0;
					for (i = 0; i < 3; i++, r++, s += 3)
					{
						c.a[p] += b.a[r] * a[s];
        	}
					p++;
    	 }
    }
		return c;
	}
	
	public void multiply(Matrix3 b)
	{
		 Matrix3 c = multiplyMatrix3(b);
		 for (int i = 0; i < 9; i++) 
			    this.a[i] = c.a[i];
	}
	
	public double determinant()
	{
	  return
		a[0]*a[4]*a[8] +
		a[1]*a[5]*a[6] +
		a[2]*a[7]*a[3] -
		a[6]*a[4]*a[2] -
		a[5]*a[7]*a[0] -
		a[8]*a[3]*a[1];
	}

	public Matrix3 inverse() throws ArithmeticException 
	{
			double t = this.determinant();
			if (Math.abs(t) < EPSILON) 
				 throw new ArithmeticException();
			t = 1.0 / t;
			double[] b = new double[9];
		 /*
			|   |a[4] a[5]|   |a[3] a[5]|   |a[3] a[4]| |
			|   |a[7] a[8]| - |a[6] a[8]|   |a[6] a[7]| |
			|                                           |
			|   |a[1] a[2]|   |a[0] a[2]|   |a[0] a[1]| |
			| - |a[7] a[8]|   |a[6] a[8]| - |a[6] a[7]| |
			|                                           |
			|   |a[1] a[2]|   |a[0] a[2]|   |a[0] a[1]| |
			|   |a[4] a[5]| - |a[3] a[5]|   |a[3] a[4]| |
			*/
      b[0] =  (a[4] * a[8] - a[5] * a[7]) * t;
    	b[1] = -(a[1] * a[8] - a[7] * a[2]) * t;
    	b[2] =  (a[1] * a[5] - a[2] * a[4]) * t;
    	b[3] = -(a[3] * a[8] - a[5] * a[6]) * t;
    	b[4] =  (a[0] * a[8] - a[2] * a[6]) * t;
    	b[5] = -(a[0] * a[5] - a[2] * a[3]) * t;
    	b[6] =  (a[3] * a[7] - a[4] * a[6]) * t;
    	b[7] = -(a[0] * a[7] - a[1] * a[6]) * t;
    	b[8] =  (a[0] * a[4] - a[1] * a[3]) * t;
			return new Matrix3(b);
	}

	public Matrix3 rotate(Vector3 v, double theta)
	{
		Vector3 w = new Vector3(v);
		w.normalize();
		double s = Math.sin(theta);
		double c = Math.cos(theta);
		double t = 1 - c;
		a[0] = t * w.x * w.x + c;
		a[1] = t * w.x * w.y + w.z * s;
		a[2] = t * w.z * w.x - w.y * s;
		a[3] = t * w.x * w.y - w.z * s;
		a[4] = t * w.y * w.y + c;
		a[5] = t * w.y * w.z + w.x * s;
		a[6] = t * w.z * w.x + w.y * s;
		a[7] = t * w.y * w.z - w.x * s;
		a[8] = t * w.z * w.z + c;
		return this;
	}
	
	public Matrix3 rotate(double dx, double dy, double dz, double theta)
	{
		Vector3 v = new Vector3(dx, dy, dz);
		return rotate(v, theta);
	}
	
	public void printMatrix()
	{
		System.out.println("|" + a[0] + " " + a[1] + " " + a[2] + "|");
		System.out.println("|" + a[3] + " " + a[4] + " " + a[5] + "|");
		System.out.println("|" + a[6] + " " + a[7] + " " + a[8] + "|");
	}

	@Override
	public String toString()
	{
		return super.toString() + "; " + "[a=" + Arrays.toString(a) + "]";
	}
	
}
