package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import view.View;

public class ReversiRules extends VBox {
    public ReversiRules() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

        Image image = new Image("assets/reversirules.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(350);

        Button backButton = new BackToMainButton();

        this.getChildren().addAll(imageView, backButton);
    }
}
