package com.johnsoft.library.swing.component.tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.johnsoft.library.swing.layout.JohnGridLayout;

/**
 * Tile Layout实现面板,按以下步骤调用：
 * 1.实现Tile接口
 * 2.setTilePanelModel设置单元格数组
 * 3.setTilePanelBounds设置容器大小
 * 4.setTilePanelLayout设置布局
 * @author john
 *
 */
public class JohnTilePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 代表Tile Layout中每一单元格
	 * @author john
	 */
	public interface Tile
	{
		/**
		 * 如何呈现单元格
		 * @param index 该单元格在表格数组中的位置
		 */
		public JPanel createTile(int index);
	}
	
	private JPanel mainTilePane;//所有单元格的容器
	private List<Tile> modelList;//单元格组件面板数组
	
	public JohnTilePanel()
	{
		mainTilePane=new JPanel();
		add(mainTilePane);
	}
	 
	public void buildTilePanel()
	{
		mainTilePane.removeAll();
		for(int i=0;i<modelList.size();i++)
		{
			mainTilePane.add(modelList.get(i).createTile(i));
		}
		doLayout();
		repaint();
	}
	
	public void setTilePanelBounds(Rectangle r)
	{
		mainTilePane.setBounds(r);
		setPreferredSize(r.getSize());
	}
	
  public Rectangle getTilePanelBounds()
  {
  	return mainTilePane.getBounds();
  }
  
  public void setTilePanelLayout(JohnGridLayout layout)
  {
  	mainTilePane.setLayout(layout);
  }
  
  public JohnGridLayout getTilePanelLayout()
  {
  	return  (JohnGridLayout)mainTilePane.getLayout();
  }
  
  public void setTilePanelModel(List<Tile> modelList)
	{
		this.modelList=modelList;
		buildTilePanel();
	}
	
	public List<Tile> getTilePanelModel()
	{
		return modelList;
	}
  
  @Override
  public void setLayout(LayoutManager mgr) {
  	super.setLayout(null);
  }
  
  @Override
  public LayoutManager getLayout()
  {
  	return null;
  }
  
  /**
   * 设置布局的快捷方法
   * @param columnCount 列数
   * @param tileSize 单元格大小
   */
  public void setTilePanelLayoutToFit(int columnCount,Dimension tileSize)
  {
  	int hgaps=getTilePanelBounds().width-tileSize.width*columnCount;
  	int hgap=hgaps/(columnCount+1);
  	int rowCount;
  	if(getTilePanelModel().size()%columnCount==0)
  	{
  		rowCount=getTilePanelModel().size()/columnCount;
  	}else{
  		rowCount=getTilePanelModel().size()/columnCount+1;
  	}
  	int vgaps=getTilePanelBounds().height-tileSize.height*rowCount;
  	int vgap=vgaps/(rowCount+1);
  	JohnGridLayout gl=new JohnGridLayout(0, columnCount, hgap, vgap, new Insets(vgap, hgap, vgap, hgap));
  	setTilePanelLayout(gl);
  }
  
  /**
   * 设置布局的快捷方法
   * @param param 第一个参数作为列数,第二个参数作为gap
   */
  public void setTilePanelLayoutToFit(int... param)
  {
  	int gap=param.length==1?5:param[1];
  	JohnGridLayout gl=new JohnGridLayout(0, param[0], gap, gap, new Insets(gap, gap, gap, gap));
  	setTilePanelLayout(gl);
  }
  
  /**
   * 将同时快捷设置布局和大小
   * @param count 行数或列数
   * @param tileSize 单元格大小
   * @param gap 间距大小
   * @param isRowFixed 是否固定行数,即count是否看成是行数
   * 如果isRowFixed为true,count作为行数,向右摆放;否则count作为列数,向下摆放
   */
  public void setTilePanelLayoutAutoBounds(int count,Dimension tileSize,int gap,boolean isRowFixed)
  {
  	if(!isRowFixed)
  	{//count作为列数,向下摆放
  		int rowCount;
  		if(getTilePanelModel().size()%count==0)
    	{
    		rowCount=getTilePanelModel().size()/count;
    	}else{
    		rowCount=getTilePanelModel().size()/count+1;
    	}
  		int width=tileSize.width*count+gap*(count+1);
  		int height=tileSize.height*rowCount+gap*(rowCount+1);
  		setTilePanelBounds(new Rectangle(width, height));
  		setTilePanelLayout(new JohnGridLayout(0, count, gap, gap, new Insets(gap, gap, gap, gap)));
  	}else{//count作为行数,向右摆放
  		int columnCount;
  		if(getTilePanelModel().size()%count==0)
    	{
  			columnCount=getTilePanelModel().size()/count;
    	}else{
    		columnCount=getTilePanelModel().size()/count+1;
    	}
  		int width=tileSize.width*columnCount+gap*(columnCount+1);
  		int height=tileSize.height*count+gap*(count+1);
  		setTilePanelBounds(new Rectangle(width, height));
  		setTilePanelLayout(new JohnGridLayout(count, 0, gap, gap, new Insets(gap, gap, gap, gap)));
  	}
  }
  
	public static void main(String[] args)
	{
		Color[] colors=new Color[]{Color.BLACK,Color.BLUE,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.YELLOW};
		List<Tile> list=new ArrayList<Tile>();
		for(int i=0;i<9;i++)
		{
			list.add(new JohnBasicTileModel(colors[i]));
		}
		JFrame jf=new JFrame();
		JohnTilePanel tp=new JohnTilePanel();
		tp.setBackground(Color.WHITE);
		tp.setTilePanelModel(list);
		tp.setTilePanelLayoutAutoBounds(2, new Dimension(70,100), 10, true);
	//	tp.setTilePanelBounds(new Rectangle(5, 5, 360, 360));
	//	tp.setTilePanelLayout(new GridLayout2(7, 4, 10, 10, new Insets(10, 10, 10, 10)));
	//	tp.setTilePanelLayoutToFit(4,new Dimension(70,100));
	//	tp.setTilePanelLayoutToFit(7);
		tp.setPreferredSize(tp.getTilePanelBounds().getSize());
		JPanel jpx=new JPanel();
		JScrollPane jsp=new JScrollPane(tp);
		jsp.setPreferredSize(new Dimension(200, 200));
		jpx.add(jsp);
		jf.add(jpx);
		jf.setMinimumSize(new Dimension(360,360));
		jf.setBounds(100, 100, 450, 450);
		jf.setVisible(true);
	}
}
