import javafx.scene.Group;
import javafx.scene.layout.Pane;

public class reversiBoard {

    public Pane getBoard(){
        Group tileGroup1 = new Group();
        final int TILE_SIZE = 10;
        final int WIDTH = 8;
        final int HEIGHT = 8;
        Pane reversiBoard = new Pane();
        reversiBoard.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        reversiBoard.getChildren().addAll(tileGroup1);

        for (int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                Tile tile = new Tile((x + y) % 2 == 0, x , y);

                tileGroup1.getChildren().add(tile);
            }
        }
        return reversiBoard;
    }
}
