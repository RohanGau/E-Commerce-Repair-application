package com.kalamcantre.cricket_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalamcantre.cricket_project.ViewHolder.Cart;
import com.kalamcantre.cricket_project.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button  nextprocess;
    private TextView textView,txtmesage;
    private FirebaseAuth mAuth;

private int overalprice=0;
private  String Totalamount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

       // Totalamount=getIntent().getStringExtra("Total Price=");
       // Toast.makeText(CartActivity.this,"Total Price = ₹"+Totalamount,Toast.LENGTH_SHORT).show();
        mAuth=FirebaseAuth.getInstance();


txtmesage=findViewById(R.id.txtmsg);
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextprocess=(Button)findViewById(R.id.next_process_btn);
        textView=(TextView)findViewById(R.id.totalpice);

        nextprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  textView.setText("Total Price = ₹"+String.valueOf(overalprice));
                Intent intent=new Intent(CartActivity.this,Confirm_final_Order.class);
               intent.putExtra("Total Price",String.valueOf(overalprice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
Checkorderstate();
        DatabaseReference cardref= FirebaseDatabase.getInstance().getReference().child("Card List");
        FirebaseRecyclerOptions<Cart>  Option=new
                FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cardref.child("User View")
                        .child(mAuth.getCurrentUser().getUid())
                        .child("Products"),Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(Option) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                        holder.txtproductname.setText(model.getPname());
                        holder.txtproductquantity.setText("Quantity  ="+model.getQuantity());
                        holder.txtproductprice.setText("Price  ="+model.getPrice()+ "₹");
                        int onetypeproductprice=((Integer.valueOf(model.getPrice())))*(Integer.valueOf(model.getQuantity()));
                        overalprice=overalprice+onetypeproductprice;
                        textView.setText("Total Price = ₹"+String.valueOf(overalprice));

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence option[]=new CharSequence[]{
                                    "Edit",
                                        "Remove"
                                };
                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("card options");

                                builder.setItems(option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which==0){
                                            Intent intent=new Intent(CartActivity.this,ProductDetailActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }
                                        if(which==1){
                                            cardref.child("User View")
                                                    .child(mAuth.getCurrentUser().getUid())
                                                    .child("Products")
                                                    .child(model.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                Snackbar.make(findViewById(R.id.rootview),"Remove Item Sucessful",Snackbar.LENGTH_LONG).show();
                                                              //  Intent intent=new Intent(CartActivity.this,HomeActivity.class);
                                                               // startActivity(intent);
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                });
                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                        CartViewHolder holder=new CartViewHolder(view);
                        return  holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private  void Checkorderstate(){
        DatabaseReference orderReff=FirebaseDatabase.getInstance().getReference().child("Orders").child(mAuth.getCurrentUser().getUid());
        orderReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingstate=dataSnapshot.child("state").getValue().toString();
                    String ussername=dataSnapshot.child("name").getValue().toString();
                    if(shippingstate.equals("shipped")){
                        textView.setText("Dear "+ussername+"\n Order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);

                        txtmesage.setVisibility(View.VISIBLE);
                        txtmesage.setText("Congratulation,Your Final Order Has Been shipped Successfully.Soon You Will Receive Your Order At Your Door step.");
                        nextprocess.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"you can purchase more product, once you receive your first final order ",Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingstate.equals("not shipped")){
                        textView.setText("shipping state = not shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtmesage.setVisibility(View.VISIBLE);
                        nextprocess.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"you can purchase more product, once you receive your first final order ",Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
