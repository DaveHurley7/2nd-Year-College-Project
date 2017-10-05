import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable{
    static final long serialVersionUID = -5467989556394509707L;
    private int paymentNum;
    static int paymentNumGen = 1;
    private double paymentAmt;
    private Date paymentDate;

    public Payment(double pAmt, Date pDate){
        paymentAmt = pAmt;
        paymentDate = pDate;
        paymentNum = paymentNumGen++;
    }

    public Payment(double pAmt, Date pDate, int num){
        paymentAmt = pAmt;
        paymentDate = pDate;
        paymentNum = num;
        paymentNumGen++;
    }

    public int getPaymentNum(){
        return paymentNum;
    }

    public double getPaymentAmt(){
        return paymentAmt;
    }

    public void setPaymentAmt(double amt){
        paymentAmt = amt;
    }

    public Date getPaymentDate(){
        return paymentDate;
    }

    public String toString(){
        return "No.: " + getPaymentNum() + "\nAmount: " + getPaymentAmt() + "\nDate: " + getPaymentDate();
    }

}