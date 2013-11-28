package com.johnsoft.library.util.action;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.Action;
import javax.swing.KeyStroke;


public final class JohnActionManager
{
	public static final String accelKeyResourcePath="E:\\out.txt";
	public static final String longDescriptionFilePath="E:\\ld.txt";
	public static final Map<String,AccelKeyDescriptor> AKDMap=new HashMap<String,AccelKeyDescriptor>();
	public static final Map<String,JohnAction> actionMap=new HashMap<String,JohnAction>();
	public static final Map<String,String> longDescriptionMap=new HashMap<String,String>();
	
	static{
		readAcceleratorKeyDescriptions();
		readLongDescriptionFile();
		registerKeyListener();
	}
	
	public static final void buildAccelKey(String uuid,String commandDescription,String key,String name,Class<?> clazz,boolean inherit)
	{
		if(name==null&&clazz==null)
		{
			System.out.println("The param 'name' and 'clazz' can't be null!");
			return;
		}
		String oldString;
		if(uuid==null)
		{
			uuid=UUID.randomUUID().toString();
			if(name!=null&&clazz!=null)
			{
				uuid="A"+uuid;
			}else if(clazz!=null)
			{
				uuid="C"+uuid;
			}else
			{
				uuid="N"+uuid;
			}
			System.out.println(uuid);
			oldString=null;
		}else{
			AccelKeyDescriptor akd=AKDMap.get(uuid);
			if(akd==null) 
			{
				System.out.println("Can't find the uuid!");
				return;
			}
			if(	(akd.accelKeyUUID.startsWith("A")&&(clazz==null||name==null))
				||(akd.accelKeyUUID.startsWith("C")&&clazz==null)
				||(akd.accelKeyUUID.startsWith("N")&&name==null)
			   )
			{
				System.out.println("Can't modify the when property in the accel key!");
				return;
			}
			oldString=akd.accelKeyUUID+"\t"+akd.key+"\t"
					    +(akd.whenTheClass!=null?akd.whenTheClass.getName():"null")+"\t"+akd.whenTheName+"\t"
						+akd.inherit+"\t"+akd.commandDescription+"\r\n";
		}
		String writeString=uuid+"\t"+key+"\t"
							+(clazz!=null?clazz.getName():"null")+"\t"+name+"\t"
							+inherit+"\t"+commandDescription+"\r\n";
		try
		{
			if(oldString!=null)
			{
				BufferedReader br=new BufferedReader(new FileReader(accelKeyResourcePath));
				StringBuffer sb=new StringBuffer();
				char[] chars=new char[1024];
				int len=0;
				while((len=br.read(chars))>0)
				{
					sb.append(chars, 0, len);
				}
				String content=sb.toString();
				br.close();
				writeString=content.replace(oldString, writeString);
			}
			FileWriter fw=new FileWriter(accelKeyResourcePath, oldString==null);
			fw.write(writeString);
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static final void addAction(JohnAction action)
	{
		 String accelKeyUUID=action.getAccelKeyUUID();
		 if(accelKeyUUID!=null)
		 {
			 String description=longDescriptionMap.get(accelKeyUUID);
			 if(description!=null)
			 {
				 action.putValue(Action.LONG_DESCRIPTION, description);
			 }
			 AccelKeyDescriptor akd=AKDMap.get(accelKeyUUID);
			 if(akd!=null)
			 {
				 action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(akd.key));
			 }
			 actionMap.put(accelKeyUUID, action);
		 } 
	}
	
	public static final void readAcceleratorKeyDescriptions()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(accelKeyResourcePath));
			StringBuffer sb=new StringBuffer();
			char[] chars=new char[1024];
			int len=0;
			while((len=br.read(chars))>0)
			{
				sb.append(chars, 0, len);
			}
			String content=sb.toString();
			br.close();
			String[] records=content.split("\r\n");
			for(int i=0;i!=records.length;++i)
			{
				String record=records[i];
				if(record.startsWith("#")) continue;
				String[] struct=record.split("\t");
				if(struct.length<5) continue;
				AccelKeyDescriptor akd=new AccelKeyDescriptor();
				akd.accelKeyUUID=struct[0];
				akd.key=struct[1];
				if(struct[0].startsWith("A"))
				{
					akd.whenTheClass=Class.forName(struct[2]);
					akd.whenTheName=struct[3];
				}else if(struct[0].startsWith("C"))
				{
					akd.whenTheClass=Class.forName(struct[2]);
				}else if(struct[0].startsWith("N"))
				{
					akd.whenTheName=struct[3];
				}else
				{
					throw new RuntimeException("The Accelerator Key File describe error!");
				}
				akd.inherit=Boolean.valueOf(struct[4]);
				if(struct.length>5)
				{
					akd.commandDescription=struct[5];
				}
				AKDMap.put(struct[0], akd);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static final void readLongDescriptionFile()
	{
		try
		{
			FileReader fr=new FileReader(longDescriptionFilePath);
			StringBuffer sb=new StringBuffer();
			char[] chars=new char[1024];
			int len=0;
			while((len=fr.read(chars))>0)
			{
				sb.append(chars, 0, len);
			}
			String content=sb.toString();
			fr.close();
			String[] records=content.split("#");
			for(int i=0;i!=records.length;++i)
			{
				String[] struct=records[i].split(":");
				if(struct.length==2)
				{
					longDescriptionMap.put(struct[0], struct[1]);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static final void registerKeyListener()
	{
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener()
		{
			@Override
			public void eventDispatched(AWTEvent e)
			{
				if(e.getID()==KeyEvent.KEY_PRESSED)
				{
					KeyEvent ke=(KeyEvent)e;
					Object obj=e.getSource();
					if(obj instanceof Component)
					{
						Component c=null,comp=(Component)obj;
						if(AKDMap.isEmpty()||actionMap.isEmpty()) return;
						for(String id:AKDMap.keySet())
						{
							c=comp;
							while(c!=null)
							{
								AccelKeyDescriptor akd=AKDMap.get(id);
								boolean isRightWhen=false;
								if(akd!=null)
								{
									if(akd.whenTheClass!=null&&akd.whenTheName!=null)
									{
										isRightWhen=akd.whenTheClass.isAssignableFrom(c.getClass())&&akd.whenTheName.equals(c.getName());
									}else if(akd.whenTheClass!=null)
									{
										isRightWhen=akd.whenTheClass.isAssignableFrom(c.getClass());
									}else if(akd.whenTheName!=null)
									{
										isRightWhen=akd.whenTheName.equals(c.getName());
									}
								}
								label:
								if(isRightWhen)
								{
									boolean a=akd.key.toLowerCase().contains("alt");
									boolean b=ke.isAltDown();
									if((a&&!b)||(!a&&b))
									{
										break label;
									}
									a=akd.key.toLowerCase().contains("ctrl");
								    b=ke.isControlDown();
									if((a&&!b)||(!a&&b))
									{
										break label;
									}
									a=akd.key.toLowerCase().contains("shift");
									b=ke.isShiftDown();
									if((a&&!b)||(!a&&b))
									{
										break label;
									}
									String[] strs=akd.key.split(" ");
									try
									{
										int code=KeyEvent.class.getField("VK_"+strs[strs.length-1]).getInt(KeyEvent.class);
										if(code==ke.getKeyCode())
										{
											JohnAction ja=actionMap.get(id);
											if(ja!=null)
											{
												ja.actionPerformed(new ActionEvent(c, ActionEvent.ACTION_PERFORMED, (String) ja.getValue(Action.ACTION_COMMAND_KEY)));
												return;
											}
										}
									} catch (Exception e1)
									{
										e1.printStackTrace();
									} 
								}
								if(akd.inherit)
								{
									c=c.getParent();
								}else{
									c=null;
								}
							}
						}
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
	}
	
	public static final class AccelKeyDescriptor
	{
		public String accelKeyUUID;
		public String key;
		public String whenTheName;
		public Class<?> whenTheClass;
		public boolean inherit;
		public String commandDescription;
	}
}
