package com.johnsoft.library.swing.o3d;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.MemoryImageSource;

import com.johnsoft.library.swing.o3d.data.Color3;
import com.johnsoft.library.swing.o3d.data.Matrix4;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex3;

public class ObjectWorld extends Object3d {

	private static final long serialVersionUID = 1L;
	Camera activeCamera = null;
	CameraList header = null; 
	CameraList tailer = null; 
	ObjectNode root = null;
	int[] pixel = null; 
	MemoryImageSource mis = null; 
	Image img = null;
	Color3 background = new Color3(0,0,0);

	public ObjectNode createUniverse(){
		Group g = new Group();
		ObjectNode node = new ObjectNode(g,"Universe");
		root = node;
		Material material = new Material();
		root.setMaterial(material);
		return node;
	}
	public ObjectNode getRootNode() { return root;}

	public void setBackgroundColor(Color3 c){
		if (c.isInvalidColor())
			throw new InternalError("");
		this.background = c;
	}
	public void setBackgroundColor(double r, double g, double b){
		if (r < 0 || g < 0 || b < 0)
			throw new InternalError("");
		this.background.r = r;
		this.background.g = g;
		this.background.b = b;
	}
	public Color3 getBackgroundColor(){
		return background;
	}

	public void accumulateWorldMatrix(){ 
		ObjectNode p = root;
		if (p == null) return;
		int level = 0;
		p.acm = new Matrix4(p.mat);
		try {
			p.inv = p.mat.inverse();
		}
		catch (ArithmeticException e){}
		level++;
		traverseMatrix4(p.child,level,p.material);
	}

	public void traverseMatrix4(ObjectNode node, int level, Material m)
		throws ArithmeticException {
		if (node == null) return;
		if (node.material == null) node.dummyMaterial = m;
		if (node.parent != null)
			node.acm = node.mat.multiplyMatrix4(node.parent.acm);
		try {
			node.inv = node.acm.inverse();
		}
		catch (ArithmeticException e){}
		if (node.child != null){
			level++;
			traverseMatrix4(node.child, level, node.dummyMaterial);
		}
		if (node.next != null) 
			traverseMatrix4(node.next, level, 
				node.parent.dummyMaterial);
	}

	public void drawWorld(){
		if (root == null) return;
		Camera c = activeCamera;
		if (c == null){ 
			c = activeCamera = new Camera();
			this.addCamera(c,"DEFAULT_CAMERA");
		}
		accumulateWorldMatrix();
		draw(c,root.child);
	}

	public void draw(Camera c, ObjectNode node){
		if (node == null) return;
		if (node.isShapeNode())	node.element.draw(c,node);
		if (node.next != null) draw(c, node.next);
		if (node.child != null)	draw(c,node.child);
	}

	public Camera setupRaytrace(){
		if (root == null) return null;
		Camera c = activeCamera;
		if (c == null){ 
			c = activeCamera = new Camera();
			this.addCamera(c,"DEFAULT_CAMERA");
		}
		pixel = new int[c.resHorizontal * c.resVertical];
		accumulateWorldMatrix();
		return c;
	}

	public int[] getPixel(){ return pixel; }
	public void setPixel(int index, int value){ pixel[index] = value;}

	public void startRaytrace(){
		Camera c = setupRaytrace();
		if (c == null) return;

		Vertex3 camOrigin = new Vertex3(0,0,0);
		Vertex3 origin = c.getWorldPosition(camOrigin);
		double currentX = c.screenMinX;
		double currentY = c.screenMaxY;
		Vertex3 camScreen = new Vertex3(currentX,currentY,-c.focalLength);
		Vertex3 screen = c.getWorldPosition(camScreen);
		Vector3 direction = new Vector3();
		int r,g,b;

		for (int j = 0; j < c.resVertical ; j++ ){

			for (int i = 0; i < c.resHorizontal ; i++ ){

				direction.assign(screen);
				direction.subtract(origin);
				direction.normalize();
				Ray ray = new Ray(screen,direction,i,j);
				ray.objectWorld = this;
				ray.shoot(root.child);

				int color = 0xff000000;
				ray.color.clamp();
				r = (int) ((double)COLORLEVEL*ray.color.r);
				g = (int) ((double)COLORLEVEL*ray.color.g);
				b = (int) ((double)COLORLEVEL*ray.color.b);
				color = color |(r << 16)|(g << 8)|b;
				pixel[j*c.resHorizontal+i] = color;

				currentX += c.deltaHorizontal;
				camScreen.x = currentX;
				camScreen.y = currentY;
				camScreen.z = -c.focalLength;
				screen = c.getWorldPosition(camScreen);
			}
			currentX = c.screenMinX;
			currentY -= c.deltaVertical;
			camScreen.x = currentX;
			camScreen.y = currentY;
			camScreen.z = -c.focalLength;
			screen = c.getWorldPosition(camScreen);
		}
	}

	public Image getRaytracedImage(Component component){
		if (activeCamera == null) return null;
		int width = activeCamera.resHorizontal;
		int height = activeCamera.resVertical;
		MemoryImageSource mis = new MemoryImageSource(
			width,height,pixel,0,width);
		return component.createImage(mis);
	}

	public void addCamera(Camera o, String s){
		if (o == null) throw new NullPointerException();
		CameraList newElement = new CameraList(o,s);
		if (tailer == null){
			tailer = newElement;
			header = newElement;
		}
		else {
			tailer.next = newElement;
			tailer = newElement;
		}
		activeCamera = o;
	}

	Camera findCamera(Camera cam){
		CameraList p = header;
		while (p != null){
			if (p.camera.equals(cam)) return p.camera;
			p = p.next;
		}
		return null;
	}

	private boolean isDefinedCamera(Camera cam){
		CameraList p = header;
		while (p != null){
			if (p.camera.equals(cam)) return true;
			p = p.next;
		}
		return false;
	}

	public void setCameraActive(Camera cam){
		if (isDefinedCamera(cam)) activeCamera = cam;
	}

	public void printWorld(){
		ObjectNode p = root;
		if (p == null) return;
		int level = 0;
		System.out.println("Node: "+p.element+" level = "+level);
		p.acm.printMatrix();
		level++;
		printObjectNode(p.child, level);
	}

	public void printObjectNode(ObjectNode node, int level){
		if (node == null) return;
		System.out.println("**************************************");
		System.out.println("Node: "+node.name+
			" parent = "+node.parent.name+" level = "+level);
		System.out.println("acm:");
		node.acm.printMatrix();
		System.out.println("inv:");
		node.inv.printMatrix();
		node.dummyMaterial.print();
		if (node.next != null) 
			printObjectNode(node.next, level);
		if (node.child != null){
			level++;
			printObjectNode(node.child, level);
		}
	}
}