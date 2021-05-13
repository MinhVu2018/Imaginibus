package com.example.imaginibus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imaginibus.Dialog.BrushDialog;
import com.example.imaginibus.Dialog.EmojiDialog;
import com.example.imaginibus.Dialog.StickerDialog;
import com.example.imaginibus.Dialog.TextDialog;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ja.burhanrashid52.photoeditor.CustomEffect;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditImage extends AppCompatActivity
                    implements BrushDialog.BrushDialogListener, TextDialog.TextDialogListener, EmojiDialog.EmojiDialogListener, StickerDialog.StickerDialogListener {
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    ImageModel cur_img;
    ImageButton btn_crop, btn_rotate, btn_undo, btn_redo, btn_brush, btn_text, btn_erase, btn_filter, btn_sticker, btn_emoji, btn_cancel, btn_save;
    TextView cur_editor;
    static int size = 0, opacity = 100, brush_color = Color.BLACK, text_color = Color.BLACK;
    static String text;
    View cur_view;
    static int cur_angle = 0;

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
        btn_crop = findViewById(R.id.btn_crop);
        btn_rotate = findViewById(R.id.btn_rotate);
        btn_undo = findViewById(R.id.btn_undo);
        btn_redo = findViewById(R.id.btn_redo);
        btn_brush = findViewById(R.id.btn_brush);
        btn_text = findViewById(R.id.btn_text);
        btn_erase = findViewById(R.id.btn_erase);
        btn_filter = findViewById(R.id.btn_filter);
        btn_sticker = findViewById(R.id.btn_sticker);
        btn_emoji = findViewById(R.id.btn_emoji);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        cur_editor = findViewById(R.id.cur_editor);

        cur_img = (ImageModel) getIntent().getSerializableExtra("IMG");

        setupEditor();
        setUpButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FullScreencall();
    }

    @Override
    protected void onResume(){
        super.onResume();
        FullScreencall();
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

    private void setUpButton() {
        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.fromFile(new File(cur_img.getImageUrl()));
                CropImage.activity(uri)
                        .start(EditImage.this);
                cur_editor.setText("Crop");
            }
        });
        btn_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Rotate");
                if (cur_angle == 360)
                    cur_angle = 0;
                CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_ROTATE)
                        .setParameter("angle", cur_angle += 90)
                        .build();
                photoEditor.setFilterEffect(customEffect);
            }
        });
        btn_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.undo();
            }
        });
        btn_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.redo();
            }
        });
        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Brush");
                BrushDialog dialog = new BrushDialog();

                Bundle args = new Bundle();
                args.putInt("size", size);
                args.putInt("opacity", opacity);
                args.putInt("color", brush_color);
                dialog.setArguments(args);

                dialog.show(getSupportFragmentManager(), "Drawing");
            }
        });
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Text");
                TextDialog dialog = new TextDialog();
                dialog.show(getSupportFragmentManager(), "Text");

                photoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
                    @Override
                    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
                        cur_view = rootView;

                        TextDialog dialog = new TextDialog();
                        Bundle args = new Bundle();
                        args.putString("text", text);
                        args.putInt("color", text_color);
                        dialog.setArguments(args);
                        dialog.show(getSupportFragmentManager(), "Text");
                    }

                    @Override
                    public void onAddViewListener(ViewType viewType, int i) {

                    }

                    @Override
                    public void onRemoveViewListener(ViewType viewType, int i) {

                    }

                    @Override
                    public void onStartViewChangeListener(ViewType viewType) {

                    }

                    @Override
                    public void onStopViewChangeListener(ViewType viewType) {

                    }
                });
            }
        });
        btn_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Eraser");
                photoEditor.brushEraser();
            }
        });
        btn_filter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cur_editor.setText("Filter");
