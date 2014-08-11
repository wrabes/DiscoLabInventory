package discoverylab.sate.com.inventory2;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;


/**
 *
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CheckOutFragment extends Fragment implements View.OnClickListener {

    List<Item> itemList;// = ItemList.checkOutList();
    ListView listOfItems;
    final String TAG = "CheckOutFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment CheckInFragment.
     */
    public static CheckOutFragment newInstance() {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    public CheckOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e(TAG, "starting checkout fragment view");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(TAG, "resuming checkout fragment view");
        ((ListItemAdapter)listOfItems.getAdapter()).notifyDataSetChanged();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "creating checkout fragment view.");
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        //TODO: implement the layout xml file with these buttons
        //getting the browse by item button to add clickListener
        Button itemFilter = (Button)view.findViewById(R.id.checkout_browseByItem);
        itemFilter.setOnClickListener(this);

        //get the browse by category button to add clickListener
        Button categoryFilter = (Button)view.findViewById(R.id.checkout_browseByCategory);
        categoryFilter.setOnClickListener(this);

        //get the listView
        listOfItems = (ListView)view.findViewById(R.id.checkout_browseListView);

        //populate itemList
        itemList = ItemList.getInstance().checkOutList();


        //create the adapter with passing List as 3rd parameter
        ListItemAdapter arrayAdapter = new ListItemAdapter(getActivity(), R.layout.browse_list_item, itemList);

        //Set the Adapter
        listOfItems.setAdapter(arrayAdapter);

        listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //argument position gives the index of item which was clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3){
                //do something here...like enter a different view
                Item selectedItem = itemList.get(position);
                showCheckOutDialog(selectedItem);

            }
        });

        return view;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e(TAG, "attaching checkout frag to activity");


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "detaching checkout frag from activity");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.checkout_browseByItem:

                final List<Item> filteredByAll = ItemList.getInstance().filter(ItemList.filterOptions.allItems,itemList);
                ListItemAdapter arrayAdapterAll = new ListItemAdapter(getActivity(), R.layout.browse_list_item, filteredByAll);

                setArrayAdapter(arrayAdapterAll);

                break;
            case R.id.checkout_browseByCategory:
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

                showCheckOutDialog(selectedItem);
            }
        });
    }

    private void showCheckOutDialog(Item item) {
        android.app.FragmentManager fm = getFragmentManager();
        ConfirmCheckOut confirm = new ConfirmCheckOut();
        confirm.setTargetFragment(this, 1);

        //gives the confirm checkIn fragment the item so that it is able to change it based onClick
        confirm.setItem(item);

        //this will show at the top of the dialog
        String dialogTitle = "Confirm Check-out for "+item.getItemName();
        confirm.show(fm, dialogTitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            itemList = ItemList.getInstance().checkOutList();

            ListItemAdapter arrayAdapterAll = new ListItemAdapter(getActivity(), R.layout.browse_list_item, itemList);
            setArrayAdapter(arrayAdapterAll);

            getActivity().onContentChanged();

        }


    }

}
