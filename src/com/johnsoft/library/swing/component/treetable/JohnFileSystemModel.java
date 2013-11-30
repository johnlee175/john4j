package com.johnsoft.library.swing.component.treetable;

import java.io.File;
import java.util.Date;


/**
 * FileSystemModel is a TreeTableModel representing a hierarchical file 
 * system. Nodes in the FileSystemModel are FileNodes which, when they 
 * are directory nodes, cache their children to avoid repeatedly querying 
 * the real file system. 
 */
public class JohnFileSystemModel extends JohnAbstractTreeTableModel
{
	// Names of the columns.
	protected static String[] cNames = { "Name", "Size", "Type", "Modified" };

	// Types of the columns.
	protected static Class<?>[] cTypes = { JohnTreeTableModel.class,
			Integer.class, String.class, Date.class };

	// The the returned file length for directories.
	public static final Integer ZERO = new Integer(0);

	public JohnFileSystemModel()
	{
		super(new JohnFileNode(new File(File.separator)));
	}

	// Some convenience methods.

	protected File getFile(Object node)
	{
		JohnFileNode fileNode = ((JohnFileNode) node);
		return fileNode.getFile();
	}

	protected Object[] getChildren(Object node)
	{
		JohnFileNode fileNode = ((JohnFileNode) node);
		return fileNode.getChildren();
	}

	// The TreeModel interface

	public int getChildCount(Object node)
	{
		Object[] children = getChildren(node);
		return (children == null) ? 0 : children.length;
	}

	public Object getChild(Object node, int i)
	{
		return getChildren(node)[i];
	}
	
	public int getColumnCount()
	{
		return cNames.length;
	}
	
	public Object getValueAt(Object node, int column)
	{
		File file = getFile(node);
		try
		{
			switch (column)
			{
			case 0:
				return file.getName();
			case 1:
				return file.isFile() ? new Integer((int) file.length()) : ZERO;
			case 2:
				return file.isFile() ? "File" : "Directory";
			case 3:
				return new Date(file.lastModified());
			}
		} catch (SecurityException se)
		{
			se.printStackTrace();
		}
		return null;
	}
	
	//override super method
	
	@Override
	public boolean isLeaf(Object node)
	{
		return getFile(node).isFile();
	}

	@Override
	public String getColumnName(int column)
	{
		return cNames[column];
	}

	@Override
	public Class<?> getColumnClass(int column)
	{
		return cTypes[column];
	}
}

