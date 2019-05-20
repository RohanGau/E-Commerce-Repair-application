package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Confirm_final_Order extends AppCompatActivity {

   public EditText nameEditetxt,addressEdittxt,phoneEdittxt,CityEdittxt;
  public   Button ConfirmBTN;
  private FirebaseAuth mAuth;
  private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final__order);

        mAuth=FirebaseAuth.getInstance();

          totalAmount=getIntent().getStringExtra("Total Price");
          Toast.makeText(Confirm_final_Order.this,"Total Price= ₹ "+totalAmount,Toast.LENGTH_SHORT).show();


          ConfirmBTN=findViewById(R.id.Confirm_button);
         nameEditetxt=findViewById(R.id.shipment_name);
         addressEdittxt=findViewById(R.id.shipment_address);
         phoneEdittxt=findViewById(R.id.shipment_phone_number);
         CityEdittxt=findViewById(R.id.shipment_city);

         ConfirmBTN.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Check();
             }
         });

    }

    private void Check() {
    if(TextUtils.isEmpty(nameEditetxt.getText().toString())){
        Toast.makeText(Confirm_final_Order.this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(addressEdittxt.getText().toString())){
        Toast.makeText(Confirm_final_Order.this,"Please Enter Your Address",Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(CityEdittxt.getText().toString())){
        Toast.makeText(Confirm_final_Order.this,"Please Enter Your City",Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(phoneEdittxt.getText().toString())){
        Toast.makeText(Confirm_final_Order.this,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show();
    }
    else {
        ConfirmOrder();

    }
    }

    private void ConfirmOrder() {
      final   String savecurrentdate,savecurrenttime;
        Calendar calendardate=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate= dateFormat.format(calendardate.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime= currenttime.format(calendardate.getTime());
        final DatabaseReference Order= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(mAuth.getCurrentUser().getUid());
       HashMap <String,Object> Ordermaps=new HashMap<>();
        Ordermaps.put("totalAmount",totalAmount);
        Ordermaps.put("name",nameEditetxt.getText().toString());
        Ordermaps.put("phone",phoneEdittxt.getText().toString());
        Ordermaps.put("address",addressEdittxt.getText().toString());
        Ordermaps.put("city",CityEdittxt.getText().toString());

        Ordermaps.put("date",savecurrentdate);
        Ordermaps.put("time",savecurrenttime);
        Ordermaps.put("state","not shipped");

        Order.updateChildren(Ordermaps).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Card List")
                            .child("User View")
                            .child(mAuth.getCurrentUser().getUid())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(Confirm_final_Order.this,"Your Final Order Has Been Success",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Confirm_final_Order.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}
