package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.View;

import java.awt.Point;
import java.util.Scanner;

public class HumanVsRemoteController implements Controller {

    private View view;
    private Model model;

    private boolean gameOver;
    private Tile playerToMove;
    private ServerCommunicator server;
    // The human player
    private Tile humanPlayer;
    // The AI player
    private Tile remotePlayer;
    private int boardSize;

    public HumanVsRemoteController(View view, Model model) {
        this.view = view;
        this.model = model;
        this.server = new ServerCommunicator(this);

        this.boardSize = model.getBoardSize();

        server.connectToServer();
        new Thread(server).start();
        server.login();
        server.getGameList();

        server.subscribe(model.getGameName());
        newGame();
    }

    @Override
    public void start(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            gameOver = false;
            newGame();
            System.out.println("-------Welcome to " + model.getGameName() + "-------");
            System.out.println("Please select your side: X or O");
            String player = scanner.nextLine();
            Tile player1 = (player.toUpperCase().startsWith("X")) ? Tile.BLACK : Tile.WHITE;
            setPlayerOne(player1);
            playerToMove = player1;
            while (true) {
                if(playerToMove == humanPlayer){
                    int userX;
                    int userY;
                    try {
                        System.out.print("x coordinate: ");
                        userX = Integer.parseInt(scanner.nextLine());
                        System.out.print("y coordinate: ");
                        userY = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a number");
                        continue;
                    }
                    // Check if the player entered a correct move
                    if (!playerMove(userX, userY)) continue;
                    if (gameOver) break;
                    if(playerHasMoves(remotePlayer)){
                        playerToMove = remotePlayer;
                    }else System.out.println("Player " + remotePlayer + " has no legal moves, returning turn to " + humanPlayer);
                }
                else if(playerToMove == remotePlayer){
                    System.out.println("----------------waiting for opponent---------------");

                    // Check if the player entered a correct move
                    //if (!remoteMove(userX, userY)) continue;
                    if (gameOver) break;
                    if(playerHasMoves(humanPlayer)){
                        playerToMove = humanPlayer;
                    }else System.out.println("Player " + humanPlayer + " has no legal moves, returning turn to " + remotePlayer);
                }
            }
            System.out.println("Game has ended, new game? y/n");
            String input = scanner.nextLine();
            if(input.toUpperCase().startsWith("N")) break;
        }
    }

    private boolean playerHasMoves(Tile player){
        return (model.getLegalMoves(player).size() != 0);
    }

    /**
     * Reset the game board back to its empty state
     */
    @Override
    public void newGame() {
        model.resetBoard();
        humanPlayer = null;
        remotePlayer = null;
        view.updateBoard(model.getBoard());
    }

    @Override
    public void setPlayerOne(Tile tile) {
        humanPlayer = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
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
            if(model.checkLegalMove(x, y, humanPlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, humanPlayer);
                if(model.hasWinner()){
                    view.updateBoard(model.getBoard());
                    hasWin();
                }else{
                    view.updateBoard(model.getBoard());
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

    public boolean remoteMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, remotePlayer)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, remotePlayer);
                if(model.hasWinner()){
                    view.updateBoard(model.getBoard());
                    hasWin();
                }else{
                    view.updateBoard(model.getBoard());
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
