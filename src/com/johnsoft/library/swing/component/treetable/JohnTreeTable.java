package com.johnsoft.library.swing.component.treetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;
/**
 * This example shows how to create a simple JTreeTable component, 
 * by using a JTree as a renderer (and editor) for the cells in a 
 * particular column in the JTable.
 */
public class JohnTreeTable extends JTable
{
	private static final long serialVersionUID = 1L;
	/**
	 *  A subclass of JTree. 
	 */
	protected TreeTableCellRenderer tree;
	
	protected boolean isNeedIcon=false;

	public JohnTreeTable(JohnTreeTableModel treeTableModel,boolean isNeedIcon)
	{
		super();
		
		this.isNeedIcon=isNeedIcon;

		// Create the tree. It will be used as a renderer and editor.
		tree = new TreeTableCellRenderer(treeTableModel);

		// Install a tableModel representing the visible rows in the tree.
		super.setModel(new JohnTreeTableModelAdapter(treeTableModel, tree));

		// Force the JTable and JTree to share their row selection models.
		ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
		tree.setSelectionModel(selectionWrapper);
		setSelectionModel(selectionWrapper.getListSelectionModel());

		// Install the tree editor renderer and editor.
		setDefaultRenderer(JohnTreeTableModel.class, tree);
		setDefaultEditor(JohnTreeTableModel.class, new TreeTableCellEditor());

		initDefaults();
	}
	
