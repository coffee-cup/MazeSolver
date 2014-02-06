package org.liquidsoap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.JPanel;

public class MazePanel extends JPanel {
	private Node[][] maze;
	private ArrayList<Node> Path;

	public final static char wall = 'X';
	public final static char path = '0';
	public final static char start = 'S';
	public final static char end = 'E';

	int mx = 0, my = 0;

	int width, height;

	public MazePanel() {
		super(true); // create a JPanel with a double buffer
		init();
	}

	public MazePanel(LayoutManager layout, boolean doublebuffer) {
		super(layout, doublebuffer);
		init();
	}

	private void init() {
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawMaze(g);
		drawPath(g);

		super.repaint();
	}

	private void drawPath(Graphics g) {
		if (Path == null) {
			return;
		}

		final float PD = 0.25f;

		g.setColor(new Color(80, 250, 252));

		for (int i = 1; i < Path.size() - 1; i++) {
			int px = (int) (((Path.get(i).getX() * width) + (width / 2)) - (width * 0.25));
			int py = (int) (((Path.get(i).getY() * height) + (height / 2)) - (height * 0.25));
			int pw = (int) (width * 0.50);
			int ph = (int) (height * 0.50);

			g.fillRect(px, py, pw, ph);
			// g.fillRect((int) ((Path.get(i).getX() * width) + (width * PD)),
			// (int) ((Path.get(i).getY() * height) + (height * PD)), (int)
			// (width * PD), (int) (height * PD));
		}
	}

	private void drawMaze(Graphics g) {
		width = super.getWidth() / maze.length;
		height = super.getHeight() / maze[1].length;

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] != null) {
					if (maze[i][j].getChar() == wall) {
						g.setColor(new Color(0, 0, 0));
					} else if (maze[i][j].getChar() == path) {
						g.setColor(new Color(255, 255, 255));
					} else if (maze[i][j].getChar() == start) {
						g.setColor(new Color(0, 255, 0));
					} else if (maze[i][j].getChar() == end) {
						g.setColor(new Color(255, 0, 0));
					}
					g.fillRect(i * width, j * height, width, height);

					g.setColor(Color.green);
					g.drawRect(i * width, j * height, width, height);
				}
			}
		}
	}

	public void setPath(ArrayList<Node> p) {
		Path = p;
	}

	public void setMaze(Node[][] m) {
		this.maze = m;
	}
}
