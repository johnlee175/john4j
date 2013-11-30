package com.johnsoft.library.swing.component.datechooser;


/**
 * 日历控件监听器的适配器,请尽可能使用该类,以防在JohnDateListener添加新的处理函数时造成不必要的大面积修改
 * @author 李哲浩
 */
public class JohnDateAdapter implements JohnDateListener
{
	@Override
	public void chooserHide(JohnDateEvent event){	
	}
	@Override
	public void chooserShow(JohnDateEvent event){
	}
}
