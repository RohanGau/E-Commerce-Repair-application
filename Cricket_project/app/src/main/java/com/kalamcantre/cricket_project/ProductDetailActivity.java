package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
private Button addtocardbutton;
    private FloatingActionButton addTocard;
    private ImageView productimagedetail;
    private ElegantNumberButton numberButtonl;
    private TextView productprice,productdescription,productnamel;
private  String productID="", state="Normal";

FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        Checkorderstate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productID=getIntent().getStringExtra("pid");
addtocardbutton=(Button)findViewById(R.id.pd_add_to_card_button);

mAuth=FirebaseAuth.getInstance();
     //   addTocard=(FloatingActionButton)findViewById(R.id.card_button);
        numberButtonl=(ElegantNumberButton)findViewById(R.id.number_btn);
        productimagedetail=(ImageView)findViewById(R.id.product_image_detail);
        productprice=(TextView)findViewById(R.id.product_price_detail);
        productdescription=(TextView)findViewById(R.id.product_description_detail);
        productnamel=(TextView)findViewById(R.id.product_name_detail);


        getProductDetail(productID);

        addtocardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(state.equals("Order Placed") || state.equals("Order Shipped") ){
                             Toast.makeText(ProductDetailActivity.this,"You Can Add Purchase More Product, Once Your Order Shipped Or Confirmed",Toast.LENGTH_LONG).show();
                    }
                    else{
                        addingToCardList();
                    }
            }
        });
    }

    private void addingToCardList() {
        String savecurrentdate,savecurrenttime;
        Calendar calendardate=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate= dateFormat.format(calendardate.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime= currenttime.format(calendardate.getTime());

        final  DatabaseReference cardlistref=FirebaseDatabase.getInstance().getReference().child("Card List");

          final HashMap<String,Object> cardmap=new HashMap<>();
          cardmap.put("pid",productID);
        cardmap.put("pname",productnamel.getText().toString());
        cardmap.put("price",productprice.getText().toString());
        cardmap.put("date",savecurrentdate);
        cardmap.put("time",savecurrenttime);
        cardmap.put("quantity",numberButtonl.getNumber());
        cardmap.put("discount","");
        cardlistref.child("User View").child(mAuth.getCurrentUser().getUid()).child("Products")
                .child(productID).updateChildren(cardmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            cardlistref.child("Admin View").child(mAuth.getCurrentUser().getUid()).child("Products")
                                    .child(productID).updateChildren(cardmap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailActivity.this,"Added to a Card",Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ProductDetailActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });





    }

    private void getProductDetail(String productID) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");
        reference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Product products=dataSnapshot.getValue(Product.class);
                    productnamel.setText(products.getPname());
                    productprice.setText(products.getPrice());
                    productdescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productimagedetail);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private  void Checkorderstate(){
        DatabaseReference orderReff=FirebaseDatabase.getInstance().getReference().child("Orders").child(mAuth.getCurrentUser().getUid());
        orderReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingstate=dataSnapshot.child("state").getValue().toString();

                    if(shippingstate.equals("shipped")){
                       state="Order Shipped";
                    }
                    else if(shippingstate.equals("not shipped")){
                        state="Order Placed";

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
