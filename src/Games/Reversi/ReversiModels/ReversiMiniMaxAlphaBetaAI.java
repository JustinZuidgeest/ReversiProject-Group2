package Games.Reversi.ReversiModels;

import Games.Reversi.AbstractReversiModel;
import Games.Tile;

import java.awt.*;
import java.util.ArrayList;

public class ReversiMiniMaxAlphaBetaAI extends AbstractReversiModel {

    private int initialDepth;
    private int evaluatedPossibilities;
    private Tile computerPlayer;
    private Tile opponentPlayer;
    private long maxTime;
    private Long startTime;

    final int[][] scoreRubric = new int[][]{
            {100, -25, 10,  5,  5, 10, -25, 100},
            {-25, -50, -4, -5, -5, -4, -50, -25},
            {8,    -4,  2,  1,  1,  2,  -4,   8},
            {6,    -5,  1,  0,  0,  1,  -5,   6},
            {6,    -5,  1,  0,  0,  1,  -5,   6},
            {8,    -4,  2,  1,  1,  2,  -4,   8},
            {-25, -50, -4, -5, -5, -4, -50, -25},
            {100, -25, 10,  5,  5, 10, -25, 100},
    };

    public ReversiMiniMaxAlphaBetaAI(int boardSize, int initialDepth, int maxTime) {
        super(boardSize);
        this.initialDepth = initialDepth;
        this.maxTime = maxTime;
    }

    @Override
    public Point nextMove(Tile player) {
        startTime = System.currentTimeMillis();
        evaluatedPossibilities = 0;
        computerPlayer = player;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, computerPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, getBoard());
        System.out.println("Minimax AI with Alpha-Beta pruning with depth " + initialDepth + " wants to move to x:" + result[1] + " y: " + result[2]);
        //System.out.println("The score for this move: " + result[0]);
        //System.out.println("AI evaluated " + evaluatedPossibilities + " possibilities to reach this conclusion");
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

        // Variables to store the best available move and score of that move
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        //Cutoff failsafe mechanism to end minimax as quickly as possible when nearing the max time limit
        if((System.currentTimeMillis() - startTime) >= maxTime){
            currentScore = evaluateScore(boardCopy);
            return new int[]{currentScore, bestCol, bestRow};
        }

        // Base case for when the end of the decision tree has been reached (if max depth is reached or a game ending move was made)
        if(depth == 0 || evluateWin(legalMovesThisPlayer, legalMovesOpponent)){
            currentScore = evaluateScore(boardCopy);
            return new int[]{currentScore, bestCol, bestRow};
        }
        // If the current player has ran itself out of moves (but the game has not ended yet)
        else if(legalMovesThisPlayer.size() == 0){
            currentScore = (player == computerPlayer) ? -500 : 500;
            return new int[]{currentScore, bestCol, bestRow};
        }else{
            for(Point legalMove:legalMovesThisPlayer){
                //A copy of the board the way it was passed to the function
                boardCopy = copyBoard(board);
                // Try the move
                boardCopy[legalMove.y][legalMove.x] = player;
                flipTiles(legalMove.x, legalMove.y, player, boardCopy);
                // If player is computer, player is maximizing player
                if(player == computerPlayer){
                    currentScore = miniMax(depth -1, opponentPlayer, alpha, beta, boardCopy)[0];
                    // If the score of this move was better than current best, replace current best
                    if(currentScore > alpha){
                        alpha = currentScore;
                        bestRow = legalMove.y;
                        bestCol = legalMove.x;
                    }
                    // If player is human, player is minimizing player
                }else{
                    currentScore = miniMax(depth -1, computerPlayer, alpha, beta, boardCopy)[0];
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
        return new int[]{((player == computerPlayer) ? alpha : beta), bestCol, bestRow};
    }

    private int evaluateScore(Tile[][] board){
        int blackScore = 0;
        int whiteScore = 0;
        for(int i=0;i<getBoardSize();i++){
            for(int j=0;j<getBoardSize();j++){
                if(board[i][j] == Tile.BLACK) blackScore += scoreRubric[i][j];
                else if(board[i][j] == Tile.WHITE) whiteScore += scoreRubric[i][j];
            }
        }
        return (computerPlayer == Tile.BLACK) ? (blackScore - whiteScore) : (whiteScore - blackScore);
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
