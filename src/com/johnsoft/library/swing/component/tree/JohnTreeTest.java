package com.johnsoft.library.swing.component.tree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class JohnTreeTest extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTree tree;
  private DefaultTreeModel model;
  private JohnTreeRenderer renderer;
  
	public JohnTreeTest()
	{
		initComponent();
	}
	
	public static void main(String[] args)
	{
		new JohnTreeTest();
	}
	
	private void initComponent()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		JohnTreeObject rootObj=new JohnTreeObject();
		rootObj.setTreeIcon(null);
    List<String[]> list=new ArrayList<String[]>();
    String[] str1=new String[]{"hello","#00FF00"};
    String[] str2=new String[]{"world","#FFFF00"};
    list.add(str1);
    list.add(str2);
    rootObj.setTreeText(list);
		JohnTreeObject oneObj=new JohnTreeObject();
		oneObj.setTreeIcon(new ImageIcon("E:/icon/dds1.png"));
		oneObj.setTreeName("韩国");
		oneObj.setUnSelectBGColor(Color.WHITE);
		oneObj.setSelectBGColor(Color.PINK);
		oneObj.setUnSelectTextColor(Color.BLACK);
		oneObj.setSelectTextColor(Color.RED);
		JohnTreeObject twoObj=new JohnTreeObject();
		twoObj.setTreeIcon(new ImageIcon("E:/icon/1_close1 (2).png"));
		twoObj.setTreeName("英国");
		twoObj.setSelectBGColor(Color.CYAN);
		JohnTreeObject helloObj=new JohnTreeObject();
		helloObj.setTreeIcon(new ImageIcon("E:/icon/1_close1 (5).png"));
		List<String[]> list1=new ArrayList<String[]>();
    String[] str3=new String[]{"welcome"};
    String[] str4=new String[]{"myname","#00F0F0"};
		list1.add(str3);
		list1.add(str4);
		helloObj.setTreeText(list1);
		
		DefaultMutableTreeNode root=new DefaultMutableTreeNode(rootObj);
		DefaultMutableTreeNode hello;
		root.add(new DefaultMutableTreeNode(oneObj));
		root.add(hello=new DefaultMutableTreeNode(twoObj));
		hello.add(new DefaultMutableTreeNode(helloObj));
		
		model=new DefaultTreeModel(root);
		tree=new JTree(model);
		renderer=new JohnTreeRenderer();
		tree.setCellRenderer(renderer);
		tree.setEditable(true);
		JScrollPane pane=new JScrollPane(tree);
		this.add(pane);
		this.setBounds(300,200, 500, 380);
		this.setVisible(true);
	}
	
	
	
}
