package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditImage extends AppCompatActivity {
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    ImageModel img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_edit_image);


        //setup
        photoEditorView = findViewById(R.id.photoEditorView);
        img = (ImageModel) getIntent().getSerializableExtra("IMG_PATH");
        setupEditor();
    }

    private void setupEditor() {
        Glide.with(this)
                .load(img.getImageUrl())
                .error(R.drawable.gray_bg)
                .into(photoEditorView.getSource());

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_light);

        //loading font from assest
        Typeface mEmojiTypeFace = ResourcesCompat.getFont(this, R.font.emojione_android);

        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();

        photoEditor.setBrushDrawingMode(true);
    }
}