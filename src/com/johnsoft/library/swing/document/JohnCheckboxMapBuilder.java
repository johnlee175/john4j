package com.johnsoft.library.swing.document;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;

public class JohnCheckboxMapBuilder
{
	private Map<String,JCheckBox> boxesMap=new HashMap<String,JCheckBox>();
	
	private JCheckBox allCheckBox=new JCheckBox("All/全选");
	
	/**
	 * 任一复选框的取消选择对全选复选框的影响设置
	 */
	private ActionListener scopedCheckActionListener=new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JCheckBox box = (JCheckBox) e.getSource();
			if (!box.isSelected())
			{
				if (allCheckBox.isSelected())
					allCheckBox.setSelected(false);
			}else{
				if (isAllSelected())
					allCheckBox.setSelected(true); 
			}
		}
	};
	
	private ActionListener selectAllListener=new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (allCheckBox.isSelected())
			{
				for (JCheckBox cb : boxesMap.values())
					cb.setSelected(true);
			} else
			{
				for (JCheckBox cb : boxesMap.values())
					cb.setSelected(false);
			}
		}
	};
	
	public JohnCheckboxMapBuilder(List<String> labels)
	{
		allCheckBox.addActionListener(selectAllListener);
		for(String label : labels)
		{
			JCheckBox box = new JCheckBox(label);
			boxesMap.put(label, box);
			box.addActionListener(scopedCheckActionListener);
		}
	}
	
	public JohnCheckboxMapBuilder(String[] labels)
	{
		allCheckBox.addActionListener(selectAllListener);
		for(String label : labels)
		{
			JCheckBox box = new JCheckBox(label);
			boxesMap.put(label, box);
			box.addActionListener(scopedCheckActionListener);
		}
	}
	
	/**
	 * @param c 容器,用来装复选框组,注意,该容器须是已经做好布局的容器(比如设置此容器为GridLayout),该函数仅仅是将所有复选框add进去
	 * @param appendAllCheck 如果为true,将在添加所有复选框之后将全选复选框添加到容器,如果为false,则是首先添加全选复选框,然后添加其他复选框,如果为null,则不会全选添加复选框
	 */
	public void fillToPane(Container c, Boolean appendAllCheck)
	{
		if(appendAllCheck!=null&&!appendAllCheck)
			c.add(allCheckBox);
		for (JCheckBox cb : boxesMap.values())
			c.add(cb);
		if(appendAllCheck!=null&&appendAllCheck)
			c.add(allCheckBox);
	}
	
	/** 获取所有复选框组里的元素,除全选复选框*/
	public JCheckBox[] getCheckBoxes()
	{
		return boxesMap.values().toArray(new JCheckBox[boxesMap.size()]);
	}
	
	/** 清除所有复选框的选择,包括全选复选框*/
	public void clearSelections()
	{
		for (JCheckBox cb : boxesMap.values())
			cb.setSelected(false);
		allCheckBox.setSelected(false);
	}
	
	/** 有任意一个复选框选择返回true*/
	public boolean hasAnySelected()
	{
		for (JCheckBox cb : boxesMap.values())
		{
			if(cb.isSelected())
				return true;
		}
		return false;
	}
	
	/** 所有复选框选中返回true*/
	public boolean isAllSelected()
	{
		for (JCheckBox cb : boxesMap.values())
		{
			if(!cb.isSelected())
				return false;
		}
		return true;
	}
	
	/**返回特定标签的复选框的选择状态*/
	public boolean isSelected(String label)
	{
		return boxesMap.get(label).isSelected();
	}
	
	/** 给全选复选框添加特定action事件*/
	public void addAllCheckBoxActionListener(ActionListener al)
	{
		allCheckBox.addActionListener(al);
	}
	
	/** 给全选复选框移除特定action事件*/
	public void removeAllCheckBoxActionListener(ActionListener al)
	{
		allCheckBox.removeActionListener(al);
	}
	 
}
