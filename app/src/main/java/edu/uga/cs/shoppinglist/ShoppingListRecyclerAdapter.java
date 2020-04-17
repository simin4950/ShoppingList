package edu.uga.cs.shoppinglist;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all job leads.
 */
public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListHolder> {

    public static final String DEBUG_TAG = "JobLeadRecyclerAdapter";

    private List<String> shoppingList;

    public ShoppingListRecyclerAdapter( List<String> shoppingList ) {
        this.shoppingList = shoppingList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class ShoppingListHolder extends RecyclerView.ViewHolder {

        TextView item;


        public ShoppingListHolder(View itemView ) {
            super(itemView);

            item = (TextView) itemView.findViewById(R.id.companyName);

        }
    }

    @Override
    public ShoppingListHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.list_item, parent, false );
        return new ShoppingListHolder( view );
    }

    // This method fills in the values of the Views to show a JobLead
    @Override
    public void onBindViewHolder( ShoppingListHolder holder, int position ) {
        String itemName = shoppingList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + itemName );

        holder.item.setText( itemName);
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }
}
