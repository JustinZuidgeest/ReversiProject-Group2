import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ScalableBoard extends Application{
    public void start(Stage primaryStage){
        int boardSize = 8;

        Pane pane = new Pane();
        pane.setPrefSize(600, 600);

        for (int i=0; i<boardSize; i++){
            for (int j=0; j<boardSize; j++){
                //Rectangle box = new Rectangle(j*width, i*height, width, height);
                Rectangle box = new Rectangle();
                box.widthProperty().bind(pane.widthProperty().divide(boardSize));
                box.heightProperty().bind(pane.heightProperty().divide(boardSize));
                box.xProperty().bind(pane.widthProperty().divide(boardSize).multiply(j));
                box.yProperty().bind(pane.heightProperty().divide(boardSize).multiply(i));
                if((i%2==0 && j%2==1) || (i%2==1 && j%2==0)){
                    box.setFill(Color.WHITE);
                }else{
                    box.setFill(Color.BLACK);
                }
                pane.getChildren().add(box);
            }
        }

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checkerboard");
        primaryStage.show();
    }
}
