package com.johnsoft.library.util.data;

/**
 * 原用于抽象特定单元格的类模板,也可通过将其中一个属性设置为null捆绑两个或直接捆绑三个相关联的值,可作为map的键值
 * @author 李哲浩
 * @param <V> 原单元格行列索引类型
 * @param <T> 原单元格数据类型
 */
public class JohnDataCell<V,T> 
{
	public V row;
	public V col;
	public T data;
	
	public JohnDataCell(){	
	}
	
	public JohnDataCell(V row,V col,T data)
	{	
		this.row=row;
		this.col=col;
		this.data=data;
	}
	
	@Override
	public String toString()
	{
		return "[ row="+row+", col="+col+", value="+data+" ]";
	}
	
	@Override  
	public int hashCode()
	{  
		int result=17;
		result=37*result+row.hashCode();
		result=37*result+col.hashCode();
		result=37*result+data.hashCode();
		return result;
	}  
	
	/**
	 * obj为null或其属性中有null或不是JohnDataCell的实例将返回false,三个属性值通过equals判断相等返回true
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof JohnDataCell)
		{
			JohnDataCell<V,T> c=(JohnDataCell<V,T>)obj;
			if(c==null||c.data==null||c.row==null||c.col==null)
			{
				return false;
			}
			if(c.row.equals(this.row)&&c.col.equals(this.col)&&c.data.equals(this.data))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 形参中有null返回false,三个属性值通过equals判断相等返回true
	 */
	public boolean equals(V row,V col, T data)
	{
		if(data==null||row==null||col==null)
		{
			return false;
		}
		if(row.equals(this.row)&&col.equals(this.col)&&data.equals(this.data))
		{
			return true;
		}
		return false;
	}
	
}
