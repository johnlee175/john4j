package com.johnsoft.library.swing.o3d;


public class CameraList extends Object3d
{
	private static final long serialVersionUID = 1L;
	static int numCameras = 0;
	Camera camera;
	String name;
	CameraList next = null;

	public CameraList(Camera camera, String name)
	{
		numCameras++;
		this.camera = camera;
		this.name = name;
		this.next = null;
	}
}
