package bot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/*
 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonSerializationContext;
 import com.google.gson.JsonSerializer;
 */

import bot.Minimax.Node;

public class Minimax {

	// node class
	public class Node {
		public int value, level;
		public Field field;
		public Move move;
		public ArrayList<Node> children = new ArrayList<>();

		public Node(Field field, Move move) {
			level = 0;
			this.field = field;
			this.move = move;
		}

		public Node(Field field, int level, Move move) {
			this.level = level;
			this.field = field;
			this.move = move;
		}

	}

	/*
	 * public class NodeSerializer implements JsonSerializer<Node> {
	 * 
	 * @Override public JsonElement serialize(Minimax.Node src, Type srcType,
	 * JsonSerializationContext context) {
	 * System.err.println("Got here dammit!"); JsonObject obj = new
	 * JsonObject(); obj.addProperty("levelxd", src.level);
	 * obj.addProperty("value", src.value); obj.addProperty("field",
	 * src.field.getFieldJson()); if (src.move != null) obj.addProperty("move",
	 * src.move.toString()); else obj.addProperty("move", "null");
	 * 
	 * for (int i = 0; i < src.children.size(); i++) { obj.add("child" + i,
	 * context.serialize(src.children.get(i))); } return obj; } }
	 */

	// vars
	public Node root;
	public int depth;

	public Minimax() {
		// this is where you set the depth
		depth = 2;
	}

	public Move chooseMove(Field field) {
		Move move = new Move();

		// hardcodam prima mutare
		if (field.isEmpty()) {
			Random rand = new Random();
			switch (Math.abs(rand.nextInt()) % 4) {
			case 0:
				move.mCol = 3;
				move.mRow = 4;
				break;
			case 1:
				move.mCol = 5;
				move.mRow = 4;
				break;
			case 2:
				move.mCol = 4;
				move.mRow = 3;
				break;
			case 3:
				move.mCol = 4;
				move.mRow = 5;
				break;

			}
			return move;
		}

		// generate tree
		ArrayList<Node> queue = new ArrayList<Node>();
		// root
		root = new Node(field, null);
		queue.add(root);

		// creates the tree for minimax
		// maybe we can improve this by not using an arraylist to store every
		// single fucking node
		for (int i = 0; i < queue.size(); i++) {
			ArrayList<Move> moves = queue.get(i).field.getAvailableMoves();
			Node crtNode = queue.get(i);
			for (Move myMove : moves) {
				Field myField = new Field(queue.get(i).field);
				myField.makeMove(myMove, i % 2 == 0);

				Node myNode = new Node(myField, crtNode.level + 1, myMove);
				crtNode.children.add(myNode);

				if (myNode.level < depth) {
					queue.add(myNode);
				}
			}
		}

		System.err.println("size " + queue.size());

		// choose between alphaBeta and alphaBeta
		// root.value = naiveMinimax(root, depth, true);
		root.value = alphaBeta(root, depth, -Integer.MAX_VALUE,
				Integer.MAX_VALUE, true);

		// prints heuristic value for root and its children
		// System.out.println("Root node: " + root.value);
		// System.out.println("Children size: " + root.children.size());
		//
		// for(Node node: root.children) {
		// System.out.println("Child node: " + node.value);
		// }

		// searches for the node with max value in the root children
		// maybe improve that by returning the move to the root ? ...or
		// something
		for (Node node : root.children) {
			if (node.value == root.value) {
				return node.move;
			}
		}

		return move;
	}

	private int naiveMinimax(Node currentNode, int currentDepth, boolean isMax) {

		if (currentNode.children.size() == 0 || currentDepth <= 0) {
			// replace this with your own awesome heuristic
			currentNode.value = shittyHeuristic(currentNode.field);
			return currentNode.value;
		}

		int value;
		if (isMax) {
			value = -Integer.MAX_VALUE;
			for (Node child : currentNode.children) {
				value = Math.max(value,
						naiveMinimax(child, currentDepth - 1, !isMax));
			}
		} else {
			value = Integer.MAX_VALUE;
			for (Node child : currentNode.children) {
				value = Math.min(value,
						naiveMinimax(child, currentDepth - 1, !isMax));
			}
		}

		currentNode.value = value;

		return value;
	}

