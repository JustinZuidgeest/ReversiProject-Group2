package view.panes.humanvshuman;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.Controllers.HumanVsHumanController;
import Games.Reversi.ReversiModels.ReversiRandomAI;
import Games.TicTacToe.TictactoeModels.TictactoeRandomAI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.View;
import view.panes.BackToMainButton;
import view.panes.BoardPane;
import view.panes.ScorePane;

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
        View.getInstance().killController();
        View.getInstance().clearStage();
        View.getInstance().setNextMove(null);

        BoardPane boardPane;
        Model model;
        Controller controller;

        if (game == Game.TICTACTOE){
            boardPane = new BoardPane(3);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            model = new TictactoeRandomAI(3);
        }
        else if(game == Game.REVERSI){
            boardPane = new BoardPane(8);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            model = new ReversiRandomAI(8);
        }
        else throw new IllegalArgumentException();

        View.getInstance().setModel(model);

        controller = new HumanVsHumanController(model);
        View.getInstance().setController(controller);

        ScorePane scorePane = new ScorePane();
        View.getInstance().setScorePane(scorePane);
        View.getInstance().setTop(scorePane);

        HumanVsHumanBottomPane humanVsHumanBottomPane = new HumanVsHumanBottomPane(game);
        View.getInstance().setBottom(humanVsHumanBottomPane);

        controller.newGame();
        controller.setPlayerOne(Tile.BLACK);
        View.getInstance().startController();
    }
}
