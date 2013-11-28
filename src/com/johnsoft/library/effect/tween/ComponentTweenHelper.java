package com.johnsoft.library.effect.tween;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


/**
 * 封装动画(定时更新),40毫秒更新0.005f的duration值
 * @author 李哲浩
 */
public class ComponentTweenHelper implements ActionListener
{
	public static final int PERIOD=40;
	public static final float PER_DURATION=0.005f;
	private TweenManager manager;
	private Timer timer;
	private BaseTween<?> baseTween;
	
	public ComponentTweenHelper()
	{
		Tween.registerAccessor(Component.class, new ComponentTweenAccessor());
		manager = new TweenManager();
		timer=new Timer(PERIOD, this);
	}
	
	@Override
	public final void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof Timer)
		{
			if(baseTween!=null&&baseTween.isFinished())
			{
				timer.stop();
				return;
			}
			manager.update(PER_DURATION);
		}
	}
	
	public void start(BaseTween<?> baseTween)
	{
		this.baseTween=baseTween;
		baseTween.start(manager);
		timer.start();
	}
	
	public void dispose()
	{
		manager.killAll();
		timer.stop();
		baseTween=null;
	}
	
}
