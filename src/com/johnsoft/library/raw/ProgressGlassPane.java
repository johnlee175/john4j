package com.johnsoft.library.raw;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;


public class ProgressGlassPane extends JComponent {
 
    private static final long serialVersionUID = 1L;

	private ImageIcon image = new ImageIcon("images/loading.gif");
    
    private Timer timer;
    
    /** Creates a new instance of ProgressGlassPane */
    public ProgressGlassPane() {
        // blocks all user input
        addMouseListener(new MouseAdapter() { });
        addMouseMotionListener(new MouseMotionAdapter() { });
        addKeyListener(new KeyAdapter() { });
        
        setFocusTraversalKeysEnabled(false);
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
                requestFocusInWindow();
            }
        });
        
        setBackground(Color.WHITE);
        setFont(new Font("Default", Font.BOLD, 16));
    }
    
    public void showGlass(){
    	
    	if(timer == null){
    		createTimer();
    	}
    	timer.start();
    	
    	this.setVisible(true);
    }
    
    public void hideGlass(){
    	
    	if(timer != null){
    		timer.stop();
    	}
    	
    	this.setVisible(false);
    }
    
    public void createTimer(){
    	
    	timer=new Timer(100, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		});
    	
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // enables anti-aliasing
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle clip = g.getClipBounds();

        AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
        g2.setComposite(alpha);
        
        // fills the background
        g2.setColor(getBackground());
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
 
        int w = image.getIconWidth();
        int h = image.getIconWidth();
        
        int x = (int) (getWidth()  / 2 - w / 2);
        int y = (int) (getHeight()  / 2 - h / 2);
        
        g2.drawImage(image.getImage(), x, y, null);

    }
}