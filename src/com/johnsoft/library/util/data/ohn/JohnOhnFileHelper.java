package com.johnsoft.library.util.data.ohn;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JohnOhnFileHelper
{
	//String content="@map:cat\nname=baidu\ncolor=red\n@list:hello\nhello\nworld\nworld\n@table:com\n{\nbegin=4\nend=4\ncurr=4\n}\n{\nuser=4\nsa=1120\n}\n{\nchans=2210\n}\n";
	
	public enum ScopeType
	{
		Map,List,Table
	}
	
	public static Map<String,Object> parse(String content)
	{
		Map<String,Object> global=new HashMap<String,Object>();
		content=content.replaceAll("[\\s&&[^\n]]+", "");
		content=content.replaceAll("%20", " ");
    String[] strs1=content.split("@");
    for(int i=1;i<strs1.length;i++)
    {
    	String[] strs2=strs1[i].split("[\n\r]+");
    	String[] strs3=strs2[0].split(":");
    	if(strs3[0].equals("map"))
    	{
    		Map<String,String> map=new HashMap<String,String>();
    		for(int j=1;j<strs2.length;j++)
    		{
    			if(strs2[j].startsWith("#")||strs2[j].trim().isEmpty())
    			{
    				continue;
    			}
    			String[] strs4=strs2[j].split("=");
    			if(strs4.length!=2)
    			{
    				continue;
    			}
    			map.put(strs4[0], strs4[1]);
    		}
    		global.put(strs3[1], map);
    	}
    	else if(strs3[0].equals("list"))
    	{
    		List<String> list=new ArrayList<String>();
    		for(int j=1;j<strs2.length;j++)
    		{
    			if(strs2[j].startsWith("#")||strs2[j].trim().isEmpty())
    			{
    				continue;
    			}
    			list.add(strs2[j]);
    		}
    		global.put(strs3[1], list);
    	}
    	else if(strs3[0].equals("table"))
    	{
    		List<Map<String,String>> table=new ArrayList<Map<String,String>>();
    		int j=1;
    		while(j<strs2.length)
    		{
    			if(strs2[j].startsWith("{"))
    			{
    				Map<String,String> map=new HashMap<String,String>();
    				for(int k=j+1;k<strs2.length;k++)
    				{
    					if(!strs2[k].startsWith("}"))
    					{
    						if(strs2[j].startsWith("#")||strs2[j].trim().isEmpty())
    	    			{
    	    				continue;
    	    			}
    						String[] strs4=strs2[k].split("=");
    						if(strs4.length!=2)
    						{
    							continue;
    						}
    						map.put(strs4[0], strs4[1]);
    					}else{
    						table.add(map);
    						j=k+1;
    						break;
    					}
    				}
    			}
    		}
    		global.put(strs3[1], table);
    	}
    }
    return global;
	}

	@SuppressWarnings("unchecked")
	public static String format(Map<String,Object> global)
	{
		String content=null;
		StringBuffer sb=new StringBuffer();
		Iterator<String> keyIter=global.keySet().iterator();
		while(keyIter.hasNext())
		{
			String keystr=keyIter.next();
			Object obj=global.get(keystr);
			if(obj instanceof Map)
			{
				sb.append("@map:").append(keystr).append("\n");
				Map<String,String> map=(Map<String,String>)obj;
				for(String k:map.keySet())
				{
					sb.append(k).append("=").append(map.get(k)).append("\n");
				}
			}
			if(obj instanceof List)
			{
				List<?> temp=(List<?>)obj;
				Object o=temp.get(0);
				if(o instanceof String)
				{
					sb.append("@list:").append(keystr).append("\n");
					List<String> list=(List<String>)obj;
					for(String str:list)
					{
						sb.append(str).append("\n");
					}
				}
				else if(o instanceof Map)
				{
					sb.append("@table:").append(keystr).append("\n");
					List<Map<String,String>> table=(List<Map<String,String>>)obj;
					for(int i=0;i<table.size();i++)
					{
						sb.append("{\n");
						Map<String,String> m=table.get(i);
						for(String k:m.keySet())
						{
							sb.append(k).append("=").append(m.get(k)).append("\n");
						}
						sb.append("}\n");
					}
				}
			}
		}
		content=sb.toString();
		return content;
	}

	protected Map<String,Object> global=new HashMap<String,Object>();
  
  protected String currentScopeName;
	
  public void save(String filePath,String charset)
	{
		JohnOhnFileIO.writeOhn(new File(filePath), format(global), charset);
	}
	
	public void load(String filePath,String charset)
	{
		global=parse(JohnOhnFileIO.readOhn(new File(filePath), charset));
	}

	public String getCurrentScopeName()
	{
		return currentScopeName;
	}
	
	public void setCurrentScopeName(String currentScopeName)
	{
		this.currentScopeName=currentScopeName;
	}
	
	public ScopeType getScopeType(String scopeName)
	{
		Object obj=global.get(scopeName);
	  if(obj instanceof Map)
		{
			return ScopeType.Map;
		}
		else if(obj instanceof List)
		{
			List<?> list=(List<?>)obj;
			Object obj2=list.get(0);
			if(obj2 instanceof String)
			{
				return ScopeType.List;
			}
			else if(obj2 instanceof Map)
			{
				return ScopeType.Table;
			}
		}
		return null;
	}
	
	public Map<String,String> convert(Object obj,boolean isPublic)
	{
		Map<String,String> map=new HashMap<String,String>();
		Field[] fields=null;
		if(isPublic)
		{
			fields=obj.getClass().getFields();
		}else{
			fields=obj.getClass().getDeclaredFields();
		}
		for(Field f:fields)
		{
			f.setAccessible(true);
			try
			{
				map.put(f.getName(), f.get(obj).toString());
			} catch (Exception e)
			{
				e.printStackTrace();
			} 
		}
		return map;
	}
	
	public List<Map<String,String>> convert(List<Object> list,boolean isPublic)
	{
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		for(Object obj:list)
		{
			result.add(convert(obj, isPublic));
		}
		return result;
	}
	
	public void putMap(Map<String,String> map,String scopeName)
	{
		global.put(scopeName, map);
	}
	
	public void putList(List<String> list,String scopeName)
	{
		global.put(scopeName, list);
	}
	
	public void putTable(List<Map<String,String>> table,String scopeName)
	{
		global.put(scopeName, table);
	}
	
	public void putMapElement(String scopeName,String key,String value)
	{
		getMap(scopeName).put(key, value);
	}
	
	public void addListElement(String scopeName,String value)
	{
		getList(scopeName).add(value);
	}
	
	public void addTableElement(String scopeName,Map<String,String> map)
	{
		getTable(scopeName).add(map);
	}
	
	public void putTableElement(String scopeName,int row,String columnName,String data)
	{
		getTableValue(scopeName, row).put(columnName, data);
	}
	
	public void setListElement(String scopeName,int index,String value)
	{
		getList(scopeName).set(index, value);
	}
	
	public void setTableElement(String scopeName,int index,Map<String,String> map)
	{
		getTable(scopeName).set(index, map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getMap(String scopeName)
	{
		return (Map<String,String>)global.get(scopeName);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getList(String scopeName)
	{
		return (List<String>)global.get(scopeName);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getTable(String scopeName)
	{
		return (List<Map<String,String>>)global.get(scopeName);
	}

	public String getMapValue(String scopeName,String key)
	{
		return getMap(scopeName).get(key);
	}
	
	public String getListValue(String scopeName,int index)
	{
		return getList(scopeName).get(index);
	}
	
	public String getTableValue(String scopeName,int row,String columnName)
	{
		return getTable(scopeName).get(row).get(columnName);
	}
	
	public Map<String,String> getTableValue(String scopeName,int row)
	{
		return getTable(scopeName).get(row);
	}
	
	public void removeScope(String scopeName)
	{
		global.remove(scopeName);
	}
	
	public void removeMapElement(String scopeName,String key)
	{
		getMap(scopeName).remove(key);
	}
	
	public void removeListElement(String scopeName,int index)
	{
		getList(scopeName).remove(index);
	}
	
	public void removeTableElement(String scopeName,int row,String columnName)
	{
		getTable(scopeName).get(row).remove(columnName);
	}
	
	public void removeTableElement(String scopeName,int row)
	{
		getTable(scopeName).remove(row);
	}
	
	public void createMap(String scopeName)
	{
		 Map<String, String> map=new HashMap<String, String>();
		 global.put(scopeName, map);
		 currentScopeName=scopeName;
	}
	
	public void createList(String scopeName)
	{
		 List<String> list=new ArrayList<String>();
		 global.put(scopeName, list);
		 currentScopeName=scopeName;
	}
	
	public void createTable(String scopeName)
	{
		List<Map<String,String>> table=new ArrayList<Map<String,String>>();
		global.put(scopeName, table);
		currentScopeName=scopeName;
	}
	
	public void putMap(Map<String,String> map)
	{
		global.put(currentScopeName, map);
	}
	
	public void putList(List<String> list)
	{
		global.put(currentScopeName, list);
	}
	
	public void putTable(List<Map<String,String>> table)
	{
		global.put(currentScopeName, table);
	}
	
	public void putMapElement(String key,String value)
	{
		getMap(currentScopeName).put(key, value);
	}
	
	public void addListElement(String value)
	{
		getList(currentScopeName).add(value);
	}
	
	public void addTableElement(Map<String,String> map)
	{
		getTable(currentScopeName).add(map);
	}
	
	public void putTableElement(int row,String columnName,String data)
	{
		getTableValue(currentScopeName, row).put(columnName, data);
	}
	
	public void setListElement(int index,String value)
	{
		getList(currentScopeName).set(index, value);
	}
	
	public void setTableElement(int index,Map<String,String> map)
	{
		getTable(currentScopeName).set(index, map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getMap()
	{
		return (Map<String,String>)global.get(currentScopeName);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getList()
	{
		return (List<String>)global.get(currentScopeName);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getTable()
	{
		return (List<Map<String,String>>)global.get(currentScopeName);
	}

	public String getMapValue(String key)
	{
		return getMap(currentScopeName).get(key);
	}
	
	public String getListValue(int index)
	{
		return getList(currentScopeName).get(index);
	}
	
	public String getTableValue(int row,String columnName)
	{
		return getTable(currentScopeName).get(row).get(columnName);
	}
	
	public Map<String,String> getTableValue(int row)
	{
		return getTable(currentScopeName).get(row);
	}
	
	public void removeScope()
	{
		global.remove(currentScopeName);
	}
	
	public void removeMapElement(String key)
	{
		getMap(currentScopeName).remove(key);
	}
	
	public void removeListElement(int index)
	{
		getList(currentScopeName).remove(index);
	}
	
	public void removeTableElement(int row,String columnName)
	{
		getTable(currentScopeName).get(row).remove(columnName);
	}
	
	public void removeTableElement(int row)
	{
		getTable(currentScopeName).remove(row);
	}
	
}
