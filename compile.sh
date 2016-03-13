#!/bin/bash
echo "Ultimate Tic Tac Toe Testing Engine"
printf "Compiling..."
mkdir bin > /dev/null 2>&1
javac -d bin/ -cp .src/:./GSON/* `find ./src -name '*.java' -regex '^[./A-Za-z0-9]*$'`
if [ $? -eq 0 ]
then
    echo "DONE :)"
else
    echo "Error! Could not compile the project."
fi
