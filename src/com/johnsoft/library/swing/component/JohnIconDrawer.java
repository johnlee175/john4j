package com.johnsoft.library.swing.component;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class JohnIconDrawer
{
	public static void drawCloseIcon(Graphics2D g2,Rectangle2D r2,int gap)
	{
		Path2D.Double path=new Path2D.Double();
		path.moveTo(r2.getX()+gap*2, r2.getY()+gap*2);
		path.lineTo(r2.getX()+r2.getWidth()-gap*2, r2.getY()+r2.getHeight()-gap*2);
		path.moveTo(r2.getX()+r2.getWidth()-gap*2, r2.getY()+gap*2);
		path.lineTo(r2.getX()+gap*2, r2.getY()+r2.getHeight()-gap*2);
		g2.draw(path);
	}
	
}
