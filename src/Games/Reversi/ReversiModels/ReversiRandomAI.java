package Games.Reversi.ReversiModels;

import Games.Reversi.AbstractReversiModel;
import Games.Tile;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ReversiRandomAI extends AbstractReversiModel {

    public ReversiRandomAI(int boardSize) { super(boardSize); }

    /**
     * Calculates the next move for Reversi based on a random choice
     * @param player The player that will make the move
     * @return The move chosen at random
     */
    @Override
    public Point nextMove(Tile player) {
        Random random = new Random();
        ArrayList<Point> legalMoves = getLegalMoves(player);
        Point move = legalMoves.get(random.nextInt(legalMoves.size()));
        System.out.println("AI chose to move to x: " + move.x + ", y: " + move.y);
        return move;
    }
}
