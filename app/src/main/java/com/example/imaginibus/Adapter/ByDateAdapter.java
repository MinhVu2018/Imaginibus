package com.example.imaginibus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imaginibus.Model.ByDateModel;
import com.example.imaginibus.ImageListDecoration;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import java.util.ArrayList;
import java.util.List;

public class ByDateAdapter extends RecyclerView.Adapter {
    List<ByDateModel> items= new ArrayList<>();
    Context context;

    public ByDateAdapter(@NonNull Context context, int resource, @NonNull List<ImageModel> objects) {
        this.context = context;
        sortImageByDate(objects);
    }

    public class ImageItemHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private RecyclerView imageListView;

        private ImageItemHolder(View view) {
             super(view);
             date = (TextView) view.findViewById(R.id.by_date);
             imageListView = (RecyclerView) view.findViewById(R.id.list_by_date);
             imageListView.addItemDecoration(new ImageListDecoration(10));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_by_date, parent, false);
        return new ImageItemHolder(item);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ByDateModel item = items.get(position);
        ImageItemHolder imageItemHolder = (ImageItemHolder) holder;

        ((ImageItemHolder) holder).date.setText(item.getImageDate());


        ImageAdapter imageAdapter = new ImageAdapter(context, R.id.list_by_date, item.getImageList());
        ((ImageItemHolder) holder).imageListView.setLayoutManager(new GridLayoutManager(context, 3));
        ((ImageItemHolder) holder).imageListView.setAdapter(imageAdapter);
    }

    public int getItemCount() {
        return items.size();
    }

    private void sortImageByDate(List<ImageModel> listImages) {
        List<ImageModel> temp = new ArrayList<>();

        String curDay = new String();

        for (ImageModel item : listImages) {
            if (!item.getImageDate().equals(curDay)) {
                ByDateModel tempByDateModel = new ByDateModel();
                tempByDateModel.setByDateModel(curDay, temp);
                items.add(tempByDateModel);
                curDay = item.getImageDate();
                temp = new ArrayList<>();
            }

            temp.add(item);
            Log.i("DAY", curDay);
        }
    }
}