	private int alphaBeta(Node currentNode, int currentDepth, int a, int b,
			boolean isMax) {
		if (currentNode.children.size() == 0 || currentDepth <= 0) {
			// replace this with your own awesome heuristic
			currentNode.value = shittyHeuristic(currentNode.field);
			return currentNode.value;
		}

		if (isMax) {
			for (Node child : currentNode.children) {
				a = Math.max(a,
						alphaBeta(child, currentDepth - 1, a, b, !isMax));

				if (a >= b)
					break;
			}

			return a;
		} else {
			for (Node child : currentNode.children) {
				b = Math.min(b,
						alphaBeta(child, currentDepth - 1, a, b, !isMax));

				currentNode.value = b;
				if (a >= b)
					break;
			}

			return b;
		}
	}

	private int shittyHeuristic(Field field) {
		// score for big games
		int[][] bigGameScore = { { 10, 5, 10 }, { 5, 20, 5 }, { 10, 5, 10 } };

		// big games count
		int bigWinMine = field.getMyBigWin(), bigWinOther = field
				.getOtherBigWin();
		// big games score
		int bigScoreMine = field.getMyBigScore(bigGameScore), bigScoreOther = field
				.getOtherBigScore(bigGameScore);

		int smallTotalScore = 0;
		int[][] board, smallGame = new int[3][3];
		board = field.getBoard();

		for (int k1 = 0; k1 < 3; k1++) {
			for (int k2 = 0; k2 < 3; k2++) {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						smallGame[i][j] = board[k1 * 3 + i][k2 * 3 + j];
					}
				}
				// use smallgame
				smallTotalScore += getSmallGameScore(smallGame);
			}
		}

		return (int) Math.round(0.75 * (bigScoreOther - bigScoreMine) + 0.25
				* smallTotalScore);
	}

	private int getSmallGameScore(int[][] smallGame) {
		int myBotId = BotParser.mBotId;
		int score = 0;
		int x = 0, y = 0;

		// vertical lines
		for (int k = 0; k < 3; k++) {
			for (int i = 0; i < 3; i++) {
				if (smallGame[i][k] == myBotId
						&& smallGame[i][k] == smallGame[i][(k + 1) % 3]) {
					if (smallGame[i][(k + 2) % 3] <= 0)
						score += 1;
				}
				if (smallGame[k][i] == myBotId
						&& smallGame[k][i] == smallGame[k][(i + 1) % 3]) {
					if (smallGame[k][(i + 2) % 3] <= 0)
						score += 1;
				}
			}
		}

		int countMe = 0, countOther = 0, countEmpty = 0;

		for (int i = 0; i < 3; i++) {
			if (smallGame[i][i] == 1) {
				countMe++;
			} else if (smallGame[i][i] == 2) {
				countOther++;
			} else {
				countEmpty++;
			}
		}

		if (smallGame[2][0] == 1) {
			countMe++;
		} else if (smallGame[2][0] == 2) {
			countOther++;
		} else {
			countEmpty++;
		}

		if (smallGame[0][2] == 1) {
			countMe++;
		} else if (smallGame[0][2] == 2) {
			countOther++;
		} else {
			countEmpty++;
		}

		score += (countMe - countOther);

		return score;
	}

	// prints the tree
	// completely useless since
	private void printTree(Node currentNode) {
		System.err.println("Node: " + currentNode.level + " "
				+ currentNode.value);
		for (Node child : currentNode.children) {
			printTree(child);
		}
	}

	public void exportMinimaxJson() {
		/*
		 * Gson gson = new Gson(); String json = gson.toJson(this);
		 * 
		 * Gson nodeGson = new GsonBuilder().registerTypeAdapter(Node.class, new
		 * NodeSerializer()).create();
		 * 
		 * try { // write converted json data to a file named "file.json" int
		 * count = new File(".\\src\\PA Viewer\\data\\minimax\\")
		 * .listFiles().length; FileWriter writer = new FileWriter(
		 * ".\\src\\PA Viewer\\data\\minimax\\minimax-" + count + ".json");
		 * writer.write(json); // writer.write(nodeGson.toJson(root));
		 * writer.close();
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 */
	}
}
