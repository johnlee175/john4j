package com.johnsoft.library.swing.document.textfield;


public class JohnLimitLenDocFilter implements JohnPlainDocFilter
{
	private JohnPlainDocFilter filter;
	private int limitLen;
	
	public JohnLimitLenDocFilter(JohnPlainDocFilter filter,int limitLen)
	{
		this.filter=filter;
		this.limitLen=limitLen;
	}

	@Override
	public boolean filter(JohnPlainDocFilterData data)
	{
		if ((data.doc.getLength() + data.string.length()) > limitLen) 
		{
			 return false;
		}
		if(filter!=null)
	    	return filter.filter(data);
	    return true;
	}
}
