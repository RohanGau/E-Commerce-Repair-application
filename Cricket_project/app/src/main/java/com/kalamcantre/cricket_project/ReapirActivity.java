package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ReapirActivity extends AppCompatActivity implements MyDialog.ExampleDialogListner {

    public ImageView imageView1,imageView2,imageView3,imageview4;
    public   String productname,productdiscription;
    public EditText text1,text2;
    private Button finalbutton;
    private static final int REQUEST_IMAGE_CAPTURE=101;
    private boolean zoomOut =  false;
    String Currentimagepath=null;


    public Uri ImageUri,Imageuri2,Imageuri3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reapir);
        imageView1= findViewById(R.id.select_user_product_image);
        imageView2=findViewById(R.id.image1);
        imageView3=findViewById(R.id.image2);
        imageview4=findViewById(R.id.image3);

          text1=findViewById(R.id.user_product_name);
          text2=findViewById(R.id.user_product_description);
          finalbutton=findViewById(R.id.add_request_product);


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){

                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK && data!=null)
        {
            Bundle extra=data.getExtras();
            Bitmap imagebitmap= (Bitmap) extra.get("data");

            if(imageView2.getDrawable()==null) {

                imageView2.setImageBitmap(imagebitmap);


            }
            else if(imageView3.getDrawable()==null){

                imageView3.setImageBitmap(imagebitmap);

            }
            else if(imageview4.getDrawable()==null){

                imageview4.setImageBitmap(imagebitmap);


            }else {
                ImageUri = data.getData();
                imageView2.setImageURI(ImageUri);
                Imageuri2=data.getData();
                imageView3.setImageURI(Imageuri2);
                Imageuri3=data.getData();
                imageview4.setImageURI(Imageuri3);
                Toast.makeText(this,"All images is added..",Toast.LENGTH_SHORT).show();
            }

            ImageUri = data.getData();
            imageView2.setImageURI(ImageUri);
            Imageuri2=data.getData();
            imageView3.setImageURI(Imageuri2);
            Imageuri3=data.getData();
            imageview4.setImageURI(Imageuri3);
        }


    }




   void display(String messgae){
        Toast.makeText(this,messgae,Toast.LENGTH_LONG).show();
      // Snackbar.make(findViewById(R.id.rootview),messgae,Snackbar.LENGTH_SHORT).show();
   }

    public void requestbutton(View view) {
        Intent data=new Intent();
        productname=text1.getText().toString();
        productdiscription=text2.getText().toString();

        if(imageView2.getDrawable()==null&&imageView3.getDrawable()==null&&imageview4.getDrawable()==null){
            Toast.makeText(this,"please insert atleat one image",Toast.LENGTH_LONG).show();
        }
           else if(TextUtils.isEmpty(productdiscription)){
            Toast.makeText(this,"please enter product description ",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productname)){
            Toast.makeText(this,"please enter product name",Toast.LENGTH_LONG).show();
        }
        else {

            opendialog();
    }

    }

    @Override
    public void applytext(String name, String address, String phone, String city) {

    }

    private void opendialog() {
       MyDialog myDialog=new MyDialog();
       myDialog.show(getSupportFragmentManager(),"user detail");

    }
}
