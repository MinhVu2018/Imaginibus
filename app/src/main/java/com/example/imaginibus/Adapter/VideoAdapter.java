package com.example.imaginibus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Activity.ViewImage;
import com.example.imaginibus.Activity.ViewVideo;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter {
    private List<VideoModel> items;
    private Context context;

    public VideoAdapter(Context context, int id, List<VideoModel> items) {
        this.items = items;
        this.context = context;
    }

    public class VideoItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private ImageView overlayView;

        private VideoItemHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.thumbnail);
            overlayView = (ImageView) view.findViewById(R.id.overlay_thumbnail);

            overlayView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ViewVideo.class);
            intent.putExtra("video_path", imageView.getTag().toString());
            intent.putExtra("list_vid", (Serializable)items);
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_item, parent, false);
        return new VideoItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoItemHolder videoItemHolder = (VideoItemHolder) holder;

        VideoModel item = items.get(position);
        String path = item.getPath();

        Glide.with(context)
                .asBitmap()
                .load(Uri.fromFile(new File(path)))
                .placeholder(R.drawable.gray_bg)
                .centerCrop()
                .into(videoItemHolder.imageView);

        videoItemHolder.imageView.setTag(path);
    }

    @Override
    public int getItemCount() { return items.size(); }
}
