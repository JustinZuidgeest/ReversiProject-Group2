package tictactoe;

import server_communication.ServerCommunicator;
import tictactoe.AI_Models.AbstractModel;

import java.awt.Point;
import java.util.ArrayList;

public class TictactoeController {

    private CommandlineView view;
    private AbstractModel model;
    private ServerCommunicator server;

    // The human player
    private Symbol playerOne;
    // The AI player
    private Symbol playerTwo;

    public TictactoeController(CommandlineView view, AbstractModel model) {
        this.view = view;
        this.model = model;
        this.server = new ServerCommunicator(this);

        server.connectToServer();
        new Thread(server).start();
        server.login();
        server.getGameList();

        server.subscribe("Tic-tac-toe");

    }

    /**
     * Reset the game board back to its empty state
     */
    public void newGame(){
        model.resetBoard();
        playerOne = null;
        playerTwo = null;
    }

    public void setPlayerOne(Symbol symbol){
        playerOne = (symbol == Symbol.X) ? Symbol.X : Symbol.O;
        playerTwo = (symbol == Symbol.X) ? Symbol.O : Symbol.X;
    }

    /**
     * Makes a move for the human player. Checks to see if the move is legal and makes it, or throws an exception
     * if the coordinates entered are not a legal move. Checks if this move was a winning move, and ends the game if it was.
     * @param x The x coordinate of the move
     * @param y The y coordinate of the move
     * @return True if the move was succesfull, false if it wasn't
     */
    public boolean playerMove(int x, int y){
        try {
            if(model.checkLegalMove(x, y)){
                // Execute the move, and execute hasWin() function if this was a winning move
                if(model.move(x, y, playerOne)){
                    //server.sendMove(x*y);
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

    /**
     * Makes a move for the computer player. Generates a computer move based on the current AI initialized in the model
     * and executes the move. Checks if this move was a winning move, and ends the game if it was.
     */
    public void aiMove(){
        // Generate a move made by the computer
        Point aiMove = model.computerMove(playerTwo);
        server.sendMove((aiMove.y * 3) + aiMove.x);
        // Execute the move, and execute hasWin() function if this was a winning move
        if(model.move(aiMove.x, aiMove.y, playerTwo)){
            view.printBoard(model.getBoard());
            hasWin();
        }else view.printBoard(model.getBoard());
    }

    /**
     * Prints the winner of the game and sets the game over flag to true so the game loop ends
     */
    public void hasWin(){
        view.printWinner(model.getBoardWinner());
        view.setGameOver(true);
    }
}
