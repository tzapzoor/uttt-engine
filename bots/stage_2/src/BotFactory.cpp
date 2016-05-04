//============================================================================
// Name        : BotFactory.cpp
// Author      : CBTeamName
//============================================================================

#include "BotFactory.h"
#include "Bot_Winning.h"

using namespace std;

AbstractBotStarter* BotFactory::createBot(const string& type) {
    if (type == "winning") {
        return new Bot_Winning();
    }
    return nullptr;
}

BotFactory::BotFactory() {}

BotFactory::~BotFactory() {}
