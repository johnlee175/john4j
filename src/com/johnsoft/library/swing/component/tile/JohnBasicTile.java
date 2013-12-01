package com.johnsoft.library.swing.component.tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.johnsoft.library.swing.component.tile.JohnTile;

/**
 * 应覆写paintTile实现展示;覆写installListeners实现控制交互
 * @author john
 */
public class JohnBasicTile implements JohnTile
{
	/**
	 * 显示图片
	 */
	protected ImageIcon icon;
	/**
	 * 显示描述文字
	 */
	protected String commandName;
	
	/**
	 * Tile面板
	 */
	protected JPanel pane;
	/**
	 * Tile面板上下文Graphics
	 */
	protected Graphics2D g2;
	/**
	 * Tile面板中可用于绘图的区域
	 */
	protected Rectangle r;
	/**
	 * 使用圆角矩形时为统一风格做出的参考圆半径
	 */
	protected int arc=16;
	
	public JohnBasicTile(ImageIcon icon,String commandName)
	{
		this.icon=icon;
		this.commandName=commandName;
	}
	
	@Override
	public JPanel createTile(int index)
	{
		pane=new JPanel(){
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g)
			{
				g2=(Graphics2D)g;
				r=g.getClipBounds();
				paintTile();
			}
		};
		installListeners();
		return pane;
	}
	
	public void paintTile()
	{
		if(icon==null)
		{
			g2.setColor(Color.ORANGE);
			g2.setStroke(new BasicStroke(2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1,new float[]{4,6,4},1));
			g2.drawRoundRect(r.x+2, r.y+2, r.width-4, r.height-4,arc,arc);
			Font font=new Font("微软雅黑", Font.BOLD, 12);
			Rectangle2D r2=g2.getFontMetrics(font).getStringBounds(commandName, g2);
			g2.setFont(font);
			g2.drawString(commandName, new Double((r.width-r2.getWidth())/2).intValue(), r.height/2);
		}
	}	 
	
	public void installListeners(){	
	}
	
	/**
	 * 调用此方法刷新重绘
	 */
	public void repaint()
	{
		pane.getTopLevelAncestor().repaint();
	}

}
