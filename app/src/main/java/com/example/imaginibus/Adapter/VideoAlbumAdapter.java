package com.example.imaginibus.Adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imaginibus.Model.AlbumVideoModel;
import com.example.imaginibus.Model.ByDateModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;
import com.example.imaginibus.Utils.ImageListDecoration;

import java.util.ArrayList;
import java.util.List;

public class VideoAlbumAdapter extends RecyclerView.Adapter {
    List<AlbumVideoModel> items= new ArrayList<>();
    Context context;

    public VideoAlbumAdapter(@NonNull Context context, @NonNull List<AlbumVideoModel> listVideo) {
        this.context = context;
        this.items = listVideo;
    }

    public class VideoItemHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private RecyclerView videoListView;

        private VideoItemHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.album);
            videoListView = (RecyclerView) view.findViewById(R.id.list_by_album);
            videoListView.addItemDecoration(new ImageListDecoration(10));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_by_album, parent, false);
        return new VideoItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AlbumVideoModel item = items.get(position);
        VideoItemHolder videoItemHolder = (VideoItemHolder) holder;

        VideoAdapter imageAdapter = new VideoAdapter(context, item.getListVideo());

        videoItemHolder.date.setText(item.getAlbumName());
        videoItemHolder.videoListView.setLayoutManager(new GridLayoutManager(context, 3));
        videoItemHolder.videoListView.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
