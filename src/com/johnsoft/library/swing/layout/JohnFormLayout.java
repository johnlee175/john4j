package com.johnsoft.library.swing.layout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

/**
 * <image src="./form_item.png"/>
 * @author 李哲浩
 */
public class JohnFormLayout implements LayoutManager
{
	private List<JohnFormItem> itemList = new ArrayList<JohnFormItem>();
	private Insets margin=new Insets(5, 5, 5, 5);
	private Insets itemBlank=new Insets(0, 5, 0, 5);
	private Container parent;
	private int midHeight;
	private Dimension tipSize;
	
	public JohnFormLayout(){
	}
	
	public JohnFormLayout(Container parent,int midHeight)
	{
		setParent(parent);
		setMidHeight(midHeight);
	}
	
	public void setMargin(int top,int left,int bottom,int right)
	{
		this.margin=new Insets(top, left, bottom, right);
	}
	
	public void setItemBlank(int hGap,int vGap,int hPadding,int vPadding)
	{
		this.itemBlank = new Insets(hGap, vGap, hPadding, vPadding);
	}
	
	public void setParent(Container parent)
	{
		this.parent=parent;
		this.parent.setLayout(this);
	}
	
	public void setMidHeight(int midHeight)
	{
		this.midHeight=midHeight;
	}
	
	public void setTipSize(int width,int height)
	{
		this.tipSize=new Dimension(width, height);
	}
	
	public void addItem(JLabel label,JTextComponent textfield,AbstractButton btn)
	{
		 if(label!=null)
		 {
			 label.setHorizontalAlignment(JLabel.RIGHT);
		 }
		 if(btn!=null&&btn.getIcon()!=null)
		 {
			 Icon icon=btn.getIcon();
			 btn.setHideActionText(true);
			 btn.setContentAreaFilled(false);
			 btn.setBorderPainted(false);
			 btn.setFocusPainted(false);
			 btn.setFocusable(false);
			 btn.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		 }
		 this.addComponent(label,textfield,btn);
	}
	
	public void addComponent(Component label,Component textfield,Component btn)
	{
		JohnFormItem item=new JohnFormItem();
		item.label=label;
		item.textfield=textfield;
		item.btn=btn;
		item.setBlank(itemBlank.top, itemBlank.left, itemBlank.bottom, itemBlank.right);
		this.add(item);
	}
	
	public void add(JohnFormItem item)
	{
		itemList.add(item);
		addToParent(item);
	}
	
	public JohnFormItem getComponent(int index)
	{
		return itemList.get(index);
	}
	
	public List<JohnFormItem> getComponents()
	{
		return itemList;
	}
	
	public int getComponentsCount()
	{
		return itemList.size();
	}
	
	public void removeAll()
	{
		itemList.clear();
		if(parent!=null)
		{
			parent.removeAll();
		}
	}
	
	public void remove(int index)
	{
		JohnFormItem item=itemList.remove(index);
		removeFromParent(item);
	}
	
	private void addToParent(JohnFormItem item)
	{
		if(parent!=null)
		{
			if(item.header!=null)
				parent.add(item.header);
			if(item.label!=null)
				parent.add(item.label);
			if(item.textfield!=null)
				parent.add(item.textfield);
			if(item.btn!=null)
				parent.add(item.btn);
			if(item.validate!=null)
				parent.add(item.validate);
			if(item.commit!=null)
				parent.add(item.commit);
			if(item.tip!=null)
				parent.add(item.tip);
		}
	}
	
	private void removeFromParent(JohnFormItem item)
	{
		if(parent!=null)
		{
			if(item.header!=null)
				parent.remove(item.header);
			if(item.label!=null)
				parent.remove(item.label);
			if(item.textfield!=null)
				parent.remove(item.textfield);
			if(item.btn!=null)
				parent.remove(item.btn);
			if(item.validate!=null)
				parent.remove(item.validate);
			if(item.commit!=null)
				parent.remove(item.commit);
			if(item.tip!=null)
				parent.remove(item.tip);
		}
	}
	
	public void setTipZOrder()
	{
		if(parent!=null)
		{
			for(JohnFormItem item:itemList)
			{
				if(item.tip!=null)
				{
					 parent.setComponentZOrder(item.tip, 0);
				}
			}
		}
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
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container c)
	{
		synchronized (c.getTreeLock())
		{
			int w=0;
			int h=0;
			for(int i=0;i<itemList.size();i++)
			{
				JohnFormItem item=itemList.get(i);
				int vblank=(item.vPadding<0?0:(item.vPadding)*2+(item.vGap<0?0:item.vGap)*2);
				int headerH=item.header==null?0:item.header.getPreferredSize().height;
				int commitH=item.commit==null?0:item.commit.getPreferredSize().height;
				int midH=0;
				if(midHeight<=0)
				{
					int labelH=item.label==null?0:item.label.getPreferredSize().height;
					int textfieldH=item.textfield==null?0:item.textfield.getPreferredSize().height;
					int btnH=item.btn==null?0:item.btn.getPreferredSize().height;
					int validateH=item.validate==null?0:item.validate.getPreferredSize().height;
					midH=Math.max(midH, labelH);
					midH=Math.max(midH, textfieldH);
					midH=Math.max(midH, btnH);
					midH=Math.max(midH, validateH);
				}else{
					midH=midHeight;
				}
				h+=vblank+headerH+commitH+midH;
				int hPadding=(item.vPadding<0?0:item.vPadding)*2;
				int headerW=item.header==null?0:item.header.getPreferredSize().width;
				int commitW=item.commit==null?0:item.commit.getPreferredSize().width;
				int midW=(item.hGap<0?0:item.hGap)*3;
				midW+=item.label==null?0:item.label.getPreferredSize().width;
				midW+=item.textfield==null?0:item.textfield.getPreferredSize().width;
				midW+=item.btn==null?0:item.btn.getPreferredSize().width;
				midW+=item.validate==null?0:item.validate.getPreferredSize().width;
				w=Math.max(w, headerW+hPadding);
				w=Math.max(w, commitW+hPadding);
				w=Math.max(w, midW+hPadding);
			}
			return new Dimension(w+margin.left+margin.right, h+margin.top+margin.bottom);
		}
	}

