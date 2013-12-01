package com.johnsoft.library.swing.component.tile;

import javax.swing.JPanel;

/**
	 * 代表Tile Layout中每一单元格
	 * @author john
	 */
	public interface JohnTile
	{
		/**
		 * 如何呈现单元格
		 * @param index 该单元格在表格数组中的位置
		 */
		public JPanel createTile(int index);
	}