package com.johnsoft.library.component.folderpane;

import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JComponent;


public class JohnCaptionButton extends JComponent implements ItemSelectable
{
	private static final long serialVersionUID = 1L;

	private ArrayList<ItemListener> listeners = new ArrayList<ItemListener>();

	private boolean expanded;
	private String text;
	private JohnCaptionButtonUI ui;
	private Color hoverColor=JohnCaptionButtonUI.DEFAULT_HOVER_COLOR;
	private Color foreground=JohnCaptionButtonUI.DEFAULT_FOREGROUND;

	public JohnCaptionButton()
	{
		this(null, true);
	}

	public JohnCaptionButton(String text, boolean expanded)
	{
		this.text = text;
		this.expanded = expanded;
		this.ui=new JohnCaptionButtonUI();
		setUI(ui);
	}

	public void addItemListener(ItemListener l)
	{
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void removeItemListener(ItemListener l)
	{
		if (listeners.contains(l))
			listeners.remove(l);
	}

	protected void fireItemStateChanged(ItemEvent e)
	{
		for (ItemListener l : listeners)
			l.itemStateChanged(e);
	}

	public Object[] getSelectedObjects()
	{
		if (!expanded)
			return null;
		return new Object[] { text };
	}

	public boolean isExpanded()
	{
		return expanded;
	}

	public void setExpanded(boolean expanded)
	{
		this.expanded = expanded;
	}
	
	public void fireExpanded(boolean expanded)
	{
		setExpanded(expanded);
		ItemEvent evt = new ItemEvent(this, isExpanded() ? 0 : 1,
				getText(), isExpanded() ? ItemEvent.SELECTED
						: ItemEvent.DESELECTED);
		fireItemStateChanged(evt);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
		repaint();
	}
	
	public Color getHoverColor()
	{
		return hoverColor;
	}

	public void setHoverColor(Color hoverColor)
	{
		if(hoverColor==null)
		{
			this.hoverColor=JohnCaptionButtonUI.DEFAULT_HOVER_COLOR;
		}else{
			this.hoverColor = hoverColor;
		}
	}
	
	@Override
	public Color getForeground()
	{
		return foreground;
	}
	
	@Override
	public void setForeground(Color foreground)
	{
		if(foreground==null)
		{
			this.foreground=JohnCaptionButtonUI.DEFAULT_FOREGROUND;
		}else{
			this.foreground=foreground;
		}
	}

	public JohnCaptionButtonUI getUI()
	{
		return ui;
	}
}
