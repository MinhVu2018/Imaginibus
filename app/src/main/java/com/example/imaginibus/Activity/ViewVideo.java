package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
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
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;

import java.util.ArrayList;
import java.util.List;

public class ViewVideo extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    VideoView videoView;
    private ViewPager viewPager;
    ArrayList<VideoModel> listVideo;
    VideoViewAdapter videoAdapter;
    int cur_vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_view_video);

        viewPager = findViewById(R.id.view_pager);
        listVideo = (ArrayList<VideoModel>) getIntent().getSerializableExtra("list_vid");

        String video_path = getIntent().getStringExtra("video_path");

        // set up list Video
        for (cur_vid = 0; cur_vid<listVideo.size(); cur_vid++){
            if ((listVideo.get(cur_vid).getPath()).equals(video_path))
                break;
        }

        //setup buttons
        SetUpButton();
    }

    private void SetUpButton(){
        // setup adapter
        videoAdapter = new VideoViewAdapter(this, listVideo);

        // set adapter to view pager
        viewPager.setAdapter(videoAdapter);
        viewPager.setCurrentItem(cur_vid);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.view_option_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sync:
                Toast.makeText(this, "Sync clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }
}