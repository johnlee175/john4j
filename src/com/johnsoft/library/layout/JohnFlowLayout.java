package com.johnsoft.library.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * FlowLayout的简单扩展,一致的特性,仅多一项属性valign控制组件垂直方向的对齐布局
 * @author john
 */
public class JohnFlowLayout implements LayoutManager, java.io.Serializable
{
	private static final long serialVersionUID = -7262534875583282175L;

	/**
	 * 对齐方式;水平对齐方式有LEFT,CENTER,RIGHT;垂直对齐方式有TOP,MIDDLE,BOTTOM.
	 */
	public enum Alignment
	{
		LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM
	}

	protected Alignment halign; // 水平对齐方式

	protected Alignment valign; // 垂直对齐方式

	protected int hgap; // 组件间水平间隙

	protected int vgap; // 组件间垂直间隙

	protected boolean alignOnBaseline; // 对齐是否依据基线

	public JohnFlowLayout()
	{
		this(Alignment.CENTER, Alignment.MIDDLE, 5, 5);
	}

	public JohnFlowLayout(int gap)
	{
		this(Alignment.CENTER, Alignment.MIDDLE, gap, gap);
	}

	public JohnFlowLayout(int hgap, int vgap)
	{
		this(Alignment.CENTER, Alignment.MIDDLE, hgap, vgap);
	}

	public JohnFlowLayout(Alignment align, Alignment valign)
	{
		this(align, valign, 5, 5);
	}

	/**
	 * @param halign 水平对齐方式,有Alignment.CENTER,Alignment.LEFT,Alignment.RIGHT可选,默认为CENTER;
	 * @param valign 垂直对齐方式,有Alignment.MIDDLE,Alignment.TOP,Alignment.BOTTOM可选,默认为MIDDLE;
	 * @param hgap 组件间水平间隙,默认为5;
	 * @param vgap 组件间垂直间隙,默认为5.
	 */
	public JohnFlowLayout(Alignment halign, Alignment valign, int hgap, int vgap)
	{
		this.hgap = hgap;
		this.vgap = vgap;
		this.halign = halign;
		this.valign = valign;
	}
	
	public Dimension preferredLayoutSize(Container parent)
	{
		return computeLayoutSize(parent, "preferred");
	}

	public Dimension minimumLayoutSize(Container parent)
	{
		return computeLayoutSize(parent, "minimum");
	}

	/**
	 * 被此布局管理器Dimension preferredLayoutSize(Container)和Dimension
	 * minimumLayoutSize(Container)调用;
	 * 因为上两个方法基本一致,仅存在取得容器内组件大小时是采用getPreferredSize(
	 * )还是getMinimumSize()的分歧,封装在一个方法里便于扩展重写.
	 * @param parent
	 *          父容器
	 * @param type
	 *          类型:preferred 或 minimum
	 * @return 容器尺寸
	 */
	protected Dimension computeLayoutSize(Container parent,String type)
	{
		synchronized (parent.getTreeLock())
		{
			Dimension dim = new Dimension(0, 0);
			int nmembers = parent.getComponentCount();
			boolean firstVisibleComponent = true;
			boolean useBaseline = getAlignOnBaseline();
			int maxAscent = 0;
			int maxDescent = 0;
			
			for (int i = 0; i < nmembers; i++)
			{
				Component comp = parent.getComponent(i);
				if (comp.isVisible())
				{
					Dimension d=null;
					if (type.equals("preferred"))
					{
						d = comp.getPreferredSize();
					} else if (type.equals("minimum"))
					{
						d = comp.getMinimumSize();
					}
					dim.height = Math.max(dim.height, d.height);
					if (firstVisibleComponent)
					{
						firstVisibleComponent = false;
					} else
					{
						dim.width += hgap;
					}
					dim.width += d.width;
					if (useBaseline)
					{
						int baseline = comp.getBaseline(d.width, d.height);
						if (baseline >= 0)
						{
							maxAscent = Math.max(maxAscent, baseline);
							maxDescent = Math.max(maxDescent, (type.equals("preferred")?d.height:dim.height) - baseline);
						}
					}
				}
			}
			
			if (useBaseline)
			{
				dim.height = Math.max(maxAscent + maxDescent, dim.height);
			}
			Insets insets = parent.getInsets();
			dim.width += insets.left + insets.right + hgap * 2;
			dim.height += insets.top + insets.bottom + vgap * 2;
			return dim;
		}
	}
	
