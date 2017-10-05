import java.io.Serializable;

public class Procedure implements Serializable{
    static final long serialVersionUID = 8140795867080206357L;
    private int procNo;
    static int procNoGen = 1;
    private String procName;
    private double procCost;

    public Procedure(String name, double cost){
        procName = name;
        procCost = cost;
        procNo = procNoGen++;
    }

    public Procedure(String name, double cost, int no){
        procName = name;
        procCost = cost;
        procNo = no;
        procNoGen++;
    }

    public int getProcNo(){
        return procNo;
    }

    public String getProcName(){
        return procName;
    }

    public double getProcCost(){
        return procCost;
    }

    public void setProcName(String pName){
        procName = pName;
    }

    public void setProcCost(double pCost){
        procCost = pCost;
    }

    public String toString(){
        return "No.: " + getProcNo() + "\nName: " + getProcName() + "\nCost: " + getProcCost();
    }
}