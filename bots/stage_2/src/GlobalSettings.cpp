//============================================================================
// Name        : GlobalSettings.cpp
// Author      : CBTeamName
//============================================================================

#include "GlobalSettings.h"

GlobalSettings& GlobalSettings::getInstance() {
    static GlobalSettings instance;
    return instance;
}

int GlobalSettings::getBotId() { return botId; }

void GlobalSettings::setBotId(int botId) {
    this->botId = botId;
    this->opponentId = (this->botId == 1) ? 2 : 1;
}

int GlobalSettings::getOpponentId() { return opponentId; }

void GlobalSettings::setTimebank(int timebank) { this->timebank = timebank; }

int GlobalSettings::getTimebank() { return this->timebank; }
