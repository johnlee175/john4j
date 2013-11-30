package com.johnsoft.library.swing.component.textgrouppane;

import java.util.EventListener;

/**
 * 文本组件组面板监听器
 * @author 李哲浩
 */
public interface JohnTextGroupListener extends EventListener
{
	public void stateChanged(JohnTextGroupEvent evt);
}
