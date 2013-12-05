package com.johnsoft.library.swing.document.textfield;


public class JohnUIntDocFilter implements JohnPlainDocFilter
{
	private JohnPlainDocFilter filter;
	
	public JohnUIntDocFilter(JohnPlainDocFilter filter)
	{
		this.filter=filter;
	}

	@Override
	public boolean filter(JohnPlainDocFilterData data)
	{
		 char[] chars = data.string.toCharArray();
	     for (int i = 0; i < chars.length; i++) 
	     {
	         if(!Character.isDigit(chars[i]))
	        	 return false;
	     }
	     if(filter!=null)
		    	return filter.filter(data);
		    return true;
	}
}
