import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Invoice implements Serializable{
    static final long serialVersionUID = -7405981848223765628L;
    private int invoiceNo;
    static int invoiceNoGen = 1;
    private double invoiceAmt;
    private Date invoiceDate;
    private boolean isPaid;
    private ArrayList<Procedure> in_procList;
    private ArrayList<Payment> in_paymentList;

    public Invoice(Date d, double iAmt){
        invoiceDate = d;
        invoiceAmt = iAmt;
        in_procList = new ArrayList<>();
        in_paymentList = new ArrayList<>();
        invoiceNo = invoiceNoGen++;
        isPaid = false;
    }

    public Invoice(Date d, double iAmt, int invNo){
        invoiceDate = d;
        invoiceAmt = iAmt;
        in_procList = new ArrayList<>();
        in_paymentList = new ArrayList<>();
        invoiceNo = invNo;
        invoiceNoGen++;
    }


    public Date getInvoiceDate(){
        return invoiceDate;
    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceAmt(double iAmt){
        invoiceAmt = iAmt;
    }

    public double getInvoiceAmt(){
        return invoiceAmt;
    }

    public void setIsPaid(boolean paid){
        isPaid = paid;
    }

    public boolean getIsPaid(){
        return isPaid;
    }

    public ArrayList<Procedure> getProcedureList() {
        return in_procList;
    }

    public Procedure getProcedure(int index){
        return in_procList.get(index);
    }

    public void removeProcedure(int index){
        in_procList.remove(index);
    }

    public void addProcedure(Procedure proc){
        in_procList.add(proc);
    }

    public ArrayList<Payment> getPaymentsList() {
        return in_paymentList;
    }

    public Payment getPayment(int index){
        return in_paymentList.get(index);
    }

    public void removePayment(int index){
        in_paymentList.remove(index);
    }

    public void removePayment(Payment pay){
        in_paymentList.remove(pay);
    }

    public void addPayment(Payment pay){
        in_paymentList.add(pay);
    }

    public String toString(){
        return "Invoice No.: " + getInvoiceNo() + "\nInvoice Date: " + getInvoiceDate() + "\nInvoice Cost : " + getInvoiceAmt();
    }

}