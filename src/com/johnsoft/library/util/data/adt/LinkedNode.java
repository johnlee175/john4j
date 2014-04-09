package com.johnsoft.library.util.data.adt;

public class LinkedNode<T>
{
	LinkedNode<T> left;
	LinkedNode<T> right;
	LinkedNode<T> parent;
	T value;
	public LinkedNode(T value)
	{
		this.value = value;
	}
}
