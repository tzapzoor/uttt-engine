#!/bin/bash

echo "Clear exported data from ./PA Viewer/data/fields/*"
rm -rf "PA Viewer/data/fields/*" > /dev/null 2>&1
touch "PA Viewer/data/fields/_placeholder_"

stderr="err.txt"
stdout="out.txt"
bot1="../ultimate-tic-tac-toe-bot/run_heur2.sh"
bot2="../ultimate-tic-tac-toe-bot/run.sh"

numGames=1
numWins1=0
numWins2=0
numDraws=0
echo "Simulating "$numGames" games..."
for i in `seq 1 $numGames`;
do
    java -cp bin/:./GSON/* com.theaigames.ultimatetictactoe.UltimateTicTacToe $bot1 $bot2 2> $stderr 1> $stdout
    result=$(cat $stdout | head -n -3 | cut -d ' ' -f3 | tail -n 1);
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
