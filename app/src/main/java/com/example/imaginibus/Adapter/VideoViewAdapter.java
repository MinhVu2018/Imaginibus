package com.example.imaginibus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;

import java.util.ArrayList;

public class VideoViewAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<VideoModel> modelArrayList;

    // constructor
    public VideoViewAdapter(Context context, ArrayList<VideoModel> modelArrayList){
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // inflate layout item_video.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, container, false);

        // init id views
        VideoView vid = view.findViewById(R.id.view_video);

        // get data
        VideoModel model = modelArrayList.get(position);

        // set data
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(vid);
        vid.setMediaController(mediaController);
        vid.setVideoPath(model.getPath());
        vid.start();

        // add view to container
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
