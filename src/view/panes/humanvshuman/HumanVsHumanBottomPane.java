package view.panes.humanvshuman;

import Games.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.GameLauncher;
import view.GameType;
import view.panes.BackToMainButton;

public class HumanVsHumanBottomPane extends HBox {
    public HumanVsHumanBottomPane(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button newGameButton = new Button("Start a new game");
        Button mainMenuButton = new BackToMainButton();

        newGameButton.setOnAction(e -> newGame(game));

        this.getChildren().addAll(mainMenuButton, newGameButton);
    }

    private void newGame(Game game){
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.startGame(game, GameType.LOCAL, Tile.BLACK, 1);
    }
}
