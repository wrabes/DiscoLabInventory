package discoverylab.sate.com.inventory2;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BrowseFragment extends Fragment implements View.OnClickListener {


    List<Item> itemList;
    ListView listOfItems;
    int counter=0;


    private static final String TAG = "BrowseFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BrowseFragment.
     */
    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }
    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "creating browse fragment view.");
       View view = inflater.inflate(R.layout.fragment_browse, container, false);

        //getting the browse by item button to add clickListener
        Button itemFilter = (Button)view.findViewById(R.id.browseByItem);
        itemFilter.setOnClickListener(this);

        //get the browse by category button to add clickListener
        Button categoryFilter = (Button)view.findViewById(R.id.browseByCategory);
        categoryFilter.setOnClickListener(this);

        //get the browse by availability button to add clickListener
        Button availabilityFilter = (Button)view.findViewById(R.id.browseByAvailability);
        availabilityFilter.setOnClickListener(this);


        //get the listView
        listOfItems = (ListView)view.findViewById(R.id.browseListView);

        //populate itemList
        itemList = ItemList.getInstance().getSortedItemList();


        //create the adapter with passing List as 3rd parameter
        ListItemAdapter arrayAdapter = new ListItemAdapter(getActivity(), R.layout.browse_list_item, itemList);

        //Set the Adapter
        listOfItems.setAdapter(arrayAdapter);


        listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //argument position gives the index of item which was clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3){
                //do something here...like enter a different view
                Item selectedItem = itemList.get(position);

                //show dialog
                showItemDialog(selectedItem);

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

        switch(view.getId()){
            case R.id.browseByItem:

                final List<Item> filteredByAll = ItemList.getInstance().filter(ItemList.filterOptions.allItems, ItemList.getInstance().masterList());
                ListItemAdapter arrayAdapterAll = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByAll);

                setArrayAdapter(arrayAdapterAll);

                break;
            case R.id.browseByCategory:
                final List<Item> filteredByCat = ItemList.getInstance().filter(ItemList.filterOptions.categories, ItemList.getInstance().masterList());
                ListItemAdapter arrayAdapterCat = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByCat);

                setArrayAdapter(arrayAdapterCat);

                break;
            case R.id.browseByAvailability:

                //for available first, counter == 0
                if(counter == 0) {
                    final List<Item> filteredByAvailable = ItemList.getInstance().filter(ItemList.filterOptions.available, ItemList.getInstance().masterList());
                    ListItemAdapter arrayAdapterAvailable = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByAvailable);

                    //sets the adapter here
                    setArrayAdapter(arrayAdapterAvailable);

                    //set the counter so next time it is clicked it changes the sort method
                    counter = 1;
                }
                //for unavailable first, counter == 1;
                else {

                    final List<Item> filteredByUnavailable = ItemList.getInstance().filter(ItemList.filterOptions.unavailable, ItemList.getInstance().masterList());
                    ListItemAdapter arrayAdapterUnavailable = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByUnavailable);

                    //set the adapter here
                    setArrayAdapter(arrayAdapterUnavailable);

                    //reset the counter so next time it is clicked it changes the sort method
                    counter = 0;
                }

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



                //show dialog
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
