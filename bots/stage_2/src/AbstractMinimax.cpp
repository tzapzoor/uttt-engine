//============================================================================
// Name        : AbstractMinimax.cpp
// Author      : CBTeamName
//============================================================================

#include "AbstractMinimax.h"
#include "Utils.h"

#include <climits>
#include <cmath>
#include <cstdint>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <list>
#include <queue>
using namespace std;

AbstractMinimax::AbstractMinimax() {}

AbstractMinimax::Node::Node(const Node &other)
    : score(other.score),
      level(other.level),
      move(other.move),
      field(other.field),
      children(other.children) {}

AbstractMinimax::Node::~Node() {
    for (Node *ch : children) {
        delete ch;
    }
    children.clear();
}

Move AbstractMinimax::nextMove(const Field &initialField,
                               int maxGenerationTime) {
    startTime = timestamp();
    Move pre = preMinimax(initialField);

    // If it is not the determined "null" move, we will not run the minimax.
    if (!(pre == Move(10, 10))) {
        return pre;
    }

    long generationTime = timestamp();
    generateGameTree(initialField, maxGenerationTime);
    generationTime = timestamp() - generationTime;

    long minimaxTime = timestamp();
    minimax(root);
    minimaxTime = timestamp() - minimaxTime;

    // Find the branch that has the maximum score.
    // The maximum score is now registered inside the root.
    vector<Move> coolMoves;
    for (Node *ch : root->children) {
        if (ch->score == root->score) {
            coolMoves.push_back(ch->move);
        }
        if (ch->field.gameWinner() == Winner::MYSELF) {
            return ch->move;
        }
    }

    srand((unsigned int)time(0));
    if (coolMoves.size() > 0) {
        return coolMoves[rand() % coolMoves.size()];
    }

    cerr << "Failed to generate move :(" << endl;
    return Move(10, 10);
}

void AbstractMinimax::minimax(Node *currentNode) {
    if (currentNode->children.size() == 0) {
        currentNode->score = heuristic(currentNode);
        return;
    }

    // This means it needs to maximize the score.
    if (currentNode->level % 2 == 0) {
        currentNode->score = INT_MIN;
        for (Node *ch : currentNode->children) {
            minimax(ch);
            currentNode->score = (currentNode->score > ch->score)
                                     ? currentNode->score
                                     : ch->score;
        }
    }
    // Otherwise, it needs to minimize the score.
    else {
        currentNode->score = INT_MAX;
        for (Node *ch : currentNode->children) {
            minimax(ch);
            currentNode->score = (currentNode->score < ch->score)
                                     ? currentNode->score
                                     : ch->score;
        }
    }
}

void AbstractMinimax::generateGameTree(const Field &initialField,
                                       int maxGenerationTime) {
    long long numNodes = 0;
    int maxLevel = 0;
    root = new Node();
    root->level = 0;
    root->field = initialField;

    queue<Node *> queue;
    queue.push(root);
    while (!queue.empty() && (timestamp() - startTime) <= maxGenerationTime) {
        Node *currentNode = queue.front();
        queue.pop();

        for (Move m : currentNode->field.getAvailableMoves()) {
            Node *newNode = new Node();
            newNode->field = currentNode->field;
            newNode->level = currentNode->level + 1;
            newNode->move = m;
            maxLevel = max(maxLevel, newNode->level);

            if (newNode->field.placeMove(m.getRow(), m.getCol(),
                                         newNode->level % 2 != 0)) {
                currentNode->children.push_back(newNode);
                queue.push(newNode);
                numNodes++;
            } else {
                cerr << "BAD MOVE, BRUH!!!! WTF" << endl;
            }
        }
    }
    cerr << "Max level: " << (int)maxLevel << '\n';
    cerr << "Num nodes: " << numNodes << endl << flush;
}

AbstractMinimax::~AbstractMinimax() { reset(); }
