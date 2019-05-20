package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kalamcantre.cricket_project.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth mAuth;
    private Sharedprefernececonfig sharedprefernececonfig;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference productref;
    private FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(productref, Product.class)
                        .build();

      /*  FirebaseRecyclerAdapter<Product,ProductViewHolder>*/
                adapter=
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + " ₹");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(HomeActivity.this,ProductDetailActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productref= FirebaseDatabase.getInstance().getReference().child("Products");

        mAuth = FirebaseAuth.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_menu);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        sharedprefernececonfig = new Sharedprefernececonfig(getApplicationContext());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    Toast.makeText(HomeActivity.this, "user logout....", Toast.LENGTH_LONG).show();
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(HomeActivity.this,CartActivity.class);
               startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);


      //  userNameTextView.setText(mAuth.getCurrentUser().getEmail());
     //   Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);

        userInfoDisplay(userNameTextView,profileImageView);

    }
    private void userInfoDisplay(final  TextView UserNameTextVieww,final CircleImageView profileImageView)
    {
        // DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getEmail());
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();


                        Picasso.get().load(image).into(profileImageView);
                        UserNameTextVieww.setText(name);
                    }
                    else {
                        UserNameTextVieww.setText(mAuth.getCurrentUser().getEmail());
                        Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
private  void firebasearch(String searchtext){

    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Products");
  //  String search1=searchtext.toLowerCase();

    FirebaseRecyclerOptions<Product> options=
            new FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(reference.orderByChild("pname").startAt(searchtext).endAt(searchtext+"\uf8ff"),Product.class)
                    .build();

//    Query firebasequery= reference.orderByChild("pname").startAt(searchtext).endAt(searchtext+"\uf8ff");



                FirebaseRecyclerAdapter<Product,ProductViewHolder>
                        adapter=new
                        FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
                                holder.txtProductName.setText(model.getPname());
                                holder.txtProductDescription.setText(model.getDescription());
                                holder.txtProductPrice.setText("Price = " + model.getPrice() + " ₹");
                                Picasso.get().load(model.getImage()).into(holder.imageView);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(HomeActivity.this,ProductDetailActivity.class);
                                        intent.putExtra("pid",model.getPid());
                                        startActivity(intent);

                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item_layout, viewGroup, false);
                                ProductViewHolder holder = new ProductViewHolder(view);
                                return holder;
                            }
                        };

    recyclerView.setAdapter(adapter);

    adapter.startListening();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);


        MenuItem search=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                onStop();
                firebasearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_cardd) {
            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.drawer_category) {
           // Toast.makeText(HomeActivity.this, "category pressed", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,RepairActivity2.class);
            startActivity(intent);
        } else if (id == R.id.drawer_logout) {
                     function();
        } else if (id == R.id.drawer_Order) {
            Intent intent=new Intent(HomeActivity.this,Requestcard.class);
            startActivity(intent);
        } else if (id == R.id.drawer_setting) {
                  Intent intent=new Intent(HomeActivity.this,SettingActivity.class);
                  startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean twice = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
         else if (twice == true) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }
            twice = true;
            Toast.makeText(HomeActivity.this, "please pree BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    twice = false;
                }
            }, 3000);

        }

        private void function () {
            sharedprefernececonfig.writeloginstatus(false);
            mAuth.signOut();
            Toast.makeText(HomeActivity.this, "user logout....", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            //  startActivity(new Intent(this,MainActivity.class));
            finish();
        }


}
