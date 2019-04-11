import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TttToGame {

    public Scene ticTacToeScene;
    static int result;

    public Scene getScene(Stage window, Scene mainMenuScene) {

        Button quitTTT = new Button("Quit game");
        quitTTT.setOnAction(e -> window.setScene(mainMenuScene));

        VBox leftMenuT = new VBox(10);
        leftMenuT.getChildren().addAll(quitTTT);

        VBox rightMenuT = new VBox(10);
        Button play = new Button("play");
        play.setOnAction(e -> startGame());

        Button stop = new Button("stop");

        rightMenuT.getChildren().addAll(play, stop);

        Button easy = new Button("easy");
        Button medium = new Button("medium");
        Button hard = new Button("hard");


        //bord maken
        int TILE_SIZE = 10;
        int WIDTH = 3;
        int HEIGHT = 3;

        Group tileGroup = new Group();
        Pane checkers = new Pane();
        checkers.getChildren().addAll(tileGroup);


        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                tileGroup.getChildren().add(tile);
            }
        }


        BorderPane borderPaneTTT = new BorderPane();
        borderPaneTTT.setTop(leftMenuT);
        borderPaneTTT.setCenter(checkers);
        borderPaneTTT.setRight(rightMenuT);

        //ttt scene maken
        ticTacToeScene = new Scene(borderPaneTTT, 1000, 700);
        ticTacToeScene.getStylesheets().addAll("style.css");
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

    }

}