package discoverylab.sate.com.inventory2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for the list items
 * Modified by Cameron on 7/15/14.
 * Created by James on 7/4/14
 *
 */
public class ListItemAdapter extends ArrayAdapter<Item>{

    public ListItemAdapter(Context context, int resource, List<Item> itemList){
        super(context, resource, itemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            //need a new view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.browse_list_item, null);
        }

        //get item
        Item item = getItem(position);

        //get views
        TextView itemView = (TextView)convertView.findViewById(R.id.list_item_name_text);
        TextView personView = (TextView)convertView.findViewById(R.id.list_person_name_text);

        //get item checkout time and availability
        String date = "";
        String person = item.getAssociatedPerson();

        //get a color ready for the background of the person


        //GradientDrawable rightSideAvailable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, greens);
       // GradientDrawable rightSideUnavailable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, reds);


        //if the item is checked out, return a checkout date
        if(!item.isAvailable()) {
            date = "Checked out on: " + item.getDateChecked();
            personView.setBackgroundResource(R.drawable.menu_unavailable_background);

        }else{
            date = "Checked in on: " + item.getDateChecked();
            personView.setBackgroundResource(R.drawable.menu_available_background);

        }
        //build a string for this box with two lines
        String personBox = "\n"+person +"\n"+ date+"\n" ;

        //get item name and
        String itemName = item.getItemName();
        String itemId = item.getItemId();
        //build a string for this box with two lines
        String itemBox = "\n"+itemName  + "\n#"+itemId+ "\n";

        //set the text and color for each textView
        itemView.setText(itemBox);

        personView.setText(personBox);


        return convertView;
    }


}
