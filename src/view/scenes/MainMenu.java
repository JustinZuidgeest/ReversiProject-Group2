package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import view.Main;

public class MainMenu {

    public Scene mainMenuScene;

    public Scene getScene() {
        //menu center layout
        VBox centerMenu = new VBox(1);
        centerMenu.setAlignment(Pos.CENTER);
        centerMenu.setPadding(new Insets(5));

        Button reversiButton = new Button("Reversi");
        reversiButton.setMaxWidth(400);
        reversiButton.setMinHeight(100);
        reversiButton.getStylesheets().add("view/style.css");
        Button ticTacToeButton = new Button("Tic Tac Toe");
        ticTacToeButton.setMaxWidth(400);
        ticTacToeButton.setMinHeight(100);
        Button rulesButton = new Button("Rules");
        rulesButton.setMaxWidth(400);
        rulesButton.setMinHeight(100);
        Button quitWindowButton = new Button("Quit");
        quitWindowButton.setMaxWidth(400);
        quitWindowButton.setMinHeight(100);

        centerMenu.getChildren().addAll(reversiButton, ticTacToeButton, rulesButton, quitWindowButton);

        reversiButton.setOnAction(e -> {
            Main.getInstance().switchScene(Scenes.REVSETTINGS);
        });
        ticTacToeButton.setOnAction(e -> {
            Main.getInstance().switchScene(Scenes.TTTSETTINGS);        });
        rulesButton.setOnAction(e -> {
            Main.getInstance().switchScene(Scenes.RULESMENU);
        });
        quitWindowButton.setOnAction(e -> Main.getInstance().closeProgram());

        //main menu window
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerMenu);

        mainMenuScene = new Scene(borderPane, 1000, 700);
        mainMenuScene.getStylesheets().add("view/style.css");
        return mainMenuScene;
    }
}
