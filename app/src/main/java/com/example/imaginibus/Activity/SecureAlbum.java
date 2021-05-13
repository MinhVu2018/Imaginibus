package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.imaginibus.Adapter.ImageAdapter;
import com.example.imaginibus.Adapter.ImageLinearAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;
import com.example.imaginibus.Utils.MyApplication;

import java.util.List;

public class SecureAlbum extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    RecyclerView recyclerView;
    List<ImageModel> listImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_secure_album);

        //find view and setup adapter
        recyclerView = findViewById(R.id.list_image);
        listImage = ((MyApplication) this.getApplication()).getListSecure();

        if (((MyApplication) this.getApplication()).currentLayout == 0) {
            setupAdapterGridLayout();
        } else {
            setupAdapterLinearLayout();
        }
        SetUpButton();
    }

    private void SetUpButton(){
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SecureAlbum.this, MainActivity.class);
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


    private void setupAdapterGridLayout() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ImageAdapter imageAdapter = new ImageAdapter(this, "Secure", listImage);
        recyclerView.setAdapter(imageAdapter);
    }

    private void setupAdapterLinearLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageLinearAdapter imageLinearAdapter = new ImageLinearAdapter(this, "Secure", listImage);
        recyclerView.setAdapter(imageLinearAdapter);
    }
}