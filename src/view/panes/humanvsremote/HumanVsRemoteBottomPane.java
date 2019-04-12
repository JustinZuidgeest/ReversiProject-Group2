package view.panes.humanvsremote;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import view.View;
import view.panes.MainMenu;

public class HumanVsRemoteBottomPane extends HBox {
    public HumanVsRemoteBottomPane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        Button leaveButton = new Button("Leave game");
        Button forfeitButton = new Button("Forfeit game");
            leaveButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu();
            View.getInstance().getController().getServer().logout();
            View.getInstance().killController();
            View.getInstance().clearStage();
            View.getInstance().setCenter(mainMenu);
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
}
