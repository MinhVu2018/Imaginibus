package com.example.imaginibus.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imaginibus.Adapter.ImageViewAdapter;
import com.example.imaginibus.Adapter.ZoomOutTransformation;
import com.example.imaginibus.BuildConfig;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import su.rbv.folderpicker.FolderPicker;

import static android.widget.Toast.LENGTH_SHORT;

public class ViewImage extends AppCompatActivity {
    private ViewPager viewPager;
    ArrayList<ImageModel> listImage;
    ImageViewAdapter imageAdapter;
    int cur_img_position;
    ImageModel cur_img;
    private TextView text_slider;
    private Timer timer;
    ImageButton btn_favorite;
    private static final int PICKFILE_REQUEST_CODE = 100;
    String title;

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

        text_slider = findViewById(R.id.text_slide);
        btn_favorite = findViewById(R.id.btn_like);
        viewPager = findViewById(R.id.view_pager);

        if (!handleIntentFilter()) {
            listImage = (ArrayList<ImageModel>) getIntent().getSerializableExtra("list_img");

            String img_path = getIntent().getStringExtra("img_path");
            for (cur_img_position = 0; cur_img_position < listImage.size(); cur_img_position++) {
                if (("file://" + listImage.get(cur_img_position).getImageUrl()).equals(img_path))
                    break;
            }
            cur_img = listImage.get(cur_img_position);
        }
        //setup image adapter
        setUp();

