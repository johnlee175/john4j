package com.johnsoft.library.swing.component;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import com.johnsoft.library.swing.layout.JohnGridLayout;

public class JohnMutexToggleButtonBar extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected boolean isRadio;
	protected BasicToggleButtonUI itemUI;
	protected ButtonGroup group=new ButtonGroup();
	protected List<JToggleButton> btnList=new ArrayList<JToggleButton>();
	
	public JohnMutexToggleButtonBar(boolean isVerticalBar)
	{
		setLayout(new TileBarLayout(isVerticalBar));
		setOpaque(false);
	}
	
	public void setItemUI(BasicToggleButtonUI ui)
	{
		itemUI=ui;
		if(itemUI!=null)
		{
			for(JToggleButton btn:btnList)
			{
				btn.setUI(itemUI);
			}
		}
	}
	
	public void applyRadioBar()
	{
		isRadio=true;
	}
	
	public String getCommand(int index,String defaults)
	{
		if(index>=btnList.size()||index<0)
			return defaults;
		return btnList.get(index).getActionCommand();
	}
	
	public int getIndex(String command)
	{
		for(int i=0;i<btnList.size();i++)
		{
			if(getCommand(i,"").equals(command))
			{
				return i;
			}
		}
		return -1;
	}
	
	public void setDefaultButton(String command)
	{
		setDefaultButton(getIndex(command));
	}
	
	public void setDefaultButton(int index)
	{
		if(index>=btnList.size()||index<0)
			return;
		btnList.get(index).setSelected(true);
	}
	
	public void setItems(String[] commandList)
	{
		clear();
		addItemList(Arrays.asList(commandList));
	}
	
	public void setItems(String[] commandList,Icon[] iconList,boolean showText)
	{
		clear();
		addItemList(Arrays.asList(commandList), Arrays.asList(iconList), showText);
	}
	
	public void addItemList(List<String> commandList)
	{
		for(String command:commandList)
		{
			addItem(command);
		}
	}
	
	public void addItemList(List<String> commandList,List<Icon> iconList,boolean showText)
	{
		int size=0;
		if(commandList!=null||iconList!=null)
		{
			size=commandList==null?iconList.size():commandList.size();
		}
		for(int i=0;i<size;i++)
		{
			addItem(commandList==null?null:commandList.get(i), iconList==null?null:iconList.get(i), showText);
		}
	}
	
	public void addItem(String command)
	{
		addItem(isRadio?new JRadioButton(command):new JToggleButton(command),command);
	}
	
	public void addItem(String command,Icon icon,boolean showText)
	{
		JToggleButton btn=isRadio?new JRadioButton(icon):new JToggleButton(icon);
		if(showText)
		{
			btn.setText(command);
		}else{
			btn.setContentAreaFilled(false);
		}
		addItem(btn,command);
	}
	
	public void addItem(JToggleButton btn,String command)
	{
		if(btn instanceof JRadioButton)
		{
			btn.setOpaque(false);
		}
		btn.setActionCommand(command);
		if(itemUI!=null)
		{
			btn.setUI(itemUI);
		}
		add(btn);
		group.add(btn);
		btnList.add(btn);
	}
	
	public JToggleButton getItem(int index)
	{
		if(index>=btnList.size()||index<0)
			return null;
		return btnList.get(index);
	}
	
	public JToggleButton getItem(String command)
	{
		return getItem(getIndex(command));
	}
	
	public boolean removeItem(String command)
	{
		return removeItem(getIndex(command));
	}
	
	public boolean removeItem(int index)
	{
		if(index>=btnList.size()||index<0)
			return false;
		JToggleButton btn=btnList.remove(index);
		group.remove(btn);
		remove(btn);
		return true;
	}
	
	public List<JToggleButton> getItems()
	{
		return btnList;
	}
	
	public void clear()
	{
		for(JToggleButton btn:btnList)
		{
			group.remove(btn);
			remove(btn);
		}
		btnList.clear();
	}
	
	public boolean isEmpty()
	{
		return btnList.isEmpty();
	}
	
	public String getSelectedCommand()
	{
		ButtonModel bm=group.getSelection();
		return bm==null?null:bm.getActionCommand();
	}
	
	public void addActionListener(ActionListener al)
	{
		for(JToggleButton btn:btnList)
		{
			btn.addActionListener(al);
		}
	}
	
	public void removeActionListener(ActionListener al)
	{
		for(JToggleButton btn:btnList)
		{
			btn.removeActionListener(al);
		}
	}
	
	protected  class TileBarLayout extends JohnGridLayout
	{
		private static final long serialVersionUID = 1L;
		
		protected boolean vertical;
		
		public boolean isVertical()
		{
			return vertical;
		}

		public void setVertical(boolean vertical)
		{
			this.vertical = vertical;
		}
		
		public TileBarLayout(boolean vertical)
		{
			super();
			setVertical(vertical);
		}
		
		protected void checkRowsAndCols()
		{
			int count=btnList.size();
			if(vertical)
			{
				setRows(count);
				setCols(1);
			}else{
				setRows(1);
				setCols(count);
			}
		}
		
		@Override
		public Dimension preferredLayoutSize(Container c)
		{
			checkRowsAndCols();
			return super.preferredLayoutSize(c);
		}
		
		@Override
		public Dimension minimumLayoutSize(Container c)
		{
			checkRowsAndCols();
			return super.minimumLayoutSize(c);
		}
		
		@Override
		public void layoutContainer(Container c)
		{
			checkRowsAndCols();
			super.layoutContainer(c);
		}
	}
	
}
