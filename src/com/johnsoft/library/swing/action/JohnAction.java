package com.johnsoft.library.swing.action;

import javax.swing.AbstractAction;
import javax.swing.Icon;

public abstract class JohnAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	
	private String accelKeyUUID;
	
	public JohnAction(String commandName,Icon icon,String tooltip,String accelKeyUUID)
	{
		this.accelKeyUUID=accelKeyUUID;
		int idx=commandName.indexOf("&");
		if(idx>=0)
		{
			commandName=commandName.substring(0, idx)+commandName.substring(idx+1);
			putValue(MNEMONIC_KEY, (int)commandName.charAt(idx));
			putValue(DISPLAYED_MNEMONIC_INDEX_KEY, idx);
		}
		putValue(NAME, commandName);
		putValue(ACTION_COMMAND_KEY, commandName);
		putValue(SMALL_ICON, icon);
		putValue(LARGE_ICON_KEY, icon);
		putValue(SHORT_DESCRIPTION, tooltip);
		if(accelKeyUUID!=null)
		{
			JohnActionManager.addAction(this);
		}
	}
	
	public JohnAction(String text,String command,Icon smallIcon,Icon largeIcon,String tooltip,String accelKeyUUID)
	{
		this.accelKeyUUID=accelKeyUUID;
		int idx=text.indexOf("&");
		if(idx>=0)
		{
			text=text.substring(0, idx)+text.substring(idx+1);
			putValue(MNEMONIC_KEY, (int)text.charAt(idx));
			putValue(DISPLAYED_MNEMONIC_INDEX_KEY, idx);
		}
		putValue(NAME, text);
		putValue(ACTION_COMMAND_KEY, command);
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(SHORT_DESCRIPTION, tooltip);
		if(accelKeyUUID!=null)
		{
			JohnActionManager.addAction(this);
		}
	}
	
	public String getAccelKeyUUID()
	{
		return accelKeyUUID;
	}
	
	public final Boolean isSelected()
	{
		return (Boolean)getValue(SELECTED_KEY);
	}
	
	public final void setSelected(Boolean selected)
	{
		putValue(SELECTED_KEY, selected);
	}
	
}
