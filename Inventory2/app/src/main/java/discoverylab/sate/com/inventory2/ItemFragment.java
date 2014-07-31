package discoverylab.sate.com.inventory2;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
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
        Log.e(TAG, "creating item fragment view.");

        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        //EditText instantiations
        itemName = (EditText) view.findViewById(R.id.itemName);
        itemId = (EditText) view.findViewById(R.id.itemId);
        description = (EditText) view.findViewById(R.id.description);
        price = (EditText) view.findViewById(R.id.price);
        locationInRoom = (EditText) view.findViewById(R.id.locationInRoom);
        warrantyExpiration = (EditText) view.findViewById(R.id.warrantyExpiration);
        quantity = (EditText) view.findViewById(R.id.quantity);

        //AutoCompleteTextView instantiations
        category = (AutoCompleteTextView) view.findViewById(R.id.category);
        associatedPerson = (AutoCompleteTextView) view.findViewById(R.id.associatedPerson);
        owner = (AutoCompleteTextView) view.findViewById(R.id.itemOwner);

        //TextView instantiations
        dateAdded = (TextView) view.findViewById(R.id.dateAdded);

        //Button instantiations
        cancel = (Button) view.findViewById(R.id.cancel);
        edit = (Button) view.findViewById(R.id.edit);
        save = (Button) view.findViewById(R.id.save);
        checkin = (Button) view.findViewById(R.id.checkIn);
        checkout = (Button) view.findViewById(R.id.checkOut);


        //set onClickListeners for Buttons
        cancel.setOnClickListener(this);
        edit.setOnClickListener(this);
        save.setOnClickListener(this);


        if(getDialog() != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setTitle("View Item");
        }

        if(!isNew){
            changeViewOptions(true, true, false);
            //test this
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
        }else{

            changeViewOptions(true, false, true);
        }

        return view;
    }

    @Override
    public void onClick(View view){

        switch(view.getId()){
            case R.id.cancel:
                if(getDialog()!=null){
                    getDialog().dismiss();
                }else{
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
                }
                break;
            case R.id.edit:
                break;
            case R.id.save:
//                itemName.setText(selectedItem.getItemName());
//                itemId.setText(selectedItem.getItemId());
//                description.setText(selectedItem.getDescription());
//                price.setText(selectedItem.getUnitPrice());
//                locationInRoom.setText(selectedItem.getLocation());
//                warrantyExpiration.setText(selectedItem.getWarrantyExpiration());
//                quantity.setText(selectedItem.getQuantity()+"");
//                owner.setText(selectedItem.getOwner());
//                category.setText(selectedItem.getCategory());
//                associatedPerson.setText(selectedItem.getAssociatedPerson());

                //if the item is a new item (accessed from the AddItem fragment) and the fields are correctly given
                if(isNew && canSave()) {
                    newItem.setItemName(itemName.getText().toString());
                    newItem.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    newItem.setCategory(category.getText().toString());
                    newItem.setOwner(owner.getText().toString());
                    newItem.setDescription(description.getText().toString());
                    newItem.setItemId(itemId.getText().toString());
                    newItem.setUnitPrice(price.getText().toString());
                    newItem.setAssociatedPerson(associatedPerson.getText().toString());
                    newItem.setLocation(locationInRoom.getText().toString());
                    newItem.setWarrantyExpiration(warrantyExpiration.getText().toString());

                    newItem.checkIn();
                    newItem.setDateAdded();

                    Toast.makeText(ctext, newItem.getItemName()+" successfully added to the inventory.", Toast.LENGTH_SHORT).show();

                    ItemList.getInstance().setItems(newItem);


                }else if(!isNew && canSave()){
                    selectedItem.setItemName(itemName.getText().toString());
                    selectedItem.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    selectedItem.setCategory(category.getText().toString());
                    selectedItem.setOwner(owner.getText().toString());
                    selectedItem.setDescription(description.getText().toString());
                    selectedItem.setItemId(itemId.getText().toString());
                    selectedItem.setUnitPrice(price.getText().toString());
                    selectedItem.setAssociatedPerson(associatedPerson.getText().toString());
                    selectedItem.setLocation(locationInRoom.getText().toString());
                    selectedItem.setWarrantyExpiration(warrantyExpiration.getText().toString());


                    ItemList.getInstance().setItems(selectedItem);
                    Toast.makeText(ctext, "Changes to "+selectedItem.getItemName()+" successfully saved.", Toast.LENGTH_SHORT).show();
                }

                //make a new item now
                setViewOptions(true);
                newItem = new Item();

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
     * @param cancelAllowed
     *      boolean value will be true always (i.e. the user can always click the cancel button)
     *
     * @param editAllowed
     *      boolean value will be true when selecting an item from the browse/search fragment
     *      boolean value will be false when selecting the AddItem fragment because the default mode is edit
     *      boolean value will be false after selecting it once
     *
     * @param saveAllowed
     *      boolean value will be true when edit is false and required fields have not yet been populated
     */
    public void changeViewOptions(boolean cancelAllowed, boolean editAllowed, boolean saveAllowed){
        //make sure the user can cancel
        cancelAllowed = true;
        cancel.setClickable(true);


        //handle the edit button and options associated
        //if editing is allowed, the button should be clickable (only if existing item) and edit button is not already selected
        if(editAllowed){
            edit.setClickable(true);
            edit.setEnabled(true);

            save.setClickable(false);
            save.setEnabled(false);
        }else{
            edit.setClickable(false);
            edit.setEnabled(false);

            //this must mean that the user is already editing the item fields. the canEdit method handles which views to make editable
            canEdit(true);

            //TODO we need to check for proper input somewhere

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
     * method to change views so that they cannot be edited
     */
    private void canEdit(boolean canEdit){

        //if the views can be edited and it is a new item, all fields should be editable
        if(canEdit && isNew){
            itemName.setClickable(true);
            owner.setClickable(true);
            description.setClickable(true);
            category.setClickable(true);
            associatedPerson.setClickable(true);
            checkin.setClickable(true);
            checkout.setClickable(true);
            locationInRoom.setClickable(true);
            price.setClickable(true);
            quantity.setClickable(true);
            warrantyExpiration.setClickable(true);

            dateAdded.setClickable(false);
            itemId.setClickable(false);
        }else if(canEdit && !isNew){
            //if the edit button is clicked after selecting an item from the browse or search lists
            description.setClickable(true);
            associatedPerson.setClickable(true);
            checkin.setClickable(true);
            checkout.setClickable(true);
            locationInRoom.setClickable(true);
            warrantyExpiration.setClickable(true);


            quantity.setClickable(false);
            itemId.setClickable(false);
            category.setClickable(false);
            itemName.setClickable(false);
            owner.setClickable(false);
            dateAdded.setClickable(false);
            price.setClickable(false);
        }else{
            //if you cannot edit the items (the save button has been clicked or you have just selected an item from the browse list or search list
            itemName.setClickable(false);
            owner.setClickable(false);
            description.setClickable(false);
            category.setClickable(false);
            associatedPerson.setClickable(false);
            checkin.setClickable(false);
            checkout.setClickable(false);
            locationInRoom.setClickable(false);
            price.setClickable(false);
            quantity.setClickable(false);
            warrantyExpiration.setClickable(false);
            dateAdded.setClickable(false);
            itemId.setClickable(false);

        }
    }

    public boolean canSave(){
        assert itemName.getText().toString() != null: "Violation of: itemName is not null";
        assert owner.getText().toString() != null: "Violation of: owner is not null";
        assert category.getText().toString() != null: "Violation of: category is not null";
        assert quantity.getText().toString() != null: "Violation of: quantity is not null";


        boolean canSave = true;


        String itemNameText = itemName.getText().toString();
        String ownerText = owner.getText().toString();
        String categoryText = category.getText().toString();
        String quantityText = quantity.getText().toString();
        if(quantityText.equals("")){ //this makes sure that the string is not empty so that an "invalidInt" exception
                                     // does not arise when using Integer.parseInt
            quantity.setText("1");
            quantityText += quantity.getText().toString();
        }



        //these are the required fields which have very specific parameters. See Toast for details.
        if((itemNameText.length() <3)){
            canSave = false;
            Toast.makeText(ctext,  "Item name must be at least three characters.", Toast.LENGTH_SHORT).show();
        }else if(itemNameText.substring(0,3).contains(" ")) {
            canSave = false;
            Toast.makeText(ctext,  "Item name must not contain spaces within the first three characters.", Toast.LENGTH_SHORT).show();
        }

        if((ownerText.length() <3)){
            canSave = false;
            Toast.makeText(ctext,  "Owner must be at least three characters.", Toast.LENGTH_SHORT).show();
        }else if(ownerText.substring(0,3).contains(" ")) {
            canSave = false;
            Toast.makeText(ctext,  "Owner must not contain spaces within the first three characters.", Toast.LENGTH_SHORT).show();
        }

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

        //return the boolean which verifies the fields are filled in correctly and the button should be clickable
        return canSave;
    }


}
