#!/bin/bash

stderr="err.txt"
stdout="out.txt"

if [ $# -eq 2 ]
then
    echo "Bot1: "$1
    echo "Bot2: "$2
    echo "Running game..."
    java -cp bin/ com.theaigames.ultimatetictactoe.UltimateTicTacToe $1 $2 2> $stderr 1> $stdout
    echo "Done. Check out "$stderr" and "$stdout"."
else
    echo "Usage "$0" [bot1] [bot2]"
fi
