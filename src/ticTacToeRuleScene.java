import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ticTacToeRuleScene extends RulesMenu{

    public Scene tttsceneRegels;

    public Scene getScene(Stage window){
        //tictactoe regels
        Button backtoRules2 = new Button("Back to rules");
        backtoRules2.setOnAction(e -> {
            window.setScene(rulesScene);
        });

        VBox rulesboxTTT = new VBox(10);
        rulesboxTTT.getChildren().addAll(backtoRules2);

        Text text = new Text();
        String textMessage = "\n\t De Tic Tac toe regels:\n\n" +
                            "\t Tic Tac Toe wordt gespeeld op 3 bij 3 velden. Bij het begin zijn alle velden leeg.\n" +
                            "\t De ene speler zet een 'kruis' en de andere speler een 'rondje'. Degene die drie \n" +
                            "\t van zijn eigen tekens op een rij heeft (diagonaal, verticaal of horizontaal),\n" +
                            "\t heeft gewonnen.";
        text.setText(textMessage);
        VBox textBox = new VBox();
        textBox.getChildren().addAll(text);
        textBox.setAlignment(Pos.TOP_LEFT);
        textBox.setFillWidth(true);

        BorderPane tttRBP = new BorderPane();
        tttRBP.setTop(rulesboxTTT);
        tttRBP.setCenter(textBox);
        tttsceneRegels = new Scene(tttRBP, 1000, 700);
        tttsceneRegels.getStylesheets().addAll("style.css");
        return tttsceneRegels;
    }
}
