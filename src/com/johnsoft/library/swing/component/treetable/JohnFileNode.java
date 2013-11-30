package com.johnsoft.library.swing.component.treetable;

import java.io.File;

/**
 * A FileNode is a derivative of the File class - though we delegate to the File
 * object rather than subclassing it. It is used to maintain a cache of a
 * directory's children and therefore avoid repeated access to the underlying
 * file system during rendering.
 */
public class JohnFileNode
{
	protected File file;
	protected Object[] children;

	public JohnFileNode(File file)
	{
		this.file = file;
	}

	public String toString()
	{
		return file.getName();
	}

	public File getFile()
	{
		return file;
	}

	protected Object[] getChildren()
	{
		if (children != null)
		{
			return children;
		}
		try
		{
			String[] files = file.list();
			if (files != null)
			{
				children = new JohnFileNode[files.length];
				String path = file.getPath();
				for (int i = 0; i < files.length; i++)
				{
					File childFile = new File(path, files[i]);
					children[i] = new JohnFileNode(childFile);
				}
			}
		} catch (SecurityException se)
		{
		}
		return children;
	}
}