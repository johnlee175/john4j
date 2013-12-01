package com.johnsoft.library.swing.component.filetree;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.TreePath;

import com.johnsoft.library.swing.component.filetree.JohnFileTreeConstants.FilterOption;

public class JohnFileTree extends JTree
{
	private static final long serialVersionUID = 1L;
	
	protected TreePath dragedPath;
	protected TreePath rollOverPath;
	protected Timer rollOverTimer;
	protected boolean isDragged;
	
	protected int stringCount;
	protected int fileCount;
	
	protected JohnFileTreeNodePopupMenu popup;	
	protected JohnFileTreeCellEditor editor;	
	protected JohnFileTreeCellRenderer renderer;
	protected JohnFileTreeModel model;
	
	public JohnFileTree(File rootFile)
	{
		createModel(rootFile);
		createRenderer();
		createEditor();
		createPopupMenu();
		installDefaults();
		installListeners();
	}
	
	public void createModel(File rootFile)
	{
		JohnFileTreeNode root=new JohnFileTreeNode(rootFile);
		iterate(rootFile,root);
		if(model==null)
		{
			model=new JohnFileTreeModel(root);
		}else{
			model.setRoot(root);
		}
		setModel(model);
	}
	
	protected void createRenderer()
	{
		renderer=new JohnFileTreeCellRenderer();
		setCellRenderer(renderer);
	}
	
	protected void createEditor()
	{
		editor=new JohnFileTreeCellEditor(this, renderer);
		setCellEditor(editor);
	}
	
	protected void createPopupMenu()
	{
		popup=new JohnFileTreeNodePopupMenu(this);
	}
	
	protected void installDefaults()
	{
		setEditable(true);
		collapsePath(new TreePath(getModel().getRoot()));
	}
	
