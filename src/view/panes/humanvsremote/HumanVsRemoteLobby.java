package view.panes.humanvsremote;

import Games.Controller;
import Games.Controllers.HumanVsRemoteController;
import Games.Model;
import Games.Reversi.ReversiModels.ReversiMiniMaxAlphaBetaAI;
import Games.Reversi.ReversiModels.ReversiRandomAI;
import Games.TicTacToe.TictactoeModels.TictactoeMinimaxAlphaBetaAI;
import Games.TicTacToe.TictactoeModels.TictactoeRandomAI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.Game;
import view.GameType;
import view.View;
import view.panes.BoardPane;
import view.panes.InfoPane;
import view.panes.MainMenu;

import java.util.ArrayList;
import java.util.Map;

public class HumanVsRemoteLobby extends HBox {

    private VBox leftPane;
    private VBox rightPane;
    private Controller controller;

    public HumanVsRemoteLobby(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        leftPane = new VBox();
        leftPane.setAlignment(Pos.TOP_LEFT);
        rightPane = new VBox();
        rightPane.setAlignment(Pos.TOP_RIGHT);

        Model model;

        if(game == Game.TICTACTOE) model = new TictactoeMinimaxAlphaBetaAI(3, 1);
        else if(game == Game.REVERSI) model = new ReversiMiniMaxAlphaBetaAI(8, 1);
        else throw new IllegalArgumentException();

        controller = new HumanVsRemoteController(model);
        View.getInstance().setController(controller);

        Button backAndLogout = new Button("Back to Main Menu");
        Button refreshButton = new Button("Refresh");
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        hBox.getChildren().addAll(backAndLogout, refreshButton);

        refreshButton.setOnAction(e -> {
            Platform.runLater(() ->{
                fillLeftPane(game);
                fillRightPane(game);
            });
        });
        backAndLogout.setOnAction(e ->  {
            MainMenu mainMenu = new MainMenu();
            View.getInstance().killController();
            View.getInstance().clearStage();
            View.getInstance().setCenter(mainMenu);
            controller.getServer().logout();
        });

        View.getInstance().setBottom(hBox);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fillLeftPane(game);
        fillRightPane(game);

        this.getChildren().addAll(leftPane, rightPane);
    }

    private void fillRightPane(Game game){
        rightPane.getChildren().clear();
        Label acceptChallenges = new Label("Accept Challenges:");
        rightPane.getChildren().add(acceptChallenges);
        ArrayList<Map> challengeList = View.getInstance().getController().getServer().controllerGetChallengeList();
        for(Map map : challengeList){
            String gameString = (game == Game.REVERSI) ? "Reversi" : "Tic-tac-toe";
            if(map.get("GAMETYPE").equals(gameString)){
                Button button = new Button("Accept challenge from " + map.get("CHALLENGER"));
                button.setOnMouseClicked(e -> acceptChallenge((String)map.get("CHALLENGENUMBER"), game));
                rightPane.getChildren().add(button);
            }
        }
    }

    private void fillLeftPane(Game game){
        leftPane.getChildren().clear();
        Label playersOnline = new Label("Invite Players:");
        leftPane.getChildren().add(playersOnline);
        ArrayList<String> namesList = View.getInstance().getController().getServer().controllerGetPlayerList();
        System.out.println(namesList);
        for(String player : namesList){
            Button button = new Button("Invite " + player);
            button.setOnMouseClicked(e -> invitePlayer(player, game));
            leftPane.getChildren().add(button);
        }
    }

    private void invitePlayer(String player, Game game){
        String gameString = (game == Game.REVERSI) ? "Reversi" : "Tic-tac-toe";
        View.getInstance().getController().getServer().challenge(player, gameString);
    }

    private void acceptChallenge(String number, Game game){
        View.getInstance().getController().getServer().acceptChallenge(Integer.parseInt(number));
        challengeAccepted(game);
    }

    private void challengeAccepted(Game game){
        View.getInstance().clearStage();
        View.getInstance().setNextMove(null);
        BoardPane boardPane;
        String stringOne;
        String stringTwo = "You are playing vs a remote opponent";

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

        InfoPane infoPane = new InfoPane(stringOne, stringTwo);
        View.getInstance().setInfoPane(infoPane);
        View.getInstance().setTop(infoPane);

        HumanVsRemoteBottomPane humanVsRemoteBottomPane = new HumanVsRemoteBottomPane();
        View.getInstance().setBottom(humanVsRemoteBottomPane);

        View.getInstance().getController().newGame();
    }
}
