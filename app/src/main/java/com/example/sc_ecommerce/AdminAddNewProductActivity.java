package com.example.sc_ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private Button btnaddproduct;
    private String CategoryName, productname, productdesc, productprice, savecurrentdate, savecurrenttime, productid, downloadimageurl;
    private EditText txtproductname, txtproductdesc, txtproductprice;
    private ImageView btnaddphoto;
    private static final int GalleryPick = 1;
    private Uri imageuri;
    private StorageReference productimagesref;
    private DatabaseReference productref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        loadingbar = new ProgressDialog(this);
        productimagesref = FirebaseStorage.getInstance().getReference().child("Product Images");
        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        btnaddproduct = (Button) findViewById(R.id.BtnAddProduct);
        txtproductname = (EditText) findViewById(R.id.TxtProductName);
        txtproductdesc = (EditText) findViewById(R.id.TxtProductDescripton);
        txtproductprice = (EditText) findViewById(R.id.TxtProductPrice);
        btnaddphoto = (ImageView) findViewById(R.id.BtnAddPhoto);

        btnaddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OpenGallery();
            }
        });

        btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData()
    {
        productname = txtproductname.getText().toString();
        productdesc = txtproductdesc.getText().toString();
        productprice = txtproductprice.getText().toString();

        if(imageuri == null)
        {
            Toast.makeText(this, "Image is empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productdesc))
        {
            Toast.makeText(this, "dec is empty...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productname))
        {
            Toast.makeText(this, "name is empty...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productprice))
        {
            Toast.makeText(this, "price is empty...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loadingbar.setTitle("Adding Product");
        loadingbar.setMessage("Please Wait...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentdata = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentdata.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());

        productid = savecurrentdate + savecurrenttime;

        final StorageReference filepath = productimagesref.child(imageuri.getPathSegments() + productid + ".jpg");
        final UploadTask uploadTask = filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Upload Success!", Toast.LENGTH_SHORT).show();
                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        downloadimageurl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {

                            downloadimageurl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "getting image url success!", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put("product_id", productid);
        productmap.put("product_date", savecurrentdate);
        productmap.put("product_time", savecurrenttime);
        productmap.put("product_name", productname);
        productmap.put("product_desc", productdesc);
        productmap.put("product_price", productprice);
        productmap.put("product_image", downloadimageurl);
        productmap.put("product_category", CategoryName);

        productref.child(productid).updateChildren(productmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful())
                     {
                         Intent intent  = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         finish();
                         loadingbar.dismiss();
                         Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully!", Toast.LENGTH_SHORT).show();
                     }
                     else
                     {
                         loadingbar.dismiss();
                         String message = task.getException().toString();
                         Toast.makeText(AdminAddNewProductActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                     }
                    }
                });

    }

    private void OpenGallery()
    {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {

            imageuri = data.getData();
            btnaddphoto.setImageURI(imageuri);


        }
    }
}
