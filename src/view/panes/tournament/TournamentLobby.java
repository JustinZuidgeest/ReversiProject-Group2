package view.panes.tournament;

import Games.Controller;
import Games.Controllers.AiVsRemoteController;
import Games.Model;
import Games.Reversi.ReversiModels.ReversiMiniMaxAlphaBetaAI;
import Games.TicTacToe.TictactoeModels.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.Game;
import view.View;
import view.panes.MainMenu;

import java.util.ArrayList;

public class TournamentLobby extends VBox {
    private Controller controller;

    public TournamentLobby(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Model model;

        if(game == Game.TICTACTOE) model = new TictactoeMinimaxAlphaBetaAI(3, 1);
        else if(game == Game.REVERSI) model = new ReversiMiniMaxAlphaBetaAI(8, 1);
        else throw new IllegalArgumentException();

        controller = new AiVsRemoteController(model);
        View.getInstance().setController(controller);

        Button backAndLogout = new Button("Back to Main Menu");
        Button refreshButton = new Button("Refresh");
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        hBox.getChildren().addAll(backAndLogout, refreshButton);

        refreshButton.setOnAction(e -> {
            Platform.runLater(() ->{
                fillPlayerList(game);
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

        Platform.runLater(() -> fillPlayerList(game));
    }

    private void fillPlayerList(Game game){
        this.getChildren().clear();
        ArrayList<String> namesList = View.getInstance().getController().getServer().controllerGetPlayerList();
        System.out.println(namesList);
        Label playerList = new Label("There are " + namesList.size() +" players connected to the lobby:");
        this.getChildren().add(playerList);
        for(String player : namesList){
            Label playerLabel = new Label(player);
            this.getChildren().add(playerLabel);
        }
    }
}
