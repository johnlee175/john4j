package com.johnsoft.library.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JohnJDBCBaseUtil
{
	
	/**释放查询结果集,报表,连接*/
	public static void free(ResultSet rs, Statement st, Connection conn)
	{
		try
		{
			if (rs != null)
			{
				rs.close(); // 关闭结果集
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				if (st != null)
				{
					st.close(); // 关闭Statement
				}
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			} 
			finally
			{
				try
				{
					if (conn != null)
					{
						conn.close(); // 关闭连接
					}
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
