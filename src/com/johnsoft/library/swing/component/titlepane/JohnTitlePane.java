package com.johnsoft.library.swing.component.titlepane;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.awt.AWTUtilities;

public class JohnTitlePane extends JPanel implements MouseListener,MouseMotionListener,WindowFocusListener
{
	private static final long serialVersionUID = 4643474005753393990L;
	
	public enum MaxImplMode{
		APP_FRAME_EXTENDED_STATE,	APP_BOUNDS, APP_FULLSCREEN
	}
	
	public enum Style{
		BUTTON,SIMPLE,GLASS,NULL
	}
	
	public enum Pos
	{
		NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)),
		SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
		EAST(new Cursor(Cursor.E_RESIZE_CURSOR)),
		WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
		NORTHEAST(new Cursor(Cursor.NE_RESIZE_CURSOR)),
		NORTHWEST(new Cursor(Cursor.NW_RESIZE_CURSOR)),
		SOUTHEAST(new Cursor(Cursor.SE_RESIZE_CURSOR)),
		SOUTHWEST(new Cursor(Cursor.SW_RESIZE_CURSOR)),
		CENTER(new Cursor(Cursor.DEFAULT_CURSOR));
		
		private Cursor cursor;
		
		Pos(Cursor cursor)
		{
			this.cursor=cursor;
		}

		public Cursor getCursor()
		{
			return cursor;
		}

		public void setCursor(Cursor cursor)
		{
			this.cursor = cursor;
		}
	}
	
	private int powerSize;
	private int gap;
	
	private Image backgroundImage;
	private AlphaComposite backgroundAlpha;
	private Image icon;
	
	private String titleString;
	private Font titleStringFont;
	private Paint titleStringPaint;
	
	private Style style;
	private Paint controllerBorderPaint;
	
	private int contentPaneVDecrument;
	private int controllerHDecrument;
	private int controllerWidthIncrument;
	private int windowArc;
	
	private boolean canResize;
	
	private boolean isLiveDrag;
	private Paint unLiveDragPaint;
	private Stroke unLiveDragStroke;
	
	private List<JohnTitleController> controllers=new ArrayList<JohnTitleController>();
	private Map<String,Rectangle2D> controllerBoundsMap=new HashMap<String,Rectangle2D>();
	private Map<Pos, Rectangle> posAreaMap=new HashMap<Pos, Rectangle>();
	
	private MaxImplMode implMode;
	private boolean isMax=false;
	private Rectangle oldBounds=new Rectangle();
	
	private boolean lostFocusUnPaint;
	
	private int abx,aby;
	private Pos dragingPos;
	private boolean isFirstDrag=true,uninit=true;
	private boolean roundbounds;
	
	private JDialog movingBorder;
	private JFrame parent;
	private JPanel contentPane;
	
	public JohnTitlePane(JFrame parent)
	{
		this.parent=parent;
		initDefaults();
		addMouseListener(this);
		addMouseMotionListener(this);
		parent.addWindowFocusListener(this);
	};
	
	/**
	 * 适宜的powerSize与gap、titleStringFont对:
	 * powerSize为14-20时gap为2,10号或12号字体.测试了18,2,12;
	 * powerSize为20-28时gap为3,14号字体.测试了24,3,14;
	 * powerSize为28-32时gap为4,16或18号字体.测试了32,4,18;
	 * 默认为28,4,16
	 */
	protected void initDefaults()
	{
		powerSize=28;
		gap=4;
		
		backgroundImage=new ImageIcon(getClass().getResource("/resource/images/NOCUT_9afhkn.jpg")).getImage();
		backgroundAlpha=AlphaComposite.SrcOver.derive(0.5f);
		
		titleString="johnSoft";
		titleStringFont=new Font("微软雅黑", Font.PLAIN, 16);
		titleStringPaint=Color.WHITE;
		
		controllerBorderPaint=Color.WHITE;
		style=Style.NULL;
		
		implMode=MaxImplMode.APP_BOUNDS;
		lostFocusUnPaint=true;
		
		contentPaneVDecrument=30;
		controllerHDecrument=10;
		controllerWidthIncrument=10;
		windowArc=20;
		
		isLiveDrag=false;
		unLiveDragPaint=Color.GREEN;
		unLiveDragStroke=new BasicStroke(2f);
		
		if(parent.isResizable())
		{
			canResize=true;
		}
		
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(0, powerSize+gap*2));
		setLayout(null);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		if(contentPane!=null)
		{
			contentPane.setBounds(computeContentPaneBounds());
			if(uninit)
			{
				uninit=false;
				revalidate();
			}
		}
		
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Rectangle rect=g2.getClipBounds();
		
		if(!isOpaque())
		{
			g2.setColor(getBackground());
			roundbounds=true;
			g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, windowArc, windowArc);
		}
		
		if(backgroundImage!=null)
		{
			Graphics2D gtemp=(Graphics2D)g2.create();
			if(backgroundAlpha!=null)
			{
				gtemp.setComposite(backgroundAlpha);
			}
			gtemp.drawImage(backgroundImage, rect.x, rect.y, rect.width, rect.height, null);
		}
		
		if(icon!=null)
		{
			g2.drawImage(icon, gap, gap, powerSize, powerSize, null);
			controllerBoundsMap.put("icon", new Rectangle(gap, gap, powerSize, powerSize));
		}
		
		if(titleString!=null&&!"".equals(titleString.trim()))
		{
			g2.setPaint(titleStringPaint);
			g2.setFont(titleStringFont);
			g2.drawString(titleString, powerSize+gap*2, ((float)(powerSize+gap*2)*2f)/3f);
			Rectangle2D rect2=g2.getFontMetrics(titleStringFont).getStringBounds(titleString, g2);
			controllerBoundsMap.put("title", new Rectangle2D.Double(powerSize+gap*2, gap, rect2.getWidth(), powerSize));
		}
		
		for(int i=0;i<controllers.size();i++)
		{
			Rectangle cellRect=computeControllerBounds(i, rect);
			controllers.get(i).setOwner(this);
			controllers.get(i).paintController((Graphics2D)g2.create(), cellRect);
			controllerBoundsMap.put(controllers.get(i).getName(), cellRect);
		}
		
		styleBorder(g2);
		
		if(!parent.isActive()||!parent.isFocused())
		{
			paintDeactivated(g2);
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(isCanResize())
		{
			computeAndAddResizeArea(getBounds());
			Pos p=getContainPos(e.getX(), e.getY());
			if(p!=null)
			{
				setCursor(p.getCursor());
				return;
			}else{
				setCursor(Pos.CENTER.getCursor());
			}
		}
		boolean overController=false;
		for(JohnTitleController controller:controllers)
		{
			if(controllerBoundsMap.get(controller.getName()).contains(new Point(e.getX(), e.getY())))
			{
				controller.mouseOver(e);
				setToolTipText(controller.getTooltip());
				overController=true;
			}else{
				controller.mouseOut(e);
			}
		}
		if(!overController)
		{
			setToolTipText(null);
		}
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		Rectangle r=parent.getBounds();
		abx=e.getXOnScreen()-r.x;
		aby=e.getYOnScreen()-r.y;
		dragingPos=getContainPos(e.getX(), e.getY());
		JohnTitleController controller=getContainedController(e.getX(), e.getY());
		if(controller!=null)
		{
			controller.mousePress(e);
		}
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		isFirstDrag=true;
		if(movingBorder!=null&&movingBorder.isVisible())
		{
			parent.setBounds(movingBorder.getBounds());
			movingBorder.setVisible(false);
		}else{
			JohnTitleController controller=getContainedController(e.getX(), e.getY());
			if(controller!=null)
			{
				controller.mouseRelease(e);
			}
		}
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(!isMax())
		{
			if(isInBlankArea(e.getX(),e.getY()))
			{
				if(isLiveDrag)
				{
					 if(dragingPos!=null)
					{
						parent.setBounds(computeResizeBounds(dragingPos,e.getXOnScreen(), e.getYOnScreen()));
						return;
					}
					parent.setLocation(e.getXOnScreen()-abx, e.getYOnScreen()-aby);
				}else{
					if(isFirstDrag)
					{
						getMovingBorder();
						isFirstDrag=false;
					}
					if(dragingPos!=null)
					{
						movingBorder.setBounds(computeResizeBounds(dragingPos,e.getXOnScreen(), e.getYOnScreen()));
						return;
					}
					movingBorder.setLocation(e.getXOnScreen()-abx, e.getYOnScreen()-aby);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount()==2&&isInBlankArea(e.getX(),e.getY()))
		{
			maxOrNormal();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		setCursor(Pos.CENTER.getCursor());
		if(movingBorder==null||!movingBorder.isVisible())
		{
			repaint();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e)
	{
		repaint();
	}

	@Override
	public void windowLostFocus(WindowEvent e)
	{
		if(movingBorder!=null&&!movingBorder.isVisible())
		{
			repaint();
		}
	}
	
	protected void getMovingBorder()
	{
		if(movingBorder!=null)
		{
			movingBorder.setBounds(parent.getBounds());
			movingBorder.setVisible(true);
		}else{
			movingBorder=new JDialog();
			movingBorder.setUndecorated(true);
			JPanel border=new JPanel(){
				private static final long serialVersionUID = 1L;
				@Override
				public void paint(Graphics g)
				{
					Graphics2D g2=(Graphics2D)g;
					Rectangle rect=g2.getClipBounds();
					g2.setStroke(unLiveDragStroke);
					g2.setPaint(unLiveDragPaint);
					if(roundbounds)
					{
						g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, windowArc, windowArc);
					}else{
						g2.draw(rect);
					}
				}
			};
			movingBorder.setContentPane(border);
			AWTUtilities.setWindowOpaque(movingBorder, false);
			movingBorder.setBounds(parent.getBounds());
			movingBorder.setVisible(true);
		}
	}
	
	protected Rectangle computeContentPaneBounds()
	{
		if(contentPaneVDecrument<0)
		{
			return new Rectangle(gap,powerSize+gap*2, parent.getBounds().width-gap*2,parent.getBounds().height-gap*3-powerSize);
		}else{
			return new Rectangle(1,powerSize+gap*2, parent.getBounds().width-2,parent.getBounds().height-gap*2-powerSize-contentPaneVDecrument);
		}
	}
	
	protected Rectangle computeControllerBounds(int index,Rectangle rect)
	{
		int x=rect.x+rect.width-(powerSize+gap+controllerWidthIncrument)*(index+1)-controllerHDecrument;
		return new Rectangle(x, gap, powerSize+controllerWidthIncrument, powerSize);
	}
	
	protected void computeAndAddResizeArea(Rectangle r)
	{
		posAreaMap.put(Pos.NORTHWEST, new Rectangle(0, 0, gap, gap));
		posAreaMap.put(Pos.WEST, new Rectangle(0, gap, gap, r.height-gap*2));
		posAreaMap.put(Pos.SOUTHWEST, new Rectangle(0, r.height-gap, gap, gap));
		posAreaMap.put(Pos.SOUTH, new Rectangle(gap, r.height-gap, r.width-gap*2, gap));
		posAreaMap.put(Pos.SOUTHEAST, new Rectangle(r.width-gap, r.height-gap, gap, gap));
		posAreaMap.put(Pos.EAST, new Rectangle(r.width-gap, gap, gap, r.height-gap*2));
		posAreaMap.put(Pos.NORTHEAST, new Rectangle(r.width-gap,0,gap,gap));
		posAreaMap.put(Pos.NORTH, new Rectangle(gap, 0, r.width-gap*2, gap));
	}
	
	protected Rectangle computeResizeBounds(Pos p,int x, int y)
	{
		Rectangle r=parent.getBounds();
		switch (p)
		{
			case EAST: 
				r.setSize(x-r.x,r.height);
				break;
			case SOUTH: 
				r.setSize(r.width,y-r.y);
				break;
			case SOUTHEAST:
				r.setSize(x-r.x, y-r.y);
				break;
			case WEST:
				r.setBounds(x, r.y, r.width+r.x-x, r.height);
				break;
			case NORTH:
				r.setBounds(r.x, y, r.width, r.height+r.y-y);
				break;
			case SOUTHWEST:
				r.setBounds(x, r.y, r.width+r.x-x, y-r.y);
				break;
			case NORTHEAST:
				r.setBounds(r.x, y, x-r.x, r.height+r.y-y);
				break;
			case NORTHWEST:
				r.setBounds(x, y, r.width+r.x-x, r.height+r.y-y);
				break;
			default: 
				break;
		}
		return r;
	}
	
	protected void paintDeactivated(Graphics2D g2)
	{
		if(lostFocusUnPaint)
		{
			return;
		}
		Rectangle rect=g2.getClipBounds();
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		g2.fill(rect);
	}
	
	protected void styleBorder(Graphics2D g2)
	{
		if(style==Style.BUTTON)
		{
			for(JohnTitleController controller:controllers)
			{
				Rectangle rect=controllerBoundsMap.get(controller.getName()).getBounds();
				if(controllerBorderPaint!=null)
				{
					g2.setPaint(controllerBorderPaint);
					g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, gap*2, gap*2);
				}else{
					Point p=new Point(abx,aby);
					SwingUtilities.convertPoint(parent, p, this);
					if(rect.contains(p))
					{
						g2.draw3DRect(rect.x, rect.y, rect.width, rect.height, false);
					}else{
						g2.draw3DRect(rect.x, rect.y, rect.width, rect.height, true);
					}
				}
			}
		}
		else if(style==Style.SIMPLE)
		{
			
		}
		else if(style==Style.GLASS)
		{
			
		}
		else if(style==Style.NULL){
		}
	}
	
	public JPanel asContentPane()
	{
		if(!parent.isUndecorated())
		{
			parent.setUndecorated(true);
		}
		AWTUtilities.setWindowOpaque(parent, false);
		setOpaque(false);
		JohnTitleDefaultControllers.register(this);
		parent.setContentPane(this);
		contentPane=new JPanel(new BorderLayout());
		contentPane.addMouseListener(new MouseAdapter(){});
		contentPane.addMouseMotionListener(new MouseAdapter(){});
		this.add(contentPane);
		return contentPane;
	}
	
	public void maxOrNormal()
	{
		if(implMode==MaxImplMode.APP_FRAME_EXTENDED_STATE)
		{
			if(parent.getExtendedState()==JFrame.NORMAL)
			{
				parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}else{
				parent.setExtendedState(JFrame.NORMAL);
			}
		}
		else if(implMode==MaxImplMode.APP_BOUNDS)
		{
			if(isMax)
			{
				parent.setBounds(oldBounds);
				isMax=false;
			}else{
				oldBounds=parent.getBounds();
				parent.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
				isMax=true;
			}
		}
		else if(implMode==MaxImplMode.APP_FULLSCREEN)
		{
			if(isMax)
			{
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
				isMax=false;
			}else{
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(parent);
				isMax=true;
			}
		}
	}
	
	public Pos getContainPos(int x,int y)
	{
		for(Pos p:posAreaMap.keySet())
		{
			if(getRect(p).contains(x, y))
			{
				return p;
			}
		}
		return null;
	}
	
	public Rectangle getRect(Pos p)
	{
		return posAreaMap.get(p);
	}
	
	public JohnTitleController getContainedController(int x,int y)
	{
		for(JohnTitleController controller:controllers)
		{
			if(controllerBoundsMap.get(controller.getName()).contains(new Point(x, y)))
			{
				return controller;
			}
		}
		return null;
	}
	
	public boolean isInBlankArea(int x,int y)
	{
		for(String key:controllerBoundsMap.keySet())
		{
			if(controllerBoundsMap.get(key).contains(new Point(x,y)))
			{
				return false;
			}
		}
		return true;
	}
	
	public void setPaintNothingOnFocusLost(boolean lostFocusUnPaint)
	{
		this.lostFocusUnPaint=lostFocusUnPaint;
	}
	
	public boolean isMax()
	{
		return isMax||(parent.getExtendedState()==JFrame.MAXIMIZED_BOTH);
	}
	
	public boolean isCanResize()
	{
		return canResize&&!isMax()&&parent.isResizable();
	}

	public void setCanResize(boolean canResize)
	{
		if(!parent.isResizable()&&canResize)
		{
			throw new RuntimeException("top level ancestor cannot resize");
		}
		this.canResize = canResize;
	}
	
  //Getter And Setter
	
	public MaxImplMode getImplMode()
	{
		return implMode;
	}

	public void setImplMode(MaxImplMode implMode)
	{
		this.implMode = implMode;
	}

	public int getPowerSize()
	{
		return powerSize;
	}

	public void setPowerSize(int powerSize)
	{
		this.powerSize = powerSize;
	}

	public int getGap()
	{
		return gap;
	}

	public void setGap(int gap)
	{
		this.gap = gap;
	}
	
	public Image getBackgroundImage()
	{
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage)
	{
		this.backgroundImage = backgroundImage;
	}

	public String getTitleString()
	{
		return titleString;
	}

	public void setTitleString(String titleString)
	{
		this.titleString = titleString;
	}

	public Font getTitleStringFont()
	{
		return titleStringFont;
	}

	public void setTitleStringFont(Font titleStringFont)
	{
		this.titleStringFont = titleStringFont;
	}

	public Paint getTitleStringPaint()
	{
		return titleStringPaint;
	}

	public void setTitleStringPaint(Paint titleStringPaint)
	{
		this.titleStringPaint = titleStringPaint;
	}
	
	public AlphaComposite getBackgroundAlpha()
	{
		return backgroundAlpha;
	}

	public void setBackgroundAlpha(AlphaComposite backgroundAlpha)
	{
		this.backgroundAlpha = backgroundAlpha;
	}

	public Image getIcon()
	{
		return icon;
	}

	public void setIcon(Image icon)
	{
		this.icon = icon;
	}
	
	public Style getStyle()
	{
		return style;
	}

	public void setStyle(Style style)
	{
		this.style = style;
	}

	public Paint getControllerBorderPaint()
	{
		return controllerBorderPaint;
	}

	public void setControllerBorderPaint(Paint controllerBorderPaint)
	{
		this.controllerBorderPaint = controllerBorderPaint;
	}
	
	public int getContentPaneVDecrument()
	{
		return contentPaneVDecrument;
	}

	public void setContentPaneVDecrument(int contentPaneVDecrument)
	{
		this.contentPaneVDecrument = contentPaneVDecrument;
	}
	
	public int getControllerHDecrument()
	{
		return controllerHDecrument;
	}

	public void setControllerHDecrument(int controllerHDecrument)
	{
		this.controllerHDecrument = controllerHDecrument;
	}

	public int getControllerWidthIncrument()
	{
		return controllerWidthIncrument;
	}

	public void setControllerWidthIncrument(int controllerWidthIncrument)
	{
		this.controllerWidthIncrument = controllerWidthIncrument;
	}

	public int getWindowArc()
	{
		return windowArc;
	}

	public void setWindowArc(int windowArc)
	{
		this.windowArc = windowArc;
	}

	public boolean isLiveDrag()
	{
		return isLiveDrag;
	}

	public void setLiveDrag(boolean isLiveDrag)
	{
		this.isLiveDrag = isLiveDrag;
	}

	public Paint getUnLiveDragPaint()
	{
		return unLiveDragPaint;
	}

	public void setUnLiveDragPaint(Paint unLiveDragPaint)
	{
		this.unLiveDragPaint = unLiveDragPaint;
	}

	public Stroke getUnLiveDragStroke()
	{
		return unLiveDragStroke;
	}

	public void setUnLiveDragStroke(Stroke unLiveDragStroke)
	{
		this.unLiveDragStroke = unLiveDragStroke;
	}
	
	public List<JohnTitleController> getControllers()
	{
		return controllers;
	}

	public void setControllers(List<JohnTitleController> controllers)
	{
		this.controllers = controllers;
	}

	public Map<String, Rectangle2D> getControllerBoundsMap()
	{
		return controllerBoundsMap;
	}

	public void setControllerBoundsMap(Map<String, Rectangle2D> controllerBoundsMap)
	{
		this.controllerBoundsMap = controllerBoundsMap;
	}
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		} 
//
//		final JFrame jf=new JFrame();
////	jf.setUndecorated(true);
//	JohnTitlePane title=new JohnTitlePane(jf);
////	JohnTitleDefaultControllers.register(title);
////	jf.add(title,BorderLayout.NORTH);
//  final JPanel content=title.asContentPane();
//		JPanel jp=new JPanel(){
//			private static final long serialVersionUID = 1L;
//			@Override
//			protected void paintComponent(Graphics g)
//			{
//				super.paintComponent(g);
//				Graphics2D g2=(Graphics2D)g;
//				g2.setColor(Color.RED);
//				JohnIconDrawer.drawCloseIcon(g2, new Rectangle(100, 100, 32, 32), 4);
//			}
//		};
//	  content.add(new JLabel("this is a title"),BorderLayout.NORTH);
//	  content.add(jp);
////		jf.setLocationRelativeTo(null);
//	//	jf.setResizable(false);
//		 jf.setBounds(100, 100, 400, 400);
//		jf.setVisible(true);
//	}
	
}
