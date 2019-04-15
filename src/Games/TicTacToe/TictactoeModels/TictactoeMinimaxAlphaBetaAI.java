package Games.TicTacToe.TictactoeModels;

import Games.TicTacToe.AbstractTictactoeModel;
import Games.Tile;

import java.awt.Point;
import java.util.ArrayList;

public class TictactoeMinimaxAlphaBetaAI extends AbstractTictactoeModel {

    private int initialDepth;
    private Tile computerPlayer;
    private Tile opponentPlayer;

    /**
     * Constructor called at object creation. Initializes the AI for a given boardsize and restrictions for maximum
     * depth.
     * @param boardSize The size of the board that will be played on
     * @param initialDepth The depth that minimax will search before retuning a result
     */
    public TictactoeMinimaxAlphaBetaAI(int boardSize, int initialDepth) {
        super(boardSize);
        this.initialDepth = initialDepth;
    }

    /**
     * Calls upon the concrete implementation of the AI algorithm to calculate the next move the computer will make
     * @param player The player that will make the move (black or white)
     * @return A point object representing the coordinate (x and y) of the next move the computer will make
     */
    @Override
    public Point nextMove(Tile player) {
        computerPlayer = player;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, computerPlayer, Integer.MIN_VALUE,Integer.MAX_VALUE);
        System.out.println("Minimax AI with depth " + initialDepth + " with Alpha-beta pruning wants to move to x:" + result[1] + ", y: " + result[2]);
        return new Point(result[1], result[2]);
    }

    /**
     * The heart of the computer AI. Minimax recursive algorithm that explores a game tree, trying every possible move and
     * calculating a score for each move.
     * @param depth The depth at which the minimax algorithm will stop calling searching the game tree
     * @param player The player (black or white) that will make the eventual move
     * @param alpha Alpha score at the start of the current recursion
     * @param beta Beta score at the start of the current recursion
     * @return An integer array with the score for the current move, the row (y) and column (x) for the current move
     */
    private int[] miniMax(int depth, Tile player, int alpha, int beta){
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

    /**
     * Calculates the score of the current game state
     * @return The score of the current game state as an int
     */
    private int evaluateScore(){
        Tile winner = getBoardWinner();
        if(winner == computerPlayer) return 10;
        else if(winner == Tile.EMPTY) return 0;
        else if(winner == opponentPlayer) return  -10;
        else return 0;
    }
}
