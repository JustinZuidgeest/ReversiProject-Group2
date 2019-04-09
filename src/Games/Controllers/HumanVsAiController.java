package Games.Controllers;

import Games.Controller;
import Games.Model;
import Games.Tile;
import Games.View;

import java.awt.Point;
import java.util.Scanner;

public class HumanVsAiController implements Controller {

    private View view;
    private Model model;

    private boolean gameOver;
    private Tile playerToMove;
    // The human player
    private Tile humanPlayer;
    // The AI player
    private Tile computerPlayer;

    private int boardSize;

    public HumanVsAiController(View view, Model model) {
        this.view = view;
        this.model = model;
        this.boardSize = model.getBoardSize();
    }

    @Override
    public void newGame() {
        model.resetBoard();
        // Black (X) moves first
        playerToMove = Tile.BLACK;
        humanPlayer = null;
        computerPlayer = null;
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
            Tile human = (player.toUpperCase().startsWith("X")) ? Tile.BLACK : Tile.WHITE;
            setPlayerOne(human);
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
                    if(playerHasMoves(computerPlayer)){
                        playerToMove = computerPlayer;
                    }else System.out.println("Player " + computerPlayer + " has no legal moves, returning turn to " + humanPlayer);
                }
                else if(playerToMove == computerPlayer){
                    aiMove();
                    if (gameOver) break;
                    if(playerHasMoves(humanPlayer)){
                        playerToMove = humanPlayer;
                    }else System.out.println("Player " + humanPlayer + " has no legal moves, returning turn to " + computerPlayer);
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
        humanPlayer = (tile == Tile.BLACK) ? Tile.BLACK : Tile.WHITE;
        computerPlayer = (tile == Tile.BLACK) ? Tile.WHITE : Tile.BLACK;
    }

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
            view.updateBoard(model.getBoard());
            hasWin();
        }else{
            view.updateBoard(model.getBoard());
            System.out.println("The scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
        }
    }

    @Override
    public void hasWin() {
        view.printWinner(model.getBoardWinner());
        System.out.println("The final scores are White: " + model.getScores()[0] + ", Black: " + model.getScores()[1]);
        gameOver = true;
    }
}
