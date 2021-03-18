package com.example.imaginibus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_search, btn_option, btn_gallery, btn_video, btn_location, btn_favorite, btn_secure;
    Locale myLocale;
    String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //load the saved
        loadLanguage();
        //will hide the title
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        //load the content
        setContentView(R.layout.activity_main);
        SetUpButton();
    }

    private void SetUpButton(){
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PreviewImage dialog = new PreviewImage();
                dialog.show(getSupportFragmentManager(), "show preview image");
            }
        });

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

        btn_video = (ImageButton) findViewById(R.id.btn_video);
        btn_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Video.class);    // not yet
                startActivity(intent);
            }
        });

        btn_location = (ImageButton) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Location.class);
                startActivity(intent);
            }
        });

        btn_favorite = (ImageButton) findViewById(R.id.btn_favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Favorite.class);
                startActivity(intent);
            }
        });

        btn_secure = (ImageButton) findViewById(R.id.btn_secure);
        btn_secure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecureRegisterActivity.class);
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
        }
        return false;
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

    private void loadLanguage(){
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
}