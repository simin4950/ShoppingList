package edu.uga.cs.shoppinglist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DecimalFormat;
import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all job leads.
 */
public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.ShoppingListHolder> {

    public static final String DEBUG_TAG = "JobLeadRecyclerAdapter";

    private List<String[]> shoppingList;


    public PurchasedListRecyclerAdapter( List<String[]> shoppingList ) {
        this.shoppingList = shoppingList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class ShoppingListHolder extends RecyclerView.ViewHolder {
        private Button purchaseButton;

        TextView item, price, email;
        String m_Text = "";



        public ShoppingListHolder(View itemView ) {
            super(itemView);

            item = (TextView) itemView.findViewById(R.id.itemNamePurchase);
            price = (TextView) itemView.findViewById(R.id.price);
            email = (TextView) itemView.findViewById(R.id.email);


        }
    }

    @Override
    public ShoppingListHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.purchase_list_item, parent, false );
        return new ShoppingListHolder( view );
    }

    // This method fills in the values of the Views to show a JobLead
    @Override
    public void onBindViewHolder( ShoppingListHolder holder, int position ) {
        String itemName = shoppingList.get(position)[0];
        String itemPrice = shoppingList.get(position)[1];
        String itemEmail = shoppingList.get(position)[2];
        double doubPrice = (double) (Integer.parseInt(itemPrice))/100;
        DecimalFormat df = new DecimalFormat("#0.00");

        String price = "$ " + df.format(doubPrice);


        Log.d(DEBUG_TAG, "onBindViewHolder: " + itemName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchased");
        DatabaseReference listRef = database.getReference("item");

        holder.item.setText(itemName);
        holder.price.setText(price);
        holder.email.setText(itemEmail);
        /*holder.purchaseButton.setOnClickListener(e -> {

            myRef.child(itemName).setValue(itemName)
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            Toast.makeText(holder.item.getContext(), "Item added to shopping List! " + itemName,
                                    Toast.LENGTH_SHORT).show();
                            listRef.child(itemName).removeValue();
                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.item.getContext());
                            builder.setTitle("Enter the price");

// Set up the input
                            final EditText input = new EditText(holder.item.getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            builder.setView(input);

// Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.m_Text = input.getText().toString();
                                    myRef.child(itemName).child("price").setValue(holder.m_Text);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    //String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    myRef.child(itemName).child("email").setValue(email);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                            shoppingList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, shoppingList.size());


                            // Clear the EditTexts for next use.

                        }
                    })
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(holder.item.getContext(), "FAILED to add item added to shopping List: " + itemName,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
 });
    }*/

    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }
}

