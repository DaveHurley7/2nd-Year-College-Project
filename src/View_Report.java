import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class View_Report {

    private Scene scene;
    private String title_pat = "Patient Details";
    private String title_pay = "Payments Due";
    private TextArea repText;

    public View_Report(Controller c){
        VBox main = new VBox();
        AnchorPane buttons = new AnchorPane();
        Button back = new Button("Back");
        Button fileOutput = new Button("Output to a file");
        scene = new Scene(main,400,500);
        buttons.getChildren().addAll(back,fileOutput);
        AnchorPane.setTopAnchor(back,2.0);
        AnchorPane.setLeftAnchor(back,2.0);
        AnchorPane.setRightAnchor(fileOutput,2.0);
        repText = new TextArea();
        repText.setPrefHeight(scene.getHeight()-buttons.getHeight());
        main.getChildren().addAll(buttons,repText);

        back.setOnAction(e -> c.goToPatientView());
        fileOutput.setOnAction(e -> c.exportReport(repText));
    }

    public Scene getScene(){
        return scene;
    }

    public String getTitlePat(){
        return title_pat;
    }

    public String getTitlePay(){
        return title_pay;
    }

    public TextArea getRepText(){
        return repText;
    }

}