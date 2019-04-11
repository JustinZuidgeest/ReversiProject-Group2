package games.tictactoe.tictactoemodels;

import games.tictactoe.AbstractTictactoeModel;
import games.Tile;

import java.awt.Point;
import java.util.ArrayList;

public class TictactoeMinimaxAlphaBetaAI extends AbstractTictactoeModel {

    private int initialDepth;
    private int evaluatedPossibilities;
    private Tile computerPlayer;
    private Tile opponentPlayer;

    public TictactoeMinimaxAlphaBetaAI(int boardSize, int initialDepth) {
        super(boardSize);
        this.initialDepth = initialDepth;
    }

    @Override
    public Point nextMove(Tile tile) {
        evaluatedPossibilities = 0;
        computerPlayer = tile;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, computerPlayer, Integer.MIN_VALUE,Integer.MAX_VALUE);
        System.out.println("Minimax AI with depth " + initialDepth + " with Alpha-beta pruning wants to move to x:" + result[1] + ", y: " + result[2]);
        System.out.println("AI evaluated " + evaluatedPossibilities + " possibilities to reach this conclusion");
        return new Point(result[1], result[2]);
    }

    private int[] miniMax(int depth, Tile player, int alpha, int beta){
        evaluatedPossibilities++;
        // A list of all the possible moves for the current game board
        updateLegalMoves();
        ArrayList<Point> legalMoves = getLegalMoves(player);
        // Variables to store the best move and score of that move
        // The computer is the maximizing player and the human is the minimizing player
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || hasWinner()){
            if(player == computerPlayer) currentScore = evaluateScore() - (initialDepth - depth);
            else currentScore = evaluateScore() + (initialDepth - depth);
            return new int[]{currentScore, bestCol, bestRow};
        }else{
            for(Point legalMove:legalMoves){
                // Try the move
                move(legalMove.x, legalMove.y, player);
                // If player is computer, player is maximizing player
                if(player == computerPlayer){
                    currentScore = miniMax(depth -1, opponentPlayer, alpha, beta)[0];
                    // If the score of this move is better than the current best available option to the path of the
                    // root node (alpha), replace alpha
                    if(currentScore > alpha){
                        alpha = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                // If player is human, player is minimizing player
                }else{
                    currentScore = miniMax(depth -1, computerPlayer, alpha, beta)[0];
                    // If the score of this move is better than the current best available option to the path of the
                    // root node (beta), replace beta
                    if(currentScore < beta){
                        beta = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                }
                // Undo the move
                move(legalMove.x, legalMove.y, Tile.EMPTY);
                // Cut off the search if a better move for the maximizer has already been found (since it will
                // always choose that one instead)
                if (alpha >= beta) break;
            }
        }
        return new int[]{(player == computerPlayer) ? alpha : beta, bestCol, bestRow};
    }

    private int evaluateScore(){
        Tile winner = getBoardWinner();
        if(winner == computerPlayer) return 10;
        else if(winner == Tile.EMPTY) return 0;
        else if(winner == opponentPlayer) return  -10;
        else return 0;
    }
}
