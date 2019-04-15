package view.panes.humanvsai;


import Games.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import view.Game;
import view.GameLauncher;
import view.GameType;
import view.panes.BackToMainButton;

public class HumanVsAiColorChoice extends VBox {
    public HumanVsAiColorChoice(Game game, int difficulty, int timeout) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Image colorImage = new Image("assets/color.png");
        ImageView colorImageView = new ImageView(colorImage);

        Button blackColor = new Button("Play as Black");
        blackColor.setPrefSize(200, 30);
        Button whiteColor = new Button("Play as White");
        whiteColor.setPrefSize(200, 30);
        Button backButton = new BackToMainButton();
        backButton.setPrefSize(200, 30);

        blackColor.setOnAction(e -> playGame(game, difficulty, Tile.BLACK, timeout));
        whiteColor.setOnAction(e -> playGame(game, difficulty, Tile.WHITE, timeout));

        this.getChildren().addAll(colorImageView, blackColor, whiteColor, backButton);
    }

    private void playGame(Game game, int difficulty, Tile player, int timeout){
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.startGame(game, GameType.VSAI, player, difficulty, timeout);
    }
}
