package com.johnsoft.library.util.data;

/**
 * 封装特定单元格索引的类
 * @author 李哲浩
 */
public class JohnCell
{
	public  int row;
	public  int col;
	
	public JohnCell(){	
	}
	
	public JohnCell(int row,int col)
	{	
		this.row=row;
		this.col=col;
	}
	
	public JohnCell(String row,String col)
	{
		this.row=new Integer(row);
		this.col=new Integer(col);
	}

	@Override
	public String toString()
	{
		return "[ row="+row+", col="+col+" ]";
	}
	
	@Override  
	public int hashCode()
	{  
		int result=17;
		result=37*result+row;
		result=37*result+col;
		return result;
	}  
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof JohnCell)
		{
			JohnCell c=(JohnCell)obj;
			if(c.row==this.row&&c.col==this.col)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(int row,int col)
	{
		if(row==this.row&&col==this.col)
		{
			return true;
		}
		return false;
	}

}
