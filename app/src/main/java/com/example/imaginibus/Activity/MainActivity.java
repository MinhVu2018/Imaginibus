package com.example.imaginibus.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.imaginibus.Adapter.ListAlbumAdapter;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_search, btn_option, btn_gallery, btn_video, btn_location, btn_favorite, btn_secure;
    Locale myLocale;
    String currentLang;
    Boolean isSDPresent;
    RecyclerView listAlbum;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //load the saved
        loadPreferences();
        //will hide the title
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        //load the content
        setContentView(R.layout.activity_main);
        SetUpButton();

        //require for reading external storage
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        //require for writing external storage
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        //check for sd card
        isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        //get all image and set to global variable
        externalReadImage();

        //set list album
        listAlbum = (RecyclerView) findViewById(R.id.list_album);
        ListAlbumAdapter listAlbumAdapter = new ListAlbumAdapter(this, R.id.list_album, ((MyApplication) this.getApplication()).getListAlbum());
        listAlbum.setLayoutManager(new LinearLayoutManager(this));
        listAlbum.setAdapter(listAlbumAdapter);

    }

    private void SetUpButton(){
        btn_search = (ImageButton) findViewById(R.id.btn_search);

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });

        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ByDate.class);
                startActivity(intent);
            }
        });

        btn_video = findViewById(R.id.btn_video);
        btn_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Video.class);
                startActivity(intent);
            }
        });

        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Location.class);
                startActivity(intent);
            }
        });

        btn_favorite = findViewById(R.id.btn_favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Favorite.class);
                startActivity(intent);
            }
        });

        btn_secure = findViewById(R.id.btn_secure);
        btn_secure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecureRegisterActivity.class);
                startActivity(intent);
            }
        });

//        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh(); // your code
//                pullToRefresh.setRefreshing(false);
//            }
//        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.main_option_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_language_english:
                setLocale("en-us");
                saveLocale("en-us");
                return true;
            case R.id.btn_language_viet:
                setLocale("vn");
                saveLocale("vn");
                return true;
            case R.id.btn_theme_dark:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveTheme(true);
                break;
            case R.id.btn_theme_light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveTheme(false);
                break;
            case R.id.btn_camera:
                Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                startActivityForResult(intent, 1001);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    public void refresh(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    public void setLocale(String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //finish current activity
        finish();
        //refresh activity
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.putExtra(currentLang, localeName);
        startActivity(refresh);
    }

    public void saveLocale(String language) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_LANGUAGE", language);
        editor.commit();
    }

    public void saveTheme(Boolean theme) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NIGHT_MODE", theme);
        editor.commit();
    }

    private void loadPreferences(){
        SharedPreferences shp = getSharedPreferences(
                "com.example.imaginibus.PREFERENCES", Context.MODE_PRIVATE);

        String language = shp.getString("USER_LANGUAGE","");
        Boolean theme = shp.getBoolean("NIGHT_MODE", false);

        Configuration config = new Configuration();

        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            config.uiMode = Configuration.UI_MODE_NIGHT_YES;
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            config.uiMode = Configuration.UI_MODE_NIGHT_NO;
        }

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void externalReadImage() {
        //create list
        List<ImageModel> imageList = new ArrayList<>();
        List<AlbumModel> albumList = new ArrayList<>();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();
        //Date format
        String format = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int dateColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            int albumColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            //calculate the date taken
            String dateTaken = cursor.getString(dateColumnIndex);
            String date = formatter.format(new Date(Long.parseLong(dateTaken) * 1000L));
            //get album name
            String album = cursor.getString(albumColumnIndex);
            //Store the path of the image
            ImageModel imageModel = new ImageModel();
            imageModel.setImage(cursor.getString(dataColumnIndex), date, album);

            //add that image to list image
            imageList.add(0, imageModel);
            //add image to album
            addImageToAlbum(albumList, imageModel, album);
        }

        // The cursor should be freed up after use with close()
        cursor.close();

        //set to application variable
        ((MyApplication) this.getApplication()).setListImage(imageList);
        ((MyApplication) this.getApplication()).setListAlbum(albumList);
    }

    private void addImageToAlbum(List<AlbumModel> listAlbum, ImageModel image, String albumName) {
        int albumSize = listAlbum.size();
        for (int i = 0; i < albumSize; i++) {
            if (listAlbum.get(i).getAlbumName().equals(albumName)) {
                listAlbum.get(i).addImage(image);
                return;
            }
        }

        AlbumModel newAlbum = new AlbumModel(image, albumName);
        listAlbum.add(newAlbum);
        return;
    }
}