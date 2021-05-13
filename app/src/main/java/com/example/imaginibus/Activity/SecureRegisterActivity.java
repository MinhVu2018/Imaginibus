package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imaginibus.R;
import com.example.imaginibus.Utils.MyApplication;

public class SecureRegisterActivity extends AppCompatActivity {
    Button btn_regis;
    ImageButton btn_back;
    EditText emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_secure_register);

        //get the view
        emailView = findViewById(R.id.email);
        btn_back = findViewById(R.id.btn_back2);

        btn_regis = (Button) findViewById(R.id.btn_next);
        btn_regis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userEmail = String.valueOf(emailView.getText());

                if (!userEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Intent intent = new Intent(SecureRegisterActivity.this, EnterPassword.class);
                    intent.putExtra("REGIS", true);
                    intent.putExtra("EMAIL", userEmail);
                    startActivity(intent);
                } else {
                    Toast.makeText(SecureRegisterActivity.this, "Your email is not valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.FullScreenCall(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApplication.FullScreenCall(this);
    }
}