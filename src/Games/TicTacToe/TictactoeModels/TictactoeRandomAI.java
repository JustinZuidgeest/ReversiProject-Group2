package Games.TicTacToe.TictactoeModels;

import Games.TicTacToe.AbstractTictactoeModel;
import Games.Tile;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class TictactoeRandomAI extends AbstractTictactoeModel {

    public TictactoeRandomAI(int boardSize) {
        super(boardSize);
    }

    /**
     * Makes a random (legal) move for a given player
     * @param player The player that will make the move
     * @return The move chosen at random for the given player as a Point object
     */
    @Override
    public Point nextMove(Tile player) {
        Random random = new Random();
        updateLegalMoves();
        ArrayList<Point> legalMoves = getLegalMoves(player);
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
}
