package com.johnsoft.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;

public class MysqlConnTest
{
	public static void main(String[] args)
	{
		Connection conn=JohnJDBCUtil.getMySQLConn("localhost", "myschema", "root", "7512175");
		try
		{
			PreparedStatement sm=conn.prepareStatement("select * from customer;");
			ResultSet rs=sm.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(3));
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
