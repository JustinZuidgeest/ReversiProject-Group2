import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    public static final int TILE_SIZE = 100;

    public Tile(boolean light, int x, int y){
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);

        relocate(x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("darkgreen") : Color.valueOf("green"));
    }
}
