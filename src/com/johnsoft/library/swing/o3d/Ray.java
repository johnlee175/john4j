package com.johnsoft.library.swing.o3d;

import com.johnsoft.library.swing.o3d.data.Color3;
import com.johnsoft.library.swing.o3d.data.Vector3;
import com.johnsoft.library.swing.o3d.data.Vertex3;

public class Ray extends Object3d
{
	private static final long serialVersionUID = 1L;
	double t = HUGEREAL;
	ObjectNode hitNode;
	int hitIndex;
	Vertex3 intersection;
	Vertex3 intersectionLocal;
	Vector3 intersectionNormal;
	Vector3 intersectionU;
	Vector3 intersectionV;
	Color3 intersectionColor;
	double u, v;
	double opacity;
	boolean isNormal = false;
	boolean isTexture = false;
	boolean isColor = false;
	int level = 0;
	boolean lightRay = false;
	Ray reflectedRay = null;
	Ray transparentRay = null;
	ObjectWorld objectWorld = null;
	double refraction = 1.0;
	Color3 color = null;
	Vertex3 origin = null;
	Vector3 direction = null;
	int currentImageX, currentImageY;
	private final static int MAXRAYLEVEL = 4;

	public Ray()
	{
		this(null, null, 0, 0);
	}

	public Ray(Vertex3 origin, Vector3 direction, int i, int j)
	{
		this.t = HUGEREAL;
		this.hitNode = null;
		this.hitIndex = -1;
		this.intersection = null;
		this.intersectionLocal = null;
		this.intersectionNormal = null;
		this.level = 0;
		this.lightRay = false;
		this.reflectedRay = null;
		this.transparentRay = null;
		this.objectWorld = null;
		this.origin = new Vertex3(origin);
		this.direction = new Vector3(direction);
		this.color = new Color3(0, 0, 0);
		this.u = this.v = 0;
		this.opacity = 1.0;
		this.refraction = 1.0;
		this.currentImageX = i;
		this.currentImageY = j;
	}

	public Ray(Ray ray, Vector3 direction)
	{
		this.t = HUGEREAL;
		this.hitNode = null;
		this.hitIndex = -1;
		this.intersection = null;
		this.intersectionLocal = null;
		this.intersectionNormal = null;
		this.level = ray.level + 1;
		this.lightRay = false;
		this.reflectedRay = null;
		this.transparentRay = null;
		this.objectWorld = ray.objectWorld;
		this.origin = new Vertex3(ray.intersection);
		this.origin.x += direction.x * LITTLE;
		this.origin.y += direction.y * LITTLE;
	  this.origin.z += direction.z * LITTLE;
		this.direction = direction;
		this.color = new Color3(0, 0, 0);
		this.u = 0;
		this.v = 0;
		this.opacity = 1.0;
		this.refraction = 1.0;
		this.currentImageX = ray.currentImageX;
		this.currentImageY = ray.currentImageY;
	}

	public void getNearerIntersection(ObjectNode node)
	{
		node.element.getNearerIntersection(this, node);
	}

	public void setNearestIntersection()
	{
		Renderable element = this.hitNode.element;
		element.setNearestIntersection(this);
	}

	// I = M(emisssive) + (1-M(transparency))*Im +
	// M(transparency)*It
	//
	// It = M(refraction)* (trasnmitted ray color)
	//
	// Im = SUM[ Li(on)*attenuation(i)*Irgb(i)*(Ai+Di+Si)] +
	// M(reflection) * (reflected ray color)
	//
	// Irgb = color of i-th light source
	// Ai = Iambient(i)*M(ambient)*M(diffuse)
	// Ai = Iambient(i)*M(ambient)
	// Di = Ii * M(diffuse) * Lambertian(i)
	// Si = Ii * M(specular) * TorranceSparrow(i)
	// Ii = intensity of i-th light source
	final static double SHADOW_ATTENUATION = 0.2;

