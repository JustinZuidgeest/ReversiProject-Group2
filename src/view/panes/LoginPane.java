package view.panes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import view.Game;
import view.GameType;
import view.View;
import view.panes.humanvsremote.HumanVsRemoteLobby;
import view.panes.tournament.TournamentLobby;

import java.io.*;
import java.util.Properties;

public class LoginPane extends HBox {

    private TextField textField;
    private Properties properties;
    private Text usernameText;
    private FileOutputStream os = null;
    String fileName = "src/Games/Controllers/settings.conf";
    private String host;
    private String port;
    private String name;

    public LoginPane(GameType gameType, Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        usernameText = new Text("Enter your username:");
        textField = new TextField();
        Button setName = new Button("Login");

        setName.setOnAction(e -> loginClicked(gameType, game));

        properties = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        host = properties.getProperty("host");
        port = properties.getProperty("port");
        name = properties.getProperty("name");

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textField.setText(name);

        this.getChildren().addAll(usernameText, textField, setName);
    }

    private void loginClicked(GameType gameType, Game game){
        String fieldString = textField.getText();
        if (!fieldString.isEmpty()) {
            try {
                os = new FileOutputStream(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Setting name to " + fieldString);
            properties.setProperty("name", fieldString);
            properties.setProperty("host", host);
            properties.setProperty("port", port);

            try {
                properties.store(os, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(gameType == GameType.REMOTE){
                HumanVsRemoteLobby humanVsRemoteLobby = new HumanVsRemoteLobby(game, name);
                View.getInstance().setCenter(humanVsRemoteLobby);
            }else if(gameType == GameType.TOURNAMENT){
                TournamentLobby tournamentLobby = new TournamentLobby(game);
                View.getInstance().setCenter(tournamentLobby);
            }
            else throw new IllegalArgumentException();

        } else {
            Platform.runLater(() -> usernameText.setText("Username can't be empty"));
        }
    }
}
