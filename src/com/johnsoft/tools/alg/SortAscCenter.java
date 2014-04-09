package com.johnsoft.tools.alg;

import java.util.Arrays;
import java.util.Random;

public class SortAscCenter
{
	public static void main(String[] args)
	{
		Random rand = new Random();
		int[] a = new int[rand.nextInt(20)];
		for(int i = 0; i < a.length; ++i)
		{
			a[i] = rand.nextInt(100);
		}
		System.out.println(Arrays.toString(a));
//		bubbleSortAsc(a, a.length);
//		insertSortAsc(a, a.length);
//		selectSortAsc(a, a.length);
//		heapSortAsc(a, a.length);
//		quickSortAsc(a, a.length);
		mergeSortAsc(a, a.length);
		System.out.println(Arrays.toString(a));
	}

	public static final void bubbleSortAsc(int[] arr, int len)
	{
		for (int i = 0; i < len; ++i)
		{
			for (int j = len - 1; j > i; --j)
			{
				if (arr[j - 1] > arr[j])
					swap(arr, j, j - 1);
			}
		}
	}

	public static final void insertSortAsc(int[] arr, int len)
	{
		for (int i = 0; i < len; ++i)
		{
			for (int j = i; j > 0; --j)
			{
				if (arr[j - 1] > arr[j])
					swap(arr, j, j - 1);
			}
		}
	}

	public static final void selectSortAsc(int[] arr, int len)
	{
		for(int i = 0; i < len; ++i)
		{
			int m = i;
			for(int j = len - 1; j > i; --j)
			{
				if(arr[j] < arr[m])
					m = j;
			}
			swap(arr, m, i);
		}
	}
	
	public static final void heapSortAsc(int[] arr, int len)
	{
		for (int parentIdx = len / 2 - 1; parentIdx >= 0; --parentIdx)
			adjustHeap(arr, parentIdx, len - 1);
		for (int i = len - 1; i > 0; --i)
		{
			swap(arr, 0, i);
			adjustHeap(arr, 0, i - 1);
		}
	}
	
	public static final void quickSortAsc(int[] arr, int len)
	{
		doQuick(arr, 0, len - 1);
	}
	
	public static final void mergeSortAsc(int[] arr, int len)
	{
		split(arr, 0, len - 1);
	}
	
	private static final void swap(int[] arr, int i, int j)
	{
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	private static final void adjustHeap(int[] arr, int low, int high)
	{
		int parentVal = arr[low];
		for (int childIdx = 2 * low + 1; childIdx <= high; childIdx = 2 * low + 1)
		{
			if (childIdx < high && arr[childIdx] < arr[childIdx + 1])
				++childIdx;
			if (arr[childIdx] < parentVal)
				break;
			arr[low] = arr[childIdx];
			low = childIdx;
		}
		arr[low] = parentVal;
	}
	
	private static final void doQuick(int[] arr, int low, int high)
	{
		if(low < high)
		{
			int pa = partition(arr, low, high);
			doQuick(arr, low, pa - 1);
			doQuick(arr, pa + 1, high);
		}
	}

	private static final int partition(int[] arr, int low, int high)
	{
		int pivot = arr[low];
		while(low < high)
		{
			while(low < high && arr[high] >= pivot)
				--high;
			arr[low] = arr[high];
			while(low < high && arr[low] <= pivot)
				++low;
			arr[high] = arr[low];
		}
		arr[low] = pivot;
		return low;
	}
	
	private static final void split(int[] arr, int low, int high)
	{
		if(low < high)
		{
			int mid = (low + high) / 2;
			split(arr, low, mid);
			split(arr, mid + 1, high);
			doMerge(arr, low, mid, high);
		}
	}

	private static final void doMerge(int[] arr, int low, int mid, int high)
	{
		int tempLen = high - low + 1;
		int[] temp = new int[tempLen];
		int firstIdx = low;
		int secondIdx = mid + 1;
		int tempIdx = 0;
		while(firstIdx <= mid && secondIdx <= high)
		{
			if(arr[firstIdx] < arr[secondIdx])
				temp[tempIdx++] = arr[firstIdx++];
			else
				temp[tempIdx++] = arr[secondIdx++];
		}
		while(firstIdx <= mid)
			temp[tempIdx++] = arr[firstIdx++];
		while(secondIdx <= high)
			temp[tempIdx++] = arr[secondIdx++];
		for(int i = 0; i < tempLen; ++i)
			arr[low + i] = temp[i];
	}
}
