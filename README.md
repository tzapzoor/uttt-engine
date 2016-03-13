Ultimate Tic Tac Toe Engine
============

To compile (Linux):

    $ ./compile.sh

To run:

    $ ./test.sh [bot1] [bot2]

[your bot1] and [your bot2] could be any command for running a bot process. For instance "java -cp /home/dev/starterbot/bin/ main.BotStarter" or "node /home/user/bot/Bot.js"

Errors will be logged to err.txt, output dump will be logged to out.txt. You can edit the saveGame() method in the AbstractGame class to output extra stuff like your bot dumps. If you want to quickly run the engine from Eclipse, change `DEV_MODE = false` to `DEV_MODE = true` in the main method of the FourInARow class and provide your own bot in that method as well.
