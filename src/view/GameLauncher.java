package view;

import Games.Controller;
import Games.Controllers.HumanVsAiController;
import Games.Controllers.HumanVsHumanController;
import Games.Model;
import Games.Reversi.ReversiModels.ReversiMiniMaxAlphaBetaAI;
import Games.Reversi.ReversiModels.ReversiRandomAI;
import Games.TicTacToe.TictactoeModels.TictactoeMinimaxAlphaBetaAI;
import Games.TicTacToe.TictactoeModels.TictactoeRandomAI;
import Games.Tile;
import view.panes.BoardPane;
import view.panes.InfoPane;
import view.panes.humanvshuman.HumanVsHumanBottomPane;

public class GameLauncher {

    public void startGame(Game game, GameType gameType, Tile player, int difficulty){
        View.getInstance().setNextMove(null);
        BoardPane boardPane;
        Model model;
        Controller controller;
        String stringOne, stringTwo = "";

        if (game == Game.TICTACTOE){
            boardPane = new BoardPane(3);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            stringOne = "Welcome to a new game of TicTacToe!";
        }
        else if(game == Game.REVERSI){
            boardPane = new BoardPane(8);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            stringOne = "Welcome to a new game of Reversi!";
        }
        else throw new IllegalArgumentException();

        if(gameType == GameType.LOCAL){
            if(game == Game.REVERSI) model = new ReversiRandomAI(8);
            else if(game == Game.TICTACTOE) model = new TictactoeRandomAI(3);
            else throw new IllegalArgumentException();
            controller = new HumanVsHumanController(model);
            stringTwo = "You are playing locally against another player";
        }
        else if(gameType == GameType.VSAI){
            if(game == Game.REVERSI) model = new ReversiMiniMaxAlphaBetaAI(8, difficulty);
            else if(game == Game.TICTACTOE) model = new TictactoeMinimaxAlphaBetaAI(3, difficulty);
            else throw new IllegalArgumentException();
            controller = new HumanVsAiController(model);
            stringTwo = "You are playing vs an AI opponent";
        }
        else throw new IllegalArgumentException();

        View.getInstance().setModel(model);

        View.getInstance().setController(controller);

        InfoPane infoPane = new InfoPane(stringOne, stringTwo);
        View.getInstance().setInfoPane(infoPane);
        View.getInstance().setTop(infoPane);

        HumanVsHumanBottomPane humanVsHumanBottomPane = new HumanVsHumanBottomPane(game);
        View.getInstance().setBottom(humanVsHumanBottomPane);

        controller.newGame();
        controller.setPlayerOne(player);
        View.getInstance().startController();
    }
}
