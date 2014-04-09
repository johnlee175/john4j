package com.johnsoft.tools.alg;

public class CommonMath
{
	public static void main(String[] args)
	{
		int a = 11, b = 18;
		System.out.println(a + "和" + b + "的最大公约数是" + greatestCommonDivisor(a, b));
		System.out.println(a + "和" + b + "的最小公倍数是" + leastCommonMultiple(a, b));
	}
	
	/**最大公约数*/
	public static final int greatestCommonDivisor(int a, int b)
	{
		int temp;
		if(a < b)
		{
			temp = a; a = b; b = temp;
		}
		while(b != 0)
		{
			temp = a % b; a = b; b = temp;
		}
		return a;
	}
	
	/**最小公倍数*/
	public static final int leastCommonMultiple(int a, int b)
	{
		return a * b / greatestCommonDivisor(a, b);
	}
}
