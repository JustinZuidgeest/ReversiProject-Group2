package Games.Reversi;

import Games.Model;
import Games.Tile;
import java.awt.Point;
import java.util.ArrayList;

public abstract class AbstractReversiModel implements Model {

    private Tile[][] board;
    private int boardSize;
    private Tile boardWinner = null;
    private int whiteScore;
    private int blackScore;
    private ArrayList<Point> whiteLegalMoves;
    private ArrayList<Point> blackLegalMoves;
    private int[][] allDirections;

    /**
     * Constructor called at object creation. Initializes the variables that represent the board, the lists of legal
     * moves and the list of directions a player can move in.
     * @param boardSize The size of a board size (a board is always square, so size dictates both width and height)
     */
    public AbstractReversiModel(int boardSize) {
        this.boardSize = boardSize;
        board = new Tile[boardSize][boardSize];
        whiteLegalMoves = blackLegalMoves = new ArrayList<>();
        // Make an array with the coordinates for every direction from this tile
        // Up, down, left, right, upper right, lower right, lower left, upper left
        allDirections = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}, {1, -1}, {1, 1}, {-1, 1}, {-1, -1}};
    }

    /**
     * Resets the game back to it's opening state by filling the board with empty tiles (no player occupies these tiles)
     * and places the four white and black tiles that make up the opening state of a Reversi board.
     */
    @Override
    public void resetBoard() {
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                board[j][i] = Tile.EMPTY;
            }
        }
        board[3][3] = board[4][4] = Tile.WHITE;
        board[3][4] = board[4][3] = Tile.BLACK;
        boardWinner = null;
        updateScores();
        updateLegalMoves();
    }

    /**
     * Makes a move for a player by turning that tile to the player's color and flipping all tiles on the board as a
     * result of this move. Checks if this move was a winner after every move and updates the boardwinner if it was
     * a winning move.
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @param player The player (black or white) that executes this move
     */
    @Override
    public void move(int x, int y, Tile player) {
        // Set the tile to the player color
        board[y][x] = player;
        // Flip all tiles
        flipTiles(x, y, player, board);
        // Update the legal moves for both players
        updateLegalMoves();
        // Update the scores after this move
        updateScores();
        // Check if this was a winning move and set boardwinner if it was
        boardWinner = checkWin(x, y, board);
    }

    /**
     * Checks to see if the entered coordinates are a legal move for the given player
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @param player The player color (black or white)
     * @return True if this is a legal move, false if it isn't
     */
    @Override
    public boolean checkLegalMove(int x, int y, Tile player) {
        ArrayList<Point> moves = getLegalMoves(player);
        Point move = new Point(x, y);
        return moves.contains(move);
    }

    /**
     * Updates the lists of legal moves for both black and white
     */
    @Override
    public void updateLegalMoves() {
        whiteLegalMoves = generateLegalMoves(Tile.WHITE, board);
        blackLegalMoves = generateLegalMoves(Tile.BLACK, board);
    }

    /**
     * Generates new legal moves for the given player and the given state of the board
     * @param player The player color (black or white)
     * @param board The current state of the board (represented by a 2D array of tiles)
     * @return An Arraylist of Point coordinates that this player can move to for the given board
     */
    public ArrayList<Point> generateLegalMoves(Tile player, Tile[][] board){
        Tile oppositeColor = (player == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        ArrayList<Point> legalMoves = new ArrayList<>();
        // Loop through all the tiles on the board
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++) {
                // Check if tile is not empty
                if(board[i][j] != Tile.EMPTY) {
                    //Check to see if this tile belongs to the requested player
                    if (board[i][j] == player) {
                        // Loop through all the directions from this tile
                        for (int[] direction : allDirections) {
                            int tempX = j + direction[0];
                            int tempY = i + direction[1];
                            // If a direction is on the board and contains the opposite color piece continue down this direction
                            if (onBoard(tempX, tempY) && board[tempY][tempX] == oppositeColor) {
                                tempX += direction[0];
                                tempY += direction[1];
                                // Check if new tile is on the board and keep going
                                while (onBoard(tempX, tempY)) {
                                    // If own tile is encountered again, stop looking and continue in the next direction
                                    if (board[tempY][tempX] == player) {
                                        break;
                                    }
                                    // If an empty tile is encountered again, add tile to list of possible moves and contine in the next direction
                                    else if (board[tempY][tempX] == Tile.EMPTY) {
                                        Point move = new Point(tempX, tempY);
                                        if (!legalMoves.contains(move)) legalMoves.add(move);
                                        break;
                                    }
                                    tempX += direction[0];
                                    tempY += direction[1];
                                }
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    /**
     * Flips all tiles on the given board for the given move of a given player
     * @param x The X coordinates of the move
     * @param y The Y coordinates of the move
     * @param player The player color (black or white)
     * @param board A board variable represented by a 2D array of tiles
     */
    public void flipTiles(int x, int y, Tile player, Tile[][] board){
        Tile oppositeColor = (player == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        // Loop through all the directions
        for(int[] direction:allDirections){
            int tempX = x + direction[0];
            int tempY = y + direction[1];
            // If a direction is on the board and contains the opposite color piece continue down this direction
            if(onBoard(tempX, tempY) && board[tempY][tempX] == oppositeColor){
                // Create a list of potential tiles to flip and add the last checked tile
                ArrayList<Point> potentialFlip = new ArrayList<>();
                potentialFlip.add(new Point(tempX, tempY));
                // Move one step in the direction
                tempX += direction[0];
                tempY += direction[1];
                // Check if new tile is on the board and keep going
                while(onBoard(tempX, tempY)) {
                    // If new tile is enemy tile, add to potential list of flippable tiles and continue this direction
                    if (board[tempY][tempX] == oppositeColor){
                        potentialFlip.add(new Point(tempX, tempY));
                    }
                    // If an empty tile is found, break the loop since there is nothing to flip in this direction
                    else if(board[tempY][tempX] == Tile.EMPTY){
                        break;
                    }
                    // If your own tile is encountered again, flip all tiles in between and check next direction
                    else if (board[tempY][tempX] == player) {
                        for (Point tile : potentialFlip) {
                            board[tile.y][tile.x] = player;
                        }
                        break;
                    }
                    tempX += direction[0];
                    tempY += direction[1];
                }
            }
        }
    }

    /**
     * Helper function to check if a coordinate is on the playing board or not
     * @param x The X coordinate of the move you want to check
     * @param y The Y coordinate of the move you want to check
     * @return True if this coordinate is on the playing board, false if it isn't
     */
    private boolean onBoard(int x, int y){
        return (x >= 0 && x < boardSize && y >= 0 && y < boardSize);
    }

    /**
     * Checks to see if the given board contains a winner after a move
     * @param x The X coordinate of the move
     * @param y The Y coordinate of the move
     * @param board The board variable represented by a 2D array
     * @return null if this board has no winner or a Tile object representing the board winner
     */
    @Override
    public Tile checkWin(int x, int y, Tile[][] board) {
        //Check if the win condition applies (both players have no legal moves remaining)
        if(getLegalMoves(Tile.WHITE).size() == 0 && getLegalMoves(Tile.BLACK).size() == 0){
            //If white has the most tiles, white wins
            if(whiteScore > blackScore){
                return Tile.WHITE;
            //If black has the most tiles, black wins
            }else if(whiteScore < blackScore){
                return Tile.BLACK;
            //If both players have an equal amount of tiles, a draw has occurred
            }else if (blackScore == whiteScore){
                return Tile.EMPTY;
            }
        }
        return null;
    }

    /**
     * Updates the scores of both players by counting the amount of tiles both players have on the board
     */
    @Override
    public void updateScores() {
        this.whiteScore = 0;
        this.blackScore = 0;
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(board[i][j] == Tile.BLACK){
                    this.blackScore++;
                }else if(board[i][j] == Tile.WHITE){
                    this.whiteScore++;
                }
            }
        }
    }

    /**
     * Calls upon the concrete implementation of a game algorithm to calculate the next move the AI would make
     * @param tile The color that the AI has to make a move for (black or white)
     * @return A Point coordinate variable (x and y coordinate) representing the move the AI has calculated
     */
    @Override
    public Point computerMove(Tile tile) {
        long oldTime = System.currentTimeMillis();
        Point computerMove = nextMove(tile);
        long newTime = System.currentTimeMillis();
        System.out.println("AI took " + (newTime - oldTime) + " milliseconds to reach decision");
        return computerMove;
    }

    /**
     * @return A string value representing the game this model implements
     */
    @Override
    public String getGameName() { return "Reversi"; }

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
    public Tile getBoardWinner() {
        return boardWinner;
    }

    /**
     * @return An int array of the scores of both players
     */
    @Override
    public int[] getScores() { return new int[]{blackScore, whiteScore}; }

    /**
     * Returns the legal moves for a given player
     * @param player The player (black or white)
     * @return An ArrayList of Point objects with all the moves this player can make
     */
    @Override
    public ArrayList<Point> getLegalMoves(Tile player) {
        if(player == Tile.BLACK) return blackLegalMoves;
        else if(player == Tile.WHITE) return whiteLegalMoves;
        else return null;
    }

    /**
     * Abstract method that is to be implemented by a concrete AI to calculate the next computer move
     * @param player The player that will make the move
     * @return A Point object representing the x and y coordinate of the move the computer has calculated
     */
    @Override
    public abstract Point nextMove(Tile player);
}
