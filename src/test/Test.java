package test;

import game.Game;
import game.PlayerComputer;
import game.Position;

import java.util.ArrayList;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        int depth = 5;
        Game game = new Game(6);
        game.printBoard();

        PlayerComputer player1 = new PlayerComputer('w', "Computer1");
        PlayerComputer player2 = new PlayerComputer('b', "computer2");

        Random random = new Random(System.currentTimeMillis());

        PlayerComputer currentPlayer = (random.nextInt(100)%2 == 0) ? player1: player2;

        while (true) {
            System.out.println(currentPlayer.getPlayerName() + " is making a move");

            ArrayList<Position> move = currentPlayer.findBestMove(game.getBoard(), depth);
            if (move.size() > 0) currentPlayer.makeMove(game.getBoard(), move.get(0).getX(), move.get(0).getY(), move.get(1).getX(), move.get(1).getY());
            game.printBoard();

            if (game.isConnected(currentPlayer.getPlayerColor())) {
                System.out.println(currentPlayer.getPlayerName() + " has won");
                break;
            }
            else if (game.isConnected(currentPlayer.getPlayerColor())) {
                System.out.println(currentPlayer.getPlayerColor() + " has won");
                break;
            }
            else {
                currentPlayer = (currentPlayer.getPlayerName().equals(player1.getPlayerName())) ? player2 : player1;
            }
        }
        game.printBoard();
    }
}