	public void shading()
	{
		Material m = this.hitNode.dummyMaterial;
		if (m == null)
			throw new NullPointerException();
		Color3 c = this.color;
		c.add(m.emissiveColor);
		if (Math.abs(m.transparency - 1) > EPSILON)
		{
			Color3 cm = materialIntensity();
			if (m.texture == null)
				cm.scale(1 - m.transparency);
			else
				cm.scale(this.opacity);
			c.add(cm);
		}
		if (Math.abs(m.transparency) > EPSILON
				|| (m.texture != null && Math.abs(this.opacity - 1) > EPSILON))
		{
			Color3 ct = transmittedIntensity();
			if (m.texture == null)
				ct.scale(m.transparency);
			else
				ct.scale(1 - this.opacity);
			c.add(ct);
		}
	}

	public double Lambertian(Vector3 light, Vector3 normal)
	{
		double t = Util3d.innerProduct(light, normal);
		if (t < 0)
			t = 0;
		return t;
	}

	public double TorranceSparrow(Vector3 light, Vector3 normal, Vector3 eye)
	{
		Vector3 h = new Vector3(0.5 * (light.x - eye.x), 0.5 * (light.y - eye.y),
				0.5 * (light.z - eye.z));
		h.normalize();
		double s = h.innerProduct(normal);
		if (s < 0)
			return 0;
		Material m = this.hitNode.dummyMaterial;
		if (m.shininess <= 0)
			return s;
		double t = Math.pow(s, m.shininess);
		return t;
	}

	public double getTexelOpacity(int i, int j)
	{
		Material m = this.hitNode.dummyMaterial;
		Texture t = m.texture;
		int value = t.texel[(t.height - j - 1) * t.width + i];
		int alpha = (value >> 24) & 0xff;
		double opacity = (double) alpha / COLORLEVEL;
		return opacity;
	}

	public Color3 getTexelColor(int i, int j)
	{
		Material m = this.hitNode.dummyMaterial;
		Texture t = m.texture;
//		int index = (t.height - j - 1) * t.width + i;
		int value = t.texel[(t.height - j - 1) * t.width + i];
		int rint = (value >> 16) & 0xff;
		int gint = (value >> 8) & 0xff;
		int bint = (value) & 0xff;
		Color3 c = new Color3((double) rint / COLORLEVEL, (double) gint
				/ COLORLEVEL, (double) bint / COLORLEVEL);
		return c;
	}

	public Color3 textureColor()
	{
//		Color3 c = new Color3(0, 0, 0);
		Material m = this.hitNode.dummyMaterial;
		double u = this.u;
		double v = this.v;
		double ss = u * (1 - u);
		double tt = v * (1 - v);

		if (ss < 0.0 || tt < 0.0)
		{
			// ray.u or ray.v is out of range [0,1]
			if (!m.texture.repeatU)
			{
				if (u < 0.0)
					u = 0.0;
				else if (u > 1.0)
					u = 1.0;
			} else if (ss < 0.0)
				u -= Math.floor(u);

			if (!m.texture.repeatV)
			{
				if (v < 0.0)
					v = 0.0;
				else if (v > 1.0)
					v = 1.0;
			} else if (tt < 0.0)
				v -= Math.floor(v);
		}

		if (m.texture.linearFilter)
		{

			int iu = (int) Math.floor(u * m.texture.width);
			double ru = u * m.texture.width - iu;
			int iuu = iu + 1;
			iu %= m.texture.width;
			iuu %= m.texture.width;

			int iv = (int) Math.floor(v * m.texture.height);
			double rv = v * m.texture.height - iv;
			int ivv = iv + 1;
			iv %= m.texture.height;
			ivv %= m.texture.height;

			Color3 value00 = getTexelColor(iu, iv);
			Color3 value10 = getTexelColor(iuu, iv);
			Color3 value01 = getTexelColor(iu, ivv);
			Color3 value11 = getTexelColor(iuu, ivv);
			value10.subtract(value00);
			value11.subtract(value01);
			value10.scale(ru);
			value11.scale(ru);
			value00.add(value10); // bottom
			value01.add(value11); // top
			value01.subtract(value00);
			value01.scale(rv);
			value00.add(value01);

			double val00 = getTexelOpacity(iu, iv);
			double val10 = getTexelOpacity(iuu, iv);
			double val01 = getTexelOpacity(iu, ivv);
			double val11 = getTexelOpacity(iuu, ivv);
			double bottom = val00 + ru * (val10 - val00);
			double top = val01 + ru * (val11 - val01);
			double opacity = bottom + rv * (top - bottom);
			this.opacity = opacity;

			return value00;
		} else
		{
			int iu = (int) Math.floor(u * m.texture.width);
			iu %= m.texture.width;
			int iv = (int) Math.floor(v * m.texture.height);
			iv %= m.texture.height;
			Color3 value00 = getTexelColor(iu, iv);
			double opacity = getTexelOpacity(iu, iv);
			this.opacity = opacity;
			return value00;
		}
	}

