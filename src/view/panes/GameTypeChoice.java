package view.panes;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.Controllers.HumanVsHumanController;
import Games.Reversi.ReversiModels.ReversiRandomAI;
import Games.TicTacToe.TictactoeModels.TictactoeRandomAI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;
import view.panes.humanvsai.DifficultyChoice;
import view.panes.humanvshuman.HumanVsHumanBottomPane;

public class GameTypeChoice extends VBox {
    public GameTypeChoice(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button vsHumanButton = new Button("Play Local Multiplayer");
        Button vsAIButton = new Button("Play Against AI");
        Button vsRemoteButton = new Button("Play vs Remote Opponent");
        Button backButton = new BackToMainButton();

        vsHumanButton.setOnAction(e -> vsHuman(game));
        vsAIButton.setOnAction(e -> vsAI(game));
        vsRemoteButton.setOnAction(e -> vsRemote(game));

        this.getChildren().addAll(vsHumanButton, vsAIButton, vsRemoteButton, backButton);
    }

    private void vsHuman(Game game){
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

    private void vsAI(Game game){
        DifficultyChoice difficultyChoice = new DifficultyChoice(game);
        View.getInstance().setCenter(difficultyChoice);
    }

    private void vsRemote(Game game){
        //TODO implement this functionality
    }
}
