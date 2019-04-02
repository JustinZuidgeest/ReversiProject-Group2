package TicTacToe;

import java.util.Scanner;

public class CommandlineView {

    private TictactoeController controller;
    private boolean gameOver;

    public void start(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            gameOver = false;
            controller.newGame();
            System.out.println("-------Welcome to TicTacToe-------");
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