	// Ai(ambient) + Di(diffuse) + Si(specular)
	public Color3 localLighting(Vector3 direction, double ambientIntensity,
			double intensity)
	{
		Material m = this.hitNode.dummyMaterial;
		Color3 ambient = new Color3(m.ambientColor);
		ambient.scale(ambientIntensity);
		Color3 diffuse;
		if (m.texture == null)
			diffuse = new Color3(m.diffuseColor);
		else
			diffuse = textureColor();

		double s = Lambertian(direction, this.intersectionNormal);
		diffuse.scale(intensity * s);

		Color3 specular = new Color3(m.specularColor);
		double t = TorranceSparrow(direction, this.intersectionNormal,
				this.direction);
		specular.scale(intensity * t);

		ambient.add(diffuse);
		ambient.add(specular);
		return ambient;
	}

	public Color3 lightIntensity(ObjectNode lightNode)
	{
		Color3 c = new Color3(0, 0, 0);
		double shadowAttenuation = 1.0;

		if (lightNode.element instanceof PointLight)
		{
			PointLight p = (PointLight) lightNode.element;
			if (!p.on)
				return c;
			Vertex3 lightWorldPosition = lightNode.getWorldPosition(p.location);
			double d = Util3d.distance(this.intersection, lightWorldPosition);
			if (d > p.radius)
				return c;
			Vector3 direction = new Vector3(lightWorldPosition.x
					- this.intersection.x, lightWorldPosition.y - this.intersection.y,
					lightWorldPosition.z - this.intersection.z);
			direction.normalize();
			Ray ray = new Ray(this, direction);
			ray.lightRay = true;
			ray.shoot(objectWorld.root.child);
			if (ray.hitNode != null && ray.t > 0 && ray.t < d)
			{
				shadowAttenuation *= SHADOW_ATTENUATION;
			}
			ray = null;
			double attenuation = p.attenuation.x + p.attenuation.y * d
					+ p.attenuation.z * d * d;
			if (attenuation < 1.0)
				attenuation = 1.0;
			else
				attenuation = 1.0 / attenuation;
			c.assign(p.color);
			c.scale(attenuation);

			c.multiply(localLighting(direction, p.ambientIntensity, p.intensity));
			c.scale(shadowAttenuation);
		} else if (lightNode.element instanceof DirectionalLight)
		{
			DirectionalLight p = (DirectionalLight) lightNode.element;
			if (!p.on)
				return c;
			Vector3 direction = lightNode.getWorldVector(p.direction);
			direction.scale(-1);
			c.assign(p.color);
			c.multiply(localLighting(direction, p.ambientIntensity, p.intensity));
		}
		return c;
	}

	public Color3 localIntensity()
	{
		ObjectNode parent = this.hitNode.parent;
		Color3 c = new Color3(0, 0, 0);
		if (parent == null)
			return c;
		ObjectNode brother = parent.child;
		while (brother != null)
		{
			if (brother.isLightNode())
			{
				c.add(lightIntensity(brother));
			}
			brother = brother.next;
		}
		while (parent != null)
		{
			if (parent.isLightNode())
			{
				c.add(lightIntensity(parent));
			}
			parent = parent.parent;
		}
		return c;
	}

