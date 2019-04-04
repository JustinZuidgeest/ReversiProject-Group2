package TicTacToe.AI_Models;

import TicTacToe.Symbol;

import java.awt.Point;
import java.util.ArrayList;

public class MinimaxAIModel extends AbstractModel {

    private int initialDepth = 8;
    private int evaluatedPossibilities;
    private Symbol computerPlayer;
    private Symbol opponentPlayer;

    public MinimaxAIModel(int boardSize) {
        super(boardSize);
    }

    @Override
    public Point nextMove(Symbol symbol) {
        evaluatedPossibilities = 0;
        computerPlayer = symbol;
        opponentPlayer = (computerPlayer == Symbol.X) ? Symbol.O : Symbol.X;
        int[] result = miniMax(initialDepth, symbol);
        System.out.println("Minimax AI wants to move to x:" + result[0] + " y: " + result[1]);
        System.out.println("AI evaluated " + evaluatedPossibilities + " possibilities to reach this conclusion");
        return new Point(result[0], result[1]);
    }

    private int[] miniMax(int depth, Symbol player){
        evaluatedPossibilities++;
        // A list of all the possible moves for the current game board
        ArrayList<Point> legalMoves = generateLegalMoves();
        // Variables to store the best move and score of that move
        // The computer is the maximizing player and the human is the minimizing player
        int bestScore = (player == computerPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || getBoardWinner() != null){
            if(player == computerPlayer) bestScore = evaluateScore() - (initialDepth - depth);
            else bestScore = evaluateScore() + (initialDepth - depth);
        }else{
            for(Point legalMove:legalMoves){
                // Try the move
                move(legalMove.x, legalMove.y, player);
                // If player is computer, player is maximizing player
                if(player == computerPlayer){
                    currentScore = miniMax(depth -1, opponentPlayer)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore > bestScore){
                        bestScore = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                // If player is human, player is minimizing player
                }else{
                    currentScore = miniMax(depth -1, computerPlayer)[2];
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

    private int evaluateScore(){
        Symbol winner = getBoardWinner();
        if(winner == computerPlayer) return 10;
        else if(winner == Symbol.EMPTY) return 0;
        else if(winner == opponentPlayer) return  -10;
        else return 0;
    }
}
