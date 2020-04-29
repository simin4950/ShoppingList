package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
* MainActivity class acts as a SplashScreen for the QuizApp
 *
 * @Author Ishita Soni
 * @Author Simin Savani
*/
public class MainActivity extends AppCompatActivity {
    // Variables for debugging purposes 
    private static final String DEBUG_TAG = "MainActivity";

    private static final int RC_SIGN_IN = 123;
    
    /**
    * onCreate is a method that creates the view for MainActivity.
    * @param savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onCreate()" );
        
        // Elements from layout
        Button signInButton = findViewById( R.id.button1 );
        Button registerButton = findViewById( R.id.button2 );
        
        // On click listeners for buttons
        signInButton.setOnClickListener( new SignInButtonClickListener() );
        registerButton.setOnClickListener( new RegisterButtonClickListener() );

        // Firebase intialization
        FirebaseApp.initializeApp(this);
        // Check if user is signed in and if signed in, sign the user out before proceeding.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null )
            mAuth.signOut();
    }
    
    /**
    * SignInButtonClickListener is a class where onClick is implemented for 
    * SignIn button in the layout
    */
    private class SignInButtonClickListener implements View.OnClickListener {
        /**
        * onClick is a method that implements the action that occurs after 
        * sign-in button is clicked 
        * @param v
        */
        @Override
        public void onClick( View v ) {
            // Here, we are just using email/password sign in
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );

            // Start a sign in activity, but come back with a result
            // The last argument is the code of the result from this call
            // It is used later in the onActivityResult listener to identify
            // the result as a result from this startActivityForResult call.
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    /**
    * RegisterButtonClickListener is a class where onClick is implemented for 
    * Register button in the layout
    */
    private class RegisterButtonClickListener implements View.OnClickListener {
        /**
        * onClick is a method that implements the action that occurs after 
        * sign-in button is clicked 
        * @param v
        */
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    // this is a listener (event handler) which is called by android when the
    // activity started by a startActivityForResult finishes and we come back
    // to the originating activity;  we need to get the result from the activity
    // that just ended.  The second argument is the code of the startActivityForResult
    // call (there may be several of them).
    /**
    * onActivityResult is an event handler called by android when startActivityFor Result 
    * is started.
    * @param requestCode, resultCode, data
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d( DEBUG_TAG, "JobLead: MainActivity.onActivityResult()" );

        // check if it is a sign in activity result
        if( requestCode == RC_SIGN_IN ) {
            IdpResponse response = IdpResponse.fromResultIntent( data );

            if( resultCode == RESULT_OK ) {

                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Log.i( "FireBase Test", "Signed in as: " + user.getEmail() );

                // after a successful sign in, start the job leads management activity
                Intent intent = new Intent( this, ShoppingManagementActivity.class );
                startActivity( intent );

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in, e.g., by using the back button
                Toast.makeText( getApplicationContext(),
                        "Sign in failed",
                        Toast.LENGTH_SHORT).show();            }
        }
    }

    // These activity callback methods are not needed and are for edational purposes only
    /**
    * onStart is a method called by android to start the main activity 
    */
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onStart()" );
        super.onStart();
    }

    /**
    * onResume is a method called by android to resume the main activity 
    */
    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onResume()" );
        super.onResume();
    }

    /**
    * onPause is a method called by android to pause the main activity 
    */
    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onPause()" );
        super.onPause();
    }

    /**
    * onStop is a method called by android to stop the main activity 
    */
    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onStop()" );
        super.onStop();
    }

    /**
    * onDestroy is a method called by android to destroy the main activity 
    */
    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onDestroy()" );
        super.onDestroy();
    }

    /**
    * onRestart is a method called by android to restart the main activity 
    */
    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "JobLead: MainActivity.onRestart()" );
        super.onRestart();
    }
}
