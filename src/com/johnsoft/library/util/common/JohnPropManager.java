package com.johnsoft.library.util.common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * *.properties文件管理重要辅助类	<br />注:本类绝大部分方法线程安全,如果方法文档没有说明可以传入null参数,则传入null皆会抛出异常.
 * 管理properties文件强烈推荐使用此类.
 * @author 李哲浩
 */
public final class JohnPropManager
{
	private Hashtable<String,String> aliasMap;//别名与文件路径的映射
	private Hashtable<String,Properties> propMap;//文件路径与属性文件对象的映射
	private Hashtable<String,List<Object>> invokeMap;//作用域别名下某属性与其在值变时通知对象的映射,List中依次循环放入对象和对象的方法
	private byte[] propLock;//锁定aliasMap和propMap的存取操作
	private byte[] invokeLock;//锁定invokeMap的存取操作
	private int flags;
	/** 标志位,指示当作为参数的作用域别名冲突时抛出异常*/
	public static final int FLAG_THROW_ON_ALIAS_CONFLICT=1<<1;
	/** 标志位,指示启用属性更改回调通知机制*/
	public static final int FLAG_ENABLE_FIRE_PROP_CHANGE=1<<2;
	
	
	private JohnPropManager()
	{
		propLock=new byte[0];
		invokeLock=new byte[0];
		aliasMap=new Hashtable<String,String>();
		propMap=new Hashtable<String,Properties>();
		invokeMap=new Hashtable<String, List<Object>>();
	}
	
	private static class JohnPropHandler
	{
		public static final JohnPropManager instance=new JohnPropManager();
	}
	
	private static class GlobalProperties
	{
		public transient static final Hashtable<String,String> processSharedGlobalVariable=new Hashtable<String,String>();
	}
	
	/**
	 * @return JohnPropHelper单例
	 */
	public static JohnPropManager getSharedInstance()
	{
		return JohnPropHandler.instance;
	}
	
