package com.johnsoft.library.swing.component.tile;

import java.awt.Color;

import javax.swing.JPanel;

import com.johnsoft.library.swing.component.tile.JohnTilePanel.Tile;

public class JohnBasicTileModel implements Tile
{
	Color bg;
	
	public JohnBasicTileModel(Color bg)
	{
		this.bg=bg;
	}
	
	@Override
	public JPanel createTile(int index)
	{
		JPanel p=new JPanel();
		p.setBackground(bg);
		return p;
	}

}
