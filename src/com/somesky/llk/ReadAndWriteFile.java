package com.somesky.llk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ReadAndWriteFile
{
	public static void main(String[] args)
	{
		getRecordPanel();
	}
	
	public static void write(MyArray array)
	{
		try
		{
			FileOutputStream fileOutput = new FileOutputStream(file);
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(array);
			objectOutput.close();
			fileOutput.close();
		}
		catch(Exception e)
		{}
	}
	
	public static MyArray read()
	{
		MyArray array = new MyArray();
		FileInputStream fileInput = null;
		ObjectInputStream objectInput = null;
		
		if(!file.exists())
		{
			return array;
		}
		
		try
		{
			fileInput = new FileInputStream(file);
			objectInput = new ObjectInputStream(fileInput);
			Object o = objectInput.readObject();
			array = (MyArray)o;
			objectInput.close();
			fileInput.close();
		}
		catch(Exception e)
		{
			try
			{
				objectInput.close();
				fileInput.close();
			}
			catch(Exception ee)
			{}
			
			
			String msg = "成绩记录文件被损坏！";
			JOptionPane.showMessageDialog(null, msg);
			file.delete();
		}
		
		return array;
	}
	
	public static JScrollPane getRecordPanel()
	{
		MyArray array = read();
		Object[][] data= new Object[array.size][3];
		for(int i = 0; i < array.size; i++)
		{
			Score score = array.array[i];
			data[i][0] = new Integer(i + 1).toString();
			data[i][1] = score.name;
			data[i][2] = new Integer(score.score).toString();
		}
		
		Object[] columnNames = new Object[3];
		columnNames[0] = "名次";
		columnNames[1] = "姓名";
		columnNames[2] = "成绩";
		
	    JTable table = new JTable(data, columnNames);
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    JScrollPane scrollpane = new JScrollPane(table);
	    return scrollpane;
	}
	
	public static void setPath(String path)
	{
		file = new File(path);
	}
	
	private static File file;
}