	@Override
	public void layoutContainer(Container c)
	{
		synchronized (c.getTreeLock())
		{
			int cw=c.getWidth();
			int x=margin.left;
			int y=0;
			int w=cw-margin.left-margin.right;
			int h=0;
			for(int i=0;i<itemList.size();i++)
			{
				JohnFormItem item=itemList.get(i);
				int vblank=(item.vPadding<0?0:(item.vPadding)*2+(item.vGap<0?0:item.vGap)*2);
				int headerH=item.header==null?0:item.header.getPreferredSize().height;
				int commitH=item.commit==null?0:item.commit.getPreferredSize().height;
				int midH=0;
				if(midHeight<=0)
				{
					int labelH=item.label==null?0:item.label.getPreferredSize().height;
					int textfieldH=item.textfield==null?0:item.textfield.getPreferredSize().height;
					int btnH=item.btn==null?0:item.btn.getPreferredSize().height;
					int validateH=item.validate==null?0:item.validate.getPreferredSize().height;
					midH=Math.max(midH, labelH);
					midH=Math.max(midH, textfieldH);
					midH=Math.max(midH, btnH);
					midH=Math.max(midH, validateH);
				}else{
					midH=midHeight;
				}
				y=margin.top+i*h;
				h=vblank+headerH+commitH+midH;
				layoutFormItem(item,x,y,w,h);
			}
		}
	}
	
	protected void layoutFormItem(JohnFormItem item,int x,int y,int w,int h)
	{
		int hPadding=item.hPadding<0?0:item.hPadding;
		int vPadding=item.vPadding<0?0:item.vPadding;
		int hGap=item.hGap<0?0:item.hGap;
		int vGap=item.vGap<0?0:item.vGap;
		int headerH=item.header==null?0:item.header.getPreferredSize().height;
		int commitH=item.commit==null?0:item.commit.getPreferredSize().height;
		if(item.header!=null)
		{
			item.header.setBounds(x+hPadding, y+vPadding, w-2*hPadding, headerH);
		}
		if(item.commit!=null)
		{
			item.commit.setBounds(x+hPadding, y+h-vPadding-commitH, w-2*hPadding, commitH);
		}
		int labelW=item.label==null?0:item.label.getPreferredSize().width;
		int btnW=item.btn==null?0:item.btn.getPreferredSize().width;
		int validateW=item.validate==null?0:item.validate.getPreferredSize().width;
		int textfieldW=item.textfield==null?0:(w-2*hPadding-3*hGap-labelW-btnW-validateW);
		textfieldW=textfieldW<0?0:textfieldW;
		int midH=midHeight>0?midHeight:(y+h-2*vPadding-2*vGap-headerH-commitH);
		int midY=y+vPadding+headerH+vGap;
		if(item.label!=null)
		{
			int labelH=item.label.getPreferredSize().height;
			item.label.setBounds(x+hPadding, midHeight>0?midY:(midY+(midH-labelH)/2), labelW, midHeight>0?midHeight:labelH);
		}
		if(item.textfield!=null)
		{
			int textfieldH=item.textfield.getPreferredSize().height;
			item.textfield.setBounds(x+hPadding+labelW+hGap, midHeight>0?midY:(midY+(midH-textfieldH)/2), textfieldW, midHeight>0?midHeight:textfieldH);
		}
		if(item.btn!=null)
		{
			int btnH=item.btn.getPreferredSize().height;
			item.btn.setBounds(x+hPadding+labelW+textfieldW+hGap*2, midHeight>0?midY:(midY+(midH-btnH)/2), btnW, midHeight>0?midHeight:btnH);
		}
		if(item.validate!=null)
		{
			int validateH=item.validate.getPreferredSize().height;
			item.validate.setBounds(x+hPadding+labelW+textfieldW+btnW+hGap*3, midHeight>0?midY:(midY+(midH-validateH)/2), validateW, midHeight>0?midHeight:validateH);
		}
		if(item.tip!=null)
		{
			int tipW=tipSize==null?item.tip.getWidth():tipSize.width;
			int tipH=tipSize==null?item.tip.getHeight():tipSize.height;
			int tipX=hPadding+labelW+textfieldW+hGap*2-tipW;
			int tipY=midY-vGap;
			item.tip.setBounds(tipX, tipY, tipW, tipH);
		}
	}

}