	protected void installListeners()
	{
		rollOverTimer=new Timer(1000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(rollOverPath!=null&&rollOverPath.equals(getSelectionPath()))
				{
					setExpandedState(getSelectionPath(), true);
				}	
			}
		});
		rollOverTimer.setRepeats(false);
		
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(isSelectionEmpty())
				{
					return;
				}
				boolean flag=false;
				for(TreePath path:getSelectionPaths())
				{
					if(getPathBounds(path)!=null&&getPathBounds(path).contains(e.getX(), e.getY()))
					{
						flag=true;
					}
				}
				if(flag&&e.getButton()==MouseEvent.BUTTON3)
				{
					dragedPath=null;
					popup.setSelectedNodes(getSelectedNodes());
					popup.show(JohnFileTree.this, e.getX(), e.getY());
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				setCursor(Cursor.getDefaultCursor());
				if(e.getButton()==MouseEvent.BUTTON3||e.getButton()==MouseEvent.BUTTON2)
				{
					return;
				}
				TreePath tp=getPathForLocation(e.getX(), e.getY());
				if(tp!=null&&dragedPath!=null&&isDragged)
				{
					isDragged=false;
          if (dragedPath.isDescendant(tp) && !dragedPath.equals(tp))  
          {  
              JOptionPane.showMessageDialog(null, "不允许向子节点拖动！",   
                  "警告", JOptionPane.WARNING_MESSAGE);  
              return;  
          }  
          else if (!dragedPath.equals(tp))  
          { 
          	JohnFileTreeNode dragNode=(JohnFileTreeNode)dragedPath.getLastPathComponent();
          	JohnFileTreeNode dropNode=(JohnFileTreeNode)tp.getLastPathComponent();
          	File dragFile=dragNode.getNodeFile();
          	File dropFile=dropNode.getNodeFile();
          	if(dragedPath.getParentPath().equals(tp))
          	{
          		int x=JOptionPane.showConfirmDialog(null, "确定要复制吗?", "提示", JOptionPane.YES_NO_OPTION);
            	if(x!=0)
            	{
            		return;
            	}
          		File file=getNewNamedChildFile(dropFile, dragFile.getName());
          		if(file.exists())
          		{
          			file.delete();
          		}
          		if(dragFile.isDirectory())
          		{
          			file.mkdir();
          		}else{
          			try
								{
									file.createNewFile();
									BufferedInputStream bis=new BufferedInputStream(new FileInputStream(dragFile));
									BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
									int len;
									byte[] bytes=new byte[bis.available()];
									while((len=bis.read(bytes))>0)
									{
										bos.write(bytes, 0, len);
									}
									bis.close();
									bos.close();
								} catch (IOException e1)
								{
									e1.printStackTrace();
								}
          		}
          		JohnFileTreeNode node=new JohnFileTreeNode(file);
          		dropNode.add(node);
          		updateUI();
          		editor.beginEditState(new TreePath(node.getPath()));
          	}else{
          		int y=JOptionPane.showConfirmDialog(null, "确定要移动吗?", "提示", JOptionPane.YES_NO_OPTION);
            	if(y!=0)
            	{
            		return;
            	}
          		JohnFileTreeNode backupNode=(JohnFileTreeNode)dragNode.getParent();
          		File backupFile=dragFile.getParentFile();
          		if(dropFile.isDirectory())
          		{
          			dropNode.add(dragNode);
          			iteratePaste(dragFile, dropFile, true);
          			dropNode.removeAllChildren();
          			iterate(dropFile, dropNode);
          			setExpandedState(new TreePath(dropNode.getPath()), true);
          			backupNode.removeAllChildren();
          			iterate(backupFile, backupNode);
          			setExpandedState(new TreePath(backupNode.getPath()), true);
          		}else{
          			JohnFileTreeNode parent=(JohnFileTreeNode)dropNode.getParent();
          			parent.insert(dragNode,parent.getIndex(dropNode));
          			if(!dragedPath.getParentPath().equals(tp.getParentPath()))
          			{
          				iteratePaste(dragFile, dropFile.getParentFile(), true);
          				parent.removeAllChildren();
          				iterate(dropFile.getParentFile(), parent);
          				setExpandedState(new TreePath(parent.getPath()), true);
          				backupNode.removeAllChildren();
          				iterate(backupFile, backupNode);
          				setExpandedState(new TreePath(backupNode.getPath()), true);
          			}
          		}
          	}
            dragedPath = null;  
            updateUI();
            if(getSelectionPath()!=null)
          	{
            	expandPath(getSelectionPath().pathByAddingChild(new JohnFileTreeNode(dragFile)));
          		//setExpandedState(getSelectionPath(), true);
          	}
          }  
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton()==MouseEvent.BUTTON1)
				{
					TreePath tp=getPathForLocation(e.getX(), e.getY());
					if(tp!=null)
					{
						dragedPath=tp;
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				isDragged=true;
				setCursor(DragSource.DefaultMoveDrop);
				TreePath tp=getClosestPathForLocation(e.getX(), e.getY());
				if(tp!=null)
				{
					setSelectionPath(tp);
					if(tp.equals(rollOverPath))
					{
						if(!rollOverTimer.isRunning())
						{
							rollOverTimer.start();
						}
					}else{
						if(rollOverTimer.isRunning())
						{
							rollOverTimer.stop();
						}
						scrollRowToVisible(getRowForPath(tp)-1);
						scrollRowToVisible(getRowForPath(tp)+1);
						rollOverPath=tp;
					}
				}
			 }
		});
	}
	
	//菜单方法
	
	public void closeOrOpenTree(boolean isOpen)
	{
		iterateExpanded(new TreePath(((JohnFileTreeNode)getModel().getRoot()).getPath()), isOpen);
	}
	
	public void refresh(int domain)
	{
		if(domain==JohnFileTreeConstants.REFRESH_ALL_TREE)
		{
			JohnFileTreeNode root=(JohnFileTreeNode)getModel().getRoot();
			createModel(root.getNodeFile());
		}
		else if(domain==JohnFileTreeConstants.REFRESH_ALL_TREE)
		{
			for(JohnFileTreeNode node:getSelectedNodes())
			{
				node.removeAllChildren();
				iterate(getSelectedFile(),node);
			}
		}
		updateUI();
	}
	
	public void browse(int openWith)
	{
		File[] files=getSelectedFiles();
		Runtime driver=Runtime.getRuntime();
		for(File file:files)
		{
			try
			{
				String command="";
				if(file.isDirectory())
				{
					command="cmd.exe /c start \"\" \""+file.getAbsolutePath()+"\"";
				}else{
					if(openWith==JohnFileTreeConstants.BROWSE_WITH_DEFAULT)
					{
						command="cmd.exe /c start \"\" \""+file.getAbsolutePath()+"\"";
					}
					else if(openWith==JohnFileTreeConstants.BROWSE_WITH_NOTEPAD)
					{
						command="notepad.exe \""+file.getAbsolutePath()+"\"";
					}
				}
				driver.exec(command);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void copy()
	{
		Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard(); 
		clip.setContents(new JohnFileTransfer(Arrays.asList(getSelectedFiles())), null);
	}
	
	@SuppressWarnings("unchecked")
	public void paste()
	{
		Clipboard clip=null; 
		List<File> sourceFiles=null;
		File[] targetFiles=null;
		try
		{
			clip=Toolkit.getDefaultToolkit().getSystemClipboard();
			sourceFiles=(List<File>)clip.getData(DataFlavor.javaFileListFlavor);
			targetFiles=getSelectedFiles();
		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "树节点只能粘贴文件或文件夹" ,"警告" ,JOptionPane.WARNING_MESSAGE);
			return;
		} 
		for(File tf:targetFiles)
		{
			if(tf.isDirectory())
			{
				for(File sf:sourceFiles)
				{
					if(System.getProperty("os.name").equals("Windows XP"))
					{
						try
						{ 
							String name=sf.isDirectory()?"\\"+sf.getName():"";
							String option=sf.isDirectory()?"/e /h /k /i ":"";
							String command="cmd.exe /c xcopy \""+sf.getAbsolutePath()+"\" \""+tf.getAbsolutePath()+name+"\" "+option;
							Runtime.getRuntime().exec(command);
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}else{
						iteratePaste(sf, tf,false);
					}
				}
			}
		}
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		for(JohnFileTreeNode node:getSelectedNodes())
		{
			node.removeAllChildren();
			iterate(getSelectedFile(),node);
		}
		updateUI();
	}
	
	@SuppressWarnings("unchecked")
	public void order(int orderBy)
	{
		JohnFileTreeNode node=getSelectedNode();
		Enumeration<JohnFileTreeNode> e=node.children();
		List<JohnFileTreeNode> list=Collections.list(e);
		if(orderBy==JohnFileTreeConstants.ORDER_BY_TYPE)
		{
			Collections.sort(list,new TreeTypeOrder());
		}
		else if(orderBy==JohnFileTreeConstants.ORDER_BY_DATE)
		{
			Collections.sort(list,new TreeDateOrder());
		}
		else if(orderBy==JohnFileTreeConstants.ORDER_BY_SIZE)
		{
			Collections.sort(list,new TreeSizeOrder());
		}
		else if(orderBy==JohnFileTreeConstants.ORDER_BY_NAME){	
			Collections.sort(list,new TreeNameOrder());
		}
		node.removeAllChildren();
		for(JohnFileTreeNode n:list)
		{
			node.add(n);
		}
		updateUI();
	}
	
	public void delete()
	{
		JohnFileTreeNode[] nodes=getSelectedNodes();
		for(JohnFileTreeNode node:nodes)
		{
			File file=node.getNodeFile();
			if(file.exists())
			{
				iterateDelete(file);
			}
			node.removeFromParent();
			updateUI();
		}
	}
	
	/**
	 * @param prop 只读为R,隐藏为H,改为系统文件为S
	 * @param isAdd 设置还是取消
	 * @param containChildren 是否应用于子文件夹 
	 */
	public void setFileProperty(char prop,boolean isAdd,boolean containChildren)
	{
		Runtime driver=Runtime.getRuntime();
		String flag=isAdd?"+":"-";
		File[] files=getSelectedFiles();
		for(File f:files)
		{
			try
			{
				if(containChildren)
				{
					driver.exec("attrib "+flag+prop+" \""+f.getAbsolutePath()+"\\*\" \\S \\D");
				}
				driver.exec("attrib "+flag+prop+" \""+f.getAbsolutePath()+"\"");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setLastModify(long time,boolean containChildren)
	{
		File[] files=getSelectedFiles();
		for(File f:files)
		{
			f.setLastModified(time);
			if(containChildren)
			{
				iterateSetModify(f, time);
			}
		}
	}
	
	public void addFileOrFolder(String type)
	{
		if(type==null||(!type.equals("file")&&!type.equals("folder")))
		{
			throw new RuntimeException("the possibly value of type is \"file\" or \"folder\"");
		}
		File file=getSelectedFile();
		if(file.isFile())
		{
			JOptionPane.showMessageDialog(null, "文件中不能包含文件或文件夹,请确定","警告",JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if(file.isDirectory())
		{
			File childFile=getNewNamedChildFile(file, type);
			try
			{
				if(type.equals("file"))
				{
					childFile.createNewFile();
				}
				else if(type.equals("folder"))
				{
					childFile.mkdir();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			JohnFileTreeNode node=new JohnFileTreeNode(childFile);
			getSelectedNode().add(node);
			updateUI();
			editor.beginEditState(new TreePath(node.getPath()));
		}
	}
	
	public void renamingFile()
	{
		editor.beginEditState(getSelectionPath());
	}
	
	public void filterHidden()
	{
		for(JohnFileTreeNode node:getSelectedNodes())
		{
			node.removeAllChildren();
			iterateFilterHidden(node.getNodeFile(), node);
		}
		updateUI();
	}
	
	public void filterNamePrefix(String prefix)
	{
		getRootNode().removeAllChildren();
		iterateFilterNamePrefix(getRootFile(),prefix);
		updateUI();
	}
	
	public void filter(EnumMap<FilterOption, Object> map)
	{
		for(JohnFileTreeNode node:getSelectedNodes())
		{
			node.removeAllChildren();
			iterateFilter(node.getNodeFile(), node, map);
		}
		updateUI();
	}
	
	public void replaceAndEncodeForPopupMenu(String searchStr,String replaceStr,boolean isCaseSensitive,String decodeStr,String encodeStr)
	{
		fileCount=stringCount=0;
		for(File f:getSelectedFiles())
		{
			iterateReplaceAndEncode(f,searchStr,replaceStr,isCaseSensitive,decodeStr,encodeStr);
		}
		JOptionPane.showMessageDialog(null,"成功替换了"+fileCount+"份文件"+stringCount+"个匹配字符!","SUCCESS",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void renameAllSameTypeFileInFolderWithRule(String fileType,String reg,String searchStr,String replaceStr)
	{
		File file=getSelectedFile();
		if(file.isFile())
		{
			return;
		}else{
			File[] files=file.listFiles();
			for(File f:files)
			{
				String temp="";
				String name=f.getName();
				if(name.endsWith(fileType))
				{
					if(searchStr!=null&&!searchStr.equals("")&&replaceStr!=null)
					{
						String[] strs=name.split(searchStr);
						for(int i=0;i<strs.length;i++)
						{
							temp+=strs[i]+replaceStr;
						}
						name=temp.substring(0, temp.length()-replaceStr.length());
					}
					
				}
			}
		}
	}
	
	//核心迭代
	
	protected void iterate(File file,JohnFileTreeNode parent)
	{
		File[] files=file.listFiles();
		if(files!=null)
		{
			for(File f:files)
			{
				JohnFileTreeNode child=new JohnFileTreeNode(f);
				parent.add(child);
				iterate(f,child);
			}
		}
	}

	protected void iterateSetModify(File file,long time)
	{
		File[] files=file.listFiles();
		if(files!=null)
		{
			for(File f:files)
			{
				f.setLastModified(time);
				iterateSetModify(f,time);
			}
		}
	}
	
	protected void iterateDelete(File file)
	{
		if(file.isFile())
		{
			file.delete();
			return;
		}else{
			for(File f:file.listFiles())
			{
				iterateDelete(f);
			}
			if(file.listFiles().length==0)
			{
				file.delete();
			}
		}
	}
	
	protected void iteratePaste(File sourceFile,File targetFolder,boolean isFromCut)
	{
		if(sourceFile.isFile())
		{
			try
			{
				BufferedInputStream bis=new BufferedInputStream(new FileInputStream(sourceFile));
				File file=new File(targetFolder.getAbsolutePath()+"/"+sourceFile.getName());
				if(!file.exists())
				{
					file.createNewFile();
				}
				BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
				int len;
				byte[] bytes=new byte[bis.available()];
				while((len=bis.read(bytes))>0)
				{
					bos.write(bytes, 0, len);
				}
				bis.close();
				bos.close();
				if(isFromCut)
				{
					sourceFile.delete();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}else{
			File file=new File(targetFolder.getAbsolutePath()+"/"+sourceFile.getName());
			if(!file.exists())
			{
				file.mkdir();
			}
			for(File f:sourceFile.listFiles())
			{
				iteratePaste(f,file,isFromCut);
			}
			if(isFromCut&&sourceFile.listFiles().length==0)
			{
				sourceFile.delete();
			}
		}
	}
	
	protected String iterateFileName(File file,String name,String base,int index)
	{
		for(File f:file.listFiles())
	  {
	  	if(f.getName().equals(name))
	  	{
	  		name="("+(index+1)+")"+base;
	  		name=iterateFileName(file,name,base,index+1);
	  	}
	  }
		return name;
	}
	
	@SuppressWarnings("unchecked")
	protected void iterateExpanded(TreePath parent,boolean setExpanded)
	{
		JohnFileTreeNode node = (JohnFileTreeNode) parent.getLastPathComponent();
	  if (node.getChildCount() > 0)
	  { 
	  	 Enumeration<JohnFileTreeNode> e = node.children();
		   while (e.hasMoreElements())
		   {
		  	JohnFileTreeNode n = (JohnFileTreeNode) e.nextElement();
		    TreePath path = parent.pathByAddingChild(n);
		    iterateExpanded(path, setExpanded);
		   }      
	  }       
	  if (setExpanded) 
	  { 
	  	expandPath(parent);       
	  } else {
	    collapsePath(parent);      
	   } 
	}
	
	@SuppressWarnings("unchecked")
	protected void iterateFilterNamePrefix(File file,String prefix)
	{
		File[] files=file.listFiles();
		if(files!=null)
		{
			for(File f:files)
			{
				if(f.getName().startsWith(prefix))
				{
					JohnFileTreeNode node=new JohnFileTreeNode(f);
					Enumeration<JohnFileTreeNode> children=getRootNode().children();
					boolean isSame=false;
					while(children.hasMoreElements())
					{
						if(f.equals(children.nextElement().getNodeFile()))
						{
							isSame=true;
						}
					}
					if(!isSame)
					{
						getRootNode().add(node);
						iterate(f, node);
					}
				}
				iterateFilterNamePrefix(f,prefix);
			}
		}
	}
	
	protected void iterateReplaceAndEncode(File file,String searchStr,String replaceStr,boolean isCaseSensitive,String decodeStr,String encodeStr)
	{
		File[] files=file.listFiles();
		if(files!=null)
		{
			for(File f:files)
			{
				if(f.isFile())
				{
					replaceAndEncode(f, searchStr, replaceStr, isCaseSensitive, decodeStr, encodeStr);
				}else{
					iterateReplaceAndEncode(f, searchStr, replaceStr, isCaseSensitive, decodeStr, encodeStr);
				}
			}
		}
	}
	
	protected void iterateFilterHidden(File file,JohnFileTreeNode parent)
	{
		File[] files=file.listFiles();
		if(files!=null)
		{
			for(File f:files)
			{
				if(!f.isHidden())
				{
					JohnFileTreeNode child=new JohnFileTreeNode(f);
					parent.add(child);
					iterateFilterHidden(f,child);
				}
			}
		}
	}
	
	protected void iterateFilter(File file,JohnFileTreeNode parent,EnumMap<FilterOption, Object> map)
	{
		File[] files=file.listFiles();
		if(files!=null)
		{
			for(File f:files)
			{
				if(complexFilterOption(f,map))
				{//如果能通过综合过滤
					JohnFileTreeNode child=new JohnFileTreeNode(f);
					parent.add(child);
					if(map.get(FilterOption.CASCADE_BOOLEAN)!=null&&(Boolean)map.get(FilterOption.CASCADE_BOOLEAN))
					{//对后代节点过滤
						iterateFilter(f,child,map);
					}else{//后代节点照常添加
						iterate(f,child);
					}
					if((child.isFolder()&&child.getChildCount()==0)
							&&!(map.get(FilterOption.FOLDER_BOOLEAN)!=null&&(Boolean)map.get(FilterOption.FOLDER_BOOLEAN)))
					{//如果是空文件夹不显示,除非仅搜索文件夹的情况
						parent.remove(child);
					}
				}
			}
		}
	}
	
	protected boolean complexFilterOption(File file,EnumMap<FilterOption, Object> map)
	{
		Object obj,operate;//为空即为忽略,全部符合条件,不过滤
		
		obj=map.get(FilterOption.FOLDER_BOOLEAN);
		if(obj!=null)
		{
			if((Boolean)obj)
			{//筛选文件夹
				if(file.isFile())
				{
					return false;
				}
			}else if(!(Boolean)obj)
			{//筛选文件
				if(file.isDirectory())
				{
					return false;
				}
			}
		}
		
		obj=map.get(FilterOption.EXECUTE_BOOLEAN);
		if(obj!=null)
		{
			if((Boolean)obj)
			{//筛选可执行
				if(!file.canExecute())
				{
					return false;
				}
			}
			else if(!(Boolean)obj)
			{//筛选不可执行
				if(file.canExecute())
				{
					return false;
				}
			}
		}
		
		obj=map.get(FilterOption.READONLY_BOOLEAN);
		if(file.isFile()&&obj!=null)
		{//文件夹视为只读
			if((Boolean)obj)
			{//筛选只读
				if(!(file.canRead()&&!file.canWrite()))
				{
					return false;
				}
			}
			else if(!(Boolean)obj)
			{//筛选非只读
				if(!(file.canRead()&&file.canWrite()))
				{
					return false;
				}
			}
		}
		
		obj=map.get(FilterOption.HIDDEN_BOOLEAN);
		if(obj!=null)
		{
			if((Boolean)obj)
			{//筛选隐藏
				if(!file.isHidden())
				{
					return false;
				}
			}
			else if(!(Boolean)obj)
			{//筛选非隐藏
				if(file.isHidden())
				{
					return false;
				}
			}
		}
		
		obj=map.get(FilterOption.SIZE_LONG);
		if(obj!=null)
		{
			Long size=(Long)obj;
			if(file.isDirectory()&&size!=0)
			{//如果是文件夹且筛选条件中没有尺寸等于0,即文件夹尺寸视为0,这一项则不能通过过滤
				return false;
			}
			operate=map.get(FilterOption.SIZE_OPERATE_INTEGER);
			if(operate!=null)
			{
				Integer oper=(Integer)operate;
				if(oper==JohnFileTreeConstants.SIZE_OPERATE_GREATER)
				{//筛选文件大小大于size
					if(file.length()<=size)
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.SIZE_OPERATE_LESS)
				{//筛选文件大小小于size
					if(file.length()>=size)
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.SIZE_OPERATE_EQUAL)
				{//筛选文件大小等于size
					if(file.length()!=size)
					{
						return false;
					}
				}
			}else{//定义了尺寸过滤项却未给出过滤条件则抛出异常
				throw new RuntimeException("size operate is undefined");
			}
		}
		
		obj=map.get(FilterOption.DATE_LONG);
		if(obj!=null)
		{
			Long date=(Long)obj;
			operate=map.get(FilterOption.DATE_OPERATE_INTEGER);
			if(operate!=null)
			{
				Integer oper=(Integer)operate;
				if(oper==JohnFileTreeConstants.DATE_OPERATE_GREATER)
				{//筛选最后修改时间大于date
					if(file.lastModified()<=date)
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.DATE_OPERATE_LESS)
				{//筛选最后修改时间小于date
					if(file.lastModified()>=date)
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.DATE_OPERATE_EQUAL)
				{//筛选最后修改时间等于date
					if(file.lastModified()!=date)
					{
						return false;
					}
				}
			}else{//定义了修改时间过滤项却未给出过滤条件则抛出异常
				throw new RuntimeException("date operate is undefined");
			}
		}
		
		obj=map.get(FilterOption.NAME_STRING);
		if(obj!=null)
		{
			String name=(String)obj;
			operate=map.get(FilterOption.NAME_OPERATE_INTEGER);
			if(operate!=null)
			{
				Integer oper=(Integer)operate;
				if(oper==JohnFileTreeConstants.NAME_OPERATE_EQUAL)
				{//筛选文件或文件夹名为name
					if(!file.getName().equals(name))
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.NAME_OPERATE_PREFIX)
				{//筛选文件或文件夹名以name开头
					if(!file.getName().split("\\.")[0].startsWith(name))
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.NAME_OPERATE_SUFFIX)
				{//筛选文件或文件夹名以name结尾
					if(!(file.getName().split("\\.")[0].endsWith(name)))
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.NAME_OPERATE_CONTAIN)
				{//筛选文件或文件夹名包含name
					if(file.getName().split("\\.")[0].indexOf(name)>=0)
					{
						return false;
					}
				}
				else if(oper==JohnFileTreeConstants.NAME_OPERATE_EXPAND)
				{//筛选扩展名为name
					String[] strs=file.getName().split("\\.");
					if(!strs[strs.length-1].equals(name))
					{
						return false;
					}
				}
			}else{//定义了文件或文件夹名称过滤项却未给出过滤条件则抛出异常
				throw new RuntimeException("name operate is undefined");
			}
		}
		
		obj=map.get(FilterOption.CONTENT_STRING);
		if(obj!=null)
		{
			String content=(String)obj;
			try
			{//筛选文件中包含字符为content的文件,byte IO流作StringBuilder拼接,依split后的长度判断
				FileInputStream fis=new FileInputStream(file);
				BufferedInputStream bis=new BufferedInputStream(fis);
				int len;
				byte[] bytes=new byte[bis.available()];
				StringBuilder sb=new StringBuilder();
				while((len=bis.read(bytes))>=0)
				{
					sb.append(new String(bytes,0,len));
				}
				bis.close();
				fis.close();
				String result=sb.toString();
				String[] strs=result.split(content);
				if(strs.length-1<=0)
				{
					return false;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	//辅助方法或类
	
	public JohnFileTreeNode getSelectedNode()
	{
		if(getSelectionPath()!=null)
		{
			return (JohnFileTreeNode)getSelectionPath().getLastPathComponent();
		}
		return null;
	}
		
  public JohnFileTreeNode[] getSelectedNodes()
	{
		JohnFileTreeNode[] nodes=new JohnFileTreeNode[getSelectionCount()];
		for(int i=0;i<nodes.length;i++)
		{
			nodes[i]=(JohnFileTreeNode)getSelectionPaths()[i].getLastPathComponent();
		}
		return nodes;
	}
  
  public JohnFileTreeNode getRootNode()
  {
    return	(JohnFileTreeNode)getModel().getRoot();
  }
  
	public File getSelectedFile()
	{
		if(getSelectedNode()!=null)
		{
			return getSelectedNode().getNodeFile();
		}
		return null;
	}
	
	public File[] getSelectedFiles()
	{
		JohnFileTreeNode[] nodes=getSelectedNodes();
		File[] files=new File[nodes.length];
		for(int i=0;i<files.length;i++)
		{
			files[i]=nodes[i].getNodeFile();
		}
		return files;
	}
	
	public File getRootFile()
	{
		return getRootNode().getNodeFile();
	}
	
	public File renameFile(String newName)
	{
		File file=getSelectedFile();
		File newFile=new File(file.getParent()+"/"+newName);
		file.renameTo(newFile);
		getSelectedNode().setUserObject(newFile);
		return newFile;
	}
	
	public void refresh(JohnFileTreeNode node)
	{
		File file=node.getNodeFile();
		node.removeAllChildren();
		iterate(file,node);
		updateUI();
	}
	
	public File getNewNamedChildFile(File file,String type)
	{
		  if(file==null||file.isFile()||type==null)
		  {
		  	throw new IllegalArgumentException("null and non-folder isn't allowed");
		  }
		  String baseName=null;
			if(type.equals("file"))
			{
				baseName="新建文本文档.txt";
			}
			else if(type.equals("folder"))
			{
				baseName="新建文件夹";
			}
			else
			{
				baseName="复制"+type;
			}
			baseName=iterateFileName(file,baseName,baseName,0);
		  return new File(file.getAbsolutePath()+"/"+baseName);
	}
	
	public String getSelectedFileContent(String charset)
	{
		String result="";
		File file=getSelectedFile();
		if(file==null||!file.exists()||file.isDirectory()||file.length()==0)
		{
			return "";
		}
		try
		{
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			StringBuilder sb=new StringBuilder();
			byte[] bytes=new byte[bis.available()];
			int len=0;
			while((len=bis.read(bytes))>=0)
			{
				if(charset!=null&&!"".equals(charset))
				{
					sb.append(new String(bytes,0,len,charset));
				}else{
					sb.append(new String(bytes,0,len));
				}
			}
			bis.close();
			fis.close();
			result=sb.toString();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void replaceAndEncode(File file,String searchStr,String replaceStr,boolean isCaseSensitive,String decodeStr,String encodeStr)
	{
		if(!file.exists()||file.isDirectory()||!file.canWrite())
		{
			return;
		}
		StringBuffer sb=new StringBuffer();
		String result="",pattern="";
		try
		{
			//按原编码解码读入
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			byte[] bytes=new byte[bis.available()];
			int len=0;
			while((len=bis.read(bytes))>=0)
			{
				if(decodeStr!=null&&!"".equals(decodeStr))
				{
					sb.append(new String(bytes,0,len,decodeStr));
				}else{
					sb.append(new String(bytes,0,len));
				}
			}
			bis.close();
			fis.close();
			//分隔替换字符
			if(searchStr!=null&&replaceStr!=null&&!"".equals(searchStr))
			{
				if(isCaseSensitive)
				{
					pattern=searchStr;
				}else{
					pattern="(?i)"+searchStr;
				}
				String[] strs=sb.toString().split(pattern);
				if(strs.length>1)
				{
					fileCount++;
				}
				sb=sb.delete(0, sb.length());
				replaceStr=replaceStr.equals("")?" ":replaceStr;
				for(int i=0;i<strs.length;i++)
				{
					sb.append(strs[i]+replaceStr);
					if(i<strs.length-1)
					{
						stringCount++;
					}
				}
				result=sb.substring(0, sb.length()-replaceStr.length());
			}else{
				result=sb.toString();
			}
			//按新编码编码写入
			FileOutputStream fos=new FileOutputStream(file);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			if(encodeStr!=null&&!"".equals(encodeStr))
			{
				bos.write(result.getBytes(encodeStr));
			}else{
				bos.write(result.getBytes());
			}
			bos.close();
			fos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected class TreeTypeOrder implements Comparator<JohnFileTreeNode>
	{
		@Override
		public int compare(JohnFileTreeNode o1, JohnFileTreeNode o2)
		{
			File file1=o1.getNodeFile();
			File file2=o2.getNodeFile();
			String name1=file1.getName();
			String name2=file2.getName();
			if(file1.isDirectory()&&file2.isDirectory())
			{
				return file1.compareTo(file2);
			}
			else if(file1.isDirectory()&&file2.isFile())
			{
				return 1;
			}
			else if(file1.isFile()&&file2.isDirectory())
			{
				return -1;
			}
			else
			{
				String[] s1=name1.split("\\.");
				String[] s2=name2.split("\\.");
				if(s1.length==1&&s2.length==1)
				{
					return s1[0].compareTo(s2[0]);
				}
				else if(s1.length>s2.length)
				{
					return -1;
				}
				else if(s1.length<s2.length)
				{
					return 1;
				}
				else if(s1.length>1&&s2.length>1)
				{
					return s1[s1.length-1].compareTo(s2[s2.length-1]);
				}
			}
			return 0;
		}
	}
	
	protected class TreeDateOrder implements Comparator<JohnFileTreeNode>
	{
		@Override
		public int compare(JohnFileTreeNode o1, JohnFileTreeNode o2)
		{
			File file1=o1.getNodeFile();
			File file2=o2.getNodeFile();
			if(file1.lastModified()<file2.lastModified())
			{
				return 1;
			}
			else if(file1.lastModified()<file2.lastModified())
			{
				return -1;
			}
			return 0;
		}
	}
		
	protected class TreeSizeOrder implements Comparator<JohnFileTreeNode>
	{
		@Override
		public int compare(JohnFileTreeNode o1, JohnFileTreeNode o2)
		{
			File file1=o1.getNodeFile();
			File file2=o2.getNodeFile();
			if(file1.isDirectory()&&file2.isFile())
			{
				return 1;
			}
			else if(file1.isFile()&&file2.isDirectory())
			{
				return -1;
			}
			else if(file1.isFile()&&file2.isFile())
			{
				if(file1.length()<file2.length())
				{
					return 1;
				}
				else if(file1.length()>file2.length())
				{
					return -1;
				}
			}
			return 0;
		}
	}
	
	protected class TreeNameOrder implements Comparator<JohnFileTreeNode>
	{
		@Override
		public int compare(JohnFileTreeNode o1, JohnFileTreeNode o2)
		{
			File file1=o1.getNodeFile();
			File file2=o2.getNodeFile();
			return file1.compareTo(file2);
		}
	}

}
