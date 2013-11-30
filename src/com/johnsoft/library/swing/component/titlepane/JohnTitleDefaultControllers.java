package com.johnsoft.library.swing.component.titlepane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;

public class JohnTitleDefaultControllers
{
	public static void register(JohnTitlePane pane)
	{
		pane.getControllers().add(new CloseTitleController());
		pane.getControllers().add(new MaxTitleController());
		pane.getControllers().add(new MinTitleController());
	}
	
	public static abstract class AbstractTitleController implements JohnTitleController
	{
		private Paint rollOverBackgroundPaint=Color.GRAY;
		private Paint rollOverForegroundPaint=Color.WHITE;
		private Stroke rollOverStroke=new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
		private Paint defaultBackgroundPaint=new Color(0, 0, 0, 0);
		private Paint defaultForegroundPaint=Color.WHITE;
		private Stroke defaultStroke=new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);;
		private Paint pressingBackgroundPaint=Color.WHITE;
		private Paint pressingForegroundPaint=Color.RED;
		private Stroke pressingStroke=new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);;
		private Paint disabledBackgroundPaint=Color.BLUE;
		private Paint disabledForegroundPaint=Color.GRAY;
		private Stroke disabledStroke=new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);;
		private boolean isMouseOver=false;
		private boolean isMousePress=false;
		private boolean isDisabled=false;
		private JohnTitlePane titlePane;
		
		//gap和powerSize仅是参考值,且设置任何属性都可在此方法中进行
		public abstract Shape getDrawShape(Rectangle2D r2);
		
		public Shape getFillShape(Rectangle2D r2)
		{
			setRollOverBackgroundPaint(new LinearGradientPaint(new Point2D.Double(r2.getX()+r2.getWidth()/2,r2.getY()), new Point2D.Double(r2.getX()+r2.getWidth()/2,r2.getY()+r2.getHeight()),new float[]{0,0.5f,1},new Color[]{Color.WHITE,new Color(0,0,0,0),Color.WHITE}));
			return new RoundRectangle2D.Double(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight(),getGap()*2, getGap()*2);
		}
		
		public Rectangle2D getFitDrawArea(Rectangle2D r2)
		{
			double x=r2.getX()+(r2.getWidth()-getPowerSize())/2;
			double y=r2.getY()+(r2.getHeight()-getPowerSize())/2;
			return new Rectangle2D.Double(x, y, getPowerSize(), getPowerSize());
		}
		
		public void initStyle(){
		}
		
		@Override
		public void paintController(Graphics2D g2, Rectangle2D r2)
		{
			Shape fillShape=getFillShape(r2);
			Shape drawShape=getDrawShape(getFitDrawArea(r2));
			initStyle();
			if(isMouseOver)
			{
				isMouseOver=false;
				g2.setPaint(rollOverBackgroundPaint);
				g2.fill(fillShape);
				g2.setStroke(rollOverStroke);
				g2.setPaint(rollOverForegroundPaint);
				g2.draw(drawShape);
			}
			else if(isMousePress)
			{
				isMousePress=false;
				g2.setPaint(pressingBackgroundPaint);
				g2.fill(fillShape);
				g2.setStroke(pressingStroke);
				g2.setPaint(pressingForegroundPaint);
				g2.draw(drawShape);
			}
			else if(isDisabled)
			{
				g2.setPaint(disabledBackgroundPaint);
				g2.fill(fillShape);
				g2.setStroke(disabledStroke);
				g2.setPaint(disabledForegroundPaint);
				g2.draw(drawShape);
			}
			else
			{
				g2.setPaint(defaultBackgroundPaint);
				g2.fill(r2);
				g2.setStroke(defaultStroke);
				g2.setPaint(defaultForegroundPaint);
				g2.draw(drawShape);
			}
		}
		
		@Override
		public void mouseOver(MouseEvent event)
		{
			if(!isDisabled)
			{
				isMouseOver=true;
			}
		}
		
		@Override
		public void mousePress(MouseEvent event)
		{
			if(!isDisabled)
			{
				isMousePress=true;
			}
		}
		
		@Override
		public void mouseOut(MouseEvent event)
		{
			if(!isDisabled)
			{
				isMouseOver=false;
				isMousePress=false;
			}
		}
		
		@Override
		public void mouseRelease(MouseEvent event)
		{
			if(!isDisabled)
			{
				isMouseOver=false;
				isMousePress=false;
			}
		}
		
		//必须在实例化子类后调用其他方法前先调用此方法传入JohnTitlePane
		@Override
		public void setOwner(JohnTitlePane titlePane)
		{
			this.titlePane=titlePane;
		}

		//Getter And Setter
		
		public Paint getRollOverBackgroundPaint()
		{
			return rollOverBackgroundPaint;
		}

		public void setRollOverBackgroundPaint(Paint rollOverBackgroundPaint)
		{
			this.rollOverBackgroundPaint = rollOverBackgroundPaint;
		}

		public Paint getRollOverForegroundPaint()
		{
			return rollOverForegroundPaint;
		}

		public void setRollOverForegroundPaint(Paint rollOverForegroundPaint)
		{
			this.rollOverForegroundPaint = rollOverForegroundPaint;
		}

		public Stroke getRollOverStroke()
		{
			return rollOverStroke;
		}

		public void setRollOverStroke(Stroke rollOverStroke)
		{
			this.rollOverStroke = rollOverStroke;
		}

		public Paint getDefaultBackgroundPaint()
		{
			return defaultBackgroundPaint;
		}

		public void setDefaultBackgroundPaint(Paint defaultBackgroundPaint)
		{
			this.defaultBackgroundPaint = defaultBackgroundPaint;
		}

		public Paint getDefaultForegroundPaint()
		{
			return defaultForegroundPaint;
		}

		public void setDefaultForegroundPaint(Paint defaultForegroundPaint)
		{
			this.defaultForegroundPaint = defaultForegroundPaint;
		}

		public Stroke getDefaultStroke()
		{
			return defaultStroke;
		}

		public void setDefaultStroke(Stroke defaultStroke)
		{
			this.defaultStroke = defaultStroke;
		}

		public Paint getPressingBackgroundPaint()
		{
			return pressingBackgroundPaint;
		}

		public void setPressingBackgroundPaint(Paint pressingBackgroundPaint)
		{
			this.pressingBackgroundPaint = pressingBackgroundPaint;
		}

		public Paint getPressingForegroundPaint()
		{
			return pressingForegroundPaint;
		}

		public void setPressingForegroundPaint(Paint pressingForegroundPaint)
		{
			this.pressingForegroundPaint = pressingForegroundPaint;
		}

		public Stroke getPressingStroke()
		{
			return pressingStroke;
		}

		public void setPressingStroke(Stroke pressingStroke)
		{
			this.pressingStroke = pressingStroke;
		}

		public Paint getDisabledBackgroundPaint()
		{
			return disabledBackgroundPaint;
		}

		public void setDisabledBackgroundPaint(Paint disabledBackgroundPaint)
		{
			this.disabledBackgroundPaint = disabledBackgroundPaint;
		}

		public Paint getDisabledForegroundPaint()
		{
			return disabledForegroundPaint;
		}

		public void setDisabledForegroundPaint(Paint disabledForegroundPaint)
		{
			this.disabledForegroundPaint = disabledForegroundPaint;
		}

		public Stroke getDisabledStroke()
		{
			return disabledStroke;
		}

		public void setDisabledStroke(Stroke disabledStroke)
		{
			this.disabledStroke = disabledStroke;
		}

		public boolean isMouseOver()
		{
			return isMouseOver;
		}

		public void setMouseOver(boolean isMouseOver)
		{
			this.isMouseOver = isMouseOver;
		}

		public boolean isMousePress()
		{
			return isMousePress;
		}

		public void setMousePress(boolean isMousePress)
		{
			this.isMousePress = isMousePress;
		}

		public boolean isDisabled()
		{
			return isDisabled;
		}

		public void setDisabled(boolean isDisabled)
		{
			this.isDisabled = isDisabled;
		}

		public int getGap()
		{
			return titlePane.getGap();
		}

		public int getPowerSize()
		{
			return titlePane.getPowerSize();
		}

		public JFrame getJframe()
		{
			return (JFrame)titlePane.getTopLevelAncestor();
		}
		
		public JohnTitlePane getTitlePane()
		{
			return titlePane;
		}
	}
	
	public static class MinTitleController extends AbstractTitleController
	{
		@Override
		public void mouseRelease(MouseEvent event)
		{
			if(!isDisabled())
			{
				super.mouseRelease(event);
				if(getJframe().getExtendedState()==JFrame.MAXIMIZED_BOTH)
				{
					getJframe().setExtendedState(JFrame.ICONIFIED|JFrame.MAXIMIZED_BOTH);
				}else{
					getJframe().setExtendedState(JFrame.ICONIFIED);
				}
			}
		}
		
		@Override
		public Shape getDrawShape(Rectangle2D r2)
		{
			return new Rectangle2D.Double(r2.getX()+getGap()*2, r2.getY()+r2.getHeight()/2-1, r2.getWidth()-getGap()*4, 2);
		}

		@Override
		public String getName()
		{
			return "min";
		}
		
		@Override
		public String getTooltip()
		{
			return "最小化";
		}
	}
	
	public static class MaxTitleController extends AbstractTitleController
	{
		@Override
		public void mouseRelease(MouseEvent event)
		{
			if(!isDisabled())
			{
				super.mouseRelease(event);
				getTitlePane().maxOrNormal();
			}
		}
		
		@Override
		public Shape getDrawShape(Rectangle2D r2)
		{
			setDefaultStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			setDisabledStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			setPressingStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			setRollOverStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			if(!getJframe().isResizable())
			{
				setDisabled(true);
			}
			Path2D.Double path=new Path2D.Double();
			if(getTitlePane().isMax())
			{
				path.append(new Rectangle2D.Double(r2.getX()+getGap()*2, r2.getY()+getGap()*3, r2.getWidth()-getGap()*5, r2.getHeight()-getGap()*5), false);
				path.moveTo(r2.getX()+getGap()*3, r2.getY()+getGap()*3);
				path.lineTo(r2.getX()+getGap()*3, r2.getY()+getGap()*2);
				path.lineTo(r2.getX()+r2.getWidth()-getGap()*2, r2.getY()+getGap()*2);
				path.lineTo(r2.getX()+r2.getWidth()-getGap()*2, r2.getY()+r2.getHeight()-getGap()*3);
				path.lineTo(r2.getX()+r2.getWidth()-getGap()*3, r2.getY()+r2.getHeight()-getGap()*3);
			}else{
				path.append(new Rectangle2D.Double(r2.getX()+getGap()*2, r2.getY()+getGap()*2, r2.getWidth()-getGap()*4, r2.getHeight()-getGap()*4),false);
				path.moveTo(r2.getX()+getGap()*2, r2.getY()+getGap()*2-1);
				path.lineTo(r2.getX()+r2.getWidth()-getGap()*2, r2.getY()+getGap()*2-1);
				path.moveTo(r2.getX()+getGap()*2, r2.getY()+getGap()*2+1);
				path.lineTo(r2.getX()+r2.getWidth()-getGap()*2, r2.getY()+getGap()*2+1);
			}
			return path;
		}

		@Override
		public String getName()
		{
			return "max";
		}
		
		@Override
		public String getTooltip()
		{
			if(getJframe().getExtendedState()==JFrame.NORMAL)
			{
				return "最大化";
			}else{
				return "还原";
			}
		}
	}

	public static class CloseTitleController extends AbstractTitleController
	{
		@Override
		public void mouseRelease(MouseEvent event)
		{
			if(isDisabled())
			{
				return;
			}
			super.mouseRelease(event);
			EventQueue eq=Toolkit.getDefaultToolkit().getSystemEventQueue();
			eq.postEvent(new WindowEvent(getJframe(), WindowEvent.WINDOW_CLOSING));
			int operate=getJframe().getDefaultCloseOperation();
			if(operate==JFrame.EXIT_ON_CLOSE)
			{
				System.exit(0);
			}
			else if(operate==JFrame.DISPOSE_ON_CLOSE)
			{
				getJframe().dispose();
			}
			else if(operate==JFrame.HIDE_ON_CLOSE)
			{
				getJframe().setVisible(false);
			}
		}
		
		@Override
		public Shape getDrawShape(Rectangle2D r2)
		{
			Path2D.Double path=new Path2D.Double();
			path.moveTo(r2.getX()+getGap()*2, r2.getY()+getGap()*2);
			path.lineTo(r2.getX()+r2.getWidth()-getGap()*2, r2.getY()+r2.getHeight()-getGap()*2);
			path.moveTo(r2.getX()+r2.getWidth()-getGap()*2, r2.getY()+getGap()*2);
			path.lineTo(r2.getX()+getGap()*2, r2.getY()+r2.getHeight()-getGap()*2);
			return path;
		}

		@Override
		public String getName()
		{
			return "close";
		}
		
		@Override
		public String getTooltip()
		{
			return "关闭";
		}
	}
}
