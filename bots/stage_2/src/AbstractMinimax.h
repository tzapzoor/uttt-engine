//============================================================================
// Name        : AbstractMinimax.h
// Author      : CBTeamName
//============================================================================

#ifndef ABSTRACT_MINIMAX_H_
#define ABSTRACT_MINIMAX_H_

#include <chrono>
#include <cstdint>
#include <memory>
#include <vector>
#include "Field.h"
#include "Move.h"

using namespace std;

class AbstractMinimax {
   protected:
    class Node {
       public:
        int score = 0;
        int level;
        Move move;
        Field field;
        vector<Node *> children;

        Node() = default;
        Node(const Node &other);
        ~Node();
    };

   private:
    long startTime;
    Node *root = nullptr;

   public:
    AbstractMinimax();
    ~AbstractMinimax();

    virtual int heuristic(Node *node) const = 0;

    // Used for hardcoding moves.
    // It is called before the actual minimax. If it returns
    // a value different than (10,10) the minimax will never run.
    virtual Move preMinimax(const Field &initialField) = 0;

    Move nextMove(const Field &initialField, int maxGenerationTime);

    inline void reset() {
        if (root != nullptr) {
            delete root;
            root = nullptr;
        }
    }

   private:
    virtual void minimax(Node *currentNode);

    void generateGameTree(const Field &initialField, int maxGenerationTime);
};

#endif /* ABSTRACT_MINIMAX_H_ */
