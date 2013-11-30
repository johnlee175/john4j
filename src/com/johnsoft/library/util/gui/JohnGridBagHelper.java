package com.johnsoft.library.util.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

/**
 * GridBagLayout的辅助类
 * 
 * 特点概述:
 * 1.以方法链代替一行一行的赋值表达式,每个方法有多个重载,按需一次设置多个值; 
 * 2.形参名即为GridBagConstraints的属性名; 
 * 3.除了几个带Component类型形参的mixbounds和nextXXX型方法外,最后都要调用add(Component)方法完成实际的组件到容器的添加; 
 * 4.如果某个属性不想设置,请将该形参置为-9,那么它将被忽略; 
 * 5.提供了goback和forward的方法,有时我们希望在某个组件上应用新配置然后回到原配置继续使用,那么goback是个不错的选择,这是通过GridBagConstraints[]实现,默认长度为3;
 * @author 李哲浩
 */
public class JohnGridBagHelper
{
    public static final int GRID_RELATIVE = -1;
    public static final int GRID_REMAINDER = 0;
    public static final int FILL_NONE = 0;
    public static final int FILL_BOTH = 1;
    public static final int FILL_HORIZONTAL = 2;
    public static final int FILL_VERTICAL = 3;
    public static final int ANCHOR_CENTER = 10;
    public static final int ANCHOR_NORTH = 11;
    public static final int ANCHOR_NORTHEAST = 12;
    public static final int ANCHOR_EAST = 13;
    public static final int ANCHOR_SOUTHEAST = 14;
    public static final int ANCHOR_SOUTH = 15;
    public static final int ANCHOR_SOUTHWEST = 16;
    public static final int ANCHOR_WEST = 17;
    public static final int ANCHOR_NORTHWEST = 18;
    public static final int ANCHOR_PAGE_START = 19;
    public static final int ANCHOR_PAGE_END = 20;
    public static final int ANCHOR_LINE_START = 21;
    public static final int ANCHOR_LINE_END = 22;
    public static final int ANCHOR_FIRST_LINE_START = 23;
    public static final int ANCHOR_FIRST_LINE_END = 24;
    public static final int ANCHOR_LAST_LINE_START = 25;
    public static final int ANCHOR_LAST_LINE_END = 26;
    public static final int ANCHOR_BASELINE = 0x100;
    public static final int ANCHOR_BASELINE_LEADING = 0x200;
    public static final int ANCHOR_BASELINE_TRAILING = 0x300;
    public static final int ANCHOR_ABOVE_BASELINE = 0x400;
    public static final int ANCHOR_ABOVE_BASELINE_LEADING = 0x500;
    public static final int ANCHOR_ABOVE_BASELINE_TRAILING = 0x600;
    public static final int ANCHOR_BELOW_BASELINE = 0x700;
    public static final int ANCHOR_BELOW_BASELINE_LEADING = 0x800;
    public static final int ANCHOR_BELOW_BASELINE_TRAILING = 0x900;
	
	private Container container;
	private GridBagLayout layout;
	
	private int p=0;
	private int size=3;
	private GridBagConstraints[] c=new GridBagConstraints[size];
	
	public JohnGridBagHelper(){	
	}
	
	/**
	 * @param container 要安装GridBagLayout布局的面板
	 */
	public JohnGridBagHelper(Container container)
	{
		buildGridBag(container);
	}
	
	/**
	 * @param container 要安装GridBagLayout布局的面板
	 * @param o 面板中组件的布局方向
	 */
	public JohnGridBagHelper(Container container,ComponentOrientation o)
	{
		createGridBag();
		gridBag(container,o);
	}
	
	/**
	 * 初始化方法:
	 * layout = new GridBagLayout();
	 * check();
	 */
	public void createGridBag()
	{
		layout = new GridBagLayout();
		check();	
	}
	
	/**
	 * 初始化方法:
	 * this.container = container;	
	 * container.setLayout(layout);
	 * container.setComponentOrientation(o);
	 */
	public void gridBag(Container container,ComponentOrientation o)
	{
		this.container = container;	
		container.setLayout(layout);
		container.setComponentOrientation(o);
	}
	
	/**
	 * 初始化方法:
	 * this.container = container;	
	 * layout = new GridBagLayout();
	 * container.setLayout(layout);
	 * check();
	 */
	public void buildGridBag(Container container)
	{
		this.container = container;	
		layout = new GridBagLayout();
		container.setLayout(layout);
		check();
	}
	
