package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword, getEditTextPasswordsecond,editText_phonenumber;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    private Button signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.user_name);
        editTextEmail = findViewById(R.id.user_email);
        editTextPassword = findViewById(R.id.user_password_first);
        getEditTextPasswordsecond = findViewById(R.id.user_password_second);
        editText_phonenumber=findViewById(R.id.user_phone);

        mAuth=FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        checkBox=findViewById(R.id.chkBox1);
        signup=findViewById(R.id.btn_signUp);
       // progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            mAuth.signOut();
        }
    }
    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = getEditTextPasswordsecond.getText().toString().trim();
        final String phone = editText_phonenumber.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }
        if(password2.isEmpty()){
            getEditTextPasswordsecond.setError(getString(R.string.input_error_password));
            getEditTextPasswordsecond.requestFocus();
            return;
        }
        if(password2.length()<6){
                getEditTextPasswordsecond.setError(getString(R.string.input_error_password_length));
                 getEditTextPasswordsecond.requestFocus();
               return;
        }
        if(!password.equals(password2)){
            getEditTextPasswordsecond.setError(getString(R.string.input_error_password_doesnot_match));
            getEditTextPasswordsecond.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editText_phonenumber.setError(getString(R.string.input_error_phone));
            editText_phonenumber.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            editText_phonenumber.setError(getString(R.string.input_error_phone_invalid));
            editText_phonenumber.requestFocus();
            return;
        }
        if(!checkBox.isChecked()){

            checkBox.setError(getString(R.string.input_error_checkbox));
            checkBox.requestFocus();
            return;
        }
       progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    User user=new User(
                            name,
                            email,
                            phone);
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                Toast.makeText(Signup.this,"successfully register",Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                Intent i=new Intent(Signup.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                            else {
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(Signup.this,"User Already Register..",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(Signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(Signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*Toast.makeText(this,"SignUp is sucessfull..",Toast .LENGTH_LONG).show();
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);*/
    }

    public void signupmethod(View view) {

        registerUser();
        mAuth.signOut();

        // finish();
    }

    public void alreadylogin(View view) {
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
   // finish();
    }
}
