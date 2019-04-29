package com.example.sc_ecommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profpic;
    private EditText txtnewphone, txtnewname, txtnewaddress;
    private TextView btnclose, btnupdate;

    private Uri imageUri;
    private String myurl = "";
    private StorageTask uploadtask;
    private StorageReference storagepictureref;
    private String checker = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storagepictureref = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profpic = (CircleImageView) findViewById(R.id.profpic_image_setting);
        txtnewphone = (EditText) findViewById(R.id.TxtProfileNumber);
        txtnewname = (EditText) findViewById(R.id.TxtNameProfile);
        txtnewaddress = (EditText) findViewById(R.id.TxtAddressProfile);
        btnclose = (TextView) findViewById(R.id.TxtClose);
        btnupdate = (TextView) findViewById(R.id.TxtUpdate);

        userinfodisplay(profpic, txtnewphone, txtnewname, txtnewaddress);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checker.equals("clicked"))
                {
                    userinfosaved();
                    Toast.makeText(SettingsActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateuserinfo();
                    Toast.makeText(SettingsActivity.this, "just update", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);


            }
        });
    }

    private void updateuserinfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", txtnewname.getText().toString());
        userMap. put("address", txtnewaddress.getText().toString());
        userMap. put("phone", txtnewphone.getText().toString());


        ref.child(Prevalent.onlineuser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profpic.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userinfosaved()
    {
        if(TextUtils.isEmpty(txtnewname.getText().toString()))
        {
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(txtnewaddress.getText().toString()))
        {
            Toast.makeText(this, "address is empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(txtnewphone.getText().toString()))
        {
            Toast.makeText(this, "Phone is empty", Toast.LENGTH_SHORT).show();
        }
        else if (checker == "clicked")
        {
            uploadimage();
        }
    }

    private void uploadimage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, we're updating your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef = storagepictureref.child(Prevalent.onlineuser.getPhone() + ".jpg");
            uploadtask = fileRef.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(SettingsActivity.this, "bisa", Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Toast.makeText(SettingsActivity.this, "mau masuk proses", Toast.LENGTH_SHORT).show();
                    if(task.isSuccessful())
                    {
                        Uri downloadurl = task.getResult();
                        myurl = downloadurl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("name", txtnewname.getText().toString());
                        userMap. put("address", txtnewaddress.getText().toString());
                        userMap. put("phone", txtnewphone.getText().toString());
                        userMap. put("image", myurl);

                        Prevalent.onlineuser.setImage(myurl);
                        Prevalent.onlineuser.setName(txtnewname.getText().toString());
                        ref.child(Prevalent.onlineuser.getPhone()).updateChildren(userMap);

                        Toast.makeText(SettingsActivity.this, "after process", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Profile update error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userinfodisplay(final CircleImageView profpic, final EditText txtnewphone, final EditText txtnewname, final EditText txtnewaddress)
    {
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineuser.getPhone());

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profpic);
                        txtnewname.setText(name);
                        txtnewphone.setText(phone);
                        txtnewaddress.setText(address);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
