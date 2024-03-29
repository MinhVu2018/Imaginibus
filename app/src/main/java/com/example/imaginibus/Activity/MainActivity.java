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
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.imaginibus.Adapter.ListAlbumAdapter;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.R;
import com.example.imaginibus.Service.FaceDetection;
import com.example.imaginibus.Service.FaceGrouping;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_option, btn_gallery, btn_video, btn_location, btn_favorite, btn_secure, btn_face;
    Locale myLocale;
    String currentLang;
    Boolean isSDPresent;
    RecyclerView listAlbum;
    Gson gson = new Gson();

    //Static number for request permission
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get all image and set to global variable
        externalReadImage();
        externalReadVideo();

        //load the saved
        loadPreferences();

        //will hide the title
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        // hide navigation bar
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

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

        //set list album
        listAlbum = (RecyclerView) findViewById(R.id.list_album);
        ListAlbumAdapter listAlbumAdapter = new ListAlbumAdapter(this, R.id.list_album, ((MyApplication) this.getApplication()).getListAlbum());
        listAlbum.setLayoutManager(new LinearLayoutManager(this));
        listAlbum.setAdapter(listAlbumAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.FullScreenCall(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListAlbumAdapter listAlbumAdapter = new ListAlbumAdapter(this, R.id.list_album, ((MyApplication) this.getApplication()).getListAlbum());
        listAlbum.setLayoutManager(new LinearLayoutManager(this));
        listAlbum.setAdapter(listAlbumAdapter);
        MyApplication.FullScreenCall(this);
    }

    private void SetUpButton(){
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
                intent.putExtra("LIST_VIDEO", (Serializable) ((MyApplication) MainActivity.this.getApplication()).getListVideo());
                startActivity(intent);
            }
        });

        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Location.class);
                intent.putExtra("LIST_VIDEO", (Serializable) ((MyApplication) MainActivity.this.getApplication()).getListVideo());
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
                SharedPreferences shp = getSharedPreferences(
                        "com.example.imaginibus.PREFERENCES", Context.MODE_PRIVATE);
                String secure_email = shp.getString("SECURE_EMAIL", null);

                Intent intent;
                if (secure_email == null)
                    intent = new Intent(MainActivity.this, SecureRegisterActivity.class);
                else
                    intent = new Intent(MainActivity.this, EnterPassword.class);
                startActivity(intent);
            }
        });
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
            case R.id.btn_sync:
                externalReadImage();
                refresh();
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
            case R.id.btn_face_group:
                if (((MyApplication) this.getApplication()).getListFace() != null) {
                    Intent intent = new Intent(MainActivity.this, FaceGroup.class);
                    startActivity(intent);
                } else {
                    if (!isMyServiceRunning(FaceDetection.class) && !isMyServiceRunning(FaceGrouping.class)) {
                        Intent faceService = new Intent(MainActivity.this, FaceDetection.class);
                        startService(faceService);
                    } else {
                        Toast.makeText(this, "Face grouping is working, please comeback later!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_camera:
                Intent intent_camera = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                startActivityForResult(intent_camera, 1001);
                break;
            case R.id.btn_change_pass:
                Intent intent_cp = new Intent(MainActivity.this, EnterPassword.class);
                intent_cp.putExtra("CHECK_PASS", true);
                startActivity(intent_cp);
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
        String fav_img_list = shp.getString(MyApplication.image_favorite_path, null);
        String fav_vid_list = shp.getString(MyApplication.video_favorite_path, null);
        String secure_list = shp.getString("SECURE_LIST", null);
        String face_list = shp.getString("FACE_LIST", null);
        int current_layout = shp.getInt("LAYOUT", 0);

        //load favorite image list
        if (fav_img_list != null) {
            Type type = new TypeToken<List<ImageModel>>(){}.getType();
            ((MyApplication) this.getApplication()).setListImageFavorite(gson.fromJson(fav_img_list, type));
        } else {
            ((MyApplication) this.getApplication()).setListImageFavorite(new ArrayList<>());
        }

        // load favorite video list
        if (fav_vid_list != null){
            Type type = new TypeToken<List<VideoModel>>(){}.getType();
            ((MyApplication) this.getApplication()).setListVideoFavorite(gson.fromJson(fav_vid_list, type));
        } else
            ((MyApplication) this.getApplication()).setListVideoFavorite(new ArrayList<>());
        
            //load secure list
        if (secure_list != null) {
            Type type = new TypeToken<List<ImageModel>>(){}.getType();
            ((MyApplication) this.getApplication()).setListSecure(gson.fromJson(secure_list, type));
        } else {
            ((MyApplication) this.getApplication()).setListSecure(new ArrayList<>());
        }

        //is face list exist
        if (face_list != null) {
            Type type = new TypeToken<List<AlbumModel>>(){}.getType();
            ((MyApplication) this.getApplication()).setListFace(gson.fromJson(face_list, type));
        }

        //load theme
        Configuration config = new Configuration();
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            config.uiMode = Configuration.UI_MODE_NIGHT_YES;
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            config.uiMode = Configuration.UI_MODE_NIGHT_NO;
        }

        //load language
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        //load layout
        ((MyApplication) this.getApplication()).currentLayout = current_layout;
    }

    private void externalReadImage() {
        //create list
        List<ImageModel> imageList = new ArrayList<>();
        List<AlbumModel> albumList = new ArrayList<>();

        final String[] columns = {MediaStore.Audio.Media.DATA,
                                MediaStore.Audio.Media.DATE_ADDED,
                                MediaStore.Audio.Media.SIZE,
                                MediaStore.Audio.Media.WIDTH,
                                MediaStore.Audio.Media.HEIGHT,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Audio.Media.DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

        //Total number of images
        int count = cursor.getCount();

        for (int i = 0; i < count; i++) {
            String[] data = new String[11];
            cursor.moveToPosition(i);

            for (int j=0; j<columns.length; j++)
                data[j] = cursor.getString(j);

            ImageModel imageModel = new ImageModel(data);

            //get image lat and long
            ExifInterface exif = null;
            double latitude = 0, longitude = 0;
            try {
                exif = new ExifInterface(imageModel.getImageUrl());
                String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                // your Final lat Long Values
                if((LATITUDE !=null) && (LATITUDE_REF !=null) && (LONGITUDE != null) && (LONGITUDE_REF !=null)) {
                    if(LATITUDE_REF.equals("N"))
                        latitude = convertToDegree(LATITUDE);
                    else
                        latitude = 0 - convertToDegree(LATITUDE);

                    if(LONGITUDE_REF.equals("E"))
                        longitude = convertToDegree(LONGITUDE);
                    else
                        longitude = 0 - convertToDegree(LONGITUDE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageModel.setImageLocation(latitude, longitude);
            //add that image to list image
            imageList.add(0, imageModel);
            //add image to album
            addImageToAlbum(albumList, imageModel, imageModel.getAlbum());
        }

        //set to application variable
        ((MyApplication) this.getApplication()).setListImage(imageList);
        ((MyApplication) this.getApplication()).setListAlbum(albumList);
        // The cursor should be freed up after use with close()
        cursor.close();
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

    private double convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void externalReadVideo() {
        //create list
        List<VideoModel> listVideo = new ArrayList<>();

        final String[] columns = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.WIDTH,
                MediaStore.Audio.Media.HEIGHT,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME };

        final String orderBy = MediaStore.Video.Media.DATE_ADDED;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);

            String[] data = new String[8];
            for (int j=0; j<columns.length; j++)
                data[j] = cursor.getString(j);

            //Store the path of the image
            VideoModel videoModel = new VideoModel(data);

            //add that image to list image
            listVideo.add(0, videoModel);
        }

        // The cursor should be freed up after use with close()
        cursor.close();

        //set to application variable
        ((MyApplication) this.getApplication()).setListVideo(listVideo);
    }
}