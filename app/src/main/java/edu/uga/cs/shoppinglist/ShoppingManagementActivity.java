package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingManagementActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "ManagementActivity";

    private TextView signedInTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button addItem, purchaseList;
    private EditText input;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private List<String> shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_management);

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

        addItem = findViewById(R.id.button);
        purchaseList = findViewById(R.id.settleButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("item");
        recyclerView = (RecyclerView) findViewById(R.id.settleView);

        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );
        shoppingList = new ArrayList<String>();


        addItem.setOnClickListener(e -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the product");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = input.getText().toString();
                    myRef.child(item).setValue(item)
                            .addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Show a quick confirmation
                                    Toast.makeText(getApplicationContext(), item + " added to shopping List! ",
                                            Toast.LENGTH_SHORT).show();

                                    // Clear the EditTexts for next use.
                                    input.setText("");

                                }
                            })
                            .addOnFailureListener( new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getApplicationContext(), item + " was not added to shopping List: " ,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    finish();
                    startActivity(getIntent());

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();



        });
        purchaseList.setOnClickListener(e-> {
            Intent intent = new Intent(this, PurchasedListManagementActivity.class);
            startActivity(intent);
        });

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                Log.d( "ENTER", "ENTERED LISTENER ");
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    String listItem = postSnapshot.getValue().toString();
                    shoppingList.add(listItem);
                    Log.d( DEBUG_TAG, "ShoppingManagementActivity.onCreate(): added: " + listItem );
                }
                Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a JobLeadRecyclerAdapter to populate a ReceyclerView to display the job leads.
                recyclerAdapter = new ShoppingListRecyclerAdapter( shoppingList);
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


