package view.panes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.Game;
import view.GameType;
import view.View;
import view.panes.humanvsremote.HumanVsRemoteLobby;
import view.panes.tournament.TournamentLobby;

import java.io.*;
import java.util.Properties;

public class LoginPane extends VBox {

    private TextField nameField;
    private TextField hostField;
    private TextField portField;
    private Properties properties;
    private Text usernameText;
    private Text hostText;
    private Text portText;
    private FileOutputStream os = null;
    private String fileName = "src/Games/Controllers/settings.conf";
    private String timeout;

    public LoginPane(GameType gameType, Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        HBox usernameBox = new HBox();
        usernameBox.setAlignment(Pos.CENTER);
        usernameBox.setSpacing(30);
        HBox hostBox = new HBox();
        hostBox.setAlignment(Pos.CENTER);
        hostBox.setSpacing(30);
        HBox portBox = new HBox();
        portBox.setAlignment(Pos.CENTER);
        portBox.setSpacing(30);

        usernameText = new Text("Enter your username:");
        hostText = new Text("Enter the host ip:");
        portText = new Text("Enter the host port:");

        nameField = new TextField();
        hostField = new TextField();
        portField = new TextField();

        usernameBox.getChildren().addAll(usernameText, nameField);
        hostBox.getChildren().addAll(hostText, hostField);
        portBox.getChildren().addAll(portText, portField);

        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> loginClicked(gameType, game));

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

        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String name = properties.getProperty("name");
        timeout = properties.getProperty("timeout");

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        nameField.setText(name);
        hostField.setText(host);
        portField.setText(port);

        this.getChildren().addAll(usernameBox, hostBox, portBox, loginButton);
    }

    private void loginClicked(GameType gameType, Game game){
        String nameString = nameField.getText();
        String hostString = hostField.getText();
        String portString = portField.getText();

        if (!nameString.isEmpty()) {
            if(!hostString.isEmpty()){
                if(!portString.isEmpty()){
                    try {
                        os = new FileOutputStream(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    properties.setProperty("name", nameString);
                    properties.setProperty("host", hostString);
                    properties.setProperty("port", portString);
                    properties.setProperty("timeout", timeout);

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
                        HumanVsRemoteLobby humanVsRemoteLobby = new HumanVsRemoteLobby(game, nameString);
                        View.getInstance().setCenter(humanVsRemoteLobby);
                    }else if(gameType == GameType.TOURNAMENT){
                        TournamentLobby tournamentLobby = new TournamentLobby(game);
                        View.getInstance().setCenter(tournamentLobby);
                    }
                    else throw new IllegalArgumentException();
                } else {
                    Platform.runLater(() -> portText.setText("Port field can't be empty"));
                }
            } else {
                Platform.runLater(() -> hostText.setText("Host field can't be empty"));
            }
        } else {
            Platform.runLater(() -> usernameText.setText("Username field can't be empty"));
        }
    }
}
