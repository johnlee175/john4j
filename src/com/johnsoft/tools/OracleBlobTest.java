package com.johnsoft.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;
import com.johnsoft.library.util.jdbc.JohnJDBCUtil.Call;

public class OracleBlobTest
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
//				PreparedStatement ps=conn.prepareStatement("insert into myblob values(?,?)");
//				ps.setInt(1, id);
//				ps.setBlob(2, BLOB.getEmptyBLOB());
//			  ps.executeUpdate();
//			  ps.close();
//			  
//			  ps=conn.prepareStatement("select myblob from myblob where id=? for update");
//			  ps.setInt(1,id);
//			  ResultSet rs=ps.executeQuery();
//			  rs.next();
//			  BLOB blob=(BLOB)rs.getBlob(1);
//	
//			  InputStream is=new ByteArrayInputStream(str.getBytes());
//			  OutputStream os=blob.setBinaryStream(0);
//			  byte[] buf=new byte[blob.getBufferSize()];
//			  int len;
//			  try
//				{
//					while((len=is.read(buf))>0)
//					{
//						os.write(buf,0,len);
//					}
//					 os.close();
//					 is.close();
//				} catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			  ps.close();
//			 
//			  ps=conn.prepareStatement("update mylob set myblob=? where id=?");
//			  ps.setBlob(1, blob);
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
