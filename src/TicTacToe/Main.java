package TicTacToe;

public class Main {

    public static void main(String[] args) {
        CommandlineView view = new CommandlineView();
        TictactoeModel model = new TictactoeModel();
        TictactoeController controller = new TictactoeController(view, model);
        view.setController(controller);

        view.start();
    }
}
