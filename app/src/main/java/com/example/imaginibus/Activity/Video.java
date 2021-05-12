package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.imaginibus.Adapter.VideoAdapter;
import com.example.imaginibus.Adapter.VideoAlbumAdapter;
import com.example.imaginibus.Model.AlbumVideoModel;
import com.example.imaginibus.Model.ByDateModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Video extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    RecyclerView listVideoView;
    TextView numVideo;
    List<AlbumVideoModel> videoAlbumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_video);

        //allocate memory
        videoAlbumList = new ArrayList<>();

        //load video
        externalReadVideo();

        //separate video to album
        separateVideoToAlbum();

        //load id
        listVideoView = findViewById(R.id.list_video);
        VideoAlbumAdapter videoAdapter = new VideoAlbumAdapter(this, videoAlbumList);
        listVideoView.setLayoutManager(new LinearLayoutManager(this));
        listVideoView.setAdapter(videoAdapter);
        numVideo = findViewById(R.id.num_videos);
        numVideo.setText(String.valueOf(((MyApplication) this.getApplication()).getListVideo().size()) + " ");

        //setup button
        SetUpButton();
    }

    private void separateVideoToAlbum() {
        List<VideoModel> allVideo = ((MyApplication) this.getApplication()).getListVideo();

        for (VideoModel videoModel : allVideo) {
            boolean added = false;
            for (AlbumVideoModel albumVideoModel : videoAlbumList) {
                if (albumVideoModel.getAlbumName().equals(videoModel.getAlbum())) {
                    albumVideoModel.addVideo(videoModel);
                    added = true;
                    break;
                }
            }

            //if this video is added
            if (added)
                continue;

            //if no album found
            AlbumVideoModel albumVideoModel = new AlbumVideoModel(videoModel, videoModel.getAlbum());
            videoAlbumList.add(0, albumVideoModel);
        }
    }


    private void SetUpButton() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Video.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_option = findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.layout_option_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
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