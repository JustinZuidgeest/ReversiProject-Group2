package view.panes;

import Games.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.GameLauncher;
import view.GameType;
import view.View;
import view.panes.humanvsai.DifficultyChoice;

public class GameTypeChoice extends VBox {
    public GameTypeChoice(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button vsHumanButton = new Button("Play Local Multiplayer");
        Button vsAIButton = new Button("Play Against AI");
        Button vsRemoteButton = new Button("Play vs Remote Opponent");
        Button tournamentButton = new Button("Participate in a tournament");
        Button backButton = new BackToMainButton();

        vsHumanButton.setOnAction(e -> vsHuman(game));
        vsAIButton.setOnAction(e -> vsAI(game));
        vsRemoteButton.setOnAction(e -> vsRemote(game));
        tournamentButton.setOnAction(e -> tournament(game));

        this.getChildren().addAll(vsHumanButton, vsAIButton, vsRemoteButton, backButton);
    }

    private void vsHuman(Game game){
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.startGame(game, GameType.LOCAL, Tile.BLACK, 1);
    }

    private void vsAI(Game game){
        DifficultyChoice difficultyChoice = new DifficultyChoice(game);
        View.getInstance().setCenter(difficultyChoice);
    }

    private void vsRemote(Game game){
        LoginPane loginPane = new LoginPane(GameType.REMOTE, game);
        View.getInstance().setCenter(loginPane);
        View.getInstance().setBottom(new BackToMainButton());
    }

    private void tournament(Game game){
        //TODO implement this functionality
    }
}
