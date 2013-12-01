package com.johnsoft.library.swing.component.filetree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import com.johnsoft.library.swing.component.filetree.JohnFileTreeConstants.FilterOption;

public class JohnFileTreeNodePopupMenu extends JPopupMenu
{
	private static final long serialVersionUID = 1L;
	
	protected JohnFileTree fileTree;
	protected ResourceBundle bundle;
	protected JohnFileTreeNode[] nodes;
	
	public void setSelectedNodes(JohnFileTreeNode[] nodes)
	{
		this.nodes=nodes;
	}
	
	public boolean hasSelectFolder()
	{
		if(nodes==null)
		{
			return false;
		}else{
			for(JohnFileTreeNode node:nodes)
			{
				if(node.isFolder())
				{
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean hasSelectFile()
	{
		if(nodes==null)
		{
			return false;
		}else{
			for(JohnFileTreeNode node:nodes)
			{
				if(!node.isFolder())
				{
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean isOnlySelectFolders()
	{
		if(nodes==null)
		{
			return false;
		}else{
			boolean isFile=false;
			for(JohnFileTreeNode node:nodes)
			{
				if(!node.isFolder())
				{
					isFile=true;
				}
			}
			return isFile?false:true;
		}
	}
	
	public boolean isOnlySelectFiles()
	{
		if(nodes==null)
		{
			return false;
		}else{
			boolean isFolder=false;
			for(JohnFileTreeNode node:nodes)
			{
				if(node.isFolder())
				{
					isFolder=true;
				}
			}
			return isFolder?false:true;
		}
	}
	
	public boolean isRootSelected()
	{
		if(nodes==null||nodes.length!=1)
		{
			return false;
		}else{
			JohnFileTreeNode root=(JohnFileTreeNode)fileTree.getModel().getRoot();
			TreePath tp_root=new TreePath(root.getPath());
			TreePath tp_node=new TreePath(nodes);
			if(tp_root.equals(tp_node))
			{
				return true;
			}
			return false;
		}
	}
	
	public JohnFileTreeNodePopupMenu(JohnFileTree fileTree)
	{
		this.fileTree=fileTree;
		bundle=ResourceBundle.getBundle("FileTreeModule");
		
		JMenu tree=new JMenu(bundle.getString("tree"));
		tree.add(new JMenuItem(getCloseOrOpenAction(false)));
		tree.add(new JMenuItem(getCloseOrOpenAction(true)));
		add(tree);
		
		JMenu refresh=new JMenu(bundle.getString("refresh"));
		refresh.add(new JMenuItem(getRefreshAction(JohnFileTreeConstants.REFRESH_ALL_TREE)));
		refresh.add(new JMenuItem(getRefreshAction(JohnFileTreeConstants.REFRESH_SELECTED_NODES)));
		add(refresh);
		
		JMenu browse=new JMenu(bundle.getString("browse"));
		browse.add(new JMenuItem(getBrowseAction(JohnFileTreeConstants.BROWSE_WITH_DEFAULT)));
		browse.add(new JMenuItem(getBrowseAction(JohnFileTreeConstants.BROWSE_WITH_NOTEPAD)));
		add(browse);
		
		add(getCopyAction());
		add(getPasteAction());
		
		JMenu order=new JMenu(bundle.getString("order"));
		order.add(new JMenuItem(getOrderAction(JohnFileTreeConstants.ORDER_BY_TYPE)));
		order.add(new JMenuItem(getOrderAction(JohnFileTreeConstants.ORDER_BY_DATE)));
		order.add(new JMenuItem(getOrderAction(JohnFileTreeConstants.ORDER_BY_SIZE)));
		order.add(new JMenuItem(getOrderAction(JohnFileTreeConstants.ORDER_BY_NAME)));
		add(order);
		
		add(getDeleteAction());
		
		JMenu fileprop=new JMenu(bundle.getString("fileprop"));
		JMenu propSet=new JMenu(bundle.getString("fileprop.set"));
		JMenu propCancel=new JMenu(bundle.getString("fileprop.cancel"));
		propSet.add(new JMenuItem(getPropertySettingAction('R', true)));
		propSet.add(new JMenuItem(getPropertySettingAction('H', true)));
		propSet.add(new JMenuItem(getPropertySettingAction('S', true)));
		propCancel.add(new JMenuItem(getPropertySettingAction('R', false)));
		propCancel.add(new JMenuItem(getPropertySettingAction('H', false)));
		propCancel.add(new JMenuItem(getPropertySettingAction('S', false)));
		fileprop.add(propSet);
		fileprop.add(propCancel);
		add(fileprop);
		
		add(getLastModifySettingAction());
		
		JMenu adds=new JMenu(bundle.getString("add"));
		adds.add(new JMenuItem(getAddAction("file")));
		adds.add(new JMenuItem(getAddAction("folder")));
		add(adds);
		
		add(getRenameAction());
		
		add(getFilterHiddenAction());
		add(getFilterAction());
		
		add(getReplaceAndEncodeAction());
		
		addInputAndActionMap(fileTree);
	}
	
	public void addInputAndActionMap(JohnFileTree tree)
	{
		tree.getInputMap().put((KeyStroke)getReplaceAndEncodeAction().getValue(Action.ACCELERATOR_KEY),getReplaceAndEncodeAction().getValue(Action.NAME));
		tree.getActionMap().put(getReplaceAndEncodeAction().getValue(Action.NAME), getReplaceAndEncodeAction());
		
		tree.getInputMap().put((KeyStroke)getFilterAction().getValue(Action.ACCELERATOR_KEY),getFilterAction().getValue(Action.NAME));
		tree.getActionMap().put(getFilterAction().getValue(Action.NAME), getFilterAction());
	
		tree.getInputMap().put((KeyStroke)getFilterHiddenAction().getValue(Action.ACCELERATOR_KEY),getFilterHiddenAction().getValue(Action.NAME));
		tree.getActionMap().put(getFilterHiddenAction().getValue(Action.NAME), getFilterHiddenAction());
	
		tree.getInputMap().put((KeyStroke)getRenameAction().getValue(Action.ACCELERATOR_KEY),getRenameAction().getValue(Action.NAME));
		tree.getActionMap().put(getRenameAction().getValue(Action.NAME), getRenameAction());
	
		tree.getInputMap().put((KeyStroke)getLastModifySettingAction().getValue(Action.ACCELERATOR_KEY),getLastModifySettingAction().getValue(Action.NAME));
		tree.getActionMap().put(getLastModifySettingAction().getValue(Action.NAME), getLastModifySettingAction());
	
		tree.getInputMap().put((KeyStroke)getDeleteAction().getValue(Action.ACCELERATOR_KEY),getDeleteAction().getValue(Action.NAME));
		tree.getActionMap().put(getDeleteAction().getValue(Action.NAME), getDeleteAction());
	
		tree.getInputMap().put((KeyStroke)getPasteAction().getValue(Action.ACCELERATOR_KEY),getPasteAction().getValue(Action.NAME));
		tree.getActionMap().put(getPasteAction().getValue(Action.NAME), getPasteAction());
		
		tree.getInputMap().put((KeyStroke)getCopyAction().getValue(Action.ACCELERATOR_KEY),getCopyAction().getValue(Action.NAME));
		tree.getActionMap().put(getCopyAction().getValue(Action.NAME), getCopyAction());
	
		tree.getInputMap().put((KeyStroke)getChangeRootAction().getValue(Action.ACCELERATOR_KEY),getChangeRootAction().getValue(Action.NAME));
		tree.getActionMap().put(getChangeRootAction().getValue(Action.NAME), getChangeRootAction());
	}
	
	@Override
	public void show(Component invoker, int x, int y)
	{
		String text=((JMenuItem)getComponent(0)).getText();
	  if(text.equals(getChangeRootAction().getValue(Action.NAME)))
	  {
	  	remove(0);
	  }
		if(isRootSelected())
		{
			insert(getChangeRootAction(), 0);
		}
		super.show(invoker, x, y);
	}
	
	public Action getChangeRootAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final JDialog jd=new JDialog();
				final JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				jfc.setCurrentDirectory(fileTree.getRootFile());
				jd.setSize(500, 400);
				jd.setModalityType(ModalityType.APPLICATION_MODAL);
				jd.setLocationRelativeTo(null);
				jd.add(jfc);
				jfc.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(e.getActionCommand().equals("ApproveSelection"))
						{
							File file=jfc.getSelectedFile();
							fileTree.createModel(file);
						}
						jd.dispose();
					}
				});
				jd.setVisible(true);
			}
		};
		action.putValue(Action.NAME, bundle.getString("root.change"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl ENTER"));
		return action;
	}
	
	public Action getCloseOrOpenAction(final boolean isOpen)
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.closeOrOpenTree(isOpen);
			}
		};
		String actionName="";
		if(isOpen)
		{
			actionName=bundle.getString("tree.open");
		}
		else 
		{
			actionName=bundle.getString("tree.close");
		}
		action.putValue(Action.NAME, actionName);
		return action;
	}
	
	public Action getRefreshAction(final int domain)
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.refresh(domain);
			}
		};
		String actionName="";
		if(domain==JohnFileTreeConstants.REFRESH_ALL_TREE)
		{
			actionName=bundle.getString("refresh.tree");
		}
		else if(domain==JohnFileTreeConstants.REFRESH_SELECTED_NODES)
		{
			actionName=bundle.getString("refresh.node");
		}
		action.putValue(Action.NAME, actionName);
		return action;
	}
	
	public Action getBrowseAction(final int openWith)
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.browse(openWith);
			}
		};
		String actionName="";
		if(openWith==JohnFileTreeConstants.BROWSE_WITH_DEFAULT)
		{
			actionName=bundle.getString("browse.default");
		}
		else if(openWith==JohnFileTreeConstants.BROWSE_WITH_NOTEPAD)
		{
			actionName=bundle.getString("browse.notepad");
		}
		action.putValue(Action.NAME, actionName);
		return action;
	}
	
	public Action getCopyAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.copy();
			}
		};
		action.putValue(Action.NAME, bundle.getString("copy"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl C"));
		return action;
	}
	
	public Action getPasteAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.paste();
			}
		};
		action.putValue(Action.NAME, bundle.getString("paste"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl V"));
		return action;
	}
	
	public Action getOrderAction(final int orderBy)
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.order(orderBy);
			}
		};
		String actionName="";
		if(orderBy==JohnFileTreeConstants.ORDER_BY_DATE)
		{
			actionName=bundle.getString("order.date");
		}
		else if(orderBy==JohnFileTreeConstants.ORDER_BY_NAME)
		{
			actionName=bundle.getString("order.name");
		}
		else if(orderBy==JohnFileTreeConstants.ORDER_BY_SIZE)
		{
			actionName=bundle.getString("order.size");
		}
		else if(orderBy==JohnFileTreeConstants.ORDER_BY_TYPE)
		{
			actionName=bundle.getString("order.type");
		}
		action.putValue(Action.NAME, actionName);
		return action;
	}
	
	public Action getDeleteAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int z=JOptionPane.showConfirmDialog(null, "确定要删除吗?", "提示", JOptionPane.YES_NO_OPTION);
      	if(z!=0)
      	{
      		return;
      	}
				fileTree.delete();
			}
		};
		action.putValue(Action.NAME, bundle.getString("delete"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
		return action;
	}
	
	public Action getPropertySettingAction(final char prop,final boolean isAdd)
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean containChildren=false;
				if(hasSelectFolder())
				{
					int yes_no_cancel=JOptionPane.showConfirmDialog(null, bundle.getString("ask.samechildren"));
					if(yes_no_cancel==0)
					{
						containChildren=true;
					}
					else if(yes_no_cancel==2)
					{
						return;
					}
				}
				fileTree.setFileProperty(prop,isAdd, containChildren);
			}
		};
		String actionName="";
		if(prop=='R')
		{
			actionName=bundle.getString("fileprop.readonly");
		}
		else if(prop=='H')
		{
			actionName=bundle.getString("fileprop.hidden");
		}
		else if(prop=='S')
		{
			actionName=bundle.getString("fileprop.system");
		}
		action.putValue(Action.NAME, actionName);
		return action;
	}
	
	public Action getLastModifySettingAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final JDialog dialog=new JDialog();
				
				JPanel center=new JPanel();
				final JTextField field=new JTextField(bundle.getString("textfield.datedemo"));
				JButton button=new JButton(bundle.getString("button.ok"));
				center.add(field);
				center.add(button);
				dialog.add(center);
				
				JPanel bottom=new JPanel();
				final JCheckBox checkBox=new JCheckBox(bundle.getString("ask.samechildren"));
				bottom.add(checkBox);
				((FlowLayout)bottom.getLayout()).setAlignment(FlowLayout.RIGHT);
				
				if(hasSelectFolder())
				{
					dialog.add(bottom,BorderLayout.SOUTH);
				}
				
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				
				field.selectAll();
				button.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Date date;
						boolean containChildren=hasSelectFolder()?checkBox.isSelected():false;
						try
						{
							date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(field.getText().trim());
							fileTree.setLastModify(date.getTime(),containChildren);
						} catch (ParseException e1)
						{
							try
							{
								date = new SimpleDateFormat("yyyy-MM-dd").parse(field.getText().trim());
								fileTree.setLastModify(date.getTime(),containChildren);
							} catch (ParseException e2)
							{
								JOptionPane.showMessageDialog(null,bundle.getString("error.dateformat"));
								e2.printStackTrace();
							}
						}finally{
							dialog.dispose();
						}
					}
				});
			}
		};
		action.putValue(Action.NAME, bundle.getString("lastmodify"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl L"));
		return action;
	}
	
	public Action getAddAction(final String type)
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.addFileOrFolder(type);
			}
		};
		String actionName="";
		if(type.equals("file"))
		{
			actionName=bundle.getString("add.file");
		}else{
			actionName=bundle.getString("add.folder");
		}
		action.putValue(Action.NAME, actionName);
		return action;
	}
	
	public Action getRenameAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.renamingFile();
			}
		};
		action.putValue(Action.NAME, bundle.getString("rename"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
		return action;
	}
	
	public Action getFilterHiddenAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileTree.filterHidden();
			}
		};
		action.putValue(Action.NAME, bundle.getString("filterhidden"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl H"));
		return action;
	}
	
	public Action getFilterAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showDialogForMap();
			}
		};
		action.putValue(Action.NAME, bundle.getString("filter"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift F"));
		return action;
	}
	
	protected void showDialogForMap()
	{
		JLabel title=new JLabel(bundle.getString("filter.option"));
		
		JLabel nameLabel=new JLabel(bundle.getString("filter.name"));
		final JComboBox<String> nameOperate=new JComboBox<String>(new String[]{bundle.getString("filter.operate.equal"),bundle.getString("filter.operate.prefix"),bundle.getString("filter.operate.suffix"),bundle.getString("filter.operate.contain"),bundle.getString("filter.operate.expand")});
		final JTextField nameField=new JTextField("");
		
		JLabel sizeLabel=new JLabel(bundle.getString("filter.size"));
		final JComboBox<String> sizeOperate=new JComboBox<String>(new String[]{bundle.getString("filter.operate.equal"),bundle.getString("filter.operate.greater"),bundle.getString("filter.operate.less")});
		final JTextField sizeField=new JTextField(bundle.getString("textfield.size"));
		sizeField.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				sizeField.setText("");
			}
		});
		
		JLabel dateLabel=new JLabel(bundle.getString("filter.date"));
		final JComboBox<String> dateOperate=new JComboBox<String>(new String[]{bundle.getString("filter.operate.equal"),bundle.getString("filter.operate.greater"),bundle.getString("filter.operate.less")});
		final JTextField dateField=new JTextField(bundle.getString("textfield.datedemo"));
		dateField.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				dateField.setText("");
			}
		});
		
		JLabel folderLabel=new JLabel(bundle.getString("filter.folder"));
		JRadioButton folderYes=new JRadioButton(bundle.getString("filter.radio.yes"));
		folderYes.setActionCommand(bundle.getString("filter.radio.yes"));
		JRadioButton folderNo=new JRadioButton(bundle.getString("filter.radio.no"));
		folderNo.setActionCommand(bundle.getString("filter.radio.no"));
		JRadioButton folderAll=new JRadioButton(bundle.getString("filter.radio.all"));
		folderAll.setActionCommand(bundle.getString("filter.radio.all"));
		folderAll.setSelected(true);
		
		JLabel executeLabel=new JLabel(bundle.getString("filter.execute"));
		JRadioButton executeYes=new JRadioButton(bundle.getString("filter.radio.yes"));
		executeYes.setActionCommand(bundle.getString("filter.radio.yes"));
		JRadioButton executeNo=new JRadioButton(bundle.getString("filter.radio.no"));
		executeNo.setActionCommand(bundle.getString("filter.radio.no"));
		JRadioButton executeAll=new JRadioButton(bundle.getString("filter.radio.all"));
		executeAll.setActionCommand(bundle.getString("filter.radio.all"));
		executeAll.setSelected(true);
		
		JLabel readOnlyLabel=new JLabel(bundle.getString("filter.readonly"));
		JRadioButton readOnlyYes=new JRadioButton(bundle.getString("filter.radio.yes"));
		readOnlyYes.setActionCommand(bundle.getString("filter.radio.yes"));
		JRadioButton readOnlyNo=new JRadioButton(bundle.getString("filter.radio.no"));
		readOnlyNo.setActionCommand(bundle.getString("filter.radio.no"));
		JRadioButton readOnlyAll=new JRadioButton(bundle.getString("filter.radio.all"));
		readOnlyAll.setActionCommand(bundle.getString("filter.radio.all"));
		readOnlyAll.setSelected(true);
		
		JLabel hiddenLabel=new JLabel(bundle.getString("filter.hidden"));
		JRadioButton hiddenYes=new JRadioButton(bundle.getString("filter.radio.yes"));
		hiddenYes.setActionCommand(bundle.getString("filter.radio.yes"));
		JRadioButton hiddenNo=new JRadioButton(bundle.getString("filter.radio.no"));
		hiddenNo.setActionCommand(bundle.getString("filter.radio.no"));
		JRadioButton hiddenAll=new JRadioButton(bundle.getString("filter.radio.all"));
		hiddenAll.setActionCommand(bundle.getString("filter.radio.all"));
		hiddenAll.setSelected(true);
		
		JLabel contentLabel=new JLabel(bundle.getString("filter.content"));
		final JTextField contentField=new JTextField("");
		final JCheckBox cascadeCheckBox=new JCheckBox(bundle.getString("filter.cascade"));
		
		JButton okButton=new JButton(bundle.getString("button.ok"));
		
		JPanel jp0=new JPanel();
		JPanel jp1=new JPanel(new GridLayout(3, 3, 10, 10));
		JPanel jp2=new JPanel(new GridLayout(4, 4, 10, 10));
		JPanel jp3=new JPanel(new GridLayout(1, 4, 10, 10));
		JPanel jp4=new JPanel();
		jp1.setBorder(BorderFactory.createTitledBorder(bundle.getString("titlepane.title1")));
		jp2.setBorder(BorderFactory.createTitledBorder(bundle.getString("titlepane.title2")));
		jp3.setBorder(BorderFactory.createTitledBorder(bundle.getString("titlepane.title3")));
		
		jp0.add(title);
		
		jp1.add(nameLabel);
		jp1.add(nameOperate);
		jp1.add(nameField);
		jp1.add(sizeLabel);
		jp1.add(sizeOperate);
		jp1.add(sizeField);
		jp1.add(dateLabel);
		jp1.add(dateOperate);
		jp1.add(dateField);
		
		jp2.add(folderLabel);
		jp2.add(folderYes);
		jp2.add(folderNo);
		jp2.add(folderAll);
		jp2.add(executeLabel);
		jp2.add(executeYes);
		jp2.add(executeNo);
		jp2.add(executeAll);
		jp2.add(readOnlyLabel);
		jp2.add(readOnlyYes);
		jp2.add(readOnlyNo);
		jp2.add(readOnlyAll);
		jp2.add(hiddenLabel);
		jp2.add(hiddenYes);
		jp2.add(hiddenNo);
		jp2.add(hiddenAll);
		
		jp3.add(contentLabel);
		jp3.add(contentField);
		jp3.add(cascadeCheckBox);
		
		jp4.add(okButton);
		
		final ButtonGroup folderGroup=new ButtonGroup();
		folderGroup.add(folderYes);
		folderGroup.add(folderNo);
		folderGroup.add(folderAll);
		final ButtonGroup executeGroup=new ButtonGroup();
		executeGroup.add(executeYes);
		executeGroup.add(executeNo);
		executeGroup.add(executeAll);
		final ButtonGroup readOnlyGroup=new ButtonGroup();
		readOnlyGroup.add(readOnlyYes);
		readOnlyGroup.add(readOnlyNo);
		readOnlyGroup.add(readOnlyAll);
		final ButtonGroup hiddenGroup=new ButtonGroup();
		hiddenGroup.add(hiddenYes);
		hiddenGroup.add(hiddenNo);
		hiddenGroup.add(hiddenAll);
		
		JPanel contentPane=new JPanel();
		BoxLayout bl=new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		contentPane.setLayout(bl);
		contentPane.add(jp0);
		contentPane.add(jp1);
		contentPane.add(jp2);
		contentPane.add(jp3);
		contentPane.add(jp4);
		
		final JDialog dialog=new JDialog();
		dialog.add(contentPane);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setTitle(bundle.getString("filter.title"));
		dialog.pack();
		dialog.setMinimumSize(dialog.getSize());
		dialog.setLocationRelativeTo(null);
		
		okButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final EnumMap<FilterOption, Object> map=new EnumMap<FilterOption, Object>(FilterOption.class);
				
				String nameO=(String)nameOperate.getSelectedItem();
				String dateO=(String)dateOperate.getSelectedItem();
				String sizeO=(String)sizeOperate.getSelectedItem();
				String folderG=folderGroup.getSelection().getActionCommand();
				String executeG=executeGroup.getSelection().getActionCommand();
				String readOnlyG=readOnlyGroup.getSelection().getActionCommand();
				String hiddenG=hiddenGroup.getSelection().getActionCommand();
				boolean cascadeC=cascadeCheckBox.isSelected();
				String nameF=nameField.getText();
				String dateF=dateField.getText();
				String sizeF=sizeField.getText();
				String contentF=contentField.getText();
				
				if(nameO.equals(bundle.getString("filter.operate.equal")))
				{
					map.put(FilterOption.NAME_OPERATE_INTEGER, JohnFileTreeConstants.NAME_OPERATE_EQUAL);
				}
				else if(nameO.equals(bundle.getString("filter.operate.prefix")))
				{
					map.put(FilterOption.NAME_OPERATE_INTEGER, JohnFileTreeConstants.NAME_OPERATE_PREFIX);
				}
				else if(nameO.equals(bundle.getString("filter.operate.suffix")))
				{
					map.put(FilterOption.NAME_OPERATE_INTEGER, JohnFileTreeConstants.NAME_OPERATE_SUFFIX);
				}
				else if(nameO.equals(bundle.getString("filter.operate.contain")))
				{
					map.put(FilterOption.NAME_OPERATE_INTEGER, JohnFileTreeConstants.NAME_OPERATE_CONTAIN);
				}
				else if(nameO.equals(bundle.getString("filter.operate.expand")))
				{
					map.put(FilterOption.NAME_OPERATE_INTEGER, JohnFileTreeConstants.NAME_OPERATE_EXPAND);
				}
				
				if(dateO.equals(bundle.getString("filter.operate.equal")))
				{
					map.put(FilterOption.DATE_OPERATE_INTEGER, JohnFileTreeConstants.DATE_OPERATE_EQUAL);
				}
				else if(dateO.equals(bundle.getString("filter.operate.greater")))
				{
					map.put(FilterOption.DATE_OPERATE_INTEGER, JohnFileTreeConstants.DATE_OPERATE_GREATER);
				}
				else if(dateO.equals(bundle.getString("filter.operate.less")))
				{
					map.put(FilterOption.DATE_OPERATE_INTEGER, JohnFileTreeConstants.DATE_OPERATE_LESS);
				}
				
				if(sizeO.equals(bundle.getString("filter.operate.equal")))
				{
					map.put(FilterOption.SIZE_OPERATE_INTEGER, JohnFileTreeConstants.SIZE_OPERATE_EQUAL);
				}
				else if(sizeO.equals(bundle.getString("filter.operate.greater")))
				{
					map.put(FilterOption.SIZE_OPERATE_INTEGER, JohnFileTreeConstants.SIZE_OPERATE_GREATER);
				}
				else if(sizeO.equals(bundle.getString("filter.operate.less")))
				{
					map.put(FilterOption.SIZE_OPERATE_INTEGER, JohnFileTreeConstants.SIZE_OPERATE_LESS);
				}
				
				if(folderG.equals(bundle.getString("filter.radio.yes")))
				{
					map.put(FilterOption.FOLDER_BOOLEAN, true);
				}
				else if(folderG.equals(bundle.getString("filter.radio.no")))
				{
					map.put(FilterOption.FOLDER_BOOLEAN, false);
				}
				
				if(executeG.equals(bundle.getString("filter.radio.yes")))
				{
					map.put(FilterOption.EXECUTE_BOOLEAN, true);
				}
				else if(executeG.equals(bundle.getString("filter.radio.no")))
				{
					map.put(FilterOption.EXECUTE_BOOLEAN, false);
				}
				
				if(readOnlyG.equals(bundle.getString("filter.radio.yes")))
				{
					map.put(FilterOption.READONLY_BOOLEAN, true);
				}
				else if(readOnlyG.equals(bundle.getString("filter.radio.no")))
				{
					map.put(FilterOption.READONLY_BOOLEAN, false);
				}
				
				if(hiddenG.equals(bundle.getString("filter.radio.yes")))
				{
					map.put(FilterOption.HIDDEN_BOOLEAN, true);
				}
				else if(hiddenG.equals(bundle.getString("filter.radio.no")))
				{
					map.put(FilterOption.HIDDEN_BOOLEAN, false);
				}
				
				if(cascadeC)
				{
					map.put(FilterOption.CASCADE_BOOLEAN, true);
				}else{
					map.put(FilterOption.CASCADE_BOOLEAN, false);
				}
				
				if(!"".equals(nameF))
				{
					map.put(FilterOption.NAME_STRING, nameF);
				}
				
				if(!"".equals(dateF)&&!bundle.getString("textfield.datedemo").equals(dateF))
				{
					Date date=null;
					try
					{
						date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateF);
					} catch (ParseException e1)
					{
						try
						{
							date=new SimpleDateFormat("yyyy-MM-dd").parse(dateF);
						} catch (ParseException e2)
						{
							JOptionPane.showMessageDialog(null, bundle.getString("error.dateformat"));
						}
					}
					if(date!=null)
					{
						map.put(FilterOption.DATE_LONG, date.getTime());
					}
				}
				
				if(!"".equals(sizeF)&&!bundle.getString("textfield.size").equals(sizeF))
				{
					long size;
					if(sizeF.toUpperCase().endsWith("GB"))
					{
					  String[] s=sizeF.toUpperCase().split("GB");
					  size=Long.parseLong(s[0])*1024*1024*1024;
					}
					else if(sizeF.toUpperCase().endsWith("MB"))
					{
						String[] s=sizeF.toUpperCase().split("MB");
						size=Long.parseLong(s[0])*1024*1024;
					}
					else if(sizeF.toUpperCase().endsWith("KB"))
					{
						String[] s=sizeF.toUpperCase().split("KB");
						size=Long.parseLong(s[0])*1024;
					}
					else
					{
						size=Long.parseLong(sizeF);
					}
					map.put(FilterOption.SIZE_LONG, size);
				}
				
				if(!"".equals(contentF))
				{
					map.put(FilterOption.CONTENT_STRING, contentF);
				}
				
				fileTree.filter(map);
				
				dialog.dispose();
			}
		});
		dialog.setVisible(true);
	}
	
	public Action getReplaceAndEncodeAction()
	{
		Action action=new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final JDialog dialog=new JDialog();
				JLabel searchLab=new JLabel(bundle.getString("character.search"));
				final JTextField search=new JTextField();
				JLabel replaceLab=new JLabel(bundle.getString("character.replace"));
				final JTextField replace=new JTextField();
				JLabel decodeLab=new JLabel(bundle.getString("character.decode"));
				final JComboBox<String> decode=new JComboBox<String>(new String[]{"GBK","UTF-8","GB18030","ISO-8859-1","Unicode"});
				JLabel encodeLab=new JLabel(bundle.getString("character.encode"));
				final JComboBox<String> encode=new JComboBox<String>(new String[]{"GBK","UTF-8","GB18030","ISO-8859-1","Unicode"});
				final JCheckBox caseSensitive=new JCheckBox(bundle.getString("character.case"));
				JButton execute=new JButton(bundle.getString("button.ok"));
				
				search.setColumns(16);
				replace.setColumns(16);
				
				execute.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						dialog.dispose();
						String searchStr=search.getText();
						String replaceStr=replace.getText();
						String decodeStr=(String)decode.getSelectedItem();
						String encodeStr=(String)encode.getSelectedItem();
						boolean isCaseSensitive=caseSensitive.isSelected();
						fileTree.replaceAndEncodeForPopupMenu(searchStr, replaceStr, isCaseSensitive, decodeStr, encodeStr);
					}
				});
				
				FlowLayout fl=new FlowLayout(FlowLayout.LEFT);
				JPanel jp1=new JPanel(fl);
				jp1.add(searchLab);
				jp1.add(search);
				JPanel jp2=new JPanel(fl);
				jp2.add(replaceLab);
				jp2.add(replace);
				JPanel jp3=new JPanel(fl);
				jp3.add(decodeLab);
				jp3.add(decode);
				JPanel jp4=new JPanel(fl);
				jp4.add(encodeLab);
				jp4.add(encode);
				JPanel jp5=new JPanel();
				jp5.add(caseSensitive);
				jp5.add(execute);
				JPanel container=new JPanel(new GridLayout(5, 1));
				container.add(jp1);
				container.add(jp2);
				container.add(jp3);
				container.add(jp4);
				container.add(jp5);
				
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setResizable(false);
				dialog.add(container);
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		};
		action.putValue(Action.NAME, bundle.getString("character"));
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl F"));
		return action;
	}
	
}