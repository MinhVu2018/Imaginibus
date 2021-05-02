package com.example.imaginibus.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.imaginibus.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BrushDialog extends AppCompatDialogFragment {
    private BrushDialogListener listener;
    private SeekBar brush_size, brush_opacity;
    private int size = 0, opacity = 100;
    private int brush_color = Color.BLACK;
    private CircleImageView black_img, white_img, red_img, yellow_img, green_img, blue_img, colorpicker_img;
    private List<CircleImageView> list_img;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.brush_dialog, null);

        brush_size = view.findViewById(R.id.BrushSize);
        brush_opacity = view.findViewById(R.id.BrushOpacity);
        black_img = view.findViewById(R.id.black_brush);
        white_img = view.findViewById(R.id.white_brush);
        red_img = view.findViewById(R.id.red_brush);
        yellow_img = view.findViewById(R.id.yellow_brush);
        green_img = view.findViewById(R.id.green_brush);
        blue_img = view.findViewById(R.id.blue_brush);
        colorpicker_img = view.findViewById(R.id.color_picker_brush);

        list_img = Arrays.asList(black_img, white_img, red_img, yellow_img, green_img, blue_img, colorpicker_img);
        builder.setView(view)
                .setTitle("Edit")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.ApplyOption(brush_size.getProgress(), brush_opacity.getProgress(), brush_color);
                    }
                });

        Bundle mArgs = getArguments();
        brush_size.setProgress(mArgs.getInt("size"));
        brush_opacity.setProgress(mArgs.getInt("opacity"));
        brush_color = mArgs.getInt("color");

        setUpImage(view);

        return builder.create();
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
        listener = (BrushDialogListener) context;
    }

    public interface BrushDialogListener{
        void ApplyOption(int size, int opacity, int color);
    }

    private void setUpImage(View view){
        view.invalidate();

        switch (brush_color){
            case Color.BLACK:
                black_img.setBorderColor(Color.GRAY);
                updateColorImg(list_img, 0);
                break;
            case Color.WHITE:
                updateColorImg(list_img, 1);
                break;
            case Color.RED:
                updateColorImg(list_img, 2);
                break;
            case Color.YELLOW:
                updateColorImg(list_img, 3);
                break;
            case Color.GREEN:
                updateColorImg(list_img, 4);
                break;
            case Color.BLUE:
                updateColorImg(list_img, 5);
                break;
            default:
                updateColorImg(list_img, 6);
                break;
        }


        black_img.setBorderColor(Color.WHITE);
        black_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_color = Color.BLACK;
                black_img.setBorderColor(Color.GRAY);
                updateColorImg(list_img, 0);
            }
        });

        white_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_color = Color.WHITE;
                updateColorImg(list_img, 1);
            }
        });

        red_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_color = Color.RED;
                updateColorImg(list_img, 2);
            }
        });

        yellow_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_color = Color.YELLOW;
                updateColorImg(list_img, 3);
            }
        });

        green_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_color = Color.GREEN;
                updateColorImg(list_img, 4);
            }
        });

        blue_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_color = Color.BLUE;
                updateColorImg(list_img, 5);
            }
        });

        colorpicker_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerDialog();
                updateColorImg(list_img, 6);
            }
        });
    }

    private void updateColorImg(List<CircleImageView> list_img, int index){
        for (int i=0; i<list_img.size(); i++){
            if (i == index)
                list_img.get(i).setBorderWidth(5);
            else
                list_img.get(i).setBorderWidth(2);
        }
    }

    private void colorPickerDialog(){
        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        brush_color = selectedColor;
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
