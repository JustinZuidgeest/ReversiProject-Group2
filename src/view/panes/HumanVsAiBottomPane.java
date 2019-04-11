package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.View;

public class HumanVsAiBottomPane extends HBox {
    public HumanVsAiBottomPane(Game game, int difficulty) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button newDifficultyButton = new Button("Select Another Difficulty");
        Button newColorButton = new Button("Select Another Color");
        Button mainMenuButton = new BackToMainButton();

        newDifficultyButton.setOnAction(e -> newGameDifficulty(game));
        newColorButton.setOnAction(e -> newGameColor(game, difficulty));

        this.getChildren().addAll(mainMenuButton, newDifficultyButton, newColorButton);
    }

    private void newGameDifficulty(Game game){
        DifficultyChoise difficultyChoise = new DifficultyChoise(game);
        View.getInstance().setCenter(difficultyChoise);
    }

    private void newGameColor(Game game, int difficulty){
        ColorChoice colorChoice = new ColorChoice(game, difficulty);
        View.getInstance().setCenter(colorChoice);
    }
}
