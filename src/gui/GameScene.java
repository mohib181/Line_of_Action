package gui;

import game.Game;
import game.Player;
import game.Position;
import game.PlayerComputer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GameScene {
    public int depth;
    public int gameSize;
    public Game game;
    public Player winner;
    public Player player1;
    public Player player2;
    public String gameMode;
    public Player currentPlayer;
    public Circle selectedCircle;
    public PlayerComputer playerComputer;

    public Button back;
    public GridPane board;
    public Label turnLabel;
    public Label moveLabel;
    public Label gameSizeLabel;
    public Label gameModeLabel;
    public Label player1NameLabel;
    public Label player2NameLabel;
    public Label player1ColorLabel;
    public Label player2ColorLabel;

    public void initGame(String string) {
        initData(string);               //game data Initialization
        initBoard();                    //game board Initialization
        game = new Game(gameSize);      //game initialization
        turnSetUp();                    //Turn Set up for currentPlayer
    }

    public void initData(String string) {
        String[] data = string.split("&&&&");

        gameSizeLabel.setText(": " + data[0] + "x" + data[0]);
        gameModeLabel.setText(": " + data[1]);
        player1NameLabel.setText(data[2]);
        player2NameLabel.setText(data[3]);

        gameSize = Integer.parseInt(data[0]);
        gameMode = data[1];

        Random random = new Random(System.currentTimeMillis());
        int r = Math.abs(random.nextInt(100));

        String color = (r%2 == 0) ? "White" : "Black";
        String opponentColor = color.equals("White") ? "Black" : "White";

        player1 = new Player(color.equals("White") ? 'w' : 'b', data[2]);
        player2 = new Player(opponentColor.equals("White") ? 'w' : 'b', data[3]);

        if (gameMode.equals("Human vs Computer")) {
            playerComputer = new PlayerComputer(player2.getPlayerColor(), player2.getPlayerName());
            if (gameSize == 6) depth = 4;
            else if (gameSize == 8) depth = 3;
        }

        player1ColorLabel.setText(": " + color);
        player2ColorLabel.setText(": " + opponentColor);

        currentPlayer = (player1.getPlayerColor() == 'b') ? player1 : player2;
        turnLabel.setText(currentPlayer.getPlayerName() +"'s Turn (" + currentPlayer.getPlayerColor() + ")");
        moveLabel.setText("");
    }

    public void initBoard() {
        int n = gameSize;
        for (int i = 0; i < n; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / n);
            colConst.setHalignment(HPos.valueOf("CENTER"));
            board.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < n; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / n);
            rowConst.setValignment(VPos.valueOf("CENTER"));
            board.getRowConstraints().add(rowConst);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board.getChildren().add(createPane(i, j));
            }
        }

        double radius = board.getColumnConstraints().get(0).getPercentWidth()*1.5;
        for (int i = 0; i < n-2; i++) {
            board.getChildren().add(createCircle(0, i+1, radius, "BLACK"));
            board.getChildren().add(createCircle(n-1, i+1, radius,"BLACK"));
        }
        for (int i = 0; i < n-2; i++) {
            board.getChildren().add(createCircle( i+1, 0, radius,"WHITE"));
            board.getChildren().add(createCircle( i+1, n-1, radius, "WHITE"));
        }
    }

    public Circle createCircle(int row, int col, double radius, String color) {
        Circle circle = new Circle(radius);

        circle.setId(color + "_" + row + "_" + col);
        circle.setFill(Paint.valueOf(color));
        circle.getProperties().put("gridpane-row", row);
        circle.getProperties().put("gridpane-column", col);
        circle.setOnMouseClicked(this::showMoves);

        return circle;
    }

    public Pane createPane(int row, int col) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #3C64A3; -fx-border-color: #000000");
        pane.getProperties().put("gridpane-row", row);
        pane.getProperties().put("gridpane-column", col);
        pane.setOnMouseClicked(this::clickOnPane);

        return pane;
    }

    public void resetAllPane() {
        for (Node node: board.getChildren()) {
            if (node instanceof Pane) {
                node.setStyle("-fx-background-color: #3C64A3; -fx-border-color: #000000");
            }
        }
    }

    public Circle searchCircleByID(String circleID) {
        for (Node node: board.getChildren()) {
            if (node instanceof Circle) {
                if (node.getId().contains(circleID)) return (Circle) node;
            }
        }
        return null;
    }

    public void turnSetUp() {
        String color = (currentPlayer.getPlayerColor() == 'w') ? "WHITE" : "BLACK";
        for (Node node: board.getChildren()) {
            if (node instanceof Circle) {
                node.setDisable(!node.getId().startsWith(color));       //Disabling all the circles that are of different color
            }
        }
        turnLabel.setText(currentPlayer.getPlayerName() + "'s Turn (" + currentPlayer.getPlayerColor() + ")");

        if (playerComputer != null && currentPlayer.getPlayerName().equals("Computer")) {
            makeMoveComputer();
        }
    }

    public void makeMove(Circle circle, Pane pane) {
        int row = (int) circle.getProperties().get("gridpane-row");
        int col = (int) circle.getProperties().get("gridpane-column");
        int moveAheadRow = (int) pane.getProperties().get("gridpane-row");
        int moveAheadCol = (int) pane.getProperties().get("gridpane-column");

        Circle moveAheadCircle = searchCircleByID(moveAheadRow + "_" + moveAheadCol);
        if (moveAheadCircle != null) board.getChildren().remove(moveAheadCircle);                   //removing the circle in target pane if there is any(Capture Move)

        board.getChildren().remove(circle);

        //updating circle position
        circle.getProperties().clear();
        circle.getProperties().put("gridpane-row", moveAheadRow);
        circle.getProperties().put("gridpane-column", moveAheadCol);

        //setting up circleID with the new Position
        String circleID = circle.getId().replace("_" + row + "_" + col, "_" + moveAheadRow + "_" + moveAheadCol);
        circle.setId(circleID);

        board.getChildren().add(circle);

        currentPlayer.makeMove(game.getBoard(), row, col, moveAheadRow, moveAheadCol);
        //System.out.println(currentPlayer.getPlayerName() + ": (" + row + "," + col + ")--->(" + moveAheadRow + "," + moveAheadCol + ")" );
        game.printBoard();

        char color = (currentPlayer.getPlayerName().equals(player1.getPlayerName())) ? player1.getPlayerColor() : player2.getPlayerColor();
        char opponentColor = (color == 'w') ? 'b' : 'w';

        if (game.isConnected(color)) {
            winner = currentPlayer;
            turnLabel.setText(winner.getPlayerName() + "(" + winner.getPlayerColor() + ") has won");
            board.setDisable(true);
            moveLabel.setText("End State reached");
            System.out.println("Game Ended");
        }
        else if (game.isConnected(opponentColor)) {
            winner = (currentPlayer.getPlayerName().equals(player1.getPlayerName())) ? player1 : player2;
            turnLabel.setText(winner.getPlayerName() + "(" + winner.getPlayerColor() + ") has won");
            board.setDisable(true);
            moveLabel.setText("End State reached");
            System.out.println("Game Ended");
        }
        else {
            currentPlayer = (currentPlayer.getPlayerName().equals(player1.getPlayerName())) ? player2 : player1;
            turnSetUp();
        }
    }

    public void makeMoveComputer() {
        //System.out.println("Computer preparing to make a move");
        ArrayList<Position> move = playerComputer.findBestMove(game.getBoard(), depth);
        if (move.size() > 0) {
            int index;
            int row = move.get(0).getX();
            int col = move.get(0).getY();
            int moveAheadRow = move.get(1).getX();
            int moveAheadCol = move.get(1).getY();
            //System.out.println(row + "," + col + "--->" + moveAheadRow + "," + moveAheadCol);
            moveLabel.setText("Computer:(" + row + "," + col + ")--->(" + moveAheadRow + "," + moveAheadCol + ")");

            index = moveAheadRow*board.getColumnCount()+moveAheadCol;
            Pane pane = (Pane) board.getChildren().get(index);                  //Destination Pane
            Circle circle = searchCircleByID(row + "_" + col);           //Source Circle

            if (circle != null) makeMove(circle, pane);
            else System.out.println("Circle Not Found");

            //System.out.println("Computer has made a move");
        }
        else System.out.println("Computer has no move");
    }

    public void clickOnPane(MouseEvent mouseEvent) {
        Pane pane = (Pane) mouseEvent.getSource();
        if (pane.getStyle().equals("-fx-background-color: #00d974; -fx-border-color: #000000")) {
            makeMove(selectedCircle, pane);
        }
        resetAllPane();
    }

    public void showMoves(MouseEvent mouseEvent) {
        resetAllPane();
        selectedCircle = (Circle) mouseEvent.getSource();

        int row = (int) selectedCircle.getProperties().get("gridpane-row");
        int col = (int) selectedCircle.getProperties().get("gridpane-column");

        int index = row*board.getColumnCount()+col;
        board.getChildren().get(index).setStyle("-fx-background-color: #fcba03; -fx-border-color: #000000");

        ArrayList<Position> moves = currentPlayer.generateMoves(game.getBoard(), row, col);
        for (Position pos: moves) {
            index = pos.getX()*board.getColumnCount()+pos.getY();
            board.getChildren().get(index).setStyle("-fx-background-color: #00d974; -fx-border-color: #000000");
        }
        //System.out.println("Showed All Possible Moves");
    }

    public void backToHomeScene(ActionEvent actionEvent) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getResource("homeScene.fxml"));
        Scene homePageScene = new Scene(homePageParent);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(homePageScene);
        stage.setTitle("Line of Action | Home");
        stage.setWidth(600);
        stage.setHeight(300);
        stage.show();
    }
}
