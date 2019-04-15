package view.panes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TextField timeoutField;
    private Properties properties;
    private Text usernameText;
    private Text hostText;
    private Text portText;
    private Text timeoutText;
    private FileOutputStream os = null;
    private String fileName = "src/Games/Controllers/settings.conf";;

    public LoginPane(GameType gameType, Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Image settingsImage = new Image("assets/settings.png");
        ImageView settingsImageView = new ImageView(settingsImage);

        HBox combiBox = new HBox(50);
        combiBox.setAlignment(Pos.CENTER);
        VBox textBox = new VBox(15);
        VBox fieldBox = new VBox(5);

        usernameText = new Text("Enter your username:");
        hostText = new Text("Enter the host ip:");
        portText = new Text("Enter the host port:");
        timeoutText = new Text("Enter maximum AI timeout (ms):");

        textBox.getChildren().addAll(usernameText, hostText, portText, timeoutText);

        nameField = new TextField();
        hostField = new TextField();
        portField = new TextField();
        timeoutField = new TextField();

        fieldBox.getChildren().addAll(nameField, hostField, portField, timeoutField);

        combiBox.getChildren().addAll(textBox, fieldBox);

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
        String timeout = properties.getProperty("timeout");

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        nameField.setText(name);
        hostField.setText(host);
        portField.setText(port);
        timeoutField.setText(timeout);

        this.getChildren().addAll(settingsImageView, combiBox, loginButton);
    }

    private void loginClicked(GameType gameType, Game game){
        String nameString = nameField.getText();
        String hostString = hostField.getText();
        String portString = portField.getText();
        String timeoutString = timeoutField.getText();

        if (!nameString.isEmpty()) {
            if(!hostString.isEmpty()){
                if(checkInput(portString)){
                    if(checkInput(timeoutString)){
                        try {
                            os = new FileOutputStream(fileName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        properties.setProperty("name", nameString);
                        properties.setProperty("host", hostString);
                        properties.setProperty("port", portString);
                        properties.setProperty("timeout", timeoutString);

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
                            TournamentLobby tournamentLobby = new TournamentLobby(game, Integer.parseInt(timeoutString));
                            View.getInstance().setCenter(tournamentLobby);
                        }
                        else throw new IllegalArgumentException();
                    } else {
                        Platform.runLater(() -> timeoutText.setText("Value has to be a number between 0 and 99999!"));
                    }
                } else {
                    Platform.runLater(() -> portText.setText("Port field has to be a number!"));
                }
            } else {
                Platform.runLater(() -> hostText.setText("Host field can't be empty"));
            }
        } else {
            Platform.runLater(() -> usernameText.setText("Username field can't be empty"));
        }
    }

    private boolean checkInput(String input){
        int intInput;
        try {
            intInput = Integer.parseInt(input);
            return (intInput > 0 && intInput < 99999);
        } catch (NumberFormatException e) {
            System.out.println("Invalid timeout entered");
        }
        return false;
    }
}
