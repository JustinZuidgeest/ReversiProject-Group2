import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class Main extends Application {

    private Stage window;
    private Scene mainMenuScene, reversiScene, ticTacToeScene, rulesScene, rulesScene2, tttsceneRegels, revsceneRegels;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Arithm Games");

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        //menu center layout
        VBox centerMenu = new VBox(1);
        centerMenu.setAlignment(Pos.CENTER);
        centerMenu.setPadding(new Insets(5));

        Button reversiButton = new Button("Reversi");
        reversiButton.setMaxWidth(400);
        reversiButton.setMinHeight(100);
        reversiButton.getStylesheets().add("style.css");
        Button ticTacToeButton = new Button("Tic Tac Toe");
        ticTacToeButton.setMaxWidth(400);
        ticTacToeButton.setMinHeight(100);
        Button rulesButton = new Button("Rules");
        rulesButton.setMaxWidth(400);
        rulesButton.setMinHeight(100);
        Button quitWindowButton = new Button("Quit");
        quitWindowButton.setMaxWidth(400);
        quitWindowButton.setMinHeight(100);

        centerMenu.getChildren().addAll(reversiButton, ticTacToeButton, rulesButton, quitWindowButton);

        reversiButton.setOnAction(e -> {
            window.setScene(reversiScene);
        });
        ticTacToeButton.setOnAction(e -> {
            window.setScene(ticTacToeScene);
        });
        rulesButton.setOnAction(e -> {
            window.setScene(rulesScene);
        });
        quitWindowButton.setOnAction(e -> closeProgram());

        //main menu window
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerMenu);

        mainMenuScene = new Scene(borderPane, 1000, 700);
        mainMenuScene.getStylesheets().add("style.css");


        window.setScene(mainMenuScene);
        window.show();



        reversiScene = new RevToGame().getScene(window, mainMenuScene);
        ticTacToeScene = new TttToGame().getScene(window, mainMenuScene);

        rulesScene = new RulesMenu().getScene(window, mainMenuScene, revsceneRegels = new ReversiRegels().getScene(window), tttsceneRegels = new ticTacToeRuleScene().getScene(window));

        




    }

    public void closeProgram(){
        boolean result = ConfirmQuit.display("Quit", "Are you sure you want to quit?");
        if(result){
            window.close();
        }
    }
}