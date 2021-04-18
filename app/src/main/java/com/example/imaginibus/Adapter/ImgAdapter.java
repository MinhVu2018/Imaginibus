package com.example.imaginibus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;
import com.example.imaginibus.View.TouchImageView;

import java.util.ArrayList;

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

        // show/hide layout


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
