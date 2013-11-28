package com.johnsoft.library.raw;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SortFilterTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	private TableModel model = null;

	private boolean[] sortAsc = null;

	private int sortColumn;

	private Row[] rows;

	public SortFilterTableModel(TableModel model)
	{
		this.model = model;
		this.sortAsc = new boolean[this.model.getColumnCount()];
		Arrays.fill(this.sortAsc, false);

		rows = new Row[model.getRowCount()];
		for (int i = 0; i < rows.length; i++)
		{
			rows[i] = new Row();
			rows[i].index = i;
		}
	}
	
	public void sort(int c)
	{
		sortColumn = c;
		sortAsc[c] = sortAsc[c] ^ true;
		Arrays.sort(rows);
		fireTableDataChanged();
	}

	public int getColumnCount()
	{
		return model.getColumnCount();
	}

	public int getRowCount()
	{
		return model.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return model.getValueAt(rows[rowIndex].index, columnIndex);
	}

	private class Row implements Comparable<Row>
	{
		public int index;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public int compareTo(Row other)
		{
			Object a = model.getValueAt(index, sortColumn);
			Object b = model.getValueAt(other.index, sortColumn);
			if (a instanceof Comparable)
			{
				return ((Comparable) a).compareTo(b);
			} else
			{
				return a.toString().compareTo(b.toString());
			}
		}
	}
}
