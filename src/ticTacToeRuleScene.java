import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ticTacToeRuleScene {

    private Scene rulesScene, tttsceneRegels;

    public Scene getScene(Stage window, Scene rulesScene){
        this.rulesScene = rulesScene;
        //tictactoe regels
        Button backtoRules2 = new Button("Back to rules");
        backtoRules2.setOnAction(e -> {
            window.setScene(rulesScene);
        });

        VBox rulesboxTTT = new VBox(10);
        rulesboxTTT.getChildren().addAll(backtoRules2);

        Text text = new Text();
        String textMessage = "Dit zijn de regels van Tic Tac Toe";
        text.setText(textMessage);
        VBox textBox = new VBox();
        textBox.getChildren().addAll(text);
        textBox.setAlignment(Pos.CENTER);

        BorderPane tttRBP = new BorderPane();
        tttRBP.setLeft(rulesboxTTT);
        tttRBP.setCenter(textBox);
        tttsceneRegels = new Scene(tttRBP, 1000, 700);
        tttsceneRegels.getStylesheets().addAll("style.css");
        return tttsceneRegels;
    }
}