	/**
	 * 创建propterties文件,并按照别名纳入管理
	 * @param path properties文件路径
	 * @param alias 作用域别名
	 * @return 如果创建成功并纳入管理返回true,如果文件已经加载过,将返回false,如果文件创建出错,返回null
	 */
	public Boolean createProperties(String path,String alias)
	{
		String oldPath;
		synchronized (propLock)
		{
			oldPath = aliasMap.get(alias);
		}
		if(oldPath!=null)
		{
			if(oldPath.equals(path))
			{
				return false;
			}else{
				if((flags & FLAG_THROW_ON_ALIAS_CONFLICT)!=0)
				{
					throw new IllegalArgumentException("The argument corresponding to the parameter named alias is name conflict!");
				}
			}
		}
		Properties prop=new Properties();
		File file=new File(path);
		if(file.isDirectory()||!path.endsWith(".properties")) return null;
		try
		{
			file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		synchronized (propLock)
		{
			aliasMap.put(alias, path);
			propMap.put(path, prop);
		}
		return true;
	}
	
	/**
	 * 载入指定目录下的properties文件,并指定作用域别名
	 * @param path properties文件路径
	 * @param alias 作用域别名
	 * @return 如果加载完成将返回true;如果已经加载过,将返回false,此时若仍旧需要加载该文件可调用reloadProperties;如果加载过程发生错误,将返回null
	 */
	public Boolean loadProperties(String path,String alias)
	{
		String oldPath;
		synchronized (propLock)
		{
			oldPath = aliasMap.get(alias);
		}
		if(oldPath!=null)
		{
			if(oldPath.equals(path))
			{
				return false;
			}else{
				if((flags & FLAG_THROW_ON_ALIAS_CONFLICT)!=0)
				{
					throw new IllegalArgumentException("The argument corresponding to the parameter named alias is name conflict!");
				}
			}
		}
		Properties prop=new Properties();
		try
		{
			prop.load(new FileInputStream(path));
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		synchronized (propLock)
		{
			aliasMap.put(alias, path);
			propMap.put(path, prop);
		}
		return true;
	}
	
	/**
	 * 重新加载指定作用域别名对应的属性文件,当确定该属性文件得到更新但未接到通知时
	 * @param alias 作用域别名
	 * @return 如果重新加载成功将返回true;如果重新加载过程中发生错误,将返回false,如果没有抛出异常应作为别名不存在处理 
	 */
	public boolean reloadProperties(String alias)
	{
		String path;
		Properties prop;
		try
		{
			synchronized (propLock)
			{
				path = aliasMap.get(alias);
				prop = propMap.get(path);
			}
			prop.load(new FileInputStream(path));
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		synchronized (propLock)
		{
			aliasMap.put(alias, path);
			propMap.put(path, prop);
		}
		return true;
	}
	
	/**
	 * 存储指定作用域别名对应的properties文件的更改
	 * @param alias 作用域别名
	 * @return 如果保存成功将返回true;如果保存过程发生错误,将返回false 
	 */
	public boolean storeProperties(String alias)
	{
		String path;
		Properties prop;
		try
		{
			synchronized (propLock)
			{
				path = aliasMap.get(alias);
				prop = propMap.get(path);
			}
			prop.store(new FileOutputStream(path), null);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	/**
	 * 在全局范围内获取键所对应值,全局范围指进程范围内共享,但是无法序列化或保存值的更改,进程结束所有的键值对失效
	 */
	public String getProperty(String key)
	{
		synchronized (propLock)
		{
			return GlobalProperties.processSharedGlobalVariable.get(key);
		}
	}
	
	/**
	 * 在全局范围内获取键所对应值,如果没有该键或值为null,将返回事前设置的默认值defaultValue,全局范围指进程范围内共享,但是无法序列化或保存值的更改,进程结束所有的键值对失效
	 */
	public String getPropertyWithDefault(String key,String defaultValue)
	{
		String value;
		synchronized (propLock)
		{
			value = GlobalProperties.processSharedGlobalVariable.get(key);
		}
		return value==null?defaultValue:value;
	}
	
	/**
	 * 在全局范围内设置键所对应值,全局范围指进程范围内共享,但是无法序列化或保存值的更改,进程结束所有的键值对失效
	 * @return 在通知变更过程中发生异常或未启动异常通知功能将返回false,否则返回true
	 */
	public boolean setProperty(String key,String value)
	{
		String oldValue;
		synchronized (propLock)
		{
			oldValue = GlobalProperties.processSharedGlobalVariable.put(key, value);
		}
		if((flags&FLAG_ENABLE_FIRE_PROP_CHANGE)!=0)
			return firePropChangeCallback(null, key, oldValue, value);
		return false;
	}
	
	/**
	 * 在作用域别名内获取键所对应值
	 */
	public String getProperty(String alias,String key)
	{
		synchronized (propLock)
		{
			return propMap.get(aliasMap.get(alias)).getProperty(key);
		}
	}
	
	/**
	 * 在作用域别名内获取键所对应值,如果没有该键或值为null,将返回事前设置的默认值defaultValue
	 */
	public String getPropertyWithDefault(String alias,String key,String defaultValue)
	{
		synchronized (propLock)
		{
			return propMap.get(aliasMap.get(alias)).getProperty(key, defaultValue);
		}
	}
	
	/**
	 * 在作用域别名内设置键所对应值
	 *  @return 在通知变更过程中发生异常或未启动异常通知功能将返回false,否则返回true
	 */
	public boolean setProperty(String alias, String key, String value)
	{
		String oldValue;
		synchronized (propLock)
		{
			oldValue = (String) propMap.get(aliasMap.get(alias)).setProperty(key, value);
		}
		if((flags&FLAG_ENABLE_FIRE_PROP_CHANGE)!=0)
			return firePropChangeCallback(alias, key, oldValue, value);
		return false;
	}
	
	/** 设置标志,取值有FLAG_THROW_ON_ALIAS_CONFLICT,FLAG_ENABLE_FIRE_PROP_CHANGE,可按位或*/
	public void setFlags(int flags)
	{
		this.flags=flags;
	}
	
	/**测试如果包含此作用域别名返回true*/
	public boolean contains(String alias)
	{
		synchronized (propLock)
		{
			return aliasMap.containsKey(alias);
		}
	}
	
	/**获取当前载入的所有别名,请尽可能在多线程环境下避免非只读的使用此方法*/
	public Set<String> getAliases()
	{
		synchronized (propLock)
		{
			return aliasMap.keySet();
		}
	}
	
	/**获取当前载入的所有属性文件对象,请尽可能在多线程环境下避免非只读的使用此方法*/
	public Collection<Properties> getAllProperties()
	{
		synchronized (propLock)
		{
			return propMap.values();
		}
	}
	
	/**
	 * 返回特定作用域别名下的特定键的所有监听器,请尽可能在多线程环境下避免非只读的使用此方法
	 * @param alias 作用域别名,如果为null,获取全局范围的监听器
	 * @param key 具体属性键,如果为null,获取监听所有范围的监听器
	 * @return 返回的列表奇数项是监听器对象,偶数项是监听器对象的回调方法,即第一个对象是监听器对象,第二个对象是第一个对象的Method对象
	 */
	public List<Object> getPropChangeCallbacks(String alias,String key)
	{
		synchronized (invokeLock)
		{
			return invokeMap.get(alias + "->" + key);
		}
	}
	
	/**
	 * 监听实现,当调用setProperty设置作用域别名为alias的key属性时,回调obj的methodName,通知这种更改.
	 * @param alias 要监听的作用域别名,如果为null,将全局范围视为作用域别名,全局范围指进程范围内共享,但是无法序列化或保存值的更改,进程结束所有的键值对失效
	 * @param key 要监听的具体属性键,如果为null,将监听该作用域别名的所有属性变化
	 * @param object 当属性变化时要通知的对象,如果为null,将抛出IllegalArgumentException
	 * @param methodName 通过回调对象的 该方法表达应对这次变更采取的措施.如果为null,将抛出IllegalArgumentException,除此该方法还必须满足下列条件:
	 * <br/>1.方法必须是public;
	 * <br/>2.如果非要该方法返回非void的值,由于方法的返回值将被丢弃,等同于无返回值;
	 * <br/>3.方法必须接受4个String类型的参数,回调时将依次传入作用域别名,属性键名,属性变更前旧值,属性变更后新值的字符串形式.
	 * @return 如果为true则添加成功,否则添加过程出现错误
	 */
	public boolean addPropChangeCallback(String alias,String key,Object object,String methodName)
	{
		if(object==null||methodName==null||methodName.isEmpty())
		{
			throw new IllegalArgumentException("The target object and methodName of the object cannot be null!");
		}
		try
		{
			Method method=object.getClass().getMethod(methodName, String.class,String.class,String.class,String.class);
			List<Object> list=null;
			synchronized (invokeLock)
			{
				list = invokeMap.get(alias + "->" + key);
			}
			if(list==null) list=new ArrayList<Object>();
			list.add(object);
			list.add(method);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * 移除监听,之后当调用setProperty设置作用域别名为alias的key属性时,不再调用object的methodName方法.
	 * 如果object和methodName都为null,alias的key属性将不再被任何对象监听
	 * @param alias 要移除监听该作用域别名的对象,如果为null,将移除监听全局范围属性变化的对象,不会移除监听特定作用域别名的监听器
	 * @param key 要移除监听该属性键的对象,如果为null,将移除监听该作用域别名所有属性变化的对象,不会移除监听该作用域别名特定属性的监听器
	 * @param object 要移除的具体监听对象,将在该作用域别名下的监听该属性键的监听器范围内查找该对象,不会移除该对象对其他作用域别名或属性的监听,
	 * 				     如果为null,将移除该作用域别名下监听该属性键的任何对象的methodName监听方法,
	 * 				     比如两个对象在监听该属性时将调用各自的同名回调方法,那么两个方法都不会再被调用
	 * @param methodName 要移除的具体方法.如果为null,将移除该作用域别名下监听该属性键的指定对象的所有监听方法,
	 * 					     比如一个对象对某种属性注册了两个回调方法,那么两个方法都不会再被调用
	 * @return 如果为true则确实移除了匹配监听器,否则可能没有匹配监听器
	 */
	public boolean removePropChangeCallback(String alias,String key,Object object,String methodName)
	{
		List<Object> list=null;
		synchronized(invokeLock)
		{
			list=invokeMap.get(alias+"->"+key);
		}
		boolean hadRemoved=false;
		if(list!=null&&!list.isEmpty())
		{
			for(int i=list.size()-2;i>=0;i-=2)
			{
				boolean objEq=true,mnEq=true;
				if(object!=null)
				{
					objEq=list.get(i)==object;
				}
				if(methodName!=null)
				{
					mnEq=((Method)list.get(i+1)).getName().equals(methodName);
				}
				if(objEq&&mnEq)
				{
					 list.remove(i+1);
					 list.remove(i);
					 hadRemoved=true;
				}
			}
		}
		return hadRemoved;
	}
	
	/**
	 * 报告绑定属性的改变,回调监听方法;
	 * 先触发监听该作用域别名的该属性键的监听器,再触发监听该作用域别名所有属性的监听器;
	 * 按照添加监听的逆序触发,即最后添加的监听器最先触发.
	 * @param alias 作用域别名
	 * @param key 具体属性键
	 * @param oldValue 该属性键的旧值
	 * @param newValue 该属性键的新值
	 * @return 如果在回调过程中发生异常将返回false,否则在调用所有回调后返回true
	 */
	public boolean firePropChangeCallback(String alias,String key,String oldValue,String newValue)
	{
		String invokeSpecialKey=alias+"->"+key;
		String invokeGeneralKey=alias+"->"+null;
		List<Object> specialList=null;
		List<Object> generalList=null;
		synchronized (invokeLock)
		{
			specialList = invokeMap.get(invokeSpecialKey);
			generalList = invokeMap.get(invokeGeneralKey);
		}
		try
		{
			if(specialList!=null&&!specialList.isEmpty())
			{
				int s_size=specialList.size();
				for (int i = s_size - 2; i >= 0; i -= 2)
				{
					((Method) specialList.get(i + 1)).invoke(specialList.get(i),
							alias, key, oldValue, newValue);
				}
			}
			if(generalList!=null&&!generalList.isEmpty())
			{
				int g_size=generalList.size();
				for (int i = g_size - 1; i >= 0; i -= 2)
				{
					((Method) generalList.get(i + 1)).invoke(generalList.get(i),
							alias, key, oldValue, newValue);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
