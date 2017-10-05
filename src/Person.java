import java.io.Serializable;

public abstract class Person implements Serializable{
    private String name;
    private String address;

    public Person(String name, String address){
        this.name = name;
        this.address= address;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String addr){
        address = addr;
    }

}