package view.panes.humanvsai;

import games.Controller;
import games.Model;
import games.Tile;
import games.controllers.HumanVsAiController;
import games.reversi.reversimodels.ReversiMinimaxAlphaBetaAI;
import games.tictactoe.tictactoemodels.TictactoeMinimaxAlphaBetaAI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;
import view.panes.BackToMainButton;
import view.panes.BoardPane;
import view.panes.ScorePane;

public class ColorChoice extends VBox {
    public ColorChoice(Game game, int difficulty) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button blackColor = new Button("Play as Black");
        Button whiteColor = new Button("Play as White");
        Button backButton = new BackToMainButton();

        blackColor.setOnAction(e -> playGame(game, difficulty, Tile.BLACK));
        whiteColor.setOnAction(e -> playGame(game, difficulty, Tile.WHITE));

        this.getChildren().addAll(blackColor, whiteColor, backButton);
    }

    private void playGame(Game game, int difficulty, Tile player){
        BoardPane boardPane;
        Model model;
        Controller controller;

        if (game == Game.TICTACTOE){
            boardPane = new BoardPane(3);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            model = new TictactoeMinimaxAlphaBetaAI(3, difficulty);
        }
        else if(game == Game.REVERSI){
            boardPane = new BoardPane(8);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            model = new ReversiMinimaxAlphaBetaAI(8, difficulty);
        }
        else throw new IllegalArgumentException();

        View.getInstance().setModel(model);

        controller = new HumanVsAiController(model);
        View.getInstance().setController(controller);

        ScorePane scorePane = new ScorePane();
        View.getInstance().setScorePane(scorePane);
        View.getInstance().setTop(scorePane);

        HumanVsAiBottomPane humanVsAiBottomPane = new HumanVsAiBottomPane(game, difficulty);
        View.getInstance().setBottom(humanVsAiBottomPane);

        controller.newGame();
        controller.setPlayerOne(player);
    }
}
