package Games.TicTacToe;

import Games.Model;
import Games.Tile;
import java.awt.Point;
import java.util.ArrayList;

public abstract class AbstractTictactoeModel implements Model {

    private Tile[][] board;
    private int boardSize;
    private Tile boardWinner = null;
    private int[] scores;
    private ArrayList<Point> legalMoves;

    /**
     * Constructor called at object creation. Initializes the variables that represent the board, the lists of legal moves.
     * @param boardSize The size of a board size (a board is always square, so size dictates both width and height)
     */
    public AbstractTictactoeModel(int boardSize) {
        this.boardSize = boardSize;
        board = new Tile[boardSize][boardSize];
        legalMoves = new ArrayList<>();
        scores = new int[]{0, 0};
    }

    /**
     * Fill the board with Tile.EMPTY (no player has moved here yet) objects to create an empty board
     * Sets the winner of the current board to null (there is no winner yet)
     */
    @Override
    public void resetBoard(){
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                board[i][j] = Tile.EMPTY;
            }
        }
        boardWinner = null;
        scores = new int[]{0, 0};
        updateLegalMoves();
    }

    /**
     * Make a move on the current board and check if this move has produced a game ending board state
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @param player The Tile (X or O) that will make the move
     * @return True if this move has produced a game ending board state and false if it hasn't
     */
    @Override
    public void move(int x, int y, Tile player){
        // Set the tile to the player symbol
        board[y][x] = player;
        // Update the legal moves
        updateLegalMoves();
        // Check if this was a winning move
        if(player != Tile.EMPTY) boardWinner = checkWin(x, y, board);
        if(boardWinner != null) scores = (boardWinner == Tile.BLACK) ? new int[]{1, 0} : (boardWinner == Tile.WHITE) ? new int[]{0, 1} : new int[]{0, 0};
    }

    /**
     * Checks if a move is legal (the move fits on the board and the square is still empty)
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @return True if the move is legal, false if it isn't
     */
    @Override
    public boolean checkLegalMove(int x, int y, Tile player){
        ArrayList<Point> moves = getLegalMoves(player);
        Point move = new Point(x, y);
        return moves.contains(move);
    }

    /**
     * Generates an ArrayList of all the legal moves that can be made for the current board position
     * Legal moves consist of squares that are still empty
     * @return The list of legal moves for the current board
     */
    @Override
    public void updateLegalMoves(){
        legalMoves = generateLegalMoves(board);
    }

    /**
     * Generates new legal moves for the given state of the board
     * @param board The current state of the board (represented by a 2D array of tiles)
     * @return An Arraylist of Point coordinates that this player can move to for the given board
     */
    public ArrayList<Point> generateLegalMoves(Tile[][] board){
        ArrayList<Point> tempLegalMoves = new ArrayList<>();
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(board[i][j] == Tile.EMPTY) tempLegalMoves.add(new Point(j, i));
            }
        }
        return tempLegalMoves;
    }

    /**
     * Checks if the last move made has produced a game winning board state. Since a game winning state can only occur
     * after a move, and the game winning combination has to include the last move, the check is limited to the row and column
     * the move was made on, and any diagonal it may have been made on.
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @return A Tile (X or O) if there is a winner. The Tile EMPTY if the game is a draw, or null if there is no winner yet
     */
    @Override
    public Tile checkWin(int x, int y, Tile[][] board){
        // Check horizontal win conditions
        for(int i=0;true;i++) {
            if(board[y][i] != board[y][i+1]){ break;}
            else if(i == (boardSize -2)) return board[y][i];
        }
        // Check vertical win conditions
        for(int i=0;true;i++) {
            if(board[i][x] != board[i+1][x]) break;
            else if(i == (boardSize -2)) return board[i][x];
        }
        // Check if move was made on left diagonal
        if(x == y) {
            // Check diagonal win conditions
            for (int i=0;true; i++){
                if (board[i][i] !=board[i+1][i+1]) break;
                else if (i == (boardSize -2)) return board[i][i];
            }
        }
        // Check if move was made on right diagonal
        if (x + y == (boardSize - 1)){
            // Check diagonal win conditions
            for (int i=0;true; i++){
                if (board[i][(boardSize -1)-i] !=board[i+1][(boardSize -1)-(i+1)]) break;
                else if (i == (boardSize -2)) return board[i][(boardSize -1)-i];
            }
        }
        // No more legal moves available, draw
        if(getLegalMoves(Tile.EMPTY).size() == 0){
            return Tile.EMPTY;
        }
        // No winner has occurred yet
        return null;
    }

    /**
     * Generates a move using the AI in the concrete model and calculates how long it took to generate this move
     * @return The move that the AI has generated as a Point (x, y) coordinates
     */
    @Override
    public Point computerMove(Tile tile){
        long oldTime = System.currentTimeMillis();
        Point computerMove = nextMove(tile);
        long newTime = System.currentTimeMillis();
        //System.out.println("AI took " + (newTime - oldTime) + " milliseconds to reach decision");
        return computerMove;
    }

    /**
     * Updates the scores of both players during the game
     */
    @Override
    public void updateScores() {
        // Empty method since unlike Chess, Checkers or Reversi, TicTacToe does not keep track of score based on moves
    }

    /**
     * @return int variable representing boardsize (width and height)
     */
    @Override
    public int getBoardSize(){
        return boardSize;
    }

    /**
     * @return The current playing board represented as a 2D object of tiles
     */
    @Override
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * @return True if this board has a winner, false if it does not
     */
    @Override
    public boolean hasWinner() {
        return boardWinner != null;
    }

    /**
     * @return The winner of this board
     */
    @Override
    public Tile getBoardWinner() { return boardWinner; }

    /**
     * @return An int array of the scores of both players
     */
    @Override
    public int[] getScores() { return scores; }

    /**
     * Returns the legal moves for a given player
     * @param player The player (black or white)
     * @return An ArrayList of Point objects with all the moves this player can make
     */
    @Override
    public ArrayList<Point> getLegalMoves(Tile player) {
        return legalMoves;
    }

    /**
     * Abstract method that is to be implemented by a concrete AI to calculate the next computer move
     * @param player The player that will make the move
     * @return A Point object representing the x and y coordinate of the move the computer has calculated
     */
    @Override
    abstract public Point nextMove(Tile player);
}
