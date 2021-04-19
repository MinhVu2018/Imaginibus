package com.example.imaginibus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.imaginibus.Activity.ViewImage;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;
import com.example.imaginibus.View.TouchImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ImgAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ImageModel> modelArrayList;

    //constructor
    public ImgAdapter(Context ct, ArrayList<ImageModel> arr){
        this.context = ct;
        this.modelArrayList = arr;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // inflate layout item.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item, container, false);

        // init uid views from item.xml
        TouchImageView img = view.findViewById(R.id.view_image);
        LinearLayout header = view.findViewById(R.id.image_header);
        LinearLayout footer = view.findViewById(R.id.image_footer);


        // get data
        ImageModel model = modelArrayList.get(position);
        String url = model.getImageUrl();

        // set data
        Glide.with(context)
                .load("file://" + url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.gray_bg)
                .into(img);

        // add view to container
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
