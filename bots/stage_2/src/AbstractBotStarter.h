//============================================================================
// Name        : AbstractBotStarter.h
// Author      : CBTeamName
//============================================================================

#ifndef ABSTRACT_BOT_STARTER_H_
#define ABSTRACT_BOT_STARTER_H_

#include "Field.h"
#include "Move.h"

class AbstractBotStarter {
   public:
    virtual Move makeTurn(const Field &field) = 0;
    virtual void clearResources() = 0;
};

#endif /* ABSTRACT_BOT_STARTER_H_ */
