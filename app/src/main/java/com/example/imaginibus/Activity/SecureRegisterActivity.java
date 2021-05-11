package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.imaginibus.R;

public class SecureRegisterActivity extends AppCompatActivity {
    Button btn_regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_secure_register);

        btn_regis = (Button) findViewById(R.id.btn_next);
        btn_regis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SecureRegisterActivity.this, SecureCode.class);
                startActivity(intent);
            }
        });
    }
}