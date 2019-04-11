package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;

public class GameTypeChoice extends VBox {
    public GameTypeChoice(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button vsHumanButton = new Button("Play Local Multiplayer");
        Button vsAIButton = new Button("Play Against AI");
        Button vsRemoteButton = new Button("Play vs Remote Opponent");
        Button backButton = new BackToMainButton();

        vsHumanButton.setOnAction(e -> vsHuman(game));
        vsAIButton.setOnAction(e -> vsAI(game));
        vsRemoteButton.setOnAction(e -> vsRemote(game));

        this.getChildren().addAll(vsHumanButton, vsAIButton, vsRemoteButton, backButton);
    }

    private void vsHuman(Game game){
        //TODO implement this functionality
    }

    private void vsAI(Game game){
        DifficultyChoise difficultyChoise = new DifficultyChoise(game);
        View.getInstance().setCenter(difficultyChoise);
    }

    private void vsRemote(Game game){
        //TODO implement this functionality
    }
}
