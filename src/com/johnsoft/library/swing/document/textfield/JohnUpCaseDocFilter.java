package com.johnsoft.library.swing.document.textfield;


public class JohnUpCaseDocFilter implements JohnPlainDocFilter
{
	private JohnPlainDocFilter filter;
	
	public JohnUpCaseDocFilter(JohnPlainDocFilter filter)
	{
		this.filter=filter;
	}

	@Override
	public boolean filter(JohnPlainDocFilterData data)
	{
		data.string=data.string.toUpperCase(); 
		if(filter!=null)
	    	return filter.filter(data);
	    return true;
	}
}
