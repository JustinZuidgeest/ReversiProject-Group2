package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import view.View;

import java.awt.Point;
import java.util.ArrayList;

public class HumanVsRemoteController implements Controller {

    private Model model;

    private boolean die = false;
    private boolean gameOver;
    private Tile playerToMove;
    private ServerCommunicator server;
    // The human player
    private Tile humanPlayer;
    // The remote player
    private Tile remotePlayer;
    private int boardSize;
    private boolean waiting;

    public HumanVsRemoteController(Model model) {
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
    public void setPlayerOne(Tile tile) {
        humanPlayer = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        remotePlayer = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
    }

    public void setPlayerToMove(){
        playerToMove = humanPlayer;
    }

    @Override
    public void run() {
        while (!die) {
            // Retrieve the move that the player clicked
            Point playerMove = View.getInstance().getNextMove();

            // Sleep the thread for small intervals to avoid cooking the CPU
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the first player is the next to move and has clicked a move
            if (playerToMove == humanPlayer && playerMove != null) {

                // Check if the player clicked a correct move, allow him to click another move if it wasn't
                // Execute the move if the move was legal
                if (!playerMove(playerMove.x, playerMove.y)) {
                    View.getInstance().setNextMove(null);
                    continue;
                }

                View.getInstance().setNextMove(null);

                //Check if the game has ended and break out of the loop if it has
                if (gameOver) break;

                // If the AI player has moves available, hand over the turn to the AI player
                if (playerHasMoves(remotePlayer)) {
                    playerToMove = remotePlayer;
                } else
                    System.out.println("Player " + remotePlayer + " has no legal moves, returning turn to " + humanPlayer);
                //If the second player is the next to move and has clicked a move

            }
            //If the second player is the next to move and has clicked a move
            else if (playerToMove == remotePlayer && playerMove != null) {
                waitingOnRemotePlayer(1);
                while(waiting){
                    //waiting on remote player
                }
                playerToMove = humanPlayer;
                //Check if the game has ended and break out of the loop if it has
                if (gameOver) break;

                // If the AI player has moves available, hand over the turn to the AI player
                if (playerHasMoves(humanPlayer)) {
                    playerToMove = humanPlayer;
                } else
                    System.out.println("Player " + humanPlayer + " has no legal moves, returning turn to " + remotePlayer);
                //If the second player is the next to move and has clicked a move
            }
        }
    }

    /**
     * Reset the game board back to its empty state
     */
    @Override
    public void newGame() {
        model.resetBoard();
        humanPlayer = null;
        remotePlayer = null;
        //view.updateBoard(model.getBoard());
    }

    public void waitingOnRemotePlayer(int i){
        waiting = i==1;
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
            if(model.checkLegalMove(x, y, humanPlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, humanPlayer);
                if(model.hasWinner()){
                    //view.updateBoard(model.getBoard());
                    hasWin();
                }else{
                    //view.updateBoard(model.getBoard());
                    System.out.println("The scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
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

    public boolean playerTwoMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, remotePlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, remotePlayer);
                if(model.hasWinner()){
                    View.getInstance().updateBoard(model.getBoard());
                    hasWin();
                }else{
                    View.getInstance().updateBoard(model.getBoard());
                    //todo infopane implementation
                    //View.getInstance().updateScores(model.getScores()[0], model.getScores()[1]);
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

    /**
     * Makes a move for the computer player. Generates a computer move based on the current AI initialized in the model
     * and executes the move. Checks if this move was a winning move, and ends the game if it was.
     */
    @Override
    public void aiMove() {
        //No AI in this controller
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    private boolean playerHasMoves(Tile player){
        return (model.getLegalMoves(player).size() != 0);
    }

    /**
     * Prints the winner of the game and sets the game over flag to true so the game loop ends
     */
    @Override
    public void hasWin() {
        //view.printWinner(model.getBoardWinner());
        System.out.println("The final scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
        gameOver = true;
    }

    @Override
    public void killThread() {
        die = true;
    }

    @Override
    public ServerCommunicator getServer() {
        return server;
    }
}
