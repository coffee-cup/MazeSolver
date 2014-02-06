package org.liquidsoap;

public class Node {
	private int x, y;
	private char letter;
	private Node parent;

	private float cost = 10;
	private float G = 0;
	private float H = 0;

	public Node(char letter, int x, int y) {
		this.letter = letter;
		this.x = x;
		this.y = y;
	}

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public float getF() {
		return (G + H);
	}

	// manhatten distance (a2 + b2 = c2)
	public void calculateH(Node end) {
		float dx = Math.abs(this.x - end.getX());
		float dy = Math.abs(this.y - end.getY());

		H = dx + dy;
	}

	public void calculateG() {
		G = parent.getG() + cost;
	}

	public float getG() {
		return (G);
	}

	public void setParent(Node p) {
		parent = p;
	}

	public Node getParent() {
		return (parent);
	}

	public char getChar() {
		return (letter);
	}

	public void setChar(char set) {
		letter = set;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return (x);
	}

	public int getY() {
		return (y);
	}
}
