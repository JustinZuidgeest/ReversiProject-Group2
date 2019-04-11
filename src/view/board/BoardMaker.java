package view.board;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class BoardMaker {

    public Pane getBoard(Tile[][] board){
        Group tileGroup1 = new Group();
        final int TILE_SIZE = 10;
        final int WIDTH = board.length;
        final int HEIGHT = board.length;
        Pane standardBoard = new Pane();
        standardBoard.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        standardBoard.getChildren().addAll(tileGroup1);

        for (int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                BoardTile boardTile = new BoardTile((x + y) % 2 == 0, x , y);
                Ellipse ellipse = new Ellipse(20, 20);
                ellipse.relocate((double)((x * 70)+15), (double)((y * 70)+15));
                Color color = (board[y][x] == Tile.BLACK) ? Color.BLACK : Color.WHITE;
                ellipse.setFill(color);
                tileGroup1.getChildren().add(boardTile);
                if(board[y][x] != Tile.EMPTY) tileGroup1.getChildren().add(ellipse);
            }
        }
        return standardBoard;
    }
}
