package Games.Reversi.ReversiModels;

import Games.Reversi.AbstractReversiModel;
import Games.Tile;

import java.awt.*;
import java.util.ArrayList;

public class ReversiMiniMaxAlphaBetaAI extends AbstractReversiModel {

    private int initialDepth;
    private Tile computerPlayer;
    private Tile opponentPlayer;
    private long maxTime;
    private Long startTime;

    //The weight model used by the AI to calculate the value of each tile and an eventual score based on these weights
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

    /**
     * Constructor called at object creation. Initializes the AI for a given boardsize and restrictions for maximum
     * depth and calculation time.
     * @param boardSize The size of the board that will be played on
     * @param maxDepth The max depth the AI is allowed to search in the tree of possible moves
     * @param maxTime The max time the AI is allowed to take to search the move tree
     */
    public ReversiMiniMaxAlphaBetaAI(int boardSize, int maxDepth, int maxTime) {
        super(boardSize);
        this.initialDepth = maxDepth;
        this.maxTime = maxTime;
    }

    /**
     * Calls upon the concrete implementation of the AI algorithm to calculate the next move the computer will make
     * @param player The player that will make the move (black or white)
     * @return A point object representing the coordinate (x and y) of the next move the computer will make
     */
    @Override
    public Point nextMove(Tile player) {
        startTime = System.currentTimeMillis();
        computerPlayer = player;
        opponentPlayer = (computerPlayer == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        int[] result = miniMax(initialDepth, computerPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, getBoard());
        System.out.println("Minimax AI with Alpha-Beta pruning with depth " + initialDepth + " wants to move to x:" + result[1] + " y: " + result[2]);
        return new Point(result[1], result[2]);
    }

    /**
     * The heart of the computer AI. Minimax recursive algorithm that explores a game tree, trying every possible move and
     * calculating a score for each move.
     * @param depth The depth at which the minimax algorithm will stop calling searching the game tree
     * @param player The player (black or white) that will make the eventual move
     * @param alpha Alpha score at the start of the current recursion
     * @param beta Beta score at the start of the current recursion
     * @param board Playing board represented by a 2D array of Tile objects
     * @return An integer array with the score for the current move, the row (y) and column (x) for the current move
     */
    private int[] miniMax(int depth, Tile player, int alpha, int beta, Tile[][] board){
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

    /**
     * Calculates a score based on a state of the playing board
     * @param board The state of the board represented by a 2D array of Tiles
     * @return An int for the score that this board gives the player
     */
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

    /**
     * Helper function to check if a winner has occurred by checking if both players are out of moves
     * @param player1Moves List of possible moves for player 1
     * @param player2Moves List of possible moves for player 2
     * @return True if a winner has occurred, false if no winner has occurred
     */
    private boolean evluateWin(ArrayList<Point> player1Moves, ArrayList<Point> player2Moves){
        return (player1Moves.size() == 0 && player2Moves.size() == 0);
    }

    /**
     * Makes a deep copy of a playing board
     * @param board The board that should be copied
     * @return A copy of the board passed into the function
     */
    private Tile[][] copyBoard(Tile[][] board){
        Tile[][] newBoard = new Tile[board.length][];
        for(int i=0;i < board.length;i++){
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }
}
