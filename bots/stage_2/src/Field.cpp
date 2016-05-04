//============================================================================
// Name        : Field.cpp
// Author      : CBTeamName
//============================================================================

#include "Field.h"
#include "GlobalSettings.h"
#include "Utils.h"

#include <algorithm>
#include <chrono>
#include <cstdint>
#include <iostream>
#include <list>
#include <random>
#include <sstream>
#include <string>
#include <vector>
using namespace std;

/*
 * Default constructor.
 */
Field::Field()
    : gameBoard(ROWS, vector<int>(COLS, 0)),
      smallBoards(ROWS / 3, vector<int>(COLS / 3, -1)) {
    mRoundNr = 1;
    mMoveNr = 1;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            activeSmallBoards.push_back(make_pair(i, j));
        }
    }
}

/*
 * Copy constructor.
 */
Field::Field(const Field& other)
    : mRoundNr(other.mRoundNr),
      mMoveNr(other.mMoveNr),
      gameBoard(other.getGameBoard()),
      smallBoards(other.getSmallBoards()),
      activeSmallBoards(other.activeSmallBoards) {}

/*
 * Determines the winner of the entire game.
 */
Winner Field::gameWinner() const {
    int id = -2;

    // Horizontal lines of small boards.
    for (int i = 0; i <= 2; i++) {
        if (smallBoards[i][0] == smallBoards[i][1] &&
            smallBoards[i][1] == smallBoards[i][2]) {
            if (smallBoards[i][0] > 0) {
                id = smallBoards[i][0];
            }
            break;
        }
    }

    // Vertical lines of small boards.
    for (int j = 0; j <= 2; j++) {
        if (smallBoards[0][j] == smallBoards[1][j] &&
            smallBoards[1][j] == smallBoards[2][j]) {
            if (smallBoards[0][j] > 0) {
                id = smallBoards[0][j];
            }
        }
    }

    // Diagonal lines of small boards.
    if (smallBoards[0][0] == smallBoards[1][1] &&
        smallBoards[1][1] == smallBoards[2][2]) {
        if (smallBoards[0][0] > 0) {
            id = smallBoards[0][0];
        }
    } else if (smallBoards[2][0] == smallBoards[1][1] &&
               smallBoards[1][1] == smallBoards[0][2]) {
        if (smallBoards[2][0] > 0) {
            id = smallBoards[2][0];
        }
    }

    // Verify if bot won the game.
    if (GlobalSettings::getInstance().getBotId() == id) {
        return Winner::MYSELF;
    }
    // Verify if opponent won the game.
    else if (GlobalSettings::getInstance().getOpponentId() == id) {
        return Winner::OPPONENT;
    }

    // Verify if the game is still undecided.
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (smallBoardWinner(i, j) == Winner::UNDECIDED) {
                return Winner::UNDECIDED;
            }
        }
    }

    return Winner::DRAW;
}

/*
 * Determines the winner of the small board
 * at position (row, col).
 */
Winner Field::smallBoardWinner(int row, int col) const {
    int botId = GlobalSettings::getInstance().getBotId();
    int opponentId = GlobalSettings::getInstance().getOpponentId();

    if (smallBoards[row][col] == botId) return Winner::MYSELF;
    if (smallBoards[row][col] == opponentId) return Winner::OPPONENT;

    // Determine top-left row and column of the small board.
    int i = row * 3;
    int j = col * 3;
    int id = -2;
    int k;

    // Horizontal lines in the given small board.
    for (k = 0; k <= 2; k++) {
        if (gameBoard[i + k][j] == gameBoard[i + k][j + 1] &&
            gameBoard[i + k][j + 1] == gameBoard[i + k][j + 2]) {
            if (gameBoard[i + k][j] > 0) {
                id = gameBoard[i + k][j];
            }
        }
    }

    // Vertical lines in the given small board.
    for (k = 0; k <= 2; k++) {
        if (gameBoard[i][j + k] == gameBoard[i + 1][j + k] &&
            gameBoard[i + 1][j + k] == gameBoard[i + 2][j + k]) {
            if (gameBoard[i][j + k] > 0) {
                id = gameBoard[i][j + k];
            }
        }
    }

    // Diagonal lines in the given small board.
    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] &&
        gameBoard[i + 1][j + 1] == gameBoard[i + 2][j + 2]) {
        if (gameBoard[i][j] > 0) {
            id = gameBoard[i][j];
        }
    } else if (gameBoard[i + 2][j] == gameBoard[i + 1][j + 1] &&
               gameBoard[i + 1][j + 1] == gameBoard[i][j + 2]) {
        if (gameBoard[i + 2][j] > 0) {
            id = gameBoard[i + 2][j];
        }
    }

    // Verify if bot won this small board.
    if (id == botId) {
        return Winner::MYSELF;
    }
    // Verify if opponent won this small board.
    else if (id == opponentId) {
        return Winner::OPPONENT;
    }

    // Verify if this small board is still undecided.
    for (int it1 = i; it1 < i + 3; it1++) {
        for (int it2 = j; it2 < j + 3; it2++) {
            if (gameBoard[it1][it2] == 0) {
                return Winner::UNDECIDED;
            }
        }
    }

    return Winner::DRAW;
}

/*
 * Checks whether the small board at position
 * (row, col) is active.
 */
inline bool Field::isSmallBoardActive(int row, int col) const {
    return (smallBoards[row][col] == -1);
}

