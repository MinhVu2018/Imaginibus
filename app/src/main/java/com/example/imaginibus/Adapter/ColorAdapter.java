package com.example.imaginibus.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imaginibus.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    ArrayList<String> list_color;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;

        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.color_image);
        }
    }

    public ColorAdapter(Context context, ArrayList<String> colors){
        this.context = context;
        list_color = colors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setBackground(Drawable.createFromPath(list_color.get(position)));
        holder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context, "Color", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_color.size();
    }
}
