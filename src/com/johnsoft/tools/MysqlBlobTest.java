package com.johnsoft.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.johnsoft.library.util.jdbc.JohnJDBCUtil;
import com.johnsoft.library.util.jdbc.JohnJDBCUtil.Call;

public class MysqlBlobTest
{
	public static void main(String[] args)
	{
		final Connection conn=JohnJDBCUtil.getConnection("Resource/database.properties");
	  
		JohnJDBCUtil.getExecute(conn, new Call<Object>()
		{
			@Override
			public Object exec() throws SQLException
			{
				StringBuilder sb=new StringBuilder("hello world ");
				for(int i=0;i<5000;i++)//注意：mysql的容量比oracle小，不能插入太大对象，i为6000时报错
				{
					sb.append("hello world ");
				}
				String str=sb.toString();
		    
				Integer id=3;
				Blob blob=conn.createBlob();
				OutputStream os=blob.setBinaryStream(1);
				InputStream is=new ByteArrayInputStream(str.getBytes());
				byte[] buf=new byte[1024];
			  int len;
			  try
				{
					while((len=is.read(buf))>0)
					{
						os.write(buf,0,len);
					}
					 os.close();
					 is.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				PreparedStatement ps=conn.prepareStatement("insert into myblob values(?,?)");
				ps.setInt(1, id);
				ps.setBlob(2, blob);
			  ps.executeUpdate();
			  ps.close();
			  System.out.println("插入成功！");
				return null;
			}
		});
		
	}

}
