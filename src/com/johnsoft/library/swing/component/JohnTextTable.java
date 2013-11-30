package com.johnsoft.library.swing.component;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * 用于长文本字段的可自动换行的简单表格
 * @author 李哲浩
 */
public class JohnTextTable extends JTable
{
	private static final long serialVersionUID = 1L;
	
	public JohnTextTable()
	{
		super();
		initDefaults();
	}
	
	public JohnTextTable(int rowNum,int colNum)
	{
		super(rowNum,colNum);
		initDefaults();
	}
	
	public JohnTextTable(String[][] data,String[] columnName)
	{
		super(data,columnName);
		initDefaults();
	}
	
	public JohnTextTable(TableModel model)
	{
		super(model);
		initDefaults();
	}
	
	protected void initDefaults()
	{
		setDefaultRenderer(Object.class, new WrapCellRenderer());
		getTableHeader().setReorderingAllowed(false);
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	public static class WrapCellRenderer extends JTextArea implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int column)
		{
		  setText(value.toString());
		  setLineWrap(true);
		  setWrapStyleWord(true);
	
		  int maxPreferredHeight = 20;
		  for(int i = 0; i < table.getColumnCount(); i++)
		  {
		   setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
		   maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
		  }
		  if(table.getRowHeight(row) != maxPreferredHeight) 
		  {
		   table.setRowHeight(row, maxPreferredHeight);
		  }
		  
		  return this;
		}
	}
	
}
