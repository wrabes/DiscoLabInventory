package discoverylab.sate.com.inventory2;

/**
 * Created by Cameron on 6/30/14.
 */
public class Person {

    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private int activeYear; //YY
    private int personId; //
    final int TWO = 2; //used to identify as a person in DB
    public int associatedItemId; //can be changed as item may be changed

    public Person(String initName, String initAddress, String initPhoneNumber, String initEmail, int initActiveYear, int initPersonId, int initAssociatedItemId){
        name = initName;
        address=initAddress;
        phoneNumber = initPhoneNumber;
        email=initEmail;
        activeYear=initActiveYear;
        personId = initPersonId;
        associatedItemId = initAssociatedItemId;
    }

    public Person(){}

    public Person(String initName){
        name=initName;
    }

    public void setName(String n){
        name = n;
    }

    public String getName(){
        return name;
    }

    public void setAddress(String a){
        address = a;
    }

    public String getAddress(){
        return address;
    }

    public void setPhoneNumber(String p){
        phoneNumber=p;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setEmail(String e){
        email = e;
    }

    public String getEmail(){
        return email;
    }

    public void setActiveYear(int y){
        activeYear = y;
    }

    public int getActiveYear(){
        return activeYear;
    }

    public void setPersonId(int pid){
        personId = pid;
    }

    public int getPersonId(){
        return personId;
    }

    public void setAssociatedItemId(int iid){
        associatedItemId = iid;
    }

    public int getAssociatedItemId(){
        return associatedItemId;
    }

    @Override
    public String toString(){
        return name;
    }

}
