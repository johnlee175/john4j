package com.johnsoft.tools;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;
import com.johnsoft.library.util.jdbc.JohnJDBCUtil.Call;
//建议参考OracleLobTest中的setString用法代替io用法
public class OracleClobTest
{
//	public static void main(String[] args)
//	{
//		final Connection conn=JohnJDBCUtil.getConnection("Resource/database.properties");
//	  
//		JohnJDBCUtil.getExecute(conn, new Call<Object>()
//		{
//			@Override
//			public Object exec() throws SQLException
//			{
//				StringBuilder sb=new StringBuilder("hello world ");
//				for(int i=0;i<10000;i++)
//				{
//					sb.append("hello world ");
//				}
//				String str=sb.toString();
//		    
//				Integer id=1;
//				PreparedStatement ps=conn.prepareStatement("insert into myclob values(?,?)");
//				ps.setInt(1, id);
//				ps.setClob(2, CLOB.getEmptyCLOB());
//			  ps.executeUpdate();
//			  ps.close();
//	
//			  ps=conn.prepareStatement("select myclob from myclob where id=? for update");
//			  ps.setInt(1,id);
//			  ResultSet rs=ps.executeQuery();
//			  rs.next();
//			  CLOB clob=(CLOB)rs.getClob(1);
//			  
//			  Reader reader=new CharArrayReader(str.toCharArray());
//			  Writer writer=clob.setCharacterStream(0);
//			  char[] buf=new char[clob.getBufferSize()];
//			  int len;
//			  try
//				{
//					while((len=reader.read(buf))>0)
//					{
//						writer.write(buf,0,len);
//					}
//					 writer.close();
//					 reader.close();
//				} catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			  ps.close();
//	
//			  ps=conn.prepareStatement("update myclob set myclob=? where id=?");
//			  ps.setClob(1, clob);
//			  ps.setInt(2, id);
//			  ps.executeUpdate();
//			  ps.close();
//			  System.out.println("插入成功！");
//				return null;
//			}
//		});
//		
//	}

}
