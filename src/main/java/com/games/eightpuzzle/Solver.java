package com.games.eightpuzzle;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Solver {
    private class Node implements Comparable<Node> {

		int priority;
		int moves;
		EightPuzzleEngine current;
		Node previous;

		public Node(EightPuzzleEngine current, Node previous, int moves) {
			this.current = current;
			this.previous = previous;
			this.moves = moves;
			this.priority = moves + current.manhattan();
		}

		@Override
		public int compareTo(Node o) {
			if (priority > o.priority)
				return 1;
			else if (priority < o.priority)
				return -1;
			else {
				if(current.hamming() > o.current.hamming())
					return 1;
				else if(current.hamming() > o.current.hamming())
					return -1;
				else 
					return 0;
			}
				
		}
		
		
	}

	private final int moves;
	private final Node goalNode;
	private final EightPuzzleEngine start;
	
	private Node solve(EightPuzzleEngine initial) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		Node initialNode = new Node(initial, null, 0);
		
		pq.add(initialNode);

		while (!pq.isEmpty()) {
			Node curr = pq.poll();

			if (curr.current.isGoal())
				return curr;

			Iterable<EightPuzzleEngine> children = curr.current.neighbors();
			for (EightPuzzleEngine theBoard : children) {
				if (curr.previous != null) {
					if (!theBoard.equals(curr.previous.current))
						pq.add(new Node(theBoard, curr, curr.moves + 1));
				} else
					pq.add(new Node(theBoard, curr, curr.moves + 1));
			}
		}
		return null;
	}

	private EightPuzzleEngine solve(EightPuzzleEngine initial, EightPuzzleEngine twin) {
		PriorityQueue<Node> pqInitial = new PriorityQueue<>();
		PriorityQueue<Node> pqTwin = new PriorityQueue<>();

		Node initialNode = new Node(initial, null, 0);
		Node twinNode = new Node(twin, null, 0);

		pqInitial.add(initialNode);
		pqTwin.add(twinNode);

		while (!pqInitial.isEmpty() || !pqTwin.isEmpty()) {
			Node currInitial = null, currTwin = null;

			if (!pqInitial.isEmpty())
				currInitial = pqInitial.poll();
			if (!pqTwin.isEmpty())
				currTwin = pqTwin.poll();

			if (currInitial.current.isGoal())
				return initial;
			if (currTwin.current.isGoal())
				return twin;

			Iterable<EightPuzzleEngine> children = currInitial.current.neighbors();
			for (EightPuzzleEngine theBoard : children) {
				if (currInitial.previous != null) {
					if (!theBoard.equals(currInitial.previous.current))
						pqInitial.add(new Node(theBoard, currInitial, currInitial.moves + 1));
				} else
					pqInitial.add(new Node(theBoard, currInitial, currInitial.moves + 1));
			}

			Iterable<EightPuzzleEngine> childrenTwin = currTwin.current.neighbors();
			for (EightPuzzleEngine theBoard : childrenTwin) {
				if (currTwin.previous != null) {
					if (!theBoard.equals(currTwin.previous.current))
						pqTwin.add(new Node(theBoard, currTwin, currTwin.moves + 1));
				} else
					pqTwin.add(new Node(theBoard, currTwin, currTwin.moves + 1));
			}
		}
		return null;
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(EightPuzzleEngine initial) {
		if (initial == null)
			throw new IllegalArgumentException();
		this.start = initial;
		if (isSolvable()) {
			goalNode = solve(initial);
			this.moves = goalNode.moves;
		} else {
			goalNode = null;
			this.moves = -1;
		}
	}

	// is the initial board solvable? (see below)
	public boolean isSolvable() {
		EightPuzzleEngine twin = this.start.twin();
		EightPuzzleEngine result = solve(start, twin);
		if (result == twin)
			return false;
		return true;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		return moves;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<EightPuzzleEngine> solution() {

		// if unsolvable return null
		if (!isSolvable())
			return null;

		List<EightPuzzleEngine> path = new LinkedList<>();

		// adding every step to the to the head of the list
		Node it = goalNode;
		while (it != null) {
			path.add(0, it.current);
			it = it.previous;
		}

		return path;
	}

}
