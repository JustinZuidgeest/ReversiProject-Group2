import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;

import static java.awt.Color.GREEN;

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
        VBox centerMenu = new VBox(1);
        centerMenu.setAlignment(Pos.CENTER);
        centerMenu.setPadding(new Insets(5));

        Button button1 = new Button("Reversi");
        button1.setMaxWidth(400);
        button1.setMinHeight(100);
        button1.getStylesheets().add("style.css");
        Button button2 = new Button("Tic Tac Toe");
        button2.setMaxWidth(400);
        button2.setMinHeight(100);
        Button button3 = new Button("Rules");
        button3.setMaxWidth(400);
        button3.setMinHeight(100);
        Button button4 = new Button("Quit");
        button4.setMaxWidth(400);
        button4.setMinHeight(100);

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
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.show();


        //Reversi

        Button quitReversi = new Button("Quit game");
        quitReversi.setOnAction(e -> window.setScene(scene));

        Group tileGroup1 = new Group();
        final int TILE_SIZE = 10;
        final int WIDTH = 8;
        final int HEIGHT = 8;
        Pane checkers2 = new Pane();
        checkers2.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        checkers2.getChildren().addAll(tileGroup1);

        for (int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                Tile tile = new Tile((x + y) % 2 == 0, x , y);

                tileGroup1.getChildren().add(tile);
            }
        }

        VBox leftMenuR = new VBox(10);
        leftMenuR.getChildren().addAll(quitReversi, checkers2);

        BorderPane borderPaneRev = new BorderPane();
        borderPaneRev.setLeft(leftMenuR);
        borderPaneRev.setCenter(checkers2);

        scene1 = new Scene(borderPaneRev, 1000, 700);
        scene1.getStylesheets().addAll("style.css");

        //Quit Tic Tac Toe
        Button quitTTT = new Button("Quit game");
        quitTTT.setOnAction(e -> window.setScene(scene));

        VBox leftMenuT = new VBox(10);
        leftMenuT.getChildren().addAll(quitTTT);



        Group tileGroup = new Group();
            Pane checkers = new Pane();
            checkers.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
            checkers.getChildren().addAll(tileGroup);

            for (int y = 0; y < HEIGHT; y++){
                for(int x = 0; x < WIDTH; x++){
                    Tile tile = new Tile((x + y) % 2 == 0, x , y);

                    tileGroup.getChildren().add(tile);
                }
            }


        BorderPane borderPaneTTT = new BorderPane();
        borderPaneTTT.setLeft(leftMenuT);
        borderPaneTTT.setCenter(checkers);

        scene2 = new Scene(borderPaneTTT, 1000, 700);
        scene2.getStylesheets().addAll("style.css");


        //Quit rules
        Button quitRules = new Button("Menu");
        Button reversiRules = new Button("Reversi rules");
        reversiRules.setMinWidth(200);
        reversiRules.setMinHeight(100);
        Button tictactoeRules = new Button("Tic Tac Toe rules");
        tictactoeRules.setMinWidth(200);
        tictactoeRules.setMinHeight(100);
        quitRules.setOnAction(e -> window.setScene(scene));
        reversiRules.setOnAction(e -> window.setScene(revsceneRegels));
        tictactoeRules.setOnAction(e -> window.setScene(tttsceneRegels));

        VBox leftMenuRules = new VBox();
        leftMenuRules.getChildren().addAll(quitRules);

        HBox centerMenuRules = new HBox();
        centerMenuRules.getChildren().addAll(reversiRules, tictactoeRules);


        BorderPane borderPaneRules = new BorderPane();
        borderPaneRules.setLeft(leftMenuRules);
        borderPaneRules.setCenter(centerMenuRules);
        centerMenuRules.setAlignment(Pos.CENTER);

        scene3 = new Scene(borderPaneRules, 1000, 700);
        scene3.getStylesheets().addAll("style.css");

        //reversi regels
        Button backtoRules = new Button("Back to rules");
        backtoRules.setOnAction(e -> {
            window.setScene(scene3);
        });
        HBox topBox = new HBox();
        topBox.getChildren().addAll(backtoRules);


        Text text2 = new Text();
        String textMessage2 = "\n\t Dit zijn de regels van Reversi: \n\n" +
                "\t Reversi wordt meestal gespeeld op een bord van 8 bij 8 vakjes. Er wordt gespeeld met 64 stenen die elk \n" +
                "\t een witte en een zwarte zijde hebben. Men spreekt over witte en zwarte stenen, en daarmee bedoelt men \n" +
                "\t stenen waarvan de witte respectievelijk zwarte zijde boven ligt. Een speler speelt wit, de ander zwart. \n" +
                "\t Zwart begint. In de beginpositie zijn de vier velden in het centrum bezet, met een witte steen op d4 en \n" +
                "\t e5, en een zwarte steen op e4 en d5. Men kan ook besluiten te beginnen met zwarte stenen op d4 en e4, \n" +
                "\t en witte stenen op d5 en e5. Staat men dit toe, dan mag de witspeler beslissen wat de beginstelling is, \n" +
                "\t waarna zwart de eerste zet doet. Deze beginstelling wordt echter op geen enkel officieel toernooi gebruikt.\n" +
                "\t Een zet bestaat uit het neerleggen van een steen, met de eigen kleur boven, op een leeg veld. Alle stenen \n" +
                "\t of series van stenen van de kleur van de tegenstander die tussen deze steen en een steen van de eigen kleur \n" +
                "\t liggen (horizontaal, verticaal of schuin), worden omgedraaid. Men mag alleen een steen neerleggen indien \n" +
                "\t daardoor minstens één steen wordt omgedraaid. Kan men dat niet, dan slaat men een beurt over. Kan men wel \n" +
                "\t een zet doen, dan is dat verplicht. Het spel is voorbij als er geen stenen meer neergelegd kunnen worden, \n" +
                "\t meestal doordat het bord vol is. Het spel duurt dus maximaal 60 zetten en kan binnen een paar minuten \n" +
                "\t gespeeld worden. De winnaar is de speler die de meeste stenen van zijn of haar kleur op het bord heeft. \n\n\n" +
                "\t referentie: https://nl.wikipedia.org/wiki/Reversi";
        text2.setText(textMessage2);
        VBox textBox2 = new VBox();
        textBox2.getChildren().addAll(text2);
        textBox2.setAlignment(Pos.TOP_LEFT);
        textBox2.setFillWidth(true);

        BorderPane revRBP = new BorderPane();
        revRBP.setTop(topBox);
        revRBP.setCenter(textBox2);

        revsceneRegels = new Scene(revRBP, 1000, 700);
        revsceneRegels.getStylesheets().addAll("style.css");

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
        tttsceneRegels.getStylesheets().addAll("style.css");


    }

    private void closeProgram(){
        boolean result = ConfirmQuit.display("Quit", "Are you sure you want to quit?");
        if(result){
            window.close();
        }
    }
}