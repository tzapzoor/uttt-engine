package bot;

import java.util.ArrayList;

public class Field implements FieldInterface {
	public final int COLS = 9, ROWS = 9;

	private int mRoundNr;
	private int mMoveNr;
	private int[][] bigBoard;
	private int[][] smallBoard;

	public Field() {
		bigBoard = new int[ROWS][COLS];
		smallBoard = new int[ROWS / 3][COLS / 3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				smallBoard[i][j] = -1;
			}
		}
	}

	// copy constructor
	public Field(Field field) {
		this.mRoundNr = field.mRoundNr;
		this.mMoveNr = field.mMoveNr;

		this.bigBoard = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				this.bigBoard[i][j] = field.bigBoard[i][j];
			}
		}
		this.smallBoard = new int[ROWS / 3][COLS / 3];
		for (int i = 0; i < ROWS / 3; i++) {
			for (int j = 0; j < COLS / 3; j++) {
				this.smallBoard[i][j] = field.smallBoard[i][j];
			}
		}
	}

	@Override
	public Winner bigBoardWinner() {
		int i = 0;
		int j = 0;
		int id = -2;
		// horizontal lines
		if (smallBoard[i][j] == smallBoard[i][j + 1]
				&& smallBoard[i][j + 1] == smallBoard[i][j + 2]) {
			if (smallBoard[i][j] > 0) {
				id = smallBoard[i][j];
			}
		}

		else if (smallBoard[i + 1][j] == smallBoard[i + 1][j + 1]
				&& smallBoard[i + 1][j + 1] == smallBoard[i + 1][j + 2]) {
			if (smallBoard[i + 1][j] > 0) {
				id = smallBoard[i + 1][j];
			}
		}

		else if (smallBoard[i + 2][j] == smallBoard[i + 2][j + 1]
				&& smallBoard[i + 2][j + 1] == smallBoard[i + 2][j + 2]) {
			if (smallBoard[i + 2][j] > 0) {
				id = smallBoard[i + 2][j];
			}
		}

		// vertical lines
		else if (smallBoard[i][j] == smallBoard[i + 1][j]
				&& smallBoard[i + 1][j] == smallBoard[i + 2][j]) {
			if (smallBoard[i][j] > 0) {
				id = smallBoard[i][j];
			}
		}

		else if (smallBoard[i][j + 1] == smallBoard[i + 1][j + 1]
				&& smallBoard[i + 1][j + 1] == smallBoard[i + 2][j + 1]) {
			if (smallBoard[i][j + 1] > 0) {
				id = smallBoard[i][j + 1];
			}
		}

		else if (smallBoard[i][j + 2] == smallBoard[i + 1][j + 2]
				&& smallBoard[i + 1][j + 2] == smallBoard[i + 2][j + 2]) {
			if (smallBoard[i][j + 2] > 0) {
				id = smallBoard[i][j + 2];
			}
		}

		// diagonal
		else if (smallBoard[i][j] == smallBoard[i + 1][j + 1]
				&& smallBoard[i + 1][j + 1] == smallBoard[i + 2][j + 2]) {
			if (smallBoard[i][j] > 0) {
				id = smallBoard[i][j];
			}
		}

		else if (smallBoard[i + 2][j] == smallBoard[i + 1][j + 1]
				&& smallBoard[i + 1][j + 1] == smallBoard[i][j + 2]) {
			if (smallBoard[i + 2][j] > 0) {
				id = smallBoard[i + 2][j];
			}
		}

		if (bot.BotParser.mBotId == id)
			return Winner.MYSELF;
		else if (id == BotParser.mOpponentId)
			return Winner.OPPONENT;

		for (int it1 = 0; it1 < 3; it1++) {
			for (int it2 = 0; it2 < 3; it2++) {
				if (smallBoardWinner(it1, it2) == Winner.UNDECIDED)
					return Winner.UNDECIDED;
			}
		}
		return Winner.DRAW;
	}

	@Override
	public Winner smallBoardWinner(int smallBoardRow, int smallBoardCol) {
		int i = smallBoardRow * 3;
		int j = smallBoardCol * 3;
		int id = -2;
		// horizontal lines
		if (bigBoard[i][j] == bigBoard[i][j + 1]
				&& bigBoard[i][j + 1] == bigBoard[i][j + 2]) {
			if (bigBoard[i][j] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i][j];
				id = bigBoard[i][j];
			}
		}

		else if (bigBoard[i + 1][j] == bigBoard[i + 1][j + 1]
				&& bigBoard[i + 1][j + 1] == bigBoard[i + 1][j + 2]) {
			if (bigBoard[i + 1][j] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i +
				// 1][j];
				id = bigBoard[i + 1][j];
			}
		}

		else if (bigBoard[i + 2][j] == bigBoard[i + 2][j + 1]
				&& bigBoard[i + 2][j + 1] == bigBoard[i + 2][j + 2]) {
			if (bigBoard[i + 2][j] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i +
				// 2][j];
				id = bigBoard[i + 2][j];
			}
		}

		// vertical lines
		else if (bigBoard[i][j] == bigBoard[i + 1][j]
				&& bigBoard[i + 1][j] == bigBoard[i + 2][j]) {
			if (bigBoard[i][j] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i][j];
				id = bigBoard[i][j];
			}
		}

		else if (bigBoard[i][j + 1] == bigBoard[i + 1][j + 1]
				&& bigBoard[i + 1][j + 1] == bigBoard[i + 2][j + 1]) {
			if (bigBoard[i][j + 1] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i][j +
				// 1];
				id = bigBoard[i][j + 1];
			}
		}

		else if (bigBoard[i][j + 2] == bigBoard[i + 1][j + 2]
				&& bigBoard[i + 1][j + 2] == bigBoard[i + 2][j + 2]) {
			if (bigBoard[i][j + 2] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i][j +
				// 2];
				id = bigBoard[i][j + 2];
			}
		}

		// diagonal
		else if (bigBoard[i][j] == bigBoard[i + 1][j + 1]
				&& bigBoard[i + 1][j + 1] == bigBoard[i + 2][j + 2]) {
			if (bigBoard[i][j] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i][j];
				id = bigBoard[i][j];
			}
		}

		else if (bigBoard[i + 2][j] == bigBoard[i + 1][j + 1]
				&& bigBoard[i + 1][j + 1] == bigBoard[i][j + 2]) {
			if (bigBoard[i + 2][j] > 0) {
				// smallBoard[smallBoardRow][smallBoardCol] = bigBoard[i +
				// 2][j];
				id = bigBoard[i + 2][j];
			}
		}

		if (bot.BotParser.mBotId == id)
			return Winner.MYSELF;
		else if (id == BotParser.mOpponentId)
			return Winner.OPPONENT;

		for (int it1 = i; it1 < i + 3; it1++) {
			for (int it2 = j; it2 < j + 3; it2++) {
				if (bigBoard[it1][it2] == 0)
					return Winner.UNDECIDED;
			}
		}
		return Winner.DRAW;
	}

	@Override
	public boolean isSmallBoardActive(int smallBoardRow, int smallBoardCol) {
		return smallBoard[smallBoardRow][smallBoardCol] == -1;
	}

	@Override
	public ArrayList<Move> getAvailableMoves() {

		ArrayList<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (isSmallBoardActive(i / 3, j / 3) && bigBoard[i][j] == 0
						&& smallBoardWinner(i / 3, j / 3) == Winner.UNDECIDED) {
					moves.add(new Move(i, j));
				}
			}
		}
		return moves;
	}

	private void updateSmallBoard(int bigBoardRow, int bigBoardCol) {
		// deactivate any active small games
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (isSmallBoardActive(i, j)) {
					smallBoard[i][j] = 0;
				}
			}
		}

		int smallBoardRow = bigBoardRow / 3;
		int smallBoardCol = bigBoardCol / 3;

		// decide current small game
		Winner res = smallBoardWinner(smallBoardRow, smallBoardCol);
		switch (res) {
		case MYSELF:
			smallBoard[smallBoardRow][smallBoardCol] = BotParser.mBotId;
			break;
		case OPPONENT:
			smallBoard[smallBoardRow][smallBoardCol] = BotParser.mOpponentId;
			break;
		case DRAW:
		case UNDECIDED:
			smallBoard[smallBoardRow][smallBoardCol] = 0;
			break;
		}

		Winner bigGameWinner = bigBoardWinner();
		if (bigGameWinner != Winner.UNDECIDED) {
			return;
		}

		// finds out the next small board to play in and checks whether it's
		// available
		int nextSmallBoardRow = bigBoardRow - (bigBoardRow / 3) * 3;
		int nextSmallBoardCol = bigBoardCol - (bigBoardCol / 3) * 3;

		if (smallBoardWinner(nextSmallBoardRow, nextSmallBoardCol) == Winner.UNDECIDED) {
			// activate next small game
			smallBoard[nextSmallBoardRow][nextSmallBoardCol] = -1;
		} else {
			// activate all undecided small games
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (smallBoardWinner(i, j) == Winner.UNDECIDED) {
						smallBoard[i][j] = -1;
					}
				}
			}
		}
	}

	@Override
	public boolean placeMove(int bigBoardRow, int bigBoardCol, boolean myself) {
		if (!isSmallBoardActive(bigBoardRow / 3, bigBoardCol / 3))
			return false;
		if (bigBoard[bigBoardRow][bigBoardCol] != 0)
			return false;

		bigBoard[bigBoardRow][bigBoardCol] = (myself) ? BotParser.mBotId
				: BotParser.mOpponentId;

		updateSmallBoard(bigBoardRow, bigBoardCol);

		return true;
	}

	@Override
	public int[][] getBigBoard() {
		return bigBoard;
	}

	@Override
	public int[][] getSmallBoard() {
		return smallBoard;
	}

	@Override
	public int getMoveNumber() {
		return mMoveNr;
	}

	@Override
	public int getRoundNumber() {
		return mRoundNr;
	}

	@Override
	public void parseGameData(String key, String value) {
		if (key.equals("round")) {
			mRoundNr = Integer.parseInt(value);
		} else if (key.equals("move")) {
			mMoveNr = Integer.parseInt(value);
		} else if (key.equals("field")) {
			parseBigBoardFromString(value); /* Parse Field with data */
		} else if (key.equals("macroboard")) {
			parseSmallBoardFromString(value); /* Parse macroboard with data */
		}

	}

	@Override
	public void parseBigBoardFromString(String s) {
		s = s.replace(";", ",");
		String[] r = s.split(",");
		int counter = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				bigBoard[i][j] = Integer.parseInt(r[counter]);
				counter++;
			}
		}

	}

	@Override
	public void parseSmallBoardFromString(String s) {
		String[] r = s.split(",");
		int counter = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				smallBoard[i][j] = Integer.parseInt(r[counter]);
				counter++;
			}
		}
	}
}
