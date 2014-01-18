package com.johnsoft.library.swing.o3d;

public interface Renderable
{
	public void draw(Camera c, ObjectNode node);

	public void getNearerIntersection(Ray ray, ObjectNode node);

	public void setNearestIntersection(Ray ray);
}
