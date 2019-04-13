package view.panes.humanvsremote;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.Game;
import view.View;
import view.panes.MainMenu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HumanVsRemoteBottomPane extends HBox {

    private Button lobbyButton;

    public HumanVsRemoteBottomPane(Game game) {
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
            Properties properties = new Properties();
            InputStream is = null;
            try {
                is = new FileInputStream("src/Games/Controllers/settings.conf");

            } catch (FileNotFoundException err) {
                err.printStackTrace();
            }
            try {
                properties.load(is);
            } catch (IOException err) {
                err.printStackTrace();
            }
            String name = properties.getProperty("name");
            View.getInstance().getController().getServer().logout();
            View.getInstance().setTop(null);
            HumanVsRemoteLobby humanVsRemoteLobby = new HumanVsRemoteLobby(game, name);
            View.getInstance().setCenter(humanVsRemoteLobby);
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
