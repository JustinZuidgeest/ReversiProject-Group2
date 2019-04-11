package view.panes.humanvsai;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;
import view.panes.BackToMainButton;

public class DifficultyChoice extends VBox {
    public DifficultyChoice(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button easyButton = new Button("Play Easy Difficulty");
        Button mediumButton = new Button("Play Medium Difficulty");
        Button hardButton = new Button("Play Hard Difficulty");
        Button backButton = new BackToMainButton();

        easyButton.setOnAction(e -> initColorChoice(game, 1));
        mediumButton.setOnAction(e -> initColorChoice(game, 3));
        hardButton.setOnAction(e -> initColorChoice(game, 8));

        this.getChildren().addAll(easyButton, mediumButton, hardButton, backButton);
    }

    private void initColorChoice(Game game, int difficulty) {
        HumanVsAiColorChoice humanVsAiColorChoice = new HumanVsAiColorChoice(game, difficulty);
        View.getInstance().setCenter(humanVsAiColorChoice);
    }
}
