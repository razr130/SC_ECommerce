package com.example.sc_ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sc_ecommerce.Model.Users;
import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_regis;
    private EditText txt_name, txt_phone, txt_password;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_regis = (Button) findViewById(R.id.btn_regis);
        txt_name = (EditText) findViewById(R.id.TxtNamaRegis);
        txt_phone = (EditText) findViewById(R.id.TxtPhoneRegis);
        txt_password = (EditText) findViewById(R.id.TxtPasswordRegis);
        loadingbar = new ProgressDialog(this);

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();

            }
        });

    }

    private void CreateAccount() {

        String name = txt_name.getText().toString();
        String phone = txt_phone.getText().toString();
        String password = txt_password.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Creat Account");
            loadingbar.setMessage("Please Wait...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            validatephonenumber(name, phone, password);
        }
    }

    private void validatephonenumber(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Your account has been created...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                AllowAccessToAccount(phone, password);
//                                Intent intent  = new Intent(RegisterActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                            else
                            {
                                loadingbar.dismiss();
                                Toast.makeText(RegisterActivity.this, "There's some problem, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This phone number is already exist...", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    txt_phone.getText().clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AllowAccessToAccount(final String phone, final String password) {


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
                                Intent intent  = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Prevalent.onlineuser = userdata;
                                startActivity(intent);
                                finish();


                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(RegisterActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Account didn't exist...", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
