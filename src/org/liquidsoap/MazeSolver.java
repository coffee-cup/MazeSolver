package org.liquidsoap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MazeSolver extends JFrame {
	private static final String title = "Maze Solver";

	private MazePanel mazepanel;
	private Node[][] maze;
	public Solver solver; // public so setup can access

	// the path
	private ArrayList<Node> Path;

	// buttons to press
	private JButton solvebutton = new JButton("Solve");
	private JButton setupbutton = new JButton("Setup");
	private JPanel buttonpanel = new JPanel(true);
	private SetupFrame setupframe = new SetupFrame(this);

	// The buttons for the image
	private JRadioButton buttonwall = new JRadioButton("wall", false);
	private JRadioButton buttonpath = new JRadioButton("path", true);
	private JRadioButton buttonstart = new JRadioButton("start", false);
	private JRadioButton buttonfinish = new JRadioButton("finish", false);;
	private ButtonGroup buttonselecter = new ButtonGroup();

	private boolean mouseclicked = false;

	private boolean path = true;
	private boolean wall = false;
	private boolean start = false;
	private boolean end = false;

	public int currentwidth = 0;
	public int currentheight = 0;

	public MazeSolver() {
		super(title);
		init();
	}

	public void start() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// super.setLayout(new BorderLayout());
		super.setSize(640, 640);
		super.setVisible(true);
	}

	private void init() {

		// create a new jpanel where the drawing of the maze will be done
		mazepanel = new MazePanel();

		// set the background of the drawing panel
		mazepanel.setBackground(Color.black);

		// add mouse listener to the drawing panel
		mazeMouseListener mlistener = new mazeMouseListener();
		mazepanel.addMouseListener(mlistener);
		mazepanel.addMouseMotionListener(mlistener);

		// add the drawing panel to the jframe
		add(mazepanel, BorderLayout.CENTER);

		// add the button panel to the jframe
		add(buttonpanel, BorderLayout.SOUTH);

		// create the action listener for the radio buttons
		nodeSelecterListener nodelistener = new nodeSelecterListener();

		// create the node slecter radio buttons
		buttonpath.addActionListener(nodelistener);
		buttonwall.addActionListener(nodelistener);
		buttonstart.addActionListener(nodelistener);
		buttonfinish.addActionListener(nodelistener);

		// create a group for the radio buttons
		buttonselecter = new ButtonGroup();
		buttonselecter.add(buttonwall);
		buttonselecter.add(buttonpath);
		buttonselecter.add(buttonstart);
		buttonselecter.add(buttonfinish);

		// add stuff to button panel
		buttonpanel.add(solvebutton);
		buttonpanel.add(buttonpath);
		buttonpanel.add(buttonwall);
		buttonpanel.add(buttonstart);
		buttonpanel.add(buttonfinish);
		buttonpanel.add(setupbutton);

		// add action listener to the solve button
		solvebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solveMaze();
			}
		});

		// add action listener to the setup button
		setupbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setupframe.createWindow();
			}
		});

		// create a new object that will solve the maze for us
		solver = new Solver(false);

		// set all nodes of the base != null
		setupMaze(20, 20, MazePanel.wall);
	}

	// setup new maze based on size and type
	public void setupMaze(int dx, int dy, char type) {
		currentwidth = dx;
		currentheight = dy;
		maze = new Node[dy][dx];
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j] = new Node(type, i, j);
			}
		}
		if (Path != null) {
			Path.clear();
		}
		mazepanel.setMaze(maze);
		solver.setMaze(maze);
	}

	private void solveMaze() {
		Path = solver.Solve();

		// set the path to draw
		mazepanel.setPath(Path);

		// print steps of maze
		// System.out.println("step : " + Path.size());

		if (Path == null) {
			JOptionPane.showMessageDialog(null, "Maze could not be solved");
		}
	}

	// change node state of node clicked on
	public void changeNodeState(int x, int y) {
		if (Path != null) {
			Path.clear();
		}

		if (x < 0 || x >= maze.length)
			return;
		if (y < 0 || y >= maze[1].length)
			return;

		if ((x <= mazepanel.getWidth() && x >= 0)
				&& (y <= mazepanel.getHeight() && y >= 0)) {
			if (maze[x][y] != null) {
				if (wall) {
					maze[x][y].setChar(MazePanel.wall);
				} else if (path) {
					maze[x][y].setChar(MazePanel.path);
				} else if (start) {
					for (int i = 0; i < maze.length; i++) {
						for (int j = 0; j < maze[i].length; j++) {
							if (maze[i][j].getChar() == MazePanel.start) {
								maze[i][j].setChar(MazePanel.wall);
								break;
							}
						}
					}
					maze[x][y].setChar(MazePanel.start);
				} else if (end) {
					for (int i = 0; i < maze.length; i++) {
						for (int j = 0; j < maze[i].length; j++) {
							if (maze[i][j].getChar() == MazePanel.end) {
								maze[i][j].setChar(MazePanel.wall);
								break;
							}
						}
					}
					maze[x][y].setChar(MazePanel.end);
				}
			}
		}
	}

	// find out what node was clicked
	// will calculate based on maze size and window size
	public void convertMouseCoors(int mx, int my) {
		int maze_area_width = mazepanel.getWidth();
		int maze_area_height = mazepanel.getHeight();
		int width = maze_area_width / maze.length;
		int height = maze_area_height / maze[1].length;
		int x = mx / width;
		int y = my / height;
		changeNodeState(x, y);
	}

	// sets all selecter nodes to false
	private void allFalse() {
		path = false;
		wall = false;
		start = false;
		end = false;
	}

	// the action listener for the radio buttons
	class nodeSelecterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonpath) {
				allFalse();
				path = true;
			} else if (e.getSource() == buttonwall) {
				allFalse();
				wall = true;
			} else if (e.getSource() == buttonstart) {
				allFalse();
				start = true;
			} else if (e.getSource() == buttonfinish) {
				allFalse();
				end = true;
			}
		}
	}

	class mazeMouseListener implements MouseListener, MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			mouseclicked = true;
			convertMouseCoors(e.getX(), e.getY());
		}

		public void mouseReleased(MouseEvent e) {
			mouseclicked = false;
		}

		public void mouseDragged(MouseEvent e) {
			if (mouseclicked) {
				convertMouseCoors(e.getX(), e.getY());
			}
		}

		public void mouseMoved(MouseEvent e) {
			mazepanel.mx = e.getX();
			mazepanel.my = e.getY();
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
			mouseclicked = false;
		}

		public void mouseClicked(MouseEvent e) {
		}
	}
}