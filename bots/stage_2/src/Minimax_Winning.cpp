#include "Minimax_Winning.h"
#include "GlobalSettings.h"

#include <algorithm>
#include <climits>
#include <cstdint>
#include <iostream>
using namespace std;

inline int Minimax_Winning::computeMoveScore(
    const vector<vector<int>> &gameBoard, int row, int col, int posx, int posy,
    int id) const {
    int otherId = (id == 1) ? 2 : 1;
    int score = 0;
    if (posx == 0) {
        if (posy == 0) {
            score = 3;
            if (gameBoard[row][col + 1] == id ||
                gameBoard[row][col + 2] == id) {
                score--;
            } else if (gameBoard[row][col + 1] == otherId ||
                       gameBoard[row][col + 2] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col] == id ||
                gameBoard[row + 2][col] == id) {
                score--;
            } else if (gameBoard[row + 1][col] == otherId ||
                       gameBoard[row + 2][col] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col + 1] == id ||
                gameBoard[row + 2][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 1][col + 1] == otherId ||
                       gameBoard[row + 2][col + 2] == otherId) {
                score++;
            }
        } else if (posy == 1) {
            score = 2;
            if (gameBoard[row][col] == id || gameBoard[row][col + 2] == id) {
                score--;
            } else if (gameBoard[row][col] == otherId ||
                       gameBoard[row][col + 2] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col + 1] == id ||
                gameBoard[row + 2][col + 1] == id) {
                score--;
            } else if (gameBoard[row + 1][col + 1] == otherId ||
                       gameBoard[row + 2][col + 1] == otherId) {
                score++;
            }
        } else if (posy == 2) {
            score = 3;
            if (gameBoard[row][col] == id || gameBoard[row][col + 1] == id) {
                score--;
            } else if (gameBoard[row][col] == otherId ||
                       gameBoard[row][col + 1] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col + 2] == id ||
                gameBoard[row + 2][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 1][col + 2] == otherId ||
                       gameBoard[row + 2][col + 2] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col + 1] == id ||
                gameBoard[row + 2][col] == id) {
                score--;
            } else if (gameBoard[row + 1][col + 1] == otherId ||
                       gameBoard[row + 2][col] == otherId) {
                score++;
            }
        }
    } else if (posx == 1) {
        if (posy == 0) {
            score = 2;
            if (gameBoard[row][col] == id || gameBoard[row + 2][col] == id) {
                score--;
            } else if (gameBoard[row][col] == otherId ||
                       gameBoard[row + 2][col] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col + 1] == id ||
                gameBoard[row + 1][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 1][col + 1] == otherId ||
                       gameBoard[row + 1][col + 2] == otherId) {
                score++;
            }

        } else if (posy == 1) {
            score = 4;
            if (gameBoard[row][col] == id ||
                gameBoard[row + 2][col + 2] == id) {
                score--;
            } else if (gameBoard[row][col] == otherId ||
                       gameBoard[row + 2][col + 2] == otherId) {
                score++;
            }
            if (gameBoard[row][col + 2] == id ||
                gameBoard[row + 2][col] == id) {
                score--;
            } else if (gameBoard[row][col + 2] == otherId ||
                       gameBoard[row + 2][col] == otherId) {
                score++;
            }
            if (gameBoard[row][col + 1] == id ||
                gameBoard[row + 2][col + 1] == id) {
                score--;
            } else if (gameBoard[row][col + 1] == otherId ||
                       gameBoard[row + 2][col + 1] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col] == id ||
                gameBoard[row + 1][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 1][col] == otherId ||
                       gameBoard[row + 1][col + 2] == otherId) {
                score++;
            }
        } else if (posy == 2) {
            score = 2;
            if (gameBoard[row + 1][col] == id ||
                gameBoard[row + 1][col + 1] == id) {
                score--;
            } else if (gameBoard[row + 1][col] == otherId ||
                       gameBoard[row + 1][col + 1] == otherId) {
                score++;
            }
            if (gameBoard[row][col + 2] == id ||
                gameBoard[row + 2][col + 2] == id) {
                score--;
            } else if (gameBoard[row][col + 2] == otherId ||
                       gameBoard[row + 2][col + 2] == otherId) {
                score++;
            }
        }
    } else if (posx == 2) {
        if (posy == 0) {
            score = 3;
            if (gameBoard[row][col] == id || gameBoard[row + 1][col] == id) {
                score--;
            } else if (gameBoard[row][col] == otherId ||
                       gameBoard[row + 1][col] == otherId) {
                score++;
            }
            if (gameBoard[row + 1][col + 1] == id ||
                gameBoard[row][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 1][col + 1] == otherId ||
                       gameBoard[row][col + 2] == otherId) {
                score++;
            }
            if (gameBoard[row + 2][col + 1] == id ||
                gameBoard[row + 2][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 2][col + 1] == otherId ||
                       gameBoard[row + 2][col + 2] == otherId) {
                score++;
            }
        } else if (posy == 1) {
            score = 2;
            if (gameBoard[row][col + 1] == id ||
                gameBoard[row + 1][col + 1] == id) {
                score--;
            } else if (gameBoard[row][col + 1] == otherId ||
                       gameBoard[row + 1][col + 1] == otherId) {
                score++;
            }
            if (gameBoard[row + 2][col] == id ||
                gameBoard[row + 2][col + 2] == id) {
                score--;
            } else if (gameBoard[row + 2][col] == otherId ||
                       gameBoard[row + 2][col + 2] == otherId) {
                score++;
            }

        } else if (posy == 2) {
            score = 3;
            if (gameBoard[row][col] == id ||
                gameBoard[row + 1][col + 1] == id) {
                score--;
            } else if (gameBoard[row][col] == otherId ||
                       gameBoard[row + 1][col + 1] == otherId) {
                score++;
            }
            if (gameBoard[row][col + 2] == id ||
                gameBoard[row + 1][col + 2] == id) {
                score--;
            } else if (gameBoard[row][col + 2] == otherId ||
                       gameBoard[row + 1][col + 2] == otherId) {
                score++;
            }
            if (gameBoard[row + 2][col] == id ||
                gameBoard[row + 2][col + 1] == id) {
                score--;
            } else if (gameBoard[row + 2][col] == otherId ||
                       gameBoard[row + 2][col + 1] == otherId) {
                score++;
            }
        }
    }

    return score;
}

