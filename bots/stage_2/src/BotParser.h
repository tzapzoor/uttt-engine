//============================================================================
// Name        : BotParser.h
// Author      : CBTeamName
//============================================================================

#ifndef BOT_PARSER_H_
#define BOT_PARSER_H_

#include <memory>
#include "AbstractBotStarter.h"
#include "Field.h"
using namespace std;

class BotParser {
   private:
    Field mField;
    unique_ptr<AbstractBotStarter> bot;

   public:
    BotParser(AbstractBotStarter* bot);
    unique_ptr<AbstractBotStarter>& getBot();
    void run();
    ~BotParser();
};

#endif /* BOT_PARSER_H */
