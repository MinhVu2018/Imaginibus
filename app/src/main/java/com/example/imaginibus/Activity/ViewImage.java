package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewImage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton btn_back, btn_option;
    List<ImageModel> listImage;
    ImageView img;
    float x1,x2,y1,y2;
    int cur_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_view_image);

        //set the current image
        img = (ImageView) findViewById(R.id.viewed_image);
        String img_path = getIntent().getStringExtra("img_path");
        Picasso.get()
                .load(img_path)
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .into(img);

        //find the current image position
        listImage = (List<ImageModel>) getIntent().getSerializableExtra("list_img");
        for (cur_img = 0; cur_img<listImage.size(); cur_img++) {
            if (("file://" + listImage.get(cur_img).getImageUrl()).equals(img_path))
                return;
        }

        //setup buttons
        SetUpButton();
    }

    private void SetUpButton(){
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        btn_option = (ImageButton) findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        img = (ImageView) findViewById(R.id.viewed_image);
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2){
                    if (cur_img > 0)
                        cur_img--;
                }
                else if (x1 > x2){
                    if (cur_img < (listImage.size() - 1))
                        cur_img++;
                }

                Log.i("POSITION", String.valueOf(cur_img));
                Picasso.get()
                        .load("file://" + listImage.get(cur_img).getImageUrl())
                        .placeholder(R.drawable.gray_bg)
                        .error(R.drawable.gray_bg)
                        .into(img);
                break;
        }
        return false;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.view_option_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sync:
                Toast.makeText(this, "Sync clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }
}