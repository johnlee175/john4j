package com.johnsoft.library.component.folderpane;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JComponent;


public class JohnFolder extends JComponent
{

	private static final long serialVersionUID = 1L;

	private static final int CAPTION_HEIGHT = 25;

	private JohnCaptionButton caption;

	private JohnDrawer drawer;

	JohnFolder(String label, JComponent comp)
	{
		this(label, true, comp);
	}

	JohnFolder(String label, boolean expanded, JComponent comp)
	{
		setLayout(new FolderTabLayout());

		caption = new JohnCaptionButton(label, expanded);
		add(caption);

		drawer = new JohnDrawer(expanded ? 1 : 0, comp);
		add(drawer);
	}

	public JohnCaptionButton getCaption()
	{
		return caption;
	}

	public JohnDrawer getDrawer()
	{
		return drawer;
	}

	class FolderTabLayout implements LayoutManager
	{
		public void addLayoutComponent(String name, Component comp)
		{
		}

		public void removeLayoutComponent(Component comp)
		{
		}

		public Dimension preferredLayoutSize(Container parent)
		{
			return parent.getPreferredSize();
		}

		public Dimension minimumLayoutSize(Container parent)
		{
			return parent.getMinimumSize();
		}

		public void layoutContainer(Container parent)
		{
			int w = parent.getWidth();
			int h = parent.getHeight();
			caption.setBounds(0, 0, w, CAPTION_HEIGHT);
			drawer.setBounds(0, CAPTION_HEIGHT, w, h - CAPTION_HEIGHT);
		}
	}

	Dimension getRequiredDimension()
	{
		int w = drawer.getContentWidth();
		int h = (int) (drawer.getContentHeight() * drawer.getRatio())
				+ CAPTION_HEIGHT;
		return new Dimension(w, h);
	}
}
