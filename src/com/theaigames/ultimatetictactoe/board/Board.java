// Copyright 2016 theaigames.com (developers@theaigames.com)

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

package com.theaigames.ultimatetictactoe.board;

public class Board {

	public final int COLS = 3;
	public final int ROWS = 3;

	private int[][] mBoard;
	private int[][] mMacroBoard;

	public String mLastError = "";
	private int mLastCol = 0;
	private int mLastRow = 0;

	public Board() {
		mBoard = new int[ROWS * COLS][ROWS * COLS];
		mMacroBoard = new int[ROWS][COLS];
		clearBoard();
	}

	public void clearBoard() {
		for (int i = 0; i < ROWS * COLS; i++) {
			for (int j = 0; j < ROWS * COLS; j++) {
				mBoard[i][j] = 0;
			}
		}

		// the first player can choose any mini board to play in
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				mMacroBoard[i][j] = -1;
			}
		}
	}

	/*
	 * Used for "pretty printing" the board
	 */
	public void dumpBoard() {
		for (int x = 0; x < ROWS * COLS; x++) {
			System.out.print("--");
		}
		System.out.println();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				System.out.print(mMacroBoard[i][j]);
				if (i < ROWS - 1 || j < COLS - 1) {
					System.out.print(",");
				}
			}
		}
		System.out.println();
		for (int x = 0; x < ROWS * COLS; x++) {
			System.out.print("--");
		}
		System.out.println();
		for (int i = 0; i < ROWS * COLS; i++) {
			for (int j = 0; j < ROWS * COLS; j++) {
				System.out.print(mBoard[i][j] + " ");
			}
			System.out.println();
		}
	}

	/*
	 * Dunno wtf is this :)
	 */
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	private int boardIsWon(int rowBegin, int colBegin, int[][] board) {
		int colEnd = colBegin + COLS;
		int rowEnd = rowBegin + ROWS;

		// check horizontal lines
		int player;
		for (int i = rowBegin; i < rowEnd; i++) {
			player = board[i][colBegin];
			if (board[i][colBegin] == board[i][colBegin + 1]
					&& board[i][colBegin + 1] == board[i][colBegin + 2]) {
				if (player > 0) {
					return player;
				}
			}
		}

		// check vertical lines
		for (int i = colBegin; i < colEnd; i++) {
			player = board[rowBegin][i];
			if (board[rowBegin][i] == board[rowBegin + 1][i]
					&& board[rowBegin + 1][i] == board[rowBegin + 2][i]) {
				if (player > 0) {
					return player;
				}
			}
		}

		// check diagonals
		player = board[rowBegin][colBegin];
		if (board[rowBegin][colBegin] == board[rowBegin + 1][colBegin + 1]
				&& board[rowBegin + 1][colBegin + 1] == board[rowBegin + 2][colBegin + 2]) {
			if (player > 0) {
				return player;
			}
		}

		player = board[rowBegin][colEnd - 1];
		if (board[rowBegin][colEnd - 1] == board[rowBegin + 1][colEnd - 2]
				&& board[rowBegin + 1][colEnd - 2] == board[rowBegin + 2][colEnd - 3]) {
			if (player > 0) {
				return player;
			}
		}

		return 0;
	}

	private boolean miniBoardIsFull(int row, int col) {
		int rowBegin = ((int) row / ROWS) * ROWS;
		int rowEnd = rowBegin + ROWS;
		int colBegin = ((int) col / COLS) * COLS;
		int colEnd = colBegin + COLS;
		for (int i = rowBegin; i < rowEnd; i++) {
			for (int j = colBegin; j < colEnd; j++) {
				if (mBoard[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/* To hardcode or not to hardcode, this is the question...*/
	private int getNextIndex(int currentIndex) {
		int begin = ((int) currentIndex / ROWS) * ROWS;
		if (currentIndex == begin) {
			return 0;
		} else if (currentIndex == begin + 1) {
			return 3;
		}
		return 6;
	}

	private void updateMacroBoard(int row, int col) {
		// make active miniboards inactive
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (mMacroBoard[i][j] == -1) {
					mMacroBoard[i][j] = 0;
				}
			}
		}

		// check if current miniboard has a winner
		int player = boardIsWon(((int) row / ROWS) * ROWS, ((int) col / COLS) * COLS,
				mBoard);
		if (player > 0) {
			mMacroBoard[(int) row / ROWS][(int) col / COLS] = player;
		}

		// check if the next miniboard is full or is already decided
		int nextRow = getNextIndex(row);
		int nextCol = getNextIndex(col);
		if (mMacroBoard[(int) nextRow / ROWS][(int) nextCol / COLS] == 0
				|| miniBoardIsFull(nextRow, nextCol)) {
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					if (mMacroBoard[i][j] == 0
							&& !miniBoardIsFull(ROWS * i, COLS * j)) {
						mMacroBoard[i][j] = -1;
					}
				}
			}
		} else {
			mMacroBoard[(int) nextRow / ROWS][(int) nextCol / COLS] = -1;
		}
	}

	/**
	 * Places a move (X or 0) on the board (if valid).
	 * 
	 * @return : true if the move is valid, otherwise false
	 */
	public Boolean placeMove(int row, int col, int player) {
		mLastError = "";
		mLastCol = col;
		mLastRow = row;

		if ((col >= 0 && col < ROWS * COLS) && (row >= 0 && row < ROWS * COLS)) {
			if (mMacroBoard[(int) row / ROWS][(int) col / COLS] == -1) {
				if (mBoard[row][col] == 0) {
					mBoard[row][col] = player;
					updateMacroBoard(row, col);
					return true;
				} else {
					mLastError = "Cell already occupied ("
							+ row + ", " + col + ").";
				}
			} else {
				mLastError = "Played in the wrong mini board ("
						+ row / ROWS + ", " + col / COLS + ")";
			}
		} else {
			mLastError = "Move out of bounds. (" + row + ", " + col + ")";
		}
		System.out.println(mLastError);
		return false;
	}

	/**
	 * Returns reason why placeMove returns false
	 * 
	 * @param args
	 *            :
	 * @return : reason why placeMove returns false
	 */
	public String getLastError() {
		return mLastError;
	}

	/**
	 * Returns last inserted column
	 * 
	 * @param args
	 *            :
	 * @return : last inserted column
	 */
	public int getLastCol() {
		return mLastCol;
	}

	public int getLastRow() {
		return mLastRow;
	}

	@Override
	/**
	 * Creates comma separated String with player names for every cell.
	 * @param args : 
	 * @return : String with player names for every cell, or 'empty' when cell is empty.
	 */
	public String toString() {
		String r = "";
		for (int i = 0; i < ROWS * COLS; i++) {
			for (int j = 0; j < ROWS * COLS; j++) {
				r += mBoard[i][j];
				if (i != ROWS * COLS - 1 || j != ROWS * COLS - 1) {
					r += ",";
				}
			}
		}
		return r;
	}

	public String macroBoardString() {
		String r = "";
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				r += mMacroBoard[i][j];
				if (i != ROWS - 1 || j != COLS - 1) {
					r += ",";
				}
			}
		}
		return r;
	}

	/**
	 * Checks if there is a winner, if so, returns player id.
	 * 
	 * @param args
	 *            :
	 * @return : Returns player id if there is a winner, otherwise returns 0.
	 */
	public int getWinner() {
		// check if macroboard has a winner
		return boardIsWon(0, 0, mMacroBoard);
	}

	public boolean isFull() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (mMacroBoard[i][j] <= 0 && !miniBoardIsFull(ROWS * i, COLS * j)) {
					return false;
				}
			}
		}
		return true;
	}
}
