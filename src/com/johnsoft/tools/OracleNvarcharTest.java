package com.johnsoft.tools;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;
import com.johnsoft.library.util.jdbc.JohnJDBCUtil.Call;

public class OracleNvarcharTest
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
//				oracle.jdbc.OraclePreparedStatement pstmt = 
//					  (oracle.jdbc.OraclePreparedStatement) 
//					conn.prepareStatement("insert into SAC values(?, ?, ?)");
//					pstmt.setFormOfUse(2, OraclePreparedStatement.FORM_NCHAR);
//					pstmt.setInt(1, 1);                    
//					pstmt.setString(2,"李四");  
//					pstmt.setDate(3, new Date(new java.util.Date().getTime()));  
//					pstmt.execute();
//				  return null;
//			}
//		});
//	}

}
