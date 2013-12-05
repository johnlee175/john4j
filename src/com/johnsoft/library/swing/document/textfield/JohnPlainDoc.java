package com.johnsoft.library.swing.document.textfield;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JohnPlainDoc extends PlainDocument
{
	private static final long serialVersionUID = 1L;
	
	private JohnPlainDocFilter filter;
	
	public JohnPlainDoc(JohnPlainDocFilter filter)
	{
		this.filter=filter;
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException
	{
		if(str==null) return;
		if(filter==null)
		{
			super.insertString(offs, str, a);
			return;
		}
		JohnPlainDocFilterData data=new JohnPlainDocFilterData(this, str);
		if(filter.filter(data))
		{
			super.insertString(offs, data.string, a);
		}
	}
}
