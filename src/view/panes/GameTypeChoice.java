package view.panes;

import Games.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        Image gameTypeImage = new Image("assets/gametype.png");
        ImageView gameTypeImageView = new ImageView(gameTypeImage);

        Button vsHumanButton = new Button("Play Local Multiplayer");
        vsHumanButton.setPrefSize(200, 30);
        Button vsAIButton = new Button("Play Against AI");
        vsAIButton.setPrefSize(200, 30);
        Button vsRemoteButton = new Button("Play vs Remote Opponent");
        vsRemoteButton.setPrefSize(200, 30);
        Button tournamentButton = new Button("Participate in a tournament");
        tournamentButton.setPrefSize(200, 30);
        Button backButton = new BackToMainButton();
        backButton.setPrefSize(200, 30);

        vsHumanButton.setOnAction(e -> vsHuman(game));
        vsAIButton.setOnAction(e -> vsAI(game));
        vsRemoteButton.setOnAction(e -> vsRemote(game));
        tournamentButton.setOnAction(e -> tournament(game));

        this.getChildren().addAll(gameTypeImageView, vsHumanButton, vsAIButton, vsRemoteButton, tournamentButton, backButton);
    }

    private void vsHuman(Game game){
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.startGame(game, GameType.LOCAL, Tile.BLACK, 1, 9000);
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
        LoginPane loginPane = new LoginPane(GameType.TOURNAMENT, game);
        View.getInstance().setCenter(loginPane);
        View.getInstance().setBottom(new BackToMainButton());
    }
}
