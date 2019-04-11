import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    public static int TILE_SIZE = 50;

    public Tile(boolean light, int x, int y){
        setHeight(TILE_SIZE);
        setWidth(TILE_SIZE);

        relocate(x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("darkgreen") : Color.valueOf("green"));

    }
}
