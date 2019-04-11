package view.panes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ScorePane extends HBox {
    private Text player1Score;
    private Text player2Score;

    public ScorePane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Text scores = new Text("Scores:");
        player1Score = new Text("Player 1: 0");
        player2Score = new Text("Player 2: 0");

        this.getChildren().addAll(scores, player1Score, player2Score);
    }

    public void updateScores(int score1, int score2){
        player1Score.setText("Player 1: " + score1);
        player2Score.setText("Player 2: " + score2);
    }
}
