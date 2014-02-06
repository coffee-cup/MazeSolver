package org.liquidsoap;

import java.util.ArrayList;

public class Solver {

	// the maze itself
	private Node[][] maze;

	// if we are able to travel diagonally
	private boolean dia;

	// the path of nodes from start to finish
	private ArrayList<Node> path;

	// start of maze
	private Node start;

	// end of maze
	private Node end;

	// current node we are looking at
	private Node current;

	// all of the nodes avaialbe to be looked at
	private ArrayList<Node> open;

	// nodes that have already been looked at
	private ArrayList<Node> closed;

	public Solver(boolean dia) {
		this.dia = dia;
		path = new ArrayList<Node>();
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
	}

	private void reset() {
		path = new ArrayList<Node>();
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		start = null;
		end = null;
	}

	public void setMaze(Node[][] m) {
		this.maze = m;
	}

	public void setDia(boolean dia) {
		this.dia = dia;
	}

	// find the start and end of the maze
	private void findStartEnd() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j].getChar() == MazePanel.start) {
					start = maze[i][j];
					open.add(start);
				} else if (maze[i][j].getChar() == MazePanel.end) {
					end = maze[i][j];
				}
				if (start != null && end != null) {
					break;
				}
			}
		}
	}

	// checks if node is in closed
	private boolean inClosed(Node n) {
		for (Node i : closed) {
			if (i.getX() == n.getX() && i.getY() == n.getY()) {
				return (true);
			}
		}
		return (false);
	}

	// checks if node is in open
	private boolean inOpen(Node n) {
		for (Node i : open) {
			if (i.getX() == n.getX() && i.getY() == n.getY()) {
				return (true);
			}
		}
		return (false);
	}

	// checks if the maze is solved
	private boolean solved(Node n) {
		if (n.getX() == end.getX() && n.getY() == end.getY()) {
			return (true);
		}
		return (false);
	}

	// if the node we are looking at is in the array, not a wall, and is not in
	// closed
	private boolean isValid(int x, int y) {
		if ((x >= 0 && x < maze.length) && (y >= 0 && y < maze[1].length)) {
			if (maze[x][y].getChar() != MazePanel.wall) {
				if (!inClosed(maze[x][y]) && !inOpen(maze[x][y])) {
					return (true);
				}
			}
		}
		return (false);
	}

	private void addNode(Node n, Node parent) {
		n.setParent(parent);
		n.calculateG();
		n.calculateH(end);

		open.add(n);
	}

	// work backwards to find path
	private void findPath(Node n) {
		while (n.getParent() != null) {
			path.add(n);
			n = n.getParent();
			// print out coordinates of path
//			 System.out.println(n.getY() + "," + n.getX());
		}
		path.add(start);
		System.out.println(path.size());
	}

	// solve the current multi-dimensional array of nodes
	public ArrayList<Node> Solve() {
		reset();

		findStartEnd();

		// there is no start or end
		if (start == null || end == null) {
			return (null);
		}

		current = start;
		current.setParent(null);

		// while the maze is not solved and there is still something in open
		while (!open.isEmpty()) {

			if (solved(current)) {
				findPath(current);
				break;
			}

			closed.add(current);
			open.remove(current);

			int x = current.getX();
			int y = current.getY();

			x = current.getX() + 1;
			if (isValid(x, y)) {
				addNode(maze[x][y], current);
			}

			x = current.getX() - 1;
			if (isValid(x, y)) {
				addNode(maze[x][y], current);
			}

			x = current.getX();
			y = current.getY() + 1;
			if (isValid(x, y)) {
				addNode(maze[x][y], current);
			}

			y = current.getY() - 1;
			if (isValid(x, y)) {
				addNode(maze[x][y], current);
			}

			if (dia) {
				x = current.getX() + 1;
				y = current.getY() + 1;
				if (isValid(x, y)) {
					addNode(maze[x][y], current);
				}
				x = current.getX() - 1;
				y = current.getY() - 1;
				if (isValid(x, y)) {
					addNode(maze[x][y], current);
				}
				x = current.getX() + 1;
				y = current.getY() - 1;
				if (isValid(x, y)) {
					addNode(maze[x][y], current);
				}
				x = current.getX() - 1;
				y = current.getY() + 1;
				if (isValid(x, y)) {
					addNode(maze[x][y], current);
				}
			}

			// no path
			if (open.isEmpty()) {
				return (null);
			}

			// finds the next best node in open (if any)
			float bestF = open.get(0).getF();
			int index = -1;

			for (int i = 0; i < open.size(); i++) {
				if (open.get(i).getF() <= bestF) {
					bestF = open.get(i).getF();
					index = i;
				}
			}
			if (index != -1) {
				current = open.get(index);
			}
		}
		return (path);
	}
}
