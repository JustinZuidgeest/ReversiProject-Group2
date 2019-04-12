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
        if(humanPlayer == Tile.WHITE){
            playerToMove = remotePlayer;
            View.getInstance().setCanMove(false);
        }
        else{
            playerToMove = humanPlayer;
            View.getInstance().setCanMove(true);
        }
        System.out.println("Set human player to: " + humanPlayer);
        System.out.println("Set remote player to: " + remotePlayer);
        System.out.println("Set player to move to: " + playerToMove);
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
                View.getInstance().setCanMove(false);

                //Check if the game has ended and break out of the loop if it has
                if (gameOver) break;

                // If the AI player has moves available, hand over the turn to the AI player
                if (playerHasMoves(remotePlayer)) {
                    playerToMove = remotePlayer;
                    String infoString = "It's the remote player's turn to move!";
                    String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                    View.getInstance().updateInfoPane(infoString, scoreString);
                } else System.out.println("Player " + remotePlayer + " has no legal moves, returning turn to " + humanPlayer);
            }
        }
    }

    /**
     * Reset the game board back to its empty state
     */
    @Override
    public void newGame() {
        model.resetBoard();
        View.getInstance().updateBoard(model.getBoard());
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
        System.out.println("Player move x: " + x + ", y: " + y);
        try {
            if(model.checkLegalMove(x, y, humanPlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, humanPlayer);
                server.sendMove((y * model.getBoardSize()) + x);
                System.out.println("Sending " + ((y * model.getBoardSize()) + x) + " to server");
                if(model.hasWinner()){
                    View.getInstance().updateBoard(model.getBoard());
                    hasWin();
                }else{
                    View.getInstance().updateBoard(model.getBoard());
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
        System.out.println("Remote opponent move x: " + x + ", y: " + y);
        try {
            if(model.checkLegalMove(x, y, remotePlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, remotePlayer);
                if(model.hasWinner()){
                    View.getInstance().updateBoard(model.getBoard());
                    hasWin();
                }else{
                    View.getInstance().updateBoard(model.getBoard());
                    // If the AI player has moves available, hand over the turn to the AI player
                    if (playerHasMoves(humanPlayer)) {
                        playerToMove = humanPlayer;
                        View.getInstance().setCanMove(true);
                        String infoString = "It's your turn to move!";
                        String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                        View.getInstance().updateInfoPane(infoString, scoreString);
                    } else
                        System.out.println("Player " + remotePlayer + " has no legal moves, returning turn to " + humanPlayer);
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
        gameOver = true;
        Tile winner = model.getBoardWinner();
        String winnerColor = (winner == Tile.BLACK) ? "Black" : (winner == Tile.WHITE) ? "White" : "Nobody";
        String gameWinner = "The game has ended! The winner is " + winnerColor;
        String gameScores = "The final scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
        View.getInstance().updateInfoPane(gameWinner, gameScores);
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
