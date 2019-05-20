package com.kalamcantre.cricket_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kalamcantre.cricket_project.ViewHolder.Cart;
import com.kalamcantre.cricket_project.ViewHolder.CartViewHolder;

public class AdminUserProductActivity extends AppCompatActivity {
    private RecyclerView productlist;
    private DatabaseReference productref;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private  String UID= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth=FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);

        UID=getIntent().getStringExtra("uid");
        productlist=findViewById(R.id.Products_list);
        productlist.setHasFixedSize(true);
       layoutManager=new LinearLayoutManager(this);
       productlist.setLayoutManager(layoutManager);


        productref= FirebaseDatabase.getInstance().getReference()
                .child("Card List").child("Admin View")
                .child(UID).child("Products");
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(productref,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtproductname.setText(model.getPname());
                holder.txtproductquantity.setText("Quantity  ="+model.getQuantity());
                holder.txtproductprice.setText("Price  ="+model.getPrice()+ "₹");
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                CartViewHolder holder=new CartViewHolder(view);
                return  holder;
            }
        };
        productlist.setAdapter(adapter);
        adapter.startListening();
    }
}
