package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = " something is wtong";
    private SignInButton signInButton;

    private FirebaseAuth mAuth;

    private final static int RC_SIGN_IN = 1;
    FirebaseAuth.AuthStateListener mAuthlistener;
    public int count = 0;

   private static final int GOOGLE_SIGN_IN = 123;
    GoogleSignInClient mGoogleSignInClient;

    private Sharedprefernececonfig sharedprefernececonfig;
    private TextView UserName, UserPassword;
    private ImageView imageView;
    ProgressBar progressBar;
    private TextView textView1, textView2;
    private CallbackManager callbackManager;


   /* @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthlistener);
    }*/
   @Override
   public void onStart() {
       super.onStart();

       // Check if user is signed in (non-null) and update UI accordingly.
       if(mAuth.getCurrentUser()!=null) {
           FirebaseUser currentUser = mAuth.getCurrentUser();

           updateUI(currentUser);
       }
   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();



        sharedprefernececonfig = new Sharedprefernececonfig(getApplicationContext());
        UserName = findViewById(R.id.user_name_text);
        UserPassword = findViewById(R.id.user_name_password);
        signInButton = (SignInButton) findViewById(R.id.google_button);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

       /*   mAuthlistener=new FirebaseAuth.AuthStateListener() {
              @Override
              public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
             if(firebaseAuth.getCurrentUser()!=null){

                 startActivity(new Intent(MainActivity.this,User_signin.class));

             }


              }
          };*/


        if (sharedprefernececonfig.readloginstatus()) {
            startActivity(new Intent(this, User_signin.class));
            finish();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(v -> SignInGoogle());

        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

    }


    public void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
        else{
            Toast.makeText(MainActivity.this,"auth is wrong",Toast.LENGTH_LONG).show();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.d("TAG", "signInWithCredential:success");

                        FirebaseUser user = mAuth.getCurrentUser();
                        Prevalent.currentOnlineUser=user;
                        updateUI(user);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.w("TAG", "signInWithCredential:failure", task.getException());

                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

        private void updateUI (FirebaseUser user){
            if (user != null) {
                String email = user.getEmail();
                String admin = "justfollowme2017@gmail.com";
                if (email.matches(admin)) {
                    startActivity(new Intent(MainActivity.this, Admin.class));
                    }
                    else {
                    Prevalent.currentOnlineUser=user;
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
            else{

            }
        }



   /* private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Toast.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this,"Authentication Failed.",Toast.LENGTH_LONG).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });


    }*/

        public void userlogin (View view){

            String username1 = UserName.getText().toString().trim();
            String userpassword1 = UserPassword.getText().toString().trim();
            if(username1.isEmpty()){
                UserName.setError("Email is required");
                UserName.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(username1).matches()){
                UserName.setError("Please Enter a Valid User");
                UserName.requestFocus();
                return;
            }
            if(userpassword1.isEmpty()){
                UserPassword.setError("Please Enter a Password");
                UserPassword.requestFocus();
                return;
            }
            if(userpassword1.length()<6){
                UserPassword.setError("Minimum length of a Password should be 6");
                UserPassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(username1,userpassword1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Intent i=new Intent(MainActivity.this, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

        public void usersignup (View view){

            startActivity(new Intent(this, Signup.class));
        }

    }
