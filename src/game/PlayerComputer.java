package game;

import java.util.ArrayList;

public class PlayerComputer extends Player {
    final static int MAXVALUE = 100000;
    final static int MINVALUE = -100000;

    char opponentColor;
    ArrayList<Position> pieces;
    ArrayList<Position> opponentPieces;

    public PlayerComputer(char playerColor, String playerName) {
        super(playerColor, playerName);
        opponentColor = (playerColor == 'w') ? 'b' : 'w';
        pieces = new ArrayList<>();
        opponentPieces = new ArrayList<>();
    }

    public int evaluateBoardPieceTable(char[][] board) {
        int[][] pieceSquareTable = {
                {-80, -25, -20, -20, -20, -20, -25, -80},
                {-25, 10, 10, 10, 10, 10, 10, -25},
                {-20, 10, 25, 25, 25, 25, 10, -20},
                {-20, 10, 25, 50, 50, 25, 10, -20},
                {-20, 10, 25, 50, 50, 25, 10, -20},
                {-20, 10, 25, 25, 25, 25, 10, -20},
                {-25, 10, 10, 10, 10, 10, 10, -25},
                {-80, -25, -20, -20, -20, -20, -25, -80}
        };

        int[][] pieceSquareTable6 = {
                {-80, -20, -20, -20, -20, -80},
                {-20, 25, 25, 25, 25, -20},
                {-20, 25, 50, 50, 25, -20},
                {-20, 25, 50, 50, 25, -20},
                {-20, 25, 25, 25, 25, -20},
                {-80, -20, -20, -20, -20, -80}
        };

        int[][] scoreTable = new int[0][];

        if (board.length == 8) scoreTable = pieceSquareTable;
        else if (board.length == 6) scoreTable = pieceSquareTable6;

        int myScore = 0;
        int opponentScore = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == playerColor) myScore += scoreTable[i][j];
                else if (board[i][j] == opponentColor) opponentScore += scoreTable[i][j];
            }
        }

        return myScore-opponentScore;
    }

    public int minimax(char[][] board, int depth, int alpha, int beta, boolean isMax) {
        int value;
        char prevValue;
        Position prev;
        ArrayList<Position> moves;

        if (depth == 0) {
            return evaluateBoardPieceTable(board);
        }

        if (isMax) {
            value = MINVALUE;

            for (Position piece: pieces) {
                moves = generateMoves(board, piece.x, piece.y);

                for (Position pos: moves) {
                    prev = new Position(piece.x, piece.y);
                    prevValue = makeMove(board, piece.x, piece.y, pos.x, pos.y);
                    piece.x = pos.x;
                    piece.y = pos.y;

                    value = Math.max(value, minimax(board, depth-1, alpha, beta, false));

                    piece.x = prev.x;
                    piece.y = prev.y;
                    undoMove(board, piece.x, piece.y, pos.x, pos.y, prevValue);


                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) break;
                }
            }
        }
        else {
            value = MAXVALUE;

            for (Position piece: opponentPieces) {
                moves = generateMoves(board, piece.x, piece.y);

                for (Position pos: moves) {
                    prev = new Position(piece.x, piece.y);
                    prevValue = makeMove(board, piece.x, piece.y, pos.x, pos.y);
                    piece.x = pos.x;
                    piece.y = pos.y;

                    value = Math.min(value, minimax(board, depth-1, alpha, beta, true));

                    piece.x = prev.x;
                    piece.y = prev.y;
                    undoMove(board, piece.x, piece.y, pos.x, pos.y, prevValue);

                    beta = Math.min(beta, value);
                    if (beta <= alpha) break;
                }
            }
        }

        /*if (isMax) {
            value = MINVALUE;

            ArrayList<Position> moves;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == playerColor) {
                        moves = generateMoves(board, i, j);

                        for (Position pos: moves) {
                            prevValue = makeMove(board, i, j, pos.x, pos.y);

                            value = Math.max(value, minimax(board, depth-1, alpha, beta, false));

                            undoMove(board, i, j, pos.x, pos.y, prevValue);

                            alpha = Math.max(alpha, value);
                            if (alpha >= beta) break;
                        }
                    }
                }
            }
        }
        else {
            value = MAXVALUE;

            ArrayList<Position> moves;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == opponentColor) {
                        moves = generateMoves(board, i, j);

                        for (Position pos: moves) {
                            prevValue = makeMove(board, i, j, pos.x, pos.y);

                            value = Math.min(value, minimax(board, depth-1, alpha, beta, true));

                            undoMove(board, i, j, pos.x, pos.y, prevValue);

                            beta = Math.min(beta, value);
                            if (beta <= alpha) break;
                        }
                    }
                }
            }
        }*/
        return value;
    }

    public ArrayList<Position> findBestMove(char[][] board, int depth) {
        int currMoveVal;
        int bestMoveVal = -1000;
        int row = -1, col = -1;
        int bestRow = -1, bestCol = -1;
        char prevValue;

        pieces.clear();
        opponentPieces.clear();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == playerColor) {
                    pieces.add(new Position(i, j));
                }
                else if (board[i][j] == opponentColor) {
                    opponentPieces.add(new Position(i, j));
                }
            }
        }

        Position prev;
        ArrayList<Position> moves = new ArrayList<>();
        for (Position piece: pieces) {
            moves = generateMoves(board, piece.x, piece.y);
            System.out.println(moves);

            for (Position pos : moves) {
                prev = new Position(piece.x, piece.y);
                prevValue = makeMove(board, piece.x, piece.y, pos.x, pos.y);
                piece.x = pos.x;
                piece.y = pos.y;

                currMoveVal = minimax(board, depth, MINVALUE, MAXVALUE, true);

                piece.x = prev.x;
                piece.y = prev.y;
                undoMove(board, piece.x, piece.y, pos.x, pos.y, prevValue);

                if (currMoveVal > bestMoveVal) {
                    row = piece.x;
                    col = piece.y;
                    bestRow = pos.x;
                    bestCol = pos.y;
                }
            }
        }

        moves.clear();
        moves.add(new Position(row, col));
        moves.add(new Position(bestRow, bestCol));

        return moves;
        //return 'x';
    }
}
