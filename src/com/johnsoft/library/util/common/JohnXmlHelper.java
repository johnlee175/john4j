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
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * xml文件的辅助操作类
 * @author 李哲浩
 */
public class JohnXmlHelper
{
	private XMLWriter writer;
	private SAXReader reader;
	private Document doc;
	private Element root;
	
	/**
	 * 读取xml文件流,如果不返还null,即读取成功,返回值为根元素
	 */
	public Element read(InputStream is)
	{
		try
		{
			if(reader==null)
			{
				reader=new SAXReader();
			}
			doc=reader.read(is);
			return root=doc.getRootElement();
		} catch (DocumentException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 读取xml文件
	 */
	public void read(String fileName)
	{
		try
		{
			if(reader==null)
			{
				reader=new SAXReader();
			}
			doc=reader.read(new File(fileName));
			root=doc.getRootElement();
		} catch (DocumentException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建打开格式化写入流
	 * @param os 被包装的待写入的目标文档流
	 * @param encode 编码方式
	 */
	public void beginWrite(OutputStream os,String encode)
	{
		try
		{
			OutputFormat format=OutputFormat.createPrettyPrint();
			format.setEncoding(encode);
			writer=new XMLWriter(os,format);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入后关闭流
	 */
	public void endWrite()
	{
		try
		{
			writer.write(doc);
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public SAXReader getReader()
	{
		return reader;
	}
	
	public XMLWriter getWriter()
	{
		return writer;
	}

	/**
	 * @return 文档对象
	 */
	public Document getDocument()
	{
		return doc;
	}

	/**
	 * @return 根节点
	 */
	public Element getRootElement()
	{
		return root;
	}
	
	/**
	 * 仅仅获取根节点下的所有元素集合
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getElementsUnderRoot()
	{
		 return (List<Element>)root.elements();
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
	public String findAttrValueFromHadAttr(Element e,String attrName,String attrValue,String findAttrName)
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
	public List<String> getChildAttr(Element parent,String attrName)
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
	 * @param attrNameKey 要作为键的属性名
	 * @param attrNameValue 要作为值的属性名
	 * @return parent为父节点的直接子节点的某两个属性的键值对表
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getChildAttrsMaps(Element parent,String attrNameKey,String attrNameValue)
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
	 * @return 将parent子节点们作为beanClass所指定的类型的对象以列表形式返回
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getPojoList(Element parent, Class<T> beanClass)
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
