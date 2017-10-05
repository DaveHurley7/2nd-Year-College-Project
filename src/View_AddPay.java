import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class View_AddPay {

    private Scene scene;
    private String title = "Add Payment";
    private int pIndex, iIndex;

    public View_AddPay(Controller c){
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(15,15,15,15));
        pane.setVgap(5); pane.setHgap(3);
        scene = new Scene(pane,320,150);

        Label amount = new Label("Amount");
        TextField amountInput = new TextField();
        Button addPay = new Button("Add");
        Button cancel = new Button("Cancel");
        HBox buttonField = new HBox(23);
        buttonField.getChildren().addAll(addPay,cancel);
        Text error = new Text(); error.setFont(new Font(15)); error.setFill(Color.RED);

        pane.add(amount,0,0);
        pane.add(amountInput,1,0);
        pane.add(buttonField,1,1);
        pane.add(error,1,2);

        addPay.setOnAction(e -> c.createPayment(amountInput,error,pIndex,iIndex));
        cancel.setOnAction(e -> c.closePayWindow());
    }

    public Scene getScene(){
        return scene;
    }

    public String getTitle(){
        return title;
    }

    public void setRootIndexes(int pIn, int iIn){
        pIndex = pIn;
        iIndex = iIn;
    }

}