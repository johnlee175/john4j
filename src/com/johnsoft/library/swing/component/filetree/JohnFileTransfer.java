package com.johnsoft.library.swing.component.filetree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JohnFileTransfer implements Transferable
{
	protected DataFlavor[] dataFlavors = new DataFlavor[] { DataFlavor.javaFileListFlavor };
	protected List<File> files=new ArrayList<File>();
	
	public JohnFileTransfer(List<File> files)
	{
		this.files=files;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
	{
		return files;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return dataFlavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		for (int i = 0; i < dataFlavors.length; i++)
		{
			if (dataFlavors[i].equals(flavor))
			{
				return true;
			}
		}
		return false;
	}
	
}
