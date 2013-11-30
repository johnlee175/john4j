package com.johnsoft.library.swing.document.colname;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class JohnColNameTableModel<T> extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	protected JohnColNameHelper helper=JohnColNameHelper.getNewInstance(getTableClass());
	
	protected List<T> list;
	
	public abstract Class<?> getTableClass();
	
	public List<T> getList()
	{
		return list;
	}
	
	@Override
	public int getRowCount()
	{
		return list.size();
	}

	@Override
	public int getColumnCount()
	{
		return helper.getColCount();
	}

	@Override
	public String getColumnName(int column)
	{
		return helper.getAttr(helper.getId(column), "cName");
	}
	
	public int i(String id)
	{
		return helper.getIndex(id);
	}

}
