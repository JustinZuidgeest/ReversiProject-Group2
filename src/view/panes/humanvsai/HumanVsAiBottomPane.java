package view.panes.humanvsai;

import Games.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.GameLauncher;
import view.GameType;
import view.View;
import view.panes.BackToMainButton;

public class HumanVsAiBottomPane extends HBox {
    public HumanVsAiBottomPane(Game game, int difficulty, Tile player) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button newDifficultyButton = new Button("Select Another Difficulty");
        Button newColorButton = new Button("Select Another Color");
        Button newGameButton = new Button("Start a new Game");
        Button mainMenuButton = new BackToMainButton();

        newDifficultyButton.setOnAction(e -> newGameDifficulty(game));
        newColorButton.setOnAction(e -> newGameColor(game, difficulty));
        newGameButton.setOnAction(e -> newGame(game, difficulty, player));

        this.getChildren().addAll(mainMenuButton, newDifficultyButton, newColorButton, newGameButton);
    }

    private void newGameDifficulty(Game game){
        DifficultyChoice difficultyChoice = new DifficultyChoice(game);
        View.getInstance().setNextMove(null);
        View.getInstance().killController();
        View.getInstance().clearStage();
        View.getInstance().setCenter(difficultyChoice);
    }

    private void newGameColor(Game game, int difficulty){
        HumanVsAiColorChoice humanVsAiColorChoice = new HumanVsAiColorChoice(game, difficulty);
        View.getInstance().setNextMove(null);
        View.getInstance().killController();
        View.getInstance().clearStage();
        View.getInstance().setCenter(humanVsAiColorChoice);
    }

    private void newGame(Game game, int difficulty, Tile player){
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.startGame(game, GameType.VSAI, player, difficulty);
    }
}
