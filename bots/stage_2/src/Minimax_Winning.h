#ifndef MINIMAX_WINNING_H_
#define MINIMAX_WINNING_H_

#include <cstdint>
#include "AbstractMinimax.h"
using namespace std;

class Minimax_Winning : public AbstractMinimax {
   private:
    vector<vector<int>> weights = {{3, 2, 3}, {2, 4, 2}, {3, 2, 3}};
    inline int computeMoveScore(const vector<vector<int>> &gameBoard, int row,
                                int col, int posx, int posy, int id) const;

   public:
    virtual int heuristic(Node *node) const;
    virtual Move preMinimax(const Field &initialField);
};

#endif /* MINIMAX_WINNING_H_ */
