#!/bin/bash

###################################################
# Script parameters : $1 = bot1
#                     $2 = bot2
#                     $3 = num games to simulate

numCores=8
###################################################

numWins1=0
numWins2=0
numDraws=0

#parameters : bot1 bot 2
function runTest() {
    output=$(java -cp bin/:./GSON/* com.theaigames.ultimatetictactoe.UltimateTicTacToe $1 $2 --no-exports 2> /dev/null)
    result=$(echo $output | tr "\n" "\0" | cut -d ' ' -f4)
    if [ "$result" == "player2" ]; then
        echo 2
    elif [ "$result" == "player1" ]; then
        echo 1
    else
        echo 0
    fi
}

function updateScores() {
    if [ "$1" == "2" ]; then
        ((numWins2++))
    elif [ "$1" == "1" ]; then
        ((numWins1++))
    else
        ((numDraws++))
    fi
}

function multipleTests() {
    for i in `seq 1 $3`; do
        runTest $1 $2 &
    done
    wait
}

printf "============================================\n"
printf "== Ultimate Tic Tac Toe Simulation Script ==\n"
printf "============================================\n\n"
if [ "$#" -ne 3 ]; then
    printf "Illegal number of parameters.\n"
    printf "Usage: $0 <bot_1> <bot_2> <number_of_games>\n"
    exit 1
fi

printf "Running $3 simulations...\n"

startTime=$(date +%s)
allGames=$3
numGames=$3


while [ $numGames -gt 0 ]; do
    sims=$(( numCores < numGames ? numCores : numGames ))
    result=$(multipleTests $1 $2 $sims)
    for i in $result; do
        updateScores $i
    done
    numGames=$(( numGames - sims ))
    echo "[$(( ((allGames - numGames) * 100) / allGames ))%] simulations completed. (b1: $numWins1, b2: $numWins2, d: $numDraws)"
done

echo "Completed in $(( $(date +%s) - startTime )) seconds."

printf "\n"
printf "Bot1 -> $1\n"
printf "Bot2 -> $2\n\n"

echo "Bot1: "$numWins1 "($(( ($numWins1 * 100) / allGames ))%)"
echo "Bot2: "$numWins2 "($(( ($numWins2 * 100) / allGames ))%)"
echo "Draws: "$numDraws "($(( ($numDraws * 100) / allGames ))%)"
