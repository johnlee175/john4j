package com.johnsoft.library.swing.component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class JohnJDBCTable extends JTable
{
	private static final long serialVersionUID = 1L;
	
	private Connection conn=getConnection();
	
	public JohnJDBCTable()
	{
		DefaultTableModel dtm=new DefaultTableModel(2000, 30);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setModel(dtm);
		selectTableModel();
	}
	
	public Connection getConnection()
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
	
	public void selectTableData()
	{
		PreparedStatement ps;
		ResultSet rs;
		if(conn!=null)
		{
			try
			{
				ps=conn.prepareStatement("select * from adm_menus");
				rs=ps.executeQuery();
				int i=0;
				while(rs.next())
				{
					this.setValueAt(rs.getString(2),i,2);
					this.setValueAt(rs.getString(3),i,3);
					this.setValueAt(rs.getString(4),i,4);
					i++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void selectTables()
	{
		ResultSet rs;
		if(conn!=null)
		{
			try
			{
				DatabaseMetaData dmd=conn.getMetaData();
				rs=dmd.getTables(null, "WMSR5USR", "%", new String[]{"TABLE"});
				int i=0;
				while(rs.next())
				{
					this.setValueAt(i+1,i,0);
					this.setValueAt(rs.getString(3),i,1);
					i++;
				}
			
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void selectTableModel()
	{
		PreparedStatement ps;
		ResultSet rs;
		if(conn!=null)
		{
			try
			{
				ps=conn.prepareStatement("select * from adm_menus");
				rs=ps.executeQuery();
				int i=0;
				while(rs.getMetaData().getColumnCount()>i)
				{
					this.setValueAt(rs.getMetaData().getColumnName(i+1),i,0);
					this.setValueAt(rs.getMetaData().getColumnTypeName(i+1),i,1);
					i++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

}
