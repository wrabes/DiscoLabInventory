package discoverylab.sate.com.inventory2;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


/**
 * A  {@link DialogFragment} subclass which contains the functionality to view and edit an item in the itemList.
 *
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ItemFragment extends DialogFragment implements View.OnClickListener{


    final String TAG = "ItemFragment";

    //REQUIRED FIELDS (quantity is set to 1 by default, because items are not consumable by default...See Item class)
    private EditText itemName;
    private EditText quantity;
    private AutoCompleteTextView category;
    private AutoCompleteTextView owner;


    private static final int CAMERA_REQUEST_CODE = 42;


    //get the context so making toast is possible
    Context ctext;



    private EditText itemId;
    private EditText description;
    private EditText price;
    private EditText locationInRoom;
    private EditText warrantyExpiration;
    private AutoCompleteTextView associatedPerson;
    private TextView dateAdded;
    private Button  checkin, checkout;


    private Button cancel, edit, save;
    Item selectedItem;
    Item newItem = new Item();
    boolean isNew;
    boolean available;




    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     *
     * @return A new instance of fragment ItemFragment.
     */
    public static ItemFragment newInstance() {

        return new ItemFragment();
    }
    public ItemFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        //EditText instantiations, AutoCompleteTextView instantiations, TextView instantiations, Button instantiations
        instViews(view);

        //set onClickListeners for Buttons
        setClickListeners();



        if(getDialog() != null){

            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setTitle("View Item #"+selectedItem.getItemId()+" – "+selectedItem.getItemName());
        }

        if(!isNew){
            changeControlViewOptions(true);
            populateItemViews();

        }else{

            changeControlViewOptions(false);
        }

        return view;
    }

    @Override
    public void onClick(View view){

        switch(view.getId()){
            case R.id.checkIn:
                available = true;
                Toast.makeText(ctext, "The item will be checked in.", Toast.LENGTH_SHORT).show();

                setCheckInButton(false);



                break;
            case R.id.checkOut:
                //if the user did not provide a person to checkout an item to, the item cannot be checked out until they do

                try {
                    if (canCheckOut()) {
                        available = false;
                        Toast.makeText(ctext, "The item can be checked out to "+associatedPerson.getText().toString(), Toast.LENGTH_SHORT).show();
                        setCheckInButton(true);


                    } else {
                        Toast.makeText(ctext, "A person must be associated in order to checkout.", Toast.LENGTH_LONG).show();
                    }
                }
                catch(NullPointerException e){
                        Toast.makeText(ctext, "A person must be associated in order to checkout.", Toast.LENGTH_LONG).show();
                };


                break;
            case R.id.cancel:

                if(getDialog()!=null){
                    getDialog().dismiss();

                }else{
                    clearNewItemView();
                }

                break;
            case R.id.edit:

                changeControlViewOptions(false);

                break;
            case R.id.save:

                if(canSave()) {


                    //if the item is a new item (accessed from the AddItem fragment) and the fields are correctly given
                    if (isNew) {
                        Log.e(TAG, "Save was clicked and isNew == true, canSave() == true");

                        saveItem(newItem);

                        //newItem.checkIn();
                        newItem.setDateAdded();


                        Toast.makeText(ctext, newItem.getItemName() + " successfully saved.", Toast.LENGTH_SHORT).show();

                        //this will add the item to the list of items
                        ItemList.getInstance().setItems(newItem);

                        //TODO add an image to the new item


                        //just resets the process of adding a new item
                        clearNewItemView();

                    } else {
                        //if the user is editing an item

                        Log.e(TAG, "Save was clicked and isNew == false, canSave() == true");

                        confirmSave();
                        saveItem(selectedItem);


                        Toast.makeText(ctext, "Changes to " + selectedItem.getItemName() + " successfully saved.", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).getmSectionsPagerAdapter().notifyDataSetChanged();


                        dismiss();
                    }
                }





                break;
            default:
                Log.e(TAG, "No idea what you clicked.");


        }
    }



    @Override
    public void onAttach(Activity activity) {
        ctext = getActivity();

        super.onAttach(activity);

    }




    @Override
    public void onDetach() {
        super.onDetach();
        ctext = null;
    }

    /**
     *
     * @param isItNew
     *      determines whether this is a new item being populated for the first time.
     *      if true, the user cannot select 'Save' until the item has been populated. Only cancel
     *      will be selectable, as the Edit button will already be selected
     *      if false, the user can 'Cancel'. Until the user presses 'Edit', they cannot 'Save'
     */
    public void setViewOptions(boolean isItNew){
        isNew = isItNew;
    }

    /**
     *
     * this method changes the state of the buttons at the bottom of the item fragment.

     *
     * @param editAllowed
     *      boolean value will be true when selecting an item from the browse/search fragment
     *      boolean value will be false when selecting the AddItem fragment because the default mode is edit
     *      boolean value will be false after selecting it once
     *
     *
     */
    private void changeControlViewOptions(boolean editAllowed){
        //make sure the user can cancel
        cancel.setClickable(true);




        //handle the edit button and options associated
        //if editing is allowed, the button should be clickable (only if existing item) and edit button is not already selected
        if(editAllowed){
            Log.e(TAG, "changeControlViewOptions: edit allowed. save not allowed.");
            edit.setClickable(true);
            edit.setEnabled(true);

            save.setClickable(false);
            save.setEnabled(false);

            changeItemViewOptions(false);
        }else{
            Log.e(TAG, "changeControlViewOptions: edit not allowed. save allowed.");

            edit.setClickable(false);
            edit.setEnabled(false);

            //this must mean that the user is already editing the item fields. the canEdit method handles which views to make editable
            changeItemViewOptions(true);

            save.setClickable(true);
            save.setEnabled(true);
        }



    }


    /**
     * method to populate the dialog of an existing item with the item's information
     */
    public void setItem(Item item){
        selectedItem = item;
    }


    /**
     * This helper method is used if the item is not a new item. It will fill the textViews with the existing data
     * from the item which was selected.
     */
    private void populateItemViews(){
        itemName.setText(selectedItem.getItemName());
        itemId.setText(selectedItem.getItemId());
        description.setText(selectedItem.getDescription());
        price.setText(selectedItem.getUnitPrice());
        locationInRoom.setText(selectedItem.getLocation());
        warrantyExpiration.setText(selectedItem.getWarrantyExpiration());
        quantity.setText(selectedItem.getQuantity()+"");
        owner.setText(selectedItem.getOwner());
        category.setText(selectedItem.getCategory());
        associatedPerson.setText(selectedItem.getAssociatedPerson());
        dateAdded.setText(selectedItem.getDateAdded());

        if(selectedItem.isAvailable()){
            available = true;
        }else{
            available = false;
        }

    }

    private void addImageToItem() {
        Intent i = new Intent(getActivity(), CameraActivity.class);
        String id = "";
        if(isNew){
            id = newItem.getItemId();
        }else{
            id = selectedItem.getItemId();
        }
        i.putExtra(CameraActivity.ID_EXTRA, id);
        startActivityForResult(i, CAMERA_REQUEST_CODE);
    }

    /**
     * helper method to toggle the checkin-checkout buttons because they will never both be clickable simultaneously
     *
     * @param  checkInClickable
     *        iff true, checkIn will be clickable
     *                  checkout will be unclickable
     *        iff false, checkIn will be unclickable
     *                   checkout will be clickable
     */
    private void setCheckInButton(boolean checkInClickable){
        if(checkInClickable){
            checkin.setEnabled(true);
            checkout.setEnabled(false);
        }else{
            checkin.setEnabled(false);
            checkout.setEnabled(true);
        }
    }

    /**
     * method to change views so that they cannot be edited
     */
    private void changeItemViewOptions(boolean canEditViews){

        //if the views can be edited and it is a new item, all fields should be editable
        if(canEditViews){


            if(isNew) {
                Log.e(TAG, "changeItemViewOptions: canEdit == true and isNew == true");
                itemName.setEnabled(true);
                owner.setEnabled(true);
                description.setEnabled(true);
                category.setEnabled(true);
                associatedPerson.setEnabled(true);
                locationInRoom.setEnabled(true);
                price.setEnabled(true);
                quantity.setEnabled(true);
                warrantyExpiration.setEnabled(true);

                checkout.setEnabled(false);
                checkin.setEnabled(false);
                dateAdded.setEnabled(false);
                itemId.setEnabled(false);
            }else {//if the edit button is clicked after selecting an item from the browse or search lists
                Log.e(TAG, "changeItemViewOptions: canEdit == true and the isNew == false");
                description.setEnabled(true);
                associatedPerson.setEnabled(true);

                if(selectedItem.isAvailable()){
                    setCheckInButton(false);
                }else{
                    setCheckInButton(true);
                }

                locationInRoom.setEnabled(true);
                warrantyExpiration.setEnabled(true);


                quantity.setEnabled(false);
                itemId.setEnabled(false);
                category.setEnabled(false);
                itemName.setEnabled(false);
                owner.setEnabled(false);
                dateAdded.setEnabled(false);
                price.setEnabled(false);
            }

        }else{
            //if you cannot edit the items (the save button has been clicked or you have just selected an item from the browse list or search list
            itemName.setEnabled(false);
            owner.setEnabled(false);
            description.setEnabled(false);
            category.setEnabled(false);
            associatedPerson.setEnabled(false);
            checkin.setEnabled(false);
            checkout.setEnabled(false);
            locationInRoom.setEnabled(false);
            price.setEnabled(false);
            quantity.setEnabled(false);
            warrantyExpiration.setEnabled(false);
            dateAdded.setEnabled(false);
            itemId.setEnabled(false);

        }
    }

    /**
     * This method handles the required fields which must be completed before an item can be saved to the inventory.
     * these required fields are {@code itemName, owner, category, quantity}. Each item must have a name, an owner, and a category
     * that must follow the convention of at least three characters, of which the first three may not contain a space.
     * If the quantity entered by the user is 1, the item is set to be NOT consumable
     *      where as if the quantity entered by user is greater than 1, it is set to be consumable
     *      if the quantity is not set by the user, the quantity is set to 1 and the item is set to consumable
     *
     * @return canSave
     *      iff length of {@code itemName} > 2, and {@code itemName.substring(0,3)} does not contain ' '
     *      iff length of {@code owner} > 2, and {@code owner.substring(0,3)} does not contain ' '
     *      iff length of {@code category} > 2, and {@code category.substring(0,3)} does not contain ' '
     *
     *
     */
    private boolean canSave(){
        assert itemName.getText().toString() != null: "Violation of: itemName is not null";
        assert owner.getText().toString() != null: "Violation of: owner is not null";
        assert category.getText().toString() != null: "Violation of: category is not null";
        assert quantity.getText().toString() != null: "Violation of: quantity is not null";


        boolean canSave = true;


        //the text of each view is stored in a String to make handling input easier
        String itemNameText = itemName.getText().toString();
        String ownerText = owner.getText().toString();
        String categoryText = category.getText().toString();
        String quantityText = quantity.getText().toString();
        String associatedPersonText = associatedPerson.getText().toString();

        if(quantityText.equals("")){ //this makes sure that the string is not empty so that an "invalidInt" exception
                                     // does not arise when using Integer.parseInt
            quantity.setText("1");
            quantityText += quantity.getText().toString();
        }



        //these are the required fields which have very specific parameters. See Toast for details.
        //handles the required field itemName
        if((itemNameText.length() <3)){
            canSave = false;
            Toast.makeText(ctext,  "Item name must be at least three characters.", Toast.LENGTH_SHORT).show();
        }else if(itemNameText.substring(0,3).contains(" ")) {
            canSave = false;
            Toast.makeText(ctext,  "Item name must not contain spaces within the first three characters.", Toast.LENGTH_SHORT).show();
        }

        //handles the required field owner
        if((ownerText.length() <3)){
            canSave = false;
            Toast.makeText(ctext,  "Owner must be at least three characters.", Toast.LENGTH_SHORT).show();
        }else if(ownerText.substring(0,3).contains(" ")) {
            canSave = false;
            Toast.makeText(ctext,  "Owner must not contain spaces within the first three characters.", Toast.LENGTH_SHORT).show();
        }

        //handles the required field category
        if((categoryText.length() <3)){
            canSave = false;
            Toast.makeText(ctext,  "Category must be at least three characters.", Toast.LENGTH_SHORT).show();
        }else if(categoryText.substring(0,3).contains(" ")) {
            canSave = false;
            Toast.makeText(ctext,  "Category must not contain spaces within the first three characters.", Toast.LENGTH_SHORT).show();
        }

        //if the quantity is set to greater than one, we must set the item to be consumable
        if(Integer.parseInt(quantityText) > 1){

            //if it is a new item, this is straightforward
            if(isNew){
                newItem.setConsumable();
            }else if(selectedItem.consumable()){ //check if the existing item is already marked as consumable. if so, change quantity
                selectedItem.setQuantity(Integer.parseInt(quantityText));
                Toast.makeText(ctext, itemNameText+" quantity set to "+quantityText, Toast.LENGTH_SHORT).show();
            }else{ //if the selected item is not new, and it is not already marked as consumable, changing the quantity is not allowed
                selectedItem.setQuantity(1);
                quantity.setText("1");
                Toast.makeText(ctext, "This item is not consumable.", Toast.LENGTH_SHORT).show();
            }
        }


        try {//if there is not a valid person associated with the item and it is listed as "unavailable"
            if (!canCheckOut() && !selectedItem.isAvailable()) {
                //meaning the user changed the name field to something invalid while the item is still checked out
                //selectedItem.checkIn();
                Toast.makeText(ctext, "The item cannot be checked out to that person.", Toast.LENGTH_LONG).show();
                canSave = false;
            }
        }catch(NullPointerException n){
            canSave = false;
            Log.e(TAG, "caught in canSave()");
        }

        //return the boolean which verifies the fields are filled in correctly and the button should be clickable
        return canSave;
    }

    private boolean canCheckOut(){

        boolean canCheckOut = true;

        if(!isNew) {
            try {
                String person = associatedPerson.getText().toString();

                if (person.equals("none") || person.equals("") || person.length() < 3) {
                    canCheckOut = false;
                } else if (person.substring(0, 3).contains(" ")) {
                    canCheckOut = false;
                }
            } catch (Exception e) {
                canCheckOut = false;
                Log.e(TAG, "caught in canCheckOut()");

            }
        }

        return canCheckOut;
    }

    /**
     * Helper method to clean up the OnCreate method. Sets onClickListener to each button in the itemFragment
     */
    private void setClickListeners(){

        checkout.setOnClickListener(this);
        checkin.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    /**
     * Helper method to clean up the OnCreate method. Finds the view associated with the resource ID given to it.
     * Instantiates a view object with its proper type given the ID.
     *
     * @param view
     *      needed to reference the resource file which contains the layout of an item fragment.
     */
    private void instViews(View view){

        itemName = (EditText) view.findViewById(R.id.itemName);
        itemId = (EditText) view.findViewById(R.id.itemId);
        description = (EditText) view.findViewById(R.id.description);
        price = (EditText) view.findViewById(R.id.price);
        locationInRoom = (EditText) view.findViewById(R.id.locationInRoom);
        warrantyExpiration = (EditText) view.findViewById(R.id.warrantyExpiration);
        quantity = (EditText) view.findViewById(R.id.quantity);

        category = (AutoCompleteTextView) view.findViewById(R.id.category);
        associatedPerson = (AutoCompleteTextView) view.findViewById(R.id.associatedPerson);
        owner = (AutoCompleteTextView) view.findViewById(R.id.itemOwner);

        dateAdded = (TextView) view.findViewById(R.id.dateAdded);

        cancel = (Button) view.findViewById(R.id.cancel);
        edit = (Button) view.findViewById(R.id.edit);
        save = (Button) view.findViewById(R.id.save);
        checkin = (Button) view.findViewById(R.id.checkIn);
        checkout = (Button) view.findViewById(R.id.checkOut);
    }

    /**
     * When the user is adding a new item to the inventory and presses 'Cancel', this helper method sets views to default value
     */
    private void clearNewItemView(){
        itemName.setText("");
        itemId.setText("");
        description.setText("");
        price.setText("");
        locationInRoom.setText("");
        warrantyExpiration.setText("");
        quantity.setText("");
        owner.setText("");
        category.setText("");
        associatedPerson.setText("");

        isNew = true;
        newItem = new Item();


    }

    /**
     * Helper method that uses the mutators in the Item class to set the item attributes as the
     * text that is contained in the associated View on the fragment
     *
     * @param item
     *      and Item to be added to the inventory (or edited)
     */
    private void saveItem(Item item){
        item.setItemName(itemName.getText().toString());
        item.setQuantity(Integer.parseInt(quantity.getText().toString()));
        item.setCategory(category.getText().toString());
        item.setOwner(owner.getText().toString());
        item.setDescription(description.getText().toString());
        item.setItemId();
        item.setUnitPrice(price.getText().toString());
        item.setAssociatedPerson(associatedPerson.getText().toString());
        item.setLocation(locationInRoom.getText().toString());
        item.setWarrantyExpiration(warrantyExpiration.getText().toString());

        //this available flag is set each time checkin is selected and reset when checkout is selected.
        //the flag is kept here so it is guaranteed to go through "canCheckOut" and "canSave"
        if(available){
            item.checkIn();
        }else{
            item.checkOut();
        }

        if(isNew){
            item.setDateAdded();
            item.setDateChecked();
        }
    }

    private void confirmSave() {

        if (getTargetFragment() != null) {
            Intent i = new Intent();
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
    }
}
