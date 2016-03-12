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

	public final int CELLS = 9;

	/*
	 * mBoard[miniboard(0-8)][cell(0-8)] -> {0, 1, 2} 0 -> cell is empty 1 ->
	 * player1 owns this cell 2 -> player2 owns this cell
	 */
	private int[][] mBoard;

	/*
	 * mMacroBoard[miniboard(0-8)] = {-1, 0, 1, 2} -1 -> the current player must
	 * play in this mini board 0 -> the mini board is a draw 1 -> the mini board
	 * is won by player1 2 -> the mini board is won by player2
	 */
	private int[] mMacroBoard;

	public String mLastError = "";
	private int mLastMiniBoard = 0;
	private int mLastCell = 0;

	public Board() {
		mBoard = new int[CELLS][CELLS];
		mMacroBoard = new int[CELLS];
		clearBoard();
	}

	public void clearBoard() {
		for (int x = 0; x < CELLS; x++) {
			for (int y = 0; y < CELLS; y++) {
				mBoard[x][y] = 0;
			}
		}

		// the first player can choose any mini board to play in
		for (int x = 0; x < CELLS; x++) {
			mMacroBoard[x] = -1;
		}
	}

	/*
	 * Used for "pretty printing" the board
	 */
	public void dumpBoard() {
		for (int x = 0; x < CELLS; x++) {
			System.out.print("--");
		}
		System.out.println("\n");
		for (int x = 0; x < CELLS; x++) {
			System.out.println(mMacroBoard[x]);
			if (x < CELLS - 1) {
				System.out.println(",");
			}
		}
		System.out.println("\n");
		for (int x = 0; x < CELLS; x++) {
			System.out.print("--");
		}
		System.out.print("\n");
		for (int y = 0; y < CELLS; y++) {
			for (int x = 0; x < CELLS; x++) {
				System.out.print(mBoard[x][y]);
				if (x < CELLS - 1) {
					System.out.print(",");
				}
			}
			System.out.print("\n");
		}
	}

	/*
	 * Dunno wtf is this :)
	 */
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	private int isWon(int[] board) {
		// check horizontal lines
		int player;
		for (int i = 0; i < 3; i++) {
			player = board[3 * i];
			if (player > 0
					&& ((board[3 * i] == board[3 * i + 1]) && (board[3 * i + 1] == board[3 * i + 2]))) {
				return player;
			}
		}
		// check vertical lines
		for (int i = 0; i < 3; i++) {
			player = board[i];
			if (player > 0
					&& ((board[i] == board[i + 3]) && (board[i + 3] == board[i + 6]))) {
				return player;
			}
		}
		// check diagonals
		player = board[0];
		if (player > 0 && (board[0] == board[4]) && (board[4] == board[8])) {
			return player;
		}
		player = board[2];
		if (player > 0 && (board[2] == board[4]) && (board[4] == board[6])) {
			return player;
		}

		// check if the board has any empty cells
		for (int i = 0; i < CELLS; i++) {
			if (board[i] == 0)
				return 0;
		}

		return -1;
	}

	private void updateMacroBoard(int miniBoard, int cell) {
		// check if current miniboard has a winner
		int player = isWon(mBoard[miniBoard]);
		if (player > 0) {
			mMacroBoard[miniBoard] = player;
		}

		// check if the next miniboard is full or is already decided
		player = isWon(mBoard[cell]);
		if (player == -1 || player > 0) {
			for (int i = 0; i < CELLS; i++) {
				if (mMacroBoard[i] == 0 && isWon(mBoard[i]) == 0) {
					mMacroBoard[i] = -1;
				}
			}
		} else {
			mMacroBoard[cell] = -1;
		}
	}

	/**
	 * Places a move (X or 0) on the board (if valid).
	 * 
	 * @return : true if the move is valid, otherwise false
	 */
	public Boolean placeMove(int miniBoard, int cell, int player) {
		mLastError = "";
		mLastMiniBoard = miniBoard;
		mLastCell = cell;

		if ((miniBoard >= 0 && miniBoard < CELLS)
				&& (cell >= 0 && cell < CELLS)) {
			if (mMacroBoard[miniBoard] == -1) {
				if (mBoard[miniBoard][cell] == 0) {
					mBoard[miniBoard][cell] = player;
					updateMacroBoard(miniBoard, cell);
					return true;
				} else {
					mLastError = "Cell already occupied (" + miniBoard + ", "
							+ cell + ").";
				}
			} else {
				mLastError = "Played in the wrong mini board (" + miniBoard
						+ ")";
			}
		} else {
			mLastError = "Move out of bounds. (" + miniBoard + ", " + cell
					+ ")";
		}
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
	public int getLastMiniBoard() {
		return mLastMiniBoard;
	}

	public int getLastCell() {
		return mLastCell;
	}

	@Override
	/**
	 * Creates comma separated String with player names for every cell.
	 * @param args : 
	 * @return : String with player names for every cell, or 'empty' when cell is empty.
	 */
	public String toString() {
		String r = "";
		for (int y = 0; y < CELLS; y++) {
			for (int x = 0; x < CELLS; x++) {
				r += mBoard[x][y];
				if (y < CELLS - 1) {
					r += ",";
				}
			}
		}
		return r;
	}

	public String macroBoardString() {
		String r = "";
		for (int x = 0; x < CELLS; x++) {
			r += mMacroBoard[x];
			if (x < CELLS - 1) {
				r += ",";
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
		return isWon(mMacroBoard);
	}
}
