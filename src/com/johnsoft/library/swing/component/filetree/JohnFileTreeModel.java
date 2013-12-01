package com.johnsoft.library.swing.component.filetree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class JohnFileTreeModel extends DefaultTreeModel
{
	private static final long serialVersionUID = 1L;

	public JohnFileTreeModel(TreeNode root)
	{
		super(root);
		if(!(root instanceof JohnFileTreeNode))
		{
			throw new RuntimeException("it is work for JohnFileTreeNode");
		}
	}
	
}
