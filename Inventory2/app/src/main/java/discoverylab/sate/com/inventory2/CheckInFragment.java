package discoverylab.sate.com.inventory2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 *
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CheckInFragment extends Fragment implements View.OnClickListener {

    List<Item> itemList ;//= ItemList.checkInList();
    ListView listOfItems;
    final String TAG = "CheckInFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment CheckInFragment.
     */
    public static CheckInFragment newInstance() {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    public CheckInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "creating checkin fragment view.");
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);

        //getting the browse by item button to add clickListener
        Button itemFilter = (Button)view.findViewById(R.id.checkin_browseByItem);
        itemFilter.setOnClickListener(this);

        //get the browse by category button to add clickListener
        Button categoryFilter = (Button)view.findViewById(R.id.checkin_browseByCategory);
        categoryFilter.setOnClickListener(this);

        //get the listView
        listOfItems = (ListView)view.findViewById(R.id.checkin_browseListView);

        //populate itemList
        itemList = ItemList.getInstance().checkInList();


        //create the adapter with passing List as 3rd parameter
        ListItemAdapter arrayAdapter = new ListItemAdapter(getActivity(), R.layout.browse_list_item, itemList);

        //Set the Adapter
        listOfItems.setAdapter(arrayAdapter);

        listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //argument position gives the index of item which was clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3){
                //do something here...like enter a different view
                Item selectedItem = itemList.get(position);
            }
        });

        return view;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.checkin_browseByItem:

                final List<Item> filteredByAll = ItemList.getInstance().filter(ItemList.filterOptions.allItems, itemList);

                ListItemAdapter arrayAdapterAll = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByAll);

                setArrayAdapter(arrayAdapterAll);

                break;
            case R.id.checkin_browseByCategory:
                final List<Item> filteredByCat = ItemList.getInstance().filter(ItemList.filterOptions.categories, itemList);
                ListItemAdapter arrayAdapterCat = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByCat);

                setArrayAdapter(arrayAdapterCat);

                break;
            default:
                Log.e(TAG, "No idea what you clicked.");
        }
    }


    /**
     * This helper method was created to avoid repeated blocks of code in the above switch statement.
     *
     * @param arrayAdapterType
     *        pass in an arrayAdapter because they differ by name
     */
    public void setArrayAdapter(ArrayAdapter<Item> arrayAdapterType){
        //Set the Adapter
        listOfItems.setAdapter(arrayAdapterType);



        listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //argument position gives the index of item which was clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3){
                //do something here...like enter a different view
                Item selectedItem = (Item)arg0.getAdapter().getItem(position);//.get(position);

                showItemDialog(selectedItem);
            }
        });
    }

    private void showItemDialog(Item item) {
        android.app.FragmentManager fm = getFragmentManager();
        ItemFragment itemFrag = new ItemFragment();
        itemFrag.setItem(item);
        itemFrag.setViewOptions(false);
        itemFrag.show(fm, "fragment_edit_name");
    }

}
