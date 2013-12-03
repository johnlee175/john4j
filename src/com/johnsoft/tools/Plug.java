package com.johnsoft.tools;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class Plug
{
	public static void main(String[] args)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(new File("E:\\a.txt")));
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File("E:\\b.txt"),true));
			String line=null;
			while((line=br.readLine())!=null)
			{
				if(line.trim().isEmpty())
				{
					bw.write("\n");
					continue;
				}
				int from=line.indexOf("\"",0)+1;
				int to=line.indexOf("\"",from);
				if(to<from)
				{
					bw.write("\n");
					continue;
				}
				bw.write(line.substring(from, to)+"\n");
			}
			bw.close();
			br.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
