package TicTacToe.AI_Algorithms;

import java.awt.Point;
import java.util.ArrayList;

public interface ComputerAlgorithm {
    Point nextMove(ArrayList<Point> legalMoves);
}