int Minimax_Winning::heuristic(Node *node) const {
    const Field &field = node->field;
    const vector<vector<int>> &gameBoard = field.getGameBoard();
    const vector<vector<int>> &smallBoards = field.getSmallBoards();

    // Winning score based on current depth.
    Winner winner = field.gameWinner();
    if (winner == Winner::MYSELF) {
        return INT_MAX - 6000 * node->level;
    } else if (winner == Winner::OPPONENT) {
        return INT_MIN + 6000 * node->level;
    } else if (winner == Winner::DRAW) {
        return 0;
    }

    int botId = GlobalSettings::getInstance().getBotId();
    int opponentId = GlobalSettings::getInstance().getOpponentId();
    int score = 0;

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (smallBoards[i][j] == botId) {
                score +=
                    23 * computeMoveScore(smallBoards, 0, 0, i, j, opponentId);
            } else if (smallBoards[i][j] == opponentId) {
                score -= 23 * computeMoveScore(smallBoards, 0, 0, i, j, botId);
            } else {
                for (int it1 = 3 * i; it1 < 3 * (i + 1); it1++) {
                    for (int it2 = 3 * j; it2 < 3 * (j + 1); it2++) {
                        if (gameBoard[it1][it2] == botId) {
                            score += weights[i][j] *
                                     computeMoveScore(gameBoard, 3 * i, 3 * j,
                                                      it1 - 3 * i, it2 - 3 * j,
                                                      opponentId);
                        } else if (gameBoard[it1][it2] == opponentId) {
                            score -= weights[i][j] *
                                     computeMoveScore(gameBoard, 3 * i, 3 * j,
                                                      it1 - 3 * i, it2 - 3 * j,
                                                      botId);
                        }
                    }
                }
            }
        }
    }

    return score;
}

Move Minimax_Winning::preMinimax(const Field &initialField) {
    if (initialField.getMoveNumber() == 1) {
        return Move(4, 4);
    }

    return Move(10, 10);
}
