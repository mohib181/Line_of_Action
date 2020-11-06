package test;

import game.Game;
import game.Position;
import game.PlayerComputer;

import java.util.ArrayList;

public class Fight {
    public static void main(String[] args) {
        int depth = 3;
        Game game = new Game(8);
        PlayerComputer player1 = new PlayerComputer('w', "Computer1");
        PlayerComputer player2 = new PlayerComputer('b', "Computer2");

        player1.setScheme(1);
        player2.setScheme(2);
        PlayerComputer currentPlayer = (player1.getPlayerColor() == 'b') ? player1 : player2;

        //Let them fight
        while (true) {
            ArrayList<Position> move = currentPlayer.findBestMove(game.getBoard(), depth);
            if (move.size() > 0) {
                currentPlayer.makeMove(game.getBoard(), move.get(0).getX(), move.get(0).getY(), move.get(1).getX(), move.get(1).getY());
                game.printBoard();
            }

            if (game.isConnected(currentPlayer.getPlayerColor())) {
                System.out.println(currentPlayer.getPlayerName() + " has won");
                break;
            }
            else if (game.isConnected( (currentPlayer.getPlayerColor() == 'w') ? 'b' : 'w' )) {
                System.out.println( (currentPlayer.getPlayerName().equals(player1.getPlayerName())) ? player2.getPlayerName() : player1.getPlayerName() + " has won");
            }
            else currentPlayer = (currentPlayer.getPlayerName().equals(player1.getPlayerName())) ? player2 : player1;
        }
        //game.printBoard();
    }
}
