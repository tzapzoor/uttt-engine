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

import com.theaigames.engine.io.IOPlayer;
import com.theaigames.game.AbstractGame;
import com.theaigames.game.player.AbstractPlayer;
import com.theaigames.ultimatetictactoe.field.Field;
import com.theaigames.ultimatetictactoe.player.Player;

public class UltimateTicTacToe extends AbstractGame {
    
    private final int TIMEBANK_MAX = 10000;
    private final int TIME_PER_MOVE = 500;
    private Field mBoard;
    private int mBotId = 1;
    
    private List<Player> players;
    

    @Override
    public void setupGame(ArrayList<IOPlayer> ioPlayers) throws Exception {    	
    	// create all the players and everything they need
        this.players = new ArrayList<Player>();
        
        // create the playing field
        this.mBoard = new Field();
        
        for(int i=0; i<ioPlayers.size(); i++) {
            // create the player
            String playerName = String.format("player%d", i+1);
            Player player = new Player(playerName, ioPlayers.get(i), TIMEBANK_MAX, TIME_PER_MOVE, i+1);
            this.players.add(player);

        }
        
        for(AbstractPlayer player : this.players) {
            sendSettings(player);
            
        }
        
        // create the processor
        super.processor = new Processor(this.players, this.mBoard);
    }
    
    @Override
    public void sendSettings(AbstractPlayer player) {
        player.sendSetting("timebank", TIMEBANK_MAX);
        player.sendSetting("time_per_move", TIME_PER_MOVE);
        player.sendSetting("player_names", this.players.get(0).getName() + "," + this.players.get(1).getName());
        player.sendSetting("your_bot", player.getName());
        player.sendSetting("your_botid", mBotId);
        mBotId++;
    }

    @Override
    protected void runEngine() throws Exception {
        super.engine.setLogic(this);
        super.engine.start();
    }
    
    // DEV_MODE can be turned on to easily test the
    // engine from eclipse
    public static void main(String args[]) throws Exception {    
    	UltimateTicTacToe game = new UltimateTicTacToe();
    	
        // DEV_MODE settings
        game.TEST_BOT = "java -cp /home/tzap/work/uttt-test-engine/bots/randomBot/bin bot.BotStarter";
        game.NUM_TEST_BOTS = 2;
        game.DEV_MODE = false;
        
        game.setupEngine(args);
        game.runEngine();
    }
}
