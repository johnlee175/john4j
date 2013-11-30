package com.johnsoft.library.swing.component.treetable;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 * An abstract implementation of the TreeTableModel interface, handling the list
 * of listeners.
 * 
 * Left to be implemented in the subclass: 
 * public Object getChild(Object parent,int index);
 * public int getChildCount(Object parent);
 * public Object getValueAt(Object node, int column);
 * public int getColumnCount();
 * 
 * Maybe sometimes override the following methods is good idea:
 * public Class<?> getColumnClass(int column);
 * public String getColumnName(int column);
 * public boolean isCellEditable(Object node, int column);
 * public void setValueAt(Object aValue, Object node, int column);
 * public void valueForPathChanged(TreePath path, Object newValue);
 * public boolean isLeaf(Object node);
 */
public abstract class JohnAbstractTreeTableModel implements JohnTreeTableModel
{
	protected Object root;
	protected EventListenerList listenerList = new EventListenerList();

	public JohnAbstractTreeTableModel(Object root)
	{
		this.root = root;
	}

	// Default impelmentations for methods in the TreeTableModel interface.

	/**
	 * @return Object.class
	 */
	public Class<?> getColumnClass(int column)
	{
		return Object.class;
	}

	/**
	 * Implements using char like A,B,C join number like 1,2,3 
	 */
	public String getColumnName(int column)
	{
		String result = "";
		for (; column >= 0; column = column / 26 - 1)
		{
			result = (char) ((char) (column % 26) + 'A') + result;
		}
		return result;
	}

	/**
	 * By default, make the column with the Tree in it the only editable one.
	 * Making this column editable causes the JTable to forward mouse and
	 * keyboard events in the Tree column to the underlying JTree.
	 * so you must write append "||(getColumnClass(column)==JohnTreeTableModel.class)" 
	 * on your condition;
	 * @return getColumnClass(column)==JohnTreeTableModel.class
	 */
	public boolean isCellEditable(Object node, int column)
	{
		return getColumnClass(column)==JohnTreeTableModel.class;
	}

	/**
	 * Empty implements
	 */
	public void setValueAt(Object aValue, Object node, int column)
	{
	}

	// Default implmentations for methods in the TreeModel interface.

	public Object getRoot()
	{
		return root;
	}

	public boolean isLeaf(Object node)
	{
		return getChildCount(node) == 0;
	}

	/**
	 * Empty implements
	 */
	public void valueForPathChanged(TreePath path, Object newValue)
	{
	}

	// This is not called in the JTree's default mode: use a native implementation.
	public int getIndexOfChild(Object parent, Object child)
	{
		for (int i = 0; i < getChildCount(parent); i++)
		{
			if (getChild(parent, i).equals(child))
			{
				return i;
			}
		}
		return -1;
	}

	public void addTreeModelListener(TreeModelListener l)
	{
		listenerList.add(TreeModelListener.class, l);
	}

	public void removeTreeModelListener(TreeModelListener l)
	{
		listenerList.remove(TreeModelListener.class, l);
	}
	
	//fire methods for TreeModelListener Notify

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireTreeNodesChanged(Object source, Object[] path,
			int[] childIndices, Object[] children)
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying those that are
		// interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireTreeNodesInserted(Object source, Object[] path,
			int[] childIndices, Object[] children)
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying those that are
		// interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireTreeNodesRemoved(Object source, Object[] path,
			int[] childIndices, Object[] children)
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying those that are
		// interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireTreeStructureChanged(Object source, Object[] path,
			int[] childIndices, Object[] children)
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying those that are
		// interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
			}
		}
	}

}