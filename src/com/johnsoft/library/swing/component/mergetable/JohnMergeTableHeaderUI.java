package com.johnsoft.library.swing.component.mergetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.johnsoft.library.swing.component.mergetable.JohnCellMergeData.HeaderMergeInfo;
import com.sun.java.swing.plaf.windows.WindowsTableHeaderUI;

/**
 * 可合并单元格的表头UI
 * @author 李哲浩
 */
public class JohnMergeTableHeaderUI extends WindowsTableHeaderUI
{
	@Override
	public void paint(Graphics g, JComponent c)
	{
		if (header.getColumnModel().getColumnCount() <= 0)
		{
			return;
		}
		boolean ltr = header.getComponentOrientation().isLeftToRight();

		Rectangle clip = g.getClipBounds();
		Point left = clip.getLocation();
		Point right = new Point(clip.x + clip.width - 1, clip.y);
		TableColumnModel cm = header.getColumnModel();
		int cMin = header.columnAtPoint(ltr ? left : right);
		int cMax = header.columnAtPoint(ltr ? right : left);
		if (cMin == -1)
		{
			cMin = 0;
		}
		if (cMax == -1)
		{
			cMax = cm.getColumnCount() - 1;
		}

		int columnWidth;
		TableColumn aColumn;
		JohnMergeTableHeader gHeader = (JohnMergeTableHeader) header;
		JohnCellMergeData data = gHeader.getCellMergeData();
		for (int row = 0, rowCount = data.getRowCount(); row < rowCount; row++)
		{
			Rectangle cellRect = gHeader.getHeaderRect(row, ltr ? cMin : cMax);
			if (ltr)
			{
				for (int column = cMin; column <= cMax; column++)
				{
					HeaderMergeInfo info = data.getHeaderMergeInfo(row, column);
					cellRect.width = 0;
					for (int from = info.cell.col, to = from + info.span.col
							- 1; from <= to; from++)
					{
						aColumn = cm.getColumn(from);
						columnWidth = aColumn.getWidth();
						cellRect.width += columnWidth;
					}
					cellRect.y = gHeader.getYOfHeaderMergeInfo(info);
					cellRect.height = gHeader.getHeightOfHeaderMergeInfo(info);
					aColumn = cm.getColumn(column);
					paintCell(g, cellRect, row, column);
					cellRect.x += cellRect.width;
					column += info.span.col - 1;
				}
			} else
			{
				for (int column = cMax; column >= cMin; column--)
				{
					HeaderMergeInfo info = data.getHeaderMergeInfo(row, column);
					cellRect.width = 0;
					for (int from = info.cell.col, to = from + info.span.col
							- 1; from <= to; from++)
					{
						aColumn = cm.getColumn(from);
						columnWidth = aColumn.getWidth();
						cellRect.width += columnWidth;
					}
					aColumn = cm.getColumn(column);
					paintCell(g, cellRect, row, column);
					cellRect.x += cellRect.width;
					column -= info.span.col - 1;
				}
			}
		}

		rendererPane.removeAll();
	}

	private void paintCell(Graphics g, Rectangle cellRect, int rowIndex,
			int columnIndex)
	{
		Component component = getHeaderRenderer(rowIndex, columnIndex);
		if (component instanceof JLabel)
		{
			((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
		}
		rendererPane.paintComponent(g, component, header, cellRect.x,
				cellRect.y, cellRect.width, cellRect.height, true);
	}

	private Component getHeaderRenderer(int rowIndex, int columnIndex)
	{
		JohnMergeTableHeader gHeader = (JohnMergeTableHeader) header;
		JohnCellMergeData data = gHeader.getCellMergeData();
		HeaderMergeInfo info = data.getHeaderMergeInfo(rowIndex, columnIndex);
		TableCellRenderer renderer = header.getDefaultRenderer();
		return renderer.getTableCellRendererComponent(header.getTable(),
				info.value, false, false, -1, columnIndex);
	}

	private int getHeaderHeight()
	{
		int height = 0;
		int tempHeight = 0;
		JohnMergeTableHeader gHeader = (JohnMergeTableHeader) header;
		JohnCellMergeData data = gHeader.getCellMergeData();
		TableColumnModel cm = header.getColumnModel();
		for (int column = 0, columnCount = cm.getColumnCount(); column < columnCount; column++)
		{
			tempHeight = 0;
			List<HeaderMergeInfo> list = data
					.getHeaderMergeInfoAtColumn(column);
			for (HeaderMergeInfo info : list)
			{
				TableCellRenderer renderer = gHeader.getDefaultRenderer();
				Component comp = renderer
						.getTableCellRendererComponent(header.getTable(),
								info.value, false, false, -1, column);
				int rendererHeight = comp.getPreferredSize().height;
				tempHeight += rendererHeight;
			}
			height = Math.max(height, tempHeight);
		}
		return height;
	}

	private Dimension createHeaderSize(long width)
	{
		// TableColumnModel columnModel = header.getColumnModel();
		// None of the callers include the intercell spacing, do it here.
		if (width > Integer.MAX_VALUE)
		{
			width = Integer.MAX_VALUE;
		}
		return new Dimension((int) width, getHeaderHeight());
	}

	public Dimension getMinimumSize(JComponent c)
	{
		long width = 0;
		Enumeration<TableColumn> enumeration = header.getColumnModel()
				.getColumns();
		while (enumeration.hasMoreElements())
		{
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
			width = width + aColumn.getMinWidth();
		}
		return createHeaderSize(width);
	}

	public Dimension getPreferredSize(JComponent c)
	{
		long width = 0;
		Enumeration<TableColumn> enumeration = header.getColumnModel()
				.getColumns();
		while (enumeration.hasMoreElements())
		{
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
			width = width + aColumn.getPreferredWidth();
		}
		return createHeaderSize(width);
	}

	public Dimension getMaximumSize(JComponent c)
	{
		long width = 0;
		Enumeration<TableColumn> enumeration = header.getColumnModel()
				.getColumns();
		while (enumeration.hasMoreElements())
		{
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
			width = width + aColumn.getMaxWidth();
		}
		return createHeaderSize(width);
	}
}
