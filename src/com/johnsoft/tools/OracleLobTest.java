package com.johnsoft.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;
import com.johnsoft.library.util.jdbc.JohnJDBCUtil.Call;

public class OracleLobTest
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
//				PreparedStatement ps=conn.prepareStatement("insert into mylob values(?,?,?)");
//				ps.setInt(1, id);
//				ps.setBlob(2, BLOB.getEmptyBLOB());
//				ps.setClob(3, CLOB.getEmptyCLOB());
//			  ps.executeUpdate();
//			 
//			  ps=conn.prepareStatement("select myblob,myclob from mylob where id=? for update");
//			  ps.setInt(1,id);
//			  ResultSet rs=ps.executeQuery();
//			  rs.next();
//			  BLOB blob=(BLOB)rs.getBlob(1);
//			  CLOB clob=(CLOB)rs.getClob(2);
//			  
//			  blob.setBytes(1,str.getBytes());
//			  clob.setString(1, str);
//			  
//			  ps=conn.prepareStatement("update mylob set myblob=? , myclob=? where id=?");
//			  ps.setBlob(1, blob);
//			  ps.setClob(2, clob);
//			  ps.setInt(3, id);
//			  ps.executeUpdate();
//			  ps.close();
//			  System.out.println("插入成功！");
//				return null;
//			}
//		});
//	}

}
