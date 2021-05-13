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
    ImageButton btn_back;
    RecyclerView listVideoView;
    TextView numVideo;
    List<AlbumVideoModel> videoAlbumList;
    List<VideoModel> listVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_video);

        //allocate memory
        videoAlbumList = new ArrayList<>();
        listVideo = (List<VideoModel>) getIntent().getSerializableExtra("LIST_VIDEO");

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
        for (VideoModel videoModel : listVideo) {
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
                finish();
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
}