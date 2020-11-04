package gui;

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


public class GameScene {
    public int gameSize;
    public String gameMode;
    public String player1;
    public String player2;

    public Button back;
    public GridPane board;
    public Label turnLabel;
    public Label moveLabel;
    public Label player1NameLabel;
    public Label player2NameLabel;
    public Label gameSizeLabel;
    public Label gameModeLabel;
    public Label player1ColorLabel;
    public Label player2ColorLabel;


    public void initGame(String string) {
        String[] data = string.split("&&&&");
        gameSize = Integer.parseInt(data[0]);
        gameMode = data[1];
        player1 = data[2];
        player2 = data[3];

        gameSizeLabel.setText(": " + data[0]);
        gameModeLabel.setText(": " + data[1]);
        player1NameLabel.setText(data[2]);
        player2NameLabel.setText(data[3]);

        player1ColorLabel.setText(": " + "White");
        player2ColorLabel.setText(": " + "Black");

        turnLabel.setText(player2 + "'s Turn");
        moveLabel.setText("Move generated in: ");

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
        pane.setStyle("-fx-background-color: #3C64A3;");
        pane.setStyle("-fx-border-color: #000000");
        pane.getProperties().put("gridpane-row", row);
        pane.getProperties().put("gridpane-column", col);

        return pane;
    }

    public void showMoves(MouseEvent mouseEvent) {
        Circle circle = (Circle) mouseEvent.getSource();

        System.out.println("ID: " + circle.getId());
        System.out.println("col: " + circle.getProperties().get("gridpane-column"));
        System.out.println("row: " + circle.getProperties().get("gridpane-row"));

        board.getChildren().remove(circle);

        circle.getProperties().clear();
        circle.getProperties().put("gridpane-row", 3);
        circle.getProperties().put("gridpane-column", 5);

        System.out.println("ID: " + circle.getId());
        System.out.println("col: " + circle.getProperties().get("gridpane-column"));
        System.out.println("row: " + circle.getProperties().get("gridpane-row"));

        board.getChildren().add(circle);

        int row = (int) circle.getProperties().get("gridpane-row");
        int col = (int) circle.getProperties().get("gridpane-column");
        System.out.println(row + " " + col);

        int index = row*board.getColumnCount()+col;
        board.getChildren().get(index).setStyle("-fx-background-color: #fcba03");
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
