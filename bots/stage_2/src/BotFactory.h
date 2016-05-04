//============================================================================
// Name        : BotFactory.h
// Author      : CBTeamName
//============================================================================

#ifndef BOT_FACTORY_H_
#define BOT_FACTORY_H_

#include "AbstractBotStarter.h"

#include <memory>
#include <string>

using namespace std;

class BotFactory {
   public:
    static AbstractBotStarter* createBot(const string& type);
    BotFactory();
    ~BotFactory();
};

#endif /* BOT_FACTORY_H_ */
