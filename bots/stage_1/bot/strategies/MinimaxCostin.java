package bot.strategies;

import java.util.ArrayList;

import bot.AbstractMinimax;
import bot.BotParser;
import bot.Field;
import bot.FieldInterface;
import bot.Move;

public class MinimaxCostin extends AbstractMinimax {

	public MinimaxCostin(int maxDepth) {
		super(maxDepth);
	}

	@Override
	public int heuristic(Node node) {
		int score = computeBoardScore(node);
		return score;
	}

	private int computeBoardScore(Node node) {
		Field field = node.field;
		// winning score based on current depth
		FieldInterface.Winner winner = field.bigBoardWinner();
		if (winner == FieldInterface.Winner.MYSELF) {
			return Integer.MAX_VALUE - 6000 * node.level;
		} else if (winner == FieldInterface.Winner.OPPONENT) {
			return Integer.MIN_VALUE + 6000 * node.level;
		}

		int score = 0;
		ArrayList<Move> games = new ArrayList<Move>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (field.getSmallBoard()[i][j] == BotParser.mBotId) {
					if (i == j) {
						if (i == 1)
							score += 24 * 4;
						else
							score += 24 * 3;
					} else if ((i == 2 && j == 0) || (i == 0 && j == 2))
						score += 24 * 3;
					else
						score += 24 * 2;
				} else if (field.getSmallBoard()[i][j] == BotParser.mOpponentId) {
					if (i == j) {
						if (i == 1)
							score -= 24 * 4;
						else
							score -= 24 * 3;
					} else if ((i == 2 && j == 0) || (i == 0 && j == 2))
						score -= 24 * 3;
					else
						score -= 24 * 2;
				} else
					games.add(new Move(i, j));
			}

		}
		
		score += computeScore(field, games);
		return score;

	}

	private int computeScore(Field field, ArrayList<Move> games) {

		int score, fscore = 0;
		Move test;
		int[][] smallGame = new int[3][3];
		for (int k1 = 0; k1 < 3; k1++) {
			for (int k2 = 0; k2 < 3; k2++) {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						smallGame[i][j] = field.getBigBoard()[k1 * 3 + i][k2
								* 3 + j];
					}
				}
				test = new Move(k1, k2);
				if (!games.contains(test))
					continue;
				score = 0;
				for (int it1 = 0; it1 < 3; it1++) {
					for (int it2 = 0; it2 < 3; it2++) {
						if (smallGame[it1][it2] == BotParser.mBotId) {
							if (it1 == it2) {
								if (it1 == 1)
									score += 4;
								else
									score += 3;
							} else if ((it1 == 2 && it2 == 0)
									|| (it1 == 0 && it2 == 2))
								score += 3;
							else
								score += 2;
						} else if (smallGame[it1][it2] == BotParser.mOpponentId) {
							if (it1 == it2) {
								if (it1 == 1)
									score -= 4;
								else
									score -= 3;
							} else if ((it1 == 2 && it2 == 0)
									|| (it1 == 0 && it2 == 2))
								score -= 3;
							else
								score -= 2;
						}
						
						//TODO: more testing to see if redundant or not
						int testut = 0;
						for (int it3 = 0; it3 < 3; it3++)
							for (int it4 = 0; it4 < 3; it4++) {
								if (field.getSmallBoard()[it3][it4] == -1)
									testut++;
								if (testut > 1) {
									score = score - (int)(0.15 * score);
								}
							}
					}

				}
				if (k1 == k2) {
					if (k1 == 1)
						fscore += 4 * score;
					else
						fscore += 3 * score;
				} else if ((k1 == 2 && k2 == 0) || (k2 == 0 && k1 == 2))
					fscore += 3 * score;
				else
					fscore += 2 * score;

			}

		}
		return fscore;
	}

	@Override
	public Move preMinimax(Field initialField) {
		if (initialField.getMoveNumber() == 1) {
			return new Move(4, 4);
		}
		if (initialField.getRoundNumber() <= 6) {
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