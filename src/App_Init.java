import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;

public class App_Init extends Application{

    boolean appRun = false;
    DataManager dataManager;
    public static void main(String[] args){
        launch(args);
    }

    public void init(){
        dataManager = new DataManager();
        if(dataManager.startDatabase(this)){
            appRun = true;
        }
    }
    public void start(Stage s){
        if(appRun){
            Controller c = new Controller(s,dataManager);
            View_Login login_view = new View_Login(c);
            View_Registration reg_view = new View_Registration(c);
            View_Patients pat_view = new View_Patients(c);
            View_ProcPay procpay_view = new View_ProcPay(c);
            View_AddPatient add_pat = new View_AddPatient(c);
            View_AddEditProc proc_manage_view = new View_AddEditProc(c);
            View_Report rep_view = new View_Report(c);
            View_AddPay pay_view = new View_AddPay(c);
            c.declareViews(login_view, reg_view, pat_view, procpay_view, add_pat, proc_manage_view, rep_view, pay_view);
            s.setScene(login_view.getScene());
            s.setTitle(login_view.getTitle());
            s.show();
        }else{
            System.exit(0);
        }
    }

    public void stop(){
        dataManager.shutdownDatabase();
    }
}