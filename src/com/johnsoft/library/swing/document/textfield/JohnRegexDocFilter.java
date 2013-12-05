package com.johnsoft.library.swing.document.textfield;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JohnRegexDocFilter implements JohnPlainDocFilter
{
	private JohnPlainDocFilter filter;
	private String regex;
	
	public JohnRegexDocFilter(JohnPlainDocFilter filter, String regex)
	{
		this.filter=filter;
		this.regex=regex;
	}

	@Override
	public boolean filter(JohnPlainDocFilterData data)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data.string);
		if (!matcher.matches())
		{
			return false;
		}
		if(filter!=null)
	    	return filter.filter(data);
	    return true;
	}
}
