import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;  //Importing Joda Time to compare dates for the payment report
import org.joda.time.Months;

public class Controller{

    //Global Variables
    private Dentist dentistOn;
    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Invoice> invoices = new ArrayList<>();
    private ArrayList<Payment> payments = new ArrayList<>();
    private ArrayList<Procedure> procedures = new ArrayList<>();
    private ArrayList<String> repOut = new ArrayList<>();
    private Stage stage;
    private Stage modalStage;
    private View_Login dLogin;
    private View_Registration dReg;
    private View_Patients patView;
    private View_ProcPay prPaView;
    private View_AddPatient patAdd;
    private View_AddEditProc procAE;
    private View_Report repView;
    private View_AddPay payView;
    private DataManager dataManager;


    //Initialises a few global variables and fills the dentist array
    public Controller(Stage s, DataManager dM){
        dataManager = dM;
        stage = s;
        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);

    }

    //Links the views to global variables
    public void declareViews(View_Login l, View_Registration r, View_Patients p, View_ProcPay m, View_AddPatient a, View_AddEditProc aep, View_Report rep, View_AddPay pay){
        dLogin = l;
        dReg = r;
        patView = p;
        prPaView = m;
        patAdd = a;
        procAE = aep;
        repView = rep;
        payView = pay;
    }

    /**********************
    *Login Scene Methods
     **********************/
    public void login(Text error, TextField name, PasswordField pwd) {   //Logs in the dentist
        if (name.getText().isEmpty() || pwd.getText().isEmpty()) {
            error.setText("All fields must have a value");
        } else {
            dentistOn = dataManager.loginDentist(name,pwd);
            if(dentistOn == null){
                error.setText("Name or password is invalid");
            }else{
                int dentNo = dataManager.getDentistNo(dentistOn);
                dataManager.initArrays(dentistOn,dentNo);
                fillPatientView();
                stage.setScene(patView.getScene());
                stage.setTitle(patView.getTitle());
            }

        }
    }

    public void showRegistration(){  //Changes to registration scene
        stage.setScene(dReg.getScene());
        stage.setTitle(dReg.getTitle());
    }

    /****************************
     * Registration Scene Methods
     *****************************/

    public void register(Text error, TextField name, TextField addr, PasswordField pwd, PasswordField confpwd){   //Registers a new dentist
        if(name.getText().isEmpty() || addr.getText().isEmpty() || pwd.getText().isEmpty() || confpwd.getText().isEmpty()){
            error.setText("All fields must have a value");
        }else {
            if (pwd.getText().equals(confpwd.getText())) {
                if(dataManager.registerDentist(name, addr, pwd)){
                    error.setText("");
                    name.clear();
                    addr.clear();
                    pwd.clear();
                    confpwd.clear();
                    stage.setScene(dLogin.getScene());
                    stage.setTitle(dLogin.getTitle());
                }else{
                    error.setText("Error with the database,\nTry again later");
                }
            } else {
                error.setText("Passwords don't match");
            }
        }
    }

    public void cancelRegistration(){   //Changes to the login scene
        stage.setScene(dLogin.getScene());
        stage.setTitle(dLogin.getTitle());
    }

    /***************************
    *Patient View Scene Methods
     ***************************/

    public void logout(){    //Changes to the login scene, clears the dentistOn object and resets the invoice list
        dentistOn = null;
        stage.setScene(dLogin.getScene());
        stage.setTitle(dLogin.getTitle());
        patView.getInvoiceList().getItems().clear();
        patView.getInvoiceList().getItems().add("Select a patient to view their invoices");
        patView.getAddInv().setDisable(true);
        patView.getRemovePat().setDisable(true);
    }

    public void getPatientInvoices(){   //Enables a few buttons, fills the invoices array and corrects the invoice number generator
        patView.getRemovePat().setDisable(false);
        int patIndex = patView.getPatientList().getSelectionModel().getSelectedIndex();
        if(patIndex > -1){
            if(!(patients.isEmpty())){
                invoices = dentistOn.getPatient(patIndex).getP_invoiceList();
                int invNo = 1;
                for(Invoice inv : invoices){
                    invNo++;
                    inv.invoiceNoGen = invNo;
                }
                patView.getShowProcs().setDisable(true);
                patView.getShowPays().setDisable(true);
                patView.getAddInv().setDisable(false);
                fillInvoiceList();
            }
        }
    }

    public void fillInvoiceList(){  //Fills the invoice list
        patView.getInvoiceList().getItems().clear();
        if (invoices.isEmpty()) {
            patView.getInvoiceList().getItems().add("No invoices");
        } else {
            for (Invoice inv : invoices) {
                patView.getInvoiceList().getItems().add(inv.getInvoiceNo() + "\t" + inv.getInvoiceAmt() + "\t\t" + inv.getInvoiceDate());
            }
        }
    }


    public void enableShowButtons(){  //Enables some buttons
        if(patView.getInvoiceList().getSelectionModel().getSelectedIndex() > -1){
            patView.getShowProcs().setDisable(false);
            patView.getShowPays().setDisable(false);
        }
    }

    public void fillPatientView(){   //Fills the patient list
        patients = dentistOn.getPatientList();
        patView.getPatientList().getItems().clear();
        if(patients.isEmpty()){
            patView.getPatientList().getItems().add("Patient list empty");
        }else{
            for (Patient pt : patients){
                patView.getPatientList().getItems().add(pt.getPatientNo() + "\t\t" + pt.getName() + "\t\t" + pt.getAddress() + "\t\t0" + pt.getPhoneNumber());
            }
        }
    }

    public void newPatient(){    //Shows the add patient window
        modalStage.setScene(patAdd.getScene());
        modalStage.setTitle(patAdd.getTitle());
        modalStage.showAndWait();
    }

    public void saveFile(){     //Writes the dentist's data to a file and shows an information window informing the save event
        dataManager.saveData(dentistOn);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save");
        alert.setHeaderText(null);
        alert.setContentText("File saved");
        alert.showAndWait();
    }

    public void removePatient(){   //Uses a confirmation window to confirm if patient should be deleted, if so deletes the patient
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Patient");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove this patient?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            int patIndex = patView.getPatientList().getSelectionModel().getSelectedIndex();
            if(patIndex > -1) {
                dentistOn.removePatient(patIndex);
                fillPatientView();
            }
        }
    }

    public void addInvoice(){   //Creates an invoice
        int patIndex = patView.getPatientList().getSelectionModel().getSelectedIndex();
        if(patIndex > -1){
            Invoice newInv = new Invoice(new Date(),0);
            patients.get(patIndex).addInvoice(newInv);
            fillInvoiceList();
        }
    }

    /***Report Methods***/
    public void generatePatientReport(){   //Generates Patient Report
        repOut.clear();
        repView.getRepText().clear();
        ArrayList<Patient> sortedPats = new ArrayList<>();
        for(Patient pat : patients){
            sortedPats.add(pat);
        }
        sortPatients(sortedPats,"name");
        for(Patient pat : sortedPats){
            repOut.add("Name: " + pat.getName());
            repOut.add("Address: " + pat.getAddress());
            repOut.add("Phone Number: 0" + pat.getPhoneNumber());
            repOut.add("Patient Number: " + pat.getPatientNo());
            repOut.add("\tInvoices:");
            if(pat.getP_invoiceList().isEmpty()){
                repOut.add("\tNo Invoices");
            }else{
                for(Invoice inv : pat.getP_invoiceList()){
                    repOut.add("\tNumber: " + inv.getInvoiceNo());
                    repOut.add("\tCost: " + inv.getInvoiceAmt());
                    repOut.add("\tDate: " + inv.getInvoiceDate());
                    repOut.add("\t\tProcedures:");
                    if(inv.getProcedureList().isEmpty()){
                        repOut.add("\t\tNo Procedures");
                    }else{
                        for(Procedure proc : inv.getProcedureList()){
                            repOut.add("\t\tNumber: " + proc.getProcNo());
                            repOut.add("\t\tCost: " + proc.getProcCost());
                            repOut.add("\t\tName: " + proc.getProcName());
                        }
                    }
                    repOut.add("\t\tPayments:");
                    if(inv.getPaymentsList().isEmpty()){
                        repOut.add("\t\tNo Payments");
                    }else {
                        for (Payment pay : inv.getPaymentsList()) {
                            repOut.add("\t\tNumber: " + pay.getPaymentNum());
                            repOut.add("\t\tAmount: " + pay.getPaymentAmt());
                            repOut.add("\t\tDate: " + pay.getPaymentDate());
                        }
                    }
                }
            }
        }
        for(int i = 0; i < repOut.size(); i++){
            repView.getRepText().appendText(repOut.get(i));
            if(i != repOut.size()-1){
                repView.getRepText().appendText("\n");
            }
        }
        stage.setScene(repView.getScene());
        stage.setTitle(repView.getTitlePat());
    }

    public void generatePaymentReport(){   //Generates Payment Report
        repOut.clear();
        repView.getRepText().clear();
        final int MAX_MONTHS_DUE = 6;
        ArrayList<Patient> patsWithPaymentsDue = new ArrayList<>();
        Date date = new Date();
        DateTime currentDate = new DateTime(date);
        for(Patient pat : patients){
            patsWithPaymentsDue.add(pat);
        }
        sortPatients(patsWithPaymentsDue,"payment");
        if(patsWithPaymentsDue.isEmpty()){
            repOut.add("No payments due");
        }
        for(Patient pat : patsWithPaymentsDue){
            boolean hasUnpaid = false;
            for(Invoice inv : pat.getP_invoiceList()){
                if(!inv.getIsPaid()){
                    hasUnpaid = true;
                }
            }
            if(hasUnpaid){
                repOut.add("Name: " + pat.getName());
                repOut.add("Address: " + pat.getAddress());
                repOut.add("Phone Number: 0" + pat.getPhoneNumber());
                repOut.add("Patient Number: " + pat.getPatientNo());
                double paymentDue = 0;
                boolean needToPay = false;
                for(Invoice inv : pat.getP_invoiceList()){
                    if(!inv.getIsPaid()){
                        if(!inv.getPaymentsList().isEmpty()) {
                            for (Payment pay : inv.getPaymentsList()) {
                                DateTime payDate = new DateTime(pay.getPaymentDate());
                                if (Months.monthsBetween(payDate, currentDate).getMonths() < MAX_MONTHS_DUE) {
                                    needToPay = true;
                                }
                            }
                            if (needToPay) {
                                paymentDue += inv.getInvoiceAmt();
                            }
                        }else{
                            paymentDue += inv.getInvoiceAmt();
                        }
                    }
                }
                repOut.add("\t\tPayment Due: " + paymentDue);
            }else{
                repOut.add("No payments due");
            }

        }
        for(int i = 0; i < repOut.size(); i++){
            repView.getRepText().appendText(repOut.get(i));
            if(i != repOut.size()-1){
                repView.getRepText().appendText("\n");
            }
        }
        stage.setScene(repView.getScene());
        stage.setTitle(repView.getTitlePay());
    }

    public void sortPatients(ArrayList<Patient> patArray, String type){  //Sorts Patient Array
        boolean sorted = false;
        int switches = 0;
        while(!sorted){
            for(int i = 0; i < patArray.size()-1; i++){
                if(patArray.get(i).comparePat(patArray.get(i+1),type)){
                    patArray.add(i+2,patArray.get(i));
                    patArray.remove(i);
                    switches++;
                }
            }
            if(switches == 0){
                sorted = true;
            }else{
                switches = 0;
            }
        }
    }

    public void exportReport(TextArea repText){  //Writes data to a file
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Export File");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter file name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if(result.equals("")){
                exportReport(repText);
            }else{
                try{
                    PrintWriter pw = new PrintWriter(result.get()+".txt");
                    pw.print(repText.getText());
                    pw.close();
                }catch(FileNotFoundException fnf){
                    System.out.println(fnf);
                }
            }
        }
    }

    /*************************************
    *Payments and Procedures Scene Methods
     **************************************/

    public void showProcedures(){   //Loads the procedure list and shows the procedures/payments scene
        int pIndex = patView.getPatientList().getSelectionModel().getSelectedIndex();
        int iIndex = patView.getInvoiceList().getSelectionModel().getSelectedIndex();
        stage.setScene(prPaView.getScene());
        stage.setTitle(prPaView.getTitle() + dentistOn.getPatient(pIndex).getName());
        prPaView.setRootIndexes(pIndex,iIndex);
        prPaView.getTabPane().getSelectionModel().select(prPaView.getProcTab());
        showProcList(pIndex,iIndex);
        showPayList(pIndex,iIndex);
    }

    public void showPayments(){    //Loads the payments list and shows the procedures/payments scene
        int pIndex = patView.getPatientList().getSelectionModel().getSelectedIndex();
        int iIndex = patView.getInvoiceList().getSelectionModel().getSelectedIndex();
        stage.setScene(prPaView.getScene());
        stage.setTitle(prPaView.getTitle() + dentistOn.getPatient(pIndex).getName());
        prPaView.setRootIndexes(pIndex,iIndex);
        prPaView.getTabPane().getSelectionModel().select(prPaView.getPayTab());
        showProcList(pIndex,iIndex);
        showPayList(pIndex,iIndex);
    }

    public void goToPatientView(){    //Goes to the patient scene and checks if exception occurs to diable some buttons
        stage.setScene(patView.getScene());
        stage.setTitle(patView.getTitle());
        try{
            patView.getInvoiceList().getSelectionModel().getSelectedItem().length();
        }catch(NullPointerException np){
            patView.getShowProcs().setDisable(true);
            patView.getShowPays().setDisable(true);
        }
        prPaView.getEditProc().setDisable(true);
        prPaView.getRemoveProc().setDisable(true);
    }

    public void addProcedure(int pIndex, int iIndex){  //Sets up the add procedure configuration
        procAE.setAddMode();
        procAE.setRootIndexes(pIndex,iIndex);
        modalStage.setScene(procAE.getScene());
        modalStage.setTitle(procAE.getTitleAdd());
        modalStage.showAndWait();
    }

    public void editProcedure(int pIndex, int iIndex){   //Sets up the adit procedure configuration
        int procIndex = prPaView.getProcList().getSelectionModel().getSelectedIndex();
        if(procIndex > -1){
            procAE.setEditMode(dentistOn.getPatient(pIndex).getInvoice(iIndex).getProcedure(procIndex));
            procAE.setRootIndexes(pIndex,iIndex);
            modalStage.setScene(procAE.getScene());
            modalStage.setTitle(procAE.getTitleEdit());
            modalStage.showAndWait();
        }
    }

    public void enableEditAndRemove(){   //Enables some buttons
        if(!(procedures.isEmpty())){
            prPaView.getEditProc().setDisable(false);
            prPaView.getRemoveProc().setDisable(false);
        }
    }

    public void removeProcedure(int pIn, int iIn){     //Removes a procedure
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Procedure");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove this procedure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            int procIndex = prPaView.getProcList().getSelectionModel().getSelectedIndex();
            if(procIndex > -1){
                double procCost = dentistOn.getPatient(pIn).getInvoice(iIn).getProcedure(procIndex).getProcCost();
                double invCost = dentistOn.getPatient(pIn).getInvoice(iIn).getInvoiceAmt();
                double newInvCost = invCost - procCost;
                dentistOn.getPatient(pIn).getInvoice(iIn).setInvoiceAmt(newInvCost);
                dentistOn.getPatient(pIn).getInvoice(iIn).removeProcedure(procIndex);
                fillInvoiceList();
                showProcList(pIn, iIn);
            }
        }

    }

    public void addPayment(int pIn, int iIn){
        payView.setRootIndexes(pIn,iIn);
        modalStage.setScene(payView.getScene());
        modalStage.setTitle(payView.getTitle());
        modalStage.showAndWait();
    }

    private void showPayList(int pIndex, int iIndex){   //Shows the payments list
        prPaView.getPayList().getItems().clear();
        payments = dentistOn.getPatient(pIndex).getInvoice(iIndex).getPaymentsList();
        int payNo = 1;
        for(Payment pay : payments){
            payNo++;
            pay.paymentNumGen = payNo;
        }
        if(payments.isEmpty()){
            prPaView.getPayList().getItems().add("Payment list empty");
        }else{
            for(Payment pay : payments){
                prPaView.getPayList().getItems().add(pay.getPaymentNum() + "\t\t" + pay.getPaymentAmt()  + "\t\t" + pay.getPaymentDate());
            }
        }

    }

    private void showProcList(int pIndex, int iIndex){   //Shows the procedures list
        prPaView.getProcList().getItems().clear();
        procedures = dentistOn.getPatient(pIndex).getInvoice(iIndex).getProcedureList();
        int procNo = 1;
        for(Procedure proc : procedures){
            procNo++;
            proc.procNoGen = procNo;
        }
        if(procedures.isEmpty()){
            prPaView.getProcList().getItems().add("Procedure list empty");
        }else{
            for(Procedure proc : procedures){
                prPaView.getProcList().getItems().add(proc.getProcNo() + "\t\t" + proc.getProcCost()  + "\t\t" + proc.getProcName());
            }
        }
    }

    /*************************
    *Add Patient Scene Methods
     **************************/

    public void addPatient(TextField nameField, TextField addrField, TextField phoneNumField, TextField pNoField, Text error){  //Adds a patient
        if(nameField.equals("") && addrField.equals("") && phoneNumField.equals("") && pNoField.equals("")){
            error.setText("All fields must have a value");
        }else{
            try{
                boolean valildPNo = true;
                for (Patient pat : patients) {
                    if (pat.getPatientNo() == Integer.parseInt(pNoField.getText())) {
                        valildPNo = false;
                    }
                }
                if (valildPNo) {
                    String name = nameField.getText();
                    String addr = addrField.getText();
                    String strNum = phoneNumField.getText();
                    if (strNum.charAt(0) == '0') {
                        strNum = strNum.substring(0);
                    }
                    int phoneNum = Integer.parseInt(strNum);
                    int pNo = Integer.parseInt(pNoField.getText());
                    patients.add(new Patient(name, addr, phoneNum, pNo));
                    error.setText("");
                    nameField.clear();
                    addrField.clear();
                    phoneNumField.clear();
                    pNoField.clear();
                    fillPatientView();
                    modalStage.close();
                } else {
                    error.setText("That patient number is\nalready in use");
                }
            }catch(NumberFormatException nf){
                error.setText("Please provide a valid phone\nnumber or patient number");
            }
        }
    }

    public void cancelNewPatient(){
        modalStage.close();
    }   //Closes the add patient window

    /*********************************
    *Add/Edit Procedures Scene Methods
     **********************************/

    public void createProcedure(TextField name, TextField cost, Text error, int pIn, int iIn){   //Adds a procedure
        if(name.getText().isEmpty() && cost.getText().isEmpty()){
            error.setText("All fields must have a value");
        }else{
            try {
                double newCost = Double.parseDouble(cost.getText());
                if(newCost <= 0){
                    throw new NumberFormatException();
                }
                Procedure proc = new Procedure(name.getText(), newCost);
                dentistOn.getPatient(pIn).getInvoice(iIn).addProcedure(proc);
                double invCost = dentistOn.getPatient(pIn).getInvoice(iIn).getInvoiceAmt();
                double newInvCost = invCost + proc.getProcCost();
                dentistOn.getPatient(pIn).getInvoice(iIn).setInvoiceAmt(newInvCost);
                fillInvoiceList();
                error.setText("");
                name.clear();
                cost.clear();
                modalStage.close();
                showProcList(pIn, iIn);
            }catch(NumberFormatException nf){
                error.setText("Please enter a valid cost");
            }
            name.clear();
            cost.clear();
        }
    }

    public void updateProcedure(TextField name, TextField cost, Text error, Procedure proc, int pIn, int iIn){   //Updates procedure
        if(name.getText().isEmpty() && cost.getText().isEmpty()){
            error.setText("All fields must have a value");
        }else{
            try{
                double oldProcCost = proc.getProcCost();
                double invCost = dentistOn.getPatient(pIn).getInvoice(iIn).getInvoiceAmt();
                double invBaseCost = invCost - oldProcCost;
                double newCost = Double.parseDouble(cost.getText());
                if(newCost <= 0){
                    throw new NumberFormatException();
                }
                proc.setProcCost(newCost);
                if(proc.getProcCost() <= 0){
                    throw new NumberFormatException();
                }
                proc.setProcName(name.getText());
                double newProcCost = proc.getProcCost();
                double newInvCost = invBaseCost + newProcCost;
                dentistOn.getPatient(pIn).getInvoice(iIn).setInvoiceAmt(newInvCost);
                fillInvoiceList();
                error.setText("");
                name.clear();
                cost.clear();
                modalStage.close();
                showProcList(pIn, iIn);
            }catch(NumberFormatException nf){
                error.setText("Please enter a valid cost");
            }
            name.clear();
            cost.clear();
        }
    }

    public void closeProcWindow(){  //Closes the procedure window
        modalStage.close();
    }

    /*********************************
     *Add Payment Scene Methods
     **********************************/

    public void createPayment(TextField amount, Text error, int pIn, int iIn){
        if(amount.getText().isEmpty()){
            error.setText("You must provide an amount");
        }else{
            try {
                double payAmt = Double.parseDouble(amount.getText());
                if(payAmt <= 0){
                    throw new NumberFormatException();
                }
                if (dentistOn.getPatient(pIn).getInvoice(iIn).getInvoiceAmt() < payAmt) {
                    error.setText("Amount entered exceeds invoice\namount. Provide a smaller amount");
                } else {
                    Date date = new Date();
                    Payment pay = new Payment(payAmt, date);
                    dentistOn.getPatient(pIn).getInvoice(iIn).addPayment(pay);
                    dentistOn.getPatient(pIn).getInvoice(iIn).setInvoiceAmt(dentistOn.getPatient(pIn).getInvoice(iIn).getInvoiceAmt() - payAmt);
                    if (dentistOn.getPatient(pIn).getInvoice(iIn).getInvoiceAmt() == 0) {
                        dentistOn.getPatient(pIn).getInvoice(iIn).setIsPaid(true);
                    } else {
                        dentistOn.getPatient(pIn).getInvoice(iIn).setIsPaid(false);
                    }
                    modalStage.close();
                    showPayList(pIn, iIn);
                    fillInvoiceList();
                }
            }catch(NumberFormatException nf){
                error.setText("Please enter a valid amount");
            }
            amount.clear();
        }
    }

    public void closePayWindow(){
        modalStage.close();
    }

}