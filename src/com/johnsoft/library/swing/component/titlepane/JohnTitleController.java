package com.johnsoft.library.swing.component.titlepane;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public interface JohnTitleController
{
	public void paintController(Graphics2D g2,Rectangle2D r2);
	
	public void mouseOver(MouseEvent event);
	
	public void mouseOut(MouseEvent event);
	
	public void mousePress(MouseEvent event);
	
	public void mouseRelease(MouseEvent event);
	
	public String getName();
	
	public String getTooltip();
	
	public void setOwner(JohnTitlePane titlePane);
}
