package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import view.View;

import java.awt.Point;

public class HumanVsAiController implements Controller {

    private Model model;

    private boolean gameOver;
    private boolean die = false;
    private Tile playerToMove;
    // The human player
    private Tile humanPlayer;
    // The AI player
    private Tile computerPlayer;

    private int boardSize;

    public HumanVsAiController(Model model) {
        this.model = model;
        this.boardSize = model.getBoardSize();
    }

    @Override
    public void newGame() {
        gameOver = false;
        model.resetBoard();
        // Black (X) moves first
        playerToMove = Tile.BLACK;
        humanPlayer = null;
        computerPlayer = null;
        View.getInstance().updateBoard(model.getBoard());
    }

    @Override
    public void run() {
        System.out.println("New Human vs AI game thread has started");
        while(!die) {
            // Retrieve the move that the player clicked
            Point playerMove = View.getInstance().getNextMove();

            // Sleep the thread for small intervals to avoid cooking the CPU
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the player is the next to move and has clicked a move
            if(playerToMove == humanPlayer && playerMove != null){

                // Check if the player clicked a correct move, allow him to click another move if it wasn't
                // Execute the move if the move was legal
                if (!playerMove(playerMove.x, playerMove.y)){
                    View.getInstance().setNextMove(null);
                    View.getInstance().setCanMove(true);
                    continue;
                }

                View.getInstance().setNextMove(null);
                View.getInstance().setCanMove(false);

                //Check if the game has ended and break out of the loop if it has
                if(gameOver) break;

                // If the AI player has moves available, hand over the turn to the AI player
                if(playerHasMoves(computerPlayer)){
                    playerToMove = computerPlayer;
                    String infoString = "The AI is calculating a move...";
                    String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                    View.getInstance().updateInfoPane(infoString, scoreString);
                }else System.out.println("Player " + computerPlayer + " has no legal moves, returning turn to " + humanPlayer);
            }
            //If the AI is the next to move
            else if(playerToMove == computerPlayer){
                // Execute an AI move
                aiMove();

                //Check if the game has ended and break out of the loop if it has
                if(gameOver) break;

                // If the Human player has moves available, hand over the turn to the human player
                if(playerHasMoves(humanPlayer)){
                    playerToMove = humanPlayer;
                    View.getInstance().setCanMove(true);
                    String infoString = "It's the your turn to move!";
                    String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
                    View.getInstance().updateInfoPane(infoString, scoreString);
                }else System.out.println("Player " + humanPlayer + " has no legal moves, returning turn to " + computerPlayer);
            }
        }
        System.out.println("Human vs AI game thread died :(");
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public void setPlayerOne(Tile tile) {
        humanPlayer = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        computerPlayer = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
        if(humanPlayer == Tile.BLACK) View.getInstance().setCanMove(true);
        else View.getInstance().setCanMove(false);
    }

    @Override
    public boolean playerMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, humanPlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, humanPlayer);
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

    @Override
    public boolean playerTwoMove(int x, int y){
        //No playerTwoMove in controller
        return false;
    }

    private boolean playerHasMoves(Tile player){
        return (model.getLegalMoves(player).size() != 0);
    }

    @Override
    public void aiMove() {
        // Generate a move made by the computer
        Point aiMove = model.computerMove(computerPlayer);
        // Execute the move, and execute hasWin() function if this was a winning move
        model.move(aiMove.x, aiMove.y, computerPlayer);
        if(model.hasWinner()){
            View.getInstance().updateBoard(model.getBoard());
            hasWin();
        }else{
            View.getInstance().updateBoard(model.getBoard());
            String infoString = "It's the player's turn to move!";
            String scoreString = "The scores are: Black - " + model.getScores()[0] + " White - " + model.getScores()[1];
            View.getInstance().updateInfoPane(infoString, scoreString);
        }
    }

    @Override
    public void hasWin() {
        gameOver = true;
        //View.getInstance().showWinScreen(model.getBoardWinner(), model.getScores()[0], model.getScores()[1]);
    }

    @Override
    public void killThread() {
        die = true;
    }
}
