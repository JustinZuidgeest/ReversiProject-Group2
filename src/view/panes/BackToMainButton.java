package view.panes;

import javafx.scene.control.Button;
import view.View;

public class BackToMainButton extends Button {
    public BackToMainButton() {
        super();
        this.setText("Back to Main Menu");
        this.setOnAction(e -> backToMain());
    }

    private void backToMain(){
        MainMenu mainMenu = new MainMenu();
        View.getInstance().setCenter(mainMenu);
    }
}
