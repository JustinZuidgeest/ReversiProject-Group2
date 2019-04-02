package TicTacToe.AI_Algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class RandomAI implements ComputerAlgorithm {

    @Override
    public Point nextMove(ArrayList<Point> legalMoves) {
        Random random = new Random();
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
}
