package TicTacToe.AI_Models;

import TicTacToe.Symbol;

import java.awt.Point;
import java.util.ArrayList;

public class MinimaxAIModel extends AbstractModel {

    public MinimaxAIModel(int boardSize) {
        super(boardSize);
    }

    @Override
    public Point nextMove() {
        int[] result = miniMax(8, Symbol.O);
        System.out.println("AI wants to move to x:" + result[0] + " y: " + result[1]);
        return new Point(result[0], result[1]);
    }

    private int[] miniMax(int depth, Symbol player){
        // A list of all the possible moves for the current game board
        ArrayList<Point> legalMoves = generateLegalMoves();
        // Variables to store the best move and score of that move, initialized to their starting values
        int bestScore = (player == Symbol.O) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || getBoardWinner() != null){
            bestScore = evaluateScore(player);
        }else{
            for(Point legalMove:legalMoves){
                // Try the move
                move(legalMove.x, legalMove.y, player);
                // If player is computer, player is maximizing player
                if(player == Symbol.O){
                    currentScore = miniMax(depth -1, Symbol.X)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore > bestScore){
                        bestScore = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                // If player is human, player is minimizing player
                }else{
                    currentScore = miniMax(depth -1, Symbol.O)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore < bestScore){
                        bestScore = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                }
                // Undo the move
                move(legalMove.x, legalMove.y, Symbol.EMPTY);
            }
        }
        return new int[]{bestCol, bestRow, bestScore};
    }

    private int evaluateScore(Symbol player){
        Symbol winner = getBoardWinner();
        if(winner == Symbol.O) return 10;
        else if(winner == Symbol.EMPTY) return 0;
        else if(winner == Symbol.X) return  -10;
        else return 0;
    }

    public void printBoard(Symbol[][] board){
        System.out.println("Current board:");
        for(Symbol[] row:board){
            for(Symbol column:row){
                String current = (column == Symbol.X) ? "X" : (column == Symbol.O) ? "O" : ".";
                System.out.print(current + "  ");
            }
            System.out.println();
        }
    }
}
