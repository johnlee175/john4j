package com.johnsoft.library.swing.document.textfield;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Test
{
	public static void main(String[] args)
	{
		JFrame jf=new JFrame();
		JPanel jp=new JPanel();
		JTextField jtf=new JTextField(30);
		jtf.setDocument(new JohnPlainDoc(new JohnEngDocFilter(new JohnLimitLenDocFilter(new JohnLetterDocFilter(new JohnUpCaseDocFilter(null)), 10))));
		jp.add(jtf);
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);;
	}
}
