package view.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import view.popupWindows.Difficulty;
import view.Main;

public class TttToGame {

    public Scene getScene() {

        Button quitTTT = new Button("Quit game");
        quitTTT.setOnAction(e -> Main.getInstance().switchScene(Scenes.MAINMENU));



        VBox leftMenuT = new VBox(10);
        leftMenuT.getChildren().addAll(quitTTT);

        VBox rightMenuT = new VBox(10);
        Button play = new Button("play");
        play.setOnAction(e -> startGame());

        Button remoteButton = new Button("Remote");

        Button stop = new Button("stop");

        rightMenuT.getChildren().addAll(play, remoteButton, stop);

        Button easy = new Button("easy");
        Button medium = new Button("medium");
        Button hard = new Button("hard");

        //TODO board

        BorderPane borderPaneTTT = new BorderPane();
        borderPaneTTT.setTop(leftMenuT);
        //borderPaneTTT.setCenter(checkers);
        borderPaneTTT.setRight(rightMenuT);

        //ttt scene maken
        Scene ticTacToeScene = new Scene(borderPaneTTT, 1000, 700);
        ticTacToeScene.getStylesheets().addAll("view/style.css");
        return ticTacToeScene;
    }

    public void startGame() {
        int result = Difficulty.difPicker("Difficulty", "Select difficulty");

        if (result == 1) {
        }
        if (result == 2) {
        }
        if (result == 3) {
        }
        if (result == 4){
            //PVP
        }

    }

}