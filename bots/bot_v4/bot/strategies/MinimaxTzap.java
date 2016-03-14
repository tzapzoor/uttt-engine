package bot.strategies;

import bot.AbstractMinimax;
import bot.BotParser;
import bot.FieldImplementation;
import bot.FieldInterface;
import bot.Move;

public class MinimaxTzap extends AbstractMinimax {

	public MinimaxTzap(int maxDepth) {
		super(maxDepth);
	}

	@Override
	public int heuristic(Node node) {
//		if (node.level != 3) {
//			System.err.println("Evaluated node level " + node.level);
//		}
		FieldInterface.Winner winner = node.field.bigBoardWinner();
		int freeCells = 0;
		if (winner == FieldInterface.Winner.MYSELF
				|| winner == FieldInterface.Winner.OPPONENT) {
			// count free cells left in the whole board
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (node.field.getBigBoard()[i][j] == 0) {
						freeCells++;
					}
				}
			}
			int winScore = 1000000 + freeCells;
			if (winner == FieldInterface.Winner.MYSELF) {
				return winScore;
			}
			return -winScore;
		}
		if (winner == FieldInterface.Winner.DRAW) {
			return 0;
		}

		// evaluate macroboard as a small board
		int ret = assesMiniBoard(node.field.getSmallBoard(), 0, 0) * 23;

		// evaluate each small board
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (node.field.smallBoardWinner(i, j) == FieldInterface.Winner.UNDECIDED) {
					ret += assesMiniBoard(node.field.getBigBoard(), 3 * i,
							3 * j);
				}
			}
		}
		return ret;
	}

	private int assesMiniBoard(int[][] board, int startRow, int startCol) {
		int playerCounter = 0;
		int opponentCounter = 0;

		int forPlayer = 0;
		int forOpponent = 0;
		// for all horizontal lines
		for (int i = 0; i < 3; i++) {
			forPlayer = 0;
			forOpponent = 0;
			if (board[startRow + i][startCol] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + i][startCol] == BotParser.mOpponentId)
				forOpponent++;
			if (board[startRow + i][startCol + 1] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + i][startCol + 1] == BotParser.mOpponentId)
				forOpponent++;
			if (board[startRow + i][startCol + 2] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + i][startCol + 2] == BotParser.mOpponentId)
				forOpponent++;
			if (forOpponent > 0 && forPlayer > 0)
				continue;
			if (forPlayer > 0) {
				if (forPlayer > 1)
					playerCounter += 7;
				playerCounter += 1;
			}
			if (forOpponent > 0) {
				if (forOpponent > 1)
					opponentCounter += 7;
				opponentCounter += 1;
			}
		}

		// for all vertical lines
		for (int i = 0; i < 3; i++) {
			forPlayer = 0;
			forOpponent = 0;
			if (board[startRow][startCol + i] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow][startCol + i] == BotParser.mOpponentId)
				forOpponent++;
			if (board[startRow + 1][startCol + i] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + 1][startCol + i] == BotParser.mOpponentId)
				forOpponent++;
			if (board[startRow + 2][startCol + i] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + 2][startCol + i] == BotParser.mOpponentId)
				forOpponent++;
			if (forOpponent > 0 && forPlayer > 0)
				continue;
			if (forPlayer > 0) {
				if (forPlayer > 1)
					playerCounter += 7;
				playerCounter += 1;
			}
			if (forOpponent > 0) {
				if (forOpponent > 1)
					opponentCounter += 7;
				opponentCounter += 1;
			}
		}

		// first diagonal
		forPlayer = 0;
		forOpponent = 0;
		for (int i = 0; i < 3; i++) {
			if (board[startRow + i][startCol + i] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + i][startCol + i] == BotParser.mOpponentId)
				forOpponent++;
		}
		if (!(forPlayer > 0 && forOpponent > 0)) {
			if (forPlayer > 0) {
				if (forPlayer > 1)
					playerCounter += 7;
				playerCounter += 1;
			}
			if (forOpponent > 0) {
				if (forOpponent > 1)
					opponentCounter += 7;
				opponentCounter += 1;
			}
		}

		// second diagonal
		forPlayer = 0;
		forOpponent = 0;
		for (int i = 0; i < 3; i++) {
			if (board[startRow + i][startCol + 2 - i] == BotParser.mBotId)
				forPlayer++;
			else if (board[startRow + i][startCol + 2 - i] == BotParser.mOpponentId)
				forOpponent++;
		}
		if (!(forPlayer > 0 && forOpponent > 0)) {
			if (forPlayer > 0) {
				if (forPlayer > 1)
					playerCounter += 7;
				playerCounter += 1;
			}
			if (forOpponent > 0) {
				if (forOpponent > 1)
					opponentCounter += 7;
				opponentCounter += 1;
			}
		}

		return playerCounter - opponentCounter;
	}

	@Override
	public Move preMinimax(FieldImplementation initialField) {
		if (initialField.getRoundNumber() < 2) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (initialField.getSmallBoard()[i][j] == -1) {
						int count = 0;
						for (int m = 3 * i; m < 3 * (i + 1); m++) {
							for (int n = 3 * j; n < 3 * (j + 1); n++) {
								if (initialField.getBigBoard()[m][n] > 0) {
									count++;
								}
							}
						}
						if (count == 0
								&& initialField.getBigBoard()[4 * i][4 * j] == 0) {
							return new Move(4 * i, 4 * j);
						}
					}
				}
			}
		}
		return null;
	}

}
