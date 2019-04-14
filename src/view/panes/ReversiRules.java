package view.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.View;

public class ReversiRules extends VBox {
    public ReversiRules() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);

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
                "\t gespeeld worden. De winnaar is de speler die de meeste stenen van zijn of haar kleur op het bord heeft. \n\n\n";
        text2.setText(textMessage2);
        VBox textBox2 = new VBox();
        textBox2.getChildren().addAll(text2);
        textBox2.setAlignment(Pos.TOP_LEFT);
        textBox2.setFillWidth(true);

        Button backButton = new BackToMainButton();

        this.getChildren().addAll(text2, backButton);
    }
}
