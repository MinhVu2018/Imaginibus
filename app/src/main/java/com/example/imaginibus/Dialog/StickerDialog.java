package com.example.imaginibus.Dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imaginibus.Adapter.StickerAdapter;
import com.example.imaginibus.R;

public class StickerDialog extends DialogFragment {
    StickerDialog.StickerDialogListener listenter;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sticker_dialog, container, false);
        mRecyclerView = v.findViewById(R.id.list_sticker);

        //set adapter
        StickerAdapter stickerAdapter = new StickerAdapter(getContext(), this);
        mRecyclerView.setAdapter(stickerAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

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
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        listenter = (StickerDialog.StickerDialogListener) context;
    }

    public interface StickerDialogListener{
        void ApplySticker(Bitmap bitmap);
    }
}
