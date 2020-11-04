package game;

import java.util.LinkedList;
import java.util.Queue;

public class Game {
    int boardSize;
    char[][] board;

    public Game(int boardSize) {
        this.boardSize = boardSize;
        this.board = new char[boardSize][boardSize];
        this.initiateBoard();
    }

    public void initiateBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = '-';
            }
        }

        for (int j = 1; j < boardSize-1; j++) {
            board[0][j] = 'b';
            board[boardSize-1][j] = 'b';
        }
        for (int i = 1; i < boardSize-1; i++) {
            board[i][0] = 'w';
            board[i][boardSize-1] = 'w';
        }
    }

    public void printBoard() {
        for (char[] chars : board) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(chars[j] + " ");
            }
            System.out.println();
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public int getPieceCount(char color) {
        int pieceCount = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == color) pieceCount++;
            }
        }
        return pieceCount;
    }

    public boolean isConnected(char color) {
        int pieceCount = getPieceCount(color);

        int row = 0, col = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == color) {
                    row = i;
                    col = j;

                    i += 100;
                    break;
                }
            }
        }
        //System.out.println(row + " " + col);

        char[][] discovered = new char[board.length][board.length];
        for (int i = 0; i < discovered.length; i++) {
            for (int j = 0; j < discovered.length; j++) {
                discovered[i][j] = ' ';
            }
        }

        int connectedCount = 0;
        Queue<Position> queue = new LinkedList<>();

        discovered[row][col] = '!';
        queue.add(new Position(row,col));
        connectedCount++;

        Position pos;
        int[] ara = {-1, 0, 1};
        while (!queue.isEmpty()) {
            pos = queue.remove();
            row = pos.getX();
            col = pos.getY();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (row+ara[i] < 0 || row+ara[i] >= board.length || col+ara[j] < 0 || col+ara[j] >= board.length) continue;
                    if (board[row+ara[i]][col+ara[j]] == color && discovered[row+ara[i]][col+ara[j]] != '!') {
                        discovered[row+ara[i]][col+ara[j]] = '!';
                        queue.add(new Position(row+ara[i],col+ara[j]));
                        connectedCount++;
                    }
                }
            }
        }

        //System.out.println("ConnectedCount: " + connectedCount + " pieceCount: " + pieceCount);
        return pieceCount == connectedCount;
    }
}
