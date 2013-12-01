package com.johnsoft.library.swing.component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JohnJDBCTable 
{
	private Connection conn;
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	
	private JTable jTable;
	private JScrollPane jScrollPane;
	
	public JohnJDBCTable(String jdbcType,String url,String username,String password)
	{
		try
		{
			if(jdbcType.equals("oracle"))
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn=DriverManager.getConnection("jdbc:oracle:thin:@"+url, username, password);
			}else{
				Class.forName("com.mysql.jdbc.Driver");
				conn=DriverManager.getConnection("jdbc:mysql://"+url, username, password);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		jTable=new JTable(){
			private static final long serialVersionUID = 1L;
			@Override
			public void setValueAt(Object aValue, int row, int column)
			{
				super.setValueAt(aValue, row, column);
				this.getModel().setValueAt(aValue, row, column);
				try
				{
					if(rsmd.getColumnTypeName(column+1).toLowerCase().indexOf("char")>=0)
					{
						rs.absolute(row+1);
						rs.updateString(rsmd.getColumnName(column+1), aValue.toString());
						rs.updateRow();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jScrollPane=new JScrollPane(jTable);
	}
	
	public void getResultSet(String tableName)
	{
		try
		{
			StringBuilder sb=new StringBuilder("SELECT ");
			ResultSet rsTemp=conn.getMetaData().getColumns(null, null, tableName.toUpperCase(), null);
			while(rsTemp.next())
			{
				sb.append(rsTemp.getString(4)).append(",");
			}
			rsTemp.close();
			sb.deleteCharAt(sb.length()-1);
			sb.append(" FROM ").append(tableName.toUpperCase());
			PreparedStatement ps=conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs=ps.executeQuery();
			rsmd=rs.getMetaData();
			rs.last();
			int rowCount=rs.getRow();
			Object[][] data=new Object[rowCount][rsmd.getColumnCount()];
			Object[] columnNames=new Object[rsmd.getColumnCount()];
			rs.beforeFirst();
			int count=0;
			while(rs.next())
			{
				for(int i=0;i<rsmd.getColumnCount();i++)
				{
					data[count][i]=rs.getString(i+1);
					columnNames[i]=rsmd.getColumnName(i+1);
				}
				count++;
			}
			jTable.setModel(new DefaultTableModel(data, columnNames));
		  
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean insertAll(String sqls)
	{
		String[] sql=sqls.split(";\n");
		for(String s:sql)
		{
			try
			{
				Statement state=conn.createStatement();
				state.executeUpdate(s);
				state.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public void searchDataType(JTable table,String tableName)
	{
		try
		{
			Statement state=conn.createStatement();
			ResultSet rs=state.executeQuery("SELECT * FROM "+tableName.toUpperCase()+" WHERE 1=2 ");
			ResultSetMetaData rsmd=rs.getMetaData();
			
			String[] typeNames=new String[rsmd.getColumnCount()];
			ResultSet rsc=conn.getMetaData().getColumns(null, null, tableName.toUpperCase(), null);
			int x=0;
			while(rsc.next())
			{
				typeNames[x]=rsc.getString("TYPE_NAME");
				x++;
			}
			
			for(int i=0;i<rsmd.getColumnCount();i++)
			{
				String type=typeNames[i];
				if(type.toLowerCase().indexOf("char")>=0||type.toLowerCase().indexOf("num")>=0)
				{
					type=type+"("+rsmd.getPrecision(i+1);
					if(type.toLowerCase().indexOf("num")>=0)
					{
						type=type+","+rsmd.getScale(i+1);
					}
					type=type+")";
				}
				table.setValueAt(type, 0, i);
				table.setValueAt(rsmd.getColumnName(i+1), 1, i);
		  }
			rs.close();
			rsc.close();
			state.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
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
					jTable.setValueAt(i+1,i,0);
					jTable.setValueAt(rs.getString(3),i,1);
					i++;
				}
			
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public JTable getJTable()
	{
		return jTable;
	}
	
	public JScrollPane getJScrollPane()
	{
		return jScrollPane;
	}
	
}
