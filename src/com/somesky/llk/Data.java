package com.somesky.llk;

public interface Data
{
	public final int LEN = 40;
	public final int MAXX = 18;
	public final int MAXY = 11;
	
	public final int UP = 70;
	public final int DOWN = UP + LEN * MAXY;
	public final int LEFT = 25;
	public final int RIGHT = LEFT + LEN * MAXX;
	
	public final int TIME_RECT_X = 150;
	public final int TIME_RECT_Y = 50;
	public final int TIME_RECT_WIDTH = 400;
	public final int TIME_RECT_HEIGHT = 15;
	
	public final int GRADETIMES1 = 150;
	public final int GRADETIMES2 = 140;
	public final int GRADETIMES3 = 130;
	public final int GRADETIMES4 = 120;
	public final int GRADETIMES5 = 110;
	
	public final int IMAGESNUM = 23;
	
	public final int INIT_STATE = 1;
	public final int RUN_STATE = 2;
	public final int PAUSE_STATE = 3;
	public final int OVER_STATE = 4;
	
	public final String RECORDFILENAME = "linklook.dat";
	
	
}
