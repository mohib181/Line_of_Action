package gui;

import game.Game;
import game.MainDemo;
import game.Position;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GameScene {
    public int gameSize;
    public String gameMode;
    public String player1;
    public String player2;
    public Game game;
    public Position selectedPos;
    public Circle selectedCircle;
    public String currentPlayer;
    public String winner;

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
        //game data Initialization
        String[] data = string.split("&&&&");
        gameSize = Integer.parseInt(data[0]);
        gameMode = data[1];
        player1 = data[2];
        player2 = data[3];

        gameSizeLabel.setText(": " + data[0]);
        gameModeLabel.setText(": " + data[1]);
        player1NameLabel.setText(data[2]);
        player2NameLabel.setText(data[3]);

        Random random = new Random(System.currentTimeMillis());
        int r = Math.abs(random.nextInt(100));

        String color = (r%2 == 0) ? "White" : "Black";
        String opponentColor = color.equals("White") ? "Black" : "White";
        System.out.println(color + " " + opponentColor);

        player1ColorLabel.setText(": " + color);
        player2ColorLabel.setText(": " + opponentColor);

        currentPlayer = color.equals("White") ? player1 : player2;
        turnLabel.setText(currentPlayer + "'s Turn");
        moveLabel.setText("Move generated in: ");


        //game board Initialization
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

        //game initialization
        game = new Game(n);
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
        int n = gameSize*gameSize;
        for (int i = 0; i < n; i++) {
            board.getChildren().get(i).setStyle("-fx-background-color: #3C64A3; -fx-border-color: #000000");
        }
    }

    public void makeMove(Circle circle, Pane pane) {
        int row = (int) circle.getProperties().get("gridpane-row");
        int col = (int) circle.getProperties().get("gridpane-column");
        int moveAheadRow = (int) pane.getProperties().get("gridpane-row");
        int moveAheadCol = (int) pane.getProperties().get("gridpane-column");

        board.getChildren().remove(circle);

        circle.getProperties().clear();
        circle.getProperties().put("gridpane-row", moveAheadRow);
        circle.getProperties().put("gridpane-column", moveAheadCol);

        board.getChildren().add(circle);

        MainDemo.makeMove(game.getBoard(), row, col, moveAheadRow, moveAheadCol);

        String color = (currentPlayer.equals(player1)) ? player1ColorLabel.getText() : player2ColorLabel.getText();
        char c = color.equals("White") ? 'w' : 'b';

        char ch = MainDemo.endStateAchieved(game.getBoard(), c);
        if (ch == '-') {
            currentPlayer = currentPlayer.equals(player2) ? player1 : player2;
            turnLabel.setText(currentPlayer + "'s Turn");
            //moveLabel.setText("Move generated in: ");
        }
        else if (ch == 'w') {
            if (ch == c) winner = currentPlayer;
            else winner = currentPlayer.equals(player2) ? player1 : player2;
            turnLabel.setText(winner + "has won");
            board.setDisable(true);
        }
        else {
            if (ch == c) winner = currentPlayer;
            else winner = currentPlayer.equals(player2) ? player1 : player2;
            turnLabel.setText(winner + "has won");
            board.setDisable(true);
        }
    }

    public void clickOnPane(MouseEvent mouseEvent) {
        Pane pane = (Pane) mouseEvent.getSource();
        System.out.println(pane.getStyle());
        if (pane.getStyle().equals("-fx-background-color: #00d974; -fx-border-color: #000000")) {
            System.out.println("making a move");
            makeMove(selectedCircle, pane);
        }
        else{
            System.out.println("just");
        }
        resetAllPane();
    }

    public void showMoves(MouseEvent mouseEvent) {
        resetAllPane();
        Circle circle = (Circle) mouseEvent.getSource();

        System.out.println("ID: " + circle.getId());
        System.out.println("row: " + circle.getProperties().get("gridpane-row"));
        System.out.println("col: " + circle.getProperties().get("gridpane-column"));


        int row = (int) circle.getProperties().get("gridpane-row");
        int col = (int) circle.getProperties().get("gridpane-column");

        selectedPos = new Position(row, col);
        selectedCircle = circle;
        //System.out.println("selected: " +row + "," + col + "\nmoves:");

        int index = row*board.getColumnCount()+col;
        board.getChildren().get(index).setStyle("-fx-background-color: #fcba03; -fx-border-color: #000000");

        ArrayList<Position> moves = MainDemo.generateMoves(game.getBoard(), row, col);
        for (Position pos: moves) {
            //System.out.println(pos.getX() + "," + pos.getY());
            index = pos.getX()*board.getColumnCount()+pos.getY();
            board.getChildren().get(index).setStyle("-fx-background-color: #00d974; -fx-border-color: #000000");
        }

        System.out.println("done");
    }

    public void backToHomeScene(ActionEvent actionEvent) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getResource("homeScene.fxml"));
        Scene homePageScene = new Scene(homePageParent);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(homePageScene);
        window.setWidth(600);
        window.setHeight(300);
        window.show();
    }
}
