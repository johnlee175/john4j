package com.johnsoft.library.component.folderpane;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.plaf.ComponentUI;


public class JohnFolderPaneUI extends ComponentUI
{
	private static final Color BACK_COLOR = new Color(116, 149, 226);
	private static final int LEFT_PADDING = 12;
	private static final int TOP_PADDING = 12;
	private static final int RIGHT_PADDING = 12;
	private static final int BOTTOM_PADDING = 12;
	private static final double INCDEC = 0.085;
	private static final double DECRATIO = 0.925;
	private static final int ANIMATION_INTERVAL = 15;
	private JohnFolderPane pane;

	public void installUI(JComponent c)
	{
		c.setOpaque(true);
		c.setBackground(BACK_COLOR);
		c.setBorder(BorderFactory.createEmptyBorder(TOP_PADDING, LEFT_PADDING,
				RIGHT_PADDING, BOTTOM_PADDING));
		pane = (JohnFolderPane) c;
	}

	void addTab(JohnFolder tab)
	{
		tab.getCaption().addItemListener(new FolderTabItemListener(tab));
	}

	class FolderTabItemListener implements ItemListener
	{
		private JohnFolder tab;
		private Timer timer;

		public FolderTabItemListener(JohnFolder t)
		{
			tab = t;
		}

		public void itemStateChanged(ItemEvent e)
		{
			if (pane.isAnimated())
			{
				if (timer != null)
				{
					if (timer.isRunning())
						timer.stop();
				}
				timer = new Timer(ANIMATION_INTERVAL, null);
				ActionListener action;
				if (e.getStateChange() == ItemEvent.DESELECTED)
					action = new FolderingAction(tab, timer);
				else
					action = new ExpandingAction(tab, timer);
				timer.addActionListener(action);
				timer.start();
			} else
			{
				tab.getDrawer().setRatio(
						e.getStateChange() == ItemEvent.DESELECTED ? 0 : 1.0);
				pane.doLayout();
			}
			pane.repaint();
		}
	}

	abstract class FolderAction implements ActionListener
	{
		private JohnFolder tab;
		private Timer timer;
		private double ratio;
		private double exponent;

		public FolderAction(JohnFolder t, Timer timer)
		{
			tab = t;
			this.timer = timer;
			ratio = INCDEC;
			exponent = DECRATIO;
			tab.getDrawer().setAnimating(true);
		}

		protected double getDelta()
		{
			double r = ratio;
			ratio = ratio * exponent;
			return r;
		}

		public void actionPerformed(ActionEvent e)
		{
			double r = delta(tab.getDrawer().getRatio(), getDelta());
			if (overflow(r))
			{
				r = bound();
				tab.getDrawer().setAnimating(false);
				doLayout(r);
				timer.stop();
				timer = null;
			} else
			{
				doLayout(r);
			}
		}

		private void doLayout(final double r)
		{
			tab.getDrawer().setRatio(r);
			Container parent = pane.getParent();
			if (parent != null)
				parent.doLayout();
			pane.doLayout();
		}

		protected abstract double delta(double r, double d);

		protected abstract boolean overflow(double r);

		protected abstract double bound();
	}

	class ExpandingAction extends FolderAction
	{
		public ExpandingAction(JohnFolder t, Timer timer)
		{
			super(t, timer);
		}

		protected double delta(double r, double d)
		{
			return r + d;
		}

		protected boolean overflow(double r)
		{
			return r > 1;
		}

		protected double bound()
		{
			return 1;
		}
	}

	class FolderingAction extends FolderAction
	{
		public FolderingAction(JohnFolder t, Timer timer)
		{
			super(t, timer);
		}

		protected double delta(double r, double d)
		{
			return r - d;
		}

		protected boolean overflow(double r)
		{
			return r < 0;
		}

		protected double bound()
		{
			return 0;
		}
	}
}
