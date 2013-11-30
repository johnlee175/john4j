package com.johnsoft.tools;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCExport
{
	private Connection conn=getConnection();
	private DatabaseMetaData dmd;
	
	public static void main(String[] args)
	{
		JDBCExport ex=new JDBCExport();
		ex.exportTables();
	}
	
	private Connection getConnection()
	{
		Connection conn=null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection("jdbc:oracle:thin:@10.200.44.91:1521:CBWMSDB",
					"WMSR5USR", "WMSR5USR");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public void exportTables()
	{
		FileOutputStream fos;
		ResultSet rs;
		StringBuilder sb=new StringBuilder();
		if(conn!=null)
		{
			try
			{
				dmd=conn.getMetaData();
				rs=dmd.getTables(null, "WMSR5USR", "%", new String[]{"TABLE"});

				while(rs.next())
				{
					sb.append("CREATE TABLE "+rs.getString(3)+" (\n");
					sb.append(selectTableModel(rs.getString(3)));
					System.out.println(rs.getString(3));
				}
			
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			fos=new FileOutputStream("E:\\table.txt");
			InputStream is=new ByteArrayInputStream(sb.toString().getBytes());
			byte[] bytes=new byte[1024];
			int len;
			while((len=is.read(bytes))>-1)
			{
				fos.write(bytes,0,len);
			}
			fos.close();
			is.close();
			conn.close();
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}
	
	private String selectTableModel(String tableName)
	{
		ResultSet rs;
		StringBuilder sb=new StringBuilder("");
		if(conn!=null)
		{
			try
			{
				rs=dmd.getColumns(null, null, tableName.toUpperCase(), null);
				
				while(rs.next())
				{
					sb.append(rs.getString(4)).append(" ").append(rs.getString(6));
					if(rs.getString(6).toLowerCase().indexOf("char")>=0||rs.getString(6).toLowerCase().indexOf("num")>=0)
					{
						sb.append("(").append(rs.getString(7));
						if(rs.getString(6).toLowerCase().indexOf("num")>=0)
						{
							sb.append(",").append(rs.getString(9));
						}
						sb.append(") ");
					}
					sb.append(rs.getInt(11)==0?" NOT NULL ":"");
					sb.append(",\n");
				}
				sb.deleteCharAt(sb.length()-2);
				sb.append(");\n");
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
