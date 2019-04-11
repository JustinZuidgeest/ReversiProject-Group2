import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RulesMenu{

    public Scene rulesScene;

    public Scene getScene(Stage window, Scene mainMenuScene, Scene revsceneRegels, Scene tttsceneRegels) {
        Button quitRules = new Button("Menu");
        Button reversiRules = new Button("Reversi rules");
        reversiRules.setMinWidth(200);
        reversiRules.setMinHeight(100);
        Button tictactoeRules = new Button("Tic Tac Toe rules");
        tictactoeRules.setMinWidth(200);
        tictactoeRules.setMinHeight(100);
        quitRules.setOnAction(e -> window.setScene(mainMenuScene));
        reversiRules.setOnAction(e -> window.setScene(revsceneRegels));
        tictactoeRules.setOnAction(e -> window.setScene(tttsceneRegels));

        VBox leftMenuRules = new VBox();
        leftMenuRules.getChildren().addAll(quitRules);

        HBox centerMenuRules = new HBox();
        centerMenuRules.getChildren().addAll(reversiRules, tictactoeRules);


        BorderPane borderPaneRules = new BorderPane();
        borderPaneRules.setLeft(leftMenuRules);
        borderPaneRules.setCenter(centerMenuRules);
        centerMenuRules.setAlignment(Pos.CENTER);

        rulesScene = new Scene(borderPaneRules, 1000, 700);
        rulesScene.getStylesheets().addAll("style.css");

        return rulesScene;
    }

}
