package com.johnsoft.library.swing.component.treetable;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * A TreeTable example, showing a JTreeTable, operating on the local file
 * system.
 */
public class Test
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		new Test();
	}

	public Test()
	{
		JFrame frame = new JFrame("TreeTable");
		//<--important
		JohnTreeTable treeTable = new JohnTreeTable(new JohnFileSystemModel(),false);
		//important-->
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new JScrollPane(treeTable));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
