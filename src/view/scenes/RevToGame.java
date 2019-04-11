package view.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Difficulty;
import view.Main;
import view.Scenes;
import view.reversiBoard;

public class RevToGame {

    //Reversi

    public Scene getScene() {
        Button quitReversi = new Button("Quit game");
        quitReversi.setOnAction(e -> Main.getInstance().switchScene(Scenes.MAINMENU));

        Pane reversiBoard = new reversiBoard().getBoard();

        VBox leftMenuR = new VBox(10);
        leftMenuR.getChildren().addAll(quitReversi);

        VBox rightMenuT = new VBox(10);

        Button play = new Button("play");
        play.setOnAction(e -> startGame());

        Button remoteButton = new Button("Remote");

        Button stop = new Button("stop");
        rightMenuT.getChildren().addAll(play, remoteButton, stop);

        BorderPane borderPaneRev = new BorderPane();
        borderPaneRev.setTop(leftMenuR);
        borderPaneRev.setCenter(reversiBoard);
        borderPaneRev.setRight(rightMenuT);

        Scene reversiScene = new Scene(borderPaneRev, 1000, 700);
        reversiScene.getStylesheets().addAll("view/style.css");
        return reversiScene;
    }

    public void startGame() {
        int result = Difficulty.difPicker("Difficulty", "Select difficulty");

        if (result == 1) {
        }
        else if (result == 2) {
        }
        else if (result == 3) {
        }
        else if (result == 4){
            //pvp
        }

    }
}
