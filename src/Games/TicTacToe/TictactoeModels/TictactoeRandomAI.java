package games.tictactoe.tictactoemodels;

import games.tictactoe.AbstractTictactoeModel;
import games.Tile;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class TictactoeRandomAI extends AbstractTictactoeModel {

    public TictactoeRandomAI(int boardSize) {
        super(boardSize);
    }

    @Override
    public Point nextMove(Tile player) {
        Random random = new Random();
        updateLegalMoves();
        ArrayList<Point> legalMoves = getLegalMoves(player);
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
}
