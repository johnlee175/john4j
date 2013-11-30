package com.johnsoft.library.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/**
 * 简单改进的GridLayout,添加了控制四周间隙和各行列比例的功能
 * @author 李哲浩
 */
public class JohnGridLayout implements LayoutManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	int rows;
	int cols;
	int hgap;
	int vgap;
	
	Insets insets;//四周间隙
	int[] rowScale;//各行比例
	int[] colScale;//各列比例
	
	boolean rowHeightIgnored;//是否取子组件的行高
	boolean colWidthIgnored;//是否取子组件的列宽

	public boolean isRowHeightIgnored()
	{
		return rowHeightIgnored;
	}
	public void setRowHeightIgnored(boolean rowHeightIgnored)
	{
		this.rowHeightIgnored = rowHeightIgnored;
	}
	public boolean isColWidthIgnored()
	{
		return colWidthIgnored;
	}
	public void setColWidthIgnored(boolean colWidthIgnored)
	{
		this.colWidthIgnored = colWidthIgnored;
	}

	public JohnGridLayout()
	{
		this(1, 0);
	}

	public JohnGridLayout(int rows, int cols)
	{
		this(rows, cols, 0);
	}

	public JohnGridLayout(int rows, int cols, int gaps)
	{
		this(rows, cols, gaps, gaps);
	}
	
	public JohnGridLayout(int rows, int cols, int hgap, int vgap)
	{
		this(rows, cols, hgap, vgap, new Insets(vgap, hgap, vgap, hgap));
	}
	
	public JohnGridLayout(int rows,int cols, int gaps,int[] colScale)
	{
		this(rows, cols, gaps, gaps, colScale);
	}

	public JohnGridLayout(int rows, int cols, int hgap, int vgap, int insets)
	{
		this(rows, cols, hgap, vgap, new Insets(insets, insets, insets, insets));
	}
	
	public JohnGridLayout(int rows, int cols, int hgap, int vgap, int[] colScale)
	{
		this(rows, cols, hgap, vgap, new Insets(vgap, hgap, vgap, hgap), null, colScale);
	}
	
	public JohnGridLayout(int rows, int cols, int hgap, int vgap, int insets, int[] colScale)
	{
		this(rows, cols, hgap, vgap, new Insets(insets, insets, insets, insets), null, colScale);
	}
	
	public JohnGridLayout(int rows, int cols, int hgap, int vgap, int hinsets, int vinsets, int[] colScale)
	{
		this(rows, cols, hgap, vgap, new Insets(vinsets, hinsets, vinsets, hinsets), null, colScale);
	}
	
	public JohnGridLayout(int rows, int cols, int hgap, int vgap, Insets insets)
	{
		this(rows, cols, hgap, vgap, insets, null, null);
	}
	
	public JohnGridLayout(int rows, int cols, int hgap, int vgap, int[] rowScale, int[] colScale)
	{
		this(rows, cols, hgap, vgap, new Insets(0,0,0,0), rowScale, colScale);
	}

	/**
	 * 行列数不能都为零
	 * @param rows 行数
	 * @param cols 列数
	 * @param hgap 组件间水平间隙
	 * @param vgap 组件间垂直间隙
	 * @param insets 四周间隙
	 * @param rowScale 各行比例
	 * @param colScale 各列比例
	 */
	public JohnGridLayout(int rows, int cols, int hgap, int vgap, Insets insets,int[] rowScale,int[] colScale)
	{
		if ((rows == 0) && (cols == 0))
			throw new IllegalArgumentException(
					"rows and cols cannot both be zero");
		this.rows = rows;
		this.cols = cols;
		this.hgap = hgap;
		this.vgap = vgap;
		
		if(insets!=null)
		{
			this.insets = insets;
		}else{
			this.insets = new Insets(0,0,0,0);
		}
		this.rowScale=rowScale;
		this.colScale=colScale;
	}
	
	/**
	 * 求int数组的值的和
	 */
	public int sumArrayValue(int[] arr)
	{
		int sum=0;
		for(int i=0;i<arr.length;i++)
		{
			sum+=arr[i];
		}
		return sum;
	}
	
	/**
	 * 填充默认的1:1比例的行列属性
	 */
	public int[] defaultScale(int size)
	{
		int[] scale=new int[size];
		for(int i=0;i<scale.length;i++)
		{
			scale[i]=1;
		}
		return scale;
	}
	
	public int getRows()
	{
		return this.rows;
	}

	public void setRows(int rows)
	{
		if ((rows == 0) && (this.cols == 0))
			throw new IllegalArgumentException(
					"rows and cols cannot both be zero");
		this.rows = rows;
	}

	public int getCols()
	{
		return this.cols;
	}

	public void setCols(int cols)
	{
		if ((cols == 0) && (this.rows == 0))
			throw new IllegalArgumentException(
					"rows and cols cannot both be zero");
		this.cols = cols;
	}

	public int getHgap()
	{
		return this.hgap;
	}

	public void setHgap(int hgap)
	{
		this.hgap = hgap;
	}

	public int getVgap()
	{
		return this.vgap;
	}

	public void setVgap(int vgap)
	{
		this.vgap = vgap;
	}

	public Insets getInsets()
	{
		return insets;
	}

	public void setInsets(Insets insets)
	{
		if(insets==null)
		{
			this.insets=new Insets(0, 0, 0, 0);
		}else{
			this.insets = insets;	
		}
	}

	public int[] getRowScale()
	{
		return rowScale;
	}

	public void setRowScale(int[] rowScale)
	{
		this.rowScale = rowScale;
	}

	public int[] getColScale()
	{
		return colScale;
	}

	public void setColScale(int[] colScale)
	{
		this.colScale = colScale;
	}

	public void addLayoutComponent(String paramString, Component paramComponent)
	{
	}

	public void removeLayoutComponent(Component paramComponent)
	{
	}

	public Dimension preferredLayoutSize(Container c)
	{
		synchronized (c.getTreeLock())
		{
			Insets localInsets = c.getInsets();
			int i = c.getComponentCount();
			int j = this.rows;
			int k = this.cols;
			if (j > 0)
				k = (i + j - 1) / j;
			else
				j = (i + k - 1) / k;
			int l = 0;
			int i1 = 0;
			for (int i2 = 0; i2 < i; ++i2)
			{
				Component localComponent = c.getComponent(i2);
				Dimension localDimension = localComponent.getPreferredSize();
				if (l < localDimension.width)
					l = localDimension.width;
				if (i1 >= localDimension.height)
					continue;
				i1 = localDimension.height;
			}
			return new Dimension(localInsets.left + insets.left
					+ localInsets.right + insets.right + k * l + (k - 1)
					* this.hgap, localInsets.top + insets.top
					+ localInsets.bottom + insets.bottom + j * i1 + (j - 1)
					* this.vgap);
		}
	}

	public Dimension minimumLayoutSize(Container c)
	{
		synchronized (c.getTreeLock())
		{
			Insets localInsets = c.getInsets();
			int i = c.getComponentCount();
			int j = this.rows;
			int k = this.cols;
			if (j > 0)
				k = (i + j - 1) / j;
			else
				j = (i + k - 1) / k;
			int l = 0;
			int i1 = 0;
			for (int i2 = 0; i2 < i; ++i2)
			{
				Component localComponent = c.getComponent(i2);
				Dimension localDimension = localComponent.getMinimumSize();
				if (l < localDimension.width)
					l = localDimension.width;
				if (i1 >= localDimension.height)
					continue;
				i1 = localDimension.height;
			}
			return new Dimension(localInsets.left + insets.left
					+ localInsets.right + insets.right + k * l + (k - 1)
					* this.hgap, localInsets.top + insets.top
					+ localInsets.bottom + insets.bottom + j * i1 + (j - 1)
					* this.vgap);
		}
	}

	public void layoutContainer(Container c)
	{
		synchronized (c.getTreeLock())
		{
			Insets localInsets = c.getInsets();
			int compCount = c.getComponentCount();
			int rows = this.rows;
			int cols = this.cols;
			if (compCount == 0)
				return;
			if (rows > 0)
				cols = (compCount + rows - 1) / rows;
			else
				rows = (compCount + cols - 1) / cols;
			if(rowScale==null)
			{
				rowScale=defaultScale(rows);
			}
			if(colScale==null)
			{
				colScale=defaultScale(cols);
			}
			int cWidth = c.getWidth()
					- (localInsets.left + insets.left + localInsets.right + insets.right);
			int cHeight = c.getHeight()
					- (localInsets.top + insets.top + localInsets.bottom + insets.bottom);
			int compWidth = cWidth;
			int compHeight = cHeight;
			int iLeft, irow, iTop, iComp, icol=0;
			iLeft = localInsets.left + insets.left;
			while (icol < cols)
			{
				irow = 0;
				iTop = localInsets.top + insets.top;
				while (irow < rows)
				{
					iComp = irow * cols + icol;
					if(colWidthIgnored)
					{
						compWidth = c.getComponent(iComp).getPreferredSize().width;
					}else{
						compWidth = ((cWidth - ((cols - 1) * this.hgap)) * colScale[icol])
								/ sumArrayValue(colScale);
					}
					if(rowHeightIgnored)
					{
						compHeight = c.getComponent(iComp).getPreferredSize().height;
					}else{
						compHeight = ((cHeight - ((rows - 1) * this.vgap)) * rowScale[irow])
								/ sumArrayValue(rowScale);
					}
					
					if (iComp < compCount)
						c.getComponent(iComp).setBounds(iLeft, iTop, compWidth, compHeight);
					++irow;
					iTop += compHeight + this.vgap;
				}
				++icol;
				iLeft += compWidth + this.hgap;
			}
		}
	}

	public String toString()
	{
		return super.getClass().getName() + "[hgap=" + this.hgap + ",vgap="
				+ this.vgap + ",rows=" + this.rows + ",cols=" + this.cols
				+ ",insets=[top:" + insets.top + ",left:" + insets.left
				+ ",bottom:" + insets.bottom + ",right:" + insets.right + "]]";
	}
}