	protected void initDefaults()
	{
		// No grid.
		setShowGrid(false);
		
		// remove hasFocus border
		setFocusable(false);
		
		//fill scrollPane with table background
		setFillsViewportHeight(true);
		
		// No interCell spacing
		setIntercellSpacing(new Dimension(0, 0));

		// And update the height of the trees row to match that of
		// the table.
		if (tree.getRowHeight() < 1)
		{
			// Metal looks better like this.
			setRowHeight(18);
		}
		
		if(!isNeedIcon)
		{
			tree.setRootVisible(false);
			//show the connection icon use for expanded or collapse
			tree.setShowsRootHandles(true);
			
			String laf=UIManager.getLookAndFeel().getName().toLowerCase();
			if(laf.indexOf("windows")>=0)
			{
				tree.setUI(new WindowsTreeUI(){
					@Override
					protected void paintHorizontalPartOfLeg(Graphics g,
							Rectangle clipBounds, Insets insets, Rectangle bounds,
							TreePath path, int row, boolean isExpanded,
							boolean hasBeenExpanded, boolean isLeaf)
					{
						return;
					}
					@Override
					protected void paintVerticalPartOfLeg(Graphics g,
							Rectangle clipBounds, Insets insets, TreePath path)
					{
						return;
					}
				});
			}
			else if(laf.indexOf("metal")>=0)
			{
				tree.setUI(new BasicTreeUI(){
					@Override
					protected void paintHorizontalPartOfLeg(Graphics g,
							Rectangle clipBounds, Insets insets, Rectangle bounds,
							TreePath path, int row, boolean isExpanded,
							boolean hasBeenExpanded, boolean isLeaf)
					{
						return;
					}
					@Override
					protected void paintVerticalPartOfLeg(Graphics g,
							Rectangle clipBounds, Insets insets, TreePath path)
					{
						return;
					}
				});
			}
			TreeCellRenderer tcr = tree.getCellRenderer();
			if (tcr instanceof DefaultTreeCellRenderer)
			{
				DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
				// For 1.1 uncomment this, 1.2 has a bug that will cause an
				// exception to be thrown if the border selection color is null.
				dtcr.setOpaque(true);
				
				dtcr.setTextSelectionColor(UIManager
						.getColor(getSelectionForeground()));
				dtcr.setTextNonSelectionColor(getForeground());
				
				dtcr.setBackgroundNonSelectionColor(UIManager
						.getColor("Table.background"));
				dtcr.setBackgroundSelectionColor(UIManager
						.getColor("Table.selectionBackground"));
				
				if(!isNeedIcon)
				{
					dtcr.setLeafIcon(null);
					dtcr.setClosedIcon(null);
					dtcr.setOpenIcon(null);
				}
			}
		}
	}
	
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
		if(tree!=null)
		{
			tree.setFont(font);
		}
	}
	
	/**
	 * Overridden to message super and forward the method to the tree. Since the
	 * tree is not actually in the component hieachy it will never receive this
	 * unless we forward it in this manner.
	 */
	public void updateUI()
	{
		super.updateUI();
		if (tree != null)
		{
			tree.updateUI();
		}
	}

	/**
	 * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
	 * paint the editor. The UI currently uses different techniques to paint the
	 * renderers and editors and overriding setBounds() below is not the right
	 * thing to do for an editor. Returning -1 for the editing row in this case,
	 * ensures the editor is never painted.
	 */
	public int getEditingRow()
	{
		return (getColumnClass(editingColumn) == JohnTreeTableModel.class) ? -1 : editingRow;
	}

	/**
	 * Overridden to pass the new rowHeight to the tree.
	 */
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if (tree != null && tree.getRowHeight() != rowHeight)
		{
			tree.setRowHeight(getRowHeight());
		}
	}

	/**
	 * Returns the tree that is being shared between the model.
	 */
	public JTree getTree()
	{
		return tree;
	}

	/**
	 * A TreeCellRenderer that displays a JTree.
	 */
	public class TreeTableCellRenderer extends JTree implements
			TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		/** 
		 * Last table/tree row asked to renderer.
		 */
		protected int visibleRow;

		public TreeTableCellRenderer(TreeModel model)
		{
			super(model);
		}

		/**
		 * updateUI is overridden to set the colors of the Tree's renderer to
		 * match that of the table.
		 */
		public void updateUI()
		{
			super.updateUI();
			// Make the tree's cell renderer use the table's cell selection colors.
			TreeCellRenderer tcr = getCellRenderer();
			if (tcr instanceof DefaultTreeCellRenderer)
			{
				DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
				// For 1.1 uncomment this, 1.2 has a bug that will cause an
				// exception to be thrown if the border selection color is null.
				dtcr.setOpaque(true);
				
				dtcr.setTextSelectionColor(UIManager
						.getColor(getSelectionForeground()));
				dtcr.setTextNonSelectionColor(getForeground());
				
				dtcr.setBackgroundNonSelectionColor(UIManager
						.getColor("Table.background"));
				dtcr.setBackgroundSelectionColor(UIManager
						.getColor("Table.selectionBackground"));
				
				if(!isNeedIcon)
				{
					dtcr.setLeafIcon(null);
					dtcr.setClosedIcon(null);
					dtcr.setOpenIcon(null);
				}
			}
		}

		/**
		 * Sets the row height of the tree, and forwards the row height to the
		 * table.
		 */
		public void setRowHeight(int rowHeight)
		{
			if (rowHeight > 0)
			{
				super.setRowHeight(rowHeight);
				if (JohnTreeTable.this != null
						&& JohnTreeTable.this.getRowHeight() != rowHeight)
				{
					JohnTreeTable.this.setRowHeight(getRowHeight());
				}
			}
		}

		/**
		 * This is overridden to set the height to match that of the JTable.
		 */
		public void setBounds(int x, int y, int w, int h)
		{
			super.setBounds(x, 0, w, JohnTreeTable.this.getHeight());
		}

		/**
		 * Sublcassed to translate the graphics such that the last visible row
		 * will be drawn at 0,0.
		 */
		public void paint(Graphics g)
		{
			g.translate(0, -visibleRow * getRowHeight());
			super.paint(g);
		}

		/**
		 * TreeCellRenderer method. Overridden to update the visible row.
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			if (isSelected)
			{
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			}else{
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}
	
			visibleRow = row;
			return this;
		}
	}

	/**
	 * TreeTableCellEditor implementation. Component returned is the JTree.
	 */
	public class TreeTableCellEditor extends AbstractCellEditor implements
			TableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int r, int c)
		{
			return tree;
		}

		/**
		 * Overridden to return false, and if the event is a mouse event it is
		 * forwarded to the tree.
		 * <p>
		 * The behavior for this is debatable, and should really be offered as a
		 * property. By returning false, all keyboard actions are implemented in
		 * terms of the table. By returning true, the tree would get a chance to
		 * do something with the keyboard events. For the most part this is ok.
		 * But for certain keys, such as left/right, the tree will
		 * expand/collapse where as the table focus should really move to a
		 * different column. Page up/down should also be implemented in terms of
		 * the table. By returning false this also has the added benefit that
		 * clicking outside of the bounds of the tree node, but still in the
		 * tree column will select the row, whereas if this returned true that
		 * wouldn't be the case.
		 * <p>
		 * By returning false we are also enforcing the policy that the tree
		 * will never be editable (at least by a key sequence).
		 */
		public boolean isCellEditable(EventObject e)
		{
			if (e instanceof MouseEvent)
			{
				for (int counter = getColumnCount() - 1; counter >= 0; counter--)
				{
					if (getColumnClass(counter) == JohnTreeTableModel.class)
					{
						MouseEvent me = (MouseEvent) e;
						MouseEvent newME = new MouseEvent(tree, me.getID(),
								me.getWhen(), me.getModifiers(), me.getX()
										- getCellRect(0, counter, true).x,
								me.getY(), me.getClickCount(),
								me.isPopupTrigger());
						tree.dispatchEvent(newME);
						break;
					}
				}
			}
			return false;
		}

		public Object getCellEditorValue()
		{
			return null;
		}
		
		public boolean shouldSelectCell(EventObject anEvent)
		{
			return false;
		}
	}

	/**
	 * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel to
	 * listen for changes in the ListSelectionModel it maintains. Once a change
	 * in the ListSelectionModel happens, the paths are updated in the
	 * DefaultTreeSelectionModel.
	 */
	public class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
	{
		private static final long serialVersionUID = 1L;
		/** 
		 * Set to true when we are updating the ListSelectionModel. 
		 */
		protected boolean updatingListSelectionModel;

		public ListToTreeSelectionModelWrapper()
		{
			super();
			getListSelectionModel().addListSelectionListener(
					createListSelectionListener());
		}

		/**
		 * Returns the list selection model. ListToTreeSelectionModelWrapper
		 * listens for changes to this model and updates the selected paths
		 * accordingly.
		 */
		public ListSelectionModel getListSelectionModel()
		{
			return listSelectionModel;
		}

		/**
		 * This is overridden to set <code>updatingListSelectionModel</code> and
		 * message super. This is the only place DefaultTreeSelectionModel
		 * alters the ListSelectionModel.
		 */
		public void resetRowSelection()
		{
			if (!updatingListSelectionModel)
			{
				updatingListSelectionModel = true;
				try
				{
					super.resetRowSelection();
				} finally
				{
					updatingListSelectionModel = false;
				}
			}
			// Notice how we don't message super if
			// updatingListSelectionModel is true. If
			// updatingListSelectionModel is true, it implies the
			// ListSelectionModel has already been updated and the
			// paths are the only thing that needs to be updated.
		}

		/**
		 * Creates and returns an instance of ListSelectionHandler.
		 */
		protected ListSelectionListener createListSelectionListener()
		{
			return new ListSelectionHandler();
		}

		/**
		 * If <code>updatingListSelectionModel</code> is false, this will reset
		 * the selected paths from the selected rows in the list selection
		 * model.
		 */
		protected void updateSelectedPathsFromSelectedRows()
		{
			if (!updatingListSelectionModel)
			{
				updatingListSelectionModel = true;
				try
				{
					// This is way expensive, ListSelectionModel needs an enumerator for iterating.
					int min = listSelectionModel.getMinSelectionIndex();
					int max = listSelectionModel.getMaxSelectionIndex();

					clearSelection();
					if (min != -1 && max != -1)
					{
						for (int counter = min; counter <= max; counter++)
						{
							if (listSelectionModel.isSelectedIndex(counter))
							{
								TreePath selPath = tree.getPathForRow(counter);

								if (selPath != null)
								{
									addSelectionPath(selPath);
								}
							}
						}
					}
				} finally
				{
					updatingListSelectionModel = false;
				}
			}
		}

		/**
		 * Class responsible for calling updateSelectedPathsFromSelectedRows
		 * when the selection of the list changse.
		 */
		public class ListSelectionHandler implements ListSelectionListener
		{
			public void valueChanged(ListSelectionEvent e)
			{
				updateSelectedPathsFromSelectedRows();
			}
		}
	}
	
}