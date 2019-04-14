package view.panes.humanvsai;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.Game;
import view.View;
import view.panes.BackToMainButton;

import java.io.*;
import java.util.Properties;

public class DifficultyChoice extends VBox {

    private String fileName = "src/Games/Controllers/settings.conf";
    private Text timeoutText;
    private Properties properties;
    private String timeout;
    private TextField timeoutField;

    public DifficultyChoice(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Button easyButton = new Button("Play Easy Difficulty");
        easyButton.setPrefSize(200, 30);
        Button mediumButton = new Button("Play Medium Difficulty");
        mediumButton.setPrefSize(200, 30);
        Button hardButton = new Button("Play Hard Difficulty");
        hardButton.setPrefSize(200, 30);

        timeoutText = new Text("AI timeout (ms):");
        timeoutField = new TextField();
        HBox timeoutBox = new HBox();

        timeoutBox.setSpacing(30);
        timeoutBox.setAlignment(Pos.CENTER);
        timeoutBox.getChildren().addAll(timeoutText, timeoutField);

        Button backButton = new BackToMainButton();
        backButton.setPrefSize(200, 30);

        easyButton.setOnAction(e -> initColorChoice(game, 1));
        mediumButton.setOnAction(e -> initColorChoice(game, 3));
        hardButton.setOnAction(e -> initColorChoice(game, 8));

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

        timeout = properties.getProperty("timeout");
        timeoutField.setText(timeout);

        this.getChildren().addAll(timeoutBox, easyButton, mediumButton, hardButton ,backButton);
    }

    private void initColorChoice(Game game, int difficulty) {
        timeout = timeoutField.getText();
        if(checkInput(timeout)){
            OutputStream os = null;
            try {
                os = new FileOutputStream(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

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
            HumanVsAiColorChoice humanVsAiColorChoice = new HumanVsAiColorChoice(game, difficulty, Integer.parseInt(timeout));
            View.getInstance().setCenter(humanVsAiColorChoice);
        }
    }

    private boolean checkInput(String input){
        int intInput;
        try {
            intInput = Integer.parseInt(input);
            if(intInput > 0 && intInput < 99999){
                return true;
            }else{
                Platform.runLater(() -> timeoutText.setText("Value has to be a number between 0 and 99999!"));
            }
        } catch (NumberFormatException e) {
            Platform.runLater(() -> timeoutText.setText("Value has to be a number between 0 and 99999!"));
            System.out.println("Timeout is not a valid number");
        }
        return false;
    }
}
