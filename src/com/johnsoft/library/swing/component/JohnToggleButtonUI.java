package com.johnsoft.library.swing.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;

public class JohnToggleButtonUI extends BasicButtonUI
{
	private int arcw=12,arch=12;//圆矩形的弧度
	private RoundRectangle2D.Double rr;//填充圆矩形
	private RoundRectangle2D.Double rrd;//边框圆矩形
	private BasicStroke stroke=new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	
	private Paint background;
	private Paint rollOverBackground;
	private Paint pressBackground;
	private Paint iconBackground=Color.BLUE;
	private Paint rollOverIconBackground=Color.GREEN;
	private Paint pressIconBackground=Color.RED;
	private Paint selectedBackground;
	
	private Paint borderPaint;
	private Paint rollOverBorderPaint=Color.CYAN;
	private Paint pressBorderPaint;
	private Paint iconBorderPaint;
	private Paint rollOverIconBorderPaint;
	private Paint pressIconBorderPaint;
	private Paint selectedBorderPaint;
	
	private boolean isRollOver=false;
	private boolean isPress=false;
	
	protected void setPaints()
	{
		pressBackground=new GradientPaint(new Point2D.Double(rr.x,rr.y+rr.height/2), new Color(0,0,0,0), new Point2D.Double(rr.x+rr.width/3*2,rr.y+rr.height/2), Color.WHITE);
		Point2D center = new Point2D.Double(rr.x+rr.width, rr.y);
		float radius = 80;
		Point2D focus = new Point2D.Float(10, 10);
		float[] dist = {0.0f, 0.6f, 1.0f};
		Color[] colors = { Color.WHITE, Color.yellow, Color.ORANGE};
		RadialGradientPaint p =
		new RadialGradientPaint(center, radius, focus,
		dist, colors,
		CycleMethod.NO_CYCLE);
		selectedBackground=p;
	}
	
	@Override
	protected BasicButtonListener createButtonListener(AbstractButton b)
	{
		return new SimpleHandler(b);
	}
	
	@Override
	public void paint(Graphics g, JComponent c)
	{
		Rectangle r=g.getClipBounds();
		JToggleButton button=(JToggleButton)c;
		Graphics2D g2=(Graphics2D)g;
		rr=new RoundRectangle2D.Double(r.x, r.y, r.width, r.height, arcw, arch);
		rrd=new RoundRectangle2D.Double(r.x, r.y, r.width, r.height, arcw, arch);
		Rectangle iconRect;
		if(r.width<r.height)
		{
			iconRect=new Rectangle(r.x, r.y+(r.height-r.width)/2, r.width, r.width);
		}else{
			iconRect=new Rectangle(r.x+(r.width-r.height)/2, r.y, r.height, r.height);
		}
		
		setPaints();
		
		paint(g2, rr, background, null);
		paint(g2, rrd, borderPaint, stroke);
		
		if(button.isSelected())
		{
			paint(g2, rr, selectedBackground, null);
			paint(g2, rrd, selectedBorderPaint, stroke);
		}
		
		if(isRollOver)
		{
			paint(g2, rr, rollOverBackground, null);
			paint(g2, rrd, rollOverBorderPaint, stroke);
		}
		
		if(isPress)
		{
			paint(g2, rr, pressBackground, null);
			paint(g2, rrd, pressBorderPaint, stroke);
		}
		
		paintIcon(g2, c, iconRect);
	}
	
	@Override
	protected void paintIcon(Graphics g, JComponent c, Rectangle r)
	{
		JToggleButton button=(JToggleButton)c;
		Graphics2D g2=(Graphics2D)g;
		
		Path2D.Double path=new Path2D.Double();
		if(button.isSelected())
		{
			path.moveTo(r.x+r.width/6*5, r.y+r.height/6);
			path.lineTo(r.x+r.width/6*5, r.y+r.height/6*5);
			path.lineTo(r.x+r.width/6, r.y+r.height/2);
			path.closePath();
		}else{
			path.moveTo(r.x+r.width/6, r.y+r.height/6);
			path.lineTo(r.x+r.width/6, r.y+r.height/6*5);
			path.lineTo(r.x+r.width/6*5, r.y+r.height/2);
			path.closePath();
		}
		
		paint(g2, path, iconBackground, null);
		paint(g2, path, iconBorderPaint, stroke);
		
		if(isRollOver)
		{
			paint(g2, path, rollOverIconBackground, null);
			paint(g2, path, rollOverIconBorderPaint, stroke);
		}
		
		if(isPress)
		{
			paint(g2, path, pressIconBackground, null);
			paint(g2, path, pressIconBorderPaint, stroke);
		}
	}
	
