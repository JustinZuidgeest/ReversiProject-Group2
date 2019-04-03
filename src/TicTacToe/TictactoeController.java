package TicTacToe;

import TicTacToe.AI_Models.AbstractModel;

import java.awt.Point;

public class TictactoeController {

    private CommandlineView view;
    private AbstractModel model;

    public TictactoeController(CommandlineView view, AbstractModel model) {
        this.view = view;
        this.model = model;
    }

    public void newGame(){
        model.resetBoard();
    }

    public boolean playerMove(int x, int y){
        try {
            if(model.checkLegalMove(x, y)){
                // Execute the move, and execute hasWin() function if this was a winning move
                if(model.move(x, y, Symbol.X)){
                    view.printBoard(model.getBoard());
                    hasWin();
                }else view.printBoard(model.getBoard());
                return true;
            }else{
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not a legal move");
            return false;
        }
    }

    public void aiMove(){
        // Generate a move made by the computer
        Point aiMove = model.computerMove();
        // Execute the move, and execute hasWin() function if this was a winning move
        if(model.move(aiMove.x, aiMove.y, Symbol.O)){
            view.printBoard(model.getBoard());
            hasWin();
        }else view.printBoard(model.getBoard());
    }

    public void hasWin(){
        view.printWinner(model.getBoardWinner());
        view.setGameOver(true);
    }
}
