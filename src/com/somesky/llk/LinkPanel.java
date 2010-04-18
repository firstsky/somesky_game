package com.somesky.llk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;


public class LinkPanel extends JPanel implements Data
{
	private static final long serialVersionUID = 8305839864789505075L;
	
	public LinkPanel(JFrame frame)
	{
		this.frame = frame;
		setBackground(Color.WHITE);
		
		timer = new Timer(1000, new TimerAction());
		
		startButton = new JButton("开始");
		add(startButton);
		startButton.addActionListener(new StartAction());
		
		pauseButton = new JButton("暂停");
		add(pauseButton);
		pauseButton.addActionListener(new PauseAction());
		
		//提示下一步
		clewButton = new JButton("提示");
		add(clewButton);
		clewButton.addActionListener(new ClewNextAction());
		
		recordButton = new JButton("排行榜");
		add(recordButton);
		recordButton.addActionListener(new RecordAction());
		
		aboutButton = new JButton("关于");
		add(aboutButton);
		aboutButton.addActionListener(new AboutAction());
		
		map = new int[MAXX][MAXY];
		inflexionList = new ArrayList();
		addMouseListener(new MouseClickListener());
		addMouseMotionListener(new MouseMoveListener());
		
		init();
	}
	
	public void init()
	{
		initMap();
		times = GRADETIMES1;
		allTimesInGameGrade = GRADETIMES1;
		score = 0;
		gameState = INIT_STATE;
		gameGrade = 1;
	}
	
