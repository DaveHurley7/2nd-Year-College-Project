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

public class View_AddEditProc {

    private Scene scene;
    private String title_add = "Add Procedure";
    private String title_edit = "Edit Procedure";
    private Button addProc;
    private Button updateProc;
    private Button cancel;
    private HBox buttonField;
    private TextField nameField, costField;
    private Procedure procedure;
    private int pIndex, iIndex;

    public View_AddEditProc(Controller c){
        GridPane fieldPane = new GridPane();
        fieldPane.setPadding(new Insets(15,15,15,15));
        fieldPane.setVgap(5); fieldPane.setHgap(3);
        scene = new Scene(fieldPane, 320, 150);


        Label name = new Label("Name: ");
        nameField = new TextField();
        Label cost = new Label("Cost: ");
        costField = new TextField();
        Text error = new Text(); error.setFont(new Font(15)); error.setFill(Color.RED);

        addProc = new Button("Add");
        updateProc = new Button("Update");
        buttonField = new HBox(23);
        cancel = new Button("Cancel");

        fieldPane.add(name,0,0);
        fieldPane.add(nameField,1,0);
        fieldPane.add(cost,0,1);
        fieldPane.add(costField,1,1);
        fieldPane.add(buttonField,1,2);
        fieldPane.add(error,1,3);

        addProc.setOnAction(e -> c.createProcedure(nameField,costField,error,pIndex,iIndex));
        updateProc.setOnAction(e -> c.updateProcedure(nameField,costField,error,procedure,pIndex,iIndex));
        cancel.setOnAction(e -> c.closeProcWindow());
    }

    public Scene getScene(){
        return scene;
    }
    public String getTitleAdd(){
        return title_add;
    }
    public String getTitleEdit(){
        return title_edit;
    }

    public void setAddMode(){
        buttonField.getChildren().clear();
        buttonField.getChildren().addAll(addProc,cancel);
    }

    public void setEditMode(Procedure proc){
        procedure = proc;
        buttonField.getChildren().clear();
        buttonField.getChildren().addAll(updateProc,cancel);
        nameField.setText(procedure.getProcName());
        costField.setText(""+procedure.getProcCost());
    }

    public void setRootIndexes(int pIn, int iIn){
        pIndex = pIn;
        iIndex = iIn;
    }

}