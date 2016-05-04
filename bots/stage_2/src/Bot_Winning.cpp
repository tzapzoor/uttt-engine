//============================================================================
// Name        : Bot_Winning.cpp
// Author      : CBTeamName
//============================================================================

#include "Bot_Winning.h"
#include "GlobalSettings.h"

#include <iostream>
#include "Utils.h"

using namespace std;

Move Bot_Winning::makeTurn(const Field &field) {
    // TODO keep a rising trend
    long totalTime = timestamp();
    int movesLeft = maxMoves - field.getRoundNumber();
    int timebank = GlobalSettings::getInstance().getTimebank();
    int generationTime =
        generationRatio *
        (double)((timebank + movesLeft * moveTime) / movesLeft);
    // cerr << "Timebank: " << timebank << endl
    //      << "Calc gen time: " << generationTime << endl
    //      << "Moves left: " << movesLeft << endl
    //      << "Generation ratio: " << generationRatio << endl
    //      << flush;

    // update generationRatio

    Move nextMove = strat.nextMove(field, generationTime);
    totalTime = timestamp() - totalTime;
    generationRatio = generationTime / (double)(totalTime);
    if (generationRatio < 0.1) {
        generationRatio = 0.3;
    }

    if (generationRatio > 0.9) {
        generationRatio = 0.9;
    }

    if (field.getMoveNumber() == 1) {
        generationRatio = 0.5;
    }

    return nextMove;
}

void Bot_Winning::clearResources() { strat.reset(); }
