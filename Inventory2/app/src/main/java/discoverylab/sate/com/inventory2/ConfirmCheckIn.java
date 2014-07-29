package discoverylab.sate.com.inventory2;



import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmCheckIn#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConfirmCheckIn extends DialogFragment implements View.OnClickListener{



    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ConfirmCheckIn.
     */
    public static ConfirmCheckIn newInstance() {
        ConfirmCheckIn fragment = new ConfirmCheckIn();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_check_in, container, false);
    }

    @Override
    public void onClick(View view){
        //TODO save the availability if the user confirms the action, or cancel the changes if user negates the actions
    }


}
