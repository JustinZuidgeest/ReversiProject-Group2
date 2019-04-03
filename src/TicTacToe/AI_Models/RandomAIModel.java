package TicTacToe.AI_Models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class RandomAIModel extends AbstractModel {

    public RandomAIModel(int boardSize) {
        super(boardSize);
    }

    @Override
    public Point nextMove() {
        Random random = new Random();
        ArrayList<Point> legalMoves = generateLegalMoves();
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
}
