package com.johnsoft.library.swing.component.filetree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class JohnFileTreeCellRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		String name=((JohnFileTreeNode)value).getNodeFile().getName();	
		return super.getTreeCellRendererComponent(tree, name, sel, expanded, leaf,
				row, hasFocus);
	}

}
