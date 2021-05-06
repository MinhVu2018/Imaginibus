package com.example.imaginibus.Activity;

import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.imaginibus.Adapter.ImageViewAdapter;
import com.example.imaginibus.BuildConfig;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        // hide navigation bar
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        viewPager = findViewById(R.id.view_pager);
        listImage = (ArrayList<ImageModel>) getIntent().getSerializableExtra("list_img");

        //set registerForContextMenu
        registerForContextMenu(viewPager);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.image_option_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.copy_image:
                copyImage();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
                shareImage();
//                Toast.makeText(ViewImage.this, "Share deeeeeeeee", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btn_copy = findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                copyImage();
            }
        });

        ImageButton btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewImage.this, EditImage.class);
                intent.putExtra("IMG", (Serializable)cur_img);
                startActivity(intent);
            }
        });

        //user need to sync to see the different!!, still need to consider this
        ImageButton btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete in external storage
                deleteImage(new File(listImage.get(cur_img_position).getImageUrl()));
                Toast.makeText(ViewImage.this, "Xóa gòi đó", Toast.LENGTH_SHORT).show();
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

    private void shareImage(){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(ViewImage.this.getContentResolver() , Uri.fromFile(new File(cur_img.getImageUrl())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"image that you wants to share");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            File imageFileToShare = new File(cur_img.getImageUrl());
            Uri imageUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                    BuildConfig.APPLICATION_ID + ".provider", imageFileToShare);

            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteImage(File file) {
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

    private void copyImage(){
        Uri uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(cur_img.getImageUrl()));
        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newUri(getApplicationContext().getContentResolver(), "a Photo", uri);
        mClipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Image copied to clipboard",Toast.LENGTH_SHORT).show();
    }
}