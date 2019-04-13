package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import view.View;

import java.awt.Point;

public class AiVsRemoteController implements Controller {

    private Model model;

    private ServerCommunicator server;
    // The human player
    private Tile AIPlayer;
    // The AI player
    private Tile remotePlayer;
    private int boardSize;

    public AiVsRemoteController(Model model) {
        this.model = model;
        this.server = new ServerCommunicator(this);

        this.boardSize = model.getBoardSize();

        server.connectToServer();
        Thread thread = new Thread(server);
        thread.setDaemon(true);
        thread.start();
        server.login();

        server.getGameList();
        server.getPlayerList();

        newGame();
    }

    @Override
    public void run(){
        //null
    }

    /**
     * Reset the game board back to its empty state
     */
    @Override
    public void newGame() {
        View.getInstance().setCanMove(false);
        model.resetBoard();
        View.getInstance().updateBoard(model.getBoard());
    }

    @Override
    public void setPlayerOne(Tile tile) {
        AIPlayer = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        remotePlayer = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
    }

    /**
     * Makes a move for the human player. Checks to see if the move is legal and makes it, or throws an exception
     * if the coordinates entered are not a legal move. Checks if this move was a winning move, and ends the game if it was.
     * @param x The x coordinate of the move
     * @param y The y coordinate of the move
     * @return True if the move was succesfull, false if it wasn't
     */
    @Override
    public boolean playerMove(int x, int y) {
        try {
            System.out.println("Remote playermove x: " + x + ", y: " + y);
            if(model.checkLegalMove(x, y, remotePlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, remotePlayer);
                if(model.hasWinner()){
                    View.getInstance().updateBoard(model.getBoard());
                    hasWin();
                }else{
                    View.getInstance().updateBoard(model.getBoard());
                    String infoString = "It's our turn to move!";
                    String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                    View.getInstance().updateInfoPane(infoString, scoreString);
                }
                return true;
            }else{
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not a legal move");
            return false;
        }
    }

    @Override
    public boolean playerTwoMove(int x, int y){
        //No playerTwoMove in controller
        return false;
    }

    /**
     * Makes a move for the computer player. Generates a computer move based on the current AI initialized in the model
     * and executes the move. Checks if this move was a winning move, and ends the game if it was.
     */
    @Override
    public void aiMove() {
        // Generate a move made by the computer
        Point aiMove = model.computerMove(AIPlayer);
        System.out.println("Our AI moves x: " + aiMove.x + ", y: " + aiMove.y);
        // Execute the move, and execute hasWin() function if this was a winning move
        server.sendMove((aiMove.y * boardSize) + aiMove.x);
        model.move(aiMove.x, aiMove.y, AIPlayer);
        if(model.hasWinner()){
            View.getInstance().updateBoard(model.getBoard());
            hasWin();
        }else{
            View.getInstance().updateBoard(model.getBoard());
            System.out.println("The scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
        }
        String infoString = "It's the remote player's turn to move!";
        String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
        View.getInstance().updateInfoPane(infoString, scoreString);
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Prints the winner of the game and sets the game over flag to true so the game loop ends
     */
    @Override
    public void hasWin() {
        //view.printWinner(model.getBoardWinner());
        System.out.println("The final scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
    }

    @Override
    public void killThread(){}

    @Override
    public ServerCommunicator getServer() {
        return server;
    }

    @Override
    public void displayGameResult(String result, String details) {
        String infoString = (result.equals("WIN")) ? "You Have won" : (result.equals("LOSS")) ? "You have lost" : "It's a draw";
        infoString = infoString + " " + details;
        String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
        View.getInstance().updateInfoPane(infoString, scoreString);
    }
}
