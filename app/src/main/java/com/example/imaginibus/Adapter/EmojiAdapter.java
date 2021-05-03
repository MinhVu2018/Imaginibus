package com.example.imaginibus.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imaginibus.Dialog.EmojiDialog;
import com.example.imaginibus.R;

import java.util.ArrayList;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiItemHolder> {
    private Context context;
    private ArrayList<String> list_emoji;
    private EmojiDialog dialog;
    public EmojiAdapter(@NonNull Context context, @NonNull ArrayList<String> list_emoji, EmojiDialog dialog){
        this.context = context;
        this.list_emoji = list_emoji;
        this.dialog = dialog;
    }

    public class EmojiItemHolder extends RecyclerView.ViewHolder{
        private TextView emoji;
        private EmojiDialog.EmojiDialogListener listener;

        private EmojiItemHolder(View view){
            super(view);
            emoji = view.findViewById(R.id.emoji);
            emoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    listener = (EmojiDialog.EmojiDialogListener) context;
                    listener.ApplyEmoji(emoji.getText().toString());
                }
            });
        }
    }

    @NonNull
    @Override
    public EmojiItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new EmojiItemHolder(item);
    }

    public void onBindViewHolder(@NonNull EmojiItemHolder emojiItemHolder, int position) {
        String emoji = list_emoji.get(position);
        emojiItemHolder.emoji.setText(emoji);
    }

    @Override
    public int getItemCount() {
        return list_emoji.size();
    }
}