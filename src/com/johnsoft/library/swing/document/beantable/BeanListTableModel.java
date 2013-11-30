package com.johnsoft.library.swing.document.beantable;

import static java.util.Locale.ENGLISH;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * 支持POJO model类的List作为JTable的model
 * 对Object的要求：必须是javaBean，提供readMethod和默认构造器，并使用TableColumn注解标明属性指代的列
 * @author john
 */
@SuppressWarnings("rawtypes")
public class BeanListTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = -1167935370002797620L;
	
	protected boolean isCellEditable;
	
	protected Class clazz;
	
	protected List data=new ArrayList();
	
	protected List columnInfo=new ArrayList();
	
	public BeanListTableModel(Class clazz)
	{
		initHeaderColumnData(clazz);
	}
	
	public BeanListTableModel(Class clazz,List data)
	{
		initHeaderColumnData(clazz);
		if(data!=null)
		{
			this.data=data;
		}
	}
	
	public BeanListTableModel(Class clazz,int rowCount)
	{
		initHeaderColumnData(clazz);
		fillDefaultRows(rowCount);
	}
	
	protected void initHeaderColumnData(Class clazz)
	{
		if(clazz==null)
		{
			throw new IllegalArgumentException("class type can not be null!");
		}
		this.clazz=clazz;
		fillNullToSetSize();
		parseClass();
	}
	
	@SuppressWarnings("unchecked")
	protected void fillDefaultRows(int rowCount)
	{
		try
		{
			for(int i=0;i<rowCount;i++)
			{
					data.add(clazz.newInstance());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	@SuppressWarnings("unchecked")
	protected void  fillNullToSetSize()
	{
		Field[] fields=clazz.getDeclaredFields();
		int size=0;
		for(Field f:fields)
		{
			f.setAccessible(true);
			if(f.isAnnotationPresent(TableColumn.class))
			{
				size++;
			}
		}
		for(int i=0;i<size;i++)
		{
			columnInfo.add(new ColumnInfo());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void parseClass()
	{
		Field[] fields=clazz.getDeclaredFields();
		for(Field f:fields)
		{
			f.setAccessible(true);
			if(f.isAnnotationPresent(TableColumn.class))
			{
				TableColumn anno=f.getAnnotation(TableColumn.class);
				int index=anno.value();
				String name=anno.name();
				String attr=f.getName();
				Method getter=null,setter=null;
				try
				{
					String attrName=attr.substring(0, 1).toUpperCase(ENGLISH) + attr.substring(1);
					if(f.getType().isAssignableFrom(boolean.class))
					{
						getter=clazz.getMethod("is"+attrName);
					}
					if(getter==null)
					{
						getter=clazz.getMethod("get"+attrName);
					}
					if(getter==null)
					{
						throw new NullPointerException("expact getter but get null when reflect attribute");
					}
					setter=clazz.getMethod("set"+attrName, f.getType());
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				ColumnInfo info=new ColumnInfo(name,getter,setter);
				columnInfo.set(index, info);
			}
		}
	}

	//未实现方法实现
	
	@Override
	public int getColumnCount()
	{
		return columnInfo==null?0:columnInfo.size();
	}

	@Override
	public int getRowCount()
	{
		return data==null?0:data.size();
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		if(data!=null&&row<getRowCount()&&column<getColumnCount()&&row>=0&&column>=0)
		{
			ColumnInfo info=(ColumnInfo)columnInfo.get(column);
			Object obj=data.get(row);
			if(info!=null&&obj!=null)
			{
				Method method=info.getGetter();
				if(method!=null)
				{
					try
					{
						return method.invoke(obj);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	//已实现方法覆写
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		if(data!=null&&rowIndex<getRowCount()&&columnIndex<getColumnCount()&&rowIndex>=0&&columnIndex>=0)
		{
			ColumnInfo info=((ColumnInfo)columnInfo.get(columnIndex));
			if(info!=null)
			{
				Method method=info.getSetter();
				if(method==null)
				{
					throw new RuntimeException("not setter for attribute which been setting value");
				}
				Object obj=data.get(rowIndex);
				if(obj!=null)
				{
					try
					{
						method.invoke(obj, aValue);
						fireTableCellUpdated(rowIndex, columnIndex);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Override
	public String getColumnName(int column)
	{
		if(column<columnInfo.size()&&column>=0)
		{
			String name=null;
			ColumnInfo info=(ColumnInfo) columnInfo.get(column);
			if(info!=null)
			{
				name=info.getColumnName();
			}
			return (name == null) ? super.getColumnName(column) : name;
		}
		return null;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex<columnInfo.size()&&columnIndex>=0)
		{
			ColumnInfo info=(ColumnInfo) columnInfo.get(columnIndex);
			if(info!=null)
			{
				Method method=info.getGetter();
				if(method!=null)
				{
					return method.getReturnType();
				}
			}
		}
		return Object.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return isCellEditable;
	}
	
	//新添方法
	
	public void setCellEditable(boolean isCellEditable)
	{
		this.isCellEditable=isCellEditable;
	}

	public List getData()
	{
		return data;
	}
	
	public void setData(List data)
	{
		this.data = data;
		fireTableDataChanged();
	}
	
	//对行操作
	
	@SuppressWarnings("unchecked")
	public void addRow(Object obj)
	{
		if(obj!=null)
		{
			if(!clazz.isAssignableFrom(obj.getClass()))
			{
				throw new IllegalArgumentException("the class of param is not matched");
			}
			int oldRowCount=getRowCount();
			data.add(obj);
			fireTableRowsInserted(oldRowCount, oldRowCount);
		}
	}

	@SuppressWarnings("unchecked")
	public void insertRow(int row, Object obj)
	{
		if(obj!=null&&row<getRowCount()&&row>=0)
		{
			if(!clazz.isAssignableFrom(obj.getClass()))
			{
				throw new IllegalArgumentException("the class of param is not matched");
			}
			data.add(row,obj);
			fireTableRowsInserted(row, row);
		}
	}
	
	public void removeRow(int row)
	{
		if(row<getRowCount()&&row>=0)
		{
			data.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}
	
	//快速辅助多行处理
	
	public void addBlankRows(int rowCount)
	{
		try
		{
			for(int i=0;i<rowCount;i++)
			{
				addRow(clazz.newInstance());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertBlankRows(int from,int rowCount)
	{
		try
		{
			for(int i=0;i<rowCount;i++)
			{
				insertRow(from+i,clazz.newInstance());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertBlankRows(int[] rowIndexs)
	{
		try
		{
			for(int row:rowIndexs)
			{
				insertRow(row,clazz.newInstance());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void removeRows(int from,int to)
	{
		for(int i=from;i<=to;i++)
		{
			removeRow(i);
		}
	}
	
	public void removeRows(int[] rowIndexs)
	{
		for(int row:rowIndexs)
		{
			removeRow(row);
		}
	}
	
	public void addRows(Object...objs)
	{
		for(Object obj:objs)
		{
			addRow(obj);
		}
	}
	
	public void addRows(List list)
	{
		for(Object obj:list)
		{
			addRow(obj);
		}
	}
	
	public void insertRows(int from,Object...objs)
	{
		for(int i=0;i<objs.length;i++)
		{
			insertRow(from+i,objs[i]);
		}
	}
	
	public void insertRows(int from,List list)
	{
		for(int i=0;i<list.size();i++)
		{
			insertRow(from+i,list.get(i));
		}
	}
	
	public void insertRows(int[] rowIndexs,Object[] objs)
	{
		if(rowIndexs.length!=objs.length)
		{
			throw new RuntimeException("the length of rowIndexs  must be equal to the length of objs");
		}
		for(int i=0;i<rowIndexs.length;i++)
		{
			insertRow(rowIndexs[i],objs[i]);
		}
	}

	//单位列数据结构
	
	protected class ColumnInfo
	{
		private String columnName;
		private Method getter;
		private Method setter;
		
		public ColumnInfo(){}
		
		public ColumnInfo(String columnName,Method getter,Method setter)
		{
			this.columnName=columnName;
			this.getter=getter;
			this.setter=setter;
		}
		
		public String getColumnName()
		{
			return columnName;
		}
		public void setColumnName(String columnName)
		{
			this.columnName = columnName;
		}
		public Method getGetter()
		{
			return getter;
		}
		public void setGetter(Method getter)
		{
			this.getter = getter;
		}
		public Method getSetter()
		{
			return setter;
		}
		public void setSetter(Method setter)
		{
			this.setter = setter;
		}
	}

}
