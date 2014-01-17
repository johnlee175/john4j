package com.johnsoft.tools;


public class RegexTester
{
	public static final String REGEX="[0-9]+";//改成你要测试的正则表达式
	
	public static void main(String[] args) throws Exception
	{
		while(true)
		{
			System.out.print("please input:");
			byte[] bytes=new byte[1024];
			int len=System.in.read(bytes);
			String str=new String(bytes,0,len);
			if(str.endsWith("\r\n"))
			{
				str=str.substring(0, str.length()-2);
			}
			else if(str.endsWith("\n")||str.endsWith("\r"))
			{
				str=str.substring(0, str.length()-1);
			}
			System.out.println(">>>"+str.matches(REGEX));
		}
	}
}