	public Color3 materialIntensity()
	{
		Material m = this.hitNode.dummyMaterial;
		Color3 c = localIntensity();
		if (Math.abs(m.reflection) > EPSILON)
		{
			Color3 c2 = reflectedIntensity();
			c2.scale(m.reflection);
			c.add(c2);
		}
		return c;
	}

	public Color3 reflectedIntensity()
	{
		if (this.level > MAXRAYLEVEL)
			return new Color3();
		Vector3 direction = ReflectedDirection();
		Ray ray = new Ray(this, direction);
		this.reflectedRay = ray;
		ray.shoot(objectWorld.root.child);
		Color3 c = new Color3(ray.color);
		ray = null;
		return c;
	}

	public Color3 transmittedIntensity()
	{
		if (this.level > MAXRAYLEVEL)
			return new Color3();
		Vector3 direction = RefractedDirection();
		Ray ray = new Ray(this, direction);
		this.transparentRay = ray;
		ray.shoot(objectWorld.root.child);
		Color3 c = new Color3(ray.color);
		ray = null;
		return c;
	}

	public Vector3 ReflectedDirection()
	{
		Vector3 v = new Vector3(this.intersectionNormal);
		double t = v.innerProduct(this.direction);
		v.scale(-2 * t);
		v.add(this.direction);
		v.normalize();
		return v;
	}

	// Snell's law: eta1 * sin(theta1) = eta2 * sin(theta2)
	public Vector3 RefractedDirection()
	{
		Material m = this.hitNode.dummyMaterial;
		if (Math.abs(m.refraction) < EPSILON)
			throw new InternalError("二擂唯が0になっています。");
		double s1, c1; // s1 = sin'theta1), c1 = cos(theta1)
		double s2, c2; // s2 = sin(theta2), c2 = cos(theta2)
		double eta = this.refraction / m.refraction;
		Vector3 n = new Vector3(this.intersectionNormal);
		double t = n.innerProduct(this.direction);// = -cos(theta1);
		c1 = -t;// = cos(theta1)
		s1 = 1 - c1 * c1;
		if (s1 < 0.0)
		{
			s1 = 0;
			s2 = 0;
		} else
		{
			s1 = Math.sqrt(s1);// = sin(theta1)
			s2 = eta * s1;// = sin(theta2)
		}
		c2 = 1 - s2 * s2;
		if (c2 < 0.0)
			c2 = 0;
		else
			c2 = Math.sqrt(c2); // = cos(theta2)
		if (c1 < 0.0)
			c2 = -c2;
		Vector3 p = new Vector3(this.intersectionNormal);
		p.scale(c1);
		p.add(this.direction);
		if (Math.abs(s1) < EPSILON)
			return (this.direction);
		p.scale(1.0 / s1);
		if (p.size() < EPSILON)
			return (this.direction);
		p.normalize();
		Vector3 direction = new Vector3(p);
		direction.scale(s2);
		n.scale(-c2);
		direction.add(n);
		direction.normalize();
		n = null;
		p = null;
		return direction;
	}

	public void shoot(ObjectNode node)
	{
		// find nearest object;
		start(node);
		if (this.t > LARGE || Math.abs(t) < EPSILON || this.hitNode == null)
		{
			this.color.r = this.objectWorld.background.r;
			this.color.g = this.objectWorld.background.g;
			this.color.b = this.objectWorld.background.b;
		} else
		{
			if (this.lightRay)
				return;
			setNearestIntersection();
			shading();
		}
	}

	public void start(ObjectNode node)
	{
		if (node == null)
			return;
		if (node.isShapeNode())
		{
			getNearerIntersection(node);
		}
		start(node.next);
		start(node.child);
	}
}
