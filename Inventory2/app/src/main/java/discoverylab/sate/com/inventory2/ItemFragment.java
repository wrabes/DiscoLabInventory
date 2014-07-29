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
    private EditText itemName;
    private EditText itemId;
    private EditText description;
    private EditText price;
    private EditText locationInRoom;
    private EditText warrantyExpiration;
    private EditText quantity;
    private AutoCompleteTextView category;
    private AutoCompleteTextView associatedPerson;
    private TextView dateAdded;
    private Button cancel, edit, save, checkin, checkout;
    Item selectedItem;
    boolean isNew;




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();

        return fragment;
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
     * @param cancel
     *      button to cancel will be clickable
     * @param edit
     *      button to edit will be selected
     * @param save
     *      button to save will not be clickable
     */
    public void changeViewOptionsNew(Button cancel, Button edit, Button save){
        //make sure the user can cancel

        //make sure the edit is already selected

        //make sure save cannot be selected

    }

    /**
     *
     * @param cancel
     *      button to cancel will be clickable
     * @param edit
     *      button to edit will be deselected
     * @param save
     *      button to save will be clickable
     */
    public void changeViewOptionsOnEdit(Button cancel, Button edit, Button save){
        //the user can save

        //edit can be deselected

        //the user can cancel
    }

    /**
     *
     * @param cancel
     *      button to cancel will be clickable
     * @param edit
     *      button to edit will be clickable
     * @param save
     *      button to save will not be clickable
     */
    public void changeViewOptionsOnExisting(Button cancel, Button edit, Button save){

        //the user can cancel (takes them back to the list)

        //the user can select edit

        //the user cannot select save

    }

    /**
     *
     * @return true if any of the fields are being edited or have been edited
     */
    public boolean isEditing(){
        return false;
    }

    /**
     * method to populate the dialog
     */
    public void setItem(Item item){
        selectedItem = item;
    }


}
