package com.example.imaginibus.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.imaginibus.R;

public class BrushDialog extends Dialog {

    public BrushDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_editor_sheet);
    }
}
