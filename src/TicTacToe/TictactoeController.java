package TicTacToe;

import java.awt.*;

public class TictactoeController {

    private CommandlineView view;
    private TictactoeModel model;

    public TictactoeController(CommandlineView view, TictactoeModel model) {
        this.view = view;
        this.model = model;
    }

    public void newGame(){
        model.resetBoard();
    }

    public boolean playerMove(int x, int y){
        try {
            if(model.checkLegalMove(x, y)){
                model.move(x, y, Symbol.X);
                view.printBoard(model.getBoard());
                checkWin(x, y, Symbol.X);
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
        Point aiMove = model.computerMove();
        model.move(aiMove.x, aiMove.y, Symbol.O);
        view.printBoard(model.getBoard());
        checkWin(aiMove.x, aiMove.y, Symbol.O);
    }

    public void checkWin(int x, int y, Symbol symbol){
        if(model.getLegalMoves().size() < 7) {
            Symbol winner = model.checkWin(x, y, symbol);
            if (winner != null) {
                view.printWinner(winner);
                view.setGameOver(true);
            }
        }
    }
}
