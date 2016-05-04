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

package com.theaigames.ultimatetictactoe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.theaigames.game.player.AbstractPlayer;
import com.theaigames.game.GameHandler;
import com.theaigames.ultimatetictactoe.field.Field;
import com.theaigames.ultimatetictactoe.moves.Move;
import com.theaigames.ultimatetictactoe.moves.MoveResult;
import com.theaigames.ultimatetictactoe.player.Player;

public class Processor implements GameHandler {

	// used for playing a simulation of the game afterwards
	private List<MoveResult> mMoveResults;
	private int[] avgMoveTime = new int[2];

	private int mRoundNumber = 1;
	private List<Player> mPlayers;
	private List<Move> mMoves;
	private Field mField;
	private int mGameOverByPlayerErrorPlayerId = 0;

	public Processor(List<Player> players, Field field) {
		mPlayers = players;
		mField = field;
		mMoves = new ArrayList<Move>();
		mMoveResults = new ArrayList<MoveResult>();

		/* Create first move with empty field */
		Move move = new Move(mPlayers.get(0));
		mMoves.add(move);
		MoveResult moveResult = new MoveResult.Builder(mPlayers.get(0))
				.withFieldData(mField).build();
		mMoveResults.add(moveResult);
	}

	@Override
	public void playRound(int roundNumber) {
		for (Player player : mPlayers) {

			this.mField.mRoundNr = mRoundNumber;
			this.mField.mMoveNr = mMoves.size();
			player.sendUpdate("round", mRoundNumber);
			player.sendUpdate("move", mMoves.size());
			player.sendUpdate("field", mField.toString());
			player.sendUpdate("macroboard", mField.macroboardString());

			if (getWinner() == null && !mField.isFull()) {
				String response = player.requestMove("move");
				Move move = new Move(player);
				if (parseResponse(response, player)) {
					move.setPosition(mField.getLastRow(), mField.getLastCol());
					move.setIllegalMove(mField.getLastError());
					mMoves.add(move);
					mMoveResults.add(new MoveResult.Builder(player)
							.withFieldData(mField).build());
				} else {
					mMoveResults.add(new MoveResult.Builder(player)
							.withFieldData(mField).build());

					player.sendUpdate("field", mField.toString());
					player.sendUpdate("macroboard", mField.macroboardString());
					response = player.requestMove("move");

					if (parseResponse(response, player)) {
						move = new Move(player);
						move.setPosition(mField.getLastRow(), mField
								.getLastCol());
						mMoves.add(move);

						mMoveResults.add(new MoveResult.Builder(player)
								.withFieldData(mField).build());
					} else {
						mMoveResults.add(new MoveResult.Builder(player)
								.withFieldData(mField).build());

						player.sendUpdate("field", mField.toString());
						player.sendUpdate("macroboard", mField
								.macroboardString());
						response = player.requestMove("move");

						if (parseResponse(response, player)) {
							move = new Move(player);
							move.setPosition(mField.getLastRow(), mField
									.getLastCol());
							mMoves.add(move);

							mMoveResults.add(new MoveResult.Builder(player)
									.withFieldData(mField).build());
						} else { /* Too many errors, other player wins */
							mMoveResults.add(new MoveResult.Builder(player)
									.withFieldData(mField).build());
							mGameOverByPlayerErrorPlayerId = player.getId();
						}
					}
				}

				player.sendUpdate("field", mField.toString());
				player.sendUpdate("macroboard", mField.macroboardString());

			}
		}
		mRoundNumber++;
	}

	/**
	 * Parses player response and inserts disc in field
	 * 
	 * @param args
	 *            : command line arguments passed on running of application
	 * @return : true if valid move, otherwise false
	 */
	private Boolean parseResponse(String r, Player player) {
		String[] parts = r.split(" ");
		if (parts.length >= 3 && parts[0].equals("place_move")) {
			int col = Integer.parseInt(parts[1]);
			int row = Integer.parseInt(parts[2]);
			if (mField.placeMove(row, col, player.getId())) {
				return true;
			}
		}
		mField.mLastError = "Unknown command";
		return false;
	}

	@Override
	public int getRoundNumber() {
		return this.mRoundNumber;
	}

	@Override
	public AbstractPlayer getWinner() {
		int winner = mField.getWinner();
		if (mGameOverByPlayerErrorPlayerId > 0) { /*
												 * Game over due to too many
												 * player errors. Look up the
												 * other player, which became
												 * the winner
												 */
			for (Player player : mPlayers) {
				if (player.getId() != mGameOverByPlayerErrorPlayerId) {
					return player;
				}
			}
		}
		if (winner != 0) {
			for (Player player : mPlayers) {
				if (player.getId() == winner) {
					return player;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a List of Moves played in this game
	 * 
	 * @param args
	 *            :
	 * @return : List with Move objects
	 */
	public List<Move> getMoves() {
		return mMoves;
	}

	public Field getField() {
		return mField;
	}

	@Override
	public boolean isGameOver() {
		return (getWinner() != null) || (mField.isFull());
	}

	@Override
	public String getPlayedGame() {
		exportData();
		String result = "";

		if (getWinner() != null) {
			result += "Conclusion: WINNER " + getWinner().getName() + " \n\n";
		} else {
			result += "Conclusion: WINNER DRAW \n\n";
		}
		
		return result
				+ "Average thinking times:\nplayer1: " + avgMoveTime[0]
				+ "ms\nplayer2: " + avgMoveTime[1] + "ms\n";
	}

	private void exportData() {
		// should create a new directory here with a timestamp

		// export field data
		int count = 0;
		String jsonDirectoryFields = UltimateTicTacToe.EXPORT_DIRECTORY
				+ "fields" + File.separator;
		for (MoveResult r : mMoveResults) {
			try {

				FileWriter writer = new FileWriter(jsonDirectoryFields
						+ "field-" + count + ".json");
				writer.write(r.getJsonField());
				writer.close();
				count++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// export thinking time and move data for each player
		// all in one file :) as an array of json objects

		// this code also computes the average thinking time for each player
		int player1Moves = 0;
		int player2Moves = 0;

		count = 0;
		String jsonDirectoryThinking = UltimateTicTacToe.EXPORT_DIRECTORY
				+ "thinking" + File.separator;
		FileWriter writer;
		try {
			writer = new FileWriter(jsonDirectoryThinking + "thinking.json");
			// start json array
			writer.write("[");
			for (int i = 0; i < mMoveResults.size(); i++) {
				try {
					writer.write(mMoveResults.get(i).getJsonMoveResult());
					if (i < mMoveResults.size() - 1) {
						writer.write(",");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (mMoveResults.get(i).getPlayerId() == 1) {
					player1Moves++;
					avgMoveTime[0] += mMoveResults.get(i).getThinkingTime();
				} else {
					player2Moves++;
					avgMoveTime[1] += mMoveResults.get(i).getThinkingTime();
				}
			}
			// end json array
			writer.write("]");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (player1Moves != 0 && player2Moves != 0) {
			avgMoveTime[0] /= player1Moves;
			avgMoveTime[1] /= player2Moves;
		}
		
		// server error dump
		for (Player p : mPlayers) {
			System.err.println("Player" + p.getId() + " dump");
			System.err.println("==============================");
			System.err.println(p.getBot().getDump());
		}
	}
}
