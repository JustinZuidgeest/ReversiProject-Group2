package view.panes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class InfoPane extends VBox {
    private Text infoOne;
    private Text infoTwo;

    public InfoPane(String textOne, String textTwo) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(5);

        infoOne = new Text(textOne);
        infoTwo = new Text(textTwo);

        this.getChildren().addAll(infoOne, infoTwo);
    }

    public void updateScores(String infoOne, String infoTwo){
        this.infoOne.setText(infoOne);
        this.infoTwo.setText(infoTwo);
    }
}
