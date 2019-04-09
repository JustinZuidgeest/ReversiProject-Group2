package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.View;

import java.util.Scanner;

public class HumanVsHumanController implements Controller {

    private View view;
    private Model model;

    private boolean gameOver;
    private Tile playerToMove;
    // The human player
    private Tile playerOne;
    // The AI player
    private Tile playerTwo;

    private int boardSize;

    public HumanVsHumanController(View view, Model model) {
        this.view = view;
        this.model = model;
        this.boardSize = model.getBoardSize();
    }

    @Override
    public void newGame() {
        model.resetBoard();
        // Black (X) moves first
        playerToMove = Tile.BLACK;
        playerOne = null;
        playerTwo = null;
        view.updateBoard(model.getBoard());
    }

    @Override
    public void start() {
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
                if(playerToMove == playerOne){
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
                    if(playerHasMoves(playerTwo)){
                        playerToMove = playerTwo;
                    }else System.out.println("Player " + playerTwo + " has no legal moves, returning turn to " + playerOne);
                }
                else if(playerToMove == playerTwo){
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
                    if (!playerTwoMove(userX, userY)) continue;
                    if (gameOver) break;
                    if(playerHasMoves(playerOne)){
                        playerToMove = playerOne;
                    }else System.out.println("Player " + playerOne + " has no legal moves, returning turn to " + playerTwo);
                }
            }
            System.out.println("Game has ended, new game? y/n");
            String input = scanner.nextLine();
            if(input.toUpperCase().startsWith("N")) break;
        }
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public void setPlayerOne(Tile tile) {
        playerOne = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        playerTwo = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
    }

    @Override
    public boolean playerMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, playerOne)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, playerOne);
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

    public boolean playerTwoMove(int x, int y) {
        try {
            if(model.checkLegalMove(x, y, playerTwo)){
                // Execute the move, and execute hasWin() function if this was a winning move
                model.move(x, y, playerTwo);
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

    @Override
    public void aiMove(){
        //No AI in this controller
    }

    private boolean playerHasMoves(Tile player){
        return (model.getLegalMoves(player).size() != 0);
    }

    @Override
    public void hasWin() {
        view.printWinner(model.getBoardWinner());
        System.out.println("The final scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
        gameOver = true;
    }
}