package bot;

import java.util.ArrayList;

public interface FieldInterface {
	public enum Winner {
		MYSELF, OPPONENT, DRAW, UNDECIDED
	};

	public Winner bigBoardWinner();

	public Winner smallBoardWinner(int macroboardRow, int macroboardCol);

	public boolean isSmallBoardActive(int macroboardRow, int macroboardCol);

	public ArrayList<Move> getAvailableMoves();

	public boolean placeMove(int boardRow, int boardCol, boolean myself);

	public int[][] getBigBoard();

	public int[][] getSmallBoard();

	public int getMoveNumber();

	public int getRoundNumber();

	/* Communication methods */
	public void parseGameData(String key, String value);

	public void parseBigBoardFromString(String s);

	public void parseSmallBoardFromString(String s);

}
