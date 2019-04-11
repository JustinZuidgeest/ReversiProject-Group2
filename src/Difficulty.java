import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Difficulty {

    static int resultaat;

    public static int difPicker(String title, String message) {
        Stage difWindow = new Stage();
        difWindow.initModality(Modality.APPLICATION_MODAL);
        difWindow.setTitle(title);
        difWindow.setMinWidth(300);
        difWindow.setMinHeight(300);
        Label label = new Label();
        label.setText(message);
        label.setPrefWidth(270);
        label.setAlignment(Pos.CENTER);

        Button easy = new Button("easy");
        easy.getStyleClass().addAll("button-green");
        Button medium = new Button("medium");
        medium.getStyleClass().addAll("button-green");
        Button hard = new Button("hard");
        hard.getStyleClass().addAll("button-green");

        easy.setOnAction(event -> {
            resultaat = 1;
            difWindow.close();
        });
        medium.setOnAction(event -> {
            resultaat = 2;
            difWindow.close();
        });
        hard.setOnAction(event -> {
            resultaat = 3;
            difWindow.close();
        });

        VBox difBox = new VBox(5);
        difBox.getChildren().addAll(label, easy, medium, hard);
        difBox.setAlignment(Pos.CENTER);

        Scene difScene = new Scene(difBox);
        difScene.getStylesheets().addAll("style.css");
        difWindow.setScene(difScene);
        difWindow.showAndWait();

        return resultaat;
    }

    public void startGame() {
        int result = Difficulty.difPicker("Difficulty", "Select difficulty");

        if (result == 1) {
        }
        if (result == 2) {
        }
        if (result == 3) {
        }
    }



}
