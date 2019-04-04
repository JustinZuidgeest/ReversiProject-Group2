import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.*;

public class Main extends Application {

    Stage window;
    Button button;
    Scene scene, scene1, scene2, scene3;

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
        VBox centerMenu = new VBox(10);
        centerMenu.setAlignment(Pos.CENTER);

        Button button1 = new Button("Reversi");
        Button button2 = new Button("Tic Tac Toe");
        Button button3 = new Button("Rules");
        Button button4 = new Button("Quit");

        centerMenu.getChildren().addAll(button1, button2, button3, button4);

        button1.setOnAction(e -> window.setScene(scene1));
        button2.setOnAction(e -> window.setScene(scene2));
        button3.setOnAction(e -> window.setScene(scene3));
        button4.setOnAction(e -> closeProgram());

        //main menu window
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerMenu);

        scene = new Scene(borderPane, 1000, 700);
        window.setScene(scene);
        window.show();


        //Reversi

        Button quitReversi = new Button("Quit game");
        quitReversi.setOnAction(e -> window.setScene(scene));
        window.setTitle("Reversi");

        VBox leftMenuR = new VBox(10);
        leftMenuR.getChildren().addAll(quitReversi);

        BorderPane borderPaneRev = new BorderPane();
        borderPaneRev.setLeft(leftMenuR);

        scene1 = new Scene(borderPaneRev, 1000, 700);

        //Quit Tic Tac Toe
        Button quitTTT = new Button("Quit game");
        quitTTT.setOnAction(e -> window.setScene(scene));
        window.setTitle("Tic Tac Toe");

        VBox leftMenuT = new VBox(10);
        leftMenuT.getChildren().addAll(quitTTT);

        BorderPane borderPaneTTT = new BorderPane();
        borderPaneTTT.setLeft(leftMenuT);

        scene2 = new Scene(borderPaneTTT, 1000, 700);


        //Quit rules
        Button quitRules = new Button("Menu");
        quitRules.setOnAction(e -> window.setScene(scene));
        window.setTitle("Rules");

        VBox leftMenuRules = new VBox(10);
        leftMenuRules.getChildren().addAll(quitRules);

        BorderPane borderPaneRules = new BorderPane();
        borderPaneRules.setLeft(leftMenuRules);

        scene3 = new Scene(borderPaneRules, 1000, 700);


    }

    private void closeProgram(){
        boolean result = ConfirmQuit.display("Quit", "Are you sure you want to quit?");
        if(result){
            window.close();
        }
    }
}