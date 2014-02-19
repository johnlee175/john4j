package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import sun.swing.table.DefaultTableCellHeaderRenderer;

import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideButton;
import com.johnsoft.library.util.data.primary.C;

/**
 * 活的,可互动的表头渲染包装器
 * @author 李哲浩
 */
public class JohnActiveHeaderRenderer implements TableCellRenderer, MouseMotionListener, MouseListener
{
	private int preDragMouseY;
	private int preDragRowHeight;
	private int resizingIdx;
	
	/**当不改变行高时显示的调整光标*/
	protected static Cursor defaultCursor=Cursor.getDefaultCursor();
	/**当改变行高时显示的调整光标*/
	protected static Cursor resizeCursor=Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
	
	private int rolloverIdx=-1;
	private int pressIdx=-1;
	
	private int defaultDataRowHeight=25;
	
	protected int colHeaderHeight;
	protected int rowHeaderColIdx;
	
	protected JTableHeader colHeader;
	protected JTable rowHeader;
	protected JTable dataTable;
	
	/**
	 * 安装行表头,注:需要保证传入的table已有了非null的TableModel,并且此TableModel已经考虑了行号
	 * @param table 不能为null,实际的table
	 * @param fitRowHeight 合适的行高,会设置table行高,而其header,行表头所用的表格组件的列表头将被设置行高为fitRowHeight+5
	 * @param rowHeaderColumnTableRef 行表头所用的表格组件
	 * @return 包含table,行表头所用的表格组件,header,行表头所用的表格组件的列表头的滚动窗格
	 */
	public static JScrollPane installRowHeader(JTable table,C<JTable> rowHeaderColumnTableRef,int fitRowHeight)
	{
		TableColumnModel dataColModel = new DefaultTableColumnModel()
		{
			private static final long serialVersionUID = 1L;
			boolean first = true;
			public void addColumn(TableColumn tc) 
			{
				if(first)
				{
					first = false;
					tc.setMinWidth(0);
					tc.setPreferredWidth(0);
					tc.setMaxWidth(0);
					tc.setWidth(0);
					tc.setResizable(false);
				}
				super.addColumn(tc);
			}
		};
		
		TableColumnModel rowHeaderModel = new DefaultTableColumnModel()
		{
			private static final long serialVersionUID = 1L;
			boolean first = true;
			public void addColumn(TableColumn tc) 
			{
				if(first)
				{
					tc.setPreferredWidth(70);
					super.addColumn(tc);
					first = false;
				};
			}
		};
		
		final JViewport jv = new JViewport();
		final JTable rowHeaderColumn = new JTable(table.getModel())
		{
			private static final long serialVersionUID = 1L;
			boolean isFirst=true;
			@Override
			public void createDefaultColumnsFromModel()
			{
				 if(isFirst)
				 {
					 isFirst=false;
					 super.createDefaultColumnsFromModel();
				 }
			}
		};
		JTableHeader dcHeader=table.getTableHeader();
		JTableHeader rhHeader=new JTableHeader()
		{
			private static final long serialVersionUID = 1L;
			private Dimension rsd=new Dimension();
			@Override
			public TableColumn getResizingColumn()
			{
				TableColumn tc=super.getResizingColumn();
				if(tc!=null){
					rsd.setSize(tc.getWidth(), rowHeaderColumn.getHeight());
					jv.setPreferredSize(rsd);
				}
				return tc;
			}
		};
		rowHeaderColumn.setTableHeader(rhHeader);
		
		table.setColumnModel(dataColModel);
		table.createDefaultColumnsFromModel();
		rowHeaderColumn.setColumnModel(rowHeaderModel);
		rowHeaderColumn.createDefaultColumnsFromModel();
		
		table.setRowHeight(fitRowHeight);
		
		rowHeaderColumn.setRowHeight(table.getRowHeight());
		table.setSelectionModel(rowHeaderColumn.getSelectionModel());
		rhHeader.setFont(dcHeader.getFont());
		rowHeaderColumn.setFont(dcHeader.getFont());
		
		new JohnActiveHeaderRenderer(dcHeader,table,fitRowHeight+5);
		new JohnActiveHeaderRenderer(rhHeader,table,fitRowHeight+5);
		new JohnActiveHeaderRenderer(rowHeaderColumn,table, 0);
		
		dcHeader.setReorderingAllowed(false);
		rhHeader.setReorderingAllowed(false);
		rowHeaderColumn.setColumnSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		jv.setView(rowHeaderColumn);
		jv.setPreferredSize(rowHeaderColumn.getPreferredSize());
		
		JScrollPane jsp = new JScrollPane(table);
		jsp.setRowHeader(jv);
		jsp.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rhHeader);
		
