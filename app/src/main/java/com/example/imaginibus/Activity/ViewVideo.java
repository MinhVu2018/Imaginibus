package com.example.imaginibus.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Adapter.VideoViewAdapter;
import com.example.imaginibus.BuildConfig;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;
import com.example.imaginibus.Utils.MyApplication;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewVideo extends AppCompatActivity {
    private ViewPager viewPager;
    ArrayList<VideoModel> listVideo;
    VideoViewAdapter videoAdapter;
    int cur_vid_position;
    VideoModel cur_vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_view_video);

        // hide navigation bar
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        viewPager = findViewById(R.id.view_pager);
        listVideo = (ArrayList<VideoModel>) getIntent().getSerializableExtra("list_vid");

        String video_path = getIntent().getStringExtra("video_path");

        // set up list Video
        for (cur_vid_position = 0; cur_vid_position<listVideo.size(); cur_vid_position++){
            if ((listVideo.get(cur_vid_position).getPath()).equals(video_path))
                break;
        }

        cur_vid = listVideo.get(cur_vid_position);
        // setup video adapter
        setUp();

        //setup buttons
        SetUpButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FullScreencall();
    }

    @Override
    protected void onResume(){
        super.onResume();
        FullScreencall();
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void setUp(){
        // setup adapter
        videoAdapter = new VideoViewAdapter(this, listVideo);

        // set adapter to view pager
        viewPager.setAdapter(videoAdapter);
        viewPager.setCurrentItem(cur_vid_position);

        // set viewpager change listener
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void SetUpButton(){
        ImageButton btn_favourite = findViewById(R.id.btn_like);
        btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_vid = listVideo.get(cur_vid_position);
                if (((MyApplication) ViewVideo.this.getApplicationContext()).isVideoInFavorite(cur_vid)) {
                    Toast.makeText(ViewVideo.this, "Remove image from favorite!", Toast.LENGTH_SHORT).show();
//                    ((MyApplication) ViewVideo.this.getApplicationContext()).removeVideoFromFavorite(cur_vid);
                } else {
                    Toast.makeText(ViewVideo.this.getApplicationContext(), "Add image to favorite!", Toast.LENGTH_SHORT).show();
//                    ((MyApplication) ViewVideo.this.getApplicationContext()).addVideoToFavorite(cur_vid);
                }

                //save to my application and sharedreferences
//                saveListFavorite(((MyApplication) ViewVideo.this.getApplicationContext()).getListVideoFavorite());
            }
        });

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        ImageButton btn_option = findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        ImageButton btn_copy = findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyVideo();
            }
        });

        ImageButton btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteVideo(new File(cur_vid.getPath()));
                Toast.makeText(ViewVideo.this, "Xóa mất tiu", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.view_option_menu);
        popup.show();
    }

    private void saveListFavorite(List<VideoModel> items) {
        // SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("FAVORITE_VIDEO_LIST", gson.toJson(items));
        editor.commit();
    }

    public boolean onMenuItemClick(MenuItem item) {
        // update image
        cur_vid = listVideo.get(cur_vid_position);

        switch (item.getItemId()) {
            case R.id.btn_detail:
                showVideoDetail();
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
            InputStream ins = new URL("file://" + cur_vid.getPath()).openStream();
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
    private void showVideoDetail() {
//        try{
//            ExifInterface exif = new ExifInterface(cur_img.getImageUrl());
//            String myAttribute="";
//            myAttribute += "DateTime: " + cur_img.getImageDateTime() + "\n";
//            myAttribute += "Size:" + cur_img.getSize() + " | ";
//            myAttribute += "Resolution: " + cur_img.getWidth() + "x" + cur_img.getHeight() + "\n";
//            myAttribute += "Path: " + cur_img.getImageUrl() + "\n";
//            myAttribute += "Title: " + cur_img.getTitle() + "\n";
//            myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
//            myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
//            myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
//
//            // create dialog to choose option
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            //Setting message manually and performing action on button click
//            builder.setMessage(myAttribute);
//            //Creating dialog box
//            AlertDialog alert = builder.create();
//            //Setting the title manually
//            alert.setTitle("Image information");
//            alert.show();
//
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
        Toast.makeText(this, "Ai cho xem", Toast.LENGTH_SHORT).show();
    }

    private String getTagString(String tag, ExifInterface exif) {
        return(tag + ": " + exif.getAttribute(tag) + "\n");
    }

    private void deleteVideo(File file) {
        // Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Images.Media._ID};

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store
        }
        c.close();
    }

    private void copyVideo(){
        Uri uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(cur_vid.getPath()));
        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newUri(getApplicationContext().getContentResolver(), "a Photo", uri);
        mClipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Video copied to clipboard",Toast.LENGTH_SHORT).show();
    }
}