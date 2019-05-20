package com.kalamcantre.cricket_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyDialog  extends DialogFragment {
    private EditText text1,text2;
    private EditText nameEditetxt, addressEdittxt, phoneEdittxt, CityEdittxt;
    private ExampleDialogListner listner;
    private   String name,address,phone,city,savecurrentdate,savecurrenttime;
    private   String productrandomkey,downloadimageurl,downloadimagerul2,downloadimageurl3;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private String productRandomKey,saveCurrentDate, saveCurrentTime;
    private Uri ImageUri;
    private FirebaseAuth mAuth;

    public List<String> fileNameList;
    public List<String> downloadurl;
    public List<String> fileDoneList;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        //ProductsRef = FirebaseDatabase.getInstance().getReference().child("Repair Products");

        // mAuth=FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.customdialoglayout, null);
        //   builder.setView(layoutInflater.inflate(R.layout.customdialoglayout,null));
        builder.setView(view)
                .setTitle("Details")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = nameEditetxt.getText().toString();
                        String address = addressEdittxt.getText().toString();
                        String phone = phoneEdittxt.getText().toString();
                        String  city = CityEdittxt.getText().toString();
                        if(name.isEmpty()){
                            Toast.makeText(getApplicationContext(), "please make sure you inserted all details", Toast.LENGTH_LONG).show();
                        }
                        if(address.isEmpty()){
                            Toast.makeText(getApplicationContext(), "please make sure you inserted all details", Toast.LENGTH_LONG).show();
                        }
                        if(phone.isEmpty()){
                            Toast.makeText(getApplicationContext(), "please make sure you inserted all details", Toast.LENGTH_LONG).show();
                        }
                        if(city.isEmpty()){
                            Toast.makeText(getApplicationContext(), "please make sure you inserted all details", Toast.LENGTH_LONG).show();
                        }else{
                            listner.applytext(name,address,phone,city);
                        }


                    }
                });
        nameEditetxt = view.findViewById(R.id.R_name);
        addressEdittxt = view.findViewById(R.id.R_address);
        phoneEdittxt = view.findViewById(R.id.R_number);
        CityEdittxt = view.findViewById(R.id.R_city);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            listner=(ExampleDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement the ExampleListner");
        }

    }

    public interface ExampleDialogListner {
        void applytext(String name, String address, String phone, String city);
    }

}
