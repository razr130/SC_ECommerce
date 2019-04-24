package com.example.sc_ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sc_ecommerce.Model.Users;
import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button btn_regis, btn_login;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_regis = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        loadingbar = new ProgressDialog(this);

        Paper.init(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String phone = Paper.book().read(Prevalent.userphone);
        String pass = Paper.book().read(Prevalent.userpass);

        if(phone != "" && pass != "")
        {
            if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass))
            {
                AllowAccess(phone, pass);
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
                            Intent intent  = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
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
                            Intent intent  = new Intent(MainActivity.this, AdminCategoryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account didn't exist...", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
