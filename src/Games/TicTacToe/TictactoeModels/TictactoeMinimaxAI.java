package Games.TicTacToe.TictactoeModels;

import Games.Tile;

import java.awt.Point;
import java.util.ArrayList;

public class TictactoeMinimaxAI extends AbstractTictactoeModel {

    private int initialDepth = 8;
    private int evaluatedPossibilities;
    private Tile computerPlayer;
    private Tile opponentPlayer;

    public TictactoeMinimaxAI(int boardSize) {
        super(boardSize);
    }

    @Override
    public Point nextMove(Tile tile) {
        evaluatedPossibilities = 0;
        computerPlayer = tile;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, tile);
        System.out.println("Minimax AI with depth " + initialDepth + " wants to move to x:" + result[0] + " y: " + result[1]);
        System.out.println("AI evaluated " + evaluatedPossibilities + " possibilities to reach this conclusion");
        return new Point(result[0], result[1]);
    }

    private int[] miniMax(int depth, Tile player){
        evaluatedPossibilities++;
        // A list of all the possible moves for the current game board
        updateLegalMoves();
        ArrayList<Point> legalMoves = getLegalMoves(player);
        // Variables to store the best move and score of that move
        // The computer is the maximizing player and the human is the minimizing player
        int bestScore = (player == computerPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || hasWinner()){
            if(player == computerPlayer) bestScore = evaluateScore() - (initialDepth - depth);
            else bestScore = evaluateScore() + (initialDepth - depth);
        }else{
            for(Point legalMove:legalMoves){
                // Try the move
                move(legalMove.x, legalMove.y, player);
                checkWin(legalMove.x, legalMove.y, getBoard());
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
                move(legalMove.x, legalMove.y, Tile.EMPTY);
            }
        }
        return new int[]{bestCol, bestRow, bestScore};
    }

    private int evaluateScore(){
        Tile winner = getBoardWinner();
        if(winner == computerPlayer) return 10;
        else if(winner == Tile.EMPTY) return 0;
        else if(winner == opponentPlayer) return  -10;
        else return 0;
    }
}
