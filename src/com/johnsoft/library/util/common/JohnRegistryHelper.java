package com.johnsoft.library.util.common;

import java.util.prefs.Preferences;
/**
 * jdk实现的不好，请用jRegistry或JohnRegistryUtil来操作注册表
 * @author Administrator
 *
 */
public class JohnRegistryHelper
{
	/**
	 * 读HKEY_LOCAL_MACHINE\SOFTWARE\Javasoft\Prefs\目录下的所有注册变量 path;没有则创建
	 */
	public static void readAllReg(String path)
	{
	  Preferences prefsdemo=Preferences.systemRoot().node(path);
	  try{
		   String[] regkeys=prefsdemo.keys();
		   for(String key:regkeys)
		   {
		    String value=prefsdemo.get(key,null);
		    System.out.println(value);
		   }
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	 }
	/**
	 * 根据名称读注册表
	 */
	public static String readRegByName(String regName,String path)
	{
	  Preferences prefsdemo=Preferences.systemRoot().node(path);
	  String returns=null;
	  try{
	  	String[] regkeys=prefsdemo.keys();
		   for(String key:regkeys)
		   {
		    if(regName.equals(prefsdemo.get(key,null)))
		    {
		    	returns=prefsdemo.get(key,null);
		    }
		   }
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	  return returns; 
	 }
	/**
	 * 写入注册表HKEY_LOCAL_MACHINE\SOFTWARE\Javasoft\Prefs\指定的path下
	 */
	public static void writeReg1(String name,String value,String path){
	  Preferences prefsdemo =Preferences.systemRoot().node(path);
	  try{
	   prefsdemo.put(name,value);
	  }catch(Exception e){
	   
	  }
	 }
	/**
	 * 写入注册表HKEY_LOCAL_MACHINE\SOFTWARE\Javasoft\Prefs\按类的包名创建节点并写入
	 */
	public static void writeReg2(String name,String value,Class<?> clazz){
	  Preferences prefsdemo =Preferences.systemNodeForPackage(clazz);
	  try{
	   prefsdemo.put(name,value);
	  }catch(Exception e){
	   
	  }
	 }
	/**
	 * 删除所有注册项
	 */
	public static void delAllReg(String path){
	  Preferences prefsdemo =Preferences.systemRoot().node(path);
	  try{
	   prefsdemo.removeNode();
	  }catch(Exception e){
	   
	  }
	 }
	/**
	 * 删除单个注册项
	 */
	public static void delRegByName(String name,String path){
	  Preferences prefsdemo =Preferences.systemRoot().node(path);
	  try{
	   prefsdemo.remove(name);
	  }catch(Exception e){
	   
	  }
	 }

}
