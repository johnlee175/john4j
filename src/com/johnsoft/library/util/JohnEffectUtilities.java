package com.johnsoft.library.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;

import com.johnsoft.library.effect.renderer.ShadowRenderer;

public class JohnEffectUtilities
{
	private static Map<String,BufferedImage> controlImages=new HashMap<String,BufferedImage>();
	
	private JohnEffectUtilities(){
	}
	
//	public static void main(String[] args)
//	{
//		JFrame jf=new JFrame();
//		
//		JPanel jp=new JPanel();
//		jp.setBackground(Color.GREEN);
//		
//		JPanel control=new JPanel();
//		control.setBackground(Color.RED);
//		control.setPreferredSize(new Dimension(200, 100));
//		JComponent comp=JohnEffectUtilities.installFastDropShadow(control, 30, 15, 0);
//		jp.add(comp);
//		
//		JPanel control1=new JPanel();
//		control1.setBackground(Color.YELLOW);
//		control1.setPreferredSize(new Dimension(200, 100));
//		JComponent comp1=JohnEffectUtilities.installDropShadow(control1, 30, 40, 20, 20, 1.0f, Color.BLACK,false);
//		jp.add(comp1);
//		JComponent comp2=JohnEffectUtilities.installDropShadow(jp, 10, 20,40,50 ,0.8f, Color.BLUE,true);
//		jf.add(comp2);
//		jf.pack();
//		jf.setVisible(true);
//	}
	
	public static JComponent installFastDropShadow(JComponent comp,int shadowExtend,int compOffset,int shadowOffset)
	{
		return installDropShadow(comp,shadowExtend,compOffset,shadowOffset, 15, 1.0f,Color.BLACK, true);
	}
	
	public static JComponent installDropShadow(JComponent comp,int shadowExtend,int compOffset,int shadowOffset,int blurSize,float shadowOpacity,Color shadowColor,boolean isFastRendering)
	{
		return installDropShadow(comp,new Dimension(shadowExtend, shadowExtend), new Point(compOffset, compOffset), new Point(shadowOffset,shadowOffset), blurSize, shadowOpacity, shadowColor,isFastRendering);
	}
	
	public static JComponent installDropShadow
			(final JComponent comp,final Dimension shadowExtend,final Point compOffset,final Point shadowOffset,
			final int blurSize,final float shadowOpacity,final Color shadowColor,final boolean isFastRendering)
	{
		Random random=new Random();
	    final String key="installDropShadow"+random.nextLong();
	    
		final Dimension dim=comp.getPreferredSize();
		final Rectangle rect=comp.getBounds();
		final Dimension size=new Dimension(Math.max(rect.width,dim.width),Math.max(rect.height,dim.height));
		
		comp.setBounds(compOffset.x, compOffset.y, size.width, size.height);
		
		BufferedImage controlImage=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d=controlImage.createGraphics();
	    comp.paint(g2d);
	    g2d.dispose();
	    
	    controlImages.put(key, controlImage);
	    
		final JComponent shadow=new JComponent()
		{
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g)
			{
				BufferedImage controlImage=controlImages.get(key);
			    BufferedImage shadowImage =null; 
			    if (!isFastRendering) {
			    	shadowImage = createDropShadow(controlImage, blurSize, shadowColor);
                } else {
                    ShadowRenderer renderer = new ShadowRenderer(blurSize / 2, shadowOpacity, shadowColor);
                    shadowImage = renderer.createShadow(controlImage);
                }
		        Graphics2D g2 = (Graphics2D) g;
		        g2.setComposite(AlphaComposite.SrcOver.derive(shadowOpacity)); 
		        
		        if (!isFastRendering) {
	                g2.drawImage(shadowImage, shadowOffset.x-blurSize*2, shadowOffset.y-blurSize*2, shadowImage.getWidth()+shadowExtend.width, shadowImage.getHeight()+shadowExtend.height, null);
	            } else {
	            	g2.drawImage(shadowImage, shadowOffset.x-blurSize/2, shadowOffset.y-blurSize/2, shadowImage.getWidth()+shadowExtend.width, shadowImage.getHeight()+shadowExtend.height, null);
	            }
			}
		};
		
		final int offsetX=Math.max(compOffset.x,shadowOffset.x)+(blurSize/6+1)+(shadowOffset.x>compOffset.x?shadowExtend.width:compOffset.x-shadowOffset.x);
		final int offsetY=Math.max(compOffset.y,shadowOffset.y)+(blurSize/6+1)+(shadowOffset.y>compOffset.y?shadowExtend.height:compOffset.y-shadowOffset.y);
		
		shadow.add(comp);
		shadow.setPreferredSize(new Dimension(size.width+offsetX, size.height+offsetY));
		shadow.setBounds(rect.x, rect.y, size.width+offsetX, size.height+offsetY);
		
		shadow.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				comp.setBounds(compOffset.x, compOffset.y, shadow.getWidth()-offsetX, shadow.getHeight()-offsetY);
				
				BufferedImage controlImage=new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
			    Graphics2D g2d=controlImage.createGraphics();
			    comp.paint(g2d);
			    g2d.dispose();
			    
			    controlImages.put(key, controlImage);
			}
		});
		
		return shadow;
	}
	
	public static BufferedImage createDropShadow(BufferedImage image,
            int size,Color color) {
        BufferedImage shadow = new BufferedImage(
            image.getWidth() + 4 * size,
            image.getHeight() + 4 * size,
            BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = shadow.createGraphics();
        g2.drawImage(image, size * 2, size * 2, null);
        
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setPaint(color);
        g2.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());       
        
        g2.dispose();
        
        shadow = getGaussianBlurFilter(size, true).filter(shadow, null);
        shadow = getGaussianBlurFilter(size, false).filter(shadow, null);
        
        return shadow;
    }
    
    public static ConvolveOp getGaussianBlurFilter(int radius,
            boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }
        
        int size = radius * 2 + 1;
        float[] data = new float[size];
        
        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;
        
        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }
        
        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }        
        
        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }
    
}
