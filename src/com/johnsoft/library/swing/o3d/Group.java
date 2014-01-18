package com.johnsoft.library.swing.o3d;

public class Group extends Object3d implements Renderable
{
	private static final long serialVersionUID = 1L;
	static int numGroups = 0;

	public Group()
	{
		numGroups++;
	}

	public int getGroupNumber()
	{
		return numGroups;
	}

	public void getNearerIntersection(Ray ray, ObjectNode node)
	{
	}

	public void setNearestIntersection(Ray ray)
	{
	}

	public void draw(Camera c, ObjectNode node)
	{
	}
}
