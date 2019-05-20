package com.kalamcantre.cricket_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kalamcantre.cricket_project.ViewHolder.UploadlistAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RepairActivity2 extends AppCompatActivity implements MyDialog.ExampleDialogListner{

    private static final int RESULT_LOAD_IMAGE =1 ;
    private ImageButton mSelectBtn;
    private RecyclerView mUploadList;

    //private List<String> fileNameList;
   // private List<String> downloadurl;
   // private List<String> fileDoneList;

    public List<String> fileNameList;
    public List<String> downloadurl;
    public List<String> fileDoneList;

    private Uri fileUri;
    private String myUrl = "";

    public EditText text1,text2;
    private StorageReference mStorage;
    private DatabaseReference ProductsRef;
    private UploadlistAdapter uploadListAdapter;
    FirebaseAuth mAuth;

    private String productRandomKey,saveCurrentDate, saveCurrentTime;
    public ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair2);

        text1=findViewById(R.id.user_product_name);
        text2=findViewById(R.id.user_product_description);
        loadingBar=new ProgressDialog(this);
        mSelectBtn=(ImageButton)findViewById(R.id.select_btn);
        mUploadList=(RecyclerView) findViewById(R.id.upload_list);


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Repair Products");
        mAuth= FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();



        fileNameList = new ArrayList<>();
        downloadurl=new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadListAdapter = new UploadlistAdapter(fileNameList, fileDoneList);

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);



        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData() != null){
                int totalItemsSelected = data.getClipData().getItemCount();

                if(totalItemsSelected<=2){

                    Toast.makeText(RepairActivity2.this, "please take more images", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    for(int i = 0; i < totalItemsSelected; i++) {

                        Uri fileUri = data.getClipData().getItemAt(i).getUri();

                        String fileName = getFileName(fileUri);

                        fileNameList.add(fileName);
                        fileDoneList.add("uploading");
                        uploadListAdapter.notifyDataSetChanged();

                        loadingBar.setTitle("Add New Repair Request");
                        loadingBar.setMessage("Dear User, please wait while we are adding the new Product Image");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();



                        StorageReference fileToUpload = mStorage.child("Repair Product Images").child(fileName);
                        final int finalI = i;
                        downloadurl.add(fileToUpload.getDownloadUrl().toString());
                        fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                fileDoneList.remove(finalI);
                                fileDoneList.add(finalI, "done");
                                loadingBar.dismiss();
                                uploadListAdapter.notifyDataSetChanged();

                            }
                        });

                    }



                }

               // Toast.makeText(RepairActivity2.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData() != null){

                Toast.makeText(RepairActivity2.this, "please take more images", Toast.LENGTH_SHORT).show();

            }


        }
        }





    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public void requestbutton(View view) {
        String  productname=text1.getText().toString();
        String  productdiscription=text2.getText().toString();
        //userInfosaved();
        if(fileNameList.isEmpty()){
            Toast.makeText(this,"please insert images ",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(productdiscription)){
            Toast.makeText(this,"please enter product description ",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(productname)){
            Toast.makeText(this,"please enter product name",Toast.LENGTH_LONG).show();
        }
        else {
            MyDialog myDialog = new MyDialog();
            myDialog.show(getSupportFragmentManager(), "user detail");

        }
    }

  /*  private void userInfosaved() {
     String  productname=text1.getText().toString();
     String  productdiscription=text2.getText().toString();
     String image1=downloadurl.get(0);
     String image2=downloadurl.get(1);
     String image3=downloadurl.get(2);


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

     if(fileNameList.isEmpty()){
         Toast.makeText(this,"please insert images ",Toast.LENGTH_LONG).show();
     }
      if(TextUtils.isEmpty(productdiscription)){
            Toast.makeText(this,"please enter product description ",Toast.LENGTH_LONG).show();
      }
      if(TextUtils.isEmpty(productname)){
            Toast.makeText(this,"please enter product name",Toast.LENGTH_LONG).show();
        }
        else{
          Toast.makeText(this,"success...",Toast.LENGTH_LONG).show();
          loadingBar.setTitle("Add New Repair Request");
          loadingBar.setMessage("Dear User, please wait while we are adding the new Repair request");
          loadingBar.setCanceledOnTouchOutside(false);
          loadingBar.show();

          HashMap<String, Object> productMap = new HashMap<>();
          productMap.put("pid", productRandomKey);
          productMap.put("date", saveCurrentDate);
          productMap.put("time", saveCurrentTime);
          productMap.put("image1", image1);
          productMap.put("image2",image2);
          productMap.put("image3",image3);
         // productMap.put("image2", image2);
          productMap.put("description", productdiscription);
          productMap.put("pname", productname);
          ProductsRef.child(mAuth.getCurrentUser().getUid()).child(productRandomKey).updateChildren(productMap)
                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task)
                      {
                          if (task.isSuccessful())
                          {
                              Intent intent = new Intent(RepairActivity2.this, HomeActivity.class);
                              startActivity(intent);
                              loadingBar.dismiss();
                              Toast.makeText(RepairActivity2.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                          }
                          else
                          {

                              String message = task.getException().toString();
                              Toast.makeText(RepairActivity2.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                          }
                      }
                  });

      }
    }*/

    @Override
    public void applytext(String name, String address, String phone, String city) {
        String  productname=text1.getText().toString();
        String  productdiscription=text2.getText().toString();
        String image1=downloadurl.get(0);
        String image2=downloadurl.get(1);
        String image3=downloadurl.get(2);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        Toast.makeText(this,"success...",Toast.LENGTH_LONG).show();
        loadingBar.setTitle("Add New Repair Request");
        loadingBar.setMessage("Dear User, please wait while we are adding the new Repair request");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image1", image1);
        productMap.put("image2",image2);
        productMap.put("image3",image3);
        productMap.put("username",name);
        productMap.put("address",address);
        productMap.put("city",city);
        productMap.put("phone",phone);
        productMap.put("description", productdiscription);
        productMap.put("pname", productname);
        ProductsRef.child(mAuth.getCurrentUser().getUid()).child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(RepairActivity2.this, HomeActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(RepairActivity2.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            String message = task.getException().toString();
                            Toast.makeText(RepairActivity2.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
