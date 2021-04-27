package com.example.imaginibus.Activity;

import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.imaginibus.Adapter.ImageViewAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewImage extends AppCompatActivity {
    private ViewPager viewPager;
    ArrayList<ImageModel> listImage;
    ImageViewAdapter imageAdapter;
    int cur_img_position;
    ImageModel cur_img;

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
        for (cur_img_position = 0; cur_img_position<listImage.size(); cur_img_position++) {
            if (("file://" + listImage.get(cur_img_position).getImageUrl()).equals(img_path))
                break;
        }
        cur_img = listImage.get(cur_img_position);
        //setup image adapter
        setUp();

        //setup button
        setupButton();
    }

    public void setUp(){
        // setup adapter
        imageAdapter = new ImageViewAdapter(this, listImage);

        // set adapter to view pager
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(cur_img_position);

        // set viewpager change listener
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                cur_img_position = position;
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
                cur_img = listImage.get(cur_img_position);
                if (((MyApplication) ViewImage.this.getApplicationContext()).isImageInFavorite(cur_img)) {
                    Toast.makeText(ViewImage.this, "Remove image from favorite!", Toast.LENGTH_SHORT).show();
                    ((MyApplication) ViewImage.this.getApplicationContext()).removeImageFromFavorite(cur_img);
                } else {
                    Toast.makeText(ViewImage.this.getApplicationContext(), "Add image to favorite!", Toast.LENGTH_SHORT).show();
                    ((MyApplication) ViewImage.this.getApplicationContext()).addImageToFavorite(cur_img);
                }

                //save to my application and sharedreferences
                saveListFavorite(((MyApplication) ViewImage.this.getApplicationContext()).getListFavorite());
            }
        });
        // set up buttons
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());


        ImageButton btn_option = findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        ImageButton btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewImage.this, "Share deeeeeeeee", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewImage.this, EditImage.class);
                intent.putExtra("IMG_PATH", (Serializable)listImage.get(cur_img_position));
                startActivity(intent);
            }
        });
        ImageButton btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewImage.this, "Xóa gòi đó", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.view_option_menu);
        popup.show();
    }
    
    private void saveListFavorite(List<ImageModel> items) {
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("FAVORITE_LIST", gson.toJson(items));
        editor.commit();
    }

    public boolean onMenuItemClick(MenuItem item) {
        // update image
        cur_img = listImage.get(cur_img_position);

        switch (item.getItemId()) {
            case R.id.btn_detail:
                showImageDetail();
                return true;
            case R.id.btn_background:
                chooseScreen();
                return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setWallpaper(String option){
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            InputStream ins = new URL("file://" + cur_img.getImageUrl()).openStream();
            if (option.equals("Home"))
                myWallpaperManager.setStream(ins, null, false, WallpaperManager.FLAG_SYSTEM);
            else if (option.equals("Lock"))
                myWallpaperManager.setStream(ins, null, false, WallpaperManager.FLAG_LOCK);
            else
                myWallpaperManager.setStream(ins, null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // choose home / lock / both
    private void chooseScreen(){
        // create dialog to choose option
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Set wallpaper to ")
                .setPositiveButton("Lock screen", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id) {
                        setWallpaper("Lock");
                        finish();
                    }
                })
                .setNegativeButton("Home screen", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id) {
                        setWallpaper("Home");
                        finish();
                    }
                })
                .setNeutralButton("Both", new DialogInterface.OnClickListener(){
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id){
                        setWallpaper("Both");
                        finish();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Set Wallpaper");
        alert.show();
    }

    // exif wrong datetime
    private void showImageDetail() {
        try{
            ExifInterface exif = new ExifInterface(cur_img.getImageUrl());
            String myAttribute="";
            myAttribute += "DateTime: " + cur_img.getImageDateTime() + "\n";
            myAttribute += "Size:" + cur_img.getSize() + " | ";
            myAttribute += "Resolution: " + cur_img.getWidth() + "x" + cur_img.getHeight() + "\n";
            myAttribute += "Path: " + cur_img.getImageUrl() + "\n";
            myAttribute += "Title: " + cur_img.getTitle() + "\n";
            myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
            myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
            myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);

            // create dialog to choose option
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Setting message manually and performing action on button click
            builder.setMessage(myAttribute);
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Image information");
            alert.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTagString(String tag, ExifInterface exif) {
        return(tag + ": " + exif.getAttribute(tag) + "\n");
    }

}