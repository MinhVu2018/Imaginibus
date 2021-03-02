package com.example.imaginibus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ByDate extends AppCompatActivity {
    ImageButton btn_back, btn_option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_date);

        SetUpButton();
    }

    private void SetUpButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ByDate.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ByDate.this, MainActivity.class);    // not yet
                startActivity(intent);
            }
        });
    }
}