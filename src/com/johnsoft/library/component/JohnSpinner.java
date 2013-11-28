package com.johnsoft.library.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

/**
 * 日历控件用的可输入可波动可选择的控件
 */
public class JohnSpinner extends JPanel implements ActionListener,ChangeListener,KeyListener,FocusListener
{
	private static final long serialVersionUID = 1L;

	protected String text;//用户操作完组件,被更改后的值
	private JComponent comp;//当改变该组件时要提示更改后值的标签或文本组件
	private String selectedItem;//设置初始化组件时显示的值
	private List<String> list;//下拉框的可选项值
	private SpinnerListModel smodel;
	private ComboBoxModel cmodel;
	private JSpinner spinner;
	private JComboBox combox;
	private JTextField textfield;
	
	public JohnSpinner(final JComponent comp,final List<String> list,String selectedItem)
	{
		this.comp=comp;
		this.list=list;
		this.selectedItem=selectedItem;
		
		smodel=new SpinnerListModel(list);
		cmodel=new DefaultComboBoxModel(list.toArray());
		//<--影响性能
		spinner=new JSpinner(smodel);
		combox=new JComboBox(cmodel);
		//影响性能-->
		textfield=(JTextField)combox.getEditor().getEditorComponent();
		setPreferredSize(new Dimension(70, 25));
		spinner.setEditor(combox);
		combox.setEditable(true);
		this.add(spinner);
		setJohnSpinnerValue(this.selectedItem);
		setOutComponentValue();
		spinner.addChangeListener(this);
		combox.addActionListener(this);
		textfield.addKeyListener(this);
		textfield.addFocusListener(this);
	}
	
	/**
	 * 当改变该组件时调用此方法将更改后值同步到标签或文本组件comp
	 */
	public void setOutComponentValue()
	{
		if(comp instanceof JLabel)
		{
			JLabel jlab=(JLabel)comp;
			jlab.setText(text);
		}
		if(comp instanceof JTextComponent)
		{
			JTextComponent jtext=(JTextComponent)comp;
			jtext.setText(text);
		}
	}
	
	/**
	 * 更改该组件的值为value
	 */
	public void setJohnSpinnerValue(String value)
	{
		textfield.setText(value);
		combox.setSelectedItem(value);
		spinner.setValue(value);
		text=value;
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		  String val=spinner.getValue().toString();
		  if(!val.equals(""))
		  {	 
			  setJohnSpinnerValue(val);
				setOutComponentValue();
		  }
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String val=combox.getSelectedItem().toString();
		if(!val.equals(""))
		{
			setJohnSpinnerValue(val);
			setOutComponentValue();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		String val=textfield.getText();
		for(int i=0;i<list.size();i++)
		{
			if(val.equals(list.get(i)))
			{
				setJohnSpinnerValue(val);
				setOutComponentValue();
				return;
			}
		}
		inputException();
	}
	
	/**
	 * 留用
	 */
	public void inputException(){
	}
  
	@Override
	public void setPreferredSize(Dimension preferredSize)
	{
		super.setPreferredSize(preferredSize);
		this.spinner.setPreferredSize(preferredSize);
	}
	
	/**
	 * @return 下拉框的可选项值
	 */
	public List<String> getList()
	{
		return list;
	}

	public void setList(List<String> list)
	{
		this.list = list;
	}

	/**
	 * @return 用户操作完组件,被更改后的值
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * @return 当改变该组件时要提示更改后值的标签或文本组件
	 */
	public JComponent getComp()
	{
		return comp;
	}

	public SpinnerListModel getSmodel()
	{
		return smodel;
	}

	public ComboBoxModel getCmodel()
	{
		return cmodel;
	}

	public JSpinner getSpinner()
	{
		return spinner;
	}

	public JComboBox getCombox()
	{
		return combox;
	}

	public JTextField getTextfield()
	{
		return textfield;
	}

	@Override
	public void keyTyped(KeyEvent e){
	}

	@Override
	public void keyPressed(KeyEvent e){
	}

	@Override
	public void focusGained(FocusEvent e){
	}

	@Override
	public void focusLost(FocusEvent e){
	}

}
