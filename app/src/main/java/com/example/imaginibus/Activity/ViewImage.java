package com.example.imaginibus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.imaginibus.Adapter.ImgAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;
import com.google.gson.Gson;

import org.xml.sax.helpers.AttributeListImpl;

import java.util.ArrayList;
import java.util.List;

public class ViewImage extends AppCompatActivity {
    private ViewPager viewPager;
    ArrayList<ImageModel> listImage;
    ImgAdapter imageAdapter;
    int cur_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_view_image);

        viewPager = findViewById(R.id.view_pager);
        listImage = (ArrayList<ImageModel>) getIntent().getSerializableExtra("list_img");

        String img_path = getIntent().getStringExtra("img_path");
        for (cur_img = 0; cur_img<listImage.size(); cur_img++) {
            if (("file://" + listImage.get(cur_img).getImageUrl()).equals(img_path))
                break;
        }

        //setup image adapter
        setUp();

        //setup button
        setupButton();
    }

    public void setUp(){
        // setup adapter
        imageAdapter = new ImgAdapter(this, listImage);

        // set adapter to view pager
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(cur_img);

        // set viewpager change listener
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                cur_img = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setupButton() {

        ImageButton btn_favorite = findViewById(R.id.btn_like);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageModel image = listImage.get(cur_img);
                if (((MyApplication) ViewImage.this.getApplicationContext()).isImageInFavorite(image)) {
                    Toast.makeText(ViewImage.this, "Remove image from favorite!", Toast.LENGTH_SHORT).show();
                    ((MyApplication) ViewImage.this.getApplicationContext()).removeImageFromFavorite(image);
                } else {
                    Toast.makeText(ViewImage.this.getApplicationContext(), "Add image to favorite!", Toast.LENGTH_SHORT).show();
                    ((MyApplication) ViewImage.this.getApplicationContext()).addImageToFavorite(image);
                }

                //save to my application and sharedreferences
                saveListFavorite(((MyApplication) ViewImage.this.getApplicationContext()).getListFavorite());
            }
        });
    }

    private void saveListFavorite(List<ImageModel> items) {
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("FAVORITE_LIST", gson.toJson(items));
        editor.commit();
    }

//    public void showPopup(View v) {
//        PopupMenu popup = new PopupMenu(this, v);
//        popup.setOnMenuItemClickListener(this);
//        popup.inflate(R.menu.view_option_menu);
//        popup.show();
//    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.btn_sync:
//                Toast.makeText(this, "Sync clicked", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return false;
//    }
}