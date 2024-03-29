package com.example.imaginibus.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.imaginibus.Adapter.VideoViewAdapter;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;
import com.example.imaginibus.Utils.MyApplication;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import su.rbv.folderpicker.FolderPicker;

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

        if (!handleIntentFilter()) {
            listVideo = (ArrayList<VideoModel>) getIntent().getSerializableExtra("list_vid");
            String video_path = getIntent().getStringExtra("video_path");
            // set up list Video
            for (cur_vid_position = 0; cur_vid_position < listVideo.size(); cur_vid_position++) {
                if ((listVideo.get(cur_vid_position).getPath()).equals(video_path))
                    break;
            }

            cur_vid = listVideo.get(cur_vid_position);
        }
        // setup video adapter
        setUp();

        //setup buttons
        SetUpButton();
    }

    private boolean handleIntentFilter(){
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null && intent.getType().indexOf("video/") != -1) {
            cur_vid = createVideoModel(uri);
            listVideo = new ArrayList<>();
            listVideo.add(cur_vid);
            return true;
        }

        return false;
    }

    public VideoModel createVideoModel(Uri contentUri) {
        final String[] columns = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME};

        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(contentUri, columns, null, null, null);

        String[] data = new String[11];
        cursor.moveToFirst();

        for (int j=0; j<columns.length; j++)
            data[j] = cursor.getString(j);

        return new VideoModel(data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.FullScreenCall(this);

        handleIntentFilter();
        // setup video adapter
        setUp();

        //setup buttons
        SetUpButton();
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApplication.FullScreenCall(this);
    }

    private void setUp(){
        // setup adapter
        videoAdapter = new VideoViewAdapter(getSupportFragmentManager(), listVideo);

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
                cur_vid_position = position;
                cur_vid = listVideo.get(position);
                ImageButton btn_favorite = findViewById(R.id.btn_like);
                boolean liked = ((MyApplication) ViewVideo.this.getApplicationContext()).isVideoInFavorite(cur_vid);
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

    private void SetUpButton(){
        ImageButton btn_favorite = findViewById(R.id.btn_like);

        if (((MyApplication) ViewVideo.this.getApplicationContext()).isVideoInFavorite(cur_vid))
            btn_favorite.setImageResource(R.drawable.icon_liked_white);
        else
            btn_favorite.setImageResource(R.drawable.icon_like_white);

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_vid = listVideo.get(cur_vid_position);
                if (((MyApplication) ViewVideo.this.getApplicationContext()).isVideoInFavorite(cur_vid)) {
                    Toast.makeText(ViewVideo.this, getResources().getText(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show();
                    btn_favorite.setImageResource(R.drawable.icon_like_white);
                    ((MyApplication) ViewVideo.this.getApplicationContext()).removeVideoFromFavorite(cur_vid);
                } else {
                    Toast.makeText(ViewVideo.this.getApplicationContext(), getResources().getText(R.string.add_to_favorite), Toast.LENGTH_SHORT).show();
                    btn_favorite.setImageResource(R.drawable.icon_liked_white);
                    ((MyApplication) ViewVideo.this.getApplicationContext()).addVideoToFavorite(cur_vid);
                }

                //save to my application and sharedreferences
                saveListFavorite(((MyApplication) ViewVideo.this.getApplicationContext()).getListVideoFavorite());
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
                Intent intent = new Intent(ViewVideo.this, FolderPicker.class);
                intent.putExtra("title", "Choose folder");
                intent.putExtra("pickFiles", false);
                intent.putExtra("showFiles", false);
                intent.putExtra("pictureFilesShowPreview", true);
                intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
                intent.putExtra("theme", R.style.SampleFolderPickerTheme);
                startActivityForResult(intent, 7777);
            }
        });

        ImageButton btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideo(new File(cur_vid.getPath()));
                Toast.makeText(ViewVideo.this, getResources().getText(R.string.delete), Toast.LENGTH_LONG).show();
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
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.share_preference_path, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(MyApplication.video_favorite_path, gson.toJson(items));
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
                Toast.makeText(this, getResources().getText(R.string.wallpaper_restrict), Toast.LENGTH_SHORT);
                return true;
        }
        return false;
    }

    // exif wrong datetime
    private void showVideoDetail() {
        try{
            ExifInterface exif = new ExifInterface(cur_vid.getPath());
            String myAttribute="";
            myAttribute += "DateTime: " + cur_vid.getVideoDateTime() + "\n";
            myAttribute += "Size:" + cur_vid.getSize() + " | ";
            myAttribute += "Resolution: " + cur_vid.getWidth() + "x" + cur_vid.getHeight() + "\n";
            myAttribute += "Path: " + cur_vid.getPath() + "\n";
            myAttribute += "Title: " + cur_vid.getTitle() + "\n";
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
        //Toast.makeText(this, "Ai cho xem", Toast.LENGTH_SHORT).show();
    }

    private String getTagString(String tag, ExifInterface exif) {
        return(tag + ": " + exif.getAttribute(tag) + "\n");
    }

    private void deleteVideo(File file) {
        // Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Video.Media._ID};

        // Match on the file path
        String selection = MediaStore.Video.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store
        }
        c.close();
    }

    private void copyVideo(String folderDes) throws IOException {
        String outputName = folderDes + "/" + "copy_of_"+ cur_vid.getName();

        File sourceLocation= new File (cur_vid.getPath());
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
                copyVideo(folderName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}