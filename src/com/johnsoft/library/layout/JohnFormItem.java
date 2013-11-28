package com.johnsoft.library.layout;
import java.awt.Component;

public class JohnFormItem
{
	public JohnFormItem(){
	}
	
	public JohnFormItem(Component label,Component textfield,Component btn,int hBlank,int vBlank)
	{
		this.label=label;
		this.textfield=textfield;
		this.btn=btn;
		this.hGap=this.hPadding=hBlank;
		this.vGap=this.vPadding=vBlank;
	}
	
	public void setComponents(Component header,Component label,Component textfield,Component tip,Component btn,Component validate,Component commit)
	{
		this.header=header;
		this.label=label;
		this.textfield=textfield;
		this.tip=tip;
		this.btn=btn;
		this.validate=validate;
		this.commit=commit;
	}
	
	public void setBlank(int hGap,int vGap,int hPadding,int vPadding)
	{
		this.hGap=hGap;
		this.vGap=vGap;
		this.hPadding=hPadding;
		this.vPadding=vPadding;
	}
	
	public Component getHeader()
	{
		return header;
	}

	public Component getLabel()
	{
		return label;
	}

	public Component getTextfield()
	{
		return textfield;
	}

	public Component getTip()
	{
		return tip;
	}

	public Component getBtn()
	{
		return btn;
	}

	public Component getValidate()
	{
		return validate;
	}

	public Component getCommit()
	{
		return commit;
	}

	public int gethGap()
	{
		return hGap;
	}

	public int getvGap()
	{
		return vGap;
	}

	public int gethPadding()
	{
		return hPadding;
	}

	public int getvPadding()
	{
		return vPadding;
	}

	Component header;
	Component label;
	Component textfield;
	Component tip;
	Component btn;
	Component validate;
	Component commit;
	
	int hGap,vGap,hPadding,vPadding;
}