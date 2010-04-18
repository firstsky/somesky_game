package com.somesky.llk;

public class MyArray implements java.io.Serializable
{
	private static final long serialVersionUID = -3217969837620490778L;
	
	public Score[] array;
	public int size;
	public final int MAX_LENGTH = 10;
	
	public MyArray()
	{
		array = new Score[MAX_LENGTH];
		size = 0;
	}
	
	public void add(Score score)
	{
		if(isEmpty())
		{
			array[0] = score;
			size++;
			return;
		}
		
		if(isFull())
		{
			if(array[MAX_LENGTH - 1].compareTo(score) < 0)
			{
				array[MAX_LENGTH - 1] = score;
				sort();
				return;
			}
			
		}
		
		array[size] = score;
		size++;
		sort();
	}
	
	private void sort()
	{
		int index = size - 1;
		int leftIndex = index - 1;
		Score temp = array[index];
		while(leftIndex >= 0 && array[leftIndex].compareTo(temp) < 0)
		{
			array[index] = array[leftIndex];
			index = leftIndex;
			leftIndex = index - 1;
		}
		
		array[index] = temp;
	}
	
	public Score getTair()
	{
		Score score = null;
		if(!isEmpty())
		{
			score = array[size - 1];
		}
		
		return score;
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	public boolean isFull()
	{
		return size == MAX_LENGTH;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < size; i++)
		{
			buffer.append((i + 1) + "  " + array[i].name + "  " + array[i].score + "\n");
		}
		
		return buffer.toString();
	}
}


