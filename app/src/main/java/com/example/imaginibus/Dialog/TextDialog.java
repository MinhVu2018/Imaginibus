package com.example.imaginibus.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.imaginibus.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextDialog extends AppCompatDialogFragment {
    private TextDialogListener listener;
    private int text_color = Color.BLACK;
    private boolean edit_flag = false;
    private EditText text_input;
    private CircleImageView black_img, white_img, red_img, yellow_img, green_img, blue_img, colorpicker_img;
    private List<CircleImageView> list_img;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.text_dialog, null);

        text_input = view.findViewById(R.id.text);

        black_img = view.findViewById(R.id.black_text);
        white_img = view.findViewById(R.id.white_text);
        red_img = view.findViewById(R.id.red_text);
        yellow_img = view.findViewById(R.id.yellow_text);
        green_img = view.findViewById(R.id.green_text);
        blue_img = view.findViewById(R.id.blue_text);
        colorpicker_img = view.findViewById(R.id.color_picker_text);
        list_img = Arrays.asList(black_img, white_img, red_img, yellow_img, green_img, blue_img, colorpicker_img);

        builder.setView(view)
                .setTitle("Text")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (edit_flag)
                            listener.EditText(text_input.getText().toString(), text_color);
                        else if (!text_input.getText().toString().equals(""))
                            listener.NewText(text_input.getText().toString(), text_color);
                    }
                });

        Bundle mArgs = getArguments();
        if (mArgs != null) {
            text_input.setText(mArgs.getString("text"));
            text_color = mArgs.getInt("color");
            edit_flag = true;
        }
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
        listener = (TextDialog.TextDialogListener) context;
    }

    public interface TextDialogListener{
        void NewText(String input_text, int color);
        void EditText(String input_text, int color);
    }

    private void setUpImage(View view){
        view.invalidate();

        switch (text_color){
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
                text_color = Color.BLACK;
                black_img.setBorderColor(Color.GRAY);
                updateColorImg(list_img, 0);
            }
        });

        white_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_color = Color.WHITE;
                updateColorImg(list_img, 1);
            }
        });

        red_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_color = Color.RED;
                updateColorImg(list_img, 2);
            }
        });

        yellow_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_color = Color.YELLOW;
                updateColorImg(list_img, 3);
            }
        });

        green_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_color = Color.GREEN;
                updateColorImg(list_img, 4);
            }
        });

        blue_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_color = Color.BLUE;
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
                        text_color = selectedColor;
                    }
                })
                .build()
                .show();
    }
}
