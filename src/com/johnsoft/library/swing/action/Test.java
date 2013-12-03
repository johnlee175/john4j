package com.johnsoft.library.swing.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class Test
{
	public static void main(String[] args)
	{
		final JFrame jf=new JFrame();
		jf.setName("world");
		JMenuItem item=new JMenuItem(new AA());
		JMenu menu=new JMenu("test(X)");
		menu.setMnemonic('X');
		menu.add(item);
		JMenuBar bar=new JMenuBar();
		bar.setPreferredSize(new Dimension());
		bar.add(menu);
		final JPanel jp=new JPanel();
		jp.setName("world");
		JButton jb=new JButton("click me(T)");
		jb.setName("world");
		JButton jb1=new JButton("click me(T)");
		JTextField jtf=new JTextField("dsafwe");
		jtf.setName("world");
		JTextField jx=new JTextField("dfwerwerwerwerwer");
		jb.setMnemonic('T');
		jb.setAction(new AA());
		//ActionManager.buildAccelKey("Ab12b0bfe-7488-4276-aec5-893e38b57b9c", "modify copy it", "ctrl B", "world", JButton.class, true);
		Action act=new JohnAction("Couren&New", null/*new ImageIcon("E:\\m_statistics.png")*/, "this is a test", "Ab12b0bfe-7488-4276-aec5-893e38b57b9c")
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("copy it-----------------");
			}
		};
		jb1.setAction(act);
		jp.add(jtf);
		jp.add(jb);
		jp.add(jx);
		jp.add(jb1);
		jf.add(jp);
		jf.getRootPane().setJMenuBar(bar);
		jf.setBounds(100,100,600,450);
		jf.setVisible(true);
	}
	
	public static class AA extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		public AA()
		{
			 putValue(NAME, "tengs(A)");
			 putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
			 putValue(MNEMONIC_KEY, 65);
			 putValue(LONG_DESCRIPTION, "sdfawerwrsdfaewrsdfaersf");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.out.println(e.getWhen());
			System.out.println(e.getModifiers());
			System.out.println(e.getActionCommand());
			System.out.println(e.getID());
			System.out.println(e.getSource());
		}
	}
}
