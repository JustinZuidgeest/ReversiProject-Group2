import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RevToGame {

    public Scene reversiScene;
    //Reversi

    public Scene getScene(Stage window, Scene mainMenuScene) {
        Button quitReversi = new Button("Quit game");
        quitReversi.setOnAction(e -> window.setScene(mainMenuScene));

        Pane reversiBoard = new reversiBoard().getBoard();

        VBox leftMenuR = new VBox(10);
        leftMenuR.getChildren().addAll(quitReversi);

        VBox rightMenuT = new VBox(10);

        Button play = new Button("play");
        play.setOnAction(e -> startGame());

        Button stop = new Button("stop");
        rightMenuT.getChildren().addAll(play, stop);

        BorderPane borderPaneRev = new BorderPane();
        borderPaneRev.setTop(leftMenuR);
        borderPaneRev.setCenter(reversiBoard);
        borderPaneRev.setRight(rightMenuT);

        reversiScene = new Scene(borderPaneRev, 1000, 700);
        reversiScene.getStylesheets().addAll("style.css");
        return reversiScene;
    }

    public void startGame() {
        int result = Difficulty.difPicker("Difficulty", "Select difficulty");

        if (result == 1) {
        }
        if (result == 2) {
        }
        if (result == 3) {
        }

    }
}
