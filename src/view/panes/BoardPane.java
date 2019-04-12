package view.panes;

import Games.Tile;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import view.View;

public class BoardPane extends Pane{

    private int boardSize;

    public BoardPane(int boardSize) {
        this.boardSize = boardSize;
        for (int i=0; i<boardSize; i++){
            for (int j=0; j<boardSize; j++){
                Rectangle box = new Rectangle();
                box.widthProperty().bind(this.widthProperty().divide(boardSize));
                box.heightProperty().bind(this.heightProperty().divide(boardSize));
                box.xProperty().bind(this.widthProperty().divide(boardSize).multiply(j));
                box.yProperty().bind(this.heightProperty().divide(boardSize).multiply(i));
                box.setOnMouseClicked(e -> clickedOnTile(e.getX(), e.getY()));
                if((i%2==0 && j%2==1) || (i%2==1 && j%2==0)){
                    box.setFill(Color.LIGHTGRAY);
                }else{
                    box.setFill(Color.DARKGRAY);
                }
                this.getChildren().add(box);
            }
        }
    }

    private void clickedOnTile(double x, double y){
        double boxWidth = this.getWidth()/boardSize;
        double boxHeight = this.getHeight()/boardSize;
        int tileX = Math.floorDiv((int)x, (int)boxWidth);
        int tileY = Math.floorDiv((int)y, (int)boxHeight);
        View.getInstance().moveMade(tileX, tileY);
    }

    public void updateBoard(Tile[][] board) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(board[i][j] != Tile.EMPTY){
                    Circle circle = new Circle();
                    circle.radiusProperty().bind(this.widthProperty().divide(boardSize*5));
                    circle.centerXProperty().bind(this.widthProperty().divide(boardSize).multiply(j + 0.5));
                    circle.centerYProperty().bind(this.heightProperty().divide(boardSize).multiply(i + 0.5));
                    Color circleColor = (board[i][j] == Tile.BLACK) ? Color.BLACK : Color.WHITE;
                    circle.setFill(circleColor);
                    this.getChildren().add(circle);
                }
            }
        }
    }
}
