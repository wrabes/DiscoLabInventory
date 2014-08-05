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
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmCheckIn#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConfirmCheckIn extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "ConfirmCheckIn";

    private Button noButton, yesButton;
    private Item itemToCheckIn;



    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ConfirmCheckIn.
     */
    public static ConfirmCheckIn newInstance() {
        ConfirmCheckIn fragment = new ConfirmCheckIn();

        return fragment;
    }
    public ConfirmCheckIn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_check_in, container, false);

        noButton = (Button) view.findViewById(R.id.Checkin_No_Button);
        yesButton = (Button) view.findViewById(R.id.Checkin_Yes_Button);

        //set onClickListeners for Buttons
        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);

        if(getDialog() != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setTitle("Confirm Check-in for "+itemToCheckIn.toString());
        }

        return view;
    }



    @Override
    public void onClick(View view){
        //TODO save the availability if the user confirms the action, or cancel the changes if user negates the actions
        switch(view.getId()){
            case R.id.Checkin_No_Button:
                confirmCheckin();
                this.dismiss();
                break;
            case R.id.Checkin_Yes_Button:
                itemToCheckIn.checkIn();
                confirmCheckin();

                //this line gets a reference to the sectons pager adapter in the main activity,
                //and updates to reflect the changes made. Used to synchronize changes made in one fragment as a workaround
                //to the ViewPager which caches each fragment that is on either "side" of the currently viewed tab.
                ((MainActivity)getActivity()).getmSectionsPagerAdapter().notifyDataSetChanged();
                this.dismiss();

                break;
            default:
                Log.e(TAG, "No idea what you clicked.");


        }
    }


    /**
     * method to mutate the class variable Item so it can be checked
     */
    public void setItem(Item item){
        itemToCheckIn = item;
    }

    private void confirmCheckin() {

        if (getTargetFragment() != null) {
            Intent i = new Intent();
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

        }
    }


}
