package view.panes.humanvsremote;

import Games.Controller;
import Games.Controllers.HumanVsRemoteController;
import Games.Model;
import Games.Reversi.ReversiModels.ReversiRandomAI;
import Games.TicTacToe.TictactoeModels.TictactoeRandomAI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.Game;
import view.View;
import view.panes.MainMenu;

import java.util.ArrayList;
import java.util.Map;

public class HumanVsRemoteLobby extends HBox {

    private VBox leftPane;
    private VBox rightPane;
    private VBox players;
    private VBox inviteButtons;
    private HBox playerRow;
    private Controller controller;
    private String ownName;

    public HumanVsRemoteLobby(Game game, String ownName) {
        this.ownName = ownName;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(30));

        leftPane = new VBox(30);
        leftPane.setAlignment(Pos.TOP_LEFT);
        rightPane = new VBox();
        rightPane.setAlignment(Pos.TOP_RIGHT);
        rightPane.setSpacing(5);

        players = new VBox(15);
        inviteButtons = new VBox(5);
        playerRow = new HBox(5);

        Model model;

        if(game == Game.TICTACTOE) model = new TictactoeRandomAI(3);
        else if(game == Game.REVERSI) model = new ReversiRandomAI(8);
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
                View.getInstance().getController().getServer().getPlayerList();
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

        View.getInstance().getController().getServer().getPlayerList();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fillLeftPane(game);
        fillRightPane(game);

        this.getChildren().addAll(leftPane, rightPane);
    }

    private void fillRightPane(Game game){
        rightPane.getChildren().clear();
        Text acceptChallenges = new Text("Accept Challenges:");
        acceptChallenges.setFill(Color.WHITE);
        rightPane.getChildren().add(acceptChallenges);
        ArrayList<Map> challengeList = View.getInstance().getController().getServer().controllerGetChallengeList();
        for(Map map : challengeList){
            String gameString = (game == Game.REVERSI) ? "Reversi" : "Tic-tac-toe";
            if(map.get("GAMETYPE").equals(gameString)){
                Button button = new Button("Accept challenge from " + map.get("CHALLENGER"));
                button.setOnMouseClicked(e -> acceptChallenge((String)map.get("CHALLENGENUMBER")));
                rightPane.getChildren().add(button);
            }
        }
    }

    private void fillLeftPane(Game game){
        leftPane.getChildren().clear();
        players.getChildren().clear();
        inviteButtons.getChildren().clear();
        playerRow.getChildren().clear();
        Text playersOnline = new Text("Invite Players:");
        playersOnline.setFill(Color.WHITE);
        leftPane.getChildren().add(playersOnline);
        ArrayList<String> namesList = View.getInstance().getController().getServer().controllerGetPlayerList();
        for(String player : namesList){
            if(!player.equals(ownName)){
                Text playerName = new Text(player);
                playerName.setFill(Color.WHITE);
                players.getChildren().add(playerName);
                Button button = new Button("Invite ");
                inviteButtons.getChildren().add(button);
                button.setOnMouseClicked(e -> invitePlayer(player, game));
            }
        }
        playerRow.getChildren().addAll(players, inviteButtons);
        leftPane.getChildren().add(playerRow);
    }

    private void invitePlayer(String player, Game game){
        String gameString = (game == Game.REVERSI) ? "Reversi" : "Tic-tac-toe";
        View.getInstance().getController().getServer().challenge(player, gameString);
    }

    private void acceptChallenge(String number){
        View.getInstance().getController().getServer().acceptChallenge(Integer.parseInt(number));
    }
}
