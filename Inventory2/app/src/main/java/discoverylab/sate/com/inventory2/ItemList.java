package discoverylab.sate.com.inventory2;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Cameron on 7/1/14.
 *
 * The ItemList exists only here, using the Singleton design pattern to prevent instantiation
 */
public final class ItemList {

    private static List<Item> items = new ArrayList<Item>();;
    private static ItemList instance;
    private static final String TAG = "ItemList";
    private ItemComparator comp = new ItemComparator();


    private ItemList(){

    }

    public static ItemList getInstance(){
        if(instance == null){
            instance = new ItemList();
        }
        return instance;
    }

    public List<Item> getSortedItemList(){
        //This sorts the master list alphanumerically on every call to getItemList
        Collections.sort(items, comp);
        return items;
    }

    public List<Item> sortList(List<Item> itemList){
        //sorts any list alphanumerically
        Collections.sort(itemList, comp);
        return itemList;
    }

    /**
     *
     * @param choice
     *      an enum corresponding to a filter
     * @return
     *  {@code if choice ==allItems}list of all items
     *  {@code if choice ==categories}list of categories
     *  {@code if choice ==available}list of availability (checkedOut = false first)
     *  {@code if choice ==unavailable}list of availability (checkedOut = true first)
     */
    public List<Item> filter(filterOptions choice, List<Item> list){

        //create an empty list
        List<Item> filteredItemList = new ArrayList<Item>();



        switch(choice){
            case allItems:

                    filteredItemList = list;


                    Log.v(TAG, "You selected 'all items'");

                break;
            case categories:


                //create copy of items list that we can change
                    List<Item> listCopy = copyList(list);
                    Log.d(TAG, "You selected 'categories'");


                //Create an iterator to sift through the entire list of items, copy those that match the
                    Iterator<Item> it = listCopy.iterator();

                    //if the list is empty, take the first entry and use it's category to find similar items
                    int filteredSize = 0;

                    //move to the next category when the iterator reaches the end of the copied list
                    boolean nextCat = false;

                    //while the copy has not been completely extinguished...
                    while(listCopy.size()>0) {

                        it = listCopy.iterator();

                        //check each entry for category and organize as such
                        while (it.hasNext()) {
                            Item tmp = it.next();

                            if (filteredSize == 0 || nextCat) {
                                filteredItemList.add(tmp);
                                filteredSize++;
                                it.remove();
                            } else if ((tmp.getCategory().equals(filteredItemList.get(filteredSize - 1).getCategory()))) { //if the category of the current item matches that of the last item added..
                                filteredItemList.add(tmp);
                                filteredSize++;
                                it.remove();
                            }

                            nextCat = false;
                        }
                        //once it reaches the end of the copied list and doesn't find any common categories as the last one chosen,
                        // it must restart and find the next category
                        nextCat = true;
                    }

                break;
            case available:
                //create copy of items list that we can change
                List<Item> listCopy2 = copyList(list);

                //Create an iterator to sift through the entire list of items, if the item is available, add it first
                Iterator<Item> it2 = listCopy2.iterator();

                //check each entry for availability. if it is available add to front, if not available add to back
                while (it2.hasNext()) {
                    Item tmp = it2.next();

                    if (tmp.isAvailable()){
                        filteredItemList.add(0,tmp);

                    }else{
                        filteredItemList.add(tmp);
                    }
                   Log.d(TAG, "You selected 'available'");
                }

                break;
            case unavailable:
                //create copy of items list that we can change
                List<Item> listCopy3 = copyList(list);

                //Create an iterator to sift through the entire list of items, if the item is unavailable, add it first
                Iterator<Item> it3 = listCopy3.iterator();

                //check each entry for availability. if it is unavailable add to front, if available add to back
                while (it3.hasNext()) {
                    Item tmp = it3.next();

                    if (tmp.isAvailable()){
                        filteredItemList.add(tmp);

                    }else{
                        filteredItemList.add(0,tmp);
                    }
                    Log.d(TAG, "You selected 'unavailable'");
                }
                break;
        }

        return filteredItemList;
    }

