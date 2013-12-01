package com.johnsoft.library.swing.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

public abstract class JohnMenuItemBaseAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;

	public JohnMenuItemBaseAction()
	{
		super();
		setActionValue();
	}
	
	public JohnMenuItemBaseAction(String name,Icon icon,String tooltip,Integer mnemonic,Integer displayIndex,KeyStroke quickKey)
	{
		super();
		setActionValue(name, icon, tooltip, mnemonic, displayIndex, quickKey);
		setActionValue();
	}
	
	public void setActionValue(){	
	}
	public void setActionValue(String name,Icon icon,String tooltip,Integer mnemonic,Integer displayIndex,KeyStroke quickKey)
	{
		this.putValue(Action.NAME, name);
		this.putValue(Action.SMALL_ICON, icon);
		this.putValue(Action.SHORT_DESCRIPTION, tooltip);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
		this.putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, displayIndex);
		this.putValue(Action.ACCELERATOR_KEY, quickKey);
	}
	
	public static Action setActionValue(Action action,String name,Icon icon,String tooltip,Integer mnemonic,Integer displayIndex,KeyStroke quickKey)
	{
		action.putValue(Action.NAME, name);
		action.putValue(Action.SMALL_ICON, icon);
		action.putValue(Action.SHORT_DESCRIPTION, tooltip);
		action.putValue(Action.MNEMONIC_KEY, mnemonic);
		action.putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, displayIndex);
		action.putValue(Action.ACCELERATOR_KEY, quickKey);
		return action;
	}
}
