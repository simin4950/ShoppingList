package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchasedListManagementActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "ManagementActivity";

    private TextView signedInTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button costButton, settleButton;
    private EditText input;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private List<String[]> purchasedList;
    private HashMap<String, Double> hs = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_list_management);

        signedInTextView = findViewById( R.id.costHeader);

        // Setup a listener for a change in the sign in status (authentication status change)
        // when it is invoked, check if a user is signed in and update the UI text view string,
        // as needed.
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if( currentUser != null ) {
                    // User is signed in
                    Log.d(DEBUG_TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Signed in as: " + userEmail );
                } else {
                    // User is signed out
                    Log.d( DEBUG_TAG, "onAuthStateChanged:signed_out" );
                    signedInTextView.setText( "Signed in as: not signed in" );
                }
            }
        });

        costButton = findViewById(R.id.settleButton);
        settleButton = findViewById(R.id.realSettle);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchased");
        recyclerView = (RecyclerView) findViewById(R.id.settleView);

        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );
        purchasedList = new ArrayList<>();
        settleButton.setOnClickListener(e-> {
            purchasedList.clear();
            DatabaseReference listRef = database.getReference("purchased");
            listRef.removeValue();
            finish();
            startActivity(getIntent());
        });
        costButton.setOnClickListener(e-> {
            hs.put("simin4950", 0.00);
            hs.put("rnbowsky", 0.00);
            hs.put("soni.ishita", 0.00);
            for (int i = 0; i < purchasedList.size(); i++) {
                String user = purchasedList.get(i)[2];
                String cost = purchasedList.get(i)[1];
                Log.d("COST: " , "" + cost);
                double cost1 =(double) (Integer.parseInt(cost))/100;
                Log.d("PRICE: $", "" + cost1);


                   double currentCost =  hs.get(user);
                   hs.put(user, currentCost + cost1);


            }
            String everything = "";
            for (Map.Entry<String, Double> entry : hs.entrySet()) {
               String userView =  entry.getKey();
               double priceView =  entry.getValue();
                DecimalFormat df = new DecimalFormat("#0.00");

               everything += "User: " + userView + "  Total purchase Amount: $ " + df.format(priceView) + '\n';

            }
            TextView roommate = findViewById(R.id.person1);
            roommate.setText(everything);
            });



        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                Log.d( "ENTER", "ENTERED LISTENER ");
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    String itemName = postSnapshot.getKey();
                    Log.d("ITEM REFERENCE: ", itemName);
                    String itemPrice = postSnapshot.child("price").getValue().toString();
                    String itemEmail = postSnapshot.child("email").getValue().toString();
                    String[] addArray = new String[3];
                    addArray[0] = itemName;
                    addArray[1] = itemPrice;
                    addArray[2] = itemEmail;
                    purchasedList.add(addArray);
                    Log.d( DEBUG_TAG, "ShoppingManagementActivity.onCreate(): added: " + addArray.toString() );
                }
                Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a JobLeadRecyclerAdapter to populate a ReceyclerView to display the job leads.
                recyclerAdapter = new PurchasedListRecyclerAdapter(purchasedList);
                recyclerView.setAdapter( recyclerAdapter );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );



        // get a Firebase DB instance reference





    }

}


