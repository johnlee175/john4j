package com.johnsoft.library.swing.component.mergetable;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.johnsoft.library.util.data.JohnCell;

/**
 * 可合并单元格的表格
 * @author 李哲浩
 */
public class JohnMergeTable extends JTable
{
	private static final long serialVersionUID = 1L;

	protected JohnCellMergeData data = new JohnCellMergeData();

	public JohnMergeTable()
	{
		super();
		super.setUI(new JohnMergeTableUI());
		init();
	}

	public JohnMergeTable(TableModel model)
	{
		super(model);
		super.setUI(new JohnMergeTableUI());
		init();
	}

	public JohnMergeTable(int row, int col)
	{
		super(row, col);
		super.setUI(new JohnMergeTableUI());
		init();
	}

	protected void init()
	{
		createModel();
		createRenderer();
		createEditor();
		installDefaults();
		installListeners();
		installMenuAction();
	}

	protected void createModel()
	{

	}

	protected void createRenderer()
	{

	}

	protected void createEditor()
	{

	}

	protected void installDefaults()
	{

	}

	protected void installListeners()
	{
		getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				repaint();
			}
		});
	}

	protected void installMenuAction()
	{

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

	@Override
	public Rectangle getCellRect(int row, int col, boolean includeSpacing)
	{
		Rectangle rect = super.getCellRect(row, col, includeSpacing);
		JohnCell xcell = data.getCellGap(row, col);
		if (xcell != null)
		{
			List<JohnCell> list = data.getMergeInfo(row, col).unvisibles;
			Set<Integer> set = new HashSet<Integer>();
			if (xcell.col > 0)
			{
				for (JohnCell cell : list)
				{
					if (cell.col != col)
					{
						set.add(cell.col);
					}
				}
				for (Integer i : set)
				{
					rect.width += getTableHeader().getColumnModel()
							.getColumn(i).getWidth();
				}
			}
			set.clear();
			if (xcell.row > 0)
			{
				for (JohnCell cell : list)
				{
					if (cell.row != row)
					{
						set.add(cell.row);
					}
				}
				for (Integer i : set)
				{
					rect.height += getRowHeight(i);
				}
			}
		}
		return rect;
	}

	@Override
	public int rowAtPoint(Point p)
	{
		int row = super.rowAtPoint(p);
		int col = super.columnAtPoint(p);
		JohnCell cell = data.getKeyFromUnvisible(row, col);
		return cell == null ? row : cell.row;
	}

	@Override
	public int columnAtPoint(Point p)
	{
		int row = super.rowAtPoint(p);
		int col = super.columnAtPoint(p);
		JohnCell cell = data.getKeyFromUnvisible(row, col);
		return cell == null ? col : cell.col;
	}
}
