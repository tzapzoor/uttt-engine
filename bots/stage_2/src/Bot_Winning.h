//============================================================================
// Name        : Bot_Winningv2.h
// Author      : CBTeamName
//============================================================================

#ifndef BOT_WINNING_H_
#define BOT_WINNING_H_

#include "AbstractBotStarter.h"
#include "Minimax_Winning.h"

using namespace std;

class Bot_Winning : public AbstractBotStarter {
   private:
    int maxMoves = 41;
    int moveTime = 400;
    double generationRatio = 0.5;
    Minimax_Winning strat;

   public:
    virtual Move makeTurn(const Field &field);
    virtual void clearResources();
};

#endif /* BOT_WINNING_H_ */
