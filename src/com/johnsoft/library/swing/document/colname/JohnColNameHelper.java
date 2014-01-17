package com.johnsoft.library.swing.document.colname;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.johnsoft.library.util.common.JohnCommonUtil;



public class JohnColNameHelper
{
	private static final int magicNumber=2784051;
	private static final Map<String, JohnColNameHelper> managePool=new HashMap<String, JohnColNameHelper>();
	private static final String basePath=System.getProperty("user.dir")+"/config/";//JohnColNameHelper.class.getResource("/").getFile();
	
	private List<Element> list;
	private Map<String,Integer> idToIdxMap;
	
	private JohnColNameHelper(){
	}
	
	public static JohnColNameHelper getSharedInstance(Class<?> clazz)
	{
		String className=clazz.getName();
		JohnColNameHelper helper=managePool.get(className);
		if(helper==null)
		{
			helper=new JohnColNameHelper();
			helper.load(clazz);
			managePool.put(className, helper);
		}
		return helper;
	}
	
	@SuppressWarnings("unchecked")
	protected void load(Class<?> clazz)
	{
		try
		{
//			String resource=clazz.getName();
			ByteArrayInputStream is=JohnCommonUtil
					.readByDecoding(basePath + clazz.getSimpleName() /*resource.replace(".", "/")*/ + ".struct", magicNumber);
			SAXReader reader=new SAXReader();
			Document doc=reader.read(is);
			Element root=doc.getRootElement();
			list=root.elements();
			idToIdxMap=new HashMap<String, Integer>();
			for(int i=0;i<list.size();i++)
			{
				String id=list.get(i).attributeValue("id");
				idToIdxMap.put(id, i);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getId(int index)
	{
		return list.get(index).attributeValue("id");
	}
	
	public int getIndex (String id)
	{
		return idToIdxMap.get(id);
	}
	
	public String getAttr(String id,String attrName)
	{
		return list.get(idToIdxMap.get(id)).attributeValue(attrName);
	}
	
	public int getColCount()
	{
		return list.size();
	}
	
	public static void createStuctFile(Class<?> clazz,String parentPathFromRoot)
	{
		String userdir=System.getProperty("user.dir");
		String inPath=userdir+"/"+parentPathFromRoot+((parentPathFromRoot.endsWith("/")||parentPathFromRoot.endsWith("\\"))?"":"/")+clazz.getSimpleName()+".struct.xml";
		//String outPath=userdir+"/src/"+clazz.getName().replace(".", "/") + ".struct";
		JohnCommonUtil.writeByEncoding(inPath, inPath.substring(0,inPath.length()-4)/*outPath*/, magicNumber);
	}
	
//	public static void main(String[] args)
//	{
//		JohnColNameHelper.createStuctFile(CurrAirPlanTable.class, "config");
//	}
	
}
