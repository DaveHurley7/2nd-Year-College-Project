import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataManager {

    Connection dbconn;
    PreparedStatement prepstmt;
    Statement stmt;

    public DataManager(){}

    public boolean startDatabase(App_Init app){
        boolean success = false;
        try{
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            dbconn = DriverManager.getConnection("jdbc:derby:dbstorage;");
            stmt = dbconn.createStatement();
            success = true;
        }catch(SQLException se){
            app.stop();
            System.out.println("Error starting database");
        }
        return success;
    }

    public Dentist loginDentist(TextField name, PasswordField pwd){
        Dentist dentist = null;
        try {
            ResultSet rs = stmt.executeQuery("SELECT name, address, password FROM dentists WHERE name = '" + name.getText() + "' AND password = '" + pwd.getText() + "'");
            while (rs.next()) {
                String dName = rs.getString("name");
                String dAddr = rs.getString("address");
                String dPass = rs.getString("password");
                if (dName.equals(name.getText()) && dPass.equals(pwd.getText())) {
                    dentist = new Dentist(dName,dAddr,dPass);
                }
            }
        }catch(SQLException sqle){}
        return dentist;
    }

    public int getDentistNo(Dentist dent){
        int dentNo = 0;
        try{
            ResultSet rs = stmt.executeQuery("SELECT name, password, id FROM dentists WHERE name = '" + dent.getName() + "' AND password = '" + dent.getPassword() + "'");
            while (rs.next()) {
                String name = rs.getString("name");
                String pwd = rs.getString("password");
                int id = rs.getInt("id");;
                if (name.equals(dent.getName()) && pwd.equals(dent.getPassword())) {
                    dentNo = id;
                }
            }
        }catch(SQLException sqle){}
        return dentNo;
    }

    public boolean registerDentist(TextField name, TextField addr, PasswordField pwd){
        boolean success = false;
        try{
            prepstmt = dbconn.prepareStatement("INSERT INTO dentists (name,password,address) VALUES (?,?,?)");
            prepstmt.setString(1,name.getText());
            prepstmt.setString(2,pwd.getText());
            prepstmt.setString(3,addr.getText());
            prepstmt.executeUpdate();
            success = true;
        }catch(SQLException sqle){
            success = false;
        }
        return success;
    }

    public void initArrays(Dentist dent, int dentNo){
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients WHERE dentistNo = " + dentNo);
            while(rs.next()){
                int pNo = rs.getInt("patientNo");
                String pName = rs.getString("name");
                String pAddr = rs.getString("address");
                int pPhoneNo = rs.getInt("phoneNo");
                dent.addPatient(new Patient(pName,pAddr,pPhoneNo,pNo));
            }
            for(Patient pat : dent.getPatientList()) {
                ResultSet invRS = stmt.executeQuery("SELECT * FROM invoices WHERE patientNo = " + pat.getPatientNo());
                while (invRS.next()) {
                    int iNo = invRS.getInt("invoiceNo");
                    Date date = stringToDate(invRS.getString("date"));
                    double amount = invRS.getDouble("amount");
                    boolean isPaid = invRS.getBoolean("isPaid");
                    Invoice inv = new Invoice(date, amount, iNo);
                    inv.setIsPaid(isPaid);
                    pat.addInvoice(inv);
                }
            }
            for(Patient patient : dent.getPatientList()){
                for(Invoice inv : patient.getP_invoiceList()){
                    ResultSet procRS = stmt.executeQuery("SELECT * FROM procedures WHERE patientNo = " + patient.getPatientNo() + " AND invoiceNo = " + inv.getInvoiceNo());
                    while(procRS.next()){
                        String name = procRS.getString("name");
                        double cost = procRS.getDouble("cost");
                        int procNo = procRS.getInt("procedureNo");
                        inv.addProcedure(new Procedure(name,cost,procNo));
                    }
                    ResultSet payRS = stmt.executeQuery("SELECT * FROM payments WHERE patientNo = " + patient.getPatientNo() + " AND invoiceNo = " + inv.getInvoiceNo());
                    while(payRS.next()){
                        double payAmount = payRS.getDouble("amount");
                        Date payDate = stringToDate(payRS.getString("date"));
                        int payNo = payRS.getInt("paymentNo");
                        inv.addPayment(new Payment(payAmount,payDate,payNo));
                    }
                }
            }
        }catch(SQLException sqle){}
    }

    public void saveData(Dentist dent){
        int dentNo = getDentistNo(dent);
        if(!dent.getPatientList().isEmpty()) {
            try {
                stmt.executeUpdate("DELETE FROM patients WHERE dentistNo = " + dentNo);
                String insPatQuery = "INSERT INTO patients (patientNo, dentistNo, name, address, phoneNo) VALUES";
                int dentSize = dent.getPatientList().size();
                for (int p = 0; p < dentSize; p++) {
                    insPatQuery += "(?,?,?,?,?)";
                    if (p != dentSize - 1) {
                        insPatQuery += ", ";
                    }
                }
                prepstmt = dbconn.prepareStatement(insPatQuery);
                int pIndex = 1;
                for (Patient pat : dent.getPatientList()) {
                    prepstmt.setInt(pIndex, pat.getPatientNo());
                    pIndex++;
                    prepstmt.setInt(pIndex, dentNo);
                    pIndex++;
                    prepstmt.setString(pIndex, pat.getName());
                    pIndex++;
                    prepstmt.setString(pIndex, pat.getAddress());
                    pIndex++;
                    prepstmt.setInt(pIndex, pat.getPhoneNumber());
                    pIndex++;
                }
                prepstmt.executeUpdate();

                for (Patient pat : dent.getPatientList()) {
                    if(!pat.getP_invoiceList().isEmpty()) {
                        stmt.executeUpdate("DELETE FROM invoices WHERE patientNo = " + pat.getPatientNo());
                        String insInvQuery = "INSERT INTO invoices (patientNo, invoiceNo, date, amount, isPaid) VALUES";
                        int invSize = pat.getP_invoiceList().size();
                        for (int i = 0; i < invSize; i++) {
                            insInvQuery += "(?,?,?,?,?)";
                            if (i != invSize - 1) {
                                insInvQuery += ", ";
                            }
                        }
                        prepstmt = dbconn.prepareStatement(insInvQuery);
                        int iIndex = 1;
                        for (Invoice inv : pat.getP_invoiceList()) {
                            prepstmt.setInt(iIndex, pat.getPatientNo());
                            iIndex++;
                            prepstmt.setInt(iIndex, inv.getInvoiceNo());
                            iIndex++;
                            prepstmt.setString(iIndex, "" + inv.getInvoiceDate());
                            iIndex++;
                            prepstmt.setDouble(iIndex, inv.getInvoiceAmt());
                            iIndex++;
                            prepstmt.setBoolean(iIndex, inv.getIsPaid());
                            iIndex++;
                        }
                        prepstmt.executeUpdate();
                    }
                }

                for (Patient pat : dent.getPatientList()) {
                    for (Invoice inv : pat.getP_invoiceList()) {
                        if (!inv.getProcedureList().isEmpty()) {
                            stmt.executeUpdate("DELETE FROM procedures WHERE patientNo = " + pat.getPatientNo() + " AND invoiceNo = " + inv.getInvoiceNo());
                            String insProcQuery = "INSERT INTO procedures (patientNo, invoiceNo, procedureNo, name, cost) VALUES";
                            int procSize = inv.getProcedureList().size();
                            for (int p = 0; p < procSize; p++) {
                                insProcQuery += "(?,?,?,?,?)";
                                if (p != procSize - 1) {
                                    insProcQuery += ", ";
                                }
                            }
                            prepstmt = dbconn.prepareStatement(insProcQuery);
                            int prIndex = 1;
                            for (Procedure proc : inv.getProcedureList()) {
                                prepstmt.setInt(prIndex, pat.getPatientNo());
                                prIndex++;
                                prepstmt.setInt(prIndex, inv.getInvoiceNo());
                                prIndex++;
                                prepstmt.setInt(prIndex, proc.getProcNo());
                                prIndex++;
                                prepstmt.setString(prIndex, proc.getProcName());
                                prIndex++;
                                prepstmt.setDouble(prIndex, proc.getProcCost());
                                prIndex++;
                            }
                            prepstmt.executeUpdate();
                        }

                        if(!inv.getPaymentsList().isEmpty()) {
                            stmt.executeUpdate("DELETE FROM payments WHERE patientNo = " + pat.getPatientNo() + " AND invoiceNo = " + inv.getInvoiceNo());
                            String insPayQuery = "INSERT INTO payments (patientNo, invoiceNo, paymentNo, amount, date) VALUES";
                            int paySize = inv.getPaymentsList().size();
                            for (int p = 0; p < paySize; p++) {
                                insPayQuery += "(?,?,?,?,?)";
                                if (p != paySize - 1) {
                                    insPayQuery += ", ";
                                }
                            }
                            prepstmt = dbconn.prepareStatement(insPayQuery);
                            int paIndex = 1;
                            for (Payment pay : inv.getPaymentsList()) {
                                prepstmt.setInt(paIndex, pat.getPatientNo());
                                paIndex++;
                                prepstmt.setInt(paIndex, inv.getInvoiceNo());
                                paIndex++;
                                prepstmt.setInt(paIndex, pay.getPaymentNum());
                                paIndex++;
                                prepstmt.setDouble(paIndex, pay.getPaymentAmt());
                                paIndex++;
                                prepstmt.setString(paIndex, "" + pay.getPaymentDate());
                                paIndex++;
                            }
                            prepstmt.executeUpdate();
                        }
                    }
                }
            } catch (SQLException sqle) {}
        }
    }

    public Date stringToDate(String date){
        try{
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date datestr = dateFormatter.parse(date);
            return datestr;
        }catch(ParseException p){}
        return null;
    }

    public void shutdownDatabase(){
        try{
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }catch(SQLException se){}
    }

}