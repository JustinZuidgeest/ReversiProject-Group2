package games.reversi;

import games.Model;
import games.Tile;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractReversiModel implements Model {

    private Tile[][] board;
    private int boardSize;
    private Tile boardWinner = null;
    private int[] scores;
    private ArrayList<Point> tilesOnBoard;
    private ArrayList<Point> whiteLegalMoves;
    private ArrayList<Point> blackLegalMoves;
    private int[][] allDirections;

    public AbstractReversiModel(int boardSize) {
        this.boardSize = boardSize;
        board = new Tile[boardSize][boardSize];
        tilesOnBoard = new ArrayList<>();
        scores = new int[2];
        whiteLegalMoves = blackLegalMoves = new ArrayList<>();
        // Make an array with the coordinates for every direction from this tile
        // Up, down, left, right, upper right, lower right, lower left, upper left
        allDirections = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}, {1, -1}, {1, 1}, {-1, 1}, {-1, -1}};
    }

    @Override
    public void resetBoard() {
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                board[j][i] = Tile.EMPTY;
            }
        }
        board[3][3] = board[4][4] = Tile.WHITE;
        board[3][4] = board[4][3] = Tile.BLACK;
        Point[] tiles = new Point[]{new Point(3, 3), new Point(3, 4), new Point(4, 3), new Point(4, 4)};
        tilesOnBoard.addAll(Arrays.asList(tiles));
        boardWinner = null;
        scores = new int[]{2, 2};
        updateScores();
        updateLegalMoves();
    }

    @Override
    public void move(int x, int y, Tile player) {
        // Set the tile to the player color
        board[y][x] = player;
        // Add this tile to the list of tiles on the board
        tilesOnBoard.add(new Point(x, y));
        // Flip all tiles
        flipTiles(x, y, player, board);
        // Update the legal moves for both players
        updateLegalMoves();
        // Update the scores after this move
        updateScores();
        // Check if this was a winning move
        boardWinner = checkWin(x, y, board);
    }

    @Override
    public boolean checkLegalMove(int x, int y, Tile player) {
        ArrayList<Point> moves = getLegalMoves(player);
        Point move = new Point(x, y);
        return moves.contains(move);
    }

    @Override
    public void updateLegalMoves() {
        whiteLegalMoves = generateLegalMoves(Tile.WHITE, board);
        blackLegalMoves = generateLegalMoves(Tile.BLACK, board);
    }

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

    private boolean onBoard(int x, int y){
        return (x >= 0 && x < boardSize && y >= 0 && y < boardSize);
    }

    @Override
    public Tile checkWin(int x, int y, Tile[][] board) {
        if(getLegalMoves(Tile.WHITE).size() == 0 && getLegalMoves(Tile.BLACK).size() == 0){
            if(scores[0] > scores[1]){
                return Tile.WHITE;
            }else if(scores[0] < scores[1]){
                return Tile.BLACK;
            }else if (scores[0] == scores[1]){
                return Tile.EMPTY;
            }
        }
        return null;
    }

    @Override
    public void updateScores() {
        int blackScore = 0;
        int whiteScore = 0;
        for(Point tile:tilesOnBoard){
            if(board[tile.y][tile.x] == Tile.BLACK){
                blackScore++;
            }else if(board[tile.y][tile.x] == Tile.WHITE){
                whiteScore++;
            }
        }
        scores[0] = blackScore;
        scores[1] = whiteScore;
    }

    @Override
    public Point computerMove(Tile tile) {
        long oldTime = System.currentTimeMillis();
        Point computerMove = nextMove(tile);
        long newTime = System.currentTimeMillis();
        //System.out.println("AI took " + (newTime - oldTime) + " milliseconds to reach decision");
        return computerMove;
    }

    @Override
    public String getGameName() {
        return "reversi";
    }

    @Override
    public int getBoardSize(){
        return boardSize;
    }

    @Override
    public Tile[][] getBoard() {
        return board;
    }

    @Override
    public boolean hasWinner() {
        return boardWinner != null;
    }

    @Override
    public Tile getBoardWinner() {
        return boardWinner;
    }

    @Override
    public int[] getScores() { return scores; }

    @Override
    public ArrayList<Point> getLegalMoves(Tile player) {
        if(player == Tile.BLACK) return blackLegalMoves;
        else if(player == Tile.WHITE) return whiteLegalMoves;
        else return null;
    }

    @Override
    public abstract Point nextMove(Tile player);
}
