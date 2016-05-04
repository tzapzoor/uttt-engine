//============================================================================
// Name        : Field.h
// Author      : CBTeamName
//============================================================================

#ifndef FIELD_H_
#define FIELD_H_

#include <cstdint>
#include <cstdint>
#include <list>
#include <vector>
#include "Move.h"
using namespace std;

#ifndef WINNER
#define WINNER
enum class Winner { MYSELF, OPPONENT, DRAW, UNDECIDED };
#endif

class Field {
   private:
    static const int COLS = 9;
    static const int ROWS = 9;

    int mRoundNr;
    int mMoveNr;
    vector<vector<int>> gameBoard;
    vector<vector<int>> smallBoards;
    vector<pair<int, int>> activeSmallBoards;

    void updateSmallBoard(int gameBoardRow, int gameBoardCol);

   public:
    Field();
    Field(const Field& other);
    virtual ~Field();

    Winner gameWinner() const;
    Winner smallBoardWinner(int row, int col) const;
    inline bool isSmallBoardActive(int row, int col) const;
    vector<Move> getAvailableMoves() const;
    bool placeMove(int gameBoardRow, int gameBoardCol, bool myself);
    const vector<vector<int>>& getGameBoard() const;
    const vector<vector<int>>& getSmallBoards() const;
    int getMoveNumber() const;
    int getRoundNumber() const;

    /* Communication methods */
    void parseGameData(string& key, string& value);
    void parseGameBoardFromString(string& s);
    void parseSmallBoardsFromString(string& s);

    /* Overloaded operators */
    friend ostream& operator<<(ostream& os, const Field& f);
};

#endif /* FIELD_H_ */
