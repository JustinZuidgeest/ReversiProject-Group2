package TicTacToe;

import TicTacToe.AI_Algorithms.ComputerAlgorithm;
import TicTacToe.AI_Algorithms.RandomAI;

import java.awt.Point;
import java.util.ArrayList;

public class TictactoeModel {

    private ComputerAlgorithm computerAlgorithm;
    private Symbol[][] board;
    private ArrayList<Point> legalMoves;

    public TictactoeModel() {
        board = new Symbol[3][3];
        legalMoves = new ArrayList<>();
        computerAlgorithm = new RandomAI();
    }

    public void resetBoard(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                board[i][j] = Symbol.EMPTY;
                legalMoves.add(new Point(j, i));
            }
        }
    }

    public boolean checkLegalMove(int x, int y){
        return (((x >= 0 && x < 3) && (y >= 0 && y < 3)) && board[y][x] == Symbol.EMPTY);
    }

    public void move(int x, int y, Symbol symbol){
        board[y][x] = symbol;
        legalMoves.remove(new Point(x, y));
    }

    public Symbol checkWin(int x, int y, Symbol symbol){
        // Check horizontal win conditions
        for(int i=0;true;i++) {
            if(board[y][i] != symbol) break;
            else if(i == 2) return symbol;
        }

        // Check vertical win conditions
        for(int i=0;true;i++) {
            if(board[i][x] != symbol) break;
            else if(i == 2) return symbol;
        }

        // Check if move was made on left diagonal
        if(x == y) {
            // Check diagonal win conditions
            for (int i=0;true; i++){
                if (board[i][i] !=symbol) break;
                else if (i == 2) return symbol;
            }
        }

        // Check if move was made on right diagonal
        if (x + y == 3){
            // Check diagonal win conditions
            for (int i=0;true; i++){
                if (board[i][2-i] !=symbol) break;
                else if (i == 2) return symbol;
            }
        }

        // No more legal moves available, draw
        if(legalMoves.size() == 0){
            return Symbol.EMPTY;
        }

        // No winner has occurred yet
        return null;
    }

    public Point computerMove(){
        return computerAlgorithm.nextMove(legalMoves);
    }

    public Symbol[][] getBoard() {
        return board;
    }

    public ArrayList<Point> getLegalMoves() {
        return legalMoves;
    }
}
