package Games.Reversi.ReversiModels;

import Games.Tile;

import java.awt.*;
import java.util.ArrayList;

public class ReversiMinimaxAI extends AbstractReversiModel {

    private int initialDepth = 8;
    private int evaluatedPossibilities;
    private Tile computerPlayer;
    private Tile opponentPlayer;

    public ReversiMinimaxAI(int boardSize) { super(boardSize); }

    @Override
    public Point nextMove(Tile player) {
        evaluatedPossibilities = 0;
        computerPlayer = player;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, player, getBoard());
        System.out.println("Minimax AI with depth " + initialDepth + " wants to move to x:" + result[0] + " y: " + result[1]);
        System.out.println("AI evaluated " + evaluatedPossibilities + " possibilities to reach this conclusion");
        return new Point(result[0], result[1]);
    }

    private int[] miniMax(int depth, Tile player, Tile[][] board){
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
        int bestScore = (player == computerPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || evluateWin(legalMovesThisPlayer, legalMovesOpponent)){
            //if(depth == 0){System.out.println("Minimax reached end because depth 0 was reached");}
            //else if(evluateWin(legalMovesThisPlayer, legalMovesOpponent)) System.out.println("Minimax reached end because there was a winner");
            bestScore = evaluateScore(boardCopy);
        }
        // If no moves for this player remain in this tree (but the game has not ended yet)
        else if(legalMovesThisPlayer.size() == 0){
            //System.out.println("Minimax reached end because it ran out of moves");
            bestScore = (player == computerPlayer) ? -64 : 64;
        }else{
            for(Point legalMove:legalMovesThisPlayer){
                // Try the move
                boardCopy[legalMove.y][legalMove.x] = player;
                flipTiles(legalMove.x, legalMove.y, player, boardCopy);
                // If player is computer, player is maximizing player
                if(player == computerPlayer){
                    currentScore = miniMax(depth -1, opponentPlayer, boardCopy)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore > bestScore){
                        bestScore = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                    // If player is human, player is minimizing player
                }else{
                    currentScore = miniMax(depth -1, computerPlayer, boardCopy)[2];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore < bestScore){
                        bestScore = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                }
            }
        }
        return new int[]{bestCol, bestRow, bestScore};
    }

    private int evaluateScore(Tile[][] board){
        int whiteScore = 0;
        int blackScore = 0;
        for(int i=0;i<getBoardSize();i++){
            for(int j=0;j<getBoardSize();j++){
                if(board[i][j] == Tile.WHITE){
                    whiteScore++;
                }else if(board[i][j] == Tile.BLACK){
                    blackScore++;
                }
            }
        }
        Tile winner = (whiteScore > blackScore) ? Tile.WHITE : (whiteScore == blackScore) ? Tile.EMPTY : Tile.BLACK;
        if(winner == computerPlayer) return 100;
        else if(winner == Tile.EMPTY) return 0;
        else if(winner == opponentPlayer) return  -100;
        else return 0;
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
