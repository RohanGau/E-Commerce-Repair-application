package com.kalamcantre.cricket_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kalamcantre.cricket_project.ViewHolder.Admin_orders;

public class AdminOrderNewActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private DatabaseReference reference;
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_new);
         reference= FirebaseDatabase.getInstance().getReference().child("Orders");

         mAuth=FirebaseAuth.getInstance();

         recyclerView=findViewById(R.id.order_card_list);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Admin_orders> options=
                new FirebaseRecyclerOptions.Builder<Admin_orders>()
                .setQuery(reference,Admin_orders.class)
                .build();
        FirebaseRecyclerAdapter<Admin_orders,AdminOrderViewHolder> adapter=
                new FirebaseRecyclerAdapter<Admin_orders, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull Admin_orders model) {
                        holder.username.setText("Name: "+ model.getName());
                        holder.userPhonenumber.setText("Phone number: "+ model.getPhone());
                        holder.userDateTime.setText("Order at: "+ model.getDate()+" "+model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: "+ model.getAddress()+","+ model.getCity());
                        holder.userTotalprice.setText("Total Price: "+ model.getTotalAmount());

                        holder.Showorderbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String UID=getRef(position).getKey();
                               Intent intent=new Intent(AdminOrderNewActivity.this,AdminUserProductActivity.class);
                               intent.putExtra("uid",mAuth.getCurrentUser().getUid());
                               intent.putExtra("uid",UID);

                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence Options[]=new CharSequence[]{
                                  "Yes",
                                  "No"
                                };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminOrderNewActivity.this);
                                builder.setTitle("Have you Shipped This Order ?");
                              builder.setItems(Options, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {

                                      if(which==0){

                                          String UID=getRef(position).getKey();
                                          removeorder(UID);
                                      }
                                      else
                                      {

                                      }
                                  }
                              });
                              builder.show();

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout,viewGroup,false);
                        return new AdminOrderViewHolder(view);

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    private static  class  AdminOrderViewHolder extends RecyclerView.ViewHolder{

        public TextView username,userPhonenumber,userTotalprice,userDateTime,userShippingAddress;
        public Button Showorderbtn;


        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.order_username);
            userPhonenumber=itemView.findViewById(R.id.order_phone_number);
            userTotalprice=itemView.findViewById(R.id.order_totalprice);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);
            Showorderbtn=itemView.findViewById(R.id.show_all_product);


        }



    }


    private void removeorder(String uid) {

           reference.child(uid).removeValue();
    }
}
