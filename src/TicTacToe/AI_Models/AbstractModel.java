package TicTacToe.AI_Models;

import TicTacToe.Symbol;

import java.awt.Point;
import java.util.ArrayList;

public abstract class AbstractModel {

    private Symbol[][] board;
    private int boardSize;
    private Symbol boardWinner = null;

    public AbstractModel(int boardSize) {
        this.boardSize = boardSize;
        board = new Symbol[boardSize][boardSize];
    }

    /**
     * Fill the board with Symbol.EMPTY (no player has moved here yet) objects to create an empty board
     * Sets the winner of the current board to null (there is no winner yet)
     */
    public void resetBoard(){
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                board[i][j] = Symbol.EMPTY;
            }
        }
        boardWinner = null;
    }

    /**
     * Checks if a move is legal (the move fits on the board and the square is still empty)
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @return True if the move is legal, false if it isn't
     */
    public boolean checkLegalMove(int x, int y){
        return (((x >= 0 && x < boardSize) && (y >= 0 && y < boardSize)) && board[y][x] == Symbol.EMPTY);
    }

    /**
     * Generates an ArrayList of all the legal moves that can be made for the current board position
     * Legal moves consist of squares that are still empty
     * @return The list of legal moves for the current board
     */
    public ArrayList<Point> generateLegalMoves(){
        ArrayList<Point> legalMoves = new ArrayList<>();
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(checkLegalMove(j, i)) legalMoves.add(new Point(j, i));
            }
        }
        return legalMoves;
    }

    /**
     * Make a move on the current board and check if this move has produced a game ending board state
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @param symbol The Symbol (X or O) that will make the move
     * @return True if this move has produced a game ending board state and false if it hasn't
     */
    public boolean move(int x, int y, Symbol symbol){
        board[y][x] = symbol;
        // If the move was made by a player, check if this was a winning move
        if(symbol != Symbol.EMPTY) boardWinner = checkWinAfterMove(x, y);
        // Return if this was a winning move or not
        return (boardWinner != null);
    }

    /**
     * Checks if the last move made has produced a game winning board state. Since a game winning state can only occur
     * after a move, and the game winning combination has to include the last move, the check is limited to the row and column
     * the move was made on, and any diagonal it may have been made on.
     * @param x The x coordinate for the move
     * @param y The y coordinate for the move
     * @return A Symbol (X or O) if there is a winner. The Symbol EMPTY if the game is a draw, or null if there is no winner yet
     */
    public Symbol checkWinAfterMove(int x, int y){
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
        if(generateLegalMoves().size() == 0){
            return Symbol.EMPTY;
        }
        // No winner has occurred yet
        return null;
    }

    /**
     * Generates a move using the AI in the concrete model and calculates how long it took to generate this move
     * @return The move that the AI has generated as a Point (x, y) coordinates
     */
    public Point computerMove(Symbol symbol){
        long oldTime = System.currentTimeMillis();
        Point computerMove = nextMove(symbol);
        long newTime = System.currentTimeMillis();
        System.out.println("AI took " + (newTime - oldTime) + " milliseconds to reach decision");
        return computerMove;
    }

    public Symbol[][] getBoard() {
        return board;
    }

    public Symbol getBoardWinner() { return boardWinner; }

    // Abstract function that has to be implemented by a concrete AI
    abstract public Point nextMove(Symbol player);
}