	public void initMap()
	{
		int index = 0;
		int[] random = ToolClass.imageIDArray();
		for(int i = 1; i < MAXX - 1; i++)
		{
			for(int j = 1; j < MAXY - 1; j++)
			{
				map[i][j] = random[index++];
			}
		}
		
		for(int i = 0; i < MAXX; i++)
		{
			map[i][0] = 0;
			map[i][MAXY - 1] = 0;
		}
		
		for(int i = 0; i < MAXY; i++)
		{
			map[0][i] = 0;
			map[MAXX - 1][i] = 0;
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(gameState == INIT_STATE)
		{
			drawStartText(g2);
		}
		
		if(gameState == PAUSE_STATE)
		{
			drawPauseText(g2);
		}
		
		if(gameState != RUN_STATE)
		{
			return;
		}
		
		drawTimeRect(g2);
		drawScoreAndGrade(g2);
		
		for(int i = 0; i < MAXX; i++)
		{
			for(int j = 0; j < MAXY; j++)
			{
				if(map[i][j] > 0) {
					Picture.draw(g, i, j, map[i][j]);
				}
			}
		}
		
		drawMousePointerRectangele(g2);
		drawLastAndCurrentPointRectangle(g2);
        
        g2.setStroke(new BasicStroke(5));
		
		for(int i = 0; isDrawClewLineFlag && i + 1 < inflexionList.size(); i++)
		{
			Point a = (Point)inflexionList.get(i);
			Point b = (Point)inflexionList.get(i + 1);
			drawLine(g2, Color.RED, a.x, a.y, b.x, b.y);
		}
		
        g2.setStroke(new BasicStroke(1));
        
		isDrawClewLineFlag = false;
	}
	
	private void drawLastAndCurrentPointRectangle(Graphics2D g2)
	{
		if(lastPoint != null)
		{
			Point p = ToolClass.getActualCoordinate(lastPoint);
			Rectangle2D rect = new Rectangle2D.Double(p.x, p.y, LEN, LEN);
			g2.setPaint(Color.RED);
			g2.draw(rect);
		}
		
		if(currentPoint != null)
		{
			Point p = ToolClass.getActualCoordinate(currentPoint);
			Rectangle2D rect = new Rectangle2D.Double(p.x, p.y, LEN, LEN);
			g2.setPaint(Color.RED);
			g2.draw(rect);
		}
	}
	
	private void drawMousePointerRectangele(Graphics2D g2)
	{
		if(currentMovePoint != null)
		{
			Rectangle2D rect = new Rectangle2D.Double(currentMovePoint.x, currentMovePoint.y, LEN, LEN);
			Rectangle2D rect2 = new Rectangle2D.Double(currentMovePoint.x - 2, currentMovePoint.y - 2, LEN + 4, LEN + 4);
			g2.setPaint(Color.GREEN);
			g2.draw(rect);
			g2.draw(rect2);
		}
		
	}
	
	public void drawBackgroundLine(Graphics2D g2)
	{
		Rectangle2D frame = new Rectangle2D.Double(LEFT, UP, LEN * MAXX, LEN * MAXY);
		g2.draw(frame);
		
		for(int row = UP + LEN; row < DOWN; row = row + LEN)
		{
			g2.draw(new Line2D.Double(new Point(LEFT, row), new Point(RIGHT, row)));
		}
		
		for(int column = LEFT + LEN; column < RIGHT; column = column + LEN)
		{
			g2.draw(new Line2D.Double(new Point(column, UP), new Point(column, DOWN)));
		}
	}
	
	public void drawLine(Graphics2D g2, Color color, int x1, int y1, int x2, int y2)
	{
		Point a = new Point(x1, y1);
		Point b = new Point(x2, y2);
		
		a = ToolClass.getActualCoordinate(a);
		b = ToolClass.getActualCoordinate(b);
		
		a.x = a.x + LEN / 2;
		a.y = a.y + LEN / 2;
		b.x = b.x + LEN / 2;
		b.y = b.y + LEN / 2;
		
		
		Line2D line = new Line2D.Double(a, b);
		g2.setPaint(Color.RED);
		g2.draw(line);
	}
	
	public void drawTimeRect(Graphics2D g2)
	{
		Rectangle2D.Double bkRect = new Rectangle2D.Double(TIME_RECT_X, TIME_RECT_Y, 
				TIME_RECT_WIDTH, TIME_RECT_HEIGHT);
		g2.draw(bkRect);
		
		int width = times * (TIME_RECT_WIDTH - 1) / allTimesInGameGrade;
		width = Math.min(width, TIME_RECT_WIDTH);
		Rectangle2D.Double moveRect = new Rectangle2D.Double(TIME_RECT_X + 1, TIME_RECT_Y + 1, 
				width, TIME_RECT_HEIGHT - 1);
		g2.setPaint(Color.ORANGE);
		g2.fill(moveRect);
	}
	
	public void drawScoreAndGrade(Graphics2D g2)
	{
		g2.setPaint(Color.BLUE);
		g2.drawString("级别：" + gameGrade, 600, 65);
		g2.drawString("成绩: " + score, 650, 65);
	}
	
	public void drawStartText(Graphics2D g2)
	{
		g2.setPaint(Color.BLUE);
		g2.drawString("请按开始按钮开始游戏！", 300, 200);
	}
	
	public void drawPauseText(Graphics2D g2)
	{
		g2.setPaint(Color.BLUE);
		g2.drawString("请按开始按钮，继续游戏！", 300, 200);
	}
	
	//判断两个点之间是否有直线连接
	private boolean hasBeelineLink(Point a, Point b)
	{
		boolean flag = false;
		int ax, ay, bx, by;
		ax = a.x;
		ay = a.y;
		bx = b.x;
		by = b.y;
		
		//如果两个点是同一个点时候 或者 两个点的横坐标和纵坐标都不相同，则退出
		if(a.equals(b) || (ax != bx && ay != by))
		{
			return flag;
		}
		
		//如果两点的横坐标相同
		if(ax == bx)
		{
			//判断两个点是否相邻
			if(ay == by + 1 || ay == by - 1)
			{
				flag = true;
			}
			else
			{
				Point maxPoint = null;
				Point minPoint = null;
				
				if(ay > by)
				{
					maxPoint = a;
					minPoint = b;
				}
				else if(ay < by)
				{
					maxPoint = b;
					minPoint = a;
				}
				
				Point[] array1 = getYUpAxesNullPoint(maxPoint);
				Point[] array2 = getYDownAxesNullPoint(minPoint);
				flag = null != getEqualsPoint(array1, array2);
			}
		}
		//如果两点的纵坐标相同 
		else if(ay == by)
		{
			//判断两个点是否相邻
			if(ax == bx + 1 || ax == bx - 1)
			{
				flag = true;
			}
			else
			{
				Point maxPoint = null;
				Point minPoint = null;
				
				if(ax > bx)
				{
					maxPoint = a;
					minPoint = b;
				}
				else if(ax < bx)
				{
					maxPoint = b;
					minPoint = a;
				}
				
				Point[] array1 = getXLeftAxesNullPoint(maxPoint);
				Point[] array2 = getXRightAxesNullPoint(minPoint);
				flag = null != getEqualsPoint(array1, array2);
			}
		}
		
		if(flag)
		{
			inflexionList.clear();
			inflexionList.add(a);
			inflexionList.add(b);
		}
		
		return flag;
	}
	
	//判断两个点之间是否有一个拐点连接
	private boolean hasOneInflexionLink(Point a, Point b)
	{
		boolean flag = false;
		Point equalsPoint = null;
		
		if(a.equals(b))
		{
			return false;
		}
		
		Point[] arrayAX = getXAxesNullPoint(a);
		Point[] arrayBY = getYAxesNullPoint(b);
		if(!flag)
		{
			equalsPoint = getEqualsPoint(arrayAX, arrayBY);
			flag = null != equalsPoint;
		}
		
		Point[] arrayAY = getYAxesNullPoint(a);
		Point[] arrayBX = getXAxesNullPoint(b);	
		if(!flag)
		{
			equalsPoint = getEqualsPoint(arrayBX, arrayAY);
			flag = null != equalsPoint;
		}
		
		if(flag)
		{
			inflexionList.clear();
			inflexionList.add(a);
			inflexionList.add(equalsPoint);
			inflexionList.add(b);
		}
		return flag;	
	}
	
	//找出两个数组中相同的一个元素，如果存在多个，就返回任意相同的一个
	//如果都不相同返回null
	private Point getEqualsPoint(Point[] array1, Point[] array2)
	{
		Point result = null;
		HashMap m = new HashMap();
		for(int i = 0; i < array1.length; i++)
		{
			m.put(array1[i], array1[i]);
		}
		
		for(int i = 0; i < array2.length; i++)
		{
			Object v = m.put(array2[i], array2[i]);
			if(v != null)
			{
				result = (Point)v;
				break;
			}
		}
		
		return result;
	}
	
	private Point[] getXLeftAxesNullPoint(Point p)
	{
		int left = p.x - 1;
		int y = p.y;
		List list = new ArrayList();
		for(int i = left; i >= 0 && i < MAXX && map[i][y] == 0; i--)
		{
			list.add(new Point(i, y));
		}
		
		Point[] array = new Point[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Point)list.get(i);
		}
		return array;
	}
	
