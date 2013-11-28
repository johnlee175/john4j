package com.johnsoft.library.component;

import java.awt.Dimension;

import javax.swing.JDialog;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.equations.Elastic;

import com.johnsoft.library.effect.tween.ComponentTweenAccessor;
import com.johnsoft.library.effect.tween.ComponentTweenHelper;
import com.johnsoft.library.util.JohnSwingUtilities;

public class JohnCornerTip
{
	protected ComponentTweenHelper helper=new ComponentTweenHelper();
	
	/**
	 * 应覆写此方法提供自定义弹窗
	 * @return 非null的弹窗
	 */
	protected JDialog createDialog()
	{
		JDialog dialog=new JDialog();
		return dialog;
	}
	
	/**
	 * 右下角显示弹窗提示
	 * @param millsecd 弹窗显示时间
	 * @param width 弹窗最后宽度,如果为0,则采用默认宽度,由createDialog定
	 * @param height 弹窗最后高度,如果为0,则采用默认高度,由createDialog定
	 */
	public void show(long millsecd,int width,int height)
	{
		final JDialog dialog=createDialog();
		float duration=(millsecd*ComponentTweenHelper.PER_DURATION)/ComponentTweenHelper.PERIOD;
		Dimension size=JohnSwingUtilities.getScreenSize();
		int taskBarH=JohnSwingUtilities.getTaskBarHeight();
		boolean elastic=true;
		if(width<=0&&height<=0) elastic=false;
		if(width<=0) width=dialog.getWidth();
		if(height<=0) height=dialog.getHeight();
		dialog.setLocation(size.width-dialog.getWidth(), size.height+dialog.getHeight());
		dialog.setVisible(true);
		Timeline tl1=Timeline.createParallel()
				.push(Tween.to(dialog, ComponentTweenAccessor.LOCATION_MOVE, duration).target(size.width-width-10, size.height-taskBarH-height-20).ease(elastic?TweenEquations.easeOutElastic:TweenEquations.easeOutBounce))
				.push(Tween.to(dialog, ComponentTweenAccessor.SIZE_SCALE, duration).target(width, height).ease(Elastic.OUT));
		Timeline tl2=Timeline.createSequence()
				.push(tl1)
				.push(Tween.to(dialog, ComponentTweenAccessor.WINDOW_OPACITY, 0.2f).target(0))
				.push(Tween.call(new TweenCallback()
				{
					public void onEvent(int i, BaseTween<?> bt)
					{
						dialog.dispose();
					}
				}));
	    helper.start(tl2);
	}
	
}
