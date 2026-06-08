package iti.eventdriven.game_service.game;

import iti.eventdriven.game_service.dto.GameDifficulty;
import org.springframework.stereotype.Component;

@Component
public class MinMaxEngine {

    private final BoardChecker boardChecker;

    public MinMaxEngine(BoardChecker boardChecker) {
        this.boardChecker = boardChecker;
    }

    public int[] bestMove(char[][] board, char aiSymbol, char humanSymbol,
                          GameDifficulty difficulty) {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] != '\0') continue;

                board[r][c] = aiSymbol;
                int score = minimax(board, aiSymbol, humanSymbol, false,
                        Integer.MIN_VALUE, Integer.MAX_VALUE, 0, difficulty.getMaxDepth());
                board[r][c] = '\0';

                if (score > bestScore) {
                    bestScore = score;
                    bestRow = r;
                    bestCol = c;
                }
            }
        }
        return new int[]{bestRow, bestCol};
    }

    private int minimax(char[][] board, char ai, char human,
                        boolean isMaximizing, int alpha, int beta,
                        int depth, int maxDepth ) {

        if (boardChecker.hasWon(board, ai))    return 10 - depth;
        if (boardChecker.hasWon(board, human)) return depth - 10;
        if (boardChecker.isBoardFull(board))   return 0;
        if (depth >= maxDepth ) return 0; // depth limit for harder difficulties

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] != '\0') continue;

                board[r][c] = isMaximizing ? ai : human;
                int score = minimax(board, ai, human, !isMaximizing, alpha, beta, depth + 1, maxDepth );
                board[r][c] = '\0';

                if (isMaximizing) {
                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, bestScore);
                } else {
                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, bestScore);
                }
                if (beta <= alpha) break;
            }
        }
        return bestScore;
    }
}
