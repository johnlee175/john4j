package com.johnsoft.library.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.johnsoft.library.util.JohnGridBagHelper;
import com.johnsoft.library.util.JohnSwingUtilities;

public class JohnRelationComparator extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static final String[] BASE_RELATION_TYPES={"等于","小于","大于","小于等于","大于等于","不等于"};
	public static final String[] RANGE_RELATION_TYPES={"介于","介于(包含)","介于(左含)","介于(右含)","非介于","非介于(包含)","非介于(左含)","非介于(右含)"};
	public static final String[] BASE_RELATION_OPERATORS={"=","<",">","≤","≥","≠"};
	public static final String[] RANGE_RELATION_OPERATORS={"A<X<B(A<B)","A≤X≤B(A<B)","A≤X<B(A<B)","A<X≤B(A<B)","X>A,X<B(A>B)","X≥A,X≤B(A>B)","X>A,X≤B(A>B)","X≥A,X<B(A>B)"};
	public static final String[] LABEL_FOR_OPERATOR={" A : "," B : "};
	public static final String[] LABEL_FOR_TYPE={"大于(等于)","小于(等于)"};
	public static final String[] OPERATOR_BUTTON_TEXT={"使用符号","禁用符号"};
	public static final String[] RANGE_BUTTON_TEXT={"启用区间","禁用区间"};
	
	private boolean useOperator;
	private boolean containRange;
	private boolean rangeSelected;
	
	private String result;
	
	protected JComboBox relationCombo;
	protected JLabel fromLabel;
	protected JLabel toLabel;
	protected JTextField lowField;
	protected JTextField highField;
	protected JToggleButton rangeTrigger;
	protected JToggleButton operatorTrigger;
	protected JPanel valuePane;
	
	
	public JohnRelationComparator()
	{
		initDefaults();
		installComponents();
		installListeners();
	}
	
	protected void initDefaults()
	{
		setOpaque(false);
	}
	
	protected void installComponents()
	{
		buildRelationCombo();
		buildValuePane();
		JPanel comboPane=new JPanel();
		comboPane.add(relationCombo);
		JPanel triggerPane=getTriggerPane();
		JohnGridBagHelper helper=new JohnGridBagHelper(this);
		helper.fill(GridBagConstraints.HORIZONTAL).bounds(0, 0, 1, 1).add(comboPane);
		helper.weightx(1.0).nextCol(valuePane).weightx(0.0).nextCol(triggerPane);
	}
	
	protected void installListeners()
	{
		rangeTrigger.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(rangeTrigger.isSelected())
				{
					containRange=true;
					rangeTrigger.setText(RANGE_BUTTON_TEXT[1]);
				}else{
					containRange=false;
					rangeTrigger.setText(RANGE_BUTTON_TEXT[0]);
				}
				buildRelationCombo();
				buildValuePane();
				repaint();
			}
		});
		operatorTrigger.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(operatorTrigger.isSelected())
				{
					useOperator=true;
					if(fromLabel!=null)
					{
						fromLabel.setText(LABEL_FOR_OPERATOR[0]);
					}
					if(toLabel!=null)
					{
						toLabel.setText(LABEL_FOR_OPERATOR[1]);
					}
					operatorTrigger.setText(OPERATOR_BUTTON_TEXT[1]);
				}else{
					useOperator=false;
					if(fromLabel!=null)
					{
						fromLabel.setText(LABEL_FOR_TYPE[0]);
					}
					if(toLabel!=null)
					{
						toLabel.setText(LABEL_FOR_TYPE[1]);
					}
					operatorTrigger.setText(OPERATOR_BUTTON_TEXT[0]);
				}
				buildRelationCombo();
				buildValuePane();
				repaint();
			}
		});
		relationCombo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(containRange)
				{
					Object item=relationCombo.getSelectedItem();
					if(useOperator)
					{
						for(String str:RANGE_RELATION_OPERATORS)
						{
							if(str.equals(item))
							{
								rangeSelected=true;
								buildValuePane();
								valuePane.revalidate();
								return;
							}
						}
					}else{
						for(String str:RANGE_RELATION_TYPES)
						{
							if(str.equals(item))
							{
								rangeSelected=true;
								buildValuePane();
								valuePane.revalidate();
								return;
							}
						}
					}
					rangeSelected=false;
					buildValuePane();
					valuePane.revalidate();
				}
			}
		});
	}
	
	protected void buildRelationCombo()
	{
		if(relationCombo==null)
		{
			relationCombo=new JComboBox();
			relationCombo.setPreferredSize(new Dimension(120, 20));
		}
		List<String> list=new ArrayList<String>();
		if(useOperator)
		{
			for(String str:BASE_RELATION_OPERATORS)
			{
				list.add(str);
			}
			if(containRange)
			{
				for(String str:RANGE_RELATION_OPERATORS)
				{
					list.add(str);
				}
			}
		}else{
			for(String str:BASE_RELATION_TYPES)
			{
				list.add(str);
			}
			if(containRange)
			{
				for(String str:RANGE_RELATION_TYPES)
				{
					list.add(str);
				}
			}
		}
		relationCombo.setModel(new DefaultComboBoxModel(list.toArray()));
		rangeSelected=false;
	}
	
	protected void buildValuePane()
	{  
		if(valuePane==null)
		{
			valuePane=new JPanel();
			valuePane.setOpaque(false);
			valuePane.setPreferredSize(new Dimension(300, 25));
		}else{
			valuePane.removeAll();
		}
		if(lowField==null)
		{
			lowField=new JTextField();
			lowField.setInputVerifier(JohnSwingUtilities.getNumberVerifier(true));
		}
		JohnGridBagHelper helper=new JohnGridBagHelper(valuePane);
		if(containRange&&rangeSelected)
		{
			if(fromLabel==null)
			{
				fromLabel=new JLabel(useOperator?LABEL_FOR_OPERATOR[0]:LABEL_FOR_TYPE[0]);
			}
			if(toLabel==null)
			{
				toLabel=new JLabel(useOperator?LABEL_FOR_OPERATOR[1]:LABEL_FOR_TYPE[1]);
			}
			if(highField==null)
			{
				highField=new JTextField();
				highField.setInputVerifier(JohnSwingUtilities.getNumberVerifier(true));
			}
			helper.fill(GridBagConstraints.HORIZONTAL).bounds(0, 0, 1, 1).add(fromLabel);
			helper.weightx(1.0).bounds(1, 0, 2, 1).add(lowField);
			helper.weightx(0.0).bounds(3, 0, 1, 1).add(toLabel);
			helper.weightx(1.0).bounds(4, 0, 2, 1).add(highField);
		}else{
			helper.fill(GridBagConstraints.HORIZONTAL).bounds(0, 0, 1, 1).weightx(1.0).add(lowField);
		}
	}
	
	protected JPanel getTriggerPane()
	{
		rangeTrigger=new JToggleButton(RANGE_BUTTON_TEXT[0]);
		operatorTrigger=new JToggleButton(OPERATOR_BUTTON_TEXT[0]);
		JPanel pane=new JPanel();
		pane.setOpaque(false);
		pane.add(rangeTrigger);
		pane.add(operatorTrigger);
		return pane;
	}
	
	protected void createResult()
	{
		
	}
	
	public String getResult()
	{
		return result;
	}
	
	public boolean parseResult(String result,Number x)
	{
		return false;
	}
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame jf=new JFrame();
		JPanel jp=new JPanel();
		jp.add(new JohnRelationComparator());
		jf.add(jp);
		jf.setVisible(true);
	}
}
