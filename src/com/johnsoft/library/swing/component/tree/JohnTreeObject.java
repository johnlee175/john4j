package com.johnsoft.library.swing.component.tree;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class JohnTreeObject
{
	private ImageIcon treeIcon;
	private ImageIcon openIcon;
	private ImageIcon closeIcon;
	private String treeName;
	private Color selectTextColor;
	private Color unSelectTextColor;
	private Color selectBGColor;
	private Color unSelectBGColor;
	private List<String[]> treeText=new ArrayList<String[]>();
	
	public ImageIcon getTreeIcon()
	{
		return treeIcon;
	}
	public void setTreeIcon(ImageIcon treeIcon)
	{
		this.treeIcon = treeIcon;
	}
	public ImageIcon getOpenIcon()
	{
		return openIcon;
	}
	public void setOpenIcon(ImageIcon openIcon)
	{
		this.openIcon = openIcon;
	}
	public ImageIcon getCloseIcon()
	{
		return closeIcon;
	}
	public void setCloseIcon(ImageIcon closeIcon)
	{
		this.closeIcon = closeIcon;
	}
	public String getTreeName()
	{
		return treeName;
	}
	public void setTreeName(String treeName)
	{
		this.treeName = treeName;
	}
	public Color getSelectTextColor()
	{
		return selectTextColor;
	}
	public void setSelectTextColor(Color selectTextColor)
	{
		this.selectTextColor = selectTextColor;
	}
	public Color getUnSelectTextColor()
	{
		return unSelectTextColor;
	}
	public void setUnSelectTextColor(Color unSelectTextColor)
	{
		this.unSelectTextColor = unSelectTextColor;
	}
	public Color getSelectBGColor()
	{
		return selectBGColor;
	}
	public void setSelectBGColor(Color selectBGColor)
	{
		this.selectBGColor = selectBGColor;
	}
	public Color getUnSelectBGColor()
	{
		return unSelectBGColor;
	}
	public void setUnSelectBGColor(Color unSelectBGColor)
	{
		this.unSelectBGColor = unSelectBGColor;
	}
	public List<String[]> getTreeText()
	{
		return treeText;
	}
	public void setTreeText(List<String[]> treeText)
	{
		this.treeText = treeText;
	}
  
	
}
