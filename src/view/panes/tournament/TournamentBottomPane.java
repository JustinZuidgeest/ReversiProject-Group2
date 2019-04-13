package view.panes.tournament;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.View;
import view.panes.MainMenu;

public class TournamentBottomPane extends HBox {

    private Button lobbyButton;

    public TournamentBottomPane(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button leaveButton = new Button("Back to Main Menu");
        lobbyButton = new Button("Back to game lobby");
        Button forfeitButton = new Button("Forfeit game");

        leaveButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu();
            View.getInstance().getController().getServer().logout();
            View.getInstance().killController();
            View.getInstance().clearStage();
            View.getInstance().setCenter(mainMenu);
        });

        lobbyButton.setOnAction(e -> {
            View.getInstance().getController().getServer().logout();
            View.getInstance().killController();
            View.getInstance().setTop(null);
            TournamentLobby tournamentLobby = new TournamentLobby(game);
            View.getInstance().setCenter(tournamentLobby);
        });

        forfeitButton.setOnAction(e -> {
            View.getInstance().getController().getServer().forfeit();
            View.getInstance().killController();
            String gameWinner = "The game has ended because you forfeited!";
            String gameScores = "";
            View.getInstance().updateInfoPane(gameWinner, gameScores);
        });

        this.getChildren().addAll(leaveButton, forfeitButton);
    }

    public void addLobbyButton(){
        Platform.runLater(() -> {
            this.getChildren().add(lobbyButton);
        });
    }
}
