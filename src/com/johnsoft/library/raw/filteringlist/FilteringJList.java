package com.johnsoft.library.raw.filteringlist;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;


public class FilteringJList extends JList
{
	private static final long serialVersionUID = 1L;
	private JTextField input;

	public FilteringJList()
	{
		FilteringModel model = new FilteringModel(this);
		setModel(model);
	}

	/**
	 * Associates filtering document listener to text component.
	 */

	public void installJTextField(JTextField input)
	{
		if (input != null)
		{
			this.input = input;
			FilteringModel model = (FilteringModel) getModel();
			this.input.getDocument().addDocumentListener(model);
		}
	}

	/**
	 * Disassociates filtering document listener from text component.
	 */

	public void uninstallJTextField(JTextField input)
	{
		if (input != null)
		{
			FilteringModel model = (FilteringModel) getModel();
			this.input.getDocument().removeDocumentListener(model);
			this.input = null;
		}
	}

	public void setModel(ListModel model)
	{
		if (!(model instanceof FilteringModel))
		{
			throw new IllegalArgumentException();
		} else
		{
			super.setModel(model);
		}
	}

	public void addElement(Object element)
	{
		((FilteringModel) getModel()).addElement(element);
	}

	public void fireChange()
	{
		FilteringModel model = (FilteringModel) getModel();
		input.getDocument().addDocumentListener(model);
	}

	public JTextField getInput()
	{
		return input;
	}

	public void setInput(JTextField input)
	{
		this.input = input;
	}
	
//	public static void main(String args[]) {
//		Runnable runner = new Runnable() {
//			public void run() {
//				JFrame frame = new JFrame("Filtering List");
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//				FilteringJList list = new FilteringJList();
//				JScrollPane pane = new JScrollPane(list);
//				frame.add(pane, BorderLayout.CENTER);
//				JTextField text = new JTextField();
//				list.installJTextField(text);
//				frame.add(text, BorderLayout.NORTH);
//				List<RoutePointTask> rpList = InitialData.getRpFilterList();
//
//				for (Object element : rpList) {
//					list.addElement(element);
//				}
//
//				frame.setSize(250, 150);
//				frame.setVisible(true);
//			}
//		};
//		EventQueue.invokeLater(runner);
//	}

}
