package com.example.imaginibus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_search, btn_option, btn_gallery, btn_video, btn_location, btn_favorite, btn_secure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);

        SetUpButton();
    }

    private void SetUpButton(){
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PreviewImage dialog = new PreviewImage();
                dialog.show(getSupportFragmentManager(), "show preview image");
            }
        });

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PreviewImage dialog = new PreviewImage();
                dialog.show(getSupportFragmentManager(), "show preview image");
            }
        });

        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ByDate.class);
                startActivity(intent);
            }
        });

        btn_video = (ImageButton) findViewById(R.id.btn_video);
        btn_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ByDate.class);    // not yet
                startActivity(intent);
            }
        });

        btn_location = (ImageButton) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Location.class);
                startActivity(intent);
            }
        });

        btn_favorite = (ImageButton) findViewById(R.id.btn_favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Favorite.class);
                startActivity(intent);
            }
        });

        btn_secure = (ImageButton) findViewById(R.id.btn_secure);
        btn_secure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecureCode.class);
                startActivity(intent);
            }
        });

    }
}