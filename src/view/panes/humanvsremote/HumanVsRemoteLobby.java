package view.panes.humanvsremote;

import Games.Controller;
import Games.Controllers.HumanVsRemoteController;
import Games.Model;
import Games.Reversi.ReversiModels.ReversiRandomAI;
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
import view.panes.MainMenu;

import java.util.ArrayList;

public class HumanVsRemoteLobby extends HBox {

    private VBox leftPane;
    private VBox rightPane;

    public HumanVsRemoteLobby(GameType gameType, Game game) {
        //this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        leftPane = new VBox();
        leftPane.setAlignment(Pos.CENTER_LEFT);
        rightPane = new VBox();

        Model model;

        if(game == Game.TICTACTOE) model = new TictactoeRandomAI(3);
        else if(game == Game.REVERSI) model = new ReversiRandomAI(8);
        else throw new IllegalArgumentException();

        Controller controller = new HumanVsRemoteController(model);
        View.getInstance().setController(controller);

        Button backAndLogout = new Button("Back to Main Menu");
        backAndLogout.setOnAction(e ->  {
            MainMenu mainMenu = new MainMenu();
            View.getInstance().killController();
            View.getInstance().clearStage();
            View.getInstance().setCenter(mainMenu);
            controller.getServer().logout();
        });

        View.getInstance().setBottom(backAndLogout);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fillLeftPane(game);

        this.getChildren().addAll(leftPane, rightPane);
    }

    private void fillLeftPane(Game game){
        Label playersOnline = new Label("Players online:\n(Click to invite)\n\n");
        leftPane.getChildren().add(playersOnline);
        ArrayList<String> namesList = View.getInstance().getController().getServer().controllerGetPlayerList();
        System.out.println(namesList);
        for(String player : namesList){
            Label label = new Label(player);
            label.setOnMouseClicked(e -> invitePlayer(player, game));
            leftPane.getChildren().add(label);
        }
    }

    private void invitePlayer(String player, Game game){
        String gameString = (game == Game.REVERSI) ? "Reversi" : "Tic-tac-toe";
        View.getInstance().getController().getServer().challenge(player, gameString);
    }
}
