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

package bot;

import java.util.Scanner;

/**
 * BotParser class
 * 
 * Main class that will keep reading output from the engine.
 * Will either update the bot state or get actions.
 * 
 * @author Jim van Eeden <jim@starapple.nl>
 */

public class BotParser {

	final Scanner scan;
	final BotStarter bot;

	private Field mField;
	public static int mBotId = 0;

	public BotParser(BotStarter bot) {
		this.scan = new Scanner(System.in);
		this.bot = bot;
	}

	public void run() {
		mField = new Field();
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(line.length() == 0) {
				continue;
			}

			String[] parts = line.split(" ");
			if(parts[0].equals("settings")) {
				if (parts[1].equals("your_botid")) {
					mBotId = Integer.parseInt(parts[2]);
				}
			} else if(parts[0].equals("update") && parts[1].equals("game")) { /* new game data */
			    mField.parseGameData(parts[2], parts[3]);
			} else if(parts[0].equals("action")) {
				if (parts[1].equals("move")) { /* move requested */
					Move move = this.bot.makeTurn(mField);
					System.out.println("place_move " + move.getX() + " " + move.getY());
				}
			} else { 
				System.out.println("unknown command");
			}
		}
	}
}