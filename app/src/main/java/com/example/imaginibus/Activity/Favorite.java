package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Adapter.ImageAdapter;
import com.example.imaginibus.Adapter.VideoAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;
import com.example.imaginibus.Utils.MyApplication;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Favorite extends AppCompatActivity {
    ImageButton btn_back;
    RecyclerView list_image, list_video;
    LinearLayout img_album, vid_album;
    List<ImageModel> listImage;
    List<VideoModel> listVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_favorite);

        //find view
        btn_back = findViewById(R.id.btn_back);
        img_album = findViewById(R.id.image_album);
        vid_album = findViewById(R.id.video_album);
        list_image = findViewById(R.id.list_image);
        list_video = findViewById(R.id.list_video);

        //setup button
        setupButton();

        //get resource
        listImage = ((MyApplication) this.getApplication()).getListImageFavorite();
        listVideo = ((MyApplication) this.getApplication()).getListVideoFavorite();

        list_image.setLayoutManager(new GridLayoutManager(this, 3));
        list_video.setLayoutManager(new GridLayoutManager(this, 3));

        ImageAdapter imageAdapter = new ImageAdapter(this, "", listImage);
        VideoAdapter videoAdapter = new VideoAdapter(this, listVideo);

        list_image.setAdapter(imageAdapter);
        list_video.setAdapter(videoAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.FullScreenCall(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApplication.FullScreenCall(this);
    }

    private void setupButton() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        img_album.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Favorite.this, AlbumImage.class);
                intent.putExtra("Title", getResources().getText(R.string.by_date_title));
                intent.putExtra("List Image", (Serializable) listImage);
                startActivity(intent);
            }
        });

        vid_album.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Favorite.this, Video.class);
                intent.putExtra("LIST_VIDEO", (Serializable) listVideo);
                startActivity(intent);
            }
        });
    }
}