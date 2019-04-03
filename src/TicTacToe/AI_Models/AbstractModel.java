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

    public void resetBoard(){
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                board[i][j] = Symbol.EMPTY;
            }
        }
        boardWinner = null;
    }

    public boolean checkLegalMove(int x, int y){
        return (((x >= 0 && x < boardSize) && (y >= 0 && y < boardSize)) && board[y][x] == Symbol.EMPTY);
    }

    public ArrayList<Point> generateLegalMoves(){
        ArrayList<Point> legalMoves = new ArrayList<>();
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(checkLegalMove(j, i)) legalMoves.add(new Point(j, i));
            }
        }
        return legalMoves;
    }

    public boolean move(int x, int y, Symbol symbol){
        board[y][x] = symbol;
        // If the move was made by a player, check if this was a winning move
        if(symbol != Symbol.EMPTY) boardWinner = checkWinAfterMove(x, y);
        // Return if this was a winning move or not
        return (boardWinner != null);
    }

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

    public Point computerMove(){
        long oldTime = System.currentTimeMillis();
        Point computerMove = nextMove();
        long newTime = System.currentTimeMillis();
        System.out.println("AI took " + (newTime - oldTime) + " milliseconds to reach decision");
        return computerMove;
    }

    public Symbol[][] getBoard() {
        return board;
    }

    public Symbol getBoardWinner() { return boardWinner; }

    abstract public Point nextMove();
}
