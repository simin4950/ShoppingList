package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
* RegisterActivty class acts as a Register page for new users 
 *
 * @Author Ishita Soni
 * @Author Simin Savani
*/
public class RegisterActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "RegisterActivity";
    
    // Elements from the layout
    private EditText emailEditText;
    private EditText passworEditText;
    private Button   registerButton;

    /**
    * onCreate is a method that creates the view for RegisterActivity.
    * @param savedInstanceState
    */    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Set up TextViews on the layout
        emailEditText = (EditText) findViewById( R.id.editText );
        passworEditText = (EditText) findViewById( R.id.editText5 );
        
        // Set up Button on the layout and add onClick Listener 
        registerButton = (Button) findViewById( R.id.button3 );
        registerButton.setOnClickListener( new RegisterButtonClickListener() );
    }

    /**
    * RegisterButtonClickListener is a class where onClick is implemented for 
    * Register button in the layout
    */
    private class RegisterButtonClickListener implements View.OnClickListener {
        /**
        * onClick is a method that implements the action that occurs after 
        * register button is clicked 
        * @param view 
        */
        @Override
        public void onClick(View view) {
            final String email = emailEditText.getText().toString();
            final String password = passworEditText.getText().toString();

            final FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // This is how we can create a new user using an email/password combination.
            // Note that we also add an onComplete listener, which will be invoked once
            // a new user has been created by Firebase.  This is how we will know the
            // new user creation succeeded or failed.
            // If a new user has been created, Firebase already signs in the new user;
            // no separate sign in is needed.
            mAuth.createUserWithEmailAndPassword( email, password )
                    .addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText( getApplicationContext(),
                                        "Registered user: " + email,
                                        Toast.LENGTH_SHORT ).show();

                                // Sign in success, update UI with the signed-in user's information
                                Log.d( DEBUG_TAG, "createUserWithEmail: success" );

                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent intent = new Intent( RegisterActivity.this, ShoppingManagementActivity.class );
                                startActivity( intent );

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(DEBUG_TAG, "createUserWithEmail: failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