//                photoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditImage.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                                                .inflate (R.layout.filter_dialog, findViewById(R.id.list_filter));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        FullScreencall();
                    }
                });
                setUpFilter(bottomSheetDialog, bottomSheetView);
            }
        });
        btn_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Sticker");
                StickerDialog dialog = new StickerDialog();

                dialog.show(getSupportFragmentManager(), "Sticker");
            }
        });
        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_editor.setText("Emoji");
                ArrayList<String> list_emoji = PhotoEditor.getEmojis(getApplicationContext());
                EmojiDialog dialog = new EmojiDialog(list_emoji);

                dialog.show(getSupportFragmentManager(), "Emoji");
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                FullScreencall();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditImage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Imaginibus");

                mediaStorageDir.mkdir();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/Imaginibus";
                String currentDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String fileName = currentDateTime + ".png";

                String imagePath = filePath + "/" + fileName;
                photoEditor.saveAsFile(imagePath, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        Toast.makeText(EditImage.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditImage.this, "Failed to save Image", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    final Uri contentUri = Uri.fromFile(new File(imagePath));
                    scanIntent.setData(contentUri);
                    sendBroadcast(scanIntent);
                } else {
                    final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                    sendBroadcast(intent);
                }
                FullScreencall();
            }
        });
    }

    private void FullScreencall() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                photoEditorView.getSource().setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // brush editor
    @Override
    public void ApplyOption(int size, int opacity, int color) {
        this.brush_color = color;
        this.size = size;
        this.opacity = opacity;

        FullScreencall();
        photoEditor.setBrushSize(size);
        photoEditor.setOpacity(opacity);
        photoEditor.setBrushColor(color);

        photoEditor.setBrushDrawingMode(true);
    }

    // text editor
    @Override
    public void NewText(String input_text, int color) {
        // args for dialog
        this.text = input_text;
        this.text_color = color;
        photoEditor.addText(text, text_color);
        FullScreencall();
    }

    @Override
    public void EditText(String input_text, int color) {
        // args for dialog
        this.text = input_text;
        this.text_color = color;
        photoEditor.editText(cur_view, text, text_color);
        FullScreencall();
    }

    // emoji editor
    @Override
    public void ApplyEmoji(String text) {
        photoEditor.addText(text, Color.BLACK);
        FullScreencall();
    }

    // sticker editor
    @Override
    public void ApplySticker(Bitmap bitmap){
        photoEditor.addImage(bitmap);
        FullScreencall();
    }

    // filter editor
    private void setUpFilter(BottomSheetDialog dialog, View view){
        ImageButton btn_none = view.findViewById(R.id.filter_none);
        btn_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.NONE);
            }
        });

        ImageButton btn_autofix = view.findViewById(R.id.filter_autofix);
        btn_autofix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.AUTO_FIX);
            }
        });
        ImageButton btn_brightness = view.findViewById(R.id.filter_brightness);
        btn_brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS);
            }
        });

        ImageButton btn_constrast = view.findViewById(R.id.filter_constrast);
        btn_constrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.CONTRAST);
            }
        });

        ImageButton btn_black_white = view.findViewById(R.id.filter_black_white);
        btn_black_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.BLACK_WHITE);
            }
        });

        ImageButton btn_cross_process = view.findViewById(R.id.filter_cross_process);
        btn_cross_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.CROSS_PROCESS);
            }
        });

        ImageButton btn_documentary = view.findViewById(R.id.filter_documentary);
        btn_documentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.DOCUMENTARY);
            }
        });

        ImageButton btn_due_tone = view.findViewById(R.id.filter_due_tone);
        btn_due_tone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.DUE_TONE);
            }
        });

        ImageButton btn_fish_eye = view.findViewById(R.id.filter_fish_eye);
        btn_fish_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.FISH_EYE);
            }
        });

        ImageButton btn_flip_horizontal = view.findViewById(R.id.filter_flip_horizontal);
        btn_flip_horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.FLIP_HORIZONTAL);
            }
        });

        ImageButton btn_flip_vertical = view.findViewById(R.id.filter_flip_vertical);
        btn_flip_vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.FLIP_VERTICAL);
            }
        });

        ImageButton btn_gray_scale = view.findViewById(R.id.filter_gray_scale);
        btn_gray_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE);
            }
        });

        ImageButton btn_negative = view.findViewById(R.id.filter_negative);
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.NEGATIVE);
            }
        });

        ImageButton btn_sepia = view.findViewById(R.id.filter_sepia);
        btn_sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.SEPIA);
            }
        });

        ImageButton btn_sharpen = view.findViewById(R.id.filter_sharpen);
        btn_sharpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.SHARPEN);
            }
        });

        ImageButton btn_vignette = view.findViewById(R.id.filter_vignette);
        btn_vignette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setFilterEffect(PhotoFilter.VIGNETTE);
            }
        });
    }
}