package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class User_signin extends AppCompatActivity {
    private Sharedprefernececonfig sharedprefernececonfig;
    private android.support.v7.widget.Toolbar toolbar;
     private Button button;
     private FirebaseAuth mAuth;
     private FirebaseAuth.AuthStateListener mAuthListener;
     MainActivity variable=new MainActivity();
     private DrawerLayout drawerLayout;
     private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        toolbar=findViewById(R.id.Toolbar_layout);
        setSupportActionBar(toolbar);
        sharedprefernececonfig=new Sharedprefernececonfig(getApplicationContext());
     //   button=findViewById(R.id.logout_button);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);



        mAuth=FirebaseAuth.getInstance();


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(User_signin.this, MainActivity.class));
                        Toast.makeText(User_signin.this, "user logout....", Toast.LENGTH_LONG).show();
                    }
                }
            };


            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){

                        case R.id.drawer_logout:
                            menuItem.setChecked(true);
                            function();
                            drawerLayout.closeDrawers();
                            return true;

                        case R.id.drawer_setting:
                            menuItem.setChecked(true);
                            Toast.makeText(User_signin.this,"this item is selected",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            return true;


                    }

                    return false;
                }
            });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  MenuInflater menuInflater=getMenuInflater();
       // menuInflater.inflate(R.menu.menu_item,menu);
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    boolean twice=false;
    @Override
    public void onBackPressed() {
        if(twice==true){
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        twice=true;
        Toast.makeText(User_signin.this,"please pree BACK again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice=false;
            }
        },3000);

    }

    public void logout(View view) {

        sharedprefernececonfig.writeloginstatus(false);
        mAuth.signOut();
        Toast.makeText(User_signin.this, "user logout....", Toast.LENGTH_LONG).show();
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
      //  startActivity(new Intent(this,MainActivity.class));
        finish();
        }
        private void function(){
            sharedprefernececonfig.writeloginstatus(false);
            mAuth.signOut();
            Toast.makeText(User_signin.this, "user logout....", Toast.LENGTH_LONG).show();
            Intent i=new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            //  startActivity(new Intent(this,MainActivity.class));
            finish();
        }
}
