Ultimate Tic Tac Toe Engine
============

Run script.
![](http://imgur.com/download/e11XFcX)

Match viewer (only works in Firefox). Use '<' and '>' to iterate through the moves.
![](http://imgur.com/download/3xb6Vtc)
![](http://imgur.com/download/MMANL8O)


Multiple simulations script.
![](http://imgur.com/download/yeSLSGN)


#### Compilation (Linux):

    $ ./compile.sh

#### Running:

    $ ./test.sh [bot1] [bot2]

[your bot1] and [your bot2] could be any command for running a bot process. For instance "java -cp /home/dev/starterbot/bin/ main.BotStarter" or "node /home/user/bot/Bot.js"

Errors will be logged to err.txt, output dump will be logged to out.txt. You can edit the saveGame() method in the AbstractGame class to output extra stuff like your bot dumps. If you want to quickly run the engine from Eclipse, change `DEV_MODE = false` to `DEV_MODE = true` in the main method of the FourInARow class and provide your own bot in that method as well.
