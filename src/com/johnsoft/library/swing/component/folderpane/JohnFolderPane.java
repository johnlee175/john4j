package com.johnsoft.library.swing.component.folderpane;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.Scrollable;


public class JohnFolderPane extends JComponent implements Scrollable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<JohnFolder> tabs = new ArrayList<JohnFolder>();
	private boolean animated;

	public JohnFolderPane()
	{
		setUI(new JohnFolderPaneUI());
		setLayout(new FolderPaneLayout());
	}

	public void addFolder(String title, JComponent comp)
	{
		addFolder(title, true, comp);
	}

	public void addFolder(String title, boolean expanded, JComponent comp)
	{
		JohnFolder tab = new JohnFolder(title, expanded, comp);
		tabs.add(tab);
		this.add(tab);
		((JohnFolderPaneUI) ui).addTab(tab);
	}

	public boolean isAnimated()
	{
		return animated;
	}

	public void setAnimated(boolean animated)
	{
		this.animated = animated;
	}

	public ArrayList<JohnFolder> getFolders()
	{
		return tabs;
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 10;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		return 100;
	}

	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}

	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	class FolderPaneLayout implements LayoutManager
	{
		private static final int INTER_TAB_PADDING = 15;

		public FolderPaneLayout()
		{
		}

		public void addLayoutComponent(String name, Component comp)
		{
		}

		public void removeLayoutComponent(Component comp)
		{
		}

		public Dimension preferredLayoutSize(Container parent)
		{
			Insets insets = parent.getInsets();
			int w = 0;
			int h = 0;
			for (JohnFolder tab : tabs)
			{
				Dimension dim = tab.getRequiredDimension();
				if (dim.width > w)
					w = dim.width;
				h += dim.height + INTER_TAB_PADDING;
			}
			w += insets.left + insets.right;
			h += insets.top + insets.bottom;
			return new Dimension(w, h);
		}

		public Dimension minimumLayoutSize(Container parent)
		{
			return new Dimension(0, 0);
		}

		public void layoutContainer(Container parent)
		{
			Insets insets = parent.getInsets();
			int x = insets.left;
			int y = insets.top;
			for (JohnFolder tab : tabs)
			{
				Dimension dim = tab.getRequiredDimension();
				tab.setBounds(x, y, dim.width, dim.height);
				tab.doLayout();
				y += dim.height + INTER_TAB_PADDING;
			}
		}
	}
}
