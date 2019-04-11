package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;


public class RulesChoice extends VBox {
    public RulesChoice() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button tictactoeRules = new Button("TicTacToe Rules");
        Button reversiRules = new Button("Reversi Rules");
        Button backButton = new BackToMainButton();

        tictactoeRules.setOnAction(e -> tictactoeRules());
        reversiRules.setOnAction(e -> reversiRules());

        this.getChildren().addAll(tictactoeRules, reversiRules, backButton);
    }

    private void reversiRules(){
        ReversiRules reversiRules = new ReversiRules();
        View.getInstance().setCenter(reversiRules);
    }

    private void tictactoeRules(){
        TictactoeRules tictactoeRules = new TictactoeRules();
        View.getInstance().setCenter(tictactoeRules);
    }
}
