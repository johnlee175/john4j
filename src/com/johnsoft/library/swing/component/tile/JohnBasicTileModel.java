package com.johnsoft.library.swing.component.tile;

import java.awt.Color;

import javax.swing.JPanel;

public class JohnBasicTileModel implements JohnTile
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
