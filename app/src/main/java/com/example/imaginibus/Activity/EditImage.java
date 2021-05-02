package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
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

public class EditImage extends AppCompatActivity implements BrushDialog.BrushDialogListener{
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    ImageModel cur_img;
    ImageButton btn_undo, btn_redo, btn_brush, btn_text, btn_erase, btn_sticker, btn_emoji, btn_cancel, btn_save;
    TextView cur_editor;
    static int size = 0, opacity = 100, color = Color.BLACK;
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
        photoEditor.setOpacity(0);
        photoEditor.setBrushSize(0);
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
                BrushDialog dialog = new BrushDialog();

                Bundle args = new Bundle();
                args.putInt("size", size);
                args.putInt("opacity", opacity);
                args.putInt("color", color);
                dialog.setArguments(args);

                dialog.show(getSupportFragmentManager(), "Drawing");
                
                cur_editor.setText("Brush");

                photoEditor.setBrushSize(size);
                photoEditor.setOpacity(opacity);
                photoEditor.setBrushColor(color);

                photoEditor.setBrushDrawingMode(true);
            }
        });
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Text");
                photoEditor.addText("ABC", Color.BLACK);
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
    @Override
    public void ApplyOption(int size, int opacity, int color) {
        this.color = color;
        this.size = size;
        this.opacity = opacity;

        FullScreencall();
        photoEditor.setBrushSize(size);
        photoEditor.setOpacity(opacity);
        photoEditor.setBrushColor(color);

        photoEditor.setBrushDrawingMode(true);
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}