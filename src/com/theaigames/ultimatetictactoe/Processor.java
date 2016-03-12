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

import java.util.ArrayList;
import java.util.List;

import com.theaigames.game.player.AbstractPlayer;
import com.theaigames.game.GameHandler;
import com.theaigames.ultimatetictactoe.board.Board;
import com.theaigames.ultimatetictactoe.moves.Move;
import com.theaigames.ultimatetictactoe.moves.MoveResult;
import com.theaigames.ultimatetictactoe.player.Player;

public class Processor implements GameHandler {

	// used for playing a simulation of the game afterwards
	private List<MoveResult> mMoveResults;

	private int mRoundNumber = 1;
	private List<Player> mPlayers;
	private List<Move> mMoves;
	private Board mBoard;
	private int mGameOverByPlayerErrorPlayerId = 0;

	public Processor(List<Player> players, Board field) {
		mPlayers = players;
		mBoard = field;
		mMoves = new ArrayList<Move>();
		mMoveResults = new ArrayList<MoveResult>();

		/* Create first move with empty field */
		Move move = new Move(mPlayers.get(0));
		MoveResult moveResult = new MoveResult(mPlayers.get(0), mBoard,
				mPlayers.get(0).getId());
		mMoves.add(move);
		mMoveResults.add(moveResult);
	}

	@Override
	public void playRound(int roundNumber) {
		for (Player player : mPlayers) {

			player.sendUpdate("round", mRoundNumber);
			player.sendUpdate("move", mMoves.size());
			player.sendUpdate("field", mBoard.toString());
			player.sendUpdate("macroboard", mBoard.macroBoardString());

			if (getWinner() == null) {
				String response = player.requestMove("move");
				Move move = new Move(player);
				MoveResult moveResult = new MoveResult(player, mBoard,
						player.getId());
				if (parseResponse(response, player)) {
					move.setPosition(mBoard.getLastMiniBoard(),
							mBoard.getLastCell());
					move.setIllegalMove(mBoard.getLastError());
					mMoves.add(move);
					moveResult = new MoveResult(player, mBoard, player.getId());
					moveResult.setPosition(mBoard.getLastMiniBoard(),
							mBoard.getLastCell());
					moveResult.setIllegalMove(mBoard.getLastError());
					mMoveResults.add(moveResult);
				} else {
					move = new Move(player);
					moveResult = new MoveResult(player, mBoard, player.getId());
					move.setPosition(mBoard.getLastMiniBoard(),
							mBoard.getLastCell());
					move.setIllegalMove(mBoard.getLastError() + " (first try)");
					mMoves.add(move);
					moveResult.setPosition(mBoard.getLastMiniBoard(),
							mBoard.getLastCell());
					moveResult.setIllegalMove(mBoard.getLastError()
							+ " (first try)");
					mMoveResults.add(moveResult);
					player.sendUpdate("field", mBoard.toString());
					player.sendUpdate("macroboard", mBoard.macroBoardString());
					response = player.requestMove("move");
					if (parseResponse(response, player)) {
						move = new Move(player);
						moveResult = new MoveResult(player, mBoard,
								player.getId());
						move.setPosition(mBoard.getLastMiniBoard(),
								mBoard.getLastCell());
						mMoves.add(move);
						moveResult.setPosition(mBoard.getLastMiniBoard(),
								mBoard.getLastCell());
						mMoveResults.add(moveResult);
					} else {
						move = new Move(player);
						moveResult = new MoveResult(player, mBoard,
								player.getId());
						move.setPosition(mBoard.getLastMiniBoard(),
								mBoard.getLastCell());
						move.setIllegalMove(mBoard.getLastError()
								+ " (second try)");
						mMoves.add(move);
						moveResult.setPosition(mBoard.getLastMiniBoard(),
								mBoard.getLastCell());
						moveResult.setIllegalMove(mBoard.getLastError()
								+ " (second try)");
						mMoveResults.add(moveResult);
						player.sendUpdate("field", mBoard.toString());
						response = player.requestMove("move");
						if (parseResponse(response, player)) {
							move = new Move(player);
							moveResult = new MoveResult(player, mBoard,
									player.getId());
							move.setPosition(mBoard.getLastMiniBoard(),
									mBoard.getLastCell());
							mMoves.add(move);
							moveResult.setPosition(mBoard.getLastMiniBoard(),
									mBoard.getLastCell());
							mMoveResults.add(moveResult);
						} else { /* Too many errors, other player wins */
							move = new Move(player);
							moveResult = new MoveResult(player, mBoard,
									player.getId());
							move.setPosition(mBoard.getLastMiniBoard(),
									mBoard.getLastCell());
							move.setIllegalMove(mBoard.getLastError()
									+ " (last try)");
							mMoves.add(move);
							moveResult.setPosition(mBoard.getLastMiniBoard(),
									mBoard.getLastCell());
							moveResult.setIllegalMove(mBoard.getLastError()
									+ " (last try)");
							mMoveResults.add(moveResult);
							mGameOverByPlayerErrorPlayerId = player.getId();
						}
					}
				}

				player.sendUpdate("field", mBoard.toString());
				player.sendUpdate("macroboard", mBoard.macroBoardString());

				mRoundNumber++;
			}
		}
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
			int miniBoard = Integer.parseInt(parts[1]);
			int cell = Integer.parseInt(parts[2]);
			if (mBoard.placeMove(miniBoard, cell, player.getId())) {
				return true;
			}
		}
		mBoard.mLastError = "Unknown command";
		return false;
	}

	@Override
	public int getRoundNumber() {
		return this.mRoundNumber;
	}

	@Override
	public AbstractPlayer getWinner() {
		int winner = mBoard.getWinner();
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

	@Override
	public String getPlayedGame() {
		return "";
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

	public Board getField() {
		return mBoard;
	}

	@Override
	public boolean isGameOver() {
		return (getWinner() != null);
	}
}