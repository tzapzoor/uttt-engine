// // Copyright 2016 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//  
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package bot;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

//import javax.naming.ldap.ManageReferralControl;

//import com.google.gson.Gson;

/**
 * Field class
 * 
 * Handles everything that has to do with the field, such as storing the current
 * state and performing calculations on the field.
 * 
 * @author Jim van Eeden <jim@starapple.nl>, Joost de Meij <joost@starapple.nl>
 */

public class Field {
	private int mRoundNr;
	private int mMoveNr;
	private int[][] mBoard;
	private int[][] mMacroboard;

	private final int COLS = 9, ROWS = 9;
	private String mLastError = "";

	public Field() {
		mBoard = new int[ROWS][COLS];
		mMacroboard = new int[ROWS / 3][COLS / 3];
		clearBoard();
	}

	// copy constructor
	public Field(Field field) {
		this.mRoundNr = field.mRoundNr;
		this.mMoveNr = field.mMoveNr;

		this.mBoard = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				this.mBoard[i][j] = field.mBoard[i][j];
			}
		}
		this.mMacroboard = new int[ROWS / 3][COLS / 3];
		for (int i = 0; i < ROWS / 3; i++) {
			for (int j = 0; j < COLS / 3; j++) {
				this.mMacroboard[i][j] = field.mMacroboard[i][j];
			}
		}
		this.mLastError = field.mLastError;
	}

	/**
	 * Parse data about the game given by the engine
	 * 
	 * @param key
	 *            : type of data given
	 * @param value
	 *            : value
	 */
	public void parseGameData(String key, String value) {
		if (key.equals("round")) {
			mRoundNr = Integer.parseInt(value);
		} else if (key.equals("move")) {
			mMoveNr = Integer.parseInt(value);
		} else if (key.equals("field")) {
			parseFromString(value); /* Parse Field with data */
		} else if (key.equals("macroboard")) {
			parseMacroboardFromString(value); /* Parse macroboard with data */
		}
	}

	/**
	 * Initialise field from comma separated String
	 * 
	 * @param String
	 *            :
	 */
	public void parseFromString(String s) {
		s = s.replace(";", ",");
		String[] r = s.split(",");
		int counter = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				mBoard[i][j] = Integer.parseInt(r[counter]);
				counter++;
			}
		}
	}

	/**
	 * Initialise macroboard from comma separated String
	 * 
	 * @param String
	 *            :
	 */
	public void parseMacroboardFromString(String s) {
		String[] r = s.split(",");
		int counter = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mMacroboard[i][j] = Integer.parseInt(r[counter]);
				counter++;
			}
		}
	}

	public void clearBoard() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				mBoard[i][j] = 0;
			}
		}
	}

	public ArrayList<Move> getAvailableMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (isInActiveMicroboard(i, j) && mBoard[i][j] == 0) {
					moves.add(new Move(i, j));
				}
			}
		}

		return moves;
	}

	public Boolean isInActiveMicroboard(int i, int j) {
		return mMacroboard[(int) i / 3][(int) j / 3] == -1;
	}

	/**
	 * Returns reason why addMove returns false
	 * 
	 * @param args
	 *            :
	 * @return : reason why addMove returns false
	 */
	public String getLastError() {
		return mLastError;
	}

	@Override
	/**
	 * Creates comma separated String with player ids for the microboards.
	 * @param args : 
	 * @return : String with player names for every cell, or 'empty' when cell is empty.
	 */
	public String toString() {
		String r = "";
		int counter = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (counter > 0) {
					r += ",";
				}
				r += mBoard[i][j];
				counter++;
			}
		}
		return r;
	}

	/**
	 * Checks whether the field is full
	 * 
	 * @param args
	 *            :
	 * @return : Returns true when field is full, otherwise returns false.
	 */
	public boolean isFull() {
		// TODO: the board is full when all the miniboards are decided
		for (int i = 0; i < ROWS / 3; i++)
			for (int j = 0; j < COLS / 3; j++)
				if (mMacroboard[i][j] == 0 && !miniboardIsFull(3 * i, 3 * j))
					return false; // At least one cell is not filled
		// All cells are filled
		return true;
	}

	public int getNrColumns() {
		return COLS;
	}

	public int getNrRows() {
		return ROWS;
	}

	public boolean isEmpty() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (mBoard[i][j] > 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns the player id on given column and row
	 * 
	 * @param args
	 *            : int column, int row
	 * @return : int
	 */
	public int getPlayerId(int row, int column) {
		return mBoard[row][column];
	}

	// dumi's code starts from here.

	public int[][] getBoard() {
		return mBoard;
	}

	public int[][] getMacroboard() {
		return mMacroboard;
	}

	public int getMoveNr() {
		return mMoveNr;
	}

	public boolean makeMove(Move move, boolean isMyBot) {
		// the current move is illegal
		if (mBoard[move.getRow()][move.getCol()] != 0
				|| mMacroboard[move.getRow() / 3][move.getCol() / 3] != -1) {
			return false;
		}

		if (!isMyBot)
			mRoundNr++;
		mMoveNr++;

		if (isMyBot)
			mBoard[move.getRow()][move.getCol()] = BotParser.mBotId;
		else
			mBoard[move.getRow()][move.getCol()] = (BotParser.mBotId == 1) ? 0
					: 1;

		checkSmallWon(move.getRow(), move.getCol());
		setNextMinigame(move.getRow(), move.getCol());
		return true;
	}

	private void setNextMinigame(int row, int col) {
		// deactivate any active minigames
		for (int i = 0; i < ROWS / 3; i++) {
			for (int j = 0; j < COLS / 3; j++) {
				if (mMacroboard[i][j] == -1) {
					mMacroboard[i][j] = 0;
				}
			}
		}

		// top left corner of the current miniboard
		int beginRow = (row / 3) * 3;
		int beginCol = (col / 3) * 3;
		int nextRow = (row == beginRow) ? 0 : ((row == beginRow + 1) ? 3 : 6);
		int nextCol = (col == beginCol) ? 0 : ((col == beginCol + 1) ? 3 : 6);

		if (mMacroboard[nextRow / 3][nextCol / 3] == 0
				&& !miniboardIsFull(nextRow, nextCol)) {
			mMacroboard[nextRow / 3][nextCol / 3] = -1;

		} else {
			// activate all non-decided miniboards
			for (int i = 0; i < ROWS / 3; i++) {
				for (int j = 0; j < COLS / 3; j++) {
					if (mMacroboard[i][j] == 0
							&& !miniboardIsFull(3 * i, 3 * j)) {
						mMacroboard[i][j] = -1;
					}
				}
			}
		}

	}

	private boolean miniboardIsFull(int row, int col) {
		int rowBegin = (row / 3) * 3;
		int rowEnd = rowBegin + 3;
		int colBegin = (col / 3) * 3;
		int colEnd = colBegin + 3;
		for (int i = rowBegin; i < rowEnd; i++) {
			for (int j = colBegin; j < colEnd; j++) {
				if (mBoard[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public int checkSmallWon(int i, int j) {
		// set i and j to the top left corner of the current minigame
		i = (i / 3) * 3;
		j = (j / 3) * 3;
		// vertical lines
		if (mBoard[i][j] == mBoard[i][j + 1]
				&& mBoard[i][j + 1] == mBoard[i][j + 2]) {
			if (mBoard[i][j] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i][j];
				return mBoard[i][j];
			}
		}

		else if (mBoard[i + 1][j] == mBoard[i + 1][j + 1]
				&& mBoard[i + 1][j + 1] == mBoard[i + 1][j + 2]) {
			if (mBoard[i + 1][j] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i + 1][j];
				return mBoard[i + 1][j];
			}
		}

		else if (mBoard[i + 2][j] == mBoard[i + 2][j + 1]
				&& mBoard[i + 2][j + 1] == mBoard[i + 2][j + 2]) {
			if (mBoard[i + 2][j] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i + 2][j];
				return mBoard[i + 2][j];
			}
		}

		// horizontal lines
		else if (mBoard[i][j] == mBoard[i + 1][j]
				&& mBoard[i + 1][j] == mBoard[i + 2][j]) {
			if (mBoard[i][j] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i][j];
				return mBoard[i][j];
			}
		}

		else if (mBoard[i][j + 1] == mBoard[i + 1][j + 1]
				&& mBoard[i + 1][j + 1] == mBoard[i + 2][j + 1]) {
			if (mBoard[i][j + 1] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i][j + 1];
				return mBoard[i][j + 1];
			}
		}

		else if (mBoard[i][j + 2] == mBoard[i + 1][j + 2]
				&& mBoard[i + 1][j + 2] == mBoard[i + 2][j + 2]) {
			if (mBoard[i][j + 2] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i][j + 2];
				return mBoard[i][j + 2];
			}
		}

		// diagonal
		else if (mBoard[i][j] == mBoard[i + 1][j + 1]
				&& mBoard[i + 1][j + 1] == mBoard[i + 2][j + 2]) {
			if (mBoard[i][j] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i][j];
				return mBoard[i][j];
			}
		}

		else if (mBoard[i + 2][j] == mBoard[i + 1][j + 1]
				&& mBoard[i + 1][j + 1] == mBoard[i][j + 2]) {
			if (mBoard[i + 2][j] > 0) {
				mMacroboard[i / 3][j / 3] = mBoard[i + 2][j];
				return mBoard[i + 2][j];
			}
		}

		return -1;
	}

	public int getMyBigWin() {
		int myBotId = BotParser.mBotId, winCount = 0;

		for (int i = 0; i < COLS; i++)
			for (int j = 0; j < ROWS; j++)
				if (mBoard[i][j] == myBotId)
					winCount++;

		return winCount;
	}

	public int getOtherBigWin() {
		int myBotId = BotParser.mBotId, winCount = 0;

		for (int i = 0; i < COLS; i++)
			for (int j = 0; j < ROWS; j++)
				if (mBoard[i][j] != 0 && mBoard[i][j] != myBotId)
					winCount++;

		return winCount;
	}

	public int getMyBigScore(int[][] bigGameScore) {
		int myBotId = BotParser.mBotId, winCount = 0;

		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				if (mBoard[i][j] == myBotId)
					winCount += bigGameScore[i / 3][j / 3];

		return winCount;
	}

	public int getOtherBigScore(int[][] bigGameScore) {
		int myBotId = BotParser.mBotId, winCount = 0;

		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < ROWS; j++)
				if (mBoard[i][j] != 0 && mBoard[i][j] != myBotId)
					winCount += bigGameScore[i / 3][j / 3];

		return winCount;
	}

	public void exportFieldJson() {
		/*
		Gson gson = new Gson();
		String json = gson.toJson(this);
		String jsonDirectory;
		try { // write converted json data to a file named "file.json" String
			jsonDirectory = ".." + File.separator + "ultimate-tic-tac-toe-bot"
					+ File.separator + "PA Viewer" + File.separator + "data"
					+ File.separator + "fields" + File.separator;
			int count = new File(jsonDirectory).listFiles().length;
			FileWriter writer = new FileWriter(jsonDirectory + "field-" + count
					+ ".json");
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	*/
	}

	public String getFieldJson() {
		//Gson gson = new Gson();
		//return gson.toJson(this);

		 return "";
	}

	public int boardIsWon() {
		int rowBegin = 0;
		int colBegin = 0;
		int rowEnd = rowBegin + 3;
		int colEnd = colBegin + 3;

		// check horizontal lines
		int player;
		for (int i = rowBegin; i < rowEnd; i++) {
			player = mMacroboard[i][colBegin];
			if (mMacroboard[i][colBegin] == mMacroboard[i][colBegin + 1]
					&& mMacroboard[i][colBegin + 1] == mMacroboard[i][colBegin + 2]) {
				if (player > 0) {
					return player;
				}
			}
		}

		// check vertical lines
		for (int i = colBegin; i < colEnd; i++) {
			player = mMacroboard[rowBegin][i];
			if (mMacroboard[rowBegin][i] == mMacroboard[rowBegin + 1][i]
					&& mMacroboard[rowBegin + 1][i] == mMacroboard[rowBegin + 2][i]) {
				if (player > 0) {
					return player;
				}
			}
		}

		// check diagonals
		player = mMacroboard[rowBegin][colBegin];
		if (mMacroboard[rowBegin][colBegin] == mMacroboard[rowBegin + 1][colBegin + 1]
				&& mMacroboard[rowBegin + 1][colBegin + 1] == mMacroboard[rowBegin + 2][colBegin + 2]) {
			if (player > 0) {
				return player;
			}
		}

		player = mMacroboard[rowBegin][colEnd - 1];
		if (mMacroboard[rowBegin][colEnd - 1] == mMacroboard[rowBegin + 1][colEnd - 2]
				&& mMacroboard[rowBegin + 1][colEnd - 2] == mMacroboard[rowBegin + 2][colEnd - 3]) {
			if (player > 0) {
				return player;
			}
		}

		return 0;
	}

	public int getRoundNumber() {
		return mRoundNr;
	}

}