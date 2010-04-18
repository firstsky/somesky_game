package com.somesky.llk;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Picture implements Data
{
	private static Image[] images = new Image[IMAGESNUM];
	private static Picture instance;
	
	private Picture() {
	}
	
	public static Picture getInstance() {
		if(instance == null) {
			instance = new Picture();
		}
		
		return instance;
	}
	
	public static void draw(Graphics g, int x, int y, int flag)
	{
		//String name = "image/" + flag + ".JPG";
		//File file = new File(name);
		//Image image = Toolkit.getDefaultToolkit().getImage(name);
		Point p = ToolClass.getActualCoordinate(new Point(x, y));
		g.drawImage(images[flag - 1], p.x, p.y, LEN, LEN, null);
	}
	
	private static void initImages() {
		
		Picture instance = Picture.getInstance();
		
		try {
			
			byte[] buf = new byte[1024];
			int len = -1;
			
			for(int i = 1; i <= IMAGESNUM; i++) {
				
				InputStream is = instance.getClass().getResourceAsStream("/com/somesky/images/" + i + ".JPG");
				ByteArrayOutputStream os = new ByteArrayOutputStream();   
				
		 		while( ( len = is.read(buf) ) > -1 ) {
		 			os.write(buf, 0, len);
		 		}
		 		
		 		images[i - 1] = Toolkit.getDefaultToolkit().createImage(os.toByteArray());
		 		
				is.close();
				os.close();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static {//≥ı ºªØ
		initImages();
	}
}
