package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {
    private Sharedprefernececonfig sharedprefernececonfig;

    private Button button,checkorder;
    private ImageView tshirt,glasses,watches,hatss,headphones,shoes;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tshirt=(ImageView)findViewById(R.id.tshirt);
        watches=(ImageView)findViewById(R.id.watches);
        shoes=(ImageView)findViewById(R.id.shoes);
        hatss=(ImageView)findViewById(R.id.cap);
        headphones=(ImageView)findViewById(R.id.headphones);
        glasses=(ImageView)findViewById(R.id.glasses);
        checkorder=findViewById(R.id.checkorder);



        mAuth=FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Admin.this, MainActivity.class));
                    Toast.makeText(Admin.this, "user logout....", Toast.LENGTH_LONG).show();
                }
            }
        };


       tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,Add_product.class);
                intent.putExtra("category","tshirts");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,Add_product.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,Add_product.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,Add_product.class);
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });
        hatss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,Add_product.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,Add_product.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });

        checkorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(Admin.this,AdminOrderNewActivity.class);
             startActivity(intent);
            }
        });


    }

    public void Logoutbutton(View view) {

        //sharedprefernececonfig.writeloginstatus(false);
        mAuth.signOut();
        Toast.makeText(Admin.this, "user logout....", Toast.LENGTH_LONG).show();
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        //  startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
