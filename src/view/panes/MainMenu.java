package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;

public class MainMenu extends VBox {

    public MainMenu() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        Button tictactoeButton = new Button("Play TicTacToe");
        Button reversiButton = new Button("Play Reversi");
        Button rulesButton = new Button("Rules");
        Button quitButton = new Button("Quit");

        tictactoeButton.setOnAction(e -> tictactoeGametypeMenu());
        reversiButton.setOnAction(e -> reversiGametypeMenu());
        rulesButton.setOnAction(e -> rulesMenu());
        quitButton.setOnAction(e -> quitApplication());

        this.getChildren().addAll(tictactoeButton, reversiButton, rulesButton, quitButton);
    }

    private void tictactoeGametypeMenu(){
        GameTypeChoice gameTypeChoice = new GameTypeChoice(Game.TICTACTOE);
        View.getInstance().setCenter(gameTypeChoice);
    }

    private void reversiGametypeMenu(){
        GameTypeChoice gameTypeChoice = new GameTypeChoice(Game.REVERSI);
        View.getInstance().setCenter(gameTypeChoice);
    }

    private void rulesMenu(){
        RulesChoice rulesChoice = new RulesChoice();
        View.getInstance().setCenter(rulesChoice);
    }

    private void quitApplication(){
        View.getInstance().quitApplication();
    }
}
