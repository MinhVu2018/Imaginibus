package com.example.imaginibus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter {
    List<ImageModel> items = new ArrayList<>();
    Context context;
    private final LruCache<String, Bitmap> cache;

    public ImageAdapter(@NonNull Context context, int resource, @NonNull List<ImageModel> objects) {
        this.context = context;
        items = objects;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory;

        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getRowBytes() / 1024;
            }
        };
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
        Bitmap myBitmap = getBitmapFromMemCache(item.getImageUrl());
        if (myBitmap == null) {
            myBitmap = BitmapFactory.decodeFile(item.getImageUrl());
            addBitmapToMemoryCache(item.getImageUrl(), myBitmap);
        }

        ImageItemHolder imageItemHolder = (ImageItemHolder) holder;
        imageItemHolder.thumb.setImageBitmap(myBitmap);
    }

    public int getItemCount() {
        return items.size();
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            cache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return cache.get(key);
    }

}
