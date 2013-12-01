package com.johnsoft.library.swing.component.filetree;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class JohnFileTreeCellEditor extends DefaultTreeCellEditor
	{
		protected boolean isEditFromPopup=false;
		protected JohnFileTree fileTree;
		
		public JohnFileTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer)
		{
			super(tree, renderer);
			fileTree=(JohnFileTree)tree;
		}
		
		@Override
		public Component getTreeCellEditorComponent(JTree tree, Object value,
				boolean isSelected, boolean expanded, boolean leaf, int row)
		{
		  String name=((JohnFileTreeNode)value).getNodeFile().getName();
			return super.getTreeCellEditorComponent(tree, name, isSelected, expanded,
					leaf, row);
		}
		
		@Override
		public Object getCellEditorValue()
		{
			return fileTree.renameFile(super.getCellEditorValue().toString());
		}
		
		@Override
		public boolean isCellEditable(EventObject event)
		{
			return isEditFromPopup;
		}
		
		public void beginEditState(TreePath path)
		{
			isEditFromPopup=true;
			fileTree.startEditingAtPath(path);
			isEditFromPopup=false;
		}
		
	}