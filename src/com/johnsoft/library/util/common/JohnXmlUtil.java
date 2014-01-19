package com.johnsoft.library.util.common;


import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * XML文件的辅助操作类
 * @author 李哲浩
 */
public class JohnXmlUtil
{
	/**
	 * 读取文档
	 * @param is XML文件输入流
	 * @param obj 如果不为null,并且该数组的长度不为0,将在第一个元素上填充Document对象,第二个元素上填充root元素
	 * @param childUnderRoot 如果不为null,将做清空操作,然后填充所有root节点的子节点集合
	 * @return 成功返回true,失败返回false
	 */
	@SuppressWarnings("unchecked")
	public static boolean read(InputStream is, Object[] obj, List<Element> childUnderRoot)
	{
		try
		{
			SAXReader reader=new SAXReader();
			Document doc=reader.read(is);
			Element root=doc.getRootElement();
			if(obj!=null)
			{
				if(obj.length==1)
				{
					obj[0]=doc;
				}
				else if(obj.length>1)
				{
					obj[0]=doc;
					obj[1]=root;
				}
			}
			if(childUnderRoot!=null)
			{
				childUnderRoot.clear();
				childUnderRoot.addAll(root.elements());
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 读取文档
	 * @param fileName XML文件路径名
	 * @param obj 如果不为null,并且该数组的长度不为0,将在第一个元素上填充Document对象,第二个元素上填充root元素
	 * @param childUnderRoot 如果不为null,将做清空操作,然后填充所有root节点的子节点集合
	 * @return 成功返回true,失败返回false
	 */
	@SuppressWarnings("unchecked")
	public static boolean read(String fileName, Object[] obj, List<Element> childUnderRoot)
	{
		try
		{
			SAXReader reader=new SAXReader();
			Document doc=reader.read(new File(fileName));
			Element root=doc.getRootElement();
			if(obj!=null)
			{
				if(obj.length==1)
				{
					obj[0]=doc;
				}
				else if(obj.length>1)
				{
					obj[0]=doc;
					obj[1]=root;
				}
			}
			if(childUnderRoot!=null)
			{
				childUnderRoot.clear();
				childUnderRoot.addAll(root.elements());
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 创建打开格式化写入流.使用完后需要调用endWrite方法
	 * @param os 被包装的待写入的目标文档流
	 * @param encode 编码方式
	 * @return 成功返回XMLWriter对象,出错返回null
	 */
	public static XMLWriter beginWrite(OutputStream os,String encode)
	{
		try
		{
			OutputFormat format=OutputFormat.createPrettyPrint();
			format.setEncoding(encode);
			return new XMLWriter(os,format);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 写入后关闭流,成功返回true,失败返回false
	 */
	public static boolean endWrite(XMLWriter writer,Document doc)
	{
		try
		{
			writer.write(doc);
			writer.close();
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 查找某个属性特定值的元素的另外某个属性
	 * @param e 父节点
	 * @param attrName 已知的属性名
	 * @param attrValue 已知的属性值
	 * @param findAttrName 要查找的未知属性值的属性名
	 * @return 查出的属性值
	 */
	@SuppressWarnings("unchecked")
	public static String findAttrValueFromHadAttr(Element e,String attrName,String attrValue,String findAttrName)
	{
		List<Element> list=e.elements();
		for(Element el:list)
		{
			String value=el.attributeValue(attrName);
			if(value!=null&&value.equals(attrValue))
			{
				return el.attributeValue(findAttrName);
			}
		}
		return null;
	}
	
	/**
	 * @return parent为父节点的直接子节点的特定属性列表
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getChildAttr(Element parent,String attrName)
	{
		List<String> result=new ArrayList<String>();
		List<Element> list=parent.elements();
		for(Element el:list)
		{
			result.add(el.attributeValue(attrName));
		}
		return result;
	}
	
	/**
	 * @return parent为父节点的直接子节点的某两个属性的键值对表
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getChildAttrsMaps(Element parent,String attrNameKey,String attrNameValue)
	{
		Map<String,String> map=new HashMap<String,String>();
		List<Element> list=parent.elements();
		for(Element el:list)
		{
			map.put(el.attributeValue(attrNameKey), el.attributeValue(attrNameValue));
		}
		return map;
	}
	
	/**
	 * @return 将parent子节点们作为beanClass所指定的类型的对象以列表形式返回,出错返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getPojoList(Element parent, Class<T> beanClass)
	{
		try
		{
			List<T> result=new ArrayList<T>();
			List<Element> list=parent.elements();
			for(Element el:list)
			{
				 List<Element> bean=el.elements();
				 T instance=beanClass.newInstance();
				 for(Element property:bean)
				 {
					 String propName=property.getName();
					 String propValue=property.getText();
					 PropertyDescriptor pd=new PropertyDescriptor(propName, beanClass);
					 Method setMethod=pd.getWriteMethod();
					 Class<?>[] clazzes=setMethod.getParameterTypes();
					 Constructor<?> constructor=clazzes[0].getConstructor(String.class);
					 setMethod.invoke(instance, constructor.newInstance(propValue));
				 }
				 result.add(instance);
			}
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} 
	}
}
