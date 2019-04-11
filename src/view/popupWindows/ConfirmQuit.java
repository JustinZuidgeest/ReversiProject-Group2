package view.popupWindows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class ConfirmQuit {

    static boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(200);
        Label label = new Label();
        label.setText(message);
        label.setPrefWidth(270);
        label.setAlignment(Pos.CENTER);


        Button yesButton = new Button("yes");
        yesButton.getStyleClass().addAll("button-green");
        Button noButton = new Button("no");
        noButton.getStyleClass().addAll("button-green");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        HBox layout = new HBox(10);
        layout.getChildren().addAll(label, yesButton, noButton );
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().addAll("view/style.css");
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }


}


