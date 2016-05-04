//============================================================================
// Name        : Move.h
// Author      : CBTeamName
//============================================================================

#ifndef MOVE_H_
#define MOVE_H_

#include <cstdint>
#include <string>
using namespace std;
class Move {
   private:
    int mRow;
    int mCol;

   public:
    Move() = default;
    Move(int row, int col);
    Move(const Move &other);

    inline int getCol() { return mCol; }
    inline int getRow() { return mRow; }

    /* Overloaded operators */
    bool operator==(const Move &other);
    friend ostream &operator<<(ostream &os, const Move &m);
};

#endif /* MOVE_H_ */