	/**
	 * 摘自FlowLayout,void layoutContainer(Container)调用,用来具体控制容器内每个组件的位置以及容器缩小导致折行时的行为;
	 */
	protected int moveComponents(Container parent, int x, int y, int width,
			int height, int rowStart, int rowEnd, boolean ltr, boolean useBaseline,
			int[] ascent, int[] descent)
	{
		switch(halign)
		{
		case LEFT:
			x += ltr ? 0 : width;
			break;
		case CENTER:
			x += width / 2;
			break;
		case RIGHT:
			x += ltr ? width : 0;
			break;
		default:
			x += width / 2;
			break;
		}
		int maxAscent = 0;
		int nonbaselineHeight = 0;
		int baselineOffset = 0;
		if (useBaseline)
		{
			int maxDescent = 0;
			for (int i = rowStart; i < rowEnd; i++)
			{
				Component comp = parent.getComponent(i);
				if (comp.isVisible())
				{
					if (ascent[i] >= 0)
					{
						maxAscent = Math.max(maxAscent, ascent[i]);
						maxDescent = Math.max(maxDescent, descent[i]);
					} else
					{
						nonbaselineHeight = Math.max(comp.getHeight(), nonbaselineHeight);
					}
				}
			}
			height = Math.max(maxAscent + maxDescent, nonbaselineHeight);
			baselineOffset = (height - maxAscent - maxDescent) / 2;
		}
		for (int i = rowStart; i < rowEnd; i++)
		{
			Component comp = parent.getComponent(i);
			if (comp.isVisible())
			{
				int cy;
				if (valign == Alignment.TOP)
				{
					cy = y;
				} else if (valign == Alignment.BOTTOM)
				{
					cy = y + (height - comp.getHeight());
				} else
				{
					if (useBaseline && ascent[i] >= 0)
					{
						cy = y + baselineOffset + maxAscent - ascent[i];
					} else
					{
						cy = y + (height - comp.getHeight()) / 2;
					}
				}
				if (ltr)
				{
					comp.setLocation(x, cy);
				} else
				{
					comp.setLocation(parent.getWidth() - x - comp.getWidth(), cy);
				}
				x += comp.getWidth() + hgap;
			}
		}
		return height;
	}

	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			Insets insets = parent.getInsets();
			int maxwidth = parent.getWidth()
					- (insets.left + insets.right + hgap * 2);
			int nmembers = parent.getComponentCount();
			int x = 0, y = insets.top + vgap;
			int rowh = 0, start = 0;
			boolean ltr = parent.getComponentOrientation().isLeftToRight();
			boolean useBaseline = getAlignOnBaseline();
			int[] ascent = null;
			int[] descent = null;
			if (useBaseline)
			{
				ascent = new int[nmembers];
				descent = new int[nmembers];
			}
			for (int i = 0; i < nmembers; i++)
			{
				Component comp = parent.getComponent(i);
				if (comp.isVisible())
				{
					Dimension d = comp.getPreferredSize();
					comp.setSize(d.width, d.height);
					if (useBaseline)
					{
						int baseline = comp.getBaseline(d.width, d.height);
						if (baseline >= 0)
						{
							ascent[i] = baseline;
							descent[i] = d.height - baseline;
						} else
						{
							ascent[i] = -1;
						}
					}
					if ((x == 0) || ((x + d.width) <= maxwidth))
					{
						if (x > 0)
						{
							x += hgap;
						}
						x += d.width;
						rowh = Math.max(rowh, d.height);
					} else
					{
						rowh = moveComponents(parent, insets.left + hgap, y, maxwidth - x,
								rowh, start, i, ltr, useBaseline, ascent, descent);
						x = d.width;
						y += vgap + rowh;
						rowh = d.height;
						start = i;
					}
				}
			}
			moveComponents(parent, insets.left + hgap, y, maxwidth - x, rowh, start,
					nmembers, ltr, useBaseline, ascent, descent);
		}
	}

	public Alignment getHAlignment()
	{
		return halign;
	}

	public void setHAlignment(Alignment halign)
	{
		this.halign = halign;
	}

	public Alignment getValignment()
	{
		return valign;
	}

	public void setValignment(Alignment valign)
	{
		this.valign = valign;
	}

	public int getHgap()
	{
		return hgap;
	}

	public void setHgap(int hgap)
	{
		this.hgap = hgap;
	}

	public int getVgap()
	{
		return vgap;
	}

	public void setVgap(int vgap)
	{
		this.vgap = vgap;
	}

	public void setAlignOnBaseline(boolean alignOnBaseline)
	{
		this.alignOnBaseline = alignOnBaseline;
	}

	public boolean getAlignOnBaseline()
	{
		return alignOnBaseline;
	}

	public void addLayoutComponent(String name, Component comp)
	{
	}

	public void removeLayoutComponent(Component comp)
	{
	}

	public String toString()
	{
		String str1 = "";
		String str2 = "";
		switch(halign)
		{
		case LEFT:
			str1 = ",halign=left";
			break;
		case CENTER:
			str1 = ",halign=center";
			break;
		case RIGHT:
			str1 = ",halign=right";
			break;
		default:
			str1 = ",halign=center";
			break;
		}
		switch(valign)
		{
		case TOP:
			str2 = ",valign=top";
			break;
		case MIDDLE:
			str2 = ",valign=middle";
			break;
		case BOTTOM:
			str2 = ",valign=bottom";
			break;
		default:
			str2 = ",valign=middle";
			break;
		}
		return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + str1
				+ str2 + "]";
	}

}