	public void goback()
	{
		if(p!=0)p--;
	}
	
	public void forward()
	{
		if(p!=size-1)p++;
	}
	
	/**
	 * 平衡和检查GridBagConstraints数组
	 */
	protected void check()
	{
		if(p==0&&c[p]==null)
		{
			c[p]=new GridBagConstraints();
		}
		else if(p==size-1&&c[p]!=null)
		{
			for(int i=0;i<p;i++)
			{
				c[i]=c[i+1];
			}
			c[p]=(GridBagConstraints)c[p].clone();
		}
		else
		{
			c[++p]=(GridBagConstraints)c[p-1].clone();
		}
	}
	
	public GridBagConstraints mixbounds(Component comp,int fill,int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty,int ipadx,int ipady,int anchor,Insets insets)
	{
		check();
		if(fill!=-9){	c[p].fill=fill;	}
		if(gridx!=-9){	c[p].gridx = gridx;	}
		if(gridy!=-9){	c[p].gridy = gridy;	}
		if(gridwidth!=-9){	c[p].gridwidth = gridwidth;	}
		if(gridheight!=-9){	c[p].gridheight = gridheight;	}
		if(weightx!=-9){	c[p].weightx=weightx;	}
		if(weighty!=-9){	c[p].weighty=weighty;	}
		if(ipadx!=-9){	c[p].ipadx=ipadx;	}
		if(ipady!=-9){	c[p].ipady=ipady;	}
		if(anchor!=-9){	c[p].anchor=anchor;	}
		if(insets!=null){	c[p].insets=insets;	}
		if(comp!=null){	container.add(comp);	}
		return c[p];
	}
	
	public GridBagConstraints mixbounds(int fill,int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty,Insets insets)
	{
		check();
		bounds(gridx, gridy, gridwidth, gridheight);
		exbounds(weightx, weighty, -9, -9);
		othbounds(fill, -9, insets);
		return c[p];
	}
	
	public GridBagConstraints mixbounds(Component comp,int fill,int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty,Insets insets)
	{
		check();
		this.mixbounds(fill, gridx, gridy, gridwidth, gridheight, weightx, weighty, insets);
		this.add(comp);
		return c[p];
	}
	
	public GridBagConstraints mixbounds(int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty,int anchor,Insets insets)
	{
		check();
		return this.mixbounds(null,-9, gridx, gridy, gridwidth, gridheight, weightx, weighty, -9, -9, anchor, insets);
	}
	
	public GridBagConstraints mixbounds(Component comp,int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty,int anchor,Insets insets)
	{
		check();
		return this.mixbounds(comp,-9, gridx, gridy, gridwidth, gridheight, weightx, weighty, -9, -9, anchor, insets);
	}
	
	public GridBagConstraints mixbounds(Component comp,int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty)
	{
		check();
		this.mixbounds(gridx, gridy, gridwidth, gridheight, weightx, weighty);
		this.add(comp);
		return c[p];
	}
	
	public GridBagConstraints mixbounds(int gridx,int gridy,int gridwidth,int gridheight,double weightx,double weighty)
	{
		check();
		bounds(gridx, gridy, gridwidth, gridheight);
		exbounds(weightx, weighty, -9, -9);
		return c[p];
	}
	
	/**
	 * gridx+1
	 */
	public JohnGridBagHelper nextCol()
	{
		check();
		c[p].gridx=c[p].gridx+1;
		return this;
	}
	
	/**
	 * gridx+1并添加组件
	 */
	public JohnGridBagHelper nextCol(Component comp)
	{
		nextCol();
		if(comp==null)
			return addSpace();
		else
			return add(comp);
	}
	
	/**
	 * gridy+1
	 */
	public JohnGridBagHelper nextRow()
	{
		check();
		c[p].gridy=c[p].gridy+1;
		return this;
	}
	
	/**
	 * gridx+1并添加组件
	 */
	public JohnGridBagHelper nextRow(Component comp)
	{
		nextRow();
		if(comp==null)
			return addSpace();
		else
			return add(comp);
	}
	
	/**
	 * gridy+1,gridx=0
	 */
	public JohnGridBagHelper wrapLine()
	{
		check();
		c[p].gridy=c[p].gridy+1;
		c[p].gridx=0;
		return this;
	}
	
	/**
	 * gridy+1,gridx=0并添加组件
	 */
	public JohnGridBagHelper wrapLine(Component comp)
	{
		wrapLine();
		if(comp==null)
			return addSpace();
		else
			return add(comp);
	}
	
