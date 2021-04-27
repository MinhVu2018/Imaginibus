package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imaginibus.Dialog.BrushDialog;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditImage extends AppCompatActivity {
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    ImageModel cur_img;
    ImageButton btn_undo, btn_redo, btn_brush, btn_text, btn_erase, btn_sticker, btn_emoji, btn_cancel, btn_save;
    TextView cur_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_edit_image);

        // hide navigation bar
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //setup
        photoEditorView = findViewById(R.id.photoEditorView);
        btn_undo = findViewById(R.id.btn_undo);
        btn_redo = findViewById(R.id.btn_redo);
        btn_brush = findViewById(R.id.btn_brush);
        btn_text = findViewById(R.id.btn_text);
        btn_erase = findViewById(R.id.btn_erase);
        btn_sticker = findViewById(R.id.btn_sticker);
        btn_emoji = findViewById(R.id.btn_emoji);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        cur_editor = findViewById(R.id.cur_editor);

        cur_img = (ImageModel) getIntent().getSerializableExtra("IMG");

        setupEditor();
        setUpButton();
    }

    private void setupEditor() {
        photoEditorView.getSource().setImageURI(Uri.parse(cur_img.getImageUrl()));

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_light);

        //loading font from assest
        Typeface mEmojiTypeFace = ResourcesCompat.getFont(this, R.font.emojione_android);

        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();
    }

    private void setUpButton(){
        btn_undo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                photoEditor.undo();
            }
        });
        btn_redo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                photoEditor.redo();
            }
        });
        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Brush");
                photoEditor.setBrushDrawingMode(true);

//                BrushDialog dialog = new BrushDialog(getApplicationContext());
//                dialog.show();
            }
        });
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Text");
//                photoEditor.addText(inputText, colorCode);
            }
        });
        btn_erase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cur_editor.setText("Eraser");
                photoEditor.brushEraser();
            }
        });
        btn_sticker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cur_editor.setText("Sticker");
                Bitmap bitmap;
//                photoEditor.addImage(bitmap);
            }
        });
        btn_emoji.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cur_editor.setText("Emoji");
                Bitmap bitmap;
//                photoEditor.addImage(bitmap);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                photoEditor.saveAsFile(filePath, new PhotoEditor.OnSaveListener() {
//                    @Override
//                    public void onSuccess(@NonNull String imagePath) {
//                        Log.e("PhotoEditor","Image Saved Successfully");
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.e("PhotoEditor","Failed to save Image");
//                    }
//                });
                finish();
            }
        });
    }
}