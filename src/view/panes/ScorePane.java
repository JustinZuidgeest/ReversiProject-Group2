package view.panes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ScorePane extends HBox {
    private Text blackScore;
    private Text whiteScore;

    public ScorePane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Text scores = new Text("Scores:");
        blackScore = new Text("Black: 0");
        whiteScore = new Text("White: 0");

        this.getChildren().addAll(scores, blackScore, whiteScore);
    }

    public void updateScores(int black, int white){
        blackScore.setText("Black: " + black);
        whiteScore.setText("White: " + white);
    }
}
