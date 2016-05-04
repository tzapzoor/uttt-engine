#!/bin/bash

echo "Clear exported data from ./Viewer/data/*"
rm ./Viewer/data/fields/*
rm ./Viewer/data/thinking/*

stderr="err.txt"
stdout="out.txt"
bot1="../ultimate-tic-tac-toe-bot/src/bot"
bot2="../ultimate-tic-tac-toe-bot/run.sh"

numGames=50
numWins1=0
numWins2=0
numDraws=0
echo "Simulating "$numGames" games..."
for i in `seq 1 $numGames`;
do
    java -cp bin/:./GSON/* com.theaigames.ultimatetictactoe.UltimateTicTacToe $bot1 $bot2 2> $stderr 1> $stdout
    result=$(cat $stdout | tr "\n" "\0" | cut -d ' ' -f3)
    if [ "$result" == "player2" ]; then
        ((numWins2++))
    elif [ "$result" == "player1" ]; then
        ((numWins1++))
    else
        ((numDraws++))
    fi
    echo "("$i"): "$result
done

echo ""
echo "Games: "$numGames
echo "Bot1: "$numWins1
echo "Bot2: "$numWins2
echo "Draws: "$numDraws
