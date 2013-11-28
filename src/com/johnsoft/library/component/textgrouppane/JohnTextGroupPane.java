package com.johnsoft.library.component.textgrouppane;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;

import com.johnsoft.library.util.data.fn.JohnObooIstrFn;

public class JohnTextGroupPane extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private EventListenerList listenerList=new EventListenerList();
	
	private Dimension fieldPreferSize;
	private Dimension plusBtnPreferSize;
	private Dimension minusBtnPreferSize;
	
	private int currRowIdx=1;
	private AppendingPaneLayout layout;
	private JohnTextGroupUnit unit;
	private JTextComponent plusField;
	private JComponent plusButton;
	private JComponent minusButton;
	
//	public static void main(String[] args)
//	{
//		JFrame jf=new JFrame();
//		final JohnAppendingTextFieldPane pane=new JohnAppendingTextFieldPane(new JohnAbstractAppendingUnit()
//		{
//			@Override
//			public JTextComponent getField()
//			{
//				JTextField jtf=new JTextField();
//				return jtf;
//			}
//		}, 10, 10, 10, 10);
//		//pane.setPreferredSize(new Dimension(500, 400));
//		pane.addMouseListener(new MouseAdapter()
//		{
//			@Override
//			public void mouseClicked(MouseEvent e)
//			{
//				if(e.getButton()==MouseEvent.BUTTON1)
//				{
//					for(String text:pane.getUnEmptyValues())
//					{
//						System.out.println(text);
//					}
//				}
//				if(e.getButton()==MouseEvent.BUTTON2)
//				{
//					for(String text:pane.getValues())
//					{
//						System.out.println(text);
//					}
//				}
//				if(e.getButton()==MouseEvent.BUTTON3)
//				{
//					pane.clear();
//				}
//				
//			}
//		});
//		jf.add(pane);
//		jf.pack();
//		jf.setVisible(true);
//	}
	
	public JohnTextGroupPane(JohnTextGroupUnit unit,int hMargin,int vMargin,int hGap,int vGap)
	{
		this.unit=unit;
		layout=new AppendingPaneLayout(hMargin, vMargin, hGap, vGap);
		super.setLayout(layout);
		plusButton=unit.getPlusButton();
		plusField=unit.getField();
		fieldPreferSize=plusField.getPreferredSize();
		plusBtnPreferSize=plusButton.getPreferredSize();
		plusButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				add();
			}
		});
		super.add(plusField);
		super.add(plusButton);
	}
	
	public void add()
	{
		fireStateChanged(new JohnTextGroupEvent(this, JohnTextGroupEvent.UNIT_ADDING,(JTextComponent)getComponent(currRowIdx==1?0:currRowIdx)));
		if(minusButton!=null)
		{
			super.remove(getComponentCount()-1);
		}
		minusButton=unit.getMinusButton();
		minusBtnPreferSize=minusButton.getPreferredSize();
		minusButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				remove();
			}
		});
		super.add(unit.getField());
		currRowIdx++;
		super.add(minusButton);
		layout.setRowCount(currRowIdx);
		fitSize();		
		revalidate();
		repaint();
		fireStateChanged(new JohnTextGroupEvent(this, JohnTextGroupEvent.UNIT_ADDED,(JTextComponent)getComponent(currRowIdx)));
	}
	
	public void remove()
	{
		fireStateChanged(new JohnTextGroupEvent(this, JohnTextGroupEvent.UNIT_REMOVEING,(JTextComponent)getComponent(currRowIdx)));
		super.remove(getComponentCount()-1);
		super.remove(currRowIdx);
		currRowIdx--;
		super.add(minusButton);
		layout.setRowCount(currRowIdx);
		fitSize();
		revalidate();
		repaint();
		fireStateChanged(new JohnTextGroupEvent(this, JohnTextGroupEvent.UNIT_REMOVEED,(JTextComponent)getComponent(currRowIdx==1?0:currRowIdx)));
	}
	
	public void clear()
	{
		fireStateChanged(new JohnTextGroupEvent(this, JohnTextGroupEvent.GROUP_CLEARING,null));
		int count=getComponentCount();
		for(int i=count-1;i>1;i--)
		{
			super.remove(i);
		}
		minusButton=null;
		currRowIdx=1;
		layout.setRowCount(currRowIdx);
		Component c=super.getComponent(0);
		if(c instanceof JTextComponent)
		{
			((JTextComponent)c).setText("");
		}
		setPreferredSize(layout.preferredLayoutSize(this));
		revalidate();
		repaint();
		fireStateChanged(new JohnTextGroupEvent(this, JohnTextGroupEvent.GROUP_CLEARED,null));
	}
	
	protected void fitSize()
	{
		int h1=Math.max(plusBtnPreferSize.height, fieldPreferSize.height);
		int h2=Math.max(minusBtnPreferSize.height, fieldPreferSize.height);
		int h=getInsets().top + getInsets().bottom +layout.getvGap()*(currRowIdx-1)+layout.getvMargin()*2;
		h+=h1+(currRowIdx>1?h2:0)+((currRowIdx>2?currRowIdx-2:0)*fieldPreferSize.height);
		setPreferredSize(new Dimension(getPreferredSize().width,h));
	}
	
	public JComponent getPlusButton()
	{
		return plusButton;
	}
	
	public JTextComponent getPlusField()
	{
		return plusField;
	}
	
	public JComponent getMinusButton()
	{
		return minusButton;
	}
	
	public List<JTextComponent> getFields()
	{
		List<JTextComponent> list=new ArrayList<JTextComponent>();
		for(Component comp:getComponents())
		{
			if(comp instanceof JTextComponent)
			{
				list.add((JTextComponent)comp);
			}
		}
		return list;
	}
	
	public List<String> getValues()
	{
		List<String> list=new ArrayList<String>();
		for(Component comp:getComponents())
		{
			if(comp instanceof JTextComponent)
			{
				JTextComponent jtf=(JTextComponent)comp;
				list.add(jtf.getText());
			}
		}
		return list;
	}
	
	public List<String> getUnEmptyValues()
	{
		return getFilteredValues(new JohnObooIstrFn()
		{
			@Override
			public boolean function(String text)
			{
				return !text.trim().isEmpty();
			}
		});
	}
	
	public List<String> getFilteredValues(JohnObooIstrFn fn)
	{
		List<String> list=new ArrayList<String>();
		for(Component comp:getComponents())
		{
			if(comp instanceof JTextComponent)
			{
				String text=((JTextComponent)comp).getText();
				if(fn.function(text))
				{
					list.add(text);
				}
			}
		}
		return list;
	}
	
	public boolean hadContent()
	{
		for(Component comp:getComponents())
		{
			if(comp instanceof JTextComponent)
			{
				String text=((JTextComponent)comp).getText();
				if(!text.trim().isEmpty())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void addTextGroupListener(JohnTextGroupListener listener)
	{
		listenerList.add(JohnTextGroupListener.class, listener);
	}
	
	public void removeTextGroupListener(JohnTextGroupListener listener)
	{
		listenerList.remove(JohnTextGroupListener.class, listener);
	}
	
	public void fireStateChanged(JohnTextGroupEvent evt)
	{
		Object[] listeners=listenerList.getListenerList();
		for(int i=listeners.length-2;i>=0;i-=2)
		{
			if(listeners[i]==JohnTextGroupListener.class)
			{
				((JohnTextGroupListener)listeners[i+1]).stateChanged(evt);
			}
		}
	}
	
	@Override
	public Component add(Component comp)
	{
		return comp;
	}
	
	@Override
	public void add(Component comp, Object constraints){
	}
	
	@Override
	public Component add(Component comp, int index){
		return comp;
	}
	
	@Override
	public void add(Component comp, Object constraints, int index){
	}
	
	@Override
	public Component add(String name, Component comp){
		return comp;
	}
	
	@Override
	public synchronized void add(PopupMenu popup){
	}
	
	@Override
	public void remove(Component comp){
	}
	
	@Override
	public void remove(int index){
	}
	
	@Override
	public void removeAll(){
	}
	
	@Override
	public synchronized void remove(MenuComponent popup){
	}
	
	@Override
	public void setLayout(LayoutManager mgr){
	}
	
}

class AppendingPaneLayout implements LayoutManager
{
	private int hMargin;
	private int vMargin;
	private int hGap;
	private int vGap;
	private int rowCount=1;
	
	public AppendingPaneLayout(int hMargin,int vMargin,int hGap,int vGap)
	{
		this.hMargin=hMargin;
		this.vMargin=vMargin;
		this.hGap=hGap;
		this.vGap=vGap;
	}
	
	public void setRowCount(int rowCount)
	{
		this.rowCount=rowCount;
	}
	
	public int getRowCount()
	{
		return rowCount;
	}
	
	public int gethMargin()
	{
		return hMargin;
	}

	public int getvMargin()
	{
		return vMargin;
	}

	public int gethGap()
	{
		return hGap;
	}

	public int getvGap()
	{
		return vGap;
	}

	@Override
	public void addLayoutComponent(String name, Component comp){
	}
	@Override
	public void removeLayoutComponent(Component comp){
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		 synchronized (parent.getTreeLock())
		 {
				Insets insets = parent.getInsets();
				Dimension dim0=parent.getComponent(0).getMinimumSize();
				Dimension dim1=parent.getComponent(1).getMinimumSize();
				return new Dimension(insets.left + insets.right + dim0.width+dim1.width+hGap+hMargin*2, 
						     		 insets.top + insets.bottom + Math.max(dim0.height,dim1.height) +vGap*(rowCount-1)+vMargin*2);
		 }
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		  synchronized (parent.getTreeLock())
		  {
				Insets insets = parent.getInsets();
				Dimension dim0=parent.getComponent(0).getPreferredSize();
				Dimension dim1=parent.getComponent(1).getPreferredSize();
				return new Dimension(insets.left + insets.right + dim0.width + dim1.width+hGap+hMargin*2, 
						     		 insets.top + insets.bottom + Math.max(dim0.height,dim1.height) + vGap*(rowCount-1) + vMargin*2);
		  }
	}

	@Override
	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock()) 
		{
			int ncomponents = parent.getComponentCount();
			if (ncomponents == 0) 
			{
			    return;
			}
			
			Insets insets = parent.getInsets();
			Dimension dim0=parent.getComponent(0).getPreferredSize();
			Dimension dim1=parent.getComponent(1).getPreferredSize();
			int pw = parent.getWidth() - (insets.left + insets.right);
//			int ph = parent.getHeight() - (insets.top + insets.bottom);
			int w1 = dim1.width;
			int h1 = dim1.height;
			int x1 = pw-hMargin-w1;
			int y1=0;
			int w0 = pw-2*hMargin-hGap-w1;
			int h0 = dim0.height;
			int x0 = hMargin;
			int y0=0;
			
			if(h0>h1)
			{
				y0=vMargin;
				y1=vMargin+(h0-h1)/2;
			}else{
				y1=vMargin;
				y0=vMargin+(h1-h0)/2;
			}
			
			parent.getComponent(0).setBounds(x0, y0, w0, h0);
			parent.getComponent(1).setBounds(x1, y1, w1, h1);
			
			y0+=h0+vGap;
			for(int i=2;i<ncomponents;i++)
			{
				if(i==ncomponents-1)
				{
					y0-=h0+vGap;
					if(h0>h1)
					{
						y1=y0+(h0-h1)/2;
					}else{
						y1=y0-(h1-h0)/2;
					}
					parent.getComponent(i).setBounds(x1, y1, w1, h1);
				}else{
					parent.getComponent(i).setBounds(x0, y0, w0, h0);
					y0+=h0+vGap;
				}
			}
		}
	}
}
