package com.johnsoft.library.swing.effect.tween;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;

import aurelienribon.tweenengine.TweenAccessor;

import com.sun.awt.AWTUtilities;


public class ComponentTweenAccessor implements TweenAccessor<Component>
{
	public static final int LOCATION_MOVE = 1;
    public static final int SIZE_SCALE = 2;
	public static final int COLOR_SHADE = 3;
	public static final int FONT_COLOR_SHADE = 4;
	public static final int FONT_SIZE_SCALE =5;
	public static final int WINDOW_OPACITY=6;

    @Override
    public int getValues(Component target, int tweenType, float[] returnValues) 
    {
        switch (tweenType) 
        {
        case LOCATION_MOVE:
        {
            returnValues[0] = target.getX();
            returnValues[1] = target.getY();
            return 2;
        }
        case SIZE_SCALE:
        {
        	returnValues[0] = target.getWidth();
            returnValues[1] = target.getHeight();
            return 2;
        }
        case FONT_COLOR_SHADE:
        {
        	Color color=target.getForeground();
        	returnValues[0] = color.getRed();
        	returnValues[1] = color.getGreen();
        	returnValues[2] = color.getBlue();
        	return 3;
        }
        case COLOR_SHADE:
        {
        	Color color=target.getBackground();
        	returnValues[0] = color.getRed();
        	returnValues[1] = color.getGreen();
        	returnValues[2] = color.getBlue();
        	return 3;
        }
        case FONT_SIZE_SCALE:
        {
        	returnValues[0] = target.getFont().getSize();
        	return 1;
        }
        case WINDOW_OPACITY:
        {
        	returnValues[0] = AWTUtilities.getWindowOpacity((Window)target);
        	return 1;
        }
        default: assert false; return -1;
        }
    }
    
    @Override
    public void setValues(Component target, int tweenType, float[] newValues)
    {
        switch (tweenType) 
        {
        case LOCATION_MOVE:
            target.setLocation((int)newValues[0],(int)newValues[1]);
            break;
        case SIZE_SCALE:
            target.setSize((int)newValues[0],(int)newValues[1]);
            break;
        case FONT_COLOR_SHADE:
        	target.setForeground(new Color((int)newValues[0],(int)newValues[1],(int)newValues[2]));
        	break;
        case COLOR_SHADE:
        	target.setBackground(new Color((int)newValues[0],(int)newValues[1],(int)newValues[2]));
        	break;
        case FONT_SIZE_SCALE:
        	target.setFont(target.getFont().deriveFont((float)newValues[0]));
        	break;
        case WINDOW_OPACITY:
        	AWTUtilities.setWindowOpacity((Window)target, newValues[0]);
        	break;
        default: assert false; break;
        }
    }														

}
