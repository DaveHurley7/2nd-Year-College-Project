import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class View_Patients {

    private ListView<String> patientList;
    private ListView<String> invoiceList;
    private Scene patientDetails;
    private String title = "Patients";
    private Button showProcs, showPays;
    private MenuItem addInv, removePat;

    public View_Patients(Controller c){
        BorderPane mainLayout = new BorderPane(); //Home Layout Pane
        patientDetails = new Scene(mainLayout, 900,400);

        //Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem logout = new MenuItem("Logout");

        Menu patMenu = new Menu("Patient");
        MenuItem addPat = new MenuItem("Add");
        removePat = new MenuItem("Remove");

        Menu procMenu = new Menu("Invoice");
        addInv = new MenuItem("Add");

        Menu repMenu = new Menu("Report");
        MenuItem patRep = new MenuItem("Patient Details");
        MenuItem payOwe = new MenuItem("Payments Due");

        fileMenu.getItems().addAll(saveFile, logout);
        patMenu.getItems().addAll(addPat,removePat);
        procMenu.getItems().add(addInv);
        repMenu.getItems().addAll(patRep,payOwe);

        //Menu Bar Actions
        saveFile.setOnAction(e -> c.saveFile());
        logout.setOnAction(e -> c.logout());

        addPat.setOnAction(e -> c.newPatient());
        removePat.setOnAction(e -> c.removePatient());
        removePat.setDisable(true);

        addInv.setDisable(true);
        addInv.setOnAction(e -> c.addInvoice());

        patRep.setOnAction(e -> c.generatePatientReport());
        payOwe.setOnAction(e -> c.generatePaymentReport());

        menuBar.getMenus().addAll(fileMenu,patMenu,procMenu,repMenu);


        //ListViews
        patientList = new ListView<>();
        invoiceList = new ListView<>();

        showProcs = new Button("Show Procedures");
        showPays = new Button("Show Payments");
        showProcs.setDisable(true);
        showPays.setDisable(true);
        showProcs.setPrefWidth(patientDetails.getWidth()*1/5);
        showPays.setPrefWidth(patientDetails.getWidth()*1/5);

        HBox buttonPane = new HBox(3);
        buttonPane.getChildren().addAll(showProcs,showPays);

        showProcs.setOnAction(e -> c.showProcedures());
        showPays.setOnAction(e -> c.showPayments());

        patientList.setPrefWidth(patientDetails.getWidth()*3/5);
        invoiceList.setPrefWidth(patientDetails.getWidth()*2/5);

        HBox listPane = new HBox(0);
        listPane.getChildren().add(patientList);

        VBox sidePane = new VBox(1);
        sidePane.getChildren().addAll(buttonPane,invoiceList);
        listPane.getChildren().add(sidePane);

        patientList.setOnMouseClicked(e -> c.getPatientInvoices());
        invoiceList.setOnMouseClicked(e -> c.enableShowButtons());

        invoiceList.getItems().add("Select a patient to view their invoices");

        mainLayout.setTop(menuBar);
        mainLayout.setCenter(listPane);
    }

    public Scene getScene(){
        return patientDetails;
    }

    public String getTitle(){
        return title;
    }

    public ListView<String> getPatientList(){
        return patientList;
    }

    public ListView<String> getInvoiceList(){
        return invoiceList;
    }

    public Button getShowProcs(){
        return showProcs;
    }

    public Button getShowPays(){
        return showPays;
    }

    public MenuItem getAddInv(){
        return addInv;
    }

    public MenuItem getRemovePat(){
        return removePat;
    }

}