vector<Move> Field::getAvailableMoves() const {
    vector<Move> moves;
    unsigned seed = std::chrono::system_clock::now().time_since_epoch().count();
    for (const pair<int, int>& activeSmallBoard : activeSmallBoards) {
        for (int i = 3 * activeSmallBoard.first;
             i < 3 * (activeSmallBoard.first + 1); i++) {
            for (int j = 3 * activeSmallBoard.second;
                 j < 3 * (activeSmallBoard.second + 1); j++) {
                if (gameBoard[i][j] == 0) {
                    moves.push_back(Move(i, j));
                }
            }
        }
    }
    shuffle(moves.begin(), moves.end(), std::default_random_engine(seed));
    return moves;
}

void Field::updateSmallBoard(int gameBoardRow, int gameBoardCol) {
    // Deactivate any active small board.
    for (const pair<int, int>& activeSmallBoard : activeSmallBoards) {
        smallBoards[activeSmallBoard.first][activeSmallBoard.second] = 0;
    }
    activeSmallBoards.clear();
    int smallBoardRow = gameBoardRow / 3;
    int smallBoardCol = gameBoardCol / 3;

    // Decide current small board.
    Winner smallWinner = smallBoardWinner(smallBoardRow, smallBoardCol);
    switch (smallWinner) {
        case Winner::MYSELF:
            smallBoards[smallBoardRow][smallBoardCol] =
                GlobalSettings::getInstance().getBotId();
            break;
        case Winner::OPPONENT:
            smallBoards[smallBoardRow][smallBoardCol] =
                GlobalSettings::getInstance().getOpponentId();
            break;
        case Winner::DRAW:
        case Winner::UNDECIDED:
            // nothing to do
            break;
    }

    // See if the game has been won.
    Winner bigWinner = gameWinner();
    if (bigWinner != Winner::UNDECIDED) {
        return;
    }

    // Find the next small board that will be activated.
    int nextSmallBoardRow = gameBoardRow - (gameBoardRow / 3) * 3;
    int nextSmallBoardCol = gameBoardCol - (gameBoardCol / 3) * 3;

    if (smallBoardWinner(nextSmallBoardRow, nextSmallBoardCol) ==
        Winner::UNDECIDED) {
        // Activate the next small game.
        smallBoards[nextSmallBoardRow][nextSmallBoardCol] = -1;

        activeSmallBoards.push_back(
            make_pair(nextSmallBoardRow, nextSmallBoardCol));
    } else {
        // Activate all undecided small games.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (smallBoards[i][j] == 0 &&
                    smallBoardWinner(i, j) == Winner::UNDECIDED) {
                    smallBoards[i][j] = -1;
                    activeSmallBoards.push_back(make_pair(i, j));
                }
            }
        }
    }
}

bool Field::placeMove(int gameBoardRow, int gameBoardCol, bool myself) {
    if (!isSmallBoardActive(gameBoardRow / 3, gameBoardCol / 3)) {
        return false;
    }
    if (gameBoard[gameBoardRow][gameBoardCol] != 0) {
        return false;
    }

    gameBoard[gameBoardRow][gameBoardCol] =
        (myself) ? GlobalSettings::getInstance().getBotId()
                 : GlobalSettings::getInstance().getOpponentId();

    updateSmallBoard(gameBoardRow, gameBoardCol);

    return true;
}

/*
 * Getters
 */
const vector<vector<int> >& Field::getGameBoard() const { return gameBoard; }

const vector<vector<int> >& Field::getSmallBoards() const {
    return smallBoards;
}

int Field::getMoveNumber() const { return mMoveNr; }

int Field::getRoundNumber() const { return mRoundNr; }

/*
 * Communication methods.
 */
void Field::parseGameData(string& key, string& value) {
    if (key == "round") {
        mRoundNr = stringToInt(value);
    } else if (key == "move") {
        mMoveNr = stringToInt(value);
    } else if (key == "field") {
        parseGameBoardFromString(value);
    } else if (key == "macroboard") {
        parseSmallBoardsFromString(value);
    }
}

void Field::parseGameBoardFromString(string& s) {
    stringstream ss(s);
    char delim;
    int temp;
    for (int i = 0; i < ROWS; i++) {
        for (int j = 0; j < COLS; j++) {
            ss >> temp >> delim;
            gameBoard[i][j] = temp;
        }
    }
}

void Field::parseSmallBoardsFromString(string& s) {
    activeSmallBoards.clear();
    stringstream ss(s);
    char delim;
    int temp;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            ss >> temp >> delim;
            smallBoards[i][j] = temp;
            if (temp == -1) activeSmallBoards.push_back(make_pair(i, j));
        }
    }
}

/*
 * Overloaded operators.
 */
ostream& operator<<(ostream& os, const Field& f) {
    os << "Game Board:" << endl;
    for (int i = 0; i < Field::ROWS; i++) {
        for (int j = 0; j < Field::COLS; j++) {
            os << (int)f.gameBoard[i][j] << ' ';
        }
        os << endl;
    }
    os << "Small boards:" << endl;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            os << f.smallBoards[i][j] << ' ';
        }
        os << endl;
    }
    return os;
}

/*
 * Default destructor.
 */
Field::~Field() {
    smallBoards.clear();
    gameBoard.clear();
    activeSmallBoards.clear();
}
