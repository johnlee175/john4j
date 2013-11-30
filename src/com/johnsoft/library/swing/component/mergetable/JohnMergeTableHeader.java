package com.johnsoft.library.swing.component.mergetable;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.johnsoft.library.swing.component.mergetable.JohnCellMergeData.HeaderMergeInfo;

/**
 * 可合并单元格的表头
 * @author 李哲浩
 */
public class JohnMergeTableHeader extends JTableHeader
{
	private static final long serialVersionUID = 1L;

	protected JohnCellMergeData data = new JohnCellMergeData();

	public JohnMergeTableHeader()
	{
		super();
		super.setUI(new JohnMergeTableHeaderUI());
		init();
	}

	public JohnMergeTableHeader(TableColumnModel cm)
	{
		super(cm);
		super.setUI(new JohnMergeTableHeaderUI());
		init();
	}

	protected void init()
	{
		setReorderingAllowed(false);
	}

	/**
	 * 设置合并数据
	 */
	public void setCellMergeData(JohnCellMergeData data)
	{
		this.data = data;
	}

	/**
	 * @return 合并数据
	 */
	public JohnCellMergeData getCellMergeData()
	{
		return data;
	}

	public Rectangle getHeaderRect(int row, int column)
	{
		Rectangle r = new Rectangle();
		TableColumnModel cm = getColumnModel();

		HeaderMergeInfo info = data.getHeaderMergeInfo(row, column);
		r.height = getHeight();

		if (column < 0)
		{
			if (!getComponentOrientation().isLeftToRight())
			{
				r.x = getWidthInRightToLeft();
			}
		} else if (column >= cm.getColumnCount())
		{
			if (getComponentOrientation().isLeftToRight())
			{
				r.x = getWidth();
			}
		} else
		{
			for (int i = 0; i < info.cell.col; i++)
			{
				r.x += cm.getColumn(i).getWidth();
			}
			for (int i = info.cell.col, j = info.cell.col + info.span.col - 1; i < j; i++)
			{
				r.width += cm.getColumn(i).getWidth();
			}
			if (!getComponentOrientation().isLeftToRight())
			{
				r.x = getWidthInRightToLeft() - r.x - r.width;
			}
		}
		return r;
	}

	public int getYOfHeaderMergeInfo(HeaderMergeInfo info)
	{
		int row = info.cell.row;
		TableCellRenderer renderer = this.getDefaultRenderer();
		Component comp = renderer.getTableCellRendererComponent(getTable(),
				info.value, false, false, info.cell.row, info.cell.col);
		return row * comp.getPreferredSize().height;
	}

	public int getHeightOfHeaderMergeInfo(HeaderMergeInfo info)
	{
		int rowSpan = info.span.row;
		TableCellRenderer renderer = this.getDefaultRenderer();
		Component comp = renderer.getTableCellRendererComponent(getTable(),
				info.value, false, false, info.cell.row, info.cell.col);
		return rowSpan * comp.getPreferredSize().height;
	}

	private int getWidthInRightToLeft()
	{
		if ((table != null)
				&& (table.getAutoResizeMode() != JTable.AUTO_RESIZE_OFF))
		{
			return table.getWidth();
		}
		return super.getWidth();
	}

	@Override
	public void setTable(JTable table)
	{
		super.setColumnModel(table.getColumnModel());
		super.setTable(table);
	}

}
