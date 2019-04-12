package view.panes.humanvsai;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.controllers.HumanVsAiController;
import Games.Reversi.ReversiModels.ReversiMiniMaxAlphaBetaAI;
import Games.TicTacToe.TictactoeModels.TictactoeMinimaxAlphaBetaAI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.View;
import view.panes.BackToMainButton;
import view.panes.BoardPane;
import view.panes.ScorePane;

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
        View.getInstance().setNextMove(null);
        View.getInstance().killController();
        View.getInstance().clearStage();

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
            model = new ReversiMiniMaxAlphaBetaAI(8, difficulty);
        }
        else throw new IllegalArgumentException();

        View.getInstance().setModel(model);

        controller = new HumanVsAiController(model);
        View.getInstance().setController(controller);

        ScorePane scorePane = new ScorePane();
        View.getInstance().setScorePane(scorePane);
        View.getInstance().setTop(scorePane);

        HumanVsAiBottomPane humanVsAiBottomPane = new HumanVsAiBottomPane(game, difficulty, player);
        View.getInstance().setBottom(humanVsAiBottomPane);

        controller.newGame();
        controller.setPlayerOne(player);
        View.getInstance().startController();
    }
}
