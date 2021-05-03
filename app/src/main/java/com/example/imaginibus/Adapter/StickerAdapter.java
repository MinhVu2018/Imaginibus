package com.example.imaginibus.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imaginibus.Dialog.StickerDialog;
import com.example.imaginibus.R;

import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeResource;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerItemHolder> {
    private Context context;
    private ArrayList<Bitmap> list_sticker;
    private StickerDialog dialog;

    public StickerAdapter(@NonNull Context context, StickerDialog dialog){
        this.context = context;
        this.dialog = dialog;
        initStickerList();
    }

    private void initStickerList(){
        list_sticker = new ArrayList<Bitmap>();
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_1));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_2));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_3));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_4));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_5));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_6));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_7));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_8));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_9));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_10));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_11));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_12));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_13));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_14));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_15));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_16));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_17));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_18));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_19));
        list_sticker.add(decodeResource(context.getResources(), R.drawable.sticker_20));
    }

    public class StickerItemHolder extends RecyclerView.ViewHolder{
        private ImageView sticker;
        private StickerDialog.StickerDialogListener listener;

        private StickerItemHolder(View view){
            super(view);

            sticker = view.findViewById(R.id.sticker);
            sticker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    listener = (StickerDialog.StickerDialogListener) context;
                    sticker.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) sticker.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    listener.ApplySticker(bitmap);
                }
            });
        }
    }

    @NonNull
    @Override
    public StickerAdapter.StickerItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new StickerAdapter.StickerItemHolder(item);
    }

    public void onBindViewHolder(@NonNull StickerAdapter.StickerItemHolder stickerItemHolder, int position) {
        Bitmap sticker = list_sticker.get(position);
        stickerItemHolder.sticker.setImageBitmap(sticker);
    }

    @Override
    public int getItemCount() {
        return list_sticker.size();
    }
}
