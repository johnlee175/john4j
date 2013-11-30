package com.johnsoft.library.swing.component.treetable;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * This is a wrapper class takes a TreeTableModel and implements 
 * the table model interface. The implementation is trivial, with 
 * all of the event dispatching support provided by the superclass: 
 * the AbstractTableModel. 
 */
public class JohnTreeTableModelAdapter extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	protected JTree tree;
	protected JohnTreeTableModel treeTableModel;

	public JohnTreeTableModelAdapter(JohnTreeTableModel treeTableModel, JTree tree)
	{
		this.tree = tree;
		this.treeTableModel = treeTableModel;
		
		installListeners();
	}

	// Wrappers, implementing TableModel interface.

	public int getColumnCount()
	{
		return treeTableModel.getColumnCount();
	}

	public String getColumnName(int column)
	{
		return treeTableModel.getColumnName(column);
	}

	public Class<?> getColumnClass(int column)
	{
		return treeTableModel.getColumnClass(column);
	}
	
	public boolean isCellEditable(int row, int column)
	{
		return treeTableModel.isCellEditable(nodeForRow(row), column);
	}

	public Object getValueAt(int row, int column)
	{
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}

	public void setValueAt(Object value, int row, int column)
	{
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}
	
	public int getRowCount()
	{
		return tree.getRowCount();
	}
	
	//Helper methods
	
	protected void installListeners()
	{
		tree.addTreeExpansionListener(new TreeExpansionListener()
		{
			// Don't use fireTableRowsInserted() here; the selection model would get updated twice.
			public void treeExpanded(TreeExpansionEvent event)
			{
				fireTableDataChanged();
			}

			public void treeCollapsed(TreeExpansionEvent event)
			{
				fireTableDataChanged();
			}
		});

		// Install a TreeModelListener that can update the table when
		// tree changes. We use delayedFireTableDataChanged as we can
		// not be guaranteed the tree will have finished processing
		// the event before us.
		treeTableModel.addTreeModelListener(new TreeModelListener()
		{
			public void treeNodesChanged(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}

			public void treeNodesInserted(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}

			public void treeNodesRemoved(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}

			public void treeStructureChanged(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}
		});
	}
	
	//Get row from tree
	protected Object nodeForRow(int row)
	{
		return tree.getPathForRow(row).getLastPathComponent();
	}

	/*
	 * Invokes fireTableDataChanged after all the pending events have been
	 * processed. SwingUtilities.invokeLater is used to handle this.
	 */
	protected void delayedFireTableDataChanged()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				fireTableDataChanged();
			}
		});
	}
	
}