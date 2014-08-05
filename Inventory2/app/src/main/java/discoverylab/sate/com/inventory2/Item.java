package discoverylab.sate.com.inventory2;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Cameron on 7/1/14.
 */
public class Item {
    private String itemName, description, locationInRoom, category, itemId, dateAdded, warrantyExpiration, associatedPerson="none", unitPrice, dateChecked, owner;
    private boolean available, consumable=false;
    private int quantity=1;
    final int ONE = 1;
    File photoDescription; //to be stored in app's local directory


    public Item(){
    }

    public Item(String initItemName, String initDescription, String initLocationInRoom, String initCategory, String initItemId, String initDateAdded, String initWarrantyExpiration, String initAssociatedPerson, String initUnitPrice, boolean initCheckedOut, boolean initConsumable){
        itemName = initItemName;
        description=initDescription;
        locationInRoom = initLocationInRoom;
        category = initCategory;
        itemId = initItemId;
        dateAdded = initDateAdded;
        warrantyExpiration=initWarrantyExpiration;
        associatedPerson=initAssociatedPerson;
        unitPrice = initUnitPrice;
        available=initCheckedOut;
        consumable=initConsumable;

    }

    public Item(String initItemName){
        itemName = initItemName;
    }



    public void setItemName(String n){
        itemName = n;
    }

    public String getItemName(){
        return itemName;
    }

    public void setDescription(String a){
        description = a;
    }

    public String getDescription(){
        return description;
    }

    public void setLocation(String loc){
        locationInRoom=loc;
    }

    public String getLocation(){
        return locationInRoom;
    }

    public void setCategory(String cat){
        category=cat;
    }

    public String getCategory(){
        return category;
    }

    public void setItemId(){

        String prefix, suffix="";
        int consumableInt = (consumable) ? 1 : 0;

        prefix = (getOwner().substring(0,3))+(getCategory().substring(0,3))+(itemName.substring(0,3))+ (consumableInt);

        if(!consumable) {
            suffix = ItemList.getInstance().itemIdEntryNumber(prefix);
        }else{
            if(quantity < 100){
                if(quantity > 9){
                    suffix = "0"+quantity;
                }else{
                    suffix = "00"+quantity;
                }
            }else{
                suffix += quantity;
            }
        }

        itemId = prefix + suffix;
    }

    public String getItemId(){
        return itemId;
    }

    public void setDateAdded(){
        dateAdded = new SimpleDateFormat("MMddyyyy_HH:mm:ss").format(Calendar.getInstance().getTime());

    }

    public void setDateAdded(String date){
        dateAdded = date;
    }

    public String getDateAdded(){
        return dateAdded;
    }

    public void setWarrantyExpiration(int months){
        String dateAdded = getDateAdded();

        int monthAdded = Integer.parseInt(dateAdded.substring(0,2));
        int day = Integer.parseInt(dateAdded.substring(2,4));
        int yearAdded = Integer.parseInt(dateAdded.substring(4));

        int combined=(monthAdded+months);
        int newMonthVal=0;

        if(combined > 12){
             newMonthVal= (combined)%12;
            if((combined)/12 > 1){
                yearAdded += ((combined)/12);
            }
        }

        warrantyExpiration = ""+newMonthVal+day+yearAdded;

    }

    public void setWarrantyExpiration(String date){
        warrantyExpiration = date;
    }

    public String getWarrantyExpiration(){
        return warrantyExpiration;
    }

    public void setDateChecked(){

        String date = new SimpleDateFormat("MMddyyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
        dateChecked = date;
    }

    public String getDateChecked(){
        return dateChecked;
    }

    public void setOwner(String givenOwner){
        owner = givenOwner;
    }

    public String getOwner(){
        return owner;
        
    }

    public void setAssociatedPerson(String personName){
        //if the person is not in the person list, create a new person object
        // otherwise, just add the string of the person's name


        if(personName.equals("") || personName.equals("none")){
            checkIn();
        }else{
            associatedPerson = personName;

        }
    }

    public String getAssociatedPerson(){
        return associatedPerson;
    }

    public void setUnitPrice(String price){
        unitPrice=price;
    }

    public String getUnitPrice(){
        return "$"+unitPrice;
    }

    public void checkOut(){
        available = false;
        setDateChecked();
    }

    public void checkIn(){
        available = true;
        setDateChecked();
    }

    public boolean isAvailable(){
        return available;
    }

    public void setConsumable(){
        consumable = true;
    }

    public boolean consumable(){
        return consumable;
    }

    public void setQuantity(int quant){
        if (consumable()){
            quantity = quant;
        }else{
            quantity = 1;
        }
    }

    public int getQuantity(){
        return quantity;
    }



    @Override
    public String toString(){
        return itemName;
    }



}