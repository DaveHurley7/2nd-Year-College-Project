import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Patient extends Person implements Serializable{
    static final long serialVersionUID = -6208893515473726138L;
    private int patientNo;
    private int phoneNumber;
    private ArrayList<Invoice> p_invoiceList;

    public Patient(String name, String address, int phoneNumber, int patientNo){
        super(name, address);
        this.patientNo = patientNo;
        this.phoneNumber = phoneNumber;
        p_invoiceList = new ArrayList<>();
    }

    public void setPatientNo(int patientNo){
        this.patientNo = patientNo;
    }

    public int getPatientNo(){
        return patientNo;
    }

    public int getPhoneNumber(){
        return phoneNumber;
    }

    public ArrayList<Invoice> getP_invoiceList(){
        return p_invoiceList;
    }

    public Invoice getInvoice(int index){
        return p_invoiceList.get(index);
    }

    public void addInvoice(Invoice invoice){
        p_invoiceList.add(invoice);
    }

    public void removeInvoice(int index){
        p_invoiceList.remove(index);
    }
    public void removeInvoice(Invoice inv){
        p_invoiceList.remove(inv);
    }

    public boolean comparePat(Patient pat, String type){
        boolean returnVal = false;
        switch(type){
            case "name":
                if(getName().compareTo(pat.getName()) > 0){
                    returnVal = true;
                }else if(getName().compareTo(pat.getName()) < 0) {
                    returnVal = false;
                }else{
                    if(getPatientNo() > pat.getPatientNo()){
                        returnVal = true;
                    }
                }
                break;
            case "payment":
                double thisPatTotal = 0;
                for(Invoice inv : getP_invoiceList()){
                    for(Payment pay : inv.getPaymentsList()){
                        thisPatTotal += pay.getPaymentAmt();
                    }
                }
                double otherPatTotal = 0;
                for(Invoice inv : getP_invoiceList()){
                    for(Payment pay : inv.getPaymentsList()){
                        otherPatTotal += pay.getPaymentAmt();
                    }
                }
                if(thisPatTotal > otherPatTotal){
                    returnVal = true;
                }else{
                    returnVal = false;
                }
                break;
        }
        return returnVal;
    }

    public String toString(){
        return "Name: " + getName() + "\nAddress: " + getAddress() + "\nPhone Number: 0" + getPhoneNumber() + "\nPatient No.: " + getPatientNo();
    }

}