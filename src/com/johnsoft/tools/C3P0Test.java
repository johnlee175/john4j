package com.johnsoft.tools;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class C3P0Test
{
	private Connection conn;
	
//	public static void main(String[] args)
//	{
//		C3P0Test tt=new C3P0Test("jdbc:oracle:thin:@127.0.0.1:1521:myschema", "scott", "tiger");
//	  List<MyName> list=tt.find("select * from myname", MyName.class);
//	  for(MyName l:list)
//	  {
//	  	System.out.println(l.getFirestname());
//	  }
//	}
	
//	public C3P0Test(String url,String username,String password)
//	{
//	    ComboPooledDataSource ds=new ComboPooledDataSource();
//		  try
//			{
//				  ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
//				  ds.setUser(username);
//				  ds.setPassword(password);
//				  ds.setJdbcUrl(url);
//				  ds.setMaxPoolSize(20);
//				  conn=ds.getConnection();
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		 
//	}
	
	
	
	public <T> List<T> find(String sql,Class<T> clazz,Object...objs)
	{
		try
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			for(int i=0;i<objs.length;i++)
			{
				if(objs[i]==null)
				{
					continue;
				}
				else
				{
					Object obj=objs[i];
					Class<?> type=obj.getClass();
					if(type==String.class)
					{
						ps.setString(i+1, (String)obj);
						continue;
					}
					else if(type==int.class||type==Integer.class)
					{
						ps.setInt(i+1,(Integer)obj);
						continue;
					}
					else if(type==double.class||type==Double.class)
					{
						ps.setDouble(i+1,(Double)obj);
						continue;
					}
					else if(type==Date.class)
					{
						ps.setDate(i+1, new java.sql.Date(((Date)obj).getTime()));
						continue;
					}
					else if(type==BigDecimal.class)
					{
						ps.setBigDecimal(i+1, (BigDecimal)obj);
						continue;
					}
					else if(type==long.class||type==Long.class)
					{
						ps.setLong(i+1, (Long)obj);
						continue;
					}
					else if(type==boolean.class||type==Boolean.class)
					{
						ps.setBoolean(i+1, (Boolean)obj);
						continue;
					}
					else
					{
						continue;
					}
			  }
			}
			ResultSet rs=ps.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			List<T> list=new ArrayList<T>();
		  while(rs.next())
		  {
		  	T t=clazz.newInstance();
		    Field[] fields=clazz.getDeclaredFields();
		  	for(int i=0;i<meta.getColumnCount();i++)
		  	{
		  		String columnName=meta.getColumnName(i+1).toLowerCase();
			  	for(int j=0;j<fields.length;j++)
			  	{
			  		if(columnName.equals(fields[j].getName().toLowerCase()))
			  		{
			  			Class<?> c=fields[j].getType();
			  			fields[j].setAccessible(true);
			  			Object param;
			  			if(c==String.class)
							{
								param=rs.getString(columnName);
							}
							else if(c==int.class||c==Integer.class)
							{
								param=rs.getInt(columnName);
							}
							else if(c==double.class||c==Double.class)
							{
								param=rs.getDouble(columnName);
							}
							else if(c==Date.class)
							{
								param=rs.getDate(columnName);
							}
							else if(c==BigDecimal.class)
							{
								param=rs.getBigDecimal(columnName);
							}
							else if(c==long.class||c==Long.class)
							{
								param=rs.getLong(columnName);
							}
							else if(c==boolean.class||c==Boolean.class)
							{
								param=rs.getBoolean(columnName);
							}
							else
							{
								param=null;
							}
			  			fields[j].set(t, param);
			  			break;
			  		}
			  	}  
		  	}
		  	list.add(t);
		  }
		return list;		  		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	} 
}

class MyName
{
	private int id;
	private String firestname;
	private String lastname;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}

	public String getFirestname()
	{
		return firestname;
	}
	public void setFirestname(String firestname)
	{
		this.firestname = firestname;
	}
	public String getLastname()
	{
		return lastname;
	}
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}
	
	
}
