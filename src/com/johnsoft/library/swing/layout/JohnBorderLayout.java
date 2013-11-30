package com.johnsoft.library.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

public class JohnBorderLayout implements LayoutManager2,Serializable
{
	  private static final long serialVersionUID = 1L;
	  
	  public static final String NORTH = "North";
	  public static final String SOUTH = "South";
	  public static final String EAST = "East";
	  public static final String WEST = "West";
	  public static final String CENTER = "Center";
	  public static final String NORTH_EAST = "NorthEast";
	  public static final String SOUTH_EAST = "SouthEast";
	  public static final String NORTH_WEST = "NorthWest";
	  public static final String SOUTH_WEST = "SouthWest";
	  
	  int hgap;
	  int vgap;
	  
	  private Component[][] components = new Component[3][];

	  public JohnBorderLayout()
	  {
	    for (int i = 0; i < this.components.length; ++i)
	      this.components[i] = new Component[3];
	  }

	  public void addLayoutComponent(String string, Component comp)
	  {
	  }

	  public void addLayoutComponent(Component comp, Object object)
	  {
	    if (object instanceof Point)
	    {
	      Point p = (Point)object;
	      this.components[p.x][p.y] = comp;
	    }
	    else
	    {
	      throw new RuntimeException("JohnBorderLayout constraint must be a Point!");
	    }
	  }

	  public float getLayoutAlignmentX(Container c)
	  {
	    return c.getAlignmentX();
	  }

	  public float getLayoutAlignmentY(Container c)
	  {
	    return c.getAlignmentY();
	  }

	  public void invalidateLayout(Container c)
	  {
	  }

	  public Dimension maximumLayoutSize(Container c)
	  {
	    int i = 0;
	    int j = 0;
	    for (int k = 0; k < 3; ++k)
	    {
	      i += getMaximumWidth(k);
	      j += getMaximumHeight(k);
	    }
	    return new Dimension(i, j);
	  }

	  private int getPreferredHeight(int height)
	  {
	    int i = 0;
	    for (int j = 0; j < 3; ++j)
	    {
	      Component comp = this.components[j][height];
	      if ((comp == null) || (!(comp.isVisible())))
	        continue;
	      int k = comp.getPreferredSize().height;
	      if (k <= i)
	        continue;
	      i = k;
	    }
	    return i;
	  }

	  private int getPreferredWidth(int width)
	  {
	    int i = 0;
	    for (int j = 0; j < 3; ++j)
	    {
	      Component comp = this.components[width][j];
	      if ((comp == null) || (!(comp.isVisible())))
	        continue;
	      int k = comp.getPreferredSize().width;
	      if (k <= i)
	        continue;
	      i = k;
	    }
	    return i;
	  }

	  private int getMinimumHeight(int height)
	  {
	    int i = 0;
	    for (int j = 0; j < 3; ++j)
	    {
	      Component comp = this.components[j][height];
	      if ((comp == null) || (!(comp.isVisible())))
	        continue;
	      int k = comp.getMinimumSize().height;
	      if (k <= i)
	        continue;
	      i = k;
	    }
	    return i;
	  }

	  private int getMinimumWidth(int width)
	  {
	    int i = 0;
	    for (int j = 0; j < 3; ++j)
	    {
	      Component comp = this.components[width][j];
	      if ((comp == null) || (!(comp.isVisible())))
	        continue;
	      int k = comp.getMinimumSize().width;
	      if (k <= i)
	        continue;
	      i = k;
	    }
	    return i;
	  }

	  private int getMaximumHeight(int height)
	  {
	    int i = 2147483647;
	    for (int j = 0; j < 3; ++j)
	    {
	      Component comp = this.components[j][height];
	      if ((comp == null) || (!(comp.isVisible())))
	        continue;
	      int k = comp.getMaximumSize().height;
	      if (k >= i)
	        continue;
	      i = k;
	    }
	    return i;
	  }

	  private int getMaximumWidth(int width)
	  {
	    int i = 0;
	    for (int j = 0; j < 3; ++j)
	    {
	      Component comp = this.components[width][j];
	      if ((comp == null) || (!(comp.isVisible())))
	        continue;
	      int k = comp.getMaximumSize().width;
	      if (k >= i)
	        continue;
	      i = k;
	    }
	    return i;
	  }

	  private static void setBounds(Component comp, Rectangle r)
	  {
	    int i = Math.min(comp.getMaximumSize().width, r.width);
	    int j = Math.min(comp.getMaximumSize().height, r.height);
	    Rectangle rect = new Rectangle(r.x + (int)(comp.getAlignmentX() * (r.width - i)), r.y + (int)(comp.getAlignmentY() * (r.height - j)), i, j);
	    comp.setBounds(rect);
	  }
	  
	  public void layoutContainer(Container c)
	  {
	    Insets insets = c.getInsets();
	    Dimension dim = new Dimension(c.getWidth() - insets.left - insets.right, c.getHeight() - insets.top - insets.bottom);
	    int[] pw = { getPreferredWidth(0), getPreferredWidth(2) };
	    int[] ph = { getPreferredHeight(0), getPreferredHeight(2) };
	    int i = insets.top;
	    for (int j = 0; j < 3; ++j)
	    {
	      int k = (j == 1) ? dim.height - ph[0] - ph[1] : ph[(j / 2)];
	      int l = insets.left;
	      for (int i1 = 0; i1 < 3; ++i1)
	      {
	        int i2 = (i1 == 1) ? dim.width - pw[0] - pw[1] : pw[(i1 / 2)];
	        Component comp = this.components[i1][j];
	        if ((comp != null) && (comp.isVisible()))
	          setBounds(comp, new Rectangle(l, i, i2, k));
	        l += i2;
	      }
	      i += k;
	    }
	  }

	  public Dimension minimumLayoutSize(Container c)
	  {
	    int i = 0;
	    int j = 0;
	    for (int k = 0; k < 3; ++k)
	    {
	      i += getMinimumWidth(k);
	      j += getMinimumHeight(k);
	    }
	    return new Dimension(i, j);
	  }

	  public Dimension preferredLayoutSize(Container c)
	  {
	    int i = 0;
	    int j = 0;
	    for (int k = 0; k < 3; ++k)
	    {
	      i += getPreferredWidth(k);
	      j += getPreferredHeight(k);
	    }
	    return new Dimension(i, j);
	  }

	  public void removeLayoutComponent(Component comp)
	  {
	    for (int i = 0; i < 3; ++i)
	      for (int j = 0; j < 3; ++j)
	      {
	        if (this.components[i][j] != comp)
	          continue;
	        this.components[i][j] = null;
	        return;
	      }
	  }
}
