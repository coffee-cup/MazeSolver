package org.liquidsoap;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SetupFrame {
	private static final String title = "Setup";

	// the maze jframe
	private MazeSolver home;

	// the setup jframe
	private JFrame frame;

	// all text fields
	private JTextField Twidth = new JTextField(2);
	private JTextField Theight = new JTextField(2);

	// all lables
	private JLabel Lwidth = new JLabel("width:");
	private JLabel Lheight = new JLabel("height:");

	// all buttons
	private JButton gobutton = new JButton("GO");
	private JButton allpath = new JButton("all path");
	private JButton allwall = new JButton("all wall");

	// if maze can go diagonal
	private JCheckBox Cdia = new JCheckBox("diagonal", false);

	// max maze size can be
	private final int MAX = 100;

	// min maze size can be
	private final int MIN = 2;

	public SetupFrame(MazeSolver s) {
		this.home = s;
	}

	// create setup window
	public void createWindow() {
		// if window is already created, set focus
		if (frame != null) {
			frame.setVisible(true);
			frame.toFront();
			return;
		}

		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setLayout(new FlowLayout());

		addItems();

		frame.setVisible(true);
	}

	// change the size of the maze
	private void changeSize() {
		int nx = home.currentwidth;
		int ny = home.currentheight;

		try {
			nx = Integer.parseInt(Twidth.getText());
			ny = Integer.parseInt(Theight.getText());
		} catch (NumberFormatException e) {
			nx = home.currentwidth;
			ny = home.currentheight;
		}
		if ((nx > MAX || ny > MAX) || (nx < MIN || ny < MIN)) {
			nx = home.currentwidth;
			ny = home.currentheight;
		}
		Twidth.setText(String.valueOf(nx));
		Theight.setText(String.valueOf(ny));

		home.setupMaze(nx, ny, MazePanel.wall);
	}

	// add items to jframe
	private void addItems() {
		frame.add(Lwidth);
		frame.add(Twidth);
		frame.add(Lheight);
		frame.add(Theight);
		frame.add(gobutton);
		frame.add(allpath);
		frame.add(allwall);
		frame.add(Cdia);

		Twidth.setText(String.valueOf(home.currentwidth));
		Theight.setText(String.valueOf(home.currentheight));

		// change size of maze
		gobutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeSize();
			}
		});

		// set whole maze to path
		allpath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				home.setupMaze(home.currentwidth, home.currentheight,
						MazePanel.path);
			}
		});
		// set whole maze to wall
		allwall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				home.setupMaze(home.currentwidth, home.currentheight,
						MazePanel.wall);
			}
		});
		// yes or no for diagonal
		Cdia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				home.solver.setDia(Cdia.isSelected());
			}
		});
	}
}