package com.somesky.llk;



import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.somesky.laf.GreenMetalTheme;

public class QQLink {
	public static void main(String[] args) {
		setFeelAndLook();	
		
		LinkFrame frame = new LinkFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static void setFeelAndLook() {
		
		UIManager.put("swing.boldMetal", Boolean.FALSE);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground","true");

        try {
        	MetalLookAndFeel.setCurrentTheme(new GreenMetalTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        }  
        catch ( UnsupportedLookAndFeelException e ) {
            System.out.println ("Metal Look & Feel not supported on this platform. \nProgram Terminated");
            System.exit(0);
        }
	}

}
