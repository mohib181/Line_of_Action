package test;

import game.Constants;
import game.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainDemo {
    static int whitePieces;
    static int blackPieces;

    public static void initiateBoard(char[][] board) {
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '-';
            }
        }

        for (int j = 1; j < n-1; j++) {
            board[0][j] = 'b';
            board[n-1][j] = 'b';
        }
        for (int i = 1; i < n-1; i++) {
            board[i][0] = 'w';
            board[i][n-1] = 'w';
        }
    }

    public static void printBoard(char[][] board) {
        int n = board.length;
        for (char[] chars : board) {
            for (int j = 0; j < n; j++) {
                System.out.print(chars[j] + " ");
            }
            System.out.println();
        }
    }

    public static int calculateMoveAhead(char[][] board, int row, int col, String dir) {
        int moveAhead = 0;
        switch (dir) {
            case "up":
            case "down":
                for (int i = 0; i < board.length; i++) {
                    if (board[i][col] != '-') moveAhead++;
                }
                break;
            case "left":
            case "right":
                for (int j = 0; j < board.length; j++) {
                    if (board[row][j] != '-') moveAhead++;
                }
                break;
            case "upper-left":
            case "lower-right":
                int min = Math.min(row, col);
                for (int i = row - min, j = col - min; i < board.length && j < board.length; i++, j++) {
                    if (board[i][j] != '-') moveAhead++;
                }
                break;
            case "lower-left":
            case "upper-right":
                for (int i = row, j = col; i < board.length && j >= 0; i++, j--) {
                    if (board[i][j] != '-') moveAhead++;
                }
                for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
                    if (board[i][j] != '-') moveAhead++;
                }
                break;
        }
        return moveAhead;
    }

    public static Position getValidMove(char[][] board, int row, int col, int moveAhead, String dir) {
        char color = board[row][col];
        char opponentColor = (color == 'w') ? 'b' : 'w';

        if ("up".equals(dir)) {
            if (row - moveAhead < 0) return null;
            if (board[row - moveAhead][col] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row - i][col] == opponentColor) return null;
            }
            return new Position(row - moveAhead, col);
        } else if ("down".equals(dir)) {
            if (row + moveAhead >= board.length) return null;
            if (board[row + moveAhead][col] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row + i][col] == opponentColor) return null;
            }
            return new Position(row + moveAhead, col);
        } else if ("right".equals(dir)) {
            if (col + moveAhead >= board.length) return null;
            if (board[row][col + moveAhead] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row][col + i] == opponentColor) return null;
            }
            return new Position(row, col + moveAhead);
        } else if ("left".equals(dir)) {
            if (col - moveAhead < 0) return null;
            if (board[row][col - moveAhead] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row][col - i] == opponentColor) return null;
            }
            return new Position(row, col - moveAhead);
        } else if ("upper-right".equals(dir)) {
            if (row - moveAhead < 0 || col + moveAhead >= board.length) return null;
            if (board[row - moveAhead][col + moveAhead] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row - i][col + i] == opponentColor) return null;
            }
            return new Position(row - moveAhead, col + moveAhead);
        } else if ("upper-left".equals(dir)) {
            if (row - moveAhead < 0 || col - moveAhead < 0) return null;
            if (board[row - moveAhead][col - moveAhead] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row - i][col - i] == opponentColor) return null;
            }
            return new Position(row - moveAhead, col - moveAhead);
        } else if ("lower-right".equals(dir)) {
            if (row + moveAhead >= board.length || col + moveAhead >= board.length) return null;
            if (board[row + moveAhead][col + moveAhead] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row + i][col + i] == opponentColor) return null;
            }
            return new Position(row + moveAhead, col + moveAhead);
        } else if ("lower-left".equals(dir)) {
            if (row + moveAhead >= board.length || col - moveAhead < 0) return null;
            if (board[row + moveAhead][col - moveAhead] == color) return null;
            for (int i = 1; i < moveAhead; i++) {
                if (board[row + i][col - i] == opponentColor) return null;
            }
            return new Position(row + moveAhead, col - moveAhead);
        }
        return null;
    }

    public static ArrayList<Position> generateMoves(char[][] board, int row, int col) {
        ArrayList<Position> moves = new ArrayList<>();
        String[] dirs = {"up", "down", "left", "right", "upper-left", "lower-right", "lower-left", "upper-right"};
        for (String dir: dirs) {
            int moveAhead = calculateMoveAhead(board, row, col, dir);

            Position pos = getValidMove(board, row, col, moveAhead, dir);
            if (pos != null) moves.add(pos);
        }
        return moves;
    }

    public static void getAllMoves(char[][] board, char color) {
        ArrayList<Position> moves;
        System.out.println("Color: " + color);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == color) {
                    moves = generateMoves(board, i, j);

                    for (Position pos: moves) {
                        System.out.println("(" + i + "," + j + ")" + "--->(" + pos.getX() + "," + pos.getY() + ")");
                    }
                    System.out.println();
                }
            }
        }
    }

    public static char makeMove(char[][] board, int row, int col, int moveAheadRow, int moveAheadCol) {
        char value = board[moveAheadRow][moveAheadCol];
        board[moveAheadRow][moveAheadCol] = board[row][col];
        board[row][col] = '-';

        if (value == 'w') {
            whitePieces--;
        }else if (value == 'b') {
            blackPieces--;
        }

        return value;
    }

    public static void undoMove(char[][] board, int row, int col, int moveAheadRow, int moveAheadCol, char prevValue) {
        board[row][col] = board[moveAheadRow][moveAheadCol];
        board[moveAheadRow][moveAheadCol] = prevValue;

        if (prevValue == 'w') {
            whitePieces++;
        }else if (prevValue == 'b') {
            blackPieces++;
        }
    }

    public static int evaluateBoard(char[][] board) {
        int[][] scoreTable = new int[board.length][board.length];

        return 0;
    }

    public static int minimax(char[][] board, int depth, int alpha, int beta, boolean isBlack) {
        int value;
        if (depth == 0) {
            return evaluateBoard(board);
        }

        if (isBlack) {
            value = Constants.MINVALUE;

            //for all black piece
            //calculate their move set
            //then for all those moves
            ArrayList<Position> moves;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 'b') {
                        moves = generateMoves(board, i, j);

                        for (Position pos: moves) {
                            //System.out.println("(" + i + "," + j + ")" + "--->(" + pos.getX() + "," + pos.getY() + ")");
                            char prevValue = makeMove(board, i, j, pos.getX(), pos.getY());

                            value = Math.max(value, minimax(board, depth-1, alpha, beta, false));

                            undoMove(board, i, j, pos.getX(), pos.getY(), prevValue);

                            alpha = Math.max(alpha, value);
                            if (alpha >= beta) break;
                        }
                    }
                }
            }
        }
        else {
            value = Constants.MAXVALUE;

            ArrayList<Position> moves;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 'w') {
                        moves = generateMoves(board, i, j);

                        for (Position pos: moves) {
                            //System.out.println("(" + i + "," + j + ")" + "--->(" + pos.getX() + "," + pos.getY() + ")");
                            char prevValue = makeMove(board, i, j, pos.getX(), pos.getY());

                            value = Math.min(value, minimax(board, depth-1, alpha, beta, true));

                            undoMove(board, i, j, pos.getX(), pos.getY(), prevValue);

                            beta = Math.min(beta, value);
                            if (beta <= alpha) break;
                        }
                    }
                }
            }
        }
        return value;
    }

    public static void findBestMove(char[][] board, char color) {
        int currMoveVal;
        int bestMoveVal = -1000;
        int row = -1, col = -1;
        int bestRow = -1, bestCol = -1;

        ArrayList<Position> moves;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == color) {
                    moves = generateMoves(board, i, j);

                    for (Position pos : moves) {
                        char prevValue = makeMove(board, i, j, pos.getX(), pos.getY());

                        currMoveVal = minimax(board, 5, Constants.MINVALUE, Constants.MAXVALUE, true);

                        undoMove(board, i, j, pos.getX(), pos.getY(), prevValue);

                        if (currMoveVal > bestMoveVal) {
                            row = i;
                            col = j;
                            bestRow = pos.getX();
                            bestCol = pos.getY();
                        }
                    }
                }
            }
        }

        System.out.println("(" + row + "," + col + ")--->(" + bestRow + "," + bestCol + ")");
    }

    public static boolean isConnected(char[][] board, char color) {
        int pieceCount = (color == 'w') ? whitePieces : blackPieces;
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

    public static char endStateAchieved(char[][] board, char currentColor) {
        char opponentColor = (currentColor == 'w') ? 'b' : 'w';
        if (isConnected(board, currentColor)) return currentColor;
        if (isConnected(board, opponentColor)) return opponentColor;
        return '-';
    }

    public static void main(String[] args) {

        int n = 8;
        whitePieces = (n-2)*2;
        blackPieces = (n-2)*2;

        long timeOut = 1000000000;
        char[][] board = new char[n][n];

        initiateBoard(board);

        printBoard(board);

        /*System.out.println("isConnected White: " + isConnected(board,'w', whitePieces));
        System.out.println("isConnected Black: " + isConnected(board,'b', blackPieces));


        makeMove(board, 2, 0, 2,1);
        makeMove(board, 3, 0, 3,2);
        makeMove(board, 4, 0, 4,3);
        makeMove(board, 5, 0, 5,4);
        makeMove(board, 6, 0, 6,5);
        makeMove(board, 6, 7, 6,6);
        makeMove(board, 1, 7, 3,1);
        makeMove(board, 2, 7, 4,2);

        makeMove(board, 0, 2, 1,2);
        makeMove(board, 0, 3, 2,3);
        makeMove(board, 0, 4, 3,4);
        makeMove(board, 0, 5, 4,5);
        makeMove(board, 0, 6, 5,6);
        makeMove(board, 7, 1, 6,4);
        makeMove(board, 7, 2, 5,5);

        printBoard(board);
        System.out.println("isConnected White: " + isConnected(board,'w', whitePieces));
        System.out.println("isConnected Black: " + isConnected(board,'b', blackPieces));

        char value = makeMove(board, 7,5, 6, 5);
        printBoard(board);
        System.out.println("WhitePieces: " + whitePieces + " BlackPieces: " + blackPieces);
        System.out.println("isConnected White: " + isConnected(board,'w', whitePieces));
        System.out.println("isConnected Black: " + isConnected(board,'b', blackPieces));

        undoMove(board, 7,5, 6, 5, value);
        printBoard(board);
        System.out.println("WhitePieces: " + whitePieces + " BlackPieces: " + blackPieces);
        System.out.println("isConnected White: " + isConnected(board,'w', whitePieces));
        System.out.println("isConnected Black: " + isConnected(board,'b', blackPieces));
        */

        /*
        long startTime = System.nanoTime();
        long start = System.currentTimeMillis();

        long endTime = System.nanoTime();
        long end = System.currentTimeMillis();

        System.out.println("Time Elapsed " + (endTime - startTime) + " nanoseconds"); //divide by 1000000 to get milliseconds.
        System.out.println("Time Elapsed " + (end-start) + " milliseconds");
        */



        /*
        int k = 0, loopCount = 100000;
        startTime = System.nanoTime();
        for (int i = 0; i < loopCount; i++) {
            k++;
        }
        System.out.println(System.nanoTime()-startTime);

        k = 0;
        startTime = System.nanoTime();
        for (int i = 0; i < loopCount; i++) {
            System.nanoTime();
            k++;
        }
        System.out.println(System.nanoTime()-startTime);


        k = 0;
        start = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            k++;
        }
        System.out.println(System.currentTimeMillis()-start);

        k = 0;
        start = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            System.currentTimeMillis();
            k++;
        }
        System.out.println(System.currentTimeMillis()-start);
        */
    }
}
