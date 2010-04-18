package com.somesky.llk;

public class Score implements Comparable, java.io.Serializable
{
	private static final long serialVersionUID = -7722692065261144129L;
	
	public String name;
	public int score;
	
	public Score(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public int compareTo(Object a)
	{
		return score - ((Score)a).score;
	}
}
