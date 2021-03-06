#!/bin/bash

echo "Clear exported data from ./Viewer/data/fields/*"
rm -rf ./Viewer/data/fields/* > /dev/null 2>&1
touch Viewer/data/fields/_placeholder_

stderr="err.txt"
stdout="out.txt"

if [ $# -eq 2 ]
then
    echo "Bot1: "$1
    echo "Bot2: "$2
    echo "Running game..."
    java -cp bin/:./GSON/* com.theaigames.ultimatetictactoe.UltimateTicTacToe $1 $2 2> $stderr 1> $stdout
    echo "Done. Check out "$stderr" and "$stdout"."
    cat $stdout
else
    echo "Usage "$0" [bot1] [bot2]"
fi
