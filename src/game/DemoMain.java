package game;

import java.util.ArrayList;

public class DemoMain {
    public static void main(String[] args) {
        int depth = 4;
        Game game = new Game(6);
        game.printBoard();

        PlayerComputer AI = new PlayerComputer('w', "Computer");

        long start = System.currentTimeMillis();
        ArrayList<Position> move = AI.findBestMove(game.getBoard(), depth);
        if (move.size() > 0) {
            AI.makeMove(game.getBoard(), move.get(0).x, move.get(0).y, move.get(1).x, move.get(1).y);
        }
        long end = System.currentTimeMillis();

        System.out.println("Duration: " + (end-start));
        game.printBoard();
    }
}
