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

package com.theaigames.ultimatetictactoe.moves;

import com.google.gson.Gson;
import com.theaigames.game.moves.AbstractMove;
import com.theaigames.ultimatetictactoe.field.Field;
import com.theaigames.ultimatetictactoe.player.Player;

public class MoveResult extends Move {
	private int mPlayerId = 0;
	private long mThinkingTime = 0;

	transient private String mJsonField = "";
	transient private String mJsonThis = "";
	
	public static class Builder {
		private MoveResult moveResult;
		private Gson exporter = new Gson();
		
		public Builder(Player player) {
			moveResult = new MoveResult(player);
		}
		
		public Builder withFieldData(Field field) {
			moveResult.mJsonField = exporter.toJson(field);
			moveResult.setPosition(field.getLastRow(), field.getLastCol());
			moveResult.setIllegalMove(field.getLastError());
			return this;
		}
		
		public MoveResult build() {
			moveResult.mJsonThis = exporter.toJson(moveResult);
			return moveResult;
		}
	}
	
	private MoveResult(Player player) {
		super(player);
		mPlayerId = player.getId();
		mThinkingTime = player.getLastMoveThinkingTime();
	}

	public int getPlayerId() {
		return mPlayerId;
	}

	public String getJsonField() {
		return mJsonField;
	}
	
	
	public String getJsonMoveResult() {
		return mJsonThis;
	}
	
	public long getThinkingTime() {
		return mThinkingTime;
	}
}
