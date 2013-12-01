/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.johnsoft.library.swing.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;


public class ColorArrowButton extends BasicArrowButton 
{
	    private static final long serialVersionUID = 1L;
	    
			private Color shadow;
		  private Color darkShadow;
		  private Color highlight;
	   
      public ColorArrowButton(int direction, Color background, Color shadow,
			Color darkShadow, Color highlight)
      {
		     super(direction, background, shadow, darkShadow, highlight);
				 this.shadow = shadow;
				 this.darkShadow = darkShadow;
		     this.highlight = highlight;
      }

        public ColorArrowButton(int direction) {
        	this(direction, UIManager.getColor("control"), UIManager.getColor("controlShadow"),
        			UIManager.getColor("controlDkShadow"), UIManager.getColor("controlLtHighlight"));
        }



	public void paint(Graphics g) {
	
	    boolean isEnabled;
	    int w, h, size;

            w = getSize().width;
            h = getSize().height;
	   
	    isEnabled = isEnabled();

          	Graphics2D g2=(Graphics2D)g;
        		g2.setPaint(new GradientPaint(1+w/8,1, new Color(195,255,195), w/2,h/2, new Color(0,255,0)));
        		g2.fillRoundRect(1, 1, w-2, h-2,5,5);
        		
            size = Math.min((h - 4) / 3, (w - 4) / 3);
            size = Math.max(size, 2);
     
        paintTriangle(g, (w - size) / 2, (h - size) / 2,
				size, direction, isEnabled);

        }

	public void paintTriangle(Graphics g, int x, int y, int size, 
					int direction, boolean isEnabled) {
	    Color oldColor = g.getColor();
	    int mid, i, j;

	    j = 0;
            size = Math.max(size, 2);
	    mid = (size / 2) - 1;
	
	    g.translate(x, y);
	    if(isEnabled)
		g.setColor(darkShadow);
	    else
		g.setColor(shadow);

            switch(direction)       {
            case NORTH:
                for(i = 0; i < size; i++)      {
                    g.drawLine(mid-i, i, mid+i, i);
                }
                if(!isEnabled)  {
                    g.setColor(highlight);
                    g.drawLine(mid-i+2, i, mid+i, i);
                }
                break;
            case SOUTH:
                if(!isEnabled)  {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for(i = size-1; i >= 0; i--)   {
                        g.drawLine(mid-i, j, mid+i, j);
                        j++;
                    }
		    g.translate(-1, -1);
		    g.setColor(shadow);
		}
		
		j = 0;
                for(i = size-1; i >= 0; i--)   {
                    g.drawLine(mid-i, j, mid+i, j);
                    j++;
                }
                break;
            case WEST:
                for(i = 0; i < size; i++)      {
                    g.drawLine(i, mid-i, i, mid+i);
                }
                if(!isEnabled)  {
                    g.setColor(highlight);
                    g.drawLine(i, mid-i+2, i, mid+i);
                }
                break;
            case EAST:
                if(!isEnabled)  {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for(i = size-1; i >= 0; i--)   {
                        g.drawLine(j, mid-i, j, mid+i);
                        j++;
                    }
		    g.translate(-1, -1);
		    g.setColor(shadow);
                }

		j = 0;
                for(i = size-1; i >= 0; i--)   {
                    g.drawLine(j, mid-i, j, mid+i);
                    j++;
                }
		break;
            }
	    g.translate(-x, -y);	
	    g.setColor(oldColor);
	}
	
}

