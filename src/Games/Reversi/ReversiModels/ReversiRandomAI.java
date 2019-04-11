package games.reversi.reversimodels;

import games.reversi.AbstractReversiModel;
import games.Tile;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ReversiRandomAI extends AbstractReversiModel {

    public ReversiRandomAI(int boardSize) { super(boardSize); }

    @Override
    public Point nextMove(Tile player) {
        Random random = new Random();
        ArrayList<Point> legalMoves = getLegalMoves(player);
        Point move = legalMoves.get(random.nextInt(legalMoves.size()));
        System.out.println("AI chose to move to x: " + move.x + ", y: " + move.y);
        return move;
    }
}
