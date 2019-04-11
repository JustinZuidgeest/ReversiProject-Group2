import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    public Scene mainMenuScene;

    public Scene getScene(Stage window, Scene reversiScene, Scene ticTacToeScene, Scene rulesScene) {
        //menu center layout
        VBox centerMenu = new VBox(1);
        centerMenu.setAlignment(Pos.CENTER);
        centerMenu.setPadding(new Insets(5));

        Button reversiButton = new Button("Reversi");
        reversiButton.setMaxWidth(400);
        reversiButton.setMinHeight(100);
        reversiButton.getStylesheets().add("style.css");
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
            window.setScene(reversiScene);
        });
        ticTacToeButton.setOnAction(e -> {
            window.setScene(ticTacToeScene);
        });
        rulesButton.setOnAction(e -> {
            window.setScene(rulesScene);
        });
        quitWindowButton.setOnAction(e -> closeProgram(window));

        //main menu window
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerMenu);

        mainMenuScene = new Scene(borderPane, 1000, 700);
        mainMenuScene.getStylesheets().add("style.css");
        return mainMenuScene;
    }

    public void closeProgram(Stage window) {
        boolean result = ConfirmQuit.display("Quit", "Are you sure you want to quit?");
        if (result) {
            window.close();
        }
    }

}
