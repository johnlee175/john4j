package com.johnsoft.tools;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class MessageDigestGUICreator
{
	public static File[] files;
	public static File outputFile;
	
	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame jf = new JFrame();
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
		final JComboBox comBox = new JComboBox(new String[]{"MD5", "SHA1"});
		final JTextField jtf = new JTextField("请选择要生成摘要的目标文件");
		final JTextField out = new JTextField("请选择存放摘要结果列表的文件");
		final JButton btn = new JButton("执行");
		
		comBox.setFocusable(false);
		jtf.setEditable(false);
		out.setEditable(false);
		btn.setFocusable(false);
		
		comBox.setPreferredSize(new Dimension(200, 30));
		jtf.setPreferredSize(new Dimension(200, 30));
		out.setPreferredSize(new Dimension(200, 30));
		btn.setPreferredSize(new Dimension(80, 30));
		
		jp.add(comBox);
		jp.add(jtf);
		jp.add(out);
		jp.add(btn);
		jf.add(jp);
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("摘要生成器");
		jf.setSize(280, 240);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		jtf.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.setMultiSelectionEnabled(true);
				if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					files = jfc.getSelectedFiles();
					if(files.length > 1)
					{
						jtf.setText(files[0].getAbsolutePath() + " ... ");
					} else {
						jtf.setText(files[0].getAbsolutePath());
					}
				}
			}
		});
		
		out.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				if(outputFile != null)
				{
					jfc.setSelectedFile(outputFile);
				}
				if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					outputFile = jfc.getSelectedFile();
					out.setText(outputFile.getAbsolutePath());
				}
			}
		});
		
		btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(files == null || files.length == 0)
				{
					JOptionPane.showMessageDialog(null, "请选择要生成摘要的目标文件!");
					return;
				}
				if(outputFile == null)
				{
					JOptionPane.showMessageDialog(null, "请选择存放摘要结果列表的文件!");
					return;
				}
				Object algorithm = comBox.getSelectedItem();
				if(algorithm == null)
				{
					JOptionPane.showMessageDialog(null, "请选择摘要算法!");
					return;
				}
				btn.setEnabled(false);
				try
				{
					prepare(outputFile, algorithm.toString(), files);
					JOptionPane.showMessageDialog(null, "生成成功!");
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				finally
				{
					btn.setEnabled(true);
				}
			}
		});
	}
	
	public static final void prepare(File outputFile, String algorithm, File[] files) throws Exception
	{
		PrintWriter pw = new PrintWriter(outputFile, "UTF-8");
		for(File file : files)
		{
			recursion(pw, algorithm, file);
		}
		pw.close();
	}
	
	public static final void recursion(PrintWriter pw, String algorithm, File file) throws Exception
	{
		if(file.isFile())
		{
			pw.println(file.getAbsolutePath() + ">>>" + digest(algorithm, file));
		}else{
			for(File f : file.listFiles())
			{
				recursion(pw, algorithm, f);
			}
		}
	}
	
	public static final String digest(String algorithm, File file) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance(algorithm);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		int len = 0;
		byte[] bytes = new byte[4096];
		while((len = bis.read(bytes)) > 0)
		{
			md.update(bytes, 0, len);
		}
		bytes = md.digest();
		bis.close();
		return byte2hex(bytes);
	}
	
	public static final String byte2hex(byte[] bytes)
	{
		String hs = "";
		String stmp = "";
		for (int i = 0; i < bytes.length; i++)
		{
			stmp = Integer.toHexString(bytes[i] & 0XFF);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
}