    //helper method to copy the original list into an empty list so filtering is easier
    public List<Item> copyList(List<Item> list){
        Iterator<Item> it = list.iterator();
        List<Item> listCopy = new ArrayList<Item>();

        while(it.hasNext()){
            Item tmp = it.next();
            listCopy.add(tmp);
        }
        return listCopy;
    }


    //returns a checkin list.
    public List<Item> checkInList(){
        Iterator<Item> it = items.iterator();
        List<Item> listUnavailable = new ArrayList<Item>();

        while(it.hasNext()){
            Item tmp = it.next();

            if(!tmp.isAvailable()){
                listUnavailable.add(tmp);
            }
        }
        sortList(listUnavailable);
        return listUnavailable;
    }

    //returns a checkout list
    public List<Item> checkOutList(){
        Iterator<Item> it = items.iterator();
        List<Item> listAvailable = new ArrayList<Item>();

        while(it.hasNext()){
            Item tmp = it.next();

            if(tmp.isAvailable()){
                listAvailable.add(tmp);
            }
        }

        sortList(listAvailable);
        return listAvailable;
    }

    public List<Item> masterList(){
        Iterator<Item> it = items.iterator();
        List<Item> masterList = new ArrayList<Item>();

        while(it.hasNext()){
            Item tmp = it.next();

            masterList.add(tmp);
        }
        return masterList;
    }

    public enum filterOptions{
        allItems, categories, available, unavailable
    }

    /**
     * Adds an Item to the {@code items} list
     *
     * @param item
     */
    public void setItems(Item item){
        items.add(item);
    }

    /**
     * reads a String and separates it into smaller Strings based on semicolons as delimiters.
     * These Strings are used to construct an Item, which is returned by the method
     *
     * @param line
     *      the raw line with delimiters from the file
     * @return
     *      an Item to be added to the list
     */
    private static Item processLine(String line){

        Item returnItem = new Item();

        String[] processedLine = line.split(";");

        returnItem.setItemName(processedLine[0]);
        returnItem.setDescription(processedLine[1]);
        returnItem.setLocation(processedLine[2]);
        returnItem.setCategory(processedLine[3]);
        returnItem.setItemId(processedLine[4]);
        returnItem.setDateAdded(processedLine[5]);
        returnItem.setWarrantyExpiration(processedLine[6]);
        returnItem.setAssociatedPerson(processedLine[7]);
        returnItem.setUnitPrice(processedLine[8]);

        //basically casting as a boolean because that's how items are set up yo
        if(Boolean.valueOf(processedLine[9])){
            returnItem.checkOut();
        }else{
            returnItem.checkIn();
        }

        //set consumable makes the item consumable. Items are not consumable by defualt
        if(Boolean.valueOf(processedLine[10])){
            returnItem.setConsumable();
        }

        return returnItem;

    }

    /**
     * Called in the MainActivity, provides the resources for the application. The raw text file
     * is read using a Reader, and then added in the call to {@code setItems}
     *
     * @param ctx
     *      the main activity
     * @param rId
     *      the resource id that points to the file containing the list
     */
    public static void readRawTextFile(Context ctx, int rId){
        //get the input stream ready for read
        InputStream fromTextFile = ctx.getResources().openRawResource(rId);

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(fromTextFile));
        String line;
        try{
            line = fileReader.readLine();
            while(line != null){
                Item itemToAdd = ItemList.processLine(line);
                items.add(itemToAdd);
                line = fileReader.readLine();

            }
        }
        catch(Exception e){
            Log.e(TAG, "There was an error reading the file.", e);
        }

    }

    //TODO create a method that writes a line to the text file when the AddItem frag is used

    public class ItemComparator implements Comparator<Item> {

        @Override
        public int compare(Item item1, Item item2){
            return item1.getItemName().compareTo(item2.getItemName());
        }
    }




}
