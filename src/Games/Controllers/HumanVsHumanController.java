package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import view.View;

import java.awt.*;

public class HumanVsHumanController implements Controller {

    private Model model;

    private boolean die = false;
    private boolean gameOver;
    private Tile playerToMove;
    // The human player
    private Tile playerOne;
    // The AI player
    private Tile playerTwo;

    private int boardSize;

    public HumanVsHumanController(Model model) {
        this.model = model;
        this.boardSize = model.getBoardSize();
    }

    @Override
    public void newGame() {
        gameOver = false;
        model.resetBoard();
        // Black (X) moves first
        playerToMove = Tile.BLACK;
        playerOne = null;
        playerTwo = null;
        View.getInstance().updateBoard(model.getBoard());
    }

    @Override
    public void run() {
        System.out.println("New Human vs Human game thread has started");
        while(!die) {
            // Retrieve the move that the player clicked
            Point playerMove = View.getInstance().getNextMove();

            // Sleep the thread for small intervals to avoid cooking the CPU
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the first player is the next to move and has clicked a move
            if(playerToMove == playerOne && playerMove != null){

                // Check if the player clicked a correct move, allow him to click another move if it wasn't
                // Execute the move if the move was legal
                if (!playerMove(playerMove.x, playerMove.y)){
                    View.getInstance().setNextMove(null);
                    continue;
                }

                View.getInstance().setNextMove(null);

                //Check if the game has ended and break out of the loop if it has
                if(gameOver) break;

                // If the AI player has moves available, hand over the turn to the AI player
                if(playerHasMoves(playerTwo)){
                    playerToMove = playerTwo;
                }else System.out.println("Player " + playerTwo + " has no legal moves, returning turn to " + playerOne);
            }
            //If the second player is the next to move and has clicked a move
            else if(playerToMove == playerTwo && playerMove != null){

                // Check if the player clicked a correct move, allow him to click another move if it wasn't
                // Execute the move if the move was legal
                if (!playerTwoMove(playerMove.x, playerMove.y)){
                    View.getInstance().setNextMove(null);
                    continue;
                }

                View.getInstance().setNextMove(null);

                //Check if the game has ended and break out of the loop if it has
                if(gameOver) break;

                // If the AI player has moves available, hand over the turn to the AI player
                if(playerHasMoves(playerOne)){
                    playerToMove = playerOne;
                }else System.out.println("Player " + playerOne + " has no legal moves, returning turn to " + playerOne);
            }
        }
        System.out.println("Human vs Human game thread died :(");
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public void setPlayerOne(Tile tile) {
        playerOne = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        playerTwo = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        View.getInstance().setCanMove(true);
    }

    @Override
    public boolean playerMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, playerOne)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, playerOne);
                if(model.hasWinner()){
                    View.getInstance().updateBoard(model.getBoard());
                    hasWin();
                }else{
                    String infoString = "It's Whites turn to move!";
                    String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                    View.getInstance().updateBoard(model.getBoard());
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

    private boolean playerTwoMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, playerTwo)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, playerTwo);
                if(model.hasWinner()){
                    View.getInstance().updateBoard(model.getBoard());
                    hasWin();
                }else{
                    String infoString = "It's Black's turn to move!";
                    String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                    View.getInstance().updateBoard(model.getBoard());
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
    public void aiMove(){
        //No AI in this controller
    }

    private boolean playerHasMoves(Tile player){
        return (model.getLegalMoves(player).size() != 0);
    }

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
}