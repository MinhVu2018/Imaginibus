package com.example.imaginibus.Dialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.imaginibus.Adapter.EmojiAdapter;
import com.example.imaginibus.R;

import java.util.ArrayList;

public class EmojiDialog extends DialogFragment {
    private EmojiDialog.EmojiDialogListener listener;
    private RecyclerView mRecyclerView;
    ArrayList<String> listEmoji;
    public EmojiDialog(ArrayList<String> list_emoji){
        this.listEmoji = list_emoji;
    }

    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.emoji_dialog, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_emoji);
        //setadapter

        EmojiAdapter emojiAdapter = new EmojiAdapter(getContext(), listEmoji, this);
        mRecyclerView.setAdapter(emojiAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));

        //get your recycler view and populate it.
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (EmojiDialog.EmojiDialogListener) context;
    }

    public interface EmojiDialogListener{
        void ApplyEmoji(String text);
    }

}