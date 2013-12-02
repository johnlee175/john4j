package com.johnsoft.library.swing.component;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

public class JohnPartableFileChooser extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JFileChooser fileChooser;
	
	private JToolBar leftBar;
	private JToolBar topBar;
	private JPanel mainPane;
	private JPanel filePane;
	private JPanel bottomPane;
	private JPanel twoLabel;
	private JPanel twoField;
	private JPanel twoButton;
	private JLabel filePathLab;
	private JLabel fileTypeLab;
	private JTextField filePathText;
	private JComboBox fileTypeCombox;
	private JButton okButton;
	private JButton cancelButton;
	private JToggleButton latestBrowse;
	private JToggleButton desk;
	private JToggleButton mydocument;
	private JToggleButton mycompute;
	private JToggleButton internetBrowse;
	private JLabel lookupLab;
	private JComboBox lookupCombox;
	private JButton upPath;
	private JButton newFolder;
	private JButton lookupType;
	
	public JohnPartableFileChooser()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		fileChooser=new JFileChooser();
		getAccessPart();
		this.setLayout(new BorderLayout());
		this.add(fileChooser,BorderLayout.CENTER);
		this.setBackground(new Color(236,233,216));
	}
	
	private void getAccessPart()
	{
		leftBar=(JToolBar)fileChooser.getComponent(0);
		topBar=(JToolBar)fileChooser.getComponent(1);
		mainPane=(JPanel)fileChooser.getComponent(2);
		filePane=(JPanel)mainPane.getComponent(1);
		bottomPane=(JPanel)mainPane.getComponent(2);
		twoLabel=(JPanel)bottomPane.getComponent(0);
		twoField=(JPanel)bottomPane.getComponent(2);
		twoButton=(JPanel)bottomPane.getComponent(4);
		filePathLab=(JLabel)twoLabel.getComponent(1);
		fileTypeLab=(JLabel)twoLabel.getComponent(3);
		filePathText=(JTextField)twoField.getComponent(1);
		fileTypeCombox=(JComboBox)twoField.getComponent(3);
		okButton=(JButton)twoButton.getComponent(1);
		cancelButton=(JButton)twoButton.getComponent(3);
		latestBrowse=(JToggleButton)leftBar.getComponent(0);
		desk=(JToggleButton)leftBar.getComponent(2);
		mydocument=(JToggleButton)leftBar.getComponent(4);
		mycompute=(JToggleButton)leftBar.getComponent(6);
		internetBrowse=(JToggleButton)leftBar.getComponent(8);
		lookupLab=(JLabel)topBar.getComponent(0);
		lookupCombox=(JComboBox)topBar.getComponent(2);
		upPath=(JButton)topBar.getComponent(4);
		newFolder=(JButton)topBar.getComponent(5);
		lookupType=(JButton)topBar.getComponent(6);
	}
	
	public JFileChooser getFileChooser()
	{
		return fileChooser;
	}

	public void setFileChooser(JFileChooser fileChooser)
	{
		this.fileChooser = fileChooser;
	}

	public JToolBar getLeftBar()
	{
		return leftBar;
	}

	public void setLeftBar(JToolBar leftBar)
	{
		this.leftBar = leftBar;
	}

	public JToolBar getTopBar()
	{
		return topBar;
	}

	public void setTopBar(JToolBar topBar)
	{
		this.topBar = topBar;
	}

	public JPanel getMainPane()
	{
		return mainPane;
	}

	public void setMainPane(JPanel mainPane)
	{
		this.mainPane = mainPane;
	}

	public JPanel getFilePane()
	{
		return filePane;
	}

	public void setFilePane(JPanel filePane)
	{
		this.filePane = filePane;
	}

	public JPanel getBottomPane()
	{
		return bottomPane;
	}

	public void setBottomPane(JPanel bottomPane)
	{
		this.bottomPane = bottomPane;
	}

	public JPanel getTwoLabel()
	{
		return twoLabel;
	}

	public void setTwoLabel(JPanel twoLabel)
	{
		this.twoLabel = twoLabel;
	}

	public JPanel getTwoField()
	{
		return twoField;
	}

	public void setTwoField(JPanel twoField)
	{
		this.twoField = twoField;
	}

	public JPanel getTwoButton()
	{
		return twoButton;
	}

	public void setTwoButton(JPanel twoButton)
	{
		this.twoButton = twoButton;
	}

	public JLabel getFilePathLab()
	{
		return filePathLab;
	}

	public void setFilePathLab(JLabel filePathLab)
	{
		this.filePathLab = filePathLab;
	}

	public JLabel getFileTypeLab()
	{
		return fileTypeLab;
	}

	public void setFileTypeLab(JLabel fileTypeLab)
	{
		this.fileTypeLab = fileTypeLab;
	}

	public JTextField getFilePathText()
	{
		return filePathText;
	}

	public void setFilePathText(JTextField filePathText)
	{
		this.filePathText = filePathText;
	}

	public JComboBox getFileTypeCombox()
	{
		return fileTypeCombox;
	}

	public void setFileTypeCombox(JComboBox fileTypeCombox)
	{
		this.fileTypeCombox = fileTypeCombox;
	}

	public JButton getOkButton()
	{
		return okButton;
	}

	public void setOkButton(JButton okButton)
	{
		this.okButton = okButton;
	}

	public JButton getCancelButton()
	{
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton)
	{
		this.cancelButton = cancelButton;
	}

	public JToggleButton getLatestBrowse()
	{
		return latestBrowse;
	}

	public void setLatestBrowse(JToggleButton latestBrowse)
	{
		this.latestBrowse = latestBrowse;
	}

	public JToggleButton getDesk()
	{
		return desk;
	}

	public void setDesk(JToggleButton desk)
	{
		this.desk = desk;
	}

	public JToggleButton getMydocument()
	{
		return mydocument;
	}

	public void setMydocument(JToggleButton mydocument)
	{
		this.mydocument = mydocument;
	}

	public JToggleButton getMycompute()
	{
		return mycompute;
	}

	public void setMycompute(JToggleButton mycompute)
	{
		this.mycompute = mycompute;
	}

	public JToggleButton getInternetBrowse()
	{
		return internetBrowse;
	}

	public void setInternetBrowse(JToggleButton internetBrowse)
	{
		this.internetBrowse = internetBrowse;
	}

	public JLabel getLookupLab()
	{
		return lookupLab;
	}

	public void setLookupLab(JLabel lookupLab)
	{
		this.lookupLab = lookupLab;
	}

	public JComboBox getLookupCombox()
	{
		return lookupCombox;
	}

	public void setLookupCombox(JComboBox lookupCombox)
	{
		this.lookupCombox = lookupCombox;
	}

	public JButton getUpPath()
	{
		return upPath;
	}

	public void setUpPath(JButton upPath)
	{
		this.upPath = upPath;
	}

	public JButton getNewFolder()
	{
		return newFolder;
	}

	public void setNewFolder(JButton newFolder)
	{
		this.newFolder = newFolder;
	}

	public JButton getLookupType()
	{
		return lookupType;
	}

	public void setLookupType(JButton lookupType)
	{
		this.lookupType = lookupType;
	}

//	public static void main(String[] args)
//	{
//		JFrame jframe=new JFrame();
//    JohnPartableFileChooser chooser=new JohnPartableFileChooser();
//		jframe.add(chooser);
//		jframe.setVisible(true);
//	}

}
