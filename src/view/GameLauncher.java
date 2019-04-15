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
import view.panes.humanvsai.HumanVsAiBottomPane;
import view.panes.humanvshuman.HumanVsHumanBottomPane;

/**
 * Class used to launch new games of both Reversi and TicTacToe with the given settings
 */
public class GameLauncher {

    /**
     * Method used to start a game based on the settings for the game passed in as variables
     * @param game The game (TicTacToe or Reversi) that should be started represented by a Game enum object
     * @param gameType The game type (local, remote, vs Ai etc.) represented by a GameType enum object
     * @param player The player color (black or white) that the person starting the game will be
     * @param difficulty The AI difficulty (as an int value indication minimax depth) of the game
     * @param timeout The maximum time the AI can compute before coming up with a move
     */
    public void startGame(Game game, GameType gameType, Tile player, int difficulty, int timeout){
        View.getInstance().setNextMove(null);
        BoardPane boardPane;
        Model model;
        Controller controller;
        String stringOne, stringTwo = "";

        //Sets the board pane for a tictactoe game
        if (game == Game.TICTACTOE){
            boardPane = new BoardPane(3);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            stringOne = "Welcome to a new game of TicTacToe!";
        }
        //Sets the board pane for a reversi game
        else if(game == Game.REVERSI){
            boardPane = new BoardPane(8);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            stringOne = "Welcome to a new game of Reversi!";
        }
        else throw new IllegalArgumentException();

        //Sets the game model for a Local game vs another human
        if(gameType == GameType.LOCAL){
            HumanVsHumanBottomPane humanVsHumanBottomPane = new HumanVsHumanBottomPane(game);
            View.getInstance().setBottom(humanVsHumanBottomPane);
            if(game == Game.REVERSI) model = new ReversiRandomAI(8);
            else if(game == Game.TICTACTOE) model = new TictactoeRandomAI(3);
            else throw new IllegalArgumentException();
            controller = new HumanVsHumanController(model);
            stringTwo = "You are playing locally against another player";
        }
        //Sets the game model for a local game vs the computer
        else if(gameType == GameType.VSAI){
            HumanVsAiBottomPane humanVsAiBottomPane = new HumanVsAiBottomPane(game, difficulty, player, timeout);
            View.getInstance().setBottom(humanVsAiBottomPane);
            if(game == Game.REVERSI) model = new ReversiMiniMaxAlphaBetaAI(8, difficulty, timeout);
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

        controller.newGame();
        controller.setPlayerOne(player);
        View.getInstance().startController();
    }
}
