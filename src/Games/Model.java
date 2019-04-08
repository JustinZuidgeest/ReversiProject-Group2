package Games;

import java.awt.*;
import java.util.ArrayList;

public interface Model {
    String getGameName();
    void resetBoard();
    boolean checkLegalMove(int x, int y, Tile player);
    void updateLegalMoves(Tile player);
    boolean move(int x, int y, Tile tile);
    Tile checkWin(int x, int y);
    void updateScores();
    Point computerMove(Tile tile);
    Tile[][] getBoard();
    Tile getBoardWinner();
    void setBoardWinner(Tile boardWinner);
    int[] getScores();
    ArrayList<Point> getLegalMoves(Tile player);
    Point nextMove(Tile player);
}
