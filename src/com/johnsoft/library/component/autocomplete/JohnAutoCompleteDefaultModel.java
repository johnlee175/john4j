package com.johnsoft.library.component.autocomplete;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JohnAutoCompleteDefaultModel implements JohnAutoCompleteModel
{
	 protected Map<String,String> inputToShowMap;
	 protected Map<String,String> showToCommitMap;
	 
	 public JohnAutoCompleteDefaultModel(Map<String,String> inputToShowMap,Map<String,String> showToCommitMap)
	 {
		 this.inputToShowMap=inputToShowMap;
		 this.showToCommitMap=showToCommitMap;
	 }
	 
	 public JohnAutoCompleteDefaultModel(List<?> list,String field1,String field2,String field3)
	 {
		 for(Object obj:list)
		 {
			 String input=getFieldValue(obj, field1);
			 String show=getFieldValue(obj, field2);
			 String commit=getFieldValue(obj, field3);
			 inputToShowMap.put(input, show);
			 showToCommitMap.put(show, commit);
		 }
	 }
	 
	 public JohnAutoCompleteDefaultModel(List<List<String>> list,int index1,int index2,int index3)
	 {
		 for(List<String> l:list)
		 {
			 inputToShowMap.put(l.get(index1), l.get(index2));
			 showToCommitMap.put(l.get(index2), l.get(index3));
		 }
	 }
	 
	 public JohnAutoCompleteDefaultModel(Map<String,String> map,boolean isInputToShow)
	 {
		 if(isInputToShow)
		 {
			 inputToShowMap=map;
			 for(String key:map.keySet())
			 {
				 String value=map.get(key);
				 showToCommitMap.put(value, value);
			 }
		 }else{
			 showToCommitMap=map;
			 for(String key:map.keySet())
			 {
				 showToCommitMap.put(key, key);
			 }
		 }
	 }
	 
	 public JohnAutoCompleteDefaultModel(List<String> input,List<String> show,List<String> commit)
	 {
		  int size=Math.min(input.size(), show.size());
		  size=Math.min(size, commit.size());
		  for(int i=0;i<size;i++)
		  {
		  	inputToShowMap.put(input.get(i), show.get(i));
		  	showToCommitMap.put(show.get(i), commit.get(i));
		  }
	 }
	 
	 public JohnAutoCompleteDefaultModel(List<String> list)
	 {
		 	this(list,list,list);
	 }
	 
	 private Method getGetMethod(Object obj,String fieldName)
	 {
		 if(obj!=null&&fieldName!=null&&!fieldName.trim().isEmpty())
		 {
				try
				{
					Class<?> clazz = obj.getClass();
					Field field=clazz.getDeclaredField(fieldName);
					String methodName=fieldName;
					if(field.getType()==boolean.class)
					{
						methodName="is"+methodName;
					}else{
						methodName="get"+methodName;
					}
					return clazz.getDeclaredMethod(methodName);
				} catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}
		 }
		 return null;
	 }
	 
	 private String getFieldValue(Object obj,String fieldName)
	 {
		 Method method=getGetMethod(obj, fieldName);
		 if(method!=null)
		 {
			 try
			{
				  Object o=method.invoke(obj);
				  return o!=null?o.toString():null;
			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			} 
		 }
		 return null;
	 }
	 
	 public Set<String> getInputKeySet()
	 {
		 return inputToShowMap.keySet();
	 }
	 
	 public Set<String> getShowKeySet()
	 {
		 return showToCommitMap.keySet();
	 }
	 
	 public String getShowValue(Object inputValue)
	 {
		 return inputToShowMap.get(inputValue);
	 }
	 
	 public String getCommitValue(Object showValue)
	 {
		 return showToCommitMap.get(showValue);
	 }
}
