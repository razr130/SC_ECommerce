package com.example.sc_ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.sc_ecommerce.Model.Users;
import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadingbar = new ProgressDialog(this);
        Paper.init(this);
        final String phone = Paper.book().read(Prevalent.userphone);
        final String pass = Paper.book().read(Prevalent.userpass);
        if(phone != "" && pass != "")
        {
            if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass))
            {
                AllowAccess(phone, pass);
            }
            else
            {
                Thread mythread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(3000);
                            //Toast.makeText(SplashScreen.this, "splashhh", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mythread.start();
                Toast.makeText(this, "splasshhhh", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phone).exists()){

                    Users userdata = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            loadingbar.dismiss();
                            Intent intent  = new Intent(SplashScreen.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Prevalent.onlineuser = userdata;
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(SplashScreen.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else if(dataSnapshot.child("Admins").child(phone).exists()){

                    Users userdata = dataSnapshot.child("Admins").child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            loadingbar.dismiss();
                            Intent intent  = new Intent(SplashScreen.this, AdminCategoryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(SplashScreen.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(SplashScreen.this, "Account didn't exist...", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
