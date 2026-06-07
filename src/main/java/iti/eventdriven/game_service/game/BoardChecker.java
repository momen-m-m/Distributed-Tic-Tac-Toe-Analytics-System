package iti.eventdriven.game_service.game;

import org.springframework.stereotype.Component;

@Component
public class BoardChecker {
    public boolean hasWon(char[][] board, char symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true;
        }
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
                || (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    public boolean isBoardFull(char[][] board) {
        for (char[] row : board)
            for (char cell : row)
                if (cell == '\0') return false;
        return true;
    }

    public boolean isValidMove(char[][] board, int row, int col) {
        return row >= 0 && row < 3
                && col >= 0 && col < 3
                && board[row][col] == '\0';
    }

    public boolean isGameOver(char[][] board, char ai, char human) {
        return hasWon(board, ai) || hasWon(board, human) || isBoardFull(board);
    }
}
