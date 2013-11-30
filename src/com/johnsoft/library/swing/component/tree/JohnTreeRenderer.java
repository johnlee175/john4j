package com.johnsoft.library.swing.component.tree;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class JohnTreeRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
				hasFocus);
		if(value instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)value;
			Object obj=node.getUserObject();
			if(obj instanceof JohnTreeObject)
			{
				    JohnTreeObject jxto=(JohnTreeObject)obj;
				    String treename = jxto.getTreeName();
				    Icon treeIcon = jxto.getTreeIcon();
				    Icon openIcon=jxto.getOpenIcon();
				    Icon closeIcon=jxto.getCloseIcon();
				    Color unSelectBGColor=jxto.getUnSelectBGColor();
				    Color selectBGColor=jxto.getSelectBGColor();
				    Color selectTextColor= jxto.getSelectTextColor();
				    Color unSelectTextColor= jxto.getUnSelectTextColor();
				    List<String[]>  treetext = jxto.getTreeText();
				    if(treeIcon!=null)
						{
							if (leaf)
							{
								setIcon(treeIcon);
							} 
							else if (expanded)
							{
								if(openIcon!=null)
								{
									setIcon(openIcon);
								}else{
									setIcon(treeIcon);
								}	
							} 
							else
							{
								if(closeIcon!=null)
								{
									setIcon(closeIcon);
								}else{
									setIcon(treeIcon);
								}
							}
						}
				    if(treetext.size()==0)
				    {
				    	if(selectTextColor!=null)
				    	{
				    		setTextSelectionColor(selectTextColor);
				    	}
				    	if(unSelectTextColor!=null)
				    	{
				    		setTextNonSelectionColor(unSelectTextColor);
				    	}
				    	if(selectBGColor!=null)
				    	{
				    		setBackgroundSelectionColor(selectBGColor);
				    	}
				      if(unSelectBGColor!=null)
				      {
				        setBackgroundNonSelectionColor(unSelectBGColor);
				      }
				      setText(treename);
				    }
				    else
				    {
				    	StringBuffer sb=new StringBuffer("<HTML>");
				    	for(int i=0;i<treetext.size();i++)
				    	{
				    		String[] strs=treetext.get(i);
				    			if(strs.length==1)
				    			{
				    				sb.append("<span>").append(strs[0]).append("</span>");
				    			}
				    			else if(strs.length==2)
				    			{
				    				sb.append("<span style='color:").append(strs[1]).append("'>")
				    					.append(strs[0]).append("<span>");
				    			}
				    			else
				    			{
				    			   continue;
				    			}
				    		}
				    	setText(sb.append("</HTML>").toString());
				    }
			   }
			}
		return this;
	}
	
	
	
	
	
	
	
}
