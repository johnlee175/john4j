package com.johnsoft.library.util.data.vcm;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * 值变监听对象,这是关注数据的产物,当值变时应由数据本身通知并自动更新所有使用该数据的视图,而不是数据更新后,每个视图做重置操作
 * @author 李哲浩
 */
public class CVCMObject<T> implements IValueChangedMonitor
{
	private HashSet<CVCLItem> set=new HashSet<CVCLItem>();//值变时要调用的对象和方法的映射表
	private T data;//封装的实际数据
	
	public CVCMObject(T t)
	{
		data=t;
	}
	
	/**获取数据*/
	public final T get()
	{
		return data;
	}
	
	/**设置数据*/
	public final void set(T t)
	{
		T old=data;
		data = t;
		fireValueChanged(old,t);
	}
	
	/**建立与数据变更时调用的对象和方法的绑定链接,两个参数不能为null;
	 * 方法名所对应实际方法必须存在,且参数应为泛型所指类型,参数个数应为一个,如果必须通知不同原型的函数,请参考invokeCallback的说明;
	 * @see #invokeCallback(Object, String, Object, Object)*/
	public final void connect(Object object,String methodName)
	{
		set.add(new CVCLItem(object, methodName));
	}
	
	/**取消与数据变更时的调用对象和方法的绑定链接,两个参数不能为null,且对象和方法必须存在*/
	public final void disconnect(Object object,String methodName)
	{
		set.remove(new CVCLItem(object, methodName));
	}
	
	/**取消与数据变更时的调用对象绑定链接,将消除该对象与此类的所有方法绑定,如果object为null,将清空与任何对象的值变绑定,如果找不到对象,将不做任何处理*/
	public final void disconnect(Object object)
	{
		if(object==null)
		{
			set.clear();
		}else{
			for(CVCLItem item:set)
			{
				if(item.object.equals(object))
				{
					set.remove(item);
				}
			}
		}
	}
	
	//实际通知绑定对象调用相对应的方法
	private void fireValueChanged(T oldData,T newData)
	{
		for(CVCLItem obj:set)
		{
			invokeCallback(obj.object, obj.methodName, oldData, newData);
		}
	}
	
	/**
	 * 虽然参数中有传入数据变化前后的值,但唤醒的obj对象的methodName方法中仅接受一个泛型所指类型的参数;
	 * 比如此泛型类T为String,methodName为"setString",被调用将形如setString(String)的方法,而非setString(String,String);
	 * 可覆写此方法,如果methodName必须传入两个参数或更多.
	 * @param obj 要通知的相关对象
	 * @param methodName 值变时调用的相关回调方法名
	 */
	protected void invokeCallback(Object obj, String methodName, T oldData, T newData)
	{
		try
		{
			Method methods=obj.getClass().getMethod(methodName,data.getClass());
			methods.invoke(obj,data);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//封装成对保存对象及其方法
	private class CVCLItem
	{
		public Object object;
		public String methodName;
		public CVCLItem(Object object, String methodName)
		{
			this.object = object;
			this.methodName = methodName;
		}
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((methodName == null) ? 0 : methodName.hashCode());
			result = prime * result
					+ ((object == null) ? 0 : object.hashCode());
			return result;
		}
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CVCLItem other = (CVCLItem) obj;
			if (methodName == null)
			{
				if (other.methodName != null)
					return false;
			} else if (!methodName.equals(other.methodName))
				return false;
			if (object == null)
			{
				if (other.object != null)
					return false;
			} else if (!object.equals(other.object))
				return false;
			return true;
		}
	}
}
