package Games;

import java.awt.*;
import java.util.ArrayList;

public interface Model {
    String getGameName();
    void resetBoard();
    boolean checkLegalMove(int x, int y, Tile player);
    void updateLegalMoves();
    void move(int x, int y, Tile tile);
    Tile checkWin(int x, int y, Tile[][] board);
    void updateScores();
    Point computerMove(Tile tile);
    Tile[][] getBoard();
    boolean hasWinner();
    Tile getBoardWinner();
    int[] getScores();
    ArrayList<Point> getLegalMoves(Tile player);
    Point nextMove(Tile player);
    int getBoardSize();
}
