//============================================================================
// Name        : Main.cpp
// Author      : CBTeamName
//============================================================================

#include "BotFactory.h"
#include "BotParser.h"
#include "Field.h"
#include "GlobalSettings.h"
#include "Move.h"

#include <iostream>
#include <sstream>

#define SUCCESS 0
#define ERROR -1

using namespace std;

int main(int argc, char *argv[]) {
    // provide the default bot to run (on theAiGames)
    string running_bot = "winning";

    if (argc != 2) {
        cerr << "Warning: no args provided => running default bot (" +
                    running_bot + ")!\n"
             << flush;
    } else {
        running_bot = argv[1];
    }

    BotParser parser(BotFactory::createBot(running_bot));

    if (parser.getBot().get() == nullptr) {
        cerr << "Error: Wrong bot name!\n" << flush;
        return ERROR;
    }

    // Success.
    parser.run();

    return SUCCESS;
}
