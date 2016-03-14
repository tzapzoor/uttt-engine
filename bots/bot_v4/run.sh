#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
java -cp $DIR bot.BotStarter 2> err.txt
