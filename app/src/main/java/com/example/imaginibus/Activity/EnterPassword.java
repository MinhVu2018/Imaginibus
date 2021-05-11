package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.imaginibus.R;

public class EnterPassword extends AppCompatActivity {
    ImageButton btn_back;
    Button btn_next;
    String email;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_enter_password);

        //find view
        pass = findViewById(R.id.editPassword);

        //get intent
        if (getIntent().getBooleanExtra("REGIS", false))
            email = getIntent().getStringExtra("EMAIL");
        else
            email = null;



        SetUpButton();
    }

    private void SetUpButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EnterPassword.this, SecureRegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email == null) {
                    SharedPreferences shp = getSharedPreferences(
                            "com.example.imaginibus.PREFERENCES", Context.MODE_PRIVATE);
                    String savedPass = shp.getString("PASS", null);
                    String curPass = String.valueOf(pass.getText());

                    if (!curPass.equals(savedPass)) {
                        Toast.makeText(EnterPassword.this, "Your password is incorrect!", Toast.LENGTH_SHORT);
                        return;
                    } else startSecureAlbum();
                } else if (email != null) {
                    String curPass = String.valueOf(pass.getText());
                    saveLocale(curPass, email);
                    startSecureAlbum();
                }
            }
        });
    }

    private void startSecureAlbum() {
        Intent intent = new Intent(EnterPassword.this, SecureAlbum.class);
        startActivity(intent);
        finish();
    }

    public void saveLocale(String password, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PASS", password);
        editor.putString("SECURE_EMAIL", email);
        editor.commit();
    }
}