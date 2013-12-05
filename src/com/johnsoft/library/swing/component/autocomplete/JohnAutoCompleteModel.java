package com.johnsoft.library.swing.component.autocomplete;

import java.util.Collection;

/**
 * 自动提示数据模型.一般采纳3个字段或列表实现.<br/>
 * 比如:希望在输入身份证号时显示匹配的姓名,当选择某个姓名时提交到文本框中的是对应的学生ID号;<br/>
 * 这里当输入身份证号的头几位时,是从<code>getInputKeySet()</code>返回的身份证列表中迭代,做指定匹配,
 * 然后匹配的身份证号会被传入<code>getShowValue(Object)</code>方法,将返回值,即对应的姓名显示出来,当选择时,
 * 将选择项的显示值传入<code>getCommitValue(Object)</code>方法,返回值提交到文本框中;<br/>
 * 需注意的是,当希望输入姓名或身份证号的时候都去匹配时,会从getShowKeySet()的返回的姓名列表再进行一次迭代,做指定匹配
 * @author 李哲浩
 */
@SuppressWarnings("rawtypes")
public interface JohnAutoCompleteModel
{
	 /**
	  * @return 用户输入与此返回集合做迭代比较以判断是否匹配
	  */
	 public Collection getInputKeys();
	 
	 /**
	  * @return 显示到自动提示列表的所有可能值的集合
	  */
	public Collection getShowKeys();
	 
	 /**
	  * 
	  * @param key getInputKeys(),getShowKeys()返回的集合中的某一项
	  * @return 参与和用户输入做匹配的项的字符串表示形式
	  */
	 public String getKeyString(Object key);
	 
	 /**
	  * @param inputValue 用户输入的值
	  * @return 用户输入的值到显示的映射值
	  */
	 public String getShowValue(Object inputValue);
	
	 /**
	  * @param showValue 自动提示列表中选择的值
	  * @param text 当前要提交到的文本框的旧值
	  * @return 选中后提交到文本框中的值
	  */
	 public String getCommitValue(Object showValue,String text);
}
