package com.somesky.llk;

import java.awt.Point;
import java.io.File;

public class ToolClass implements Data
{
	public static void main(String[] args)
	{
		int[] images = imageIDArray();
		
		for(int i = 0; i < images.length; i++)
		{
			System.out.println(images[i]);
		}
	}
	
	public static boolean allImagesExist()
	{
		boolean result = true;
		for(int i = 1; i <= IMAGESNUM; i++)
		{
			File file = new File("image/" + i + ".jpg");
			result = file.exists();
			if(!result)
			{
				break;
			}
		}
		return result;
	}
	
	public static Point getActualCoordinate(Point p)
	{
		int x = LEFT + LEN * p.x;
		int y = UP + LEN * p.y;
		return new Point(x, y);
	}
	
	public static Point getVritualCoordinate(Point p)
	{
		int x = (p.x - LEFT) / LEN;
		int y = (p.y - UP) / LEN;
		return new Point(x, y);
	}
	
	public static int[] imageIDArray()
	{
		int[] images = new int[ARRAY_LEN];
		int[] random = new int[ARRAY_LEN];
		for(int index = 0, imageId = 1; index < images.length / 2; index++)
		{
			images[index] = imageId;
			images[images.length - index - 1] = imageId;
			imageId++;
			if(imageId >= IMAGESNUM + 1)
			{
				imageId = 1;
			}
		}
		
		for(int i = 0, lastIndex = images.length - 1; i < images.length && lastIndex >= 0; i++, lastIndex--)
		{
			
			int rand = (int)(Math.random() * (lastIndex + 1));
			random[i] = images[rand];
			images[rand] = images[lastIndex];
		}
		
		return random;
	}
	
	
	//Task: 将map数组元素尽可能的向上移动
	public static void moveElementUp(int map[][])
	{
		for(int y = minYIndex + 1; y <= maxYIndex; y++)
		{
			for(int x = minXIndex; x <= maxXIndex; x++)
			{
				for(int m = y; m >= minYIndex + 1; m--)
				{
					if(map[x][m - 1] == 0)
					{
						map[x][m - 1] = map[x][m];
						map[x][m] = 0;
					}
				}
			}
		}
		
	}
	
	//Task: 将map数组元素尽可能向下移动
	public static void moveElementDown(int map[][])
	{
		for(int y = maxYIndex - 1; y >= minYIndex; y--)
		{
			for(int x = minXIndex; x <= maxXIndex; x++)
			{
				for(int m = y; m <= maxYIndex - 1; m++)
				{
					if(map[x][m + 1] == 0)
					{
						map[x][m + 1] = map[x][m];
						map[x][m] = 0;
					}
				}
			}
		}
	}
	
	//Task: 将map数组元素尽可能向左移动
	public static void moveElementLeft(int map[][])
	{
		for(int x = minXIndex + 1; x <= maxXIndex; x++)
		{
			for(int y = minYIndex; y <= maxYIndex; y++)
			{
				for(int m = x; m >= minXIndex + 1; m--)
				{
					if(map[m - 1][y] == 0)
					{
						map[m - 1][y] = map[m][y];
						map[m][y] = 0;
					}
				}
			}
		}
	}
	
	//Task: 将map数组元素尽可能向右移动
	public static void moveElementRight(int map[][])
	{
		for(int x = maxXIndex - 1; x >= minXIndex; x--)
		{
			for(int y = minYIndex; y <= maxYIndex; y++)
			{
				for(int m = x; m <= maxXIndex - 1; m++)
				{
					if(map[m + 1][y] == 0)
					{
						map[m + 1][y] = map[m][y];
						map[m][y] = 0;
					}
				}
			}
		}
	}
	
	private static int minXIndex = 1;
	private static int maxXIndex = MAXX - 2;
	private static int minYIndex = 1;
	private static int maxYIndex = MAXY - 2;
	private static int ARRAY_LEN = (MAXX - 2) * (MAXY - 2);
}