	public class SimpleHandler extends BasicButtonListener
	{
		public SimpleHandler(AbstractButton b)
		{
			super(b);
		}
		@Override
		public void mouseEntered(MouseEvent e)
		{
			super.mouseEntered(e);
			selectOption(true, false);
		}
		@Override
		public void mouseExited(MouseEvent e)
		{
			super.mouseExited(e);
			selectOption(false, false);
		}
		@Override
		public void mousePressed(MouseEvent e)
		{
			super.mousePressed(e);
			selectOption(false, true);
		}
		@Override
		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);
			selectOption(false, false);
		}
	}
	
	private void paint(Graphics2D g2,Shape shape,Paint paint,BasicStroke stroke)
	{
		if(paint==null)
		{
			return;
		}
		g2.setPaint(paint);
		if(stroke!=null)
		{
			g2.setStroke(stroke);
			g2.draw(shape);
		}else{
			g2.fill(shape);
		}
	}
	
	private void selectOption(boolean isRollOver,boolean isPress)
	{
		this.isRollOver=isRollOver;
		this.isPress=isPress;
	}

	public int getArcw()
	{
		return arcw;
	}

	public void setArcw(int arcw)
	{
		this.arcw = arcw;
	}

	public int getArch()
	{
		return arch;
	}

	public void setArch(int arch)
	{
		this.arch = arch;
	}

	public Paint getBackground()
	{
		return background;
	}

	public void setBackground(Paint background)
	{
		this.background = background;
	}

	public Paint getRollOverBackground()
	{
		return rollOverBackground;
	}

	public void setRollOverBackground(Paint rollOverBackground)
	{
		this.rollOverBackground = rollOverBackground;
	}

	public Paint getPressBackground()
	{
		return pressBackground;
	}

	public void setPressBackground(Paint pressBackground)
	{
		this.pressBackground = pressBackground;
	}

	public Paint getIconBackground()
	{
		return iconBackground;
	}

	public void setIconBackground(Paint iconBackground)
	{
		this.iconBackground = iconBackground;
	}

	public Paint getRollOverIconBackground()
	{
		return rollOverIconBackground;
	}

	public void setRollOverIconBackground(Paint rollOverIconBackground)
	{
		this.rollOverIconBackground = rollOverIconBackground;
	}

	public Paint getPressIconBackground()
	{
		return pressIconBackground;
	}

	public void setPressIconBackground(Paint pressIconBackground)
	{
		this.pressIconBackground = pressIconBackground;
	}

	public Paint getSelectedBackground()
	{
		return selectedBackground;
	}

	public void setSelectedBackground(Paint selectedBackground)
	{
		this.selectedBackground = selectedBackground;
	}

	public Paint getBorderPaint()
	{
		return borderPaint;
	}

	public void setBorderPaint(Paint borderPaint)
	{
		this.borderPaint = borderPaint;
	}

	public Paint getRollOverBorderPaint()
	{
		return rollOverBorderPaint;
	}

	public void setRollOverBorderPaint(Paint rollOverBorderPaint)
	{
		this.rollOverBorderPaint = rollOverBorderPaint;
	}

	public Paint getPressBorderPaint()
	{
		return pressBorderPaint;
	}

	public void setPressBorderPaint(Paint pressBorderPaint)
	{
		this.pressBorderPaint = pressBorderPaint;
	}

	public Paint getIconBorderPaint()
	{
		return iconBorderPaint;
	}

	public void setIconBorderPaint(Paint iconBorderPaint)
	{
		this.iconBorderPaint = iconBorderPaint;
	}

	public Paint getRollOverIconBorderPaint()
	{
		return rollOverIconBorderPaint;
	}

	public void setRollOverIconBorderPaint(Paint rollOverIconBorderPaint)
	{
		this.rollOverIconBorderPaint = rollOverIconBorderPaint;
	}

	public Paint getPressIconBorderPaint()
	{
		return pressIconBorderPaint;
	}

	public void setPressIconBorderPaint(Paint pressIconBorderPaint)
	{
		this.pressIconBorderPaint = pressIconBorderPaint;
	}

	public Paint getSelectedBorderPaint()
	{
		return selectedBorderPaint;
	}

	public void setSelectedBorderPaint(Paint selectedBorderPaint)
	{
		this.selectedBorderPaint = selectedBorderPaint;
	}

	public boolean isRollOver()
	{
		return isRollOver;
	}

	public void setRollOver(boolean isRollOver)
	{
		this.isRollOver = isRollOver;
	}

	public boolean isPress()
	{
		return isPress;
	}

	public void setPress(boolean isPress)
	{
		this.isPress = isPress;
	}

}
