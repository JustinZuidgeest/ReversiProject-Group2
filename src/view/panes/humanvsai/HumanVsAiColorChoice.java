package view.panes.humanvsai;


import Games.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.GameLauncher;
import view.GameType;
import view.panes.BackToMainButton;

public class HumanVsAiColorChoice extends VBox {
    public HumanVsAiColorChoice(Game game, int difficulty, int timeout) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button blackColor = new Button("Play as Black");
        Button whiteColor = new Button("Play as White");
        Button backButton = new BackToMainButton();

        blackColor.setOnAction(e -> playGame(game, difficulty, Tile.BLACK, timeout));
        whiteColor.setOnAction(e -> playGame(game, difficulty, Tile.WHITE, timeout));

        this.getChildren().addAll(blackColor, whiteColor, backButton);
    }

    private void playGame(Game game, int difficulty, Tile player, int timeout){
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.startGame(game, GameType.VSAI, player, difficulty, timeout);
    }
}
