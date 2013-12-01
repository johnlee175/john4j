package com.johnsoft.library.swing.component.filetree;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class JohnFileTreeNode extends DefaultMutableTreeNode
{
	private static final long serialVersionUID = 1L;
	
	public JohnFileTreeNode(File file)
	{
		super(file);
	}
	
	public File getNodeFile()
	{
		return (File)super.getUserObject();
	}
	
	public boolean isFolder()
	{
		return getNodeFile().isDirectory();
	}
	
	@Override
	public boolean getAllowsChildren()
	{
		return isFolder();
	}
	
	@Override
	public void setAllowsChildren(boolean allows)
	{
		if(!isFolder())
		{
			return;
		}
		super.setAllowsChildren(allows);
	}
	
	@Override
	public boolean isLeaf()
	{
		return !isFolder();
	}

}
