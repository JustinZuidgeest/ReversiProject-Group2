package view.panes.tournament;

import Games.Controller;
import Games.Controllers.AiVsRemoteController;
import Games.Model;
import Games.Reversi.ReversiModels.ReversiMiniMaxAlphaBetaAI;
import Games.TicTacToe.TictactoeModels.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.Game;
import view.View;
import view.panes.MainMenu;

import java.util.ArrayList;

public class TournamentLobby extends VBox {
    private Controller controller;
    private Text playerListText;
    private TextArea playerList;
    private int playerCount;

    public TournamentLobby(Game game, int timeout) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(50));

        Image tournamentLobbyImage = new Image("assets/tournament.png");
        ImageView tournamentLobbyImageView = new ImageView(tournamentLobbyImage);

        Model model;

        if(game == Game.TICTACTOE) {
            model = new TictactoeMinimaxAlphaBetaAI(3, 10);
            System.out.println("Tictactoe");
        }
        else if(game == Game.REVERSI) model = new ReversiMiniMaxAlphaBetaAI(8, 7, timeout);
        else throw new IllegalArgumentException();

        controller = new AiVsRemoteController(model);
        View.getInstance().setController(controller);

        playerListText = new Text();
        playerListText.setFill(Color.WHITE);
        playerList = new TextArea();
        playerList.setWrapText(true);
        playerList.setEditable(false);
        playerList.setPrefRowCount(30);

        Button backAndLogout = new Button("Back to Main Menu");
        Button refreshButton = new Button("Refresh");
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        hBox.getChildren().addAll(backAndLogout, refreshButton);

        refreshButton.setOnAction(e -> {
            Platform.runLater(() ->{
                View.getInstance().getController().getServer().getPlayerList();
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

        View.getInstance().getController().getServer().getPlayerList();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fillPlayerList(game);

        this.getChildren().addAll(tournamentLobbyImageView, playerListText, playerList);
    }

    private void fillPlayerList(Game game){
        StringBuilder stringBuilder = new StringBuilder();
        playerCount = 0;
        ArrayList<String> namesList = View.getInstance().getController().getServer().controllerGetPlayerList();
        for(String player : namesList){
            stringBuilder.append(player);
            stringBuilder.append("\n");
            playerCount++;
        }
        Platform.runLater(() -> {
            playerListText.setText("There are " + playerCount +" players connected to the lobby:");
            playerList.setText(stringBuilder.toString());
        });
    }
}
