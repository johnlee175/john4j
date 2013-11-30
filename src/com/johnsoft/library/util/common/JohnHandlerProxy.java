package com.johnsoft.library.util.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JohnHandlerProxy implements InvocationHandler
{
	  private Class<?> clazz;
	  
	  public JohnHandlerProxy(){
		}
	  
	  public JohnHandlerProxy(Class<?> clazz)
	  {
	  	this.clazz=clazz;
	  }
	  
	  public JohnHandlerProxy setTarget(Object obj)
	  {
	  	this.clazz=obj.getClass();
	  	return this;
	  }
	  
	  public JohnHandlerProxy setTarget(Class<?> clazz)
	  {
	  	this.clazz=clazz;
	  	return this;
	  }

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable
		{
			beforeExecute();
			Object obj=method.invoke(clazz.newInstance(), args);
			afterExecute();
			return obj;
		}
	  
		public Object getInstance()
		{
			ClassLoader cl=this.getClass().getClassLoader();
			Class<?>[] clazzs=clazz.getInterfaces();
			return Proxy.newProxyInstance(cl, clazzs, this);
		}
		
		@SuppressWarnings("unchecked")
		public static <T> T getInstance(Class<T> t)
		{
			ClassLoader cl=t.getClassLoader();
			Class<?>[] clazzs=t.getInterfaces();
			return (T)Proxy.newProxyInstance(cl, clazzs, new JohnHandlerProxy(t));
		}
		
		public void beforeExecute(){
		}
		
		public void afterExecute(){
		}
}
