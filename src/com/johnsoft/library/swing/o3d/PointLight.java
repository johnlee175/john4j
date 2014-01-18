package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Color3;
import com.johnsoft.library.swing.o3d.data.Vertex3;

public class PointLight extends Object3d implements Renderable 
{
	private static final long serialVersionUID = 1L;
	Vertex3 location; 
	double intensity = 1.0;
	Color3 color; 
	// 1/(attenuation.x+r*attenuation.y+r*r*attenuation.z)
	Vertex3 attenuation; 
	boolean on = true; 
	double radius = LARGE; 
	double ambientIntensity = 0.0; 

	public PointLight(){
		location = new Vertex3(0,0,0);
		intensity = 1.0;
		color = new Color3(1,1,1);
		attenuation = new Vertex3(1,0,0);
		on = true;
		radius = LARGE;
		ambientIntensity = 0.0;
	}
	public PointLight(PointLight p){
		location = new Vertex3(p.location);
		intensity = p.intensity;
		color = new Color3(p.color);
		attenuation = new Vertex3(p.attenuation);
		on = p.on;
		radius = p.radius;
		ambientIntensity = p.ambientIntensity;
	}
	public PointLight(Vertex3 v){
		this();
		this.location = v;
	}
	public PointLight(double x, double y, double z){
		this(new Vertex3(x,y,z));
	}

	public void setLocation(Vertex3 v){
		location.x = v.x; location.y = v.y; location.z = v.z;
	}
	public void setLocation(double x, double y, double z){
		location.x = x; location.y = y; location.z = z;
	}
	public Vertex3 getLocation(){ return location; }

	public void setIntensity(double intensity){
		if (intensity <= 0)
			throw new InternalError("");
		this.intensity = intensity;
	}
	public double getIntensity(){ return intensity;	}

	public void setAmbientIntensity(double ambientIntensity){
		if (ambientIntensity <= 0)
			throw new InternalError("");
		this.ambientIntensity = ambientIntensity;
	}
	public double getAmbientIntensity(){ return ambientIntensity; }

	public void setColor(Color3 color){
		if (color.isInvalidColor())
			throw new InternalError("");
		this.color = color;
	}
	public void setColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		color.r = r; color.g = g; color.b = b;
	}
	public Color3 getColor(){ return color;	}

	public void setAttenuation(Vertex3 attenuation){
		if (Math.abs(attenuation.x) < EPSILON &&
		    Math.abs(attenuation.y) < EPSILON &&
		    Math.abs(attenuation.z) < EPSILON)
			throw new InternalError("");
		this.attenuation = attenuation;
	}
	public void setAttenuation(double x, double y, double z){
		if (Math.abs(x) < EPSILON &&
		    Math.abs(y) < EPSILON &&
		    Math.abs(z) < EPSILON)
			throw new InternalError("");
		attenuation.x = x; attenuation.y = y; attenuation.z = z;
	}
	public Vertex3 getAttenuation(){ return attenuation; }

	public void setRadius(double radius){ 
		if (radius <= 0)
			throw new InternalError("");
		this.radius = radius; 
	}
	public double gteRadius(){ return radius; }

	public void setSwitch(boolean on){ this.on = on; }
	public boolean getSwitch(){ return on; }

	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){}
}