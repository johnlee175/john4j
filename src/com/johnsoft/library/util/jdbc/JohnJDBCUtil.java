package com.johnsoft.library.util.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.johnsoft.library.util.gui.JohnException;

public class JohnJDBCUtil
{
	public static Connection getConnection(String fileName)
	{
		try
		{
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			Properties prop=new Properties();
			prop.load(is);
			is.close();
			
			String drivers=prop.getProperty("jdbc.drivers");
			String url=prop.getProperty("jdbc.url");
			String username=prop.getProperty("jdbc.username");
			String password=prop.getProperty("jdbc.password");
			
			if(drivers!=null){System.setProperty("jdbc.drivers", drivers);}
		
			return DriverManager.getConnection(url, username, password);
	
		} catch (Exception e)
		{
		  new JohnException(e, "无法取得有效数据库连接！");
			return null;
		}
	}
	
	public static Connection getMySQLConn(String IP,String databaseName,String username,String password)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			if(IP.indexOf(":")<0)
			{
				IP=IP+":3306";
			}
			String url="jdbc:mysql://"+IP+"/"+databaseName;
			return DriverManager.getConnection(url, username, password);
		
		} catch (Exception e)
		{
			new JohnException(e, "无法取得有效数据库连接！");
			return null;
		}
	}
	
	public static Connection getOracleConn(String IP,String databaseName,String username,String password)
	{
		try
		{
			Driver driver=(Driver)Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			DriverManager.registerDriver(driver);
			if(IP.indexOf(":")<0)
			{
				IP=IP+":1521";
			}
			String url="jdbc:oracle:thin:@"+IP+":"+databaseName;
			return DriverManager.getConnection(url, username, password);
		
		} catch (Exception e)
		{
			new JohnException(e, "无法取得有效数据库连接！");
			return null;
		}
	}
	
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
	
	public static Object getExecute(Connection conn,Call<Object> callback)
	{
		Object obj=null;
		try
		{
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			boolean autoCommit=conn.getAutoCommit();
			conn.setAutoCommit(false);
		 	obj=callback.exec();
		 	conn.commit();
		 	conn.setAutoCommit(autoCommit);
		} catch (SQLException e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback successfully!");
			} catch (SQLException e1)
			{
				new JohnException(e1, "回滚事务异常！");
			}
			new JohnException(e, "SQL语句执行异常！");
		}
		finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				new JohnException(e, "连接关闭异常！");
			}
		}
		return obj;
	}
	
	public abstract static class Call<T>
	{
		public abstract T exec() throws SQLException;
	}
	
}
