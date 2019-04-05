import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.event.*;

public class Main extends Application {

    Stage window;
    Button button;
    Scene scene, scene1, scene2, scene3, tttsceneRegels, revsceneRegels;

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

        button1.setOnAction(e -> {
            window.setScene(scene1);
        });
        button2.setOnAction(e -> {
            window.setScene(scene2);
        });
        button3.setOnAction(e -> {
            window.setScene(scene3);
        });
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

        VBox leftMenuR = new VBox(10);
        leftMenuR.getChildren().addAll(quitReversi);

        BorderPane borderPaneRev = new BorderPane();
        borderPaneRev.setLeft(leftMenuR);

        scene1 = new Scene(borderPaneRev, 1000, 700);

        //Quit Tic Tac Toe
        Button quitTTT = new Button("Quit game");
        quitTTT.setOnAction(e -> window.setScene(scene));

        VBox leftMenuT = new VBox(10);
        leftMenuT.getChildren().addAll(quitTTT);

        BorderPane borderPaneTTT = new BorderPane();
        borderPaneTTT.setLeft(leftMenuT);

        scene2 = new Scene(borderPaneTTT, 1000, 700);


        //Quit rules
        Button quitRules = new Button("Menu");
        Button reversiRules = new Button("Reversi rules");
        Button tictactoeRules = new Button("Tic Tac Toe rules");
        quitRules.setOnAction(e -> window.setScene(scene));
        reversiRules.setOnAction(e -> window.setScene(revsceneRegels));
        tictactoeRules.setOnAction(e -> window.setScene(tttsceneRegels));

        VBox leftMenuRules = new VBox(10);
        leftMenuRules.getChildren().addAll(quitRules);

        HBox centerMenuRules = new HBox(10);
        centerMenuRules.getChildren().addAll(reversiRules, tictactoeRules);


        BorderPane borderPaneRules = new BorderPane();
        borderPaneRules.setLeft(leftMenuRules);
        borderPaneRules.setCenter(centerMenuRules);
        centerMenuRules.setAlignment(Pos.CENTER);

        scene3 = new Scene(borderPaneRules, 1000, 700);

        //reversi regels
        Button backtoRules = new Button("Back to rules");
        backtoRules.setOnAction(e -> {
            window.setScene(scene3);
        });

        VBox rulesboxReversi = new VBox(10);
        rulesboxReversi.getChildren().addAll(backtoRules);

        Text text2 = new Text();
        String textMessage2 = "Dit zijn de regels van Reversi";
        text2.setText(textMessage2);
        VBox textBox2 = new VBox();
        textBox2.getChildren().addAll(text2);
        textBox2.setAlignment(Pos.CENTER);

        BorderPane revRBP = new BorderPane();
        revRBP.setLeft(rulesboxReversi);
        revRBP.setCenter(textBox2);
        revsceneRegels = new Scene(revRBP, 1000, 700);

        //tictactoe regels
        Button backtoRules2 = new Button("Back to rules");
        backtoRules2.setOnAction(e -> {
            window.setScene(scene3);
        });

        VBox rulesboxTTT = new VBox(10);
        rulesboxTTT.getChildren().addAll(backtoRules2);

        Text text = new Text();
        String textMessage = "Dit zijn de regels van Tic Tac Toe";
        text.setText(textMessage);
        VBox textBox = new VBox();
        textBox.getChildren().addAll(text);
        textBox.setAlignment(Pos.CENTER);

        BorderPane tttRBP = new BorderPane();
        tttRBP.setLeft(rulesboxTTT);
        tttRBP.setCenter(textBox);
        tttsceneRegels = new Scene(tttRBP, 1000, 700);


    }

    private void closeProgram(){
        boolean result = ConfirmQuit.display("Quit", "Are you sure you want to quit?");
        if(result){
            window.close();
        }
    }
}