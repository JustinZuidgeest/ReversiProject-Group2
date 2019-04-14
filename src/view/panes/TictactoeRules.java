package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.View;

public class TictactoeRules extends VBox {
    public TictactoeRules() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Text text = new Text();
        String textMessage = "\n\t De Tic Tac toe regels:\n\n" +
                "\t Tic Tac Toe wordt gespeeld op 3 bij 3 velden. Bij het begin zijn alle velden leeg.\n" +
                "\t De ene speler zet een 'kruis' en de andere speler een 'rondje'. Degene die drie \n" +
                "\t van zijn eigen tekens op een rij heeft (diagonaal, verticaal of horizontaal),\n" +
                "\t heeft gewonnen.";
        text.setText(textMessage);
        VBox textBox = new VBox();
        textBox.getChildren().addAll(text);
        textBox.setAlignment(Pos.CENTER);
        textBox.setFillWidth(true);

        Button backButton = new BackToMainButton();

        this.getChildren().addAll(textBox, backButton);
    }
}
