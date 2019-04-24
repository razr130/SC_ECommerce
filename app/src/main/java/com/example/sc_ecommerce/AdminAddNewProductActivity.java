package com.example.sc_ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private Button btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        btn_logout = (Button) findViewById(R.id.btn_logouthome);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().destroy();

                Intent intent  = new Intent(AdminAddNewProductActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Toast.makeText(this, "hello bitch", Toast.LENGTH_SHORT).show();
    }
}
