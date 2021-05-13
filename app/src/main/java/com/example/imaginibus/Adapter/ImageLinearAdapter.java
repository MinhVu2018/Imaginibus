package com.example.imaginibus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Activity.ViewImage;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import java.io.Serializable;
import java.util.List;

public class ImageLinearAdapter extends RecyclerView.Adapter<ImageLinearAdapter.ImageLinearHolder> {
    List<ImageModel> items;
    Context context;
    String title;

    public ImageLinearAdapter(@NonNull Context context, String title, @NonNull List<ImageModel> objects) {
        this.context = context;
        this.items = objects;
        this.title = title;
    }

    public class ImageLinearHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView album, path, date;

        private ImageLinearHolder(View view) {
            super(view);
            thumb = (ImageView) view.findViewById(R.id.image);
            album = (TextView) view.findViewById(R.id.album);
            path = (TextView) view.findViewById(R.id.path);
            date = (TextView) view.findViewById(R.id.date);

            thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewImage.class);
                    intent.putExtra("Title", title);
                    intent.putExtra("img_path", thumb.getTag().toString());
                    intent.putExtra("list_img", (Serializable) items);

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ImageLinearHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item_linear, parent, false);
        return new ImageLinearHolder(item);
    }

    public void onBindViewHolder(@NonNull ImageLinearHolder imageLinearHolder, int position) {
        ImageModel item = items.get(position);

        //file the path of image
        String url = "file://" + item.getImageUrl();

        Glide.with(context)
                .load(url)
//                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .centerCrop()
                .into(imageLinearHolder.thumb);

        //set text
        imageLinearHolder.album.setText(item.getName());
        imageLinearHolder.date.setText(item.getImageDate());
        imageLinearHolder.path.setText(item.getAlbum());

        //add the path value as tag
        imageLinearHolder.thumb.setTag(url);
    }

    public int getItemCount() {
        return items.size();
    }
}
