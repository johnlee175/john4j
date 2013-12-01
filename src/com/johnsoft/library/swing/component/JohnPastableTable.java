package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JohnPastableTable 
{
	private Clipboard clip;
	private DefaultTableModel model;
	private int modelRow=200,modelCol=26;
	private boolean isCross=false;
	private JScrollPane jScrollPane;
	private JTable jTable;
	private JTable jRowHeader;
	private JLabel upperLeftCorner;
	private JLabel lowerLeftCorner;
	private Map<Integer, Integer> colWidths=new HashMap<Integer, Integer>();
	private List<Cell> hasDataCell=new ArrayList<Cell>();
	
	public JohnPastableTable()
	{
		clip=Toolkit.getDefaultToolkit().getSystemClipboard();
		
		model=new DefaultTableModel(modelRow,modelCol);
		
		jRowHeader=new JTable(modelRow, 1);
		jRowHeader.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 1L;
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row,
					int column)
			{
				JLabel jl=(JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
						row, column);
				jl.setOpaque(true);
				jl.setHorizontalAlignment(JLabel.CENTER);
				if(isSelected)
				{
					jl.setBackground(SystemColor.controlLtHighlight);
					jl.setForeground(Color.RED);
				}else{
				jl.setBackground(SystemColor.control);
					jl.setForeground(Color.BLACK);
				}
				jl.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SystemColor.controlShadow));
				return jl;
			}
		});

		jTable=new JTable(model);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTable.setRowSelectionAllowed(true);
		jTable.setColumnSelectionAllowed(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		
		jTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 1L;
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row,
					int column)
			{
				JLabel jl=(JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
						row, column);
				jl.setOpaque(true);
				jl.setHorizontalAlignment(JLabel.CENTER);
			  jl.setBackground(SystemColor.control);
				jl.setForeground(Color.BLACK);
				jl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, SystemColor.controlDkShadow));
				return jl;
			}
		});
		
		jTable.setSelectionModel(jRowHeader.getSelectionModel());
		jRowHeader.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				jTable.setColumnSelectionAllowed(false);
			}
		});
		jTable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				jTable.setColumnSelectionAllowed(true);
			}
		});
		
		jScrollPane=new JScrollPane();
		JViewport jViewport=new JViewport();
		jViewport.setView(jRowHeader);
		jViewport.setPreferredSize(new Dimension(80,jViewport.getPreferredSize().height));
		jScrollPane.setRowHeader(jViewport);
		jScrollPane.setViewportView(jTable);
		
		upperLeftCorner=new JLabel();
		upperLeftCorner.setBorder(BorderFactory.createRaisedBevelBorder());
		jScrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER,upperLeftCorner);
		lowerLeftCorner=new JLabel();
		lowerLeftCorner.setBorder(BorderFactory.createRaisedBevelBorder());
		jScrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER,lowerLeftCorner);
		JLabel jLabel3=new JLabel();
		jLabel3.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, SystemColor.controlShadow));
		jScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER,jLabel3);
		setLowerLeftCornerText();
	}
	
	public void setCross(boolean isCross)
	{
		this.isCross=isCross;
		setLowerLeftCornerText();
	}
	
	public boolean isCross()
	{
		return this.isCross;
	}
	
	public void setLowerLeftCornerText()
	{
		if(isCross)
		{
			lowerLeftCorner.setText("转置状态");
			lowerLeftCorner.setForeground(Color.RED);
		}else{
			lowerLeftCorner.setText("非转置状态");
			lowerLeftCorner.setForeground(Color.BLACK);
		}
		lowerLeftCorner.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public JTable getJTable()
	{
		return this.jTable;
	}
	
	public JTable getJRowHeader()
	{
		return this.jRowHeader;
	}
	
	public JScrollPane getJScrollPane()
	{
		return this.jScrollPane;
	}
	
	public void setCellCount(int modelRow,int modelCol)
	{
		this.modelRow=modelRow;
		this.modelCol=modelCol;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<File> readCopyedFiles()
	{
		List<File> list=new ArrayList<File>();
		try
		{
			list=(List<File>)clip.getData(DataFlavor.javaFileListFlavor);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public boolean likeExcel()
	{
		try
		{
			clip.getData(DataFlavor.stringFlavor);
			return true;
		} catch (Exception e)
		{
			return false;
		}
	}
	
	public String readFromClipboard()
	{
		String x=null;
		try
		{
			x=(String)clip.getData(DataFlavor.stringFlavor);
		}catch(Exception e){
			e.printStackTrace();
		}
		return x;
	}
	
	public void pasteFilePath(int beginRow,int beginCol) 
	{
	  List<File> data=readCopyedFiles();
	  for(int i=0;i<data.size();i++)
	  {
	  	File temp=data.get(i);
	  	if(temp.isDirectory())
	  	{
	  		continue;
	  	}
	  	String str=temp.getAbsolutePath();
		  	if(!isCross)
			  { 
		  		if(beginRow+i>jTable.getRowCount()-2)
		  		{
		  			model.setRowCount(beginRow+i+2);
		  		}
		  		addColWidths(str, beginCol);
		  		model.setValueAt(str, beginRow+i, beginCol);
		  		hasDataCell.add(new Cell(beginRow+i,beginCol));
		  	}else{
		  		if(beginCol+i>jTable.getColumnCount()-2)
		  		{
		  			model.setColumnCount(beginCol+i+2);
		  		}
		  		addColWidths(str, beginCol+i);
		  		model.setValueAt(str,beginRow,beginCol+i);
		  		hasDataCell.add(new Cell(beginRow,beginCol+i));
	  	}
	  }
	  setColumnWidth();
	}
	
	public void pasteFromExcel(int beginRow,int beginCol)
	{
		  List<List<String>> data=new ArrayList<List<String>>(); 
		  String[] strs=readFromClipboard().split("\n");
		  for(String str:strs)
		  {
		  	List<String> row=new ArrayList<String>();
		  	for(String s:str.split("\t"))
		  	{
		  		row.add(s);
		  	}
		  	 data.add(row);
		  } 
		  for(int i=0;i<data.size();i++)
		  {
		  	List<String> temp=(List<String>)data.get(i);
		  	for(int j=0;j<temp.size();j++)
		  	{
		  		String str=(String)temp.get(j);
		  		
			  	if(!isCross)
				  { 
			  		if(beginRow+i>jTable.getRowCount()-2)
			  		{
			  			model.setRowCount(beginRow+i+2);
			  		}
			  		if(beginCol+j>=jTable.getColumnCount()-1)
			  		{
			  			model.setColumnCount(beginCol+j+2);
			  		}
			  		addColWidths(str, beginCol+j);
			  		model.setValueAt(str, beginRow+i, beginCol+j);
			  		hasDataCell.add(new Cell(beginRow+i,beginCol+j));
			  	}else{
			  		if(beginRow+j>=jTable.getRowCount()-1)
			  		{
			  			model.setRowCount(beginRow+j+2);
			  		}
			  		if(beginCol+i>jTable.getColumnCount()-2)
			  		{
			  			model.setColumnCount(beginCol+i+2);
			  		}
			  		addColWidths(str, beginCol+i);
			  		model.setValueAt(str,beginRow+j,beginCol+i);
			  		hasDataCell.add(new Cell(beginRow+j,beginCol+i));
			  	}
		  	}
		  }
		  setColumnWidth();
	}
	
	public void removeAllData()
	{
		for(Cell c:hasDataCell)
		{
			model.setValueAt("",c.getRow(), c.getCol());
		}
	}
	
	public void removeSelectedData()
	{
		int[] rs=jTable.getSelectedRows();
		int[] cs=jTable.getSelectedColumns();
		for(int i=0;i<rs.length;i++)
		{
			for(int j=0;j<cs.length;j++)
			{
				model.setValueAt("", rs[i], cs[j]);
			}
		}
	}
	
	public void setColumnWidth()
	{
		Iterator<Integer> iter=colWidths.keySet().iterator();
	  while(iter.hasNext())
	  {
	  	int col=iter.next();
	  	int width=colWidths.get(col);
	  	jTable.getColumnModel().getColumn(col).setPreferredWidth(width);
	  }
	}
	
	public void addColWidths(String cellStr,Integer colIndex)
	{
		int width=jTable.getFont().getSize()*cellStr.length()+jTable.getFont().getSize()/2;
		Integer temp=colWidths.get(colIndex);
		if(temp!=null&&temp>width)
		{
			return;
		}
		else if(temp!=null&&temp<width)
		{
			colWidths.remove(colIndex);
		}
		colWidths.put(colIndex, width);
	}
	
	static class Cell{
		private int row;
		private int col;
		public Cell(int row,int col)
		{
			this.row=row;
			this.col=col;
		}
		public int getRow()
		{
			return row;
		}
		public int getCol()
		{
			return col;
		}
	}
}
