//============================================================================
// Name        : Move.cpp
// Author      : CBTeamName
//============================================================================

#include "Move.h"

#include <cstdint>
#include <sstream>
#include <string>
using namespace std;

Move::Move(int row, int col) : mRow(row), mCol(col) {}

Move::Move(const Move &other) {
    this->mRow = other.mRow;
    this->mCol = other.mCol;
}

bool Move::operator==(const Move &other) {
    return this->mCol == other.mCol && this->mRow == other.mRow;
}

ostream &operator<<(ostream &os, const Move &m) {
    os << '(' << m.mRow << ", " << m.mCol << ')';
    return os;
}
