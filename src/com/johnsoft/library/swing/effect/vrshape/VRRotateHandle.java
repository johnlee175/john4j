package com.johnsoft.library.swing.effect.vrshape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.johnsoft.library.swing.effect.ConicalGradientPaint;

public class VRRotateHandle implements VRShape
{
	public VRRotateHandle(Rectangle r)
	{
		Dimension size=r.getSize();
		int diameter=Math.min(size.width-60, size.height-60);
		this.diameter=diameter<0?0:diameter;
		center=new Point(size.width/2, size.height/2-5);
		radius=diameter/2;
	}
	private int tickCount=36;
	private Point center;
	private int diameter;
	private int radius;
	private int handleAngle;
	
	public void paint(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setPaint(new ConicalGradientPaint(center, new float[]{0.0f,0.2f,0.5f,1.0f}, new Color[]{Color.DARK_GRAY,new Color(150, 150, 200),Color.WHITE,Color.DARK_GRAY}));
		g2d.fillOval(center.x-radius, center.y-radius, diameter, diameter);
		
		int tempScale=50;
		g2d.setPaint(new RadialGradientPaint(center, radius-tempScale, new float[]{0.0f,0.8f,0.9f,1.0f}, new Color[]{Color.LIGHT_GRAY,Color.DARK_GRAY,Color.WHITE,Color.BLACK}));
		g2d.fillOval(center.x-radius+tempScale, center.y-radius+tempScale, diameter-tempScale*2, diameter-tempScale*2);
		
		g2d.setPaint(new LinearGradientPaint(new Point(center.x,center.y-20), new Point(center.x,center.y+20), new float[]{0.0f,0.2f,0.3f,0.8f,1.0f}, new Color[]{Color.BLACK,new Color(150, 150, 200),Color.LIGHT_GRAY,Color.WHITE,Color.BLACK}));
		g2d.rotate(-Math.toRadians(handleAngle), center.x, center.y);
		g2d.fillRoundRect(center.x-radius+tempScale, center.y-20, diameter-tempScale*2, 40, 35, 35);
		
		g2d.setColor(Color.BLACK);
		g2d.drawOval(center.x-radius, center.y-radius, diameter, diameter);
		
		Font font=new Font("Times New Roman", Font.BOLD, 24);
		g2d.setFont(font);
		g2d.setColor(new Color(210, 0, 0));
		FontMetrics fm=g2d.getFontMetrics();
		int descent=fm.getDescent();
		int ascent=fm.getAscent();
		int leading=fm.getLeading();
		g2d.drawString("90", center.x-(int)fm.getStringBounds("3", g2d).getWidth(), center.y-radius-descent);
		g2d.drawString("180", center.x-radius-(int)fm.getStringBounds("27", g2d).getWidth()-leading, center.y+descent+2);
		g2d.drawString("270", center.x-(int)fm.getStringBounds("1", g2d).getWidth(), center.y+radius+ascent);
		g2d.drawString("0", center.x+radius+leading, center.y+descent+2);
		
		double arc=(2*Math.PI)/tickCount;
		int rotatex=center.x;
		int rotatey=center.y-radius;
		int centerY=center.y;
		int smallTickLen=center.y-radius+15;
		int bigTickLen=center.y-radius+20;
		int bigTickLenTwo=bigTickLen*2;
		BasicStroke thick=new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
		BasicStroke thin=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
		for(int i=0;i<tickCount;i++)
		{
			if(i%9==0)
			{
				g2d.setColor(Color.RED);
				g2d.fill(new Arc2D.Float(rotatex-bigTickLen, rotatey, bigTickLenTwo, bigTickLen, 80, 20, Arc2D.PIE));
			}
			else
			if(i%3==0)
			{
				g2d.setStroke(thick);
				g2d.drawLine(rotatex, rotatey, rotatex, bigTickLen);
			}
			else
			{
				g2d.drawLine(rotatex, rotatey, rotatex, smallTickLen);	
			}
			g2d.rotate(arc, rotatex, centerY);
			g2d.setColor(Color.BLACK);
			g2d.setStroke(thin);
		}
		
		g2d.dispose();
	}
	
//	public static void main(String[] args)
//	{
//		JFrame jf=new JFrame();
//		JPanel jp=new JPanel()
//		{
//			@Override
//			protected void paintComponent(Graphics g)
//			{
//				VRRotateHandle h=new VRRotateHandle(getBounds());
//				h.paint((Graphics2D)g);
//			}
//		};
//		jf.add(jp);
//		jf.setSize(500,400);
//		jf.setVisible(true);
//	}
}
