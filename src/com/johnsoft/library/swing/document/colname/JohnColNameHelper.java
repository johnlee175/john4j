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
	
	protected String basePath=System.getProperty("user.dir")+"/config/";//JohnColNameHelper.class.getResource("/").getFile();
	protected CurrData currData;
	protected Map<String,CurrData>  resourceMap=new HashMap<String, CurrData>();
	
	private JohnColNameHelper(){
	}
	
	private static class JohnColNameHolder
	{
		public static final JohnColNameHelper instance=new JohnColNameHelper();
	}
	
	private class CurrData
	{
		public CurrData(List<Element> list,Map<String,Integer> idToIdxMap)
		{
			this.list=list;
			this.idToIdxMap=idToIdxMap;
		}
		public List<Element> list;
		public Map<String,Integer> idToIdxMap;
	}
	
	public static JohnColNameHelper getSharedInstance(Class<?> clazz)
	{
		JohnColNameHelper helper=JohnColNameHolder.instance;
		CurrData currData=helper.resourceMap.get(clazz.getName());
		if(currData==null)
		{
			helper.load(clazz);
		}else{
			helper.currData=currData;
		}
		return helper;
	}
	
	public static JohnColNameHelper getNewInstance(Class<?> clazz)
	{
		JohnColNameHelper helper=new JohnColNameHelper();
		helper.load(clazz);
		return helper;
	}
	
	@SuppressWarnings("unchecked")
	protected void load(Class<?> clazz)
	{
		try
		{
			String resource=clazz.getName();
			ByteArrayInputStream is=JohnCommonUtil
					.readByDecoding(basePath + clazz.getSimpleName() /*resource.replace(".", "/")*/ + ".struct", magicNumber);
			SAXReader reader=new SAXReader();
			Document doc=reader.read(is);
			Element root=doc.getRootElement();
			List<Element> list=root.elements();
			Map<String,Integer> idToIdxMap=new HashMap<String, Integer>();
			for(int i=0;i<list.size();i++)
			{
				String id=list.get(i).attributeValue("id");
				idToIdxMap.put(id, i);
			}
			CurrData currData=new CurrData(list, idToIdxMap);
			resourceMap.put(resource, currData);
			this.currData=currData;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getId(int index)
	{
		return currData.list.get(index).attributeValue("id");
	}
	
	public int getIndex (String id)
	{
		return currData.idToIdxMap.get(id);
	}
	
	public String getAttr(String id,String attrName)
	{
		return currData.list.get(currData.idToIdxMap.get(id)).attributeValue(attrName);
	}
	
	public int getColCount()
	{
		return currData.list.size();
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
