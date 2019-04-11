package view.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.Main;

public class BoardTile extends Rectangle {

    private static int TILE_SIZE = 70;

    public BoardTile(boolean light, int x, int y) {
        setHeight(TILE_SIZE);
        setWidth(TILE_SIZE);

        relocate(x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("darkgreen") : Color.valueOf("green"));

        this.setOnMouseClicked(e -> Main.getInstance().tileClicked(x, y));
    }
}
