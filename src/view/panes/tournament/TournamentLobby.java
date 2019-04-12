package view.panes.tournament;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import view.Game;

public class TournamentLobby extends VBox {
    public TournamentLobby(Game game) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
    }
}
