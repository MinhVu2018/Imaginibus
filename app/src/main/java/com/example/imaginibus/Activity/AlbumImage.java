package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.imaginibus.Adapter.ImageAdapter;
import com.example.imaginibus.Adapter.ImageLinearAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.R;

import java.io.Serializable;
import java.util.List;

public class AlbumImage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    TextView title, num_img;
    RecyclerView listImageView;
    List<ImageModel> listImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_album_image);

        //find view
        title = findViewById(R.id.title);
        listImageView = findViewById(R.id.list_image);
        num_img = findViewById(R.id.num_images);

        //setup the title for an album
        title.setText(getIntent().getStringExtra("Title"));
        listImage = (List<ImageModel>) getIntent().getSerializableExtra("List Image");
        if (((MyApplication) this.getApplication()).currentLayout == 0) {
            setupAdapterGridLayout();
        } else {
            setupAdapterLinearLayout();
        }
        num_img.setText(String.valueOf(listImage.size()) + " ");

        //set up the buttons
        SetUpButton();
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

    private void setupAdapterGridLayout() {
        listImageView.setLayoutManager(new GridLayoutManager(this, 3));
        ImageAdapter imageAdapter = new ImageAdapter(this, getIntent().getStringExtra("Title"), listImage);
        listImageView.setAdapter(imageAdapter);
    }

    private void setupAdapterLinearLayout() {
        listImageView.setLayoutManager(new LinearLayoutManager(this));
        ImageLinearAdapter imageLinearAdapter = new ImageLinearAdapter(this, getIntent().getStringExtra("Title"), listImage);
        listImageView.setAdapter(imageLinearAdapter);
    }

    private void SetUpButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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
        switch (item.getItemId()) {
            case R.id.btn_layout:
                ((MyApplication) this.getApplication()).currentLayout = Math.abs(((MyApplication) this.getApplication()).currentLayout - 1);
                Intent intent = new Intent(AlbumImage.this, AlbumImage.class);
                if (getIntent().hasExtra("Title")) {
                    intent.putExtra("Title", getIntent().getStringExtra("Title"));
                    intent.putExtra("List Image", (Serializable) listImage);
                }
                saveLayout();
                startActivity(intent);
                finish();
                break;
        }

        return true;
    }

    public void saveLayout() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LAYOUT", ((MyApplication) this.getApplication()).currentLayout);
        editor.commit();
    }
}