package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.OnSwipeTouchListener;
import com.example.imaginibus.R;

import java.util.List;

public class ViewImage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    List<ImageModel> listImage;
    ImageView img;
    LinearLayout header, footer;
    int cur_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_view_image);

        //set the current image
        img = (ImageView) findViewById(R.id.viewed_image);
        String img_path = getIntent().getStringExtra("img_path");

        //find the current image position
        listImage = (List<ImageModel>) getIntent().getSerializableExtra("list_img");
        for (cur_img = 0; cur_img<listImage.size(); cur_img++) {
            if (("file://" + listImage.get(cur_img).getImageUrl()).equals(img_path))
                //return; // sao return o dayyyyyyyyyyyyyyyyyyyyyyyyyyyy
                break;
        }
        showImage();

        //setup buttons
        SetUp();
    }

    private void SetUp(){
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });

        header = (LinearLayout) findViewById(R.id.image_header);
        footer = (LinearLayout) findViewById(R.id.image_footer);

        img.setOnTouchListener(new OnSwipeTouchListener(ViewImage.this) {
            @Override
            public void onSwipeUp() {
                Toast.makeText(ViewImage.this, "top", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeRight() {
                if (cur_img > 0) {
                    cur_img--;
                    showImage();
                }
            }
            @Override
            public void onSwipeLeft() {
                if (cur_img < listImage.size()-1) {
                    cur_img++;
                    showImage();
                }
            }
            @Override
            public void onSwipeDown() {
                Toast.makeText(ViewImage.this, "bottom", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick() {
                if (header.isShown()) {
                    header.setVisibility(LinearLayout.GONE);
                    footer.setVisibility(LinearLayout.GONE);
                }
                else {
                    header.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void showImage(){
        Glide.with(this)
                .load("file://" + listImage.get(cur_img).getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                //.placeholder(R.drawable.gray_bg) // placeholder filled the whole imageview size
                .error(R.drawable.gray_bg)
                .into(img);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.view_option_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sync:
                Toast.makeText(this, "Sync clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }


}