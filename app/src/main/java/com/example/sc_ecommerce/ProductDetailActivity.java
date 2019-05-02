package com.example.sc_ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sc_ecommerce.Model.Products;
import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productimage;
    private TextView TxtProductTitle, TxtProductDescription, TxtProductPrice;
    private Button btnaddtocart;
    private ElegantNumberButton BtnCounter;
    private String product_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product_id = getIntent().getStringExtra("product_id");

        productimage = (ImageView) findViewById(R.id.Product_detail_pic);
        TxtProductTitle = (TextView) findViewById(R.id.TxtProductNameDetail);
        TxtProductDescription = (TextView) findViewById(R.id.TxtProductDescriptionDetail);
        TxtProductPrice = (TextView) findViewById(R.id.TxtProductPriceDetail);
      btnaddtocart = (Button) findViewById(R.id.BtnAddToCart);
        BtnCounter = (ElegantNumberButton) findViewById(R.id.counter);

        getProductDetails(product_id);

        btnaddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingtocartlist();
            }
        });

    }

    private void addingtocartlist()
    {

        String savecurrenttime, savecurentdate;

        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MM dd, yyyy");
        savecurentdate = currentdate.format(calfordate.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calfordate.getTime());

        final DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartmap = new HashMap<>();
        cartmap.put("product_id", product_id);
        cartmap.put("product_name", TxtProductTitle.getText().toString());
        cartmap.put("product_price", TxtProductPrice.getText().toString());
        cartmap.put("date", savecurentdate);
        cartmap.put("time", savecurrenttime);
        cartmap.put("quantity", BtnCounter.getNumber());
        cartmap.put("discount", "");

        cartlist.child("User View").child(Prevalent.onlineuser.getPhone()).child("Products")
                .child(product_id).updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    cartlist.child("Admin View").child(Prevalent.onlineuser.getPhone()).child("Products")
                            .child(product_id).updateChildren(cartmap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ProductDetailActivity.this, "added to cart list", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        
                                    }
                                }
                            });
                }
            }
        });


    }

    private void getProductDetails(String product_id)
    {
        DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Products");
        productref.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
             if(dataSnapshot.exists())
             {
                 Products products = dataSnapshot.getValue(Products.class);
                 TxtProductTitle.setText(products.getProduct_name());
                 TxtProductDescription.setText(products.getProduct_desc());
                 TxtProductPrice.setText(products.getProduct_price());
                 Picasso.get().load(products.getProduct_image()).into(productimage);


             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
