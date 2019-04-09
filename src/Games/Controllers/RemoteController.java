package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.View;

import java.awt.Point;
import java.util.ArrayList;

public class TictactoeController implements Controller {

    private View view;
    private Model model;

    private boolean gameOver;
    private Tile playerToMove;
    // The human player
    private Tile humanPlayer;
    // The AI player
    private Tile computerPlayer;

    public TictactoeController(View view, Model model) {
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
    @Override
    public void newGame() {
        model.resetBoard();
        humanPlayer = null;
        computerPlayer = null;
        view.updateBoard(model.getBoard());
    }

    @Override
    public void setHumanPlayer(Tile tile) {
        humanPlayer = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        computerPlayer = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
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
                if(model.move(x, y, humanPlayer)){
                    //server.sendMove(x*y);
                    view.updateBoard(model.getBoard());
                    hasWin();
                }else view.updateBoard(model.getBoard());
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
        Point aiMove = model.computerMove(computerPlayer);
        server.sendMove((aiMove.y * 3) + aiMove.x);
        // Execute the move, and execute hasWin() function if this was a winning move
        if(model.move(aiMove.x, aiMove.y, computerPlayer)){
            view.updateBoard(model.getBoard());
            hasWin();
        }else view.updateBoard(model.getBoard());
    }

    /**
     * Prints the winner of the game and sets the game over flag to true so the game loop ends
     */
    @Override
    public void hasWin() {
        view.printWinner(model.getBoardWinner());
        System.out.println("The final scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
        gameOver = true;
    }
}
