package com.johnsoft.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;

public class OracleConnTest
{
//	public static void main(String[] args)
//	{
//		Connection conn=JohnJDBCUtil.getOracleConn("localhost", "myschema", "scott", "tiger");
//		try
//		{
//			Statement sm=conn.createStatement();
//			ResultSet rs=sm.executeQuery("select * from emp");
//		 while(rs.next())
//		 {
//			 System.out.println(rs.getString(2));
//		 }
//		 rs.close();
//		} catch (SQLException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			try
//			{
//				conn.close();
//			} catch (SQLException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
