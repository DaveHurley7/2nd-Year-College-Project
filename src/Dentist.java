import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Dentist extends Person implements Serializable{
    static final long serialVersionUID = -5827269940534774932L;
    private String password;
    private ArrayList<Patient> patientList;

    public Dentist(String name, String address, String password){
        super(name, address);
        this.password = password;
        patientList = new ArrayList<>();
    }

    public String getPassword(){
        return password;
    }

    public ArrayList<Patient> getPatientList(){
        return patientList;
    }

    public Patient getPatient(int index){
        return patientList.get(index);
    }

    public void removePatient(int index){
        patientList.remove(index);
    }

    public void addPatient(Patient pat){
        patientList.add(pat);
    }

    public String toString(){
        return "Name: " + getName() + "\nAddress: " + getAddress();
    }
}