package TicTacToe;

import java.util.Scanner;

public class CommandlineView {

    private TictactoeController controller;
    private boolean gameOver;

    /**
     * Main game loop. Allows the player to enter coordinates for a move, followed by the AI making a move.
     * Continues until the game is won, at which point a new game can be started
     */
    public void start(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            gameOver = false;
            controller.newGame();
            System.out.println("-------Welcome to TicTacToe-------");
            System.out.println("Please select your symbol: X or O");
            String player = scanner.nextLine();
            Symbol playerOne = (player.toUpperCase().startsWith("X")) ? Symbol.X : Symbol.O;
            controller.setPlayerOne(playerOne);
            if(playerOne != Symbol.X) controller.aiMove();
            while (true) {
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
                if (!controller.playerMove(userX, userY)) continue;
                if (gameOver) break;
                controller.aiMove();
                if (gameOver) break;
            }
            System.out.println("Game has ended, new game? y/n");
            String input = scanner.nextLine();
            if(input.toUpperCase().startsWith("N")) break;
        }
    }

    /**
     * Prints the current state of the game board on the console
     * @param board The game board
     */
    public void printBoard(Symbol[][] board){
        System.out.println("Current board:");
        for(Symbol[] row:board){
            for(Symbol column:row){
                String current = (column == Symbol.X) ? "X" : (column == Symbol.O) ? "O" : ".";
                System.out.print(current + "  ");
            }
            System.out.println();
        }
    }

    /**
     * Prints a message that the game has been won
     * @param player The Symbol object that represents the winning player
     */
    public void printWinner(Symbol player){
        if(player == Symbol.X) System.out.println("The X player has won!");
        else if(player == Symbol.O) System.out.println("The O player has won!");
        else if(player == Symbol.EMPTY) System.out.println("It's a draw!");
    }

    public void setController(TictactoeController controller) {
        this.controller = controller;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
