package com.johnsoft.library.util.data.adt;

public class LinkedItem<T>
{
	LinkedItem<T> head;
	LinkedItem<T> tail;
	T value;
	public LinkedItem(T value)
	{
		this.value = value;
	}
}