	public JohnGridBagHelper bounds(int gridx,int gridy,int gridwidth,int gridheight)
	{
		check();
		if(gridx!=-9){	c[p].gridx = gridx;	}
		if(gridy!=-9){	c[p].gridy = gridy;	}
		if(gridwidth!=-9){	c[p].gridwidth = gridwidth;	}
		if(gridheight!=-9){	c[p].gridheight = gridheight;	}
		return this;
	}
	
	public JohnGridBagHelper pos(int gridx,int gridy)
	{
		check();
		if(gridx!=-9){	c[p].gridx = gridx;	}
		if(gridy!=-9){	c[p].gridy = gridy;	}
		return this;
	}
	
	public JohnGridBagHelper size(int gridwidth,int gridheight)
	{
		check();
		if(gridwidth!=-9){	c[p].gridwidth = gridwidth;	}
		if(gridheight!=-9){	c[p].gridheight = gridheight;	}
		return this;
	}
	
	public JohnGridBagHelper exbounds(double weightx,double weighty,int ipadx,int ipady)
	{
		check();
		if(weightx!=-9){	c[p].weightx=weightx;	}
		if(weighty!=-9){	c[p].weighty=weighty;	}
		if(ipadx!=-9){	c[p].ipadx=ipadx;	}
		if(ipady!=-9){	c[p].ipady=ipady;	}
		return this;
	}
	
	public JohnGridBagHelper weight(double weightx,double weighty)
	{
		check();
		if(weightx!=-9){	c[p].weightx=weightx;	}
		if(weighty!=-9){	c[p].weighty=weighty;	}
		return this;
	}
	
	public JohnGridBagHelper ipad(int ipadx,int ipady)
	{
		check();
		if(ipadx!=-9){	c[p].ipadx=ipadx;	}
		if(ipady!=-9){	c[p].ipady=ipady;	}
		return this;
	}
	
	public JohnGridBagHelper othbounds(int fill,int anchor,Insets insets)
	{
		check();
		if(fill!=-9){	c[p].fill=fill;	}
		if(anchor!=-9){	c[p].anchor=anchor;	}
		if(insets!=null){	c[p].insets=insets;	}
		return this;
	}
	
	public JohnGridBagHelper add(Component comp)
	{
		if(comp!=null)
		{
			container.add(comp,c[p]);
		}
		return this;
	}
	
	/**添加占位符*/
	public JohnGridBagHelper addSpace()
	{
		return add(new JLabel());
	}
	
	public JohnGridBagHelper fill(int fill)
	{
		check();
		c[p].fill=fill;
		return this;
	}
	
	public JohnGridBagHelper anchor(int anchor)
	{
		check();
		c[p].anchor=anchor;
		return this;
	}
	
	public JohnGridBagHelper insets(int top,int left,int bottom,int right)
	{
		check();
		c[p].insets=new Insets(top, left, bottom, right);
		return this;
	}
	
	public JohnGridBagHelper ipadx(int ipadx)
	{
		check();
		c[p].ipadx=ipadx;
		return this;
	}
	
	public JohnGridBagHelper ipady(int ipady)
	{
		check();
		c[p].ipady=ipady;
		return this;
	}
	
	public JohnGridBagHelper weighty(double weighty)
	{
		check();
		c[p].weighty=weighty;
		return this;
	}
	
	public JohnGridBagHelper weightx(double weightx)
	{
		check();
		c[p].weightx=weightx;
		return this;
	}
	
	public JohnGridBagHelper gridx(int gridx)
	{
		check();
		c[p].gridx=gridx;
		return this;
	}
	
	public JohnGridBagHelper gridy(int gridy)
	{
		check();
		c[p].gridy=gridy;
		return this;
	}
	
	public JohnGridBagHelper gridwidth(int gridwidth)
	{
		check();
		c[p].gridwidth=gridwidth;
		return this;
	}
	
	public JohnGridBagHelper gridheight(int gridheight)
	{
		check();
		c[p].gridheight=gridheight;
		return this;
	}
	
	public Container getContainer()
	{
		return container;
	}

	public void setContainer(Container container)
	{
		this.container = container;
	}

	public GridBagLayout getLayout()
	{
		return layout;
	}

	public void setLayout(GridBagLayout layout)
	{
		this.layout = layout;
	}

	public GridBagConstraints getGridBagConstraints()
	{
		return c[p];
	}

	public void setGridBagConstraints(GridBagConstraints c)
	{
		this.c[p] = c;
	}
	
}
