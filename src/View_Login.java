import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View_Login{

    private Scene dentistLogin;
    private String title = "Login";

    public View_Login(Controller c){
        GridPane loginLayout = new GridPane(); //Login Layout Pane
        loginLayout.setPadding(new Insets(15));
        loginLayout.setVgap(5); loginLayout.setHgap(3);

        dentistLogin = new Scene(loginLayout,370,150);

        Label loginName = new Label("Name: ");
        TextField loginNameField = new TextField();
        Label loginPwd = new Label("Password: ");
        PasswordField loginPwdField = new PasswordField();
        Button login = new Button("Login");
        Button noAccount = new Button("No Account? Register");
        HBox homeButtonLayout = new HBox(25); //Login and register buttons
        Text loginError = new Text(); loginError.setFont(new Font(15)); loginError.setFill(Color.RED);

        homeButtonLayout.getChildren().addAll(login,noAccount);

        loginLayout.add(loginName, 0, 0);
        loginLayout.add(loginNameField, 1, 0);
        loginLayout.add(loginPwd, 0, 1);
        loginLayout.add(loginPwdField, 1, 1);
        loginLayout.add(homeButtonLayout, 1, 2);
        loginLayout.add(loginError, 1, 3);
        loginLayout.setAlignment(Pos.CENTER);

        login.setOnAction(e -> c.login(loginError, loginNameField, loginPwdField));
        noAccount.setOnAction(e -> c.showRegistration());
    }

    public Scene getScene(){
        return dentistLogin;
    }
    public String getTitle(){
        return title;
    }

}