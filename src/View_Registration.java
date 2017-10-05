import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View_Registration {

    private Scene dentistReg;
    private String title = "Register";

    public View_Registration(Controller c){
        GridPane regLayout = new GridPane();
        regLayout.setPadding(new Insets(15,15,15,15));
        regLayout.setVgap(5); regLayout.setHgap(3);

        dentistReg = new Scene(regLayout,350,250);

        Label regName = new Label("Name: ");
        TextField regNameField = new TextField();
        Label regAddr = new Label("Address: ");
        TextField regAddrField = new TextField();
        Label regPwd = new Label("Password: ");
        PasswordField regPwdField = new PasswordField();
        Label confRegPwd = new Label("Confirm Password: ");
        PasswordField confRegPwdField = new PasswordField();
        Button register = new Button("Register");
        Text regError = new Text(); regError.setFont(new Font(15)); regError.setFill(Color.RED);
        Button cancel = new Button("Cancel");

        HBox regSceneButtons = new HBox(47);
        regSceneButtons.getChildren().addAll(register, cancel);

        regLayout.add(regName, 0, 0);
        regLayout.add(regNameField, 1, 0);
        regLayout.add(regAddr, 0, 1);
        regLayout.add(regAddrField, 1, 1);
        regLayout.add(regPwd, 0, 2);
        regLayout.add(regPwdField, 1, 2);
        regLayout.add(confRegPwd, 0, 3);
        regLayout.add(confRegPwdField, 1, 3);
        regLayout.add(regSceneButtons, 1, 4);
        regLayout.add(regError, 1, 5);
        regLayout.setAlignment(Pos.CENTER);

        register.setOnAction(e -> c.register(regError, regNameField, regAddrField, confRegPwdField, confRegPwdField));
        cancel.setOnAction(e -> c.cancelRegistration());
    }

    public Scene getScene(){
        return dentistReg;
    }
    public String getTitle(){
        return title;
    }
}