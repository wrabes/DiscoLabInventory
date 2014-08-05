package discoverylab.sate.com.inventory2;



import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmCheckIn#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConfirmCheckOut extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "ConfirmCheckOut";

    private Button noButton, yesButton;
    private AutoCompleteTextView associatedPerson;
    private Item itemToCheckOut;
    private PersonList persons;
    private ListView ListPersons;
    private int canConfirm = 0;



    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ConfirmCheckIn.
     */
    public static ConfirmCheckOut newInstance() {
        ConfirmCheckOut fragment = new ConfirmCheckOut();

        return fragment;
    }
    public ConfirmCheckOut() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_check_out, container, false);

        //give the personList data members
        persons = PersonList.getInstance();

        ArrayList<String> personNames = persons.personListNames();


        //TODO make a layout for this fragment
        noButton = (Button) view.findViewById(R.id.Checkout_No_Button);
        yesButton = (Button) view.findViewById(R.id.Checkout_Yes_Button);
        associatedPerson = (AutoCompleteTextView) view.findViewById(R.id.Checkout_associatedPerson);

        //set onClickListeners for Buttons
        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);

        //set onClickListener for textview
        associatedPerson.setOnClickListener(this);

        


        if(getDialog() != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setTitle("Confirm Check-out for "+itemToCheckOut.toString());
        }

        return view;
    }



    @Override
    public void onClick(View view){
        //TODO save the availability if the user confirms the action, or cancel the changes if user negates the actions
        switch(view.getId()){
            case R.id.Checkout_No_Button:
                confirmCheckOut();
                this.dismiss();
                break;
            case R.id.Checkout_Yes_Button:
                itemToCheckOut.checkOut();
                confirmCheckOut();
                this.dismiss();

                break;
            case R.id.Checkout_associatedPerson:
                //TODO do not enable the confirm button until the user selects a user from the list (person list must be populated)

                break;
            default:
                Log.e(TAG, "No idea what you clicked.");


        }
    }


    /**
     * method to mutate the class variable Item so it can be checked
     */
    public void setItem(Item item){
        itemToCheckOut = item;
    }

    private void confirmCheckOut() {

        if (getTargetFragment() != null) {
            // for now just return patient id and note id so requester may retrieve it from the global store.
            Intent i = new Intent();
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
    }


}
