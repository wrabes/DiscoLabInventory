package discoverylab.sate.com.inventory2;

import android.app.Activity;
import android.app.DialogFragment;
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
        }


        return view;
    }

    @Override
    public void onClick(View view){

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }




    @Override
    public void onDetach() {
        super.onDetach();
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

        //make sure the edit is already selected

        //make sure save cannot be selected

    }



    /**
     *
     * @return true if any of the fields are being edited or have been edited
     */
    //TODO be able to determine if the user is editing at the moment
    public boolean isEditing(){
        return false;
    }

    /**
     * method to populate the dialog
     */
    public void setItem(Item item){
        selectedItem = item;
    }

    /**
     * method to change views so that they cannot be edited
     */
    private void canEdit(){

    }


}
