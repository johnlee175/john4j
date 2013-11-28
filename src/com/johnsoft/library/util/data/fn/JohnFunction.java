package com.johnsoft.library.util.data.fn;

/**
 * 标记性接口.
 * 因不像c/c++一样有函数指针,或c#一样有委托事件,只能用类或接口模拟函数指针.
 * 
 * 函数指针规范:
 * 1.以接口模拟,其内有且仅有一个公开函数,命名为function,形参变量命名为param,多个形参依次数字化,如param1,param2...;
 * 2.接口命名规范:
 * 		1.标志符打头,这里是John,仅用作区别其他人写的库文件开头,也可以是JI(java interface)或JC(java class);
 *		2.紧跟O,即Output,表示返回,其后是返回类型的缩写;
 *		3.再跟I,即Input,表示传参,其后是形参类型的缩写;
 * 		4.最后以Fn结尾,表示模拟函数指针.
 * 		5.除void以外,常用缩写为三个简码(用户自定义的特定类型或一次性类型用x开头加两个简码):
 * 			1.boo->boolean,Boolean
 *   		2.str->String
 *   		3.int->int,Integer
 *   		4.flt->float,Float
 *   		5.dbl->double,Double
 *   		6.chr->char,Char
 *   		7.byt->byte,Byte
 *   		8.lng->long,Long
 *   		9.obj->Object
 * @author 李哲浩
 */
public interface JohnFunction
{
}
