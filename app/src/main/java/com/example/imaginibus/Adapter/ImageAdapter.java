package com.example.imaginibus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Activity.ViewImage;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import java.io.Serializable;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter {
    List<ImageModel> items;
    Context context;

    public ImageAdapter(@NonNull Context context, int resource, @NonNull List<ImageModel> objects) {
        this.context = context;
        this.items = objects;
    }

    public class ImageItemHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;

        private ImageItemHolder(View view) {
            super(view);
            thumb = (ImageView) view.findViewById(R.id.image);

            thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewImage.class);
                    intent.putExtra("img_path", thumb.getTag().toString());
                    intent.putExtra("list_img", (Serializable) items);

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
        return new ImageItemHolder(item);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageModel item = items.get(position);
        ImageItemHolder imageItemHolder = (ImageItemHolder) holder;

        //file the path of image
        String url = "file://" + item.getImageUrl();

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .centerCrop()
                .into(imageItemHolder.thumb);

        //add the path value as tag
        imageItemHolder.thumb.setTag(url);
    }

    public int getItemCount() {
        return items.size();
    }
}