	private Point[] getXRightAxesNullPoint(Point p)
	{
		int right = p.x + 1;
		int y = p.y;
		List list = new ArrayList();
		for(int i = right; i >= 0 && i < MAXX && map[i][y] == 0; i++)
		{
			list.add(new Point(i, y));
		}
		
		Point[] array = new Point[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Point)list.get(i);
		}
		
		return array;
	}
	
	private Point[] getYUpAxesNullPoint(Point p)
	{
		int up = p.y - 1;
		int x = p.x;
		List list = new ArrayList();
		
		for(int i = up; i >= 0 && i < MAXY && map[x][i] == 0; i--)
		{
			list.add(new Point(x, i));
		}
		Point[] array = new Point[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Point)list.get(i);
		}
		
		return array;
	}
	
	private Point[] getYDownAxesNullPoint(Point p)
	{
		int down = p.y + 1;
		int x = p.x;
		List list = new ArrayList();
		for(int i = down; i >= 0 && i < MAXY && map[x][i] == 0; i++)
		{
			list.add(new Point(x, i));
		}
		
		Point[] array = new Point[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Point)list.get(i);
		}
		
		return array;
	}
	
	//得到p的纵坐标上和p相邻的空点
	private Point[] getYAxesNullPoint(Point p)
	{
		List list = new ArrayList();
		Point[] p1 = getYUpAxesNullPoint(p);
		Point[] p2 = getYDownAxesNullPoint(p);
		for(int i = 0; i < p1.length; i++)
		{
			list.add(p1[i]);
		}
		
		for(int i = 0; i < p2.length; i++)
		{
			list.add(p2[i]);
		}
		
		Point[] array = new Point[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Point)list.get(i);
		}
		
		return array;
	}
	
	private Point[] getXAxesNullPoint(Point p)
	{
		List list = new ArrayList();
		Point[] p1 = getXLeftAxesNullPoint(p);
		Point[] p2 = getXRightAxesNullPoint(p);
		for(int i = 0; i < p1.length; i++)
		{
			list.add(p1[i]);
		}
		
		for(int i = 0; i < p2.length; i++)
		{
			list.add(p2[i]);
		}
		
		Point[] array = new Point[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Point)list.get(i);
		}
		
		return array;
	}
	
	//判断两个点之间是否有两个拐点连接
	private boolean hasTwoInflexionLink(Point a, Point b)
	{
		int ax = a.x;
		int ay = a.y;
		int bx = b.x;
		int by = b.y;
		boolean result = false;
		Point m = null;
		Point n = null;
		
		if(!result)
		{
			Point[] arrayAX = getXAxesNullPoint(a);
			Point[] arrayBX = getXAxesNullPoint(b);
			Integer[] xCoordinates = getXCoordinateEqualsPoints(arrayAX, arrayBX);
			for(int i = 0; i < xCoordinates.length; i++)
			{
				m = new Point(xCoordinates[i].intValue(), ay);
				n = new Point(xCoordinates[i].intValue(), by);
				if(hasBeelineLink(m, n))
				{
					result = true;
					break;
				}
			}
		}
		
		if(!result)
		{
			Point[] arrayAY = getYAxesNullPoint(a);
			Point[] arrayBY = getYAxesNullPoint(b);
			Integer[] yCoordinates = getYCoordinateEqualsPoints(arrayAY, arrayBY);
			for(int i = 0; i < yCoordinates.length; i++)
			{
				m = new Point(ax, yCoordinates[i].intValue());
				n = new Point(bx, yCoordinates[i].intValue());
				if(hasBeelineLink(m, n))
				{
					result = true;
					break;
				}
			}
		}
		
		if(result)
		{
			inflexionList.clear();
			inflexionList.add(a);
			inflexionList.add(m);
			inflexionList.add(n);
			inflexionList.add(b);
		}
		
		return result;
	}
	
	private Integer[] getXCoordinateEqualsPoints(Point[] a, Point[] b)
	{
		HashMap m = new HashMap();
		List list = new ArrayList();
		for(int i = 0; i < a.length; i++)
		{
			m.put(new Integer(a[i].x), new Integer(a[i].x));
		}
		
		for(int i = 0; i < b.length; i++)
		{
			Object v = m.put(new Integer(b[i].x), new Integer(b[i].x));
			if(v != null)
			{
				list.add(v);
			}
		}
		
		Integer[] array = new Integer[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Integer)list.get(i);
		}
		
		return array;
	}
	
	private Integer[] getYCoordinateEqualsPoints(Point[] a, Point[] b)
	{
		HashMap m = new HashMap();
		List list = new ArrayList();
		for(int i = 0; i < a.length; i++)
		{
			m.put(new Integer(a[i].y), new Integer(a[i].y));
		}
		
		for(int i = 0; i < b.length; i++)
		{
			Object v = m.put(new Integer(b[i].y), new Integer(b[i].y));
			if(v != null)
			{
				list.add(v);
			}
		}
		
		Integer[] array = new Integer[list.size()];
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (Integer)list.get(i);
		}
		
		return array;
	}
	
	//判断是否游戏进入下一关
	private void passGameGate()
	{
		//如果出现残局并且玩家还有时间
		if(!isHasNextStep() && isHasTime())
		{
			timer.stop();
			times = 0;
			repaint(TIME_RECT_X, TIME_RECT_Y, TIME_RECT_WIDTH, TIME_RECT_HEIGHT);
			JOptionPane.showMessageDialog(LinkPanel.this, "           恭喜你！\n         成功通过第 " + gameGrade + " 关！");
			gameGrade++;
			
			if(gameGrade == 6)
			{
				JOptionPane.showMessageDialog(LinkPanel.this, "恭喜你！你已经通关了！");
				
				//录入player的姓名
				inputPlayerInfo();
				
				//询问是否重新开始游戏
				askRestartGame();
				
				repaint();
				return;
			}
			
			
			initMap();
			gameState = RUN_STATE;
			timer.start();
				
			switch(gameGrade)
			{
				case 2:
					times = GRADETIMES2;
					allTimesInGameGrade = GRADETIMES2;
					break;
				case 3:
					times = GRADETIMES3;
					allTimesInGameGrade = GRADETIMES3;
					break;
				case 4:
					times = GRADETIMES4;
					allTimesInGameGrade = GRADETIMES4;
					break;
				case 5:
					times = GRADETIMES5;
					allTimesInGameGrade = GRADETIMES5;
					break;
				default:
					break;
			}
			
			repaint();
		}
	}
	
	
	private class StartAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			//如果有下一步
			if(isHasNextStep())
			{
				gameState = RUN_STATE;
				timer.start();
				repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);
			}
			else
			{
				//如果刚开始游戏就出现残局直接进入下一关
				passGameGate();
			}
		}
	}
	
	private class PauseAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(gameState == RUN_STATE)
			{
				gameState = PAUSE_STATE;
				timer.stop();
				repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);	
			}
			
		}
	}
	
	private class MouseMoveListener implements MouseMotionListener
	{
		public void mouseDragged(MouseEvent e) 
		{}
		
		public void mouseMoved(MouseEvent e)  
		{
			Point point = e.getPoint();
			
			int virtualX = (point.x - LEFT) / LEN;
			int virtualY = (point.y - UP) / LEN;
			point.x = LEFT + virtualX * LEN;
			point.y = UP + virtualY * LEN;
			
			if(point.x >= LEFT && point.x < RIGHT &&
					point.y >= UP && point.y < DOWN &&
					map[virtualX][virtualY] != 0 &&
					(currentMovePoint == null || !currentMovePoint.equals(point)))
			{
				currentMovePoint = point;
				
				repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);	
			}
		}
	}
	
	private void moveMapElements()
	{
		switch(gameGrade)
		{
			case 1:
				break;
				
			case 2:
				ToolClass.moveElementUp(map);
				break;
				
			case 3:
				ToolClass.moveElementDown(map);
				break;
				
			case 4:
				ToolClass.moveElementLeft(map);
				break;
				
			case 5:
				ToolClass.moveElementRight(map);
				break;
				
			default:
				break;
		}
	}
	
	private class MouseClickListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{}
		
		public void mousePressed(MouseEvent e)
		{
			if(gameState != RUN_STATE)
			{
				return;
			}
			
			Point p = ToolClass.getVritualCoordinate(e.getPoint());
			if(p.x >= 1 && p.x < MAXX && 
					p.y >= 1 && p.y < MAXY && 
					map[p.x][p.y] != 0)
			{
				lastPoint = currentPoint;
				currentPoint = p;
			}
			else
			{
				return;
			}
			
			
			if(lastPoint != null && currentPoint != null && 
					map[lastPoint.x][lastPoint.y] == map[currentPoint.x][currentPoint.y] && 
					map[lastPoint.x][lastPoint.y] != 0)
			{
				boolean flag = false;
				if(hasBeelineLink(lastPoint, currentPoint))
				{
					flag = true;
				}
				else if(hasOneInflexionLink(lastPoint, currentPoint))
				{
					flag = true;
				}
				else if(hasTwoInflexionLink(lastPoint, currentPoint))
				{
					flag = true;
				}
				
				if(flag)
				{
					isDrawClewLineFlag = true;
					score = score + 20;
					repaint();
					
					times++;
					repaint(TIME_RECT_X, TIME_RECT_Y, TIME_RECT_WIDTH, TIME_RECT_HEIGHT);
					
					map[lastPoint.x][lastPoint.y] = 0;
					map[currentPoint.x][currentPoint.y] = 0;
					lastPoint = null;
					currentPoint = null;
					currentMovePoint = null;
					
					//根据游戏等级移动元素
					moveMapElements();
					
					repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);
					new RepaintThread().start();
				}
			}
		}
		
		public void mouseReleased(MouseEvent e)
		{
			//判断玩家是否可以进入下一关
			passGameGate();
		}
		
		public void mouseEntered(MouseEvent e)
		{}
		
		public void mouseExited(MouseEvent e)
		{}
	}
	
	private class RepaintThread extends Thread
	{
		public void run()
		{
			try
			{
				Thread.sleep(200);
			}
			catch(Exception e)
			{}
			repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);
		}
	}
	
	//判断玩家时间是否有时间，如果有返回true
	private boolean isHasTime()
	{
		return times >= 0;
	}
	
	//判断是否有下一步，如果有返回true
	private boolean isHasNextStep()
	{
		Point a = new Point();
		Point b = new Point();
		
		boolean flag = false;
		
		label :for(int i = 0; i < MAXX; i++)
		{
			for(int j = 0; j < MAXY; j++)
			{
				a.x = i;
				a.y = j;
				
				for(int m = 0; m < MAXX; m++)
				{
					for(int n = 0; n < MAXY; n++)
					{
						b.x = m;
						b.y = n;
						
						if(map[i][j] != 0  && map[i][j] == map[m][n])
						{
							if(hasBeelineLink(a, b))
							{
								flag = true;
							}
							else if(hasOneInflexionLink(a, b))
							{
								flag = true;
							}
							else if(hasTwoInflexionLink(a, b))
							{
								flag = true;
							}
							
							if(flag)
							{
								repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);
								break label;
							}
								
						}
					}
				}
			}
		}
		
		
		if(flag)
		{
			new RepaintThread().start();
		}
		
		return flag;
	}
	
	private class ClewNextAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(gameState != RUN_STATE)
			{
				return;
			}
			
			if(isHasTime() && isHasNextStep())
			{
				isDrawClewLineFlag = true;
				times--;
				
			}
		}
	}
	
	private class AboutAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String string="作者信息：\n" +
							"姓名：徐晓蕾\n" +
							"E-mail: xuleixiao@126.com\n" +
							"MSN: xxl_msn@hotmail.com\n" +
							"QQ: 287834418";
			JOptionPane.showMessageDialog(LinkPanel.this, string);
		}
	}
	
	private class RecordAction implements ActionListener
	{
		JDialog recordDialog;
		public void actionPerformed(ActionEvent event)
		{
			ReadAndWriteFile.setPath(RECORDFILENAME);
			JScrollPane panel = ReadAndWriteFile.getRecordPanel();
			JPanel southPanel = new JPanel();
			JButton closeButton = new JButton("确定");
			southPanel.add(closeButton);
			closeButton.addActionListener(new CloseAction());

			recordDialog = new JDialog(frame, "连连看", true);
			recordDialog.setBounds(300, 300, 300, 255);

			Container container = recordDialog.getContentPane();
			container.add(panel, BorderLayout.CENTER);
			container.add(southPanel, BorderLayout.SOUTH);
			
			recordDialog.setVisible(true);
		}
		
		private class CloseAction implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				RecordAction.this.recordDialog.setVisible(false);
			}
		}
	}
	
	
	
	private void inputPlayerInfo()
	{
		//录入游戏者的名字
		ReadAndWriteFile.setPath(RECORDFILENAME);
		MyArray array = ReadAndWriteFile.read();
		if(score > 0 && !array.isFull() || array.getTair().score < score)
		{
			String msg = "游戏结束!\n请输入你的名字";
			String playerName = JOptionPane.showInputDialog(msg);
			if(playerName == null || playerName.length() == 0)
			{
				playerName = "无名英雄";
			}
			
			array.add(new Score(playerName, score));
			ReadAndWriteFile.write(array);
		}
	}
	
	private void askRestartGame()
	{
		int result =JOptionPane.showConfirmDialog(LinkPanel.this, 
				"是否重新开始？", "连连看", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION)
		{
			init();
			
			gameState = RUN_STATE;
			timer.start();
		}
		else
		{
			System.exit(0);
		}
	}
	
	private class TimerAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(times > 0)
			{
				times--;
			}
			
			repaint(TIME_RECT_X, TIME_RECT_Y, TIME_RECT_WIDTH, TIME_RECT_HEIGHT);
			
			if(times <= 0)
			{
				gameState = OVER_STATE;
				repaint(LEFT, UP, LEN * MAXX, LEN * MAXY);
				timer.stop();
				
				inputPlayerInfo();
				
				askRestartGame();
				
				repaint();
			}
		}
	}
	
	private JButton startButton;
	private JButton pauseButton;
	private JButton clewButton;
	private JButton recordButton;
	private JButton aboutButton;
	//private JLabel timesLabel;
	//private JLabel scoreLabel;
	
	private int allTimesInGameGrade;
	private boolean isDrawClewLineFlag;
	private int map[][];
	private List inflexionList;
	private Point lastPoint;
	private Point currentPoint;
	private Point currentMovePoint;
	private Timer timer;
	private int gameState;
	private int times;
	private int score;
	private int gameGrade;
	private JFrame frame;
}
