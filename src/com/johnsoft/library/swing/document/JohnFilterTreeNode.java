package com.johnsoft.library.swing.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public class JohnFilterTreeNode extends DefaultMutableTreeNode implements
		DocumentListener
{
	private static final long serialVersionUID = 1L;

	private String lastFilter = "";

	private List<MutableTreeNode> list = new ArrayList<MutableTreeNode>();

	private DefaultTreeModel treeModel;

	public JohnFilterTreeNode(Object node)
	{
		super(node);
	}

	public void clearTreeNode()
	{
		list.clear();
	}

	public void add(MutableTreeNode newChild)
	{
		if (newChild != null && newChild.getParent() == this)
			insert(newChild, getChildCount() - 1);
		else
			insert(newChild, getChildCount());
		list.add(newChild);
	}

	@SuppressWarnings("unchecked")
	void filter(String search)
	{
		children.removeAllElements();
		for (Object element : list)
		{

			if (element.toString().toUpperCase()
					.startsWith(search.toUpperCase()))
			{
				children.add(element);
				continue;
			}
			int index = element.toString().indexOf("-");

			if (index != -1
					&& element.toString().toUpperCase().substring(index + 1)
							.startsWith(search.toUpperCase()))
			{
				children.add(element);
				continue;
			}
		}

		Collections.sort(children, new Comparator<MutableTreeNode>()
		{
			@Override
			public int compare(MutableTreeNode o1, MutableTreeNode o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		});
		treeModel.reload(JohnFilterTreeNode.this);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{

	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		Document doc = e.getDocument();

		try
		{
			lastFilter = doc.getText(0, doc.getLength());

			filter(lastFilter);

		} catch (BadLocationException ble)
		{
			ble.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		Document doc = e.getDocument();
		try
		{
			lastFilter = doc.getText(0, doc.getLength());
			filter(lastFilter);
		} catch (BadLocationException ble)
		{
			ble.printStackTrace();
		}
	}

	public DefaultTreeModel getTreeModel()
	{
		return treeModel;
	}

	public void setTreeModel(DefaultTreeModel treeModel)
	{
		this.treeModel = treeModel;
	}

}
