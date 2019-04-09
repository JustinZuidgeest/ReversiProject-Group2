package Games.Reversi.ReversiModels;

import Games.Tile;

import java.awt.*;
import java.util.ArrayList;

public class ReversiMiniMaxAlphaBetaAI extends AbstractReversiModel{

    private int initialDepth = 5;
    private int evaluatedPossibilities;
    private Tile computerPlayer;
    private Tile opponentPlayer;

    public ReversiMiniMaxAlphaBetaAI(int boardSize) { super(boardSize); }

    @Override
    public Point nextMove(Tile player) {
        evaluatedPossibilities = 0;
        computerPlayer = player;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, computerPlayer, Integer.MIN_VALUE,Integer.MAX_VALUE, getBoard());
        System.out.println("Minimax AI with Alpha-Beta pruning with depth " + initialDepth + " wants to move to x:" + result[1] + " y: " + result[2]);
        System.out.println("The score for this move: " + result[0]);
        System.out.println("AI evaluated " + evaluatedPossibilities + " possibilities to reach this conclusion");
        return new Point(result[1], result[2]);
    }

    private int[] miniMax(int depth, Tile player, int alpha, int beta, Tile[][] board){
        evaluatedPossibilities++;
        // Keep track of who the opponent is for this recursion
        Tile opponent = (player == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        //A copy of the board the way it was passed to the function
        Tile[][] boardCopy = copyBoard(board);
        // A list of all the possible moves this player can do for the current game board
        ArrayList<Point> legalMovesThisPlayer = generateLegalMoves(player, boardCopy);
        // A list of all the possible moves the opponent can make for the current game board
        ArrayList<Point> legalMovesOpponent = generateLegalMoves(opponent, boardCopy);

        // Variables to store the best move and score of that move
        // The computer is the maximizing player and the human is the minimizing player
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || evluateWin(legalMovesThisPlayer, legalMovesOpponent)){
            currentScore = evaluateScore(boardCopy, player);
            //if(depth == 0){System.out.println("Minimax reached end because depth 0 was reached. Score was " + currentScore);}
            //else if(evluateWin(legalMovesThisPlayer, legalMovesOpponent)) System.out.println("Minimax reached end because there was a winner. Score was " + currentScore);
            return new int[]{currentScore, bestCol, bestRow};
        }
        // If no moves for this player remain in this tree (but the game has not ended yet)
        else if(legalMovesThisPlayer.size() == 0){
            currentScore = (player == computerPlayer) ? 0 : 0;
            //System.out.println("Minimax reached end because it ran out of moves. Score was: " + currentScore);
            return new int[]{currentScore, bestCol, bestRow};
        }else{
            for(Point legalMove:legalMovesThisPlayer){
                // Try the move
                boardCopy[legalMove.y][legalMove.x] = player;
                flipTiles(legalMove.x, legalMove.y, player, boardCopy);
                // If player is computer, player is maximizing player
                if(player == computerPlayer){
                    currentScore = miniMax(depth -1, opponentPlayer, alpha, beta, boardCopy)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore > alpha){
                        alpha = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                    // If player is human, player is minimizing player
                }else{
                    currentScore = miniMax(depth -1, computerPlayer, alpha, beta, boardCopy)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore < beta){
                        beta = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                }
                // Cut off the search if a better move for the maximizer has already been found (since it will
                // always choose that one instead)
                if (alpha >= beta) break;
            }
        }
        return new int[]{(player == computerPlayer) ? alpha : beta, bestCol, bestRow};
    }

    private int evaluateScore(Tile[][] board, Tile player){
        return 0;
    }

    private boolean evluateWin(ArrayList<Point> player1Moves, ArrayList<Point> player2Moves){
        return (player1Moves.size() == 0 && player2Moves.size() == 0);
    }

    private Tile[][] copyBoard(Tile[][] board){
        Tile[][] newBoard = new Tile[board.length][];
        for(int i=0;i < board.length;i++){
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }
}
