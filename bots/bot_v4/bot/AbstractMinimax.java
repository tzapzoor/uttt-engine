package bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public abstract class AbstractMinimax {
	protected static class Node {
		public int score = 0;
		public int level;
		public Move move = null;
		public FieldImplementation field = null;
		public List<Node> children = new LinkedList<Node>();
	}

	protected int maxDepth;
	protected Node root;

	public AbstractMinimax(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	// a.k.a where the magic happens
	public abstract int heuristic(Node node);

	// Used for hardcoding moves.
	// It is called before the actual minimax. If it returns
	// a value different from null the minimax will never run.
	public abstract Move preMinimax(FieldImplementation initialField);

	public Move nextMove(FieldImplementation initialField) {
		Move pre = preMinimax(initialField);
		if (pre != null) {
			System.err.println("NO MINIMAX BRUH!");
			return pre;
		}

		generateGameTree(initialField);

		// call minimax recursion
		minimax(root);

		// find the branch that has the maximum score
		// the maximum score is now registered inside the root
		ArrayList<Move> coolMoves = new ArrayList<Move>();
		System.err.println("Computed score: " + root.score);
		for (Node ch : root.children) {
			if (ch.score == root.score) {
				coolMoves.add(ch.move);
			}
		}

		for (Node m : root.children) {
			System.err.print("[" + m.move);
			System.err.println(" " + "score=" + m.score + "]");
			
		}
		System.err.println();

		Random random = new Random();
		if (coolMoves.size() > 0)
			return coolMoves.get(random.nextInt(coolMoves.size()));

		System.err.println("Failed to generate move :(");
		// should know if it ever gets here
		return null;
	}

	private void minimax(Node currentNode) {
		if (currentNode.children.size() == 0) {
			currentNode.score = heuristic(currentNode);
			return;
		}
		// this means it needs to maximixe the score
		if (currentNode.level % 2 == 0) {
			currentNode.score = Integer.MIN_VALUE;
			for (Node ch : currentNode.children) {
				minimax(ch);
				currentNode.score = Math.max(currentNode.score, ch.score);
			}
		} else {
			currentNode.score = Integer.MAX_VALUE;
			for (Node ch : currentNode.children) {
				minimax(ch);
				currentNode.score = Math.min(currentNode.score, ch.score);
			}
		}
	}

	private void generateGameTree(FieldImplementation initialField) {
		root = new Node();
		root.level = 0;
		root.field = new FieldImplementation(initialField);

		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		while (!queue.isEmpty()) {
			Node currentNode = queue.poll();

			// TODO:DEBUG
			// currentNode.field.exportFieldJson();

			// reached the maximum depth
			if (currentNode.level >= maxDepth) {
				continue;
			}
			ArrayList<Move> nextMoves = currentNode.field.getAvailableMoves();
			for (Move m : nextMoves) {
				Node newNode = new Node();
				newNode.field = new FieldImplementation(currentNode.field);
				newNode.level = currentNode.level + 1;
				newNode.move = m;

				// extra safety due to paranoia :)
				if (newNode.field.placeMove(m.getRow(), m.getCol(),
						newNode.level % 2 != 0)) {
					currentNode.children.add(newNode);
					queue.add(newNode);
				} else {
					System.err.println("BAD MOVE, BRUH!!!!!!!! WTF");
				}
			}
		}
	}
}
