package com.example.imaginibus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Activity.AlbumImage;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import java.io.Serializable;
import java.util.List;

public class ListAlbumAdapter extends RecyclerView.Adapter {
    List<AlbumModel> items;
    Context context;

    public ListAlbumAdapter(@NonNull Context context, int resource, @NonNull List<AlbumModel> objects) {
        this.items = objects;
        this.context = context;
    }

    public class ListAlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView albumName;
        private ImageView demoImg1, demoImg2, demoImg3;
        private ImageButton more;

        public ListAlbumHolder(@NonNull View view) {
            super(view);
            albumName = view.findViewById(R.id.album);
            demoImg1 = view.findViewById(R.id.image_view_1);
            demoImg2 = view.findViewById(R.id.image_view_2);
            demoImg3 = view.findViewById(R.id.image_view_3);
            more = view.findViewById(R.id.btn_more_camera);

            demoImg1.setOnClickListener(this);
            demoImg2.setOnClickListener(this);
            demoImg3.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AlbumImage.class);

            for (int i=0; i<items.size(); i++) {
                if (items.get(i).getAlbumName().equals(albumName.getText().toString())) {
                    intent.putExtra("Title", items.get(i).getAlbumName());
                    intent.putExtra("List Image", (Serializable) items.get(i).getListImage());
                    context.startActivity(intent);
                    return;
                }
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_album, parent, false);
        return new ListAlbumHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AlbumModel item = items.get(position);
        ListAlbumHolder listAlbumHolder = (ListAlbumHolder) holder;

        //get list image of an album
        List<ImageModel> listImage = item.getListImage();

        listAlbumHolder.albumName.setText(item.getAlbumName());
        //set the first image (always correct)
        Glide.with(context)
                .load("file://" + listImage.get(0).getImageUrl())
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .centerCrop()
                .into(listAlbumHolder.demoImg1);

        //check for the second
        if (listImage.size() >= 2) {
            Glide.with(context)
                    .load("file://" + listImage.get(1).getImageUrl())
                    .placeholder(R.drawable.gray_bg)
                    .error(R.drawable.gray_bg)
                    .centerCrop()
                    .into(listAlbumHolder.demoImg2);
            listAlbumHolder.demoImg2.setVisibility(View.VISIBLE);
            if (listImage.size() >= 3) {
                Glide.with(context)
                        .load("file://" + listImage.get(2).getImageUrl())
                        .placeholder(R.drawable.gray_bg)
                        .error(R.drawable.gray_bg)
                        .centerCrop()
                        .into(listAlbumHolder.demoImg3);
                listAlbumHolder.demoImg3.setVisibility(View.VISIBLE);
            } else {
                listAlbumHolder.demoImg3.setVisibility(View.INVISIBLE);
            }
        } else {
            listAlbumHolder.demoImg2.setVisibility(View.INVISIBLE);
            listAlbumHolder.demoImg3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }
}
