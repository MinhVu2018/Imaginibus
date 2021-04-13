package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.imaginibus.Adapter.ImageAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import java.util.List;

public class Favorite extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    TextView title, num_img;
    RecyclerView listImageView;
    List<ImageModel> listImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_favorite);

        //find view
        title = findViewById(R.id.title);
        listImageView = findViewById(R.id.list_image);
        num_img = findViewById(R.id.num_images);

        //setup the title if it is an album
        if (getIntent().hasExtra("Title")) {
            title.setText(getIntent().getStringExtra("Title"));

            listImage = (List<ImageModel>) getIntent().getSerializableExtra("List Image");
            listImageView.setLayoutManager(new GridLayoutManager(this, 3));
            ImageAdapter imageAdapter = new ImageAdapter(this, R.id.list_image, listImage);
            listImageView.setAdapter(imageAdapter);
            num_img.setText(String.valueOf(listImage.size()) + " ");
        }


        SetUpButton();
    }

    private void SetUpButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Favorite.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.layout_option_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

}