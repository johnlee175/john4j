package com.johnsoft.library.component.mergetable;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

import com.johnsoft.library.util.data.JohnCell;

/**
 * 可合并单元格的表格UI
 * @author 李哲浩
 */
public class JohnMergeTableUI extends BasicTableUI
{
	@Override
	public void paint(Graphics g, JComponent c)
	{
		Rectangle r = g.getClipBounds();
		clear();
		iterateCell(g, r);
		paintGrid(g, 0, table.getRowCount() - 1, 0, table.getColumnCount() - 1);
	}

	protected void clear()
	{
		rendererPane.removeAll();
	}

	protected void iterateCell(Graphics g, Rectangle r)
	{
		for (int i = 0; i < table.getColumnCount(); i++)
		{
			for (int j = 0; j < table.getRowCount(); j++)
			{
				if (table instanceof JohnMergeTable)
				{
					if (((JohnMergeTable) table).getCellMergeData()
							.isUnvisible(j, i))
					{
						continue;
					}
				}
				Rectangle rcell = table.getCellRect(j, i, true);
				if (rcell.intersects(r))
				{
					paintCell(g, rcell, j, i);
				}
			}
		}
	}

	protected void paintCell(Graphics g, Rectangle area, int row, int column)
	{
		int verticalMargin = table.getRowMargin();
		int horizontalMargin = table.getColumnModel().getColumnMargin();

		area.setBounds(area.x + horizontalMargin / 2, area.y + verticalMargin
				/ 2, area.width - horizontalMargin, area.height
				- verticalMargin);

		if (table.isEditing() && table.getEditingRow() == row
				&& table.getEditingColumn() == column)
		{
			Component component = table.getEditorComponent();
			component.setBounds(area);
			component.validate();
		} else
		{
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);
			if (component.getParent() == null)
			{
				rendererPane.add(component);
			}
			rendererPane.paintComponent(g, component, table, area.x, area.y,
					area.width, area.height, true);
		}
	}

	protected void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax)
	{
		JohnCellMergeData data = ((JohnMergeTable) table).getCellMergeData();
		if (table.getShowHorizontalLines())
		{
			for (int row = rMin; row <= rMax; row++)
			{
				for (int col = cMin; col <= cMax; col++)
				{
					Rectangle rcell = table.getCellRect(row, col, true);
					if (data.contain(row, col))
					{
						if (data.getLastUnvisibleCell(row, col).row > row)
						{
							continue;
						}
					} else if (data.isUnvisible(row, col))
					{
						JohnCell cell = data.getKeyFromUnvisible(row, col);
						if (data.getLastUnvisibleCell(cell.row, cell.col).row > row)
						{
							continue;
						}
					}
					g.setColor(table.getGridColor());
					g.drawLine(rcell.x, rcell.y + rcell.height - 1, rcell.x
							+ rcell.width - 1, rcell.y + rcell.height - 1);
				}
			}
		}

		if (table.getShowVerticalLines())
		{
			for (int col = cMin; col <= cMax; col++)
			{
				for (int row = rMin; row <= rMax; row++)
				{
					Rectangle rcell = table.getCellRect(row, col, true);
					if (data.contain(row, col))
					{
						if (data.getLastUnvisibleCell(row, col).col > col)
						{
							continue;
						}
					} else if (data.isUnvisible(row, col))
					{
						JohnCell cell = data.getKeyFromUnvisible(row, col);
						if (data.getLastUnvisibleCell(cell.row, cell.col).col > col)
						{
							continue;
						}
					}
					g.setColor(table.getGridColor());
					g.drawLine(rcell.x + rcell.width - 1, rcell.y, rcell.x
							+ rcell.width - 1, rcell.y + rcell.height - 1);
				}
			}
		}

	}

}
