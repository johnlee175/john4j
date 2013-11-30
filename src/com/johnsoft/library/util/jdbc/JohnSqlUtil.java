package com.johnsoft.library.util.jdbc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JohnSqlUtil
{
	private Connection conn;
	private static JohnSqlUtil johnSqlUtil = new JohnSqlUtil();

	private JohnSqlUtil()
	{
		try
		{
			conn = JohnJDBCUtil.getConnection("Resource/database.properties");
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			conn.setAutoCommit(false);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static JohnSqlUtil getJohnSqlUtil()
	{
		return johnSqlUtil;
	}

	public void distroy()
	{
		try
		{
			conn.close();
			johnSqlUtil = null;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private void checkFieldAndColumnName(PreparedStatement ps, Object t,
			List<String> columnNames, int j) throws Exception
	{
		Field[] fields = t.getClass().getDeclaredFields();
		for (int i = 0; i < columnNames.size(); i++)
		{
			String columnName = columnNames.get(i).toLowerCase();
			for (Field f : fields)
			{
				String fieldName = f.getName().toLowerCase();
				if (fieldName.equals(columnName))
				{
					Class<?> type = f.getType();
					f.setAccessible(true);
					Object obj = f.get(t);
					if (obj == null)
					{
						ps.setNull(i + 1 + j, Types.NULL);
						break;
					}
					setParam(ps, type, obj, i + j);
					break;
				}
			}
		}
	}

	private void setParam(PreparedStatement ps, Class<?> type, Object obj, int i)
			throws SQLException
	{
		if (type == String.class)
		{
			ps.setString(i + 1, (String) obj);
			return;
		} else if (type == int.class || type == Integer.class)
		{
			ps.setInt(i + 1, (Integer) obj);
			return;
		} else if (type == double.class || type == Double.class)
		{
			ps.setDouble(i + 1, (Double) obj);
			return;
		} else if (type == Date.class)
		{
			ps.setDate(i + 1, new java.sql.Date(((Date) obj).getTime()));
			return;
		} else if (type == BigDecimal.class)
		{
			ps.setBigDecimal(i + 1, (BigDecimal) obj);
			return;
		} else if (type == long.class || type == Long.class)
		{
			ps.setLong(i + 1, (Long) obj);
			return;
		} else if (type == boolean.class || type == Boolean.class)
		{
			ps.setBoolean(i + 1, (Boolean) obj);
			return;
		} else
		{
			ps.setNull(i + 1, Types.NULL);
		}
	}

	private Object getParam(ResultSet rs, Class<?> type, String columnName)
			throws SQLException
	{
		if (type == String.class)
		{
			return rs.getString(columnName);
		} else if (type == int.class || type == Integer.class)
		{
			return rs.getInt(columnName);
		} else if (type == double.class || type == Double.class)
		{
			return rs.getDouble(columnName);
		} else if (type == Date.class)
		{
			return rs.getDate(columnName);
		} else if (type == BigDecimal.class)
		{
			return rs.getBigDecimal(columnName);
		} else if (type == long.class || type == Long.class)
		{
			return rs.getLong(columnName);
		} else if (type == boolean.class || type == Boolean.class)
		{
			return rs.getBoolean(columnName);
		} else
		{
			return null;
		}
	}

	private ResultSet search(String sql, Object... objs) throws SQLException
	{
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 0; i < objs.length; i++)
		{
			if (objs[i] == null)
			{
				continue;
			} else
			{
				Object obj = objs[i];
				Class<?> type = obj.getClass();
				setParam(ps, type, obj, i);
			}
		}
		ResultSet rs = ps.executeQuery();
		return rs;
	}

	public List<String> getColumnNames(String tableName)
	{
		try
		{
			List<String> list = new ArrayList<String>();
			tableName = tableName.toUpperCase();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getColumns(null, null, tableName, null);
			while (rs.next())
			{
				list.add(rs.getString("COLUMN_NAME"));
			}
			return list;
		} catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getPrimaryKeys(String tableName)
	{
		try
		{
			List<String> list = new ArrayList<String>();
			tableName = tableName.toUpperCase();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getPrimaryKeys(null, null, tableName);
			while (rs.next())
			{
				list.add(rs.getString("COLUMN_NAME"));
			}
			return list;
		} catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public <T> int insert(T t, String tableName)
	{
		try
		{
			List<String> columnNames = getColumnNames(tableName);
			StringBuilder sql = new StringBuilder("INSERT INTO ");
			sql.append(tableName.toUpperCase()).append(" VALUES(");
			for (int i = 0; i < columnNames.size(); i++)
			{
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1).append(")");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			checkFieldAndColumnName(ps, t, columnNames, 0);
			ps.executeUpdate();
			ps.close();
			conn.commit();
			return 1;
		} catch (Exception e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return 0;
	}

	public <T> int delete(T t, String tableName)
	{
		try
		{
			List<String> keys = getPrimaryKeys(tableName);
			if(keys.size()==0)
			{
				keys.add(getColumnNames(tableName).get(0));
			}
			StringBuilder sql = new StringBuilder("DELETE FROM ");
			sql.append(tableName.toUpperCase()).append(" WHERE ");
			for (String key : keys)
			{
				sql.append(key).append("=? AND ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			checkFieldAndColumnName(ps, t, keys, 0);
			ps.executeUpdate();
			ps.close();
			conn.commit();
			return 1;
		} catch (Exception e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return 0;
	}

	public <T> int update(T t, String tableName)
	{
		try
		{
			List<String> columnNames = getColumnNames(tableName);
			List<String> keys = getPrimaryKeys(tableName);
			if(keys.size()==0)
			{
				keys.add(columnNames.get(0));
			}
			StringBuilder sql = new StringBuilder("UPDATE ");
			sql.append(tableName.toUpperCase()).append(" SET ");
			for (String columnName : columnNames)
			{
				sql.append(columnName).append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1).append(" WHERE ");
			for (String key : keys)
			{
				sql.append(key).append("=? AND ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			checkFieldAndColumnName(ps, t, columnNames, 0);
			checkFieldAndColumnName(ps, t, keys, columnNames.size());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			return 1;
		} catch (Exception e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return 0;
	}
	
	public <T> List<T> find(String sql, Class<T> clazz, Object... objs)
	{
		try
		{
			ResultSet rs = search(sql, objs);
			ResultSetMetaData meta = rs.getMetaData();
			List<T> list = new ArrayList<T>();
			while (rs.next())
			{
				T t = clazz.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < meta.getColumnCount(); i++)
				{
					String columnName = meta.getColumnName(i + 1).toLowerCase();
					for (Field field : fields)
					{
						String fieldName = field.getName().toLowerCase();
						if (columnName.equals(fieldName))
						{
							Class<?> type = field.getType();
							field.setAccessible(true);
							Object param = getParam(rs, type, columnName);
							field.set(t, param);
							break;
						}
					}
				}
				list.add(t);
			}
			return list;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findColumn(String sql,Class<T> clazz, Object... objs)
	{
		try
		{
			ResultSet rs = search(sql, objs);
			ResultSetMetaData meta = rs.getMetaData();
			List<T> list = new ArrayList<T>();
			while (rs.next())
			{
					String name = meta.getColumnName(1).toLowerCase();
				  list.add((T)getParam(rs, clazz, name));
			}
			return list;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public int findCount(String sql, Object... objs)
	{
		int count = 0;
		try
		{
			ResultSet rs = search(sql, objs);
			while (rs.next())
			{
				count = rs.getInt(1);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return count;
	}

	public <T> T findOne(String sql, Class<T> clazz, Object... objs)
	{
		List<T> list = find(sql, clazz, objs);
		return list.get(0);
	}
	
	public int exeUpdate(String sql,Object...objs)
	{
		try
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			for(int i=0;i<objs.length;i++)
			{
				setParam(ps, objs[i].getClass(), objs[i], i);
			}
			int re=ps.executeUpdate();
			ps.close();
			conn.commit();
			return re;
		} catch (SQLException e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return 0;
	}

	public int[] exeUpdateAll(String sql,Object[][] objs)
	{
		try
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.clearBatch();
			for(int i=0;i<objs.length;i++)
			{
				for(int j=0;j<objs[i].length;j++)
				{
					setParam(ps, objs[i][j].getClass(), objs[i][j], j);
				}
				ps.addBatch();
			}
			int[] ints=ps.executeBatch();
			ps.close();
			conn.commit();
			return ints;
		} catch (SQLException e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return new int[0];
	}
	
	public <T> int[] insertAll(List<T> list,String tableName)
	{
		try
		{
			List<String> columnNames = getColumnNames(tableName);
			StringBuilder sql = new StringBuilder("INSERT INTO ");
			sql.append(tableName.toUpperCase()).append(" VALUES(");
			for (int i = 0; i < columnNames.size(); i++)
			{
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1).append(")");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.clearBatch();
			for(T t:list)
			{
				checkFieldAndColumnName(ps, t, columnNames, 0);
				ps.addBatch();
			}
			int[] ints=ps.executeBatch();
			ps.close();
			conn.commit();
			return ints;
		} catch (Exception e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return new int[0];
	}
	
	public <T> int[] deleteAll(List<T> list,String tableName)
	{
		try
		{
			List<String> keys = getPrimaryKeys(tableName);
			if(keys.size()==0)
			{
				keys.add(getColumnNames(tableName).get(0));
			}
			StringBuilder sql = new StringBuilder("DELETE FROM ");
			sql.append(tableName.toUpperCase()).append(" WHERE ");
			for (String key : keys)
			{
				sql.append(key).append("=? AND ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.clearBatch();
			for(T t:list)
			{
				checkFieldAndColumnName(ps, t, keys, 0);
				ps.addBatch();
			}
			int[] ints=ps.executeBatch();
			ps.close();
			conn.commit();
			return ints;
		} catch (Exception e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return new int[0];
	}
	
	public <T> int[] updateAll(List<T> list,String tableName)
	{
		try
		{
			List<String> columnNames = getColumnNames(tableName);
			List<String> keys = getPrimaryKeys(tableName);
			if(keys.size()==0)
			{
				keys.add(columnNames.get(0));
			}
			StringBuilder sql = new StringBuilder("UPDATE ");
			sql.append(tableName.toUpperCase()).append(" SET ");
			for (String columnName : columnNames)
			{
				sql.append(columnName).append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1).append(" WHERE ");
			for (String key : keys)
			{
				sql.append(key).append("=? AND ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.clearBatch();
			for(T t:list)
			{
				checkFieldAndColumnName(ps, t, columnNames, 0);
				checkFieldAndColumnName(ps, t, keys, columnNames.size());
				ps.addBatch();
			}
			int[] ints=ps.executeBatch();
			ps.close();
			conn.commit();
			return ints;
		} catch (Exception e)
		{
			try
			{
				conn.rollback();
				System.out.println("rollback sucessfully!");
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return new int[0];
	}
	
	public int insert(TableEntity entity)
	{
		return insert(entity, entity.getTableName());
	}

	public int delete(TableEntity entity)
	{
		return delete(entity, entity.getTableName());
	}

	public int update(TableEntity entity)
	{
		return update(entity, entity.getTableName());
	}

	public interface TableEntity
	{
		public String getTableName();
	}

}
