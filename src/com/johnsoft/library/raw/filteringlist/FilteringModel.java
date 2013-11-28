package com.johnsoft.library.raw.filteringlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class FilteringModel extends AbstractListModel implements
		DocumentListener
{
	private static final long serialVersionUID = 1L;
	protected List<Object> list;

	protected List<Object> filteredList;

	String lastFilter = "";

	protected FilteringJList owner = null;

	public FilteringModel()
	{
		this(null);

	}

	public FilteringModel(FilteringJList father)
	{
		this.owner = father;
		list = new ArrayList<Object>();
		filteredList = new ArrayList<Object>();
	}

	public void addElement(Object element)
	{
		list.add(element);
		filter(lastFilter);
	}

	public void clear()
	{
		list.clear();
		filteredList.clear();
	}

	@Override
	public int getSize()
	{

		return filteredList.size();
	}

	@Override
	public Object getElementAt(int index)
	{
		Object returnValue;
		if (index < filteredList.size())
		{
			returnValue = filteredList.get(index);
		} else
		{
			returnValue = null;
		}
		return returnValue;
	}

	public void filter(String search)
	{
		filteredList.clear();

		for (Object element : list)
		{

			if (element.toString().toUpperCase()
					.startsWith(search.toUpperCase(), 0))
			{
				filteredList.add(element);
				continue;
			}
			int index = element.toString().indexOf("-");
			if (index != -1
					&& element.toString().toUpperCase()
							.startsWith(search, index + 1))
			{
				filteredList.add(element);
				continue;
			}
		}
		Collections.sort(filteredList, new Comparator<Object>()
		{

			public int compare(Object o1, Object o2)
			{

				return o1.toString().compareTo(o2.toString());
			}

		});

		fireContentsChanged(this, 0, getSize());

		if (owner != null)
		{
			if (owner.getModel().getSize() > 0)
			{
				owner.setSelectedIndex(0);
			} else
			{

			}
		}
	}

	public void insertUpdate(DocumentEvent event)
	{
		Document doc = event.getDocument();

		try
		{

			lastFilter = doc.getText(0, doc.getLength());

			filter(lastFilter);

		} catch (BadLocationException ble)
		{
			ble.printStackTrace();
		}
	}

	public void removeUpdate(DocumentEvent event)
	{
		Document doc = event.getDocument();
		try
		{
			lastFilter = doc.getText(0, doc.getLength());
			filter(lastFilter);

		} catch (BadLocationException ble)
		{
			ble.printStackTrace();
		}
	}

	public void changedUpdate(DocumentEvent event)
	{

	}
}