		if(rowHeaderColumnTableRef!=null)
			rowHeaderColumnTableRef.ref=rowHeaderColumn;
		return jsp;
	}
	
	/**
	 * @param colHeader 横向列表头
	 * @param dataTable 主数据区表格组件
	 * @param colHeaderHeight 列表头行的行高
	 */
	public JohnActiveHeaderRenderer(JTableHeader colHeader,JTable dataTable,int colHeaderHeight)
	{
		this.colHeader=colHeader;
		this.colHeaderHeight=colHeaderHeight;
		this.dataTable=dataTable;
		colHeader.addMouseListener(this);
		colHeader.addMouseMotionListener(this);
		colHeader.setDefaultRenderer(this);
	}
	
	/**
	 * @param rowHeader 纵向行表头
	 * @param dataTable 主数据区表格组件,不是行表头rowHeader.getTableHeader()后的结果
	 * @param rowHeaderColIdx 行表头所处的序号
	 */
	public JohnActiveHeaderRenderer(JTable rowHeader,JTable dataTable,int rowHeaderColIdx)
	{
		this.rowHeader=rowHeader;
		this.rowHeaderColIdx=rowHeaderColIdx;
		this.dataTable=dataTable;
		rowHeader.addMouseListener(this);
		rowHeader.addMouseMotionListener(this);
		rowHeader.setDefaultRenderer(Object.class, this);
		rowHeader.setIntercellSpacing(new Dimension()); 
		rowHeader.setShowGrid(false);
	}
	
	/**返回鼠标悬停的行号或列号,如果是横向表头,则是列号*/
	public int getRolloverIdx()
	{
		return rolloverIdx;
	}

	/**返回鼠标按压的行号或列号,如果是横向表头,则是列号*/
	public int getPressIdx()
	{
		return pressIdx;
	}

	/**
	 * @param defaultDataRowHeight 当在调整行高的模式下双击鼠标还原合适行高的值
	 */
	public void setDefaultDataRowHeight(int defaultDataRowHeight)
	{
		this.defaultDataRowHeight = defaultDataRowHeight;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		LookAndFeel laf = UIManager.getLookAndFeel(); //because of JideButton will change the default LookAndFeel
		JideButton btn = new JideButton();
		btn.setButtonStyle(JideButton.TOOLBOX_STYLE);
		btn.setBackgroundOfState(ThemePainter.STATE_DEFAULT,Color.WHITE);
//		btn.setBackgroundOfState(ThemePainter.STATE_SELECTED,new Color(0xA9DEF0));
//		btn.setBackgroundOfState(ThemePainter.STATE_PRESSED, new Color(0xA9DEF0));
//		btn.setBackgroundOfState(ThemePainter.STATE_ROLLOVER, new Color(0xDFF0FF));
		try
		{
			UIManager.setLookAndFeel(laf); //because of JideButton will change the default LookAndFeel
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		btn.setBorderPainted(false);
		btn.putClientProperty("JideButton.alwaysPaintBackground", true);
		btn.setBorder(BorderFactory.createEmptyBorder());
		if(colHeader!=null)
		{
			btn.setFont(colHeader.getFont());
			btn.setPreferredSize(new Dimension(0, colHeaderHeight));
		}else{
			btn.setFont(rowHeader.getFont());
		}
		btn.setText(value==null?"":value.toString());
		ButtonModel bm=btn.getModel();
		if(rolloverIdx!=-1&&((rowHeader==null&&rolloverIdx==column)||colHeader==null&&rolloverIdx==row))
		{
			bm.setRollover(true);
//				btn.setBorder(BorderFactory.createLineBorder(new Color(0x00B2EE)));
		}
		if(pressIdx!=-1&&((rowHeader==null&&pressIdx==column)||colHeader==null&&pressIdx==row))
		{
			bm.setPressed(true);
//				btn.setBorder(BorderFactory.createLineBorder(new Color(0x008B8B)));
		}
		if(isSelected)
		{
			bm.setSelected(true);
//				btn.setBorder(BorderFactory.createLineBorder(new Color(0x00FA9A)));
		}
		Icon icon=null;
		SortOrder so=DefaultTableCellHeaderRenderer.getColumnSortOrder(table, column);
		if(so!=null)
		{
			switch (so.ordinal())
			{
			case 0:
				icon=UIManager.getIcon("Table.ascendingSortIcon");
				break;
			case 1:
				icon=UIManager.getIcon("Table.descendingSortIcon");
				break;
			}
		}
		if(icon!=null)
		{
			btn.setIcon(icon);
			btn.setHorizontalTextPosition(SwingConstants.CENTER);
			btn.setVerticalTextPosition(SwingConstants.BOTTOM);
			btn.setIconTextGap(0);
		}
		return btn;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(rowHeader!=null
			&&rowHeader.getCursor().equals(resizeCursor)
			&&e.getButton()==MouseEvent.BUTTON1
			&&e.getClickCount()==2)
		{
			rowHeader.setRowHeight(defaultDataRowHeight);
			if(dataTable!=null)
				dataTable.setRowHeight(defaultDataRowHeight);
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Point p=e.getPoint();
		if(rowHeader==null)
		{
			pressIdx=colHeader.columnAtPoint(p);
			colHeader.repaint();
		}else{
			pressIdx=rowHeader.rowAtPoint(p);
			if(rowHeader!=null&&rowHeader.getCursor().equals(resizeCursor)&&rowHeader.getRowCount()>resizingIdx)
			{
				preDragMouseY=p.y;
				preDragRowHeight=rowHeader.getRowHeight(resizingIdx);
			}
			rowHeader.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		pressIdx=-1;
		if(rowHeader==null)
		{
			colHeader.repaint();
		}else{
			rowHeader.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		//Do Nothing!
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		rolloverIdx=-1;
		pressIdx=-1;
		if(rowHeader==null)
		{
			colHeader.repaint();
		}else{
			rowHeader.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(rowHeader!=null&&rowHeader.getCursor().equals(resizeCursor)&&rowHeader.getRowCount()>resizingIdx)
		{
			int h=preDragRowHeight+e.getY()-preDragMouseY;
			if(h<1) h=1;
			rowHeader.setRowHeight(resizingIdx, h);
			if(dataTable!=null)
				dataTable.setRowHeight(resizingIdx, h);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point p=e.getPoint();
		if(rowHeader==null)
		{
			rolloverIdx=colHeader.columnAtPoint(p);
			colHeader.repaint();
		}else{
			rolloverIdx=rowHeader.rowAtPoint(p);
			rowHeader.repaint();
			Rectangle rect=rowHeader.getCellRect(rolloverIdx, rowHeaderColIdx, true);
			rect.grow(0, -(int)(rect.height/4f));
			rect.translate(0, (int)(3f*rect.height/4f));
			if(rect.contains(p))
			{
				rowHeader.setCursor(resizeCursor);
				resizingIdx=rolloverIdx;
			}else{
				rowHeader.setCursor(defaultCursor);
			}
		}
	}

}
