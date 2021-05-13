package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;
import com.example.imaginibus.Utils.MyApplication;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Favorite extends AppCompatActivity {
    ImageButton btn_back;
    ImageView img_1, img_2, img_3, vid_1, vid_2, vid_3;
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
        img_1 = findViewById(R.id.image_view_1);
        img_2 = findViewById(R.id.image_view_2);
        img_3 = findViewById(R.id.image_view_3);
        vid_1 = findViewById(R.id.video_view_1);
        vid_2 = findViewById(R.id.video_view_2);
        vid_3 = findViewById(R.id.video_view_3);
        img_album = findViewById(R.id.image_album);
        vid_album = findViewById(R.id.video_album);

        //setup button
        setupButton();

        //get resource
        listImage = ((MyApplication) this.getApplication()).getListImageFavorite();
        listVideo = ((MyApplication) this.getApplication()).getListVideoFavorite();

        //show image to view
        showImageToView();
        showVideoToView();
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

    private void showImageToView() {
        //if no image
        if (listImage.size() == 0) {
            img_album.setVisibility(View.GONE);
            return;
        }

        //if have image
        img_album.setVisibility(View.VISIBLE);
        //set the first image (always correct)
        Glide.with(this)
                .load("file://" + listImage.get(0).getImageUrl())
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .centerCrop()
                .into(img_1);

        //check for the second
        if (listImage.size() >= 2) {
            Glide.with(this)
                    .load("file://" + listImage.get(1).getImageUrl())
                    .placeholder(R.drawable.gray_bg)
                    .error(R.drawable.gray_bg)
                    .centerCrop()
                    .into(img_2);
            img_2.setVisibility(View.VISIBLE);
            if (listImage.size() >= 3) {
                Glide.with(this)
                        .load("file://" + listImage.get(2).getImageUrl())
                        .placeholder(R.drawable.gray_bg)
                        .error(R.drawable.gray_bg)
                        .centerCrop()
                        .into(img_3);
                img_1.setVisibility(View.VISIBLE);
            } else {
                img_1.setVisibility(View.INVISIBLE);
            }
        } else {
            img_2.setVisibility(View.INVISIBLE);
            img_3.setVisibility(View.INVISIBLE);
        }
    }

    private void showVideoToView() {
        //if no video
        if (listVideo.size() == 0) {
            vid_album.setVisibility(View.GONE);
            return;
        }

        //if have image
        vid_album.setVisibility(View.VISIBLE);
        //set the first image (always correct)
        Glide.with(this)
                .asBitmap()
                .load(Uri.fromFile(new File(listVideo.get(0).getPath())))
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .centerCrop()
                .into(vid_1);

        //check for the second
        if (listVideo.size() >= 2) {
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.fromFile(new File(listVideo.get(1).getPath())))
                    .placeholder(R.drawable.gray_bg)
                    .error(R.drawable.gray_bg)
                    .centerCrop()
                    .into(vid_2);
            vid_2.setVisibility(View.VISIBLE);
            if (listVideo.size() >= 3) {
                Glide.with(this)
                        .asBitmap()
                        .load(Uri.fromFile(new File(listVideo.get(2).getPath())))
                        .placeholder(R.drawable.gray_bg)
                        .error(R.drawable.gray_bg)
                        .centerCrop()
                        .into(vid_3);
                vid_3.setVisibility(View.VISIBLE);
            } else {
                vid_3.setVisibility(View.INVISIBLE);
            }
        } else {
            vid_2.setVisibility(View.INVISIBLE);
            vid_3.setVisibility(View.INVISIBLE);
        }
    }
}