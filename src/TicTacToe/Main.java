package TicTacToe;

import TicTacToe.AI_Models.MinimaxAIModel;
import TicTacToe.AI_Models.AbstractModel;
import TicTacToe.AI_Models.MinimaxAlphaBetaAIModel;
import TicTacToe.AI_Models.RandomAIModel;

public class Main {

    public static void main(String[] args) {
        // Create the view
        CommandlineView view = new CommandlineView();

        // Create a model

        // Use this model for a stupid AI that makes random moves
        //AbstractModel model = new RandomAIModel(3);

        // Use this model for a minimax AI without alpha-beta pruning
        //AbstractModel model = new MinimaxAIModel(3);

        // Use this model for a minimax AI with alpha-beta pruning
        AbstractModel model = new MinimaxAlphaBetaAIModel(3);

        // Create the controller and pass the view and model
        TictactoeController controller = new TictactoeController(view, model);
        // Set the controller for the view
        view.setController(controller);
        // Start the main game loop
        view.start();
    }
}
