package com.johnsoft.library.component;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class JohnSeparator extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public enum StrokeStyle
	{
		THIN, BOLD, THICK
	}
	
	public enum CurveStyle
	{
		NEAR_LINE,NORMAL,NEAR_ARC
	}
	
	public enum StretchStyle
	{
		LIGHT, HEAVY
	}
	
	private StrokeStyle stroke=StrokeStyle.THIN;
	private CurveStyle curve=CurveStyle.NEAR_LINE;
	private StretchStyle stretch=StretchStyle.HEAVY;
	private boolean reverse=false;
	
	public JohnSeparator(){
	}
	
	public JohnSeparator(boolean reverse)
	{	
		this.reverse=reverse;
	}
	
	public JohnSeparator(boolean reverse,int width,int height)
	{
		this.reverse=reverse;
		Dimension dim=new Dimension(width, height);
		setPreferredSize(dim);
		setSize(dim);
	}
	
	public JohnSeparator(boolean reverse,StrokeStyle stroke,CurveStyle curve,StretchStyle stretch)
	{
		this(reverse,stroke,curve,stretch,10,10);
	}
	
	public JohnSeparator(boolean reverse,StrokeStyle stroke,CurveStyle curve,StretchStyle stretch,int width,int height)
	{	
		this.reverse=reverse;
		this.stroke=stroke;
		this.curve=curve;
		this.stretch=stretch;
		Dimension dim=new Dimension(width, height);
		setPreferredSize(dim);
		setSize(dim);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Rectangle r=getBounds();
		CubicCurve2D besier;
		int gap=switchStroke();
		double stretchFactor=switchStetch();
		double curveFactor=switchCurve();
		
		g2d.setStroke(new BasicStroke(gap));
		
		g2d.setColor(UIManager.getColor("Separator.foreground"));
		if(reverse)
		{
			besier=new CubicCurve2D.Double(0, 0, r.width*curveFactor, r.height*(1-stretchFactor), r.width*(1-curveFactor), r.height*stretchFactor, r.width, r.height);
		}else{
			besier=new CubicCurve2D.Double(0, 0, r.width*(1-curveFactor), r.height*stretchFactor, r.width*curveFactor, r.height*(1-stretchFactor), r.width, r.height);
		}
		g2d.draw(besier);
		
		g2d.setColor(UIManager.getColor("Separator.background"));
		if(reverse)
		{
			besier=new CubicCurve2D.Double(gap, 0, r.width*curveFactor, r.height*(1-stretchFactor), gap+r.width*(1-curveFactor), r.height*stretchFactor, gap+r.width, r.height);
		}else{
			besier=new CubicCurve2D.Double(gap, 0, gap+r.width*(1-curveFactor), r.height*stretchFactor, gap+r.width*curveFactor, r.height*(1-stretchFactor), gap+r.width, r.height);
		}
		g2d.draw(besier);
	}
	
	protected int switchStroke()
	{
		switch (stroke)
		{
		case THIN:
			return 1;
		case BOLD:
			return 2;
		case THICK:
			return 4;
		default:
			return 1;
		}
	}
	
	protected double switchStetch()
	{
		switch (stretch)
		{
		case LIGHT:
			return 0.3;
		case HEAVY:
			return 0.1;
		default:
			return 0.3;
		}
	}
	
	protected double switchCurve()
	{
		switch (curve)
		{
		case NEAR_LINE:
			return 0.5;
		case NORMAL:
			return 0;
		case NEAR_ARC:
			return -1;
		default:
			return 0;
		}
	}

	public StrokeStyle getStroke()
	{
		return stroke;
	}

	public void setStroke(StrokeStyle stroke)
	{
		this.stroke = stroke;
	}

	public CurveStyle getCurve()
	{
		return curve;
	}

	public void setCurve(CurveStyle curve)
	{
		this.curve = curve;
	}

	public StretchStyle getStretch()
	{
		return stretch;
	}

	public void setStretch(StretchStyle stretch)
	{
		this.stretch = stretch;
	}

	public boolean isReverse()
	{
		return reverse;
	}

	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
	}
	
}
