package Games.Reversi;

import Games.Tile;
import Games.View;

public class ReversiCommandlineView implements View {

    /**
     * Prints the current state of the game board on the console
     * @param board The game board
     */
    @Override
    public void updateBoard(Tile[][] board){
        System.out.println("Current board:");
        System.out.print("  ");
        for(int i=0;i < board.length; i++){
            System.out.print(i + "  ");
        }
        System.out.println();
        int i =0;
        for(Tile[] row:board){
            System.out.print(i + " ");
            i++;
            for(Tile column:row){
                String current = (column == Tile.BLACK) ? "X" : (column == Tile.WHITE) ? "O" : ".";
                System.out.print(current + "  ");
            }
            System.out.println();
        }
    }

    @Override
    public void printWinner(Tile player) {
        if(player == Tile.BLACK) System.out.println("The Black color has won!");
        else if(player == Tile.WHITE) System.out.println("The White color has won!");
        else if(player == Tile.EMPTY) System.out.println("It's a draw!");
    }
}
