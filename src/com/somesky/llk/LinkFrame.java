package com.somesky.llk;

import java.awt.Container;

import javax.swing.JFrame;

public class LinkFrame extends JFrame
{
	
	private static final long serialVersionUID = 8552625091118744641L;
	
	public LinkFrame()
	{
		setTitle("Á¬Á¬¿´");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		LinkPanel panel = new LinkPanel(this);
		Container container = getContentPane();
		container.add(panel);
	}
	
	
	private final int WIDTH = 778;
	private final int HEIGHT = 572;
}
