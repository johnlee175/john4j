package com.johnsoft.library.component;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *如果拖拽时不是去画形状而是希望显示图像时，此类或许对你很有用; 
 *创建实例后通过addDragSnapshot添加监听,可通过设置setImageAlpha确定在拖动时图片的透明度;
 *理论上只应该重写move方法以确定拖拽结束时的行为,当你希望在拖动时显示其他图片或者组件的一部分时可重写createComImage方法,重写时可调用snapshotImageFromScreen截取屏幕中的任意区域;
 *如果你希望在拖动时显示组件的一部分,而这部分的矩形原点并非组件的原点,考虑覆写getMovingImageRelativePoint,以点坐标形式传递与组件原点的x,y距离,默认该方法返回(0,0)点以显示整个组件
 *@author John
 */
public class JohnDragSnapshot extends MouseAdapter
{
	private Point startPoint;//拖拽起始位置
	private Point endPoint;//拖拽终止位置
    private Point dragingPoint;//当前拖拽位置
	private Image comImage;//组件图像
	private Image bkImage;//背景图像
	private Component glass;//拖拽时glassPane
	private Cursor cursor;//拖拽时光标
	private Component oldGlass;//拖拽前后的glassPane
	private Cursor oldCursor;//拖拽前后的光标
	private Robot robot;//截图像用的机器人
	private boolean isDraging=false;//当前是否拖拽中
	private boolean hadSnapshot=false;//已经截过图
	private float imageAlpha=0.5f;//拖拽图像的清晰度
	
	/*public static void main(String[] args)
	{
		JFrame jf=new JFrame();
		JPanel jp=new JPanel(null);
		JLabel jl=new JLabel("this is a test!");
		JTextField jtf=new JTextField("this is a test!");
		JButton jb=new JButton("this is a test!");
		jl.setBounds(100,100,100,30);
		jtf.setBounds(100,150,100,30);
		jb.setBounds(100,200,100,30);
		jb.setFocusable(false);
		jp.add(jl);
		jp.add(jtf);
		jp.add(jb);
		jf.add(jp);
		JohnDragSnapshot ex=new JohnDragSnapshot();
		ex.addDragSnapshot(jl);
		ex.addDragSnapshot(jtf);
		ex.addDragSnapshot(jb);
		jf.setBounds(400,400, 400, 400);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}*/

	public JohnDragSnapshot()
	{
		try
		{
			robot = new Robot();
		} catch (AWTException e1)
		{
			e1.printStackTrace();
		}
		glass = new JComponent()
		{
			private static final long serialVersionUID = 1L;
		};
		cursor = DragSource.DefaultMoveDrop;
	}
	
	/**添加拖拽时显示图像的功能*/
	public void addDragSnapshot(JComponent com)
	{
		com.addMouseListener(this);
		com.addMouseMotionListener(this);
	}
	
	/**取消拖拽时显示图像的功能*/
	public void removeDragSnapshot(JComponent com)
	{
		com.removeMouseListener(this);
		com.removeMouseMotionListener(this);
	}
	
	/**设置拖拽时图像的清晰度*/
	public void setImageAlpha(float alpha)
	{
		this.imageAlpha=alpha;
	}
	
	protected Point getMovingImageRelativePoint()
	{
		return new Point();
	}
	
	protected Image createComImage(JComponent com)
	{
		  return snapshotImageFromScreen(new Rectangle(com.getLocationOnScreen(),
					com.getSize()));
	}
	
	protected Image snapshotImageFromScreen(Rectangle r)
	{
		return robot.createScreenCapture(r);
	}
	
	/**在鼠标释放后调用此方法使组件移动,如果除了移动,还需要其他行为,可覆写,并调用super的move方法*/
	public void move(JComponent com,Point start,Point end)
	{
		Rectangle r = com.getBounds();
		r.x += end.x - start.x;
		r.y += end.y - start.y;
		com.setLocation(r.x, r.y);
	}
	
	/**拖拽前安装glassPane,仅对JFrame和JDialog及其子类适用,如果需要JWindow类等,需覆写*/
	protected void installGlassPane(JComponent com)
	{
		Container container=com.getTopLevelAncestor();
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			oldGlass=frame.getGlassPane();
			frame.setGlassPane(glass);
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			oldGlass=dialog.getGlassPane();
			dialog.setGlassPane(glass);
		}
		else
		{
			throw new RuntimeException("top level ancestor is not instance of JFrame or JDialog!");
		}
	}
	
	/**拖拽后卸载glassPane,换回拖拽前的glassPane,仅对JFrame和JDialog及其子类适用,如果需要JWindow类等,需覆写*/
	protected void uninstallGlassPane(JComponent com)
	{
		Container container=com.getTopLevelAncestor();
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			if(oldGlass!=null)
			{
				frame.setGlassPane(oldGlass);
			}
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			if(oldGlass!=null)
			{
				dialog.setGlassPane(oldGlass);
			}
		}
		else
		{
			throw new RuntimeException("top level ancestor is not instance of JFrame or JDialog!");
		}
	}

	/**获取顶层容器窗口的内容面板,仅对JFrame和JDialog及其子类适用,如果需要JWindow类等,需覆写*/
	protected Container getContentPane(JComponent com)
	{
		Container container=com.getTopLevelAncestor();
		if(container instanceof JFrame)
		{
			JFrame frame=(JFrame)container;
			return	frame.getContentPane();
		}
		else if(container instanceof JDialog)
		{
			JDialog dialog=(JDialog)container;
			return	dialog.getContentPane();
		}
		else
		{
			throw new RuntimeException("top level ancestor is not instance of JFrame or JDialog!");
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.getButton()==MouseEvent.BUTTON1)
		{
			JComponent com=(JComponent)e.getSource();
			startPoint = e.getPoint();
			installGlassPane(com);
			glass.setVisible(true);
			isDraging=true;
			
			oldCursor=glass.getCursor();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.getButton()==MouseEvent.BUTTON1&&isDraging)
		{
			JComponent com=(JComponent)e.getSource();
			endPoint=e.getPoint();
			glass.setVisible(false);
			uninstallGlassPane(com);
			isDraging=false;
			hadSnapshot=false;
			
			if(oldCursor!=null)
			{
				glass.setCursor(oldCursor);
			}
			
			move(com,startPoint,endPoint);
		}
	}

	@Override
	public void mouseDragged(final MouseEvent e)
	{//核心方法
		if(isDraging)
		{
			JComponent com=(JComponent)e.getSource();
			
			glass.setCursor(cursor);
			
			dragingPoint=e.getLocationOnScreen();
			SwingUtilities.convertPointFromScreen(dragingPoint, glass);
		
			if(!hadSnapshot)
			{
				hadSnapshot=true;
				bkImage = robot.createScreenCapture(new Rectangle(getContentPane(com).getLocationOnScreen(),
						getContentPane(com).getSize()));
				comImage = createComImage(com);
				
			}//创建背景图片和拖动图片
			
			Point relative=getMovingImageRelativePoint();
			
			Image bufImage=glass.createImage(glass.getWidth(), glass.getHeight());
			Graphics2D g2=(Graphics2D)bufImage.getGraphics();
			g2.drawImage(bkImage, 0, 0, null);
			g2.setComposite(AlphaComposite.SrcAtop.derive(imageAlpha));
			g2.drawImage(comImage,dragingPoint.x-startPoint.x+relative.x, dragingPoint.y-startPoint.y+relative.y, null);//将两张图片alpha混合在一起
			
			glass.getGraphics().drawImage(bufImage, 0, 0, null);
		}
	}

}
