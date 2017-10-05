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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class View_AddPatient {

    private Scene scene;
    private String title = "Add Patient";
    public View_AddPatient(Controller c){
        GridPane fieldPane = new GridPane();
        fieldPane.setPadding(new Insets(15,15,15,15));
        fieldPane.setVgap(5); fieldPane.setHgap(3);
        scene = new Scene(fieldPane,350,250);

        Label name = new Label("Name: ");
        TextField nameField = new TextField();
        Label addr = new Label("Address: ");
        TextField addrField = new TextField();
        Label phoneNum = new Label("Phone Number: ");
        TextField phoneNumField = new TextField();
        Label pNo = new Label("Patient Number: ");
        TextField pNoField = new TextField();
        HBox buttonPane = new HBox(73);
        Button addPatient = new Button("Add");
        Button cancel = new Button("Cancel");
        buttonPane.getChildren().addAll(addPatient,cancel);
        Text error = new Text(); error.setFont(new Font(15)); error.setFill(Color.RED);

        fieldPane.add(name,0,0);
        fieldPane.add(nameField,1,0);
        fieldPane.add(addr,0,1);
        fieldPane.add(addrField,1,1);
        fieldPane.add(phoneNum,0,2);
        fieldPane.add(phoneNumField,1,2);
        fieldPane.add(pNo,0,3);
        fieldPane.add(pNoField,1,3);
        fieldPane.add(buttonPane,1,4);
        fieldPane.add(error,1,5);
        addPatient.setOnAction(e -> c.addPatient(nameField, addrField, phoneNumField, pNoField, error));
        cancel.setOnAction(e -> c.cancelNewPatient());
    }

    public Scene getScene(){
        return scene;
    }
    public String getTitle(){
        return title;
    }

}