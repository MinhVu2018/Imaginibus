package com.example.imaginibus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter {
    List<ImageModel> items = new ArrayList<>();
    Context context;

    public ImageAdapter(@NonNull Context context, int resource, @NonNull List<ImageModel> objects) {
        this.context = context;
        items = objects;
    }

    public class ImageItemHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;

        private ImageItemHolder(View view) {
             super(view);
             thumb = (ImageView) view.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image, parent, false);
        return new ImageItemHolder(item);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageModel item = items.get(position);
        ImageItemHolder imageItemHolder = (ImageItemHolder) holder;

        //get width and height of image
        String url = "file://" + item.getImageUrl();
        Log.d("FILE_STRING", url);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .centerCrop()
                .fit()
                .into(imageItemHolder.thumb);
    }

    public int getItemCount() {
        return items.size();
    }

}
