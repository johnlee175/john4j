package com.johnsoft.library.swing.component.filteredlist;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;


public class JohnFilteredList extends JList
{
	private static final long serialVersionUID = 1L;
	private JTextField input;

	public JohnFilteredList()
	{
		JohnFilteredModel model = new JohnFilteredModel(this);
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
			JohnFilteredModel model = (JohnFilteredModel) getModel();
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
			JohnFilteredModel model = (JohnFilteredModel) getModel();
			this.input.getDocument().removeDocumentListener(model);
			this.input = null;
		}
	}

	public void setModel(ListModel model)
	{
		if (!(model instanceof JohnFilteredModel))
		{
			throw new IllegalArgumentException();
		} else
		{
			super.setModel(model);
		}
	}

	public void addElement(Object element)
	{
		((JohnFilteredModel) getModel()).addElement(element);
	}

	public void fireChange()
	{
		JohnFilteredModel model = (JohnFilteredModel) getModel();
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
