package com.johnsoft.tools.alg;


public class SearchCenter
{
	public static final int binarySearch(int[] arr,int key) 
	{
		int low = 0;
		int high = arr.length - 1;
		while (low <= high) 
		{
			int mid = (low + high) / 2;
			int midVal = arr[mid];
			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else
				return mid;
		}
		return -(low + 1);
	}
	
	public static final int hash(String key, int size)
	{
		if(key == null)
			return 0;
		int hashValue = 0;
		for(int i = 0; i < key.length(); ++i)
			hashValue = 37 * hashValue + key.charAt(i);
		hashValue %= size;
		if(hashValue < 0)
			hashValue += size;
		return hashValue;
	}
	
	public static final class HashFactory
	{
		public static final long RSHash(String str)
		{
			int b = 378551;
			int a = 63689;
			long hash = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = hash * a + str.charAt(i);
				a = a * b;
			}
			return hash;
		}
		
		public static final long JSHash(String str)
		{
			long hash = 1315423911;
			for(int i = 0; i < str.length(); ++i)
			{
				hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
			}
			return hash;
		}
		
		public static final long PJWHash(String str)
		{
			long BitsInUnsignedInt = (long)(4 * 8);
			long ThreeQuarters = (long)((BitsInUnsignedInt * 3) / 4);
			long OneEighth = (long)(BitsInUnsignedInt / 8);
			long HighBits = (long)(0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
			long hash = 0;
			long test = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = (hash << OneEighth) + str.charAt(i);
				if((test = hash & HighBits) != 0)
				{
					hash = (( hash ^ (test >> ThreeQuarters)) & (~HighBits));
				}
			}
			return hash;
		}
		
		public static final long ELFHash(String str)
		{
			long hash = 0;
			long x = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = (hash << 4) + str.charAt(i);
				if((x = hash & 0xF0000000L) != 0)
				{
					hash ^= (x >> 24);
				}
				hash &= ~x;
			}
			return hash;
		}
		
		public static final long BKDRHash(String str)
		{
			long seed = 13131;
			long hash = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = (hash * seed) + str.charAt(i);
			}
			return hash;
		}
		
		public static final long SDBMHash(String str)
		{
			long hash = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
			}
			return hash;
		}
		
		public static final long DJBHash(String str)
		{
			long hash = 5381;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = ((hash << 5) + hash) + str.charAt(i);
			}
			return hash;
		}
		
		public static final long DEKHash(String str)
		{
			long hash = str.length();
			for(int i = 0; i < str.length(); ++i)
			{
				hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
			}
			return hash;
		}
		
		public static final long BPHash(String str)
		{
			long hash = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash = hash << 7 ^ str.charAt(i);
			}
			return hash;
		}
		
		public static final long FNVHash(String str)
		{
			long fnv_prime = 0x811C9DC5;
			long hash = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				hash *= fnv_prime;
				hash ^= str.charAt(i);
			}
			return hash;
		}
		
		public static final long APHash(String str)
		{
			long hash = 0xAAAAAAAA;
			for(int i = 0; i < str.length(); ++i)
			{
				if ((i & 1) == 0)
				{
					hash ^= ((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
				}
				else
				{
					hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
				}
			}
			return hash;
		}
	}
}