        //setup button
        setupButton();
    }

    private boolean handleIntentFilter(){
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && intent.getType().indexOf("image/") != -1) {
            cur_img = createImageModel(data);
            listImage = new ArrayList<>();
            listImage.add(cur_img);
            return true;
        }

        return false;
    }

    public ImageModel createImageModel(Uri contentUri) {
        final String[] columns = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.WIDTH,
                MediaStore.Audio.Media.HEIGHT,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Audio.Media.DISPLAY_NAME};

        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(contentUri, columns, null, null, null);

        String[] data = new String[11];
        cursor.moveToFirst();

        for (int j=0; j<columns.length; j++)
            data[j] = cursor.getString(j);

        return new ImageModel(data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.FullScreenCall(this);

        handleIntentFilter();
        //setup image adapter
        setUp();

        //setup button
        setupButton();
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApplication.FullScreenCall(this);
    }

    private void createAddTagDialog() {
        AlertDialog.Builder addDialogBuilder;
        AlertDialog addDialog;

        addDialogBuilder = new AlertDialog.Builder(this);
        final View addTagView = getLayoutInflater().inflate(R.layout.add_tag_dialog, null);

        addDialogBuilder.setView(addTagView);
        addDialog = addDialogBuilder.create();
        addDialog.show();

        EditText tag_name = (EditText) addTagView.findViewById(R.id.tag_name);
        Button save = (Button) addTagView.findViewById(R.id.btn_save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag_name_string = tag_name.getText().toString();
                ((MyApplication) ViewImage.this.getApplication()).addTag(cur_img, tag_name_string);
                Toast.makeText(ViewImage.this, "Tag added", LENGTH_SHORT).show();
                addDialog.cancel();
            }
        });
    }

    public void setUp(){
        //setup button like
        //setup like button when page change
        boolean liked = ((MyApplication) ViewImage.this.getApplicationContext()).isImageInFavorite(cur_img);
        if (liked)
            btn_favorite.setImageResource(R.drawable.icon_liked_white);
        else
            btn_favorite.setImageResource(R.drawable.icon_like_white);

        // setup adapter
        imageAdapter = new ImageViewAdapter(this, listImage);

        // set adapter to view pager
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(cur_img_position);

        //setup title
        title = getIntent().getStringExtra("Title");

        viewPager.setPageTransformer(true, new ZoomOutTransformation());
        // set viewpager change listener
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                cur_img_position = position;
                cur_img = listImage.get(cur_img_position);

                //setup like button when page change
                boolean liked = ((MyApplication) ViewImage.this.getApplicationContext()).isImageInFavorite(cur_img);
                if (liked)
                    btn_favorite.setImageResource(R.drawable.icon_liked_white);
                else
                    btn_favorite.setImageResource(R.drawable.icon_like_white);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setupButton() {
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if this image is in secure, stop the action
                if (((MyApplication) ViewImage.this.getApplication()).isImageInSecure(cur_img)) {
                    Toast.makeText(ViewImage.this, getResources().getText(R.string.fav_secure_conflict), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (((MyApplication) ViewImage.this.getApplicationContext()).isImageInFavorite(cur_img)) {
                    btn_favorite.setImageResource(R.drawable.icon_like_white);
                    ((MyApplication) ViewImage.this.getApplication()).removeImageFromFavorite(cur_img);
                    Toast.makeText(ViewImage.this, getResources().getText(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show();
                } else {
                    btn_favorite.setImageResource(R.drawable.icon_liked_white);
                    Toast.makeText(ViewImage.this, getResources().getText(R.string.add_to_favorite), Toast.LENGTH_SHORT).show();
                    ((MyApplication) ViewImage.this.getApplication()).addImageToFavorite(cur_img);
                }

                //save to my application and sharedreferences
                saveListFavorite(((MyApplication) ViewImage.this.getApplicationContext()).getListImageFavorite());
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

        ImageButton btn_slideshow = findViewById(R.id.btn_slideshow);
        btn_slideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = text_slider.getText().toString();

                if (text.equals("")) {
                    text_slider.setText(getResources().getText(R.string.slideshow_on));
                    slideshowImage();
                }
                else {
                    text_slider.setText("");
                    stopSlideshowImage();
                }
            }
        });

        ImageButton btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

        ImageButton btn_copy = findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ViewImage.this, FolderPicker.class);
                intent.putExtra("title", "Choose folder");
                intent.putExtra("pickFiles", false);
                intent.putExtra("showFiles", false);
                intent.putExtra("pictureFilesShowPreview", true);
                intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
                intent.putExtra("theme", R.style.SampleFolderPickerTheme);
                startActivityForResult(intent, 7777);
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
                cur_img = listImage.get(cur_img_position);

                //delete in secure
                if (((MyApplication) ViewImage.this.getApplication()).isImageInSecure(cur_img)) {
                    ((MyApplication) ViewImage.this.getApplication()).removeImageFromSecure(cur_img);
                    //save to my application and sharedreferences
                    saveListSecure(((MyApplication) ViewImage.this.getApplicationContext()).getListSecure());
                    return;
                }

                //delete in favorite
                if (((MyApplication) ViewImage.this.getApplication()).isImageInFavorite(cur_img)) {
                    ((MyApplication) ViewImage.this.getApplication()).removeImageFromFavorite(cur_img);
                    saveListFavorite(((MyApplication) ViewImage.this.getApplication()).getListImageFavorite());
                }

                //delete in external storage
                deleteImage(new File(listImage.get(cur_img_position).getImageUrl()));
                //delete in list image

                ((MyApplication) ViewImage.this.getApplication()).removeImage(cur_img);
                Toast.makeText(ViewImage.this, getResources().getText(R.string.delete), Toast.LENGTH_SHORT).show();

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
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.share_preference_path, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(MyApplication.image_favorite_path, gson.toJson(items));
        editor.commit();
    }

    private void saveListSecure(List<ImageModel> items) {
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("SECURE_LIST", gson.toJson(items));
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
            case R.id.btn_secure:
                addImageToSecure();
                return true;
            case R.id.btn_add_tag:
                createAddTagDialog();
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

    private void addImageToSecure() {
        cur_img = listImage.get(cur_img_position);
        if (((MyApplication) ViewImage.this.getApplicationContext()).isImageInSecure(cur_img)) {
            Toast.makeText(ViewImage.this, getResources().getText(R.string.image_is_in_secure), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ViewImage.this.getApplicationContext(), getResources().getText(R.string.add_image_to_secure) , Toast.LENGTH_SHORT).show();
            deleteImage(new File(cur_img.getImageUrl()));
            ((MyApplication) ViewImage.this.getApplicationContext()).addImageToSecure(cur_img);
        }

        //save to my application and sharedreferences
        saveListSecure(((MyApplication) ViewImage.this.getApplicationContext()).getListSecure());
    }

    // choose home / lock / both
    private void chooseScreen(){
        // create dialog to choose option
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage(getResources().getText(R.string.set_wallpaper_to) + " ")
                .setPositiveButton(getResources().getText(R.string.lock_screen), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id) {
                        setWallpaper("Lock");
                        finish();
                    }
                })
                .setNegativeButton(getResources().getText(R.string.home_screen), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id) {
                        setWallpaper("Home");
                        finish();
                    }
                })
                .setNeutralButton(getResources().getText(R.string.both_screen), new DialogInterface.OnClickListener(){
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int id){
                        setWallpaper("Both");
                        finish();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getResources().getText(R.string.set_wallpaper));
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
            alert.setTitle(getResources().getText(R.string.information));
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

            startActivity(Intent.createChooser(intent, getResources().getText(R.string.share_item)));
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

    private void copyImage(String folderDes) throws IOException {
//        Uri uri = FileProvider.getUriForFile(
//                this,
//                BuildConfig.APPLICATION_ID + ".provider",
//                new File(cur_img.getImageUrl()));
//        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newUri(getApplicationContext().getContentResolver(), "a Photo", uri);
//        mClipboard.setPrimaryClip(clip);
//        Toast.makeText(this,getResources().getText(R.string.copy_image),Toast.LENGTH_SHORT).show();

        String outputName = folderDes + "/" + "copy_of_"+ cur_img.getName();

        File sourceLocation= new File (cur_img.getImageUrl());
        File targetLocation= new File (outputName);

        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(targetLocation);
            scanIntent.setData(contentUri);
            sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        }

        Toast.makeText(this,getResources().getText(R.string.copy_image),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7777 && resultCode == RESULT_OK && data != null) {
            String folderName = data.getStringExtra("data");
            try {
                copyImage(folderName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void slideshowImage(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
    }

    private void stopSlideshowImage(){
        timer.cancel();
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run(){
            ViewImage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < listImage.size() - 1)
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }
}