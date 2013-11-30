package com.johnsoft.library.util.common;

/**执行位操作辅助类*/
public class JohnBitUtil
{
	public static final byte b00000001=1<<0;
	public static final byte b00000010=1<<1;
	public static final byte b00000100=1<<2;
	public static final byte b00001000=1<<3;
	public static final byte b00010000=1<<4;
	public static final byte b00100000=1<<5;
	public static final byte b01000000=1<<6;
	public static final byte b10000000=(byte) (1<<7);
	public static final short only9bit=1<<8;
	public static final short only10bit=1<<9;
	public static final short only11bit=1<<10;
	public static final short only12bit=1<<11;
	public static final short only13bit=1<<12;
	public static final short only14bit=1<<13;
	public static final short only15bit=1<<14;
	public static final short only16bit=(short) (1<<15);
	
	public static boolean get(int value,int bitIdx)
	{
		return (value & (1 << bitIdx)) == 0 ? false : true;
	}
	
	public static int set(int value,int bitIdx)
	{
		return value | (1 << bitIdx);
	}
	
	public static int clear(int value,int bitIdx)
	{
		return value & (~(1 << bitIdx));
	}
	
	public static int getInt(int value,int bitIdx)
	{
		return (value & (1 << bitIdx)) == 0 ? 0 : 1;
	}
	
	public static int clearAll(int value)
	{
		return value & 0;
	}
	
	public static int setAll(int value)
	{
		return value | -1;
	}
	
	public static String toString(int value)
	{
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<32;i++)
		{
			sb.append(getInt(value, i));
		}
		return sb.reverse().toString();
	}
	
	public static boolean get(short value,int bitIdx)
	{
		return (value & (1 << bitIdx)) == 0 ? false : true;
	}
	
	public static short set(short value,int bitIdx)
	{
		return (short) (value | (1 << bitIdx));
	}
	
	public static short clear(short value,int bitIdx)
	{
		return (short) (value & (~(1 << bitIdx)));
	}
	
	public static int getInt(short value,int bitIdx)
	{
		return (value & (1 << bitIdx)) == 0 ? 0 : 1;
	}
	
	public static short clearAll(short value)
	{
		return  0;
	}
	
	public static short setAll(short value)
	{
		return -1;
	}
	
	public static String toString(short value)
	{
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<16;i++)
		{
			sb.append(getInt(value, i));
		}
		return sb.reverse().toString();
	}
	
	public static boolean get(byte value,int bitIdx)
	{
		return (value & (1 << bitIdx)) == 0 ? false : true;
	}
	
	public static byte set(byte value,int bitIdx)
	{
		return (byte) (value | (1 << bitIdx));
	}
	
	public static byte clear(byte value,int bitIdx)
	{
		return (byte) (value & (~(1 << bitIdx)));
	}
	
	public static int getInt(byte value,int bitIdx)
	{
		return (value & (1 << bitIdx)) == 0 ? 0 : 1;
	}
	
	public static byte clearAll(byte value)
	{
		return 0;
	}
	
	public static byte setAll(byte value)
	{
		return -1;
	}
	
	public static String toString(byte value)
	{
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<8;i++)
		{
			sb.append(getInt(value, i));
		}
		return sb.reverse().toString();
	}
	
}
