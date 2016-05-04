//============================================================================
// Name        : BotParser.cpp
// Author      : CBTeamName
//============================================================================

#include "BotParser.h"
#include "AbstractBotStarter.h"
#include "GlobalSettings.h"
#include "Utils.h"

#include <cstdint>
#include <iostream>
using namespace std;

BotParser::BotParser(AbstractBotStarter* bot) : bot(bot) {
    GlobalSettings::getInstance().setBotId(0);
}

unique_ptr<AbstractBotStarter>& BotParser::getBot() { return bot; }

void BotParser::run() {
    string line;
    while (getline(cin, line)) {
        if (line.size() == 0) {
            continue;
        }
        vector<string> parts;
        split(line, ' ', parts);
        if (parts[0] == "settings") {
            // Initial settings.
            if (parts[1] == "your_botid") {
                int botId = stringToInt(parts[2]);
                GlobalSettings::getInstance().setBotId(botId);
            }
        } else if (parts[0] == "update" && parts[1] == "game") {
            // New game data.
            mField.parseGameData(parts[2], parts[3]);
        } else if (parts[0] == "action") {
            if (parts[1] == "move") {
                GlobalSettings::getInstance().setTimebank(
                    stringToInt(parts[2]));
                // Move requested.
                Move move = bot->makeTurn(mField);
                cout << "place_move " << move.getCol() << " " << move.getRow()
                     << endl
                     << flush;

                // Ask the bot to kindly clear its resources while the
                // opponent is thinking, in order to prepare for the next move
                // :).
                bot->clearResources();
            }
        } else {
            cout << "unknown command" << endl << flush;
        }
    }
}

BotParser::~BotParser() {